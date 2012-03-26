/**
 * Copyright 2009-2011 École de technologie supérieure,
 * Communication Research Centre Canada,
 * Inocybe Technologies Inc. and 6837247 CANADA Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opengsn.controller.testharness;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import com.google.gson.annotations.Expose;
import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;
import com.opengsn.controller.testharness.exceptions.BadTimeException;
import com.opengsn.controller.testharness.exceptions.CannedProductionProcessingException;
import com.opengsn.controller.testharness.utils.CannedDayProduction;
import com.opengsn.controller.testharness.vmmodel.VMHarness;
import com.opengsn.services.cloudmanager.model.VM;

/**
 * @author hzhang
 *
 */
public class VirtualSolarFacility extends AbstractVirtualFacility {

	//theoretical battery's half capacity in amp hours;
	//should be 257; use 60 here for testing
	private static final double FULL_CAPACITY = 60.00;

	//by default four canned 24 hour period production series, from mid-night to mid-night
	private static final int NUM_OF_DAY_PRODUCTION_SERIES = 5;

	private enum SolarFacilityStatus {
		ONSOLAR, ONGRID, ERROR
	}

	//help jason serialization/deserialization
	@Expose
	private static String facilityType = VirtualSolarFacility.class.getName();

	//always ONSOLAR and fully charged when a VSF first starts
	@Expose
	private SolarFacilityStatus status = SolarFacilityStatus.ONSOLAR;

	//assuming a VirtualSolarFacility starts fully charged
	@Expose
	private double residualCapacity = FULL_CAPACITY;

	@Expose
	private int randomSerieId = 0;

	//average and peak current, for calculating two op-hours
	@Expose
	private double averageCurrentAmps;
	@Expose
	private double peakCurrentAmps;

	@Expose
	private double productionSinceStart = 0.0;
	@Expose
	private double consumptionSinceStart = 0.0;

	public VirtualSolarFacility() {
		super();
	}

	public VirtualSolarFacility(String facilityId, HostList hostList,
			double staticCurrent, double voltage, String timeZone, long startTime) {
		super(facilityId, hostList, staticCurrent, voltage, timeZone, startTime);

		this.randomSerieId = randomSelector(NUM_OF_DAY_PRODUCTION_SERIES);

	}

	public VirtualSolarFacility(VirtualSolarFacility vsf) {
		super(vsf.getFacilityId(), vsf.getHostList(), vsf.getStaticCurrent(), vsf.getVoltage(), vsf.getTimeZone(), vsf.getStartTime());

		this.randomSerieId = randomSelector(NUM_OF_DAY_PRODUCTION_SERIES);
	}

	public void refreshModel(long currentTime) throws BadTimeException, CannedProductionProcessingException {

		if(currentTime <= this.getCurrenTime())
			throw new BadTimeException(""); // TODO make proper time format

		double production = getProduction(this.getCurrenTime(), currentTime);
		productionSinceStart += production;

		double consumption = getConsumption(this.getCurrenTime(), currentTime);
		consumptionSinceStart += consumption;

		residualCapacity = residualCapacity + production - consumption;

		if(residualCapacity >= FULL_CAPACITY) {
			residualCapacity = FULL_CAPACITY;

			if(status.equals(SolarFacilityStatus.ONGRID)) {
				status = SolarFacilityStatus.ONSOLAR;
				System.out.println("Facility " + this.getFacilityId() + " has switched from Grid to Solar");
			}
		}

		if(residualCapacity <= 0) {
			residualCapacity = 0;

			if(status.equals(SolarFacilityStatus.ONSOLAR)) {
				status = SolarFacilityStatus.ONGRID;
				System.out.println("Facility " + this.getFacilityId() + " has switched from Solar to Grid");
			}
		}

		setCurrentTime(currentTime);

		if(status.equals(SolarFacilityStatus.ONGRID)) {
			remainingHours = 0.0;
			remainingMaxLoadHours = 0.0;
		}

		else {
			remainingHours = residualCapacity/averageCurrentAmps;
			remainingMaxLoadHours = residualCapacity/peakCurrentAmps;
		}
		
		//propagate the newest Facility remaining hours to all its hosts
		List<Host> hosts = this.getHostList().getHosts();
		
		for(Host host : hosts) {
			host.setLifeTime(this.getRemainingHours());
			host.setLifetimeUnderMaxLoad(this.getRemainingMaxLoadHours());
			host.setThreshold(1);
			host.setThresholdUnderMax(4);
		}
	}

