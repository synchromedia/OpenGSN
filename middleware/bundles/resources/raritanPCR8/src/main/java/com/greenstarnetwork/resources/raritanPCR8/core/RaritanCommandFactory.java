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
package com.greenstarnetwork.resources.raritanPCR8.core;

import com.greenstarnetwork.resources.raritanPCR8.commandset.EchoCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.OffCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.OnCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ResetPowerCurrentCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.SetOutletConsumerCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowCurrSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowOutletCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowPowerSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowSystemCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowVoltSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ToggleCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.ICommand;
import com.iaasframework.capabilities.commandset.ICommandFactory;

public class RaritanCommandFactory implements ICommandFactory{

	public ICommand createCommand(String commandId) throws CommandException 
	{
		if (commandId.equals(EchoCommand.COMMAND)){
			return new EchoCommand();
		}else if (commandId.equals(ShowSystemCommand.COMMAND)){
			return new ShowSystemCommand();
		}else if (commandId.equals(ShowOutletCommand.COMMAND)){
			return new ShowOutletCommand();
		}else if (commandId.equals(ShowCurrSensorCommand.COMMAND)){
			return new ShowCurrSensorCommand();
		}else if (commandId.equals(ShowVoltSensorCommand.COMMAND)){
			return new ShowVoltSensorCommand();
		}else if (commandId.equals(ShowPowerSensorCommand.COMMAND)){
			return new ShowPowerSensorCommand();
		}else if (commandId.equals(OnCommand.COMMAND)){
			return new OnCommand();
		}else if (commandId.equals(OffCommand.COMMAND)){
			return new OffCommand();
		}else if (commandId.equals(ToggleCommand.COMMAND)){
			return new ToggleCommand();
		}else if (commandId.equals(ResetPowerCurrentCommand.COMMAND)){
			return new ResetPowerCurrentCommand();
		}else if (commandId.equals(SetOutletConsumerCommand.COMMAND)){
			return new SetOutletConsumerCommand();
		}else{
			throw new CommandException("Command "+commandId+" not found");
		}
	}

}