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
package com.greenstarnetwork.resources.facilityResource.commandset;

import com.greenstarnetwork.resources.facilityResource.core.OPSpecsCalculator;
import com.greenstarnetwork.resources.facilityResource.core.SimulatedOPSpecsCalculator;


/**
 *
 *This command will calculate the operating hour left on the facility according to the consumption of the facility. 
 *
 *@author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public class SimulatedCalculateOPSpecsCommand extends CalculateOPSpecsCommand {

	public SimulatedCalculateOPSpecsCommand() {
		super();
	}

	@Override
	public OPSpecsCalculator createOPSpecsCalculator() {
		return new SimulatedOPSpecsCalculator(facilityModel);
	}

}