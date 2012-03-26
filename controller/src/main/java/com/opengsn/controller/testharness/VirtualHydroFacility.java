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

import java.util.List;

import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;
import com.opengsn.controller.testharness.exceptions.BadTimeException;

/**
 * @author hzhang
 *
 */
public class VirtualHydroFacility extends AbstractVirtualFacility {

	public VirtualHydroFacility() {
		super();
	}
	
	public VirtualHydroFacility(String facilityId, HostList hostList,
			double staticCurrent, double voltage, String timeZone, long startTime) {
		super(facilityId, hostList, staticCurrent, voltage, timeZone, startTime);
		
		this.remainingHours = Double.MAX_VALUE;;		
		this.remainingMaxLoadHours = Double.MAX_VALUE;
	}
	
	public VirtualHydroFacility(VirtualHydroFacility vhf) {
		super(vhf.getFacilityId(), vhf.getHostList(), vhf.getStaticCurrent(), vhf.getVoltage(), vhf.getTimeZone(), vhf.getStartTime());

		this.remainingHours = Double.MAX_VALUE;;		
		this.remainingMaxLoadHours = Double.MAX_VALUE;
	}
	
	@Override
	public void refreshModel(long currentTime) throws BadTimeException {
		if(currentTime <= this.getCurrenTime())
			throw new BadTimeException(""); // TODO make proper time format
		
		setCurrentTime(currentTime);
		
		List<Host> hosts = this.getHostList().getHosts();
	
		for(Host host : hosts) {
			host.setLifeTime(this.getRemainingHours());
			host.setLifetimeUnderMaxLoad(this.getRemainingMaxLoadHours());
			host.setThreshold(1);
			host.setThresholdUnderMax(4);
		}
	}
	
}
