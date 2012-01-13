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
package com.greenstarnetwork.controller.generator;

import java.util.ArrayList;
import java.util.List;

import com.greenstarnetwork.controller.model.VMList;
import com.greenstarnetwork.controller.model.VirtualMachine;
import com.greenstarnetwork.services.cloudmanager.model.VM;
import com.greenstarnetwork.services.cloudmanager.model.VMStatus;

/**
 * Generate parameters for VirtualMachines.
 * @author knguyen
 * @author Ali LAHLOU (Synchromedia, ETS)
 */
public class VMParamGenerator extends BaseGenerator
{
	public static int MAX_PRIORITY = 10;				//VM priority levels
	
	private int max_priority = MAX_PRIORITY;
	
	private int min_memory = 0;

	public VMParamGenerator() {
	}
	
	public void setMaxPriority(int max) {
		this.max_priority = max;
	}
	
	public double[] generateVMPriority(int max, int n) {
		return this.generateDoubleRange(max, n);
	}
	
	/**
	 * Randomly generate priorities for a list of VMs, then return a VMList object
	 * @param modellist
	 * @return
	 */
	public VMList generateVMList(List<VM> modellist) 
	{
		if (modellist == null)
			return null;
		
		int n = modellist.size();
		if (n == 0)
			return null;
		
		double[] priority_list = this.generateVMPriority(max_priority, n);
		
		VMList ret = new VMList();
		for (int pi=0; pi<n; pi++) {
			VirtualMachine v = new VirtualMachine();
			v.setVM(modellist.get(pi));
			v.setPriority(priority_list[pi]);
			ret.addVM(v);
		}
		return ret;
	}
	
	/**
	 * Generate a fake VM whose the number of CPUs < maxCPU and memory capacity < maxMem
	 * @param maxCPU
	 * @param maxMem
	 * @return
	 */
	public VM generateFakeVM(int maxCPU, int maxMem) {
		VM ret = new VM();
		ret.setId(this.generateID());
		ret.setIp(this.generateIP());
		ret.setName("Fake-" + this.generateString(20));
		ret.setOs("ubuntu");
		ret.setEmulator("vnc");
		ret.setGraphics("graphic");
		int mem = this.generateIntegerWithMin(min_memory, maxMem);
		ret.setMemory(new Integer(mem).toString());
		if (mem == min_memory)
			ret.setCurrentMemory(new Integer(min_memory).toString());
		else
			ret.setCurrentMemory(new Integer(this.generateIntegerNotNull(mem)).toString());
		ret.setNetwork("network");
		ret.setStatus(VMStatus.STARTED);
		ret.setVcpu(new Integer(this.generateIntegerNotNull(maxCPU)).toString());
		return ret;
	}
	
	/**
	 * Generate a set of VMs, each has no more than maxCPU and maxMemory
	 * @param maxNumberOfVM
	 * @param maxCPU
	 * @param maxMem
	 * @return
	 */
	public List<VM> generateFakeVMList(int maxNumberOfVM, int maxCPU, int maxMem) {
		List<VM> modellist = new ArrayList<VM>();
		int numberOfVM = this.generateInteger(maxNumberOfVM);
		int availMem = maxMem;
		int availCpu = maxCPU;
		for (int pi=0; pi<numberOfVM; pi++) {
			if (availMem < this.getMin_memory())
				break;
			if (availCpu < 1)
				break;
			VM vm = this.generateFakeVM(availCpu, availMem);
			modellist.add(vm);
			availMem -= Integer.parseInt(vm.getMemory());
			availCpu -= Integer.parseInt(vm.getVcpu());
		}
		return modellist;
	}

	/**
	 * @param min_memory the min_memory to set
	 */
	public void setMin_memory(int min_memory) {
		this.min_memory = min_memory;
	}

	/**
	 * @return the min_memory
	 */
	public int getMin_memory() {
		return min_memory;
	}
}
