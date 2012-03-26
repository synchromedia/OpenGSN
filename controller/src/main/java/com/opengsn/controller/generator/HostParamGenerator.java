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
package com.opengsn.controller.generator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;
import com.opengsn.services.cloudmanager.model.VM;
import com.opengsn.services.cloudmanager.model.VmHostModel;
import com.opengsn.services.utils.ObjectSerializer;

/**
 * Generate parameters for hosts.
 * @author knguyen
 * @author Ali LAHLOU (Synchromedia, ETS)
 */
public class HostParamGenerator extends BaseGenerator
{
	public static int MAX_ENERGY = 10;				//Energy levels
	public static int MAX_LIFETIME = 10;			//Lifetime levels
	public static int MAX_VM_PER_HOST = 10;			//Number of VMs per host
	public static int MAX_CPU = 16;					//Max CPU per host
	public static int MAX_MEMORY = 40000;			//Max memory capacity per host
	
	//Highest energy level of generated hosts
	private int max_energy = MAX_ENERGY;

	//Highest lifetime level of generated hosts 
	private int max_lifetime = MAX_LIFETIME;
	
	//Maximum number of CPUs in each generated host 
	private int max_CPU = MAX_CPU;
	
	//Maximum capacity of memory in each generated host
	private int max_Memory = MAX_MEMORY;
	
	//Minimum capacity of memory in each generated VM
	private int min_VM_Memory = 0;
	
	//Maximum number of VMs per generated hosts
	private int max_VMs = MAX_VM_PER_HOST;
	
	//Network address of generated hosts
	private InetAddress network_address = null;
	
	//Network mask of generated hosts
	private InetAddress network_mask = null;
	
	public HostParamGenerator() {
	}

	public void setMaxEnergy(int max) {
		this.max_energy = max;
	}
	
	public void setMaxLifeTime(int max) {
		this.max_lifetime = max;
	}
	
	public void setMaxCPU(int cpu) {
		this.max_CPU = cpu;
	}
	
	public int getMaxCPU() {
		return this.max_CPU;
	}
	
	public void setMaxMemory(int mem) {
		this.max_Memory = mem;
	}
	
	public int getMaxMemory() {
		return this.max_Memory;
	}
	
	public int getMaxVMsPerHost() {
		return this.max_VMs;
	}
	
	public void setMaxVMsPerHost(int vms) {
		this.max_VMs = vms;
	}
	
	public InetAddress getNetworkAddress() {
		return this.network_address;
	}
	
	public void setNetworkAddress(String addr) throws UnknownHostException{
		this.network_address = InetAddress.getByName(addr);
	}
	
	public InetAddress getNetworkMask() {
		return this.network_mask;
	}
	
	public void setNeworkMask(String addr)  throws UnknownHostException{
		this.network_mask = InetAddress.getByName(addr);
	}
	
	public double[] generateEnergy(int max, int n) {
		return this.generateDoubleRange(max, n);
	}

	public double[] generateLifeTime(int max, int n) {
		return this.generateDoubleRange(max, n);
	}
	
	/**
	 * Generate energy and lifetime values for a list of hosts, then create a HostList object
	 * @param hosts
	 * @return
	 */
	public HostList generateHostList(List<Host> hosts) 
	{
		if (hosts == null)
			return null;
		
		int n = hosts.size();
		if (n == 0)
			return null;
		
		double[] energy_list = this.generateEnergy(max_energy, n);
		double[] lifetime_list = this.generateLifeTime(max_lifetime, n);
		
		HostList ret = new HostList();
		for (int pi=0; pi<n; pi++) {
			hosts.get(pi).setEnergyPriority(energy_list[pi]);
			hosts.get(pi).setLifeTime(lifetime_list[pi]);
			hosts.get(pi).setLifetimeUnderMaxLoad(lifetime_list[pi]/2);
			hosts.get(pi).setThreshold(1);
			hosts.get(pi).setThresholdUnderMax(4);
		}
		ret.setHosts(hosts);
		return ret;
	}
	
	/**
	 * Write a HostList object to a file (in XML format)
	 * @param file
	 * @param list
	 * @throws IOException
	 */
	public void writeHostListToFile(String file, HostList list) throws IOException {
		if (list != null)
		{
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file)); 
			String s = ObjectSerializer.toXml(list);
			os.write(s);
			os.flush();
			os.close();
		}
	}
	
	/**
	 * Load a HostList object from an XML file
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public HostList loadHostListFromFile(String file) throws IOException 
	{
		HostList ret = (HostList)fromXml(file);
		return ret;
	}

	/**
	 * Serialize an Object into a XML string
	 * @param xml
	 * @return
	 */
	public Object fromXml(String xml) {	

		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(HostList.class);
			Object obj = context
			.createUnmarshaller().unmarshal(in);
			return obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}

	/**
	 * Generate a fake host, with the number of CPUs < max CPU, memory capacity < maxMemory and a given VM lists
	 * @param maxCPU
	 * @param maxMemory
	 * @param vmlist
	 * @return
	 */
	public Host generateFakeHost(int maxCPU, int maxMemory, List<VM> vmlist) 
	{
		VmHostModel hostmodel = new VmHostModel();
		if ((network_address == null) || (network_mask == null))
			hostmodel.setAddress(this.generateIP());
		else
			hostmodel.setAddress(this.generateIP(network_address, network_mask));
	
		int usedMem = 0;
		int usedCPUs = 0;
		for (int pj=0; pj<vmlist.size(); pj++) {
			hostmodel.addVM(vmlist.get(pj));
			usedMem += new Integer(vmlist.get(pj).getMemory());
			usedCPUs += new Integer(vmlist.get(pj).getVcpu());
		}
		
		int nbreCPU = this.generateIntegerWithMin(usedCPUs, maxCPU);
		hostmodel.setNbrCPUs( Integer.toString( nbreCPU ) );
		
		hostmodel.setBufferMemory(new Integer(usedMem).toString());
		hostmodel.setCoresPerSocket("4");
		int freeMem = 0;
		if ((maxMemory - usedMem) <= 1)
			hostmodel.setFreeMemory("0");
		else {
			freeMem = this.generateInteger(maxMemory - usedMem);
			hostmodel.setFreeMemory(new Integer(freeMem).toString());
		}
		hostmodel.setTotalMemory(new Integer(usedMem + freeMem).toString());
		Host h = new Host();
		h.setHostModel(hostmodel);
		h.setResourceID(this.generateID());
		return h;
	}
	
	/**
	 * Generate a set of hosts, each has no more than max CPU and maxMemory
	 * @param numberOfHost
	 * @param maxCPU
	 * @param maxMemory
	 * @return
	 */
	public HostList generateFakeHostList(int numberOfHost) {
		List<Host> hosts = new ArrayList<Host>();
		for (int pi=0; pi < numberOfHost; pi++)
		{
			VMParamGenerator vmgen = new VMParamGenerator();
			vmgen.setMin_memory(min_VM_Memory);
			List<VM> vmlist = vmgen.generateFakeVMList(max_VMs, max_CPU, max_Memory);
			Host h = this.generateFakeHost(max_CPU, max_Memory, vmlist);
			hosts.add(h);
			System.out.println("----> Host: " + h.getHostModel().getAddress());
		}
		return this.generateHostList(hosts);
	}

	/**
	 * @param min_VM_Memory the min_VM_Memory to set
	 */
	public void setMin_VM_Memory(int min_VM_Memory) {
		this.min_VM_Memory = min_VM_Memory;
	}

	/**
	 * @return the min_VM_Memory
	 */
	public int getMin_VM_Memory() {
		return min_VM_Memory;
	}
	
}
