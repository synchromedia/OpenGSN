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
package com.greenstarnetwork.resources.facilityResource.core;

import com.greenstarnetwork.resources.facilityResource.commandset.CalculateOPSpecsCommand;
import com.greenstarnetwork.resources.facilityResource.commandset.CheckPowerCommand;
import com.greenstarnetwork.resources.facilityResource.commandset.InitializeCommand;
import com.greenstarnetwork.resources.facilityResource.commandset.SetOPHoursCommand;
import com.greenstarnetwork.resources.facilityResource.commandset.SimulatedCalculateOPSpecsCommand;
import com.greenstarnetwork.resources.facilityResource.commandset.SimulatedInitializeCommand;
import com.greenstarnetwork.resources.facilityResource.commandset.SimulatedRefreshClimateCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.ICommand;
import com.iaasframework.capabilities.commandset.ICommandFactory;

/**
 * 
 * create command instances for facility resource.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */



public class SimulatedFacilityResourceCommandFactory implements ICommandFactory{

	public ICommand createCommand(String commandId) throws CommandException {
		if (commandId.equals(SimulatedRefreshClimateCommand.COMMAND)){
			return new SimulatedRefreshClimateCommand();
		}else if (commandId.equals(CalculateOPSpecsCommand.COMMAND)){
			return new SimulatedCalculateOPSpecsCommand();
		}else if (commandId.equals(CheckPowerCommand.COMMAND)){
			return new CheckPowerCommand();
		}else if (commandId.equals(InitializeCommand.COMMAND)){
			return new SimulatedInitializeCommand();
		}else if (commandId.equals(SetOPHoursCommand.COMMAND)){
			return new SetOPHoursCommand();
		}else{
			throw new CommandException("Command "+commandId+" not found");
		}
	}

}
