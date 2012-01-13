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
package com.greenstarnetwork.resources.libvirt.core;

import com.greenstarnetwork.resources.libvirt.commandset.CopyTemplateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.CreateVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.CreateXMLCommand;
import com.greenstarnetwork.resources.libvirt.commandset.DestroyCommand;
import com.greenstarnetwork.resources.libvirt.commandset.EchoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.GetMACCommand;
import com.greenstarnetwork.resources.libvirt.commandset.HostRunningInfoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.MigrateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.NodeInfoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.ResumeVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.SCPCommand;
import com.greenstarnetwork.resources.libvirt.commandset.SetVMIPCommand;
import com.greenstarnetwork.resources.libvirt.commandset.SetVMStatusCommand;
import com.greenstarnetwork.resources.libvirt.commandset.ShutdownVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.StartVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.SuspendVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UndefineCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UpdateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.VMInfoCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.ICommand;
import com.iaasframework.capabilities.commandset.ICommandFactory;

/**
 * 
 * @author knguyen
 *
 */
public class vmmCommandFactory implements ICommandFactory{

	public ICommand createCommand(String commandId) throws CommandException {
		if (commandId.equals(CreateVMCommand.COMMAND)){
			return new CreateVMCommand();
		}else if (commandId.equals(DestroyCommand.COMMAND)){
			return new DestroyCommand();
		}else if (commandId.equals(EchoCommand.COMMAND)){
			return new EchoCommand();
		}else if (commandId.equals(MigrateCommand.COMMAND)){
			return new MigrateCommand();
		}else if (commandId.equals(UpdateCommand.COMMAND)){
			return new UpdateCommand();
		}else if (commandId.equals(VMInfoCommand.COMMAND)){
			return new VMInfoCommand();
		}else if (commandId.equals(StartVMCommand.COMMAND)){
			return new StartVMCommand();
		}else if (commandId.equals(ShutdownVMCommand.COMMAND)){
			return new ShutdownVMCommand();
		}else if (commandId.equals(SuspendVMCommand.COMMAND)){
			return new SuspendVMCommand();
		}else if (commandId.equals(ResumeVMCommand.COMMAND)){
			return new ResumeVMCommand();
		}else if (commandId.equals(SetVMIPCommand.COMMAND)){
			return new SetVMIPCommand();
		}else if (commandId.equals(NodeInfoCommand.COMMAND)){
			return new NodeInfoCommand();
		}else if (commandId.equals(HostRunningInfoCommand.COMMAND)){
			return new HostRunningInfoCommand();
		}else if (commandId.equals(GetMACCommand.COMMAND)){
			return new GetMACCommand();
		}else if (commandId.equals(CopyTemplateCommand.COMMAND)){
			return new CopyTemplateCommand();
		}else if (commandId.equals(CreateXMLCommand.COMMAND)){
			return new CreateXMLCommand();
		}else if (commandId.equals(UndefineCommand.COMMAND)){
			return new UndefineCommand();
		}else if (commandId.equals(SetVMStatusCommand.COMMAND)){
			return new SetVMStatusCommand();
		}else if (commandId.equals(SCPCommand.COMMAND)){//TODO EXPORT
			return new SCPCommand();
		}else{
			throw new CommandException("Command "+commandId+" not found");
		}
	}

}
