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
package com.greenstarnetwork.tests.resources.facilityResource.commandset;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.resources.facilityResource.commandset.SimulatedCalculateOPSpecsCommand;
import com.greenstarnetwork.resources.facilityResource.core.FacilityResourceToFromFile;
import com.iaasframework.capabilities.commandset.CommandException;

/**
 * Test Facility Resource commands.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public class CommandTest {

	


	
	
	@Test
	public void testCalculateOPHourCommand() throws Exception {
		FacilityResourceToFromFile facilityResourceToFromFile = new FacilityResourceToFromFile();
		FacilityModel facilityModel = facilityResourceToFromFile.readFacilityModelFromFile();
		SimulatedCalculateOPSpecsCommand simulatedCalculateOPSpecsCommand = new SimulatedCalculateOPSpecsCommand();
		simulatedCalculateOPSpecsCommand.setModel(facilityModel);
		simulatedCalculateOPSpecsCommand.executeCommandMain();
		Assert.assertEquals(facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad(), facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad(), 100000);
		Assert.assertEquals(facilityModel.getOperationalSpecs().getOpHourUnderMaximumLoad(), facilityModel.getOperationalSpecs().getOpHourUnderMaximumLoad(), 100000);
		System.out.println(facilityModel.toXML());
	}
	
	
	
	
	
}
