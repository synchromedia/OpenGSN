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

import com.greenstarnetwork.services.cloudmanager.model.VM;


/**
 * A VirtualMachine include a VM model and a priority.
 * The lower the priority is, the sooner the VirtualMachine should be migrated
 * @author knguyen
 *
 */
public class VirtualMachine implements Comparable<VirtualMachine> 
{
	private VM vm = null;					//VM model (@see  com.greenstarnetwork.models.vmm)
	private double priority = 0;			//priority of the VirtualMachine
	/////private Host host;
	
	public VirtualMachine() {
	}

	public VirtualMachine(VM vm/*/////, Host host*/) {
		this.vm = vm;
		/////this.host = host;
	}

	public void setVM(VM vm) {
		this.vm = vm;
	}
	
	public VM getVM() {
		return this.vm;
	}
	
	public void setPriority(double p) {
		this.priority = p;
	}
	
	public double getPriority() {
		return this.priority;
	}
	
	/**
	 * Compare two VirtualMachines
	 * This function should be overwritten in case where a derived class wants to change the comparison criteria.
	 * Return: 0   (VirtualMachine1 = VirtualMachine2)
	 *         1   (VirtualMachine1 > VirtualMachine2)
	 *        -1   (VirtualMachine1 < VirtualMachine2)
	 */
	public int compareTo(VirtualMachine o) {
		if (this.priority == o.priority)
			return 0;
		else 
			return (this.priority > o.priority) ? 1 : -1;
	}

	@Override
	public String toString(){
		return "VM ("+vm.getIp()+")";
		
	}

	/**
	 * 
	 * @return
	 */
	/*public Host getHost() {
		return host;
	}
	
	public void setHost(Host host) {
		this.host=host;
	}*/
}
