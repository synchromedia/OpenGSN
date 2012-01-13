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

import com.greenstarnetwork.controller.model.Host;
import com.greenstarnetwork.controller.model.HostList;
import com.greenstarnetwork.controller.model.VirtualMachine;
import com.greenstarnetwork.services.cloudmanager.model.VM;
import com.greenstarnetwork.services.cloudmanager.model.VmHostModel;

/**
 * This class will provide simple data structure for junit tests. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

public class BasicGenerator {

	public BasicGenerator() {
	}
	
	public HostList getBasicHosts(){
		
		/* hydro host 5 config */
		Host hydroHost5 = new Host(new VmHostModel());
		
		VirtualMachine vm9 = new VirtualMachine(new VM()/*,hydroHost5*/);
		vm9.getVM().setName("9");
		vm9.getVM().setVcpu("1");
		vm9.getVM().setMemory("1000");
		vm9.getVM().setIp("10.10.10.109");
		
		VirtualMachine vm10 = new VirtualMachine(new VM()/*, hydroHost5*/);
		vm10.getVM().setName("10");
		vm10.getVM().setVcpu("2");
		vm10.getVM().setMemory("2000");
		vm10.getVM().setIp("10.10.10.110");
		
		
		
		hydroHost5.getHostModel().addVM(vm9.getVM());
		hydroHost5.getHostModel().addVM(vm10.getVM());
		hydroHost5.getHostModel().setTotalMemory("50000");
		hydroHost5.getHostModel().setNbrCPUs("50");
		hydroHost5.setLifeTime(1000000);
		hydroHost5.setLifetimeUnderMaxLoad(1000000);
		hydroHost5.setThreshold(1);
		hydroHost5.setThresholdUnderMax(4);
		hydroHost5.setEnergyPriority(Host.HYDRO_ENERGY_PRIORITY);
		hydroHost5.getHostModel().setAddress("10.10.10.5");
		
		/* host 1 config */
		Host host1 = new Host(new VmHostModel());
		
		VirtualMachine vm1 = new VirtualMachine(new VM()/*, host1*/);
		vm1.getVM().setName("1");
		vm1.getVM().setVcpu("1");
		vm1.getVM().setMemory("1000");
		vm1.getVM().setIp("10.10.10.101");
		
		VirtualMachine vm2 = new VirtualMachine(new VM()/*, host1*/);
		vm2.getVM().setName("2");
		vm2.getVM().setVcpu("2");
		vm2.getVM().setMemory("2000");
		vm2.getVM().setIp("10.10.10.102");
		
			
		
		
		host1.getHostModel().addVM(vm1.getVM());
		host1.getHostModel().addVM(vm2.getVM());
		host1.getHostModel().setTotalMemory("5000");
		host1.getHostModel().setNbrCPUs("5");
		host1.setLifeTime(5);
		host1.setLifetimeUnderMaxLoad(2.5);
		host1.setThreshold(1);
		host1.setThresholdUnderMax(4);
		host1.setEnergyPriority(Host.WIND_ENERGY_PRIORITY);
		host1.getHostModel().setAddress("10.10.10.1");
		
		
		/* host 2 config */
		Host host2 = new Host(new VmHostModel());
		
		VirtualMachine vm3 = new VirtualMachine(new VM()/*, host2*/);
		vm3.getVM().setName("3");
		vm3.getVM().setVcpu("2");
		vm3.getVM().setMemory("1000");
		vm3.getVM().setIp("10.10.10.103");
		
		VirtualMachine vm4 = new VirtualMachine(new VM()/*,  host2*/);
		vm4.getVM().setName("4");
		vm4.getVM().setVcpu("1");
		vm4.getVM().setMemory("2000");
		vm4.getVM().setIp("10.10.10.104");
		
				
		host2.getHostModel().addVM(vm3.getVM());
		host2.getHostModel().addVM(vm4.getVM());
		
		host2.getHostModel().setTotalMemory("4000");
		host2.getHostModel().setNbrCPUs("8");
		host2.setLifeTime(2);
		host2.setLifetimeUnderMaxLoad(1);
		host2.setThreshold(1);
		host2.setThresholdUnderMax(4);
		host2.setEnergyPriority(Host.SUN_ENERGY_PRIORITY);
		host2.getHostModel().setAddress("10.10.10.2");
		
		
		/* host 3 config */
		Host host3 = new Host(new VmHostModel());
		
		VirtualMachine vm5 = new VirtualMachine(new VM()/*, host3*/);
		vm5.getVM().setName("5");
		vm5.getVM().setVcpu("1");
		vm5.getVM().setMemory("3000");
		vm5.getVM().setIp("10.10.10.105");
		
		VirtualMachine vm6 = new VirtualMachine(new VM()/*, host3*/);
		vm6.getVM().setName("6");
		vm6.getVM().setVcpu("3");
		vm6.getVM().setMemory("1000");
		vm6.getVM().setIp("10.10.10.106");
		
		
		
		host3.getHostModel().addVM(vm5.getVM());
		host3.getHostModel().addVM(vm6.getVM());
		host3.getHostModel().setTotalMemory("5000");
		//host3.getHostModel().setTotalMemory("15000");
		host3.getHostModel().setNbrCPUs("15");
		host3.setLifeTime(10);
		host3.setLifetimeUnderMaxLoad(7);
		host3.setThreshold(1);
		host3.setThresholdUnderMax(4);
		host3.setEnergyPriority(Host.SUN_ENERGY_PRIORITY);
		host3.getHostModel().setAddress("10.10.10.3");
		
		/* host 4 config */
		Host host4 = new Host(new VmHostModel());
		
		VirtualMachine vm7 = new VirtualMachine(new VM()/*, host4*/);
		vm7.getVM().setName("7");
		vm7.getVM().setVcpu("2");
		vm7.getVM().setMemory("3000");
		vm7.getVM().setIp("10.10.10.107");
		
		VirtualMachine vm8 = new VirtualMachine(new VM()/*, host4*/);
		vm8.getVM().setName("8");
		vm8.getVM().setVcpu("3");
		vm8.getVM().setMemory("2000");
		vm8.getVM().setIp("10.10.10.108");
		
		
		
		host4.getHostModel().addVM(vm7.getVM());
		host4.getHostModel().addVM(vm8.getVM());
		host4.getHostModel().setTotalMemory("5000");
		host4.getHostModel().setNbrCPUs("7");
		host4.setLifeTime(1);
		host4.setLifetimeUnderMaxLoad(.7);
		host4.setThreshold(1);
		host4.setThresholdUnderMax(4);
		host4.setEnergyPriority(Host.SUN_ENERGY_PRIORITY);
		host4.getHostModel().setAddress("10.10.10.4");
		
		
		/* host list config */
		HostList hostList = new HostList();
		hostList.addHost(host1);
		hostList.addHost(host2);
		hostList.addHost(host3);
		hostList.addHost(host4);
		hostList.addHost(hydroHost5);

		
		return hostList;
		
	}

}

