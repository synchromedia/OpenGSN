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

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.services.utils.ObjectSerializer;


/**
 * this class represent the class model
 * 
 * @author adaouadji (adaouadji@synchromedia.ca)
 *
 */

@XmlRootElement
public class VmHostModel implements  Serializable {
	
	private static final long serialVersionUID = 8785700078966462589L;

	private String address;			//IP address
	
	private String location;		//geographical location

	private String totalMemory;		//total memory

	private String freeMemory;		//free memory

	private String bufferMemory;	//buffer memory
	
	private String cpu;				//CPU model

	private String speed;			//CPU speed

	private String hypervisor;		//hypervisor type

	private String os;				//OS name

	private String nbr_CPUs;		//number of CPUs

	private String CPU_sockets;		//number of CPU sockets
	
	private String cores_per_socket;	//number of CPU cores per socket
	
	private String threads_per_core;	//number of threads per CPU core
	
	private String NUMA;				//number of NUMA cells
	
	private String totalSwapMemory;		//total swap memory capacity
	
	private String freeSwapMemory;		//free swap memory
	
	private String cachedSwapMemory;	//cached swap memory
	
	private String cpu_usertime;		//CPU Statistical information
	
	private String cpu_systemtime;	
	
	private String cpu_nicetime;
	
	private String cpu_idletime;

	private String cpu_iotime;
	
	private String cpu_hardwaretime;
	
	private String cpu_softwaretime;
	
	private String cpu_stealtime;
	
	private String maxVMs;				//maximal number of VMs to be created
	
	@XmlElement(name = "vm")
	private List<VM>VMList = null;
	
	public VmHostModel(){
		
		VMList = new ArrayList<VM>();
			}
	
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	
	public String getNbrCPUs() {
		return this.nbr_CPUs;
	}
	
	public void setNbrCPUs(String nbrCPUs) {
		this.nbr_CPUs = nbrCPUs;
	}
	
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
	public String getCPUSockets() {
		return this.CPU_sockets;
	}
	
	public void setCPUSockets(String CPUSockets) {
		this.CPU_sockets = CPUSockets;
	}
	
	public String getCoresPerSocket() {
		return this.cores_per_socket;
	}

	public void setCoresPerSocket(String cores) {
		this.cores_per_socket = cores;
	}
	
	public String getThreadsPerCore() {
		return this.threads_per_core;
	}
	
	public void setThreadsPerCore(String threads) {
		this.threads_per_core = threads;
	}
	
	public String getNUMA() {
		return this.NUMA;
	}
	
	public void setNUMA(String numa) {
		this.NUMA = numa;
	}
	
	public String getHypervisor() {
		return hypervisor;
	}
	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}

	public String getFreeMemory() {
		return freeMemory;
	}
	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}
	
	public String getBufferMemory() {
		return this.bufferMemory;
	}
	public void setBufferMemory(String mem) {
		this.bufferMemory = mem;
	}

	public String getTotalSwapMemory() {
		return this.totalSwapMemory;
	}
	public void setTotalSwapMemory(String mem) {
		this.totalSwapMemory = mem;
	}
	
	public String getFreeSwapMemory() {
		return this.freeSwapMemory;
	}
	public void setFreeSwapMemory(String mem) {
		this.freeSwapMemory = mem;
	}
	
	public String getCachedSwapMemory() {
		return this.cachedSwapMemory;
	}
	public void setCachedSwapMemory(String mem) {
		this.cachedSwapMemory = mem;
	}
	
	public String getCPUUserTime() {
		return this.cpu_usertime;
	}
	public void setCPUUserTime(String time) {
		this.cpu_usertime = time;
	}
	
	public String getCPUSystemTime() {
		return this.cpu_systemtime;
	}
	public void setCPUSystemTime(String time) {
		this.cpu_systemtime = time;
	}

	public String getCPUNiceTime() {
		return this.cpu_nicetime;
	}
	public void setCPUNiceTime(String time) {
		this.cpu_nicetime = time;
	}

	public String getCPUIdleTime() {
		return this.cpu_idletime;
	}
	public void setCPUIdleTime(String time) {
		this.cpu_idletime = time;
	}
	
	public String getCPUIOTime() {
		return this.cpu_iotime;
	}
	public void setCPUIOTime(String time) {
		this.cpu_iotime = time;
	}

	public String getCPUHardwareTime() {
		return this.cpu_hardwaretime;
	}
	public void setCPUHardwareTime(String time) {
		this.cpu_hardwaretime = time;
	}

	public String getCPUSoftwareTime() {
		return this.cpu_softwaretime;
	}
	public void setCPUSoftwareTime(String time) {
		this.cpu_softwaretime = time;
	}

	public String getCPUStealTime() {
		return this.cpu_stealtime;
	}
	public void setCPUStealTime(String time) {
		this.cpu_stealtime = time;
	}

	public String getMaxVMs() {
		return this.maxVMs;
	}
	
	public void setMaxVMs(String maxVMs) {
		this.maxVMs = maxVMs;
	}
	
	public boolean addVM(VM vm)
	{
	   if (VMList == null)
		   VMList = new ArrayList<VM>(); 
		for (int i=0; i < VMList.size(); i++) 
		{
			if (vm.getName().equals(VMList.get(i).getName()))
				return false;
		}
		VMList.add(vm);	
		return true;
	}

	public VM getVM(String vmName){
		VM vm = null;
		for (int i=0; i < VMList.size(); i++) {
			if (vmName.equals(VMList.get(i).getName()))
			vm = (VM) VMList.get(i);
			}
	
		return vm;
	}
	
	public List<VM> getVMList() {
		return this.VMList;
	}
	
	public boolean removeVM(String vmName)
	{
		for (int i=0; i < VMList.size(); i++) 
		{
			if (vmName.equals(VMList.get(i).getName()))
			{
				VMList.remove(i);
				return true;
			}
		}
	
		return false;
	}
	
	public void removeVMs(){
		VMList.clear();
	}
	
	
	/**
	 * Serialize model to a XML string
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	/**
	 * Get a model from a XML string
	 * @param xml
	 * @return
	 */
	public static VmHostModel fromXML(String xml) {
		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(VmHostModel.class);
			Object obj = context.createUnmarshaller().unmarshal(in);
			return (VmHostModel)obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}
}
