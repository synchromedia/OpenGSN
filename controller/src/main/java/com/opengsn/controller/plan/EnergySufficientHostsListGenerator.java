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
package com.opengsn.controller.plan;

/**
 * This class will provide tools for creating a list of energy sufficient hosts from a list of host with a lifeTime parameter. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


import java.util.Iterator;

import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;

public class EnergySufficientHostsListGenerator {

	/**
	 * This parameter shows the minimum LifeTime for a host to consider energy sufficient.
	 */
	//private double necessaryLifeTime;

	/**
	 * 
	 * @param hostList
	 * @return
	 */
	public HostList getEnergySufficientHostsList(HostList hostList) {
		HostList hostList2 = new HostList();
		if (hostList==null || hostList.getHosts()==null){
			return hostList2;
		}

		hostList.sort();

		Iterator<Host> iterator = hostList.getHosts().iterator();
		while (iterator.hasNext()) {
			Host host = (Host)iterator.next();
			//if (host.getLifetimeUnderMaxLoad()>=necessaryLifeTime){
			if (host.getLifetimeUnderMaxLoad()>=host.getThresholdUnderMax() || host.getEnergyPriority()==Host.HYDRO_ENERGY_PRIORITY){
				hostList2.addHost(host);
			}
		}
		return hostList2;
	}

	/*
	public void setmaxNecessaryLifeTime(Double maxNecessaryLifeTime){
		this.necessaryLifeTime = maxNecessaryLifeTime;
	}
	
	public Double getmaxNecessaryLifeTime(){
		return this.necessaryLifeTime;
	}
	*/

	
}
