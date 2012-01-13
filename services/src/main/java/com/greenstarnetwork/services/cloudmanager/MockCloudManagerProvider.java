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
package com.greenstarnetwork.services.cloudmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.greenstarnetwork.services.cloudmanager.model.Host;
import com.greenstarnetwork.services.cloudmanager.model.VirtualMachine;
import com.greenstarnetwork.services.networkmanager.model.VMNetAddress;

public class MockCloudManagerProvider implements ICloudManagerProvider {
	private Map<String,Host> hosts = new HashMap<String,Host>();
	public VMNetAddress getNetworkAddress(String vmName) {
		VMNetAddress vmnet = new VMNetAddress();
		vmnet.setAssigned(true);
		vmnet.setName("vmname");
		vmnet.setIp("10.0.0.1");
		vmnet.setMac("EF:EF:EF:EF:EF:EF");
		return vmnet;
	}

	public List<Host> listAllHosts() {
		Host host1 = new Host();
		host1.setResourceId("host1");
		host1.setProcessorSpeed(3000);
		host1.setMaxVM(10);
		host1.setTotalMemory(1000);
		host1.setTotalProcessors(4);
		hosts.put("host1",host1);
		List list = new ArrayList(hosts.values());
		return list;
	}

	public Host getHostInformation(String id) {
		Host host1 = new Host();
		host1.setMaxVM(4);
		host1.setProcessorSpeed(3000);
		host1.setIp("10.0.0.1");
		host1.setTotalMemory(65535);
		host1.setHostPriority(0);
		host1.setTotalProcessors(4);
		VirtualMachine vmac1 = new VirtualMachine();
		vmac1.setName("vmname");
		List<VirtualMachine> list = new LinkedList<VirtualMachine>();
		list.add(vmac1);
		host1.setVMs(list);
		return host1; 
	}
	
	public String execute(String id, String name, Map<String,String> payload) {
		System.out.println("Executing Engine "+id+" Command: " + name + " with arguments: "+payload.toString());
		return "";
	}

	@Override
	public List<Host> hostQuery(String ip, String location, String freeMemory, String availableCPU, String vmUUID) {
		return listAllHosts();
	}
}
