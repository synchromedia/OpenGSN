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
package com.greenstarnetwork.services.facilitymanager.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.services.utils.ObjectSerializer;

/**
 * 
 * maintain operational data such as op-hour
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class OperationalSpecs implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3485700078966462589L;
	
	/**
	 * the number of hours which this site could work with current load.
	 */
	private double opHourUnderCurrentLoad = 0;
	
	/**
	 * the number of hours which this site could work with under maximum load.
	 */
	private double opHourUnderMaximumLoad = 0;
	
	
    private String domainGreenPercentage= null;
    
    private String gridGreenPercentage= null;
    
    private String powerSourceGreenPercentage= null;
	
	private double totalConsummingPower = 0;
	
	private double totalGeneratingPower = 0;
	
	private String opHourThreshold = null;
	
	private String opHourThresholdUnderMax = null;
	
	private double batteryChargePercentage = 0;
	
	private String powerSourceType = null;
	
	private String onGrid = null;
	
	
	/**
	 * 
	 */
	public OperationalSpecs(){
	}

	/**
	 * 
	 * @return
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	/**
	 * 
	 * @param opHourUnderCurrentLoad
	 */
	public void setOpHourUnderCurrentLoad(double opHourWithCurrentLoad) {
		this.opHourUnderCurrentLoad = opHourWithCurrentLoad;
	}

	/**
	 * 
	 * @return
	 */
	public double getOpHourUnderCurrentLoad() {
		return opHourUnderCurrentLoad;
	}

	/**
	 * 
	 * @param opHourUnderMaximumLoad
	 */
	public void setOpHourUnderMaximumLoad(double opHourUnderMaximunLoad) {
		this.opHourUnderMaximumLoad = opHourUnderMaximunLoad;
	}

	/**
	 * 
	 * @return
	 */
	public double getOpHourUnderMaximumLoad() {
		return opHourUnderMaximumLoad;
	}

	
	public void setDomainGreenPercentage(String greenPercentage) {
		this.domainGreenPercentage = greenPercentage;
	}




	public String getDomainGreenPercentage() {
		return domainGreenPercentage;
	}




	public void setTotalConsummingPower(double totalConsummingPower) {
		this.totalConsummingPower = totalConsummingPower;
	}




	public double getTotalConsummingPower() {
		return totalConsummingPower;
	}




	public void setTotalGeneratingPower(double totalGeneratingPower) {
		this.totalGeneratingPower = totalGeneratingPower;
	}




	public double getTotalGeneratingPower() {
		return totalGeneratingPower;
	}

	public void setOpHourThreshold(String opHourThreshold) {
		this.opHourThreshold = opHourThreshold;
	}

	public String getOpHourThreshold() {
		return opHourThreshold;
	}

	public void setBatteryChargePercentage(double batteryChargePercentage) {
		this.batteryChargePercentage = batteryChargePercentage;
	}

	public double getBatteryChargePercentage() {
		return batteryChargePercentage;
	}

	public void setPowerSourceType(String powerSource) {
		this.powerSourceType = powerSource;
	}

	public String getPowerSourceType() {
		return powerSourceType;
	}

	public void setGridGreenPercentage(String domainGridGreenPercentage) {
		this.gridGreenPercentage = domainGridGreenPercentage;
	}

	public String getGridGreenPercentage() {
		return gridGreenPercentage;
	}

	public void setOnGrid(String powerSourceMode) {
		this.onGrid = powerSourceMode;
	}

	public String getOnGrid() {
		return onGrid;
	}

	public void setPowerSourceGreenPercentage(String powerSourceGreenPercentage) {
		this.powerSourceGreenPercentage = powerSourceGreenPercentage;
	}

	public String getPowerSourceGreenPercentage() {
		return powerSourceGreenPercentage;
	}

	public void setOpHourThresholdUnderMax(String opHourThresholdUnderMax) {
		this.opHourThresholdUnderMax = opHourThresholdUnderMax;
	}

	public String getOpHourThresholdUnderMax() {
		return opHourThresholdUnderMax;
	}

	

	
	
	
	
	
}
