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
package com.greenstarnetwork.controller.model;

import com.greenstarnetwork.services.cloudmanager.model.VmHostModel;



/**
 * A Host represents a physical server hosting a set of VMs.
 * It includes a VmHostModel and some additional information such as energy_priority and lifetime 
 * @author knguyen
 *
 */
public class Host implements Comparable<Host>
{
	public static final double HYDRO_ENERGY_PRIORITY = 1;
	public static final double SUN_ENERGY_PRIORITY = 2;
	public static final double WIND_ENERGY_PRIORITY = 3;

	/**
	 * Green percentage of hydro nodes
	 * All nodes with a lower green percentage are considered hydro nodes.
	 */
	public static final double HYDRO_GREEN_PERCENTAGE = 85;		 

	/**
	 * Green percentage of solar nodes
	 * All nodes with a lower green percentage are considered solar nodes.
	 */
	public static final double SUN_GREEN_PERCENTAGE = 95;		 
	
	/**
	 * Green percentage of wind nodes
	 * All nodes with a lower green percentage are considered wind nodes.
	 */
	public static final double WIND_GREEN_PERCENTAGE = 98;		 

	private double energy_priority;					//priority energy sources, for example wind > solar > hydro
	private double lifetime;						//battery time
	private double lifetimeUnderMaxLoad;			//battery time under maximum load
	private double threshold;
	private double thresholdUnderMax;
	private VmHostModel hostModel;
	
	private String resourceID = null;				//Resource ID representing the host in the IaaS environment
	private String resourceName = null;				//Resource Name
	private String domainID = null;					//The romain where resource is running
	
	public Host() {
		energy_priority = 0;
		lifetime = 0;
		lifetimeUnderMaxLoad = 0;
		hostModel = null;
		
	}
	
	public Host(VmHostModel host) {
		this.hostModel = host;
	}

	public String getResourceID() {
		return this.resourceID;
	}
	
	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}
	
	public double getEnergyPriority() {
		return this.energy_priority;
	}
	
	public void setEnergyPriority(double energy_priority) {
		this.energy_priority = energy_priority;
	}
	
	public double getLifeTime() {
		return this.lifetime;
	}
	
	public void setLifeTime(double lifetime) {
		this.lifetime = lifetime;
		/*if (lifetimeUnderMaxLoad==0){
			lifetimeUnderMaxLoad=lifetime;
		}*/
	}
	
	public VmHostModel getHostModel() {
		return this.hostModel;
	}
	
	public void setHostModel (VmHostModel hostModel) {
		this.hostModel = hostModel;
	}

	/**
	 * Compare two hosts
	 * This function should be overwritten in case where a derived class wants to change the comparison criteria.
	 * Return: 0   (host1 = host2)
	 *         1   (host1 > host2)
	 *        -1   (host1 < host2)
	 */
	public int compareTo(Host h) {
		if (this.lifetime < 0)
			return -1;
		else if (h.lifetime < 0)
			return 1;
		else { 
			if (this.energy_priority > h.energy_priority) 
				return 1;
			else if (this.energy_priority == h.energy_priority) {
				if (this.lifetime > h.lifetime)
					return 1;
				else if (this.energy_priority == h.energy_priority)
					return 0;
				else
					return -1;
			}else
				return -1;
		}
	}

	@Override
	public String toString(){
		return "Host ("+hostModel.getAddress()+")";
		
	}

	/**
	 * @param domainID the domainID to set
	 */
	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}

	/**
	 * @return the domainID
	 */
	public String getDomainID() {
		return domainID;
	}
	
	//Set host priority based on green energy percentage
	public void setPriorityBasedOnGreenPercentage(double percentage) {
		if (percentage <= HYDRO_GREEN_PERCENTAGE)
			this.energy_priority = HYDRO_ENERGY_PRIORITY;
		else if (percentage <= SUN_GREEN_PERCENTAGE)
			this.energy_priority = SUN_ENERGY_PRIORITY;
		else
			this.energy_priority = WIND_GREEN_PERCENTAGE;
	}

	//Set host priority based on type of energy source
	public void setPriorityBasedOnSourceType(String type) {
		if (type.compareTo("SOLAR") == 0)
			this.energy_priority = SUN_ENERGY_PRIORITY;
		else if (type.compareTo("WIND") == 0)
			this.energy_priority = WIND_ENERGY_PRIORITY;
		else if (type.compareTo("HYDRO") == 0)
			this.energy_priority = HYDRO_ENERGY_PRIORITY;
		else
			this.energy_priority = HYDRO_ENERGY_PRIORITY;
	}

	public void setLifetimeUnderMaxLoad(double lifetimeUnderMaxLoad) {
		this.lifetimeUnderMaxLoad = lifetimeUnderMaxLoad;
		/*if (lifetime==0){
			lifetime=lifetimeUnderMaxLoad;
		}*/
	}

	public double getLifetimeUnderMaxLoad() {
		return lifetimeUnderMaxLoad;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThresholdUnderMax(double thresholdUnderMax) {
		this.thresholdUnderMax = thresholdUnderMax;
	}

	public double getThresholdUnderMax() {
		return thresholdUnderMax;
	}

	/**
	 * @param resourceName the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}
}