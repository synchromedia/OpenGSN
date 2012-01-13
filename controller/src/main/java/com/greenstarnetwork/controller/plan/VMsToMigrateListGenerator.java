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
package com.greenstarnetwork.controller.plan;

/**
 * This class will provide tools for creating a list of VMs which are on hosts with not enough energy to operate. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import java.util.Iterator;
import java.util.List;

import com.greenstarnetwork.controller.model.Host;
import com.greenstarnetwork.controller.model.HostList;
import com.greenstarnetwork.controller.model.VMList;
import com.greenstarnetwork.controller.model.VirtualMachine;
import com.greenstarnetwork.services.cloudmanager.model.VM;

public class VMsToMigrateListGenerator {

	/**
	 * This parameter shows the maximum LifeTime which consider a host to be not energy sufficient.
	 */
	//private double acceptableLifeTime;

	/**
	 * 
	 * @param hostList
	 * @return
	 */
	public VMList getVMsToMigrateList(HostList hostList) {

		VMList vmList = new VMList();

		if (hostList==null || hostList.getHosts()==null){
			return vmList;
		}

		hostList.sort();

		Iterator<Host> iterator = hostList.getHosts().iterator();
		while (iterator.hasNext()) {
			Host host = (Host)iterator.next();
			//if (host.getLifeTime()<acceptableLifeTime){
			if (host.getLifeTime()<host.getThreshold() || host.getEnergyPriority()==Host.HYDRO_ENERGY_PRIORITY){
				List<VM> vmList2 = host.getHostModel().getVMList();
				Iterator<VM> iterator2 = vmList2.iterator();
				while (iterator2.hasNext()) {
					vmList.addVM(new VirtualMachine((VM)iterator2.next()/*/////,host*/));
				}
			}

		}

		return vmList;
	}
	
	/*
	public void setMinAcceptableLifeTime(Double minAcceptableLifeTime){
		this.acceptableLifeTime = minAcceptableLifeTime;
	}
	
	public Double getMinAcceptableLifeTime(){
		return this.acceptableLifeTime;
	}
	*/

}
