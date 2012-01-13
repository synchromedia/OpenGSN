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
package com.greenstarnetwork.resources.eatonPW.core;

import com.greenstarnetwork.resources.eatonPW.commandset.OutletCommand;
import com.greenstarnetwork.resources.eatonPW.commandset.SetOutletConsumerCommand;
import com.greenstarnetwork.resources.eatonPW.commandset.SysDescrCommand;
import com.greenstarnetwork.resources.eatonPW.commandset.UnitCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.ICommand;
import com.iaasframework.capabilities.commandset.ICommandFactory;

public class EatonCommandFactory implements ICommandFactory{

	public ICommand createCommand(String commandId) throws CommandException 
	{
		if (commandId.equals(SysDescrCommand.COMMAND)){
			return new SysDescrCommand();
		}else if (commandId.equals(OutletCommand.COMMAND)){
			return new OutletCommand();
		}else if (commandId.equals(UnitCommand.COMMAND)){
			return new UnitCommand();
		}else if (commandId.equals(SetOutletConsumerCommand.COMMAND)){
			return new SetOutletConsumerCommand();
		}else{
			throw new CommandException("Command "+commandId+" not found");
		}
	}

}