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

import java.util.Map;

import com.iaasframework.capabilities.commandset.CommandException;



/**
 *
 *This command will calculate the operating hour left on the facility according to the consumption of the facility. 
 *
 *@author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public class SetOPHoursCommand extends BasicCommand {

	public static final String COMMAND = "SetOPHoursCommand";

	public SetOPHoursCommand() {
		super(COMMAND);
	}

	

	public void executeCommandMain() throws Exception {
		Map<Object, Object> args = this.commandRequestMessage.getArguments();
		if (!args.containsKey((String)"opHourUnderCurrentLoad"))
			throw new CommandException("can not find opHourUnderCurrentLoad in the args.");
		if (!args.containsKey((String)"opHourUnderMaximumLoad"))
			throw new CommandException("can not find opHourUnderMaximumLoad in the args.");

		String opHourUnderCurrentLoad = (String)args.get((String)"opHourUnderCurrentLoad");
		String opHourUnderMaximumLoad = (String)args.get((String)"opHourUnderMaximumLoad");
		facilityModel.getOperationalSpecs().setOpHourUnderCurrentLoad(Double.valueOf(opHourUnderCurrentLoad));
		facilityModel.getOperationalSpecs().setOpHourUnderMaximumLoad(Double.valueOf(opHourUnderMaximumLoad));
		facilityModel.getOperationalSpecs().setOnGrid("FALSE");
		if (facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad() < 
				Double.valueOf(facilityModel.getOperationalSpecs().getOpHourThreshold()))
			facilityModel.getOperationalSpecs().setStatus("NONGREEN");
		else if (facilityModel.getOperationalSpecs().getOpHourUnderMaximumLoad() > 
				Double.valueOf(facilityModel.getOperationalSpecs().getOpHourThresholdUnderMax()))
			facilityModel.getOperationalSpecs().setStatus("GREEN");
	}



	


}