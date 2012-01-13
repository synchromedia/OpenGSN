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
package com.greenstarnetwork.resources.facilityResource.core;

import java.util.List;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.facilityModel.PowerDistribution;
import com.greenstarnetwork.models.facilityModel.PowerSource;
import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.powersource.PowerSourceModel;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;

public class OPSpecsCalculator {

	protected static final String GRID = "GRID";
	protected static final String HYDRO = "HYDRO";
	protected static final double INFINITY = 999999999.0;
	protected static final String TRUE = "TRUE";
	protected static final String FALSE = "FALSE";
	protected static final String AC_USE = "AC USE";
	protected static final double BATTERY_VOLTAGE_MAX = 29;
	protected static final double BATTERY_VOLTAGE_MIN = 23;
	protected static final double batteryChargePercentage_MAX = 100;
	protected static final double batteryChargePercentage_MIN = 0;
	protected static final double batteryFullChargeKWH = 6;
	protected static final double invACCurrentMAX = 4;
	protected double allPowerSourcesOPHourWithCurrentLoad = INFINITY;
	protected double allPowerSourcesOPHourUnderMaximunLoad = INFINITY;
	protected FacilityModel facilityModel;
	protected double totalConsummingPower = 0;
	protected double totalGeneratingPower = 0;
	protected ResourceManagerService resourceManagerService = null;
	protected ModelCapabilityClient modelClient=null;
	protected RequestModelResponse reqModel=null;
	protected double batteryChargePercentage = 0;
	protected String onGrid = "TRUE";
	protected String domainGreenPercentage = null;
	protected String status = "NONGREEN";

	public OPSpecsCalculator(FacilityModel facilityModel) {
		this.facilityModel = facilityModel;
	}


	public void calculateOPSpecs() throws Exception {
		init();
		for (PowerDistribution powerDistribution:facilityModel.getPowerDistributionList()){

			PDUModel pduModel = getPDUModel(powerDistribution.getResourceID());
			if (pduModel==null){throw new Exception("pduModel("+powerDistribution.getResourceID()+") is null");}

			double totalConsumtionACCurrent=0;
			double averageConsumtionACVoltage = 0;

			{// calculating averageConsumtionACVoltage and totalConsumtionACCurrent 
				List<PDUElement> list = pduModel.getOutlets();
				Double outletNumber = 0.0;
				for (PDUElement outlet:list){
					try{
					totalConsumtionACCurrent = totalConsumtionACCurrent + Double.valueOf(outlet.getLoad());
					averageConsumtionACVoltage = averageConsumtionACVoltage + Double.valueOf(outlet.getVoltage());
					outletNumber = outletNumber +1;
					}catch(Exception e){
						
					}
				}
				if (outletNumber>0.0){
					averageConsumtionACVoltage = averageConsumtionACVoltage / outletNumber;
				}else{
					throw new Exception("Error: Sorry, I cannot calculate the outlet average voltage. Number of readable outlets in PDU is zero.");
				}
			}

			
			for (PowerSource powerSource:powerDistribution.getPowerSourceList()){
				PowerSourceModel powerSourceModel = getPowerSourceModel(powerSource.getPort().getConnectedResourceID());
				calculateOPSpecsDetails(powerSourceModel, totalConsumtionACCurrent, averageConsumtionACVoltage);
			}

			if (facilityModel.getOperationalSpecs().getPowerSourceType().equalsIgnoreCase(HYDRO)){
				onGrid=TRUE;
				status="GREEN";
			}else if (facilityModel.getOperationalSpecs().getPowerSourceType().equalsIgnoreCase(GRID)){
				onGrid=TRUE;
				status="NONGREEN";
			}else{
				if (onGrid.equals(TRUE)){
					status="NONGREEN";
				}else{
					status="GREEN";
				}
			}


			{ //calculating green percentage
				String powerSourceGreenPercentage = facilityModel.getOperationalSpecs().getPowerSourceGreenPercentage();
				String gridGreenPercentage = facilityModel.getOperationalSpecs().getGridGreenPercentage();
				if (onGrid.equalsIgnoreCase(FALSE)){
					domainGreenPercentage = powerSourceGreenPercentage;
				} else {
					domainGreenPercentage = gridGreenPercentage;
				}
			}

			totalConsummingPower=totalConsummingPower + averageConsumtionACVoltage*totalConsumtionACCurrent/1000;

		}

		if (allPowerSourcesOPHourUnderMaximunLoad==INFINITY){
			allPowerSourcesOPHourUnderMaximunLoad=0;
		}
		if (allPowerSourcesOPHourWithCurrentLoad==INFINITY){
			allPowerSourcesOPHourWithCurrentLoad=0;
		}
	}