	public double getProductionSinceStart() {
		return productionSinceStart;
	}

	public double getConsumptionSinceStart() {
		return consumptionSinceStart;
	}

	private double getConsumption(long start, long end) {

		// if ONGRID then consumes nothing green
		if(status.equals(SolarFacilityStatus.ONGRID))
			return 0.0;

		// calculate the total of VM-level amp hours 
		// and sum up that of all VMs per host, and all hosts per facility
		// and update the averageCurrentAmps and peakCurrentAmps

		averageCurrentAmps = this.getStaticCurrent();
		peakCurrentAmps = this.getStaticCurrent();
		double consumption = 0.0;

		List<Host> hosts = getHostList().getHosts();

		for(Host host : hosts) {
			List<VM> vms = host.getHostModel().getVMList();

			for(VM vm : vms) {
				if(vm instanceof VMHarness) {
					VMHarness vmharness = (VMHarness) vm;
					consumption += vmharness.getConsumption(start, end);
					averageCurrentAmps += vmharness.getAverage(start, end);
					peakCurrentAmps += vmharness.getPeak(start, end);
				}

			}
		}

		return consumption;
	}

	private double getProduction(long start, long end) throws CannedProductionProcessingException {

		// randomly choose among 4 typical day-charging time series
		// retrieve production based on time of day from start till end
		// if start and end spans 12:00 PM, it should be ok since no production
		// is available during that time anyway, however a new day-charging time
		// series needs to be randomly picked for the next day
		if(areSameDay(start, end)) {
			return CannedDayProduction.getProductionFromCsv(this.getTimeZone(), this.randomSerieId, start, end);
		}
		else {
			System.out.println("a new day has begin at facility " + this.getFacilityId());
			
			double productionSum = 0;

			long eod = getEndOfDay(start);
			productionSum += CannedDayProduction.getProductionFromCsv(this.getTimeZone(), this.randomSerieId, start, eod);

			int oldId = this.randomSerieId;
			this.randomSerieId = randomSelector(NUM_OF_DAY_PRODUCTION_SERIES);
			//System.out.println("Old randomId: " + oldId + " new randomId: " + this.randomSerieId);
			
			productionSum += CannedDayProduction.getProductionFromCsv(this.getTimeZone(), this.randomSerieId, eod, end);

			return productionSum;
		}

	}

	/*
	 * randomly select a day series between [1, total]
	 */
	private int randomSelector(int total) {

		Random rand = new Random();
		double dbl = rand.nextDouble() * total;

		int i;

		for(i=0; i<total; i++) {
			if(dbl >= i && dbl <= i+1 ) 
				break;
		}	

		return i + 1;		
	}

	/*
	 * decide if two dates are on the same day and 
	 * therefore belong to the same random day series
	 */
	private boolean areSameDay(long date1, long date2) {
		Calendar cal1 = new GregorianCalendar(TimeZone.getTimeZone(this.getTimeZone()));
		cal1.setTimeInMillis(date1);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);

		Calendar cal2 = new GregorianCalendar(TimeZone.getTimeZone(this.getTimeZone()));
		cal2.setTimeInMillis(date2);
		int day2= cal2.get(Calendar.DAY_OF_YEAR);

		if(day1 == day2)
			return true;
		else 
			return false;
	}

	/* 
	 * get the 12:00 midnight time of the day that 'date' ends upon
	 */
	private long getEndOfDay(long date) {
		Calendar cal1 = new GregorianCalendar(TimeZone.getTimeZone(this.getTimeZone()));
		cal1.setTimeInMillis(date);
		int year = cal1.get(Calendar.YEAR);
		int month = cal1.get(Calendar.MONTH);
		int day = cal1.get(Calendar.DAY_OF_YEAR);

		cal1.set(year, month, day, 23, 59, 59);
		return cal1.getTimeInMillis();
	}

}
