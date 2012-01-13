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
package com.greenstarnetwork.resources.raritanclimate.core;

import com.greenstarnetwork.resources.raritanclimate.commandset.EchoCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.RefreshCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.RefreshTempCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.ShowSystemCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.GetHumidityCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.GetTemperatureCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.LoginCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.LogoutCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.ICommand;
import com.iaasframework.capabilities.commandset.ICommandFactory;

public class ClimateCommandFactory implements ICommandFactory {

	public ICommand createCommand(String commandId) throws CommandException {
		if (commandId.equals(LogoutCommand.COMMAND)) {
			return new LogoutCommand();

		} else if (commandId.equals(LoginCommand.COMMAND)) {
			return new LoginCommand();

		} else if (commandId.equals(GetTemperatureCommand.COMMAND)) {
			return new GetTemperatureCommand();

		} else if (commandId.equals(GetHumidityCommand.COMMAND)) {
			return new GetHumidityCommand();

		} else if (commandId.equals(ShowSystemCommand.COMMAND)) {
			return new ShowSystemCommand();

		} else if (commandId.equals(RefreshCommand.COMMAND)) {
			return new RefreshCommand();

		} else if (commandId.equals(EchoCommand.COMMAND)) {
			return new EchoCommand();

		}		
		else if (commandId.equals(RefreshTempCommand.COMMAND)) {
			return new RefreshTempCommand();

		}
		
		else {
			throw new CommandException("Command " + commandId + " not found");
		}

	}

}