	private void calculateOPSpecsDetails(PowerSourceModel powerSourceModel,	double totalConsumtionACCurrent, double averageConsumtionACVoltage) throws Exception {

		{// observing if powerSource is on Grid or not?
			onGrid = powerSourceModel.getInvACMode();
			if (onGrid.equalsIgnoreCase(AC_USE)){
				onGrid = TRUE;
			}else{
				onGrid = FALSE;
			}
		}

		{// calculating battery charge and generating power
			if (!facilityModel.getOperationalSpecs().getPowerSourceType().equalsIgnoreCase(HYDRO) && 
					!facilityModel.getOperationalSpecs().getPowerSourceType().equalsIgnoreCase(GRID)){
				double batteryVoltage = Double.parseDouble(powerSourceModel.getChargerBatteryVoltage());
				double batteryCurrent = Double.parseDouble(powerSourceModel.getChargerCurrent());
				totalGeneratingPower=totalGeneratingPower + batteryVoltage * batteryCurrent/1000;
				batteryChargePercentage = batteryChargePercentage_MIN + (batteryVoltage - BATTERY_VOLTAGE_MIN)/(BATTERY_VOLTAGE_MAX-BATTERY_VOLTAGE_MIN)*(batteryChargePercentage_MAX - batteryChargePercentage_MIN);
			}else{
				batteryChargePercentage = 0;
			}

			if (batteryChargePercentage>batteryChargePercentage_MAX){
				batteryChargePercentage = batteryChargePercentage_MAX;
			}
			if (batteryChargePercentage<batteryChargePercentage_MIN){
				batteryChargePercentage = batteryChargePercentage_MIN;
			}
		}

		{// calculating op hour
			double thisPowerSourceOPHourWithCurrentLoad;
			double thisPowerSourceOPHourUnderMaximunLoad;

			thisPowerSourceOPHourWithCurrentLoad = 1000*batteryFullChargeKWH*batteryChargePercentage/100/(averageConsumtionACVoltage*totalConsumtionACCurrent);
			thisPowerSourceOPHourUnderMaximunLoad = 1000*batteryFullChargeKWH*batteryChargePercentage/100/(averageConsumtionACVoltage*invACCurrentMAX );
			
			if (thisPowerSourceOPHourUnderMaximunLoad>thisPowerSourceOPHourWithCurrentLoad){
				thisPowerSourceOPHourUnderMaximunLoad=thisPowerSourceOPHourWithCurrentLoad;
			}

			if (thisPowerSourceOPHourWithCurrentLoad<allPowerSourcesOPHourWithCurrentLoad){
				allPowerSourcesOPHourWithCurrentLoad = thisPowerSourceOPHourWithCurrentLoad;
			}
			if (thisPowerSourceOPHourUnderMaximunLoad<allPowerSourcesOPHourUnderMaximunLoad){
				allPowerSourcesOPHourUnderMaximunLoad = thisPowerSourceOPHourUnderMaximunLoad;
			}
		}




	}


	public PowerSourceModel getPowerSourceModel(String resourceID) throws Exception {
		resourceManagerService.checkExist(resourceID);
		modelClient = new ModelCapabilityClient(resourceID);
		reqModel = modelClient.requestModel(true);
		PowerSourceModel powerSourceModel = (PowerSourceModel)(reqModel.getResourceModel());
		return powerSourceModel;

	}


	public void init() throws Exception {
		resourceManagerService = new ResourceManagerService();
		resourceManagerService.init();
	}


	public PDUModel getPDUModel(String resourceID) throws Exception {
		resourceManagerService.checkExist(resourceID);
		modelClient = new ModelCapabilityClient(resourceID);
		reqModel = modelClient.requestModel(true);
		PDUModel pduModel = (PDUModel)(reqModel.getResourceModel());
		return pduModel;

	}


	public double getOPHourWithCurrentLoad() {
		return allPowerSourcesOPHourWithCurrentLoad;
	}


	public double getOPHourUnderMaximunLoad() {
		return allPowerSourcesOPHourUnderMaximunLoad;
	}


	public double getTotalConsummingPower() {
		return totalConsummingPower;
	}


	public double getTotalGeneratingPower() {
		return totalGeneratingPower;
	}

	public double getBatteryChargePercentage() {
		return batteryChargePercentage;
	}
	public String getOnGrid() {
		return onGrid;
	}

	public String getDomainGreenPercentage() {
		return domainGreenPercentage;
	}


	public String getStatus() {
		return status;
	}

}

