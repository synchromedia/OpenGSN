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
package com.greenstarnetwork.controller.testharness.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.greenstarnetwork.controller.model.Host;
import com.greenstarnetwork.controller.model.HostList;
import com.greenstarnetwork.controller.model.VirtualMachine;
import com.greenstarnetwork.controller.testharness.IFacility;
import com.greenstarnetwork.controller.testharness.VirtualHydroFacility;
import com.greenstarnetwork.controller.testharness.VirtualSolarFacility;
import com.greenstarnetwork.controller.testharness.exceptions.FacilityPersistenceException;
import com.greenstarnetwork.controller.testharness.vmmodel.Memtest86VMHarness;
import com.greenstarnetwork.services.cloudmanager.model.VmHostModel;

public class VirtualFacilitiesGenerator extends FacilitySerializer {
		
	public static void main(String[] args) throws IOException {
		
		//crc facility
		Host Host1 = new Host(new VmHostModel());
		
		VirtualMachine vm11 = new VirtualMachine(new Memtest86VMHarness());
		vm11.getVM().setName("5c85573d-78e1-4c7f-8ae9-16b12b34e581");
		vm11.getVM().setVcpu("4");
		vm11.getVM().setMemory("1024");
		vm11.getVM().setIp("10.0.0.2");
		
		VirtualMachine vm12 = new VirtualMachine(new Memtest86VMHarness());
		vm12.getVM().setName("e2fbb0b3-98ad-4334-9ca3-4d0c1bff3a7e");
		vm12.getVM().setVcpu("2");
		vm12.getVM().setMemory("512");
		vm12.getVM().setIp("10.0.0.3");
		
		VirtualMachine vm13 = new VirtualMachine(new Memtest86VMHarness());
		vm13.getVM().setName("ff632a4c-b754-4291-8d4a-bf424e702149");
		vm13.getVM().setVcpu("3");
		vm13.getVM().setMemory("1024");
		vm13.getVM().setIp("10.0.0.4");
		
		VirtualMachine vm14 = new VirtualMachine(new Memtest86VMHarness());
		vm14.getVM().setName("c196a3f3-a917-416e-a360-04510856e127");
		vm14.getVM().setVcpu("2");
		vm14.getVM().setMemory("768");
		vm14.getVM().setIp("10.0.0.5");
		
		Host1.getHostModel().addVM(vm11.getVM());
		Host1.getHostModel().addVM(vm12.getVM());
		Host1.getHostModel().addVM(vm13.getVM());
		Host1.getHostModel().addVM(vm14.getVM());
		
		Host1.getHostModel().setTotalMemory("48000");
		Host1.getHostModel().setCpu("32");
		Host1.setResourceID("CRC-gsn");
		Host1.setLifeTime(1000000);
		Host1.setLifetimeUnderMaxLoad(1000000);
		Host1.setThreshold(1);
		Host1.setThresholdUnderMax(4);
		Host1.setEnergyPriority(Host.SUN_ENERGY_PRIORITY);
		Host1.getHostModel().setAddress("10.0.0.1");
		
		HostList hostList1 = new HostList();
		hostList1.addHost(Host1);
		
		VirtualSolarFacility vsf1 = new VirtualSolarFacility("CRC", hostList1, 1.5, 120.0, "America/New_York", System.currentTimeMillis() );
		//end of crc facility
		
		//cybera facility
		Host Host2 = new Host(new VmHostModel());
		
		VirtualMachine vm21 = new VirtualMachine(new Memtest86VMHarness());
		vm21.getVM().setName("08669d3a-272d-4152-8e09-7e99ffa33dad");
		vm21.getVM().setVcpu("4");
		vm21.getVM().setMemory("2048");
		vm21.getVM().setIp("10.0.0.12");
		
		VirtualMachine vm22 = new VirtualMachine(new Memtest86VMHarness());
		vm22.getVM().setName("6ca9e5a2-3148-403a-8cc1-a4608b2599cf");
		vm22.getVM().setVcpu("2");
		vm22.getVM().setMemory("512");
		vm22.getVM().setIp("10.0.0.13");
		
		VirtualMachine vm23 = new VirtualMachine(new Memtest86VMHarness());
		vm23.getVM().setName("5212d7f7-113b-4b74-ad93-6362d8f779f2");
		vm23.getVM().setVcpu("3");
		vm23.getVM().setMemory("1024");
		vm23.getVM().setIp("10.0.0.14");
		
		VirtualMachine vm24 = new VirtualMachine(new Memtest86VMHarness());
		vm24.getVM().setName("4da1af8d-6823-405e-b3d4-96f259cce257");
		vm24.getVM().setVcpu("2");
		vm24.getVM().setMemory("768");
		vm24.getVM().setIp("10.0.0.15");
		
		Host2.getHostModel().addVM(vm21.getVM());
		Host2.getHostModel().addVM(vm22.getVM());
		Host2.getHostModel().addVM(vm23.getVM());
		Host2.getHostModel().addVM(vm24.getVM());
		
		Host2.getHostModel().setTotalMemory("48000");
		Host2.getHostModel().setCpu("32");
		Host2.setResourceID("Calgary-gsn");
		Host2.setLifeTime(1000000);
		Host2.setLifetimeUnderMaxLoad(1000000);
		Host2.setThreshold(1);
		Host2.setThresholdUnderMax(4);
		Host2.setEnergyPriority(Host.SUN_ENERGY_PRIORITY);
		Host2.getHostModel().setAddress("10.0.0.11");
		
		HostList hostList2 = new HostList();
		hostList2.addHost(Host2);
		
		VirtualSolarFacility vsf2 = new VirtualSolarFacility("Cybera", hostList2, 1.5, 120.0, "America/Edmonton", System.currentTimeMillis() );
		//end of cybera facility
		
		//ets facility
		Host Host3 = new Host(new VmHostModel());
		
		VirtualMachine vm31 = new VirtualMachine(new Memtest86VMHarness());
		vm31.getVM().setName("81bde757-a721-4412-bca9-e6f610d61cea");
		vm31.getVM().setVcpu("2");
		vm31.getVM().setMemory("512");
		vm31.getVM().setIp("10.0.0.22");
		
		VirtualMachine vm32 = new VirtualMachine(new Memtest86VMHarness());
		vm32.getVM().setName("d5272a4f-0306-4544-b2ce-721c9f45eec6");
		vm32.getVM().setVcpu("2");
		vm32.getVM().setMemory("768");
		vm32.getVM().setIp("10.0.0.23");
		
		Host3.getHostModel().addVM(vm31.getVM());
		Host3.getHostModel().addVM(vm32.getVM());
		Host3.getHostModel().setTotalMemory("48000");
		Host3.getHostModel().setCpu("48");
		Host3.setResourceID("ETS-gsn");
		Host3.setLifeTime(Double.MAX_VALUE);
		Host3.setLifetimeUnderMaxLoad(Double.MAX_VALUE);
		Host3.setThreshold(1);
		Host3.setThresholdUnderMax(4);
		Host3.setEnergyPriority(Host.HYDRO_ENERGY_PRIORITY);
		Host3.getHostModel().setAddress("10.0.0.21");
		
		HostList hostList3 = new HostList();
		hostList3.addHost(Host3);
		
		VirtualHydroFacility vhf1 = new VirtualHydroFacility("ETS", hostList3, 1.5, 120.0, "America/New_York", System.currentTimeMillis() );
		//end of ets facility
		
		ArrayList<IFacility> facilities = new ArrayList<IFacility>();
		facilities.add(vsf1);
		facilities.add(vsf2);
		facilities.add(vhf1);
		
		try {
			saveFacilities(facilities, System.currentTimeMillis());
		} catch (FacilityPersistenceException e) {
			e.printStackTrace();
		}
		
		/*
		try {
			saveFacilities(facilities, 0);
		} catch (FacilityPersistenceException e) {
			e.printStackTrace();
		}*/

		/*
		ArrayList<VirtualSolarFacility> facilities = new ArrayList<VirtualSolarFacility>();
		facilities.add(vsf1);
		facilities.add(vsf2);
		
		String json = gson.toJson(facilities);
		System.out.println(json);  
		
		Type type = new TypeToken<List<VirtualSolarFacility>>() {}.getType();		
		List<VirtualSolarFacility> aux = gson.fromJson(json, type);
		
		System.out.println(aux.size());
		System.out.println(aux.get(0).getHostList().getHost("10.0.0.1").getResourceID());
		System.out.println(aux.get(1).getHostList().getHost("10.0.0.11").getHostModel().getVMList().get(1).getClass().getName());
		*/
		
	}
	
	
}
