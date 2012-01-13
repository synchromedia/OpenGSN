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
package com.greenstarnetwork.tests.services.controller;

/**
 * This class will test the PlanGenerator class. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */



import org.junit.Test;

import com.greenstarnetwork.services.controller.generator.BasicGenerator;
import com.greenstarnetwork.services.controller.internal.model.HostExtraInfoList;
import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.Migration;
import com.greenstarnetwork.services.controller.model.MigrationPlan;
import com.greenstarnetwork.services.controller.model.VMList;
import com.greenstarnetwork.services.controller.plan.EnergySufficientHostsListGenerator;
import com.greenstarnetwork.services.controller.plan.PlanGenerator;
import com.greenstarnetwork.services.controller.plan.VMsToMigrateListGenerator;
import com.iaasframework.core.internal.persistence.utilities.Assert;


public class PlanGeneratorTest{
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPlanGenerator() throws Exception {
		
		
		BasicGenerator basicGenerator = new BasicGenerator();
		
		HostList hostList = basicGenerator.getBasicHosts();
		
		/* calculating the list of vms to migrate */
		VMsToMigrateListGenerator vmsToMigrateListGenerator = new VMsToMigrateListGenerator();
		//vmsToMigrateListGenerator.setMinAcceptableLifeTime(3.00);
		VMList vmsToMigrateList = vmsToMigrateListGenerator.getVMsToMigrateList(hostList);
		
		/* calculating the list of energy sufficient hosts */
		EnergySufficientHostsListGenerator energySufficientHostsListGenerator = new EnergySufficientHostsListGenerator();
		//energySufficientHostsListGenerator.setmaxNecessaryLifeTime(4.00);
		HostList energySufficientHostsList = energySufficientHostsListGenerator.getEnergySufficientHostsList(hostList);
		
		HostExtraInfoList hostExtraInfoList = new HostExtraInfoList();
		hostExtraInfoList.syncWith(energySufficientHostsList);
		
		PlanGenerator planGenerator = new PlanGenerator();
		planGenerator.execute(energySufficientHostsList, hostExtraInfoList,vmsToMigrateList, hostList);
		
		MigrationPlan migrationPlan = planGenerator.getMigrationPlan();
		
		migrationPlan.setupIterator();
		while (migrationPlan.hasNextMigration()) {
			Migration mig1 = migrationPlan.getNextMigration();
			System.out.println(mig1.getVirtualMachine()+" MIGRATE FROM "+mig1.getSourceHost()+" TO "+mig1.getDestinationHost());
		}
		Assert.notNull(migrationPlan, "migration plan is null");
	}
}
