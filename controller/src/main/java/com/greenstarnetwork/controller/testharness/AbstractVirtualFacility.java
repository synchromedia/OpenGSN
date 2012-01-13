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

import com.google.gson.annotations.Expose;
import com.greenstarnetwork.controller.model.HostList;
import com.greenstarnetwork.controller.testharness.exceptions.BaseFacilityException;

/**
 * @author hzhang
 *
 */
public abstract class AbstractVirtualFacility implements IFacility {

	@Expose
	protected HostList hostList = null;
	
	@Expose
	protected String facilityId = null;
	
	@Expose
	protected double staticCurrent;
	
	@Expose
	protected double voltage;
	
	@Expose
	protected String timeZone;
	
	@Expose
	protected long startTime;
	
	@Expose
	protected long currentTime;
	
	@Expose
	protected double remainingHours = 0.0;
	
	@Expose
	protected double remainingMaxLoadHours = 0.0;
	
	@Override
	public String getFacilityId() {
		return facilityId;
	}

	@Override
	public HostList getHostList() {
		return hostList;
	}
	
	public double getStaticCurrent() {
		return staticCurrent;
	}

	public double getVoltage() {
		return voltage;
	}

	@Override
	public String getTimeZone() {
		return timeZone;
	}
	
	@Override
	public double getRemainingHours() {
		return remainingHours;
	}

	@Override
	public double getRemainingMaxLoadHours() {
		return remainingMaxLoadHours;
	}

	@Override 
	public abstract void refreshModel(long currentTime) throws BaseFacilityException; 
	
	@Override
	public void migrate(String vmIp, String srcHostIp, String destHostIp) {

	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public long getCurrenTime() {
		return currentTime;
	}
	
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	
	public AbstractVirtualFacility() {
		
	}
	
	public AbstractVirtualFacility(String facilityId, HostList hostList, double staticCurrent, double voltage, String timeZone, long startTime) {
		super();
		this.facilityId = facilityId;
		this.hostList = hostList;
		this.staticCurrent = staticCurrent;
		this.voltage = voltage;
		this.timeZone = timeZone;
		this.startTime = startTime;
		this.currentTime = startTime;
	}

}
