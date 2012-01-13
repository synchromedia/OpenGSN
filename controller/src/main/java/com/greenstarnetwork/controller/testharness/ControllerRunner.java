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
package com.greenstarnetwork.controller.testharness;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.greenstarnetwork.controller.ControllerException;
import com.greenstarnetwork.controller.IControllerProvider;
import com.greenstarnetwork.controller.model.HostList;
import com.greenstarnetwork.controller.testharness.exceptions.BaseFacilityException;
import com.greenstarnetwork.controller.testharness.exceptions.FacilityRetrievalException;
import com.greenstarnetwork.controller.testharness.utils.FacilitySerializer;
import com.greenstarnetwork.controller.testharness.utils.TestHarnessLinkGenerator;

/**
 * @author hzhang
 *
 */

public class ControllerRunner {
	
	private static FacilityOpHourSpec opHrSpec;
	private static int interval;
	private static Timer timer;
	private static IControllerProvider controller;
	private static IFacilityLocator facilityLocator;
	private static List<IFacility> facilityList = null;
	private static long currentTime;
	
	private static boolean isVirtual;
	private static String status = "offline";

	public void setInterval(int interval) {
		ControllerRunner.interval = interval;
	}

	public static void restart(long startTime) throws FacilityRetrievalException {

		if(isVirtual)
			facilityList = ( (FileFacilityLocator)facilityLocator ).getFacilities(); //can specify a virtual time for playing-back
		else
			facilityList = facilityLocator.getFacilities();

		if(facilityList == null || facilityList.isEmpty())
			throw new FacilityRetrievalException("No facility is available!");

		currentTime = startTime;
		timer = new Timer();

		if(isVirtual)
			timer.scheduleAtFixedRate(new RemindTask(), Constants.SIMULATOR_INTERVAL * 1000, Constants.SIMULATOR_INTERVAL * 1000);
		else
			timer.scheduleAtFixedRate(new RemindTask(), interval * 1000, interval * 1000);
		
		status = "online";
		System.out.println("@" + getTimeString() + " Controller successfully restarted");
	}

	public static void stop() {
		timer.cancel();
		
		status = "offline";
		System.out.println("@" + getTimeString() + " Controller successfully stopped");
	}

	public static void execute() throws BaseFacilityException {

		if(isVirtual) //if simulation mode runs every Constants.SIMULATOR_INTERVAL but pretend it is an hour
			currentTime += 3600*1000;
		else
			currentTime = System.currentTimeMillis();

		HostList hostList = new HostList();

		for(IFacility f : facilityList) {
			f.refreshModel(currentTime);

			HostList aux = f.getHostList();
			hostList.mergeHostList(aux);	
		}

		controller.setHostList(hostList);
		controller.setLinkTable( TestHarnessLinkGenerator.getLinkTable(hostList) );
		
		try {// how about 2 hours and 5 hours as a start?
			controller.generatePlan();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(controller.getMigrationPlan() != null && controller.getMigrationPlan().getPlan() != null && controller.getMigrationPlan().getPlan().size() >=1)
			controller.executePlan();
		} catch (ControllerException e) {
			// TODO set LinkTable to execute the plan properly
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		FacilitySerializer.saveFacilities(facilityList, currentTime);
		
		//statistics about the controller algorithm
		double totalProduction = 0.0, totalConsumption = 0.0;

		for(IFacility f : facilityList) {

			if (f instanceof VirtualSolarFacility) {
				VirtualSolarFacility vsf = (VirtualSolarFacility) f;

				totalProduction += vsf.getProductionSinceStart();
				totalConsumption += vsf.getConsumptionSinceStart();
			}
		}
		
		System.out.println("@" + getTimeString() + " total production since start: " + totalProduction + " total Consumption since start: " + totalConsumption);
	}
	
	public ControllerRunner(IControllerProvider controller, IFacilityLocator facilityLocator, long startTime, int interval, FacilityOpHourSpec opHrSpec) throws FacilityRetrievalException {

		ControllerRunner.controller = controller;
		ControllerRunner.interval = interval;
		ControllerRunner.currentTime = startTime;
		ControllerRunner.opHrSpec = opHrSpec;

		ControllerRunner.facilityLocator = facilityLocator;
		facilityList = facilityLocator.getFacilities();

		if(facilityList == null || facilityList.isEmpty())
			throw new FacilityRetrievalException("No facility is available!");

		ControllerRunner.timer = new Timer();

		if(facilityLocator instanceof FileFacilityLocator) {
			isVirtual = true;
			timer.scheduleAtFixedRate(new RemindTask(), Constants.SIMULATOR_INTERVAL * 1000, Constants.SIMULATOR_INTERVAL * 1000);
		}

		else if (facilityLocator instanceof IaaSFacilityLocator) {
			isVirtual = false;
			timer.scheduleAtFixedRate(new RemindTask(), interval * 1000, interval * 1000);
		}

		status = "online";
		System.out.println("@" + getTimeString() + " Controller successfully started");
	}

	public ControllerRunner(IControllerProvider controller, IFacilityLocator facilityLocator, FacilityOpHourSpec opHrSpec) throws FacilityRetrievalException  {
		new ControllerRunner(controller, facilityLocator, System.currentTimeMillis(), 3600, opHrSpec);
	}

	public static String status() {
		return status;
	}
	
	static class RemindTask extends TimerTask {
		public void run() {
			try {
				execute();
			} catch (BaseFacilityException e) {
				System.err.println("Error in ControllerTimer execution :: " + e);
			}

		}
	}
	
	static String getTimeString() {
		Date date = new Date();
		date.setTime(currentTime);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		return format.format(date).toString();
	}

}
