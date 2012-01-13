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
 * This class will provide tools to create an aggregated migration plan for VM migrations by using the PlanGenerator for several times on different scenarios. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import java.util.Iterator;

import com.greenstarnetwork.controller.internal.model.HostExtraInfoList;
import com.greenstarnetwork.controller.model.Host;
import com.greenstarnetwork.controller.model.HostList;
import com.greenstarnetwork.controller.model.Migration;
import com.greenstarnetwork.controller.model.MigrationPlan;
import com.greenstarnetwork.controller.model.VMList;
import com.greenstarnetwork.controller.tools.Logger;

public class AggregatedPlanGenerator {

	private Logger log = Logger.getLogger();

	/**
	 * Aggregated migration plan which is a merge of several migration plan. 
	 */
	private MigrationPlan migrationPlan;
	
	private HostList energySufficientHostsList = null;

	private HostList hydroHostList = null;

	/**
	 * 
	 * @param sunWindHostList
	 * @throws Exception
	 */
	//public void execute(HostList hostList, double minAcceptableLifeTime, double maxNecessaryLifeTime) throws Exception {
	public void execute(HostList hostList) throws Exception {

//		log.debug("AggregatedPlanGenerator executing....");
		setHydroHostList(new HostList());
		HostList sunWindHostList = new HostList();

		Iterator<Host> iterator = hostList.getHosts().iterator();
		while (iterator.hasNext()){
			Host host = iterator.next();
			if (host.getEnergyPriority()==Host.HYDRO_ENERGY_PRIORITY){
				getHydroHostList().addHost(host);
			} else {
				sunWindHostList.addHost(host);
			}
		}

//		log.debug("Hydro list: \n" + getHydroHostList().toXML());
//		log.debug("SunWind list: \n" + sunWindHostList.toXML());

		/* calculating the list of vms to migrate */
		VMsToMigrateListGenerator vmsToMigrateListGenerator = new VMsToMigrateListGenerator();
		//vmsToMigrateListGenerator.setMinAcceptableLifeTime(minAcceptableLifeTime);
		VMList vmsToMigrateList = vmsToMigrateListGenerator.getVMsToMigrateList(sunWindHostList);

//		log.debug("vmsToMigrateList list: \n" + vmsToMigrateList.toXML());

		/* calculating the list of energy sufficient hosts */
		EnergySufficientHostsListGenerator energySufficientHostsListGenerator = new EnergySufficientHostsListGenerator();
		//energySufficientHostsListGenerator.setmaxNecessaryLifeTime(maxNecessaryLifeTime);
		energySufficientHostsList = energySufficientHostsListGenerator.getEnergySufficientHostsList(sunWindHostList);

//		log.debug("energySufficientHostsList list: \n" + energySufficientHostsList.toXML());

		PlanGenerator planGenerator = new PlanGenerator();
		PlanGenerator planGenerator2 = new PlanGenerator();
		PlanGenerator planGenerator3 = new PlanGenerator();

		HostExtraInfoList hostExtraInfoList = new HostExtraInfoList();
		hostExtraInfoList.syncWith(energySufficientHostsList);

//		log.debug("hostExtraInfoList list: \n" + hostExtraInfoList.toXML());
		
		HostExtraInfoList hydroHostInternalInfoList = new HostExtraInfoList();
		hydroHostInternalInfoList.syncWith(getHydroHostList());
		
//		log.debug("hydroHostInternalInfoList list: \n" + hydroHostInternalInfoList.toXML());

		planGenerator.execute(energySufficientHostsList, hostExtraInfoList,vmsToMigrateList,sunWindHostList);
		VMList nonHostedVMsToMigrateList = planGenerator.getNonHostedVMs();

//		log.debug("nonHostedVMsToMigrateList list: \n" + nonHostedVMsToMigrateList.toXML());
		
		if ((nonHostedVMsToMigrateList.getVMs()!=null)&&(nonHostedVMsToMigrateList.getVMs().iterator().hasNext())){
			planGenerator2.execute(getHydroHostList(), hydroHostInternalInfoList,nonHostedVMsToMigrateList, sunWindHostList);
		} else {
			VMsToMigrateListGenerator vmsToMigrateListGenerator2 = new VMsToMigrateListGenerator();
			//vmsToMigrateListGenerator2.setMinAcceptableLifeTime(999999999999999999999999999999999999999999999.99);

			VMList hydroVMList = vmsToMigrateListGenerator2.getVMsToMigrateList(getHydroHostList());

//			log.debug("hydroVMList list: \n" + hydroVMList.toXML());

			if ((hydroVMList.getVMs()!=null)&&(hydroVMList.getVMs().iterator().hasNext())){
				planGenerator3.execute(energySufficientHostsList, hostExtraInfoList,hydroVMList, getHydroHostList());
			}
		}

		migrationPlan = planGenerator.getMigrationPlan();
		if (planGenerator2.getMigrationPlan()!=null && planGenerator2.getMigrationPlan().getPlan()!=null ){
			if (migrationPlan==null){
				migrationPlan = planGenerator2.getMigrationPlan();
			}else{
				migrationPlan.append(planGenerator2.getMigrationPlan());
			}
		}
		if (planGenerator3.getMigrationPlan()!=null && planGenerator3.getMigrationPlan().getPlan()!=null){
			if (migrationPlan==null){
				migrationPlan = planGenerator3.getMigrationPlan();
			}else{
				migrationPlan.append(planGenerator3.getMigrationPlan());
			}
		}

		if(migrationPlan == null || migrationPlan.getPlan() == null || migrationPlan.getPlan().size() == 0 )
			System.out.println("empty migration plan");

		else {
			migrationPlan.setupIterator();
			while (migrationPlan.hasNextMigration()) {
				Migration mig1 = migrationPlan.getNextMigration();
				String message = "Planner :: "+mig1.getVirtualMachine()+" MIGRATE FROM "+mig1.getSourceHost()+" TO "+mig1.getDestinationHost(); 
				System.out.println(message);
				log.debug(message);

			}
		}

	}

	/**
	 * 
	 * @return
	 */
	public MigrationPlan getMigrationPlan() {
		return migrationPlan;
	}



	public HostList getEnergySufficientHostsList() {
		return energySufficientHostsList;
	}

	/**
	 * @param hydroHostList the hydroHostList to set
	 */
	public void setHydroHostList(HostList hydroHostList) {
		this.hydroHostList = hydroHostList;
	}

	/**
	 * @return the hydroHostList
	 */
	public HostList getHydroHostList() {
		return hydroHostList;
	}

}
