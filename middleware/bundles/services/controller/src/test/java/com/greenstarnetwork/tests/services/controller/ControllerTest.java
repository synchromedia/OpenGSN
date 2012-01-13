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

import org.junit.Test;

import com.greenstarnetwork.services.controller.core.ControllerException;
import com.greenstarnetwork.services.controller.core.ControllerImpl;
import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.core.Logger;
import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.LinkTable;
import com.greenstarnetwork.services.controller.model.MigrationPlan;
import com.iaasframework.core.internal.persistence.utilities.Assert;

public class ControllerTest {

	private Logger log = Logger.getLogger();
	
	@Test
	public void testController() {
		IController controller = new ControllerImpl();
		controller.generateHostList(4);
		
		HostList hosts = controller.getHostList();
//		System.err.println(hosts.toXML());
		log.debug(hosts.toXML());
		
		LinkTable links = controller.getLinkTable();
//		System.err.println(links.toXML());
		log.debug(links.toXML());
		
		try {
			controller.generatePlan();
			MigrationPlan plan = controller.getMigrationPlan();
			controller.executePlan();
			Assert.notNull(plan, "plan is null !!");
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
