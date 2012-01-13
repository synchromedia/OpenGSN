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
package com.greenstarnetwork.tests.services.controller.testharness;

import org.junit.Test;

import com.greenstarnetwork.services.controller.core.ControllerImpl;
import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.core.testharness.FacilityOpHourSpec;
import com.greenstarnetwork.services.controller.core.testharness.FileFacilityLocator;
import com.greenstarnetwork.services.controller.core.testharness.IFacilityLocator;

public class ControllerRunnerTest {

	@Test
	public void runController() throws Exception {
		IController controller = new ControllerImpl();
		IFacilityLocator facilityLocator = new FileFacilityLocator();
		FacilityOpHourSpec opSpec = new FacilityOpHourSpec(2.0, 5.0);
		
		//uncomment to run controller testharness as a unit test
		/*
		ControllerRunner cRunner = new ControllerRunner(controller, facilityLocator, System.currentTimeMillis(), 3600, opSpec);
		
		Thread.sleep(3600*1000);
		
		cRunner.stop();
		*/
		
	}

}
