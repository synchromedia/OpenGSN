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
 * This class will provide tools for creating a migration plan. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.opengsn.controller.internal.model.HostExtraInfoList;
import com.opengsn.controller.internal.model.HostInternalCoreBased;
import com.opengsn.controller.internal.model.VMInternalCoreBased;
import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;
import com.opengsn.controller.model.Migration;
import com.opengsn.controller.model.MigrationPlan;
import com.opengsn.controller.model.VMList;
import com.opengsn.controller.model.VirtualMachine;
import com.opengsn.services.cloudmanager.model.VM;


public class PlanGenerator {

	/**
	 * A data structure to store the VMs data internally.
	 * There is two type of VMInternal which can be used here: CoreBased and MemoryBased which affect of the way VMs and Hosts will sort for assigning to each other.
	 * Here CoreBased is used because we assume that number of cores are more limited than memory otherwise MemoryBased should used. 
	 */
	VMInternalCoreBased[] vm = new VMInternalCoreBased[1000000];

	/**
	 * A data structure to store the Hosts data internally.
	 * There is two type of VMInternal which can be used here: CoreBased and MemoryBased which affect of the way VMs and Hosts will sort for assigning to each other.
	 * Here CoreBased is used because we assume that number of cores are more limited than memory otherwise MemoryBased should used. 
	 */
	HostInternalCoreBased[] host= new HostInternalCoreBased[1000000];

	/**
	 * Number of VMs.
	 */
	int vm_num;

	/**
	 * Number of Hosts.
	 */
	int host_num;

	/**
	 * To store the result migration plan.
	 */
	private MigrationPlan migrationPlan;

	private boolean byPassed = false;

	private VMList nonHostedVMsEqualToVMsToMigrateVMList = null;
	
	

	/**
	 * 
	 */
	private void generatePlan() {
		for (int h=0;h<host_num;h++){
			for (int v=0;v<vm_num;v++){
				if (vm[v].getDestinatinHost()==null){
					if ((host[h].getCore()>=vm[v].getCore())&&(host[h].getMemory()>=vm[v].getMemory())){
						host[h].setCore(host[h].getCore()-vm[v].getCore());
						host[h].setMemory(host[h].getMemory()-vm[v].getMemory());
						vm[v].setDestinatinHost(host[h].getHostRef());
						host[h].getHostInternalInfoRef().setReservedCore(host[h].getHostInternalInfoRef()
								.getReservedCore()
								+ vm[v].getCore());
						host[h].getHostInternalInfoRef().setReservedMemory(host[h].getHostInternalInfoRef()
								.getReservedMemory()
								+ vm[v].getMemory());
					}
				}
			}
		}
	}

	/**
	 * This function will fetch the data from HostList and VMList to internal data structures and will sort them. 
	 * 
	 * @param destinationHostsList
	 * @param vmsToMigrateList
	 * @throws Exception
	 */
	private void fetchData(HostList destinationHostsList, HostExtraInfoList hostExtraInfoList, VMList vmsToMigrateList, HostList allHostsList) throws Exception {

		if (destinationHostsList==null || destinationHostsList.getHosts()==null ||
				vmsToMigrateList==null || vmsToMigrateList.getVMs()==null){
			throw new Exception("EnergySufficientHostsList or VMList vmsToMigrateList is empty");
		}

		host_num=0;
		Iterator<Host> iterator = destinationHostsList.getHosts().iterator();
		while (iterator.hasNext()) {
			Host host1 = (Host)iterator.next();
			HostInternalCoreBased hostInternalCoreBased = new HostInternalCoreBased(host1,hostExtraInfoList.getHost(host1.getHostModel().getAddress()),Integer.parseInt(host1.getHostModel().getNbrCPUs())-hostExtraInfoList.getHost(host1.getHostModel().getAddress()).getReservedCore(),Integer.parseInt(host1.getHostModel().getTotalMemory())-hostExtraInfoList.getHost(host1.getHostModel().getAddress()).getReservedMemory()); 
			this.host[host_num]= hostInternalCoreBased;
			host_num++;
		}

		vm_num=0;
		Iterator<VirtualMachine> iterator2 = vmsToMigrateList.getVMs().iterator();
		while (iterator2.hasNext()) {
			VirtualMachine virtualMachine = (VirtualMachine)iterator2.next();
			this.vm[vm_num]=new VMInternalCoreBased(virtualMachine,Integer.parseInt(virtualMachine.getVM().getVcpu()),Integer.parseInt(virtualMachine.getVM().getMemory()),FindHost(virtualMachine, allHostsList));
			vm_num++;
		}

		Arrays.sort(vm,0,vm_num, Collections.reverseOrder());
		Arrays.sort(host,0,host_num, Collections.reverseOrder());
	}

