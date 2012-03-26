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
package com.opengsn.services.cloudmanager.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Host {
	
	private String resourceId;
	private String ip;
	private int hostPriority;
	private int totalProcessors;
	private int totalMemory;
	private int processorSpeed;
	private int maxVM;
	private String state;
	private List<VirtualMachine> VMs = null;
	
	public Host(){
		VMs = new ArrayList<VirtualMachine>();
	}
	public int getIdleMemory(){
		int usedMemory = 0;
		Iterator<VirtualMachine> itr = VMs.iterator();
		while(itr.hasNext()) {
		    VirtualMachine vm = itr.next();
		    usedMemory += vm.getMemory();
		}
		
		return totalMemory - usedMemory;
	}
	
	public int getIdleProcessors(){
		int usedProcessors = 0;
		Iterator<VirtualMachine> itr = VMs.iterator();
		while(itr.hasNext()) {
		    VirtualMachine vm = itr.next();
		    usedProcessors += vm.getCpuNumber();
		}
		
		return totalProcessors - usedProcessors;
	}
	
	public int getCurrentVMNumber(){
		return VMs.size();
	}
	
	public boolean hasReachedMaxVM(){
		if(maxVM - VMs.size() > 0){
			return false;
		}
		return true;
	}
	
	public void removeVM(String name){
		if (VMs == null)
			return;
		Iterator<VirtualMachine> itr = VMs.iterator();
		while(itr.hasNext()) {
		    VirtualMachine vm = itr.next();
		    if(vm.getName().compareTo(name)==0){
		    	itr.remove();
		    }
		}
	}
	
	public VirtualMachine getVM(String name){
		Iterator<VirtualMachine> itr = VMs.iterator();
		while(itr.hasNext()) {
		    VirtualMachine vm = itr.next();
		    if(vm.getName().compareTo(name)==0){
		    	return vm;
		    }
		}
		return null;
	}

	
	public String getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getTotalProcessors() {
		return totalProcessors;
	}
	
	public void setTotalProcessors(int totalProcessors) {
		this.totalProcessors = totalProcessors;
	}
	
	public int getTotalMemory() {
		return totalMemory;
	}
	
	public void setTotalMemory(int totalMemory) {
		this.totalMemory = totalMemory;
	}
	
	public int getProcessorSpeed() {
		return processorSpeed;
	}
	
	public void setProcessorSpeed(int processorSpeed) {
		this.processorSpeed = processorSpeed;
	}

	public void setHostPriority(int hostPriority) {
		this.hostPriority = hostPriority;
	}

	public int getHostPriority() {
		return hostPriority;
	}

	public void setMaxVM(int maxVM) {
		this.maxVM = maxVM;
	}

	public int getMaxVM() {
		return maxVM;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setVMs(List<VirtualMachine> vMs) {
		VMs = vMs;
	}

	public List<VirtualMachine> getVMs() {
		return VMs;
	}
	

}
