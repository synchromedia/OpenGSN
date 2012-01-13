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
package com.greenstarnetwork.resources.servertechCWG.core;

import com.greenstarnetwork.resources.servertechCWG.commandset.IStatCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.OStatCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.OffCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.OnCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.SetOutletConsumerCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.StatusCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.ToggleCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.ICommand;
import com.iaasframework.capabilities.commandset.ICommandFactory;

/**
 * 
 * @author knguyen
 *
 */
public class ServerTechCommandFactory implements ICommandFactory{

	public ICommand createCommand(String commandId) throws CommandException {
		if (commandId.equals(IStatCommand.COMMAND)){
			return new IStatCommand();
		}else if (commandId.equals(OStatCommand.COMMAND)){
			return new OStatCommand();
		}else if (commandId.equals(StatusCommand.COMMAND)){
			return new StatusCommand();
		}else if (commandId.equals(OnCommand.COMMAND)){
			return new OnCommand();
		}else if (commandId.equals(OffCommand.COMMAND)){
			return new OffCommand();
		}else if (commandId.equals(ToggleCommand.COMMAND)){
			return new ToggleCommand();
		}else if (commandId.equals(SetOutletConsumerCommand.COMMAND)){
			return new SetOutletConsumerCommand();
		}else{
			throw new CommandException("Command "+commandId+" not found");
		}
	}

}