	private Host FindHost(VirtualMachine virtualMachine, HostList allHostsList) {
		Iterator<Host> iterator = allHostsList.getHosts().iterator();
		while (iterator.hasNext()) {
			Host host1 = (Host)iterator.next();
			Iterator<VM> iterator2 = host1.getHostModel().getVMList().iterator();
			while (iterator2.hasNext()) {
				VM vm1 = (VM)iterator2.next();
				if (vm1==virtualMachine.getVM()){
					return host1;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 */
	private void populateMigrationPlan() {
		this.migrationPlan = new MigrationPlan();
		for (int i=0;i<vm_num;i++){
			if (vm[i].getDestinatinHost()!=null){
				Migration migration = new Migration();
				migration.setDestinationHost(vm[i].getDestinatinHost());
				migration.setSourceHost(vm[i].getSourceHost());
				migration.setVirtualMachine(vm[i].getVirtualMachineRef());
				this.migrationPlan.addMigration(migration);
			}
		}


	}

	/**
	 * 
	 * @param destinationHostsList
	 * @param vmsToMigrateList
	 * @throws Exception
	 */
	public void execute(HostList destinationHostsList, HostExtraInfoList hostExtraInfoList,VMList vmsToMigrateList, HostList allHostsList) throws Exception {
		if (!(destinationHostsList==null || destinationHostsList.getHosts()==null ||
				vmsToMigrateList==null || vmsToMigrateList.getVMs()==null)){
			fetchData(destinationHostsList, hostExtraInfoList,vmsToMigrateList,allHostsList);
			decreaseCurrentUsageFromCapacity();
			generatePlan();
			populateMigrationPlan();
		}else{
			byPassed = true;
			nonHostedVMsEqualToVMsToMigrateVMList = vmsToMigrateList;
		}
		
	}

	/**
	 * This function will calculate the amount of available resources for each host by decreasing the amount of already used resources from the total.
	 */
	private void decreaseCurrentUsageFromCapacity() {
		for (int h=0;h<host_num;h++){
			Host host1 = this.host[h].getHostRef();
			List<VM> vmList1 = host1.getHostModel().getVMList();
			Iterator<VM> iterator1 = vmList1.iterator();
			while (iterator1.hasNext()) {
				VM virtualMachine1 = (VM)iterator1.next();
				host[h].setCore(host[h].getCore()-Integer.parseInt(virtualMachine1.getVcpu()));
				host[h].setMemory(host[h].getMemory()-Integer.parseInt(virtualMachine1.getMemory()));
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public MigrationPlan getMigrationPlan() {
		return this.migrationPlan;
	}

	/**
	 * 
	 * @return the VMs which this plan generator was not able to host on the host list.
	 */
	public VMList getNonHostedVMs() {
		if (!byPassed ){
		VMList vmList = new VMList();
		for (int i=0;i<vm_num;i++){
			if (vm[i].getDestinatinHost() == null){
				vmList.addVM(vm[i].getVirtualMachineRef());
			}
		}
		return vmList;}else{
			return nonHostedVMsEqualToVMsToMigrateVMList ;
		}
	}

}

