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
package com.greenstarnetwork.resources.libvirt.commandset;

import java.util.Map;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * 
 * @author knguyen
 *
 */
public class MigrateCommand extends BasicCommand {
	
	public static final String COMMAND = "MigrateCommand";	//Query command ID
	private String hypervisor = null;
	private String vmName = null;
	private VmHostModel hostmodel = null;

	public MigrateCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
		
			//Destination engineID
			if (!args.containsKey((String)"destHost"))
				throw new CommandException("Need destination host parameter for Migrate command!");
			
			if (!args.containsKey((String)"vmName"))
				throw new CommandException("Need VM name parameter for Migrate command!");
			vmName = (String) args.get("vmName");
			VM vm = hostmodel.getVM(vmName);
			if (vm == null)
				throw new CommandException("VM: " + vmName + "not found!");
//			requestEngineModel(false);
//			vm.setStatus(VMStatus.MIGRATING);
//			this.model = hostmodel;
			
			String destHost = (String) args.get("destHost");
		
			String command = "virsh -c qemu:///system migrate --live " + vmName + " " + hypervisor + 
					"+ssh://$USER@" + destHost + "/system";// + " ; virsh undefine " + vmName;
			sendCommandToProtocol(command);
		}
	}
	
	@Override
	public void initializeCommand(IResourceModel arg0) throws CommandException {
		hostmodel = (VmHostModel)arg0;
		hypervisor = hostmodel.getHypervisor();
		if ( (hypervisor == null) || hypervisor.isEmpty() ) 
			hypervisor = "qemu";
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if (s.indexOf("error:") > -1)
		{
			System.err.println("Migration error response: " + s);
			if(s.indexOf("cannot recv data: Connection reset by peer") < 0 )		//otherwise, retry migration
				throw new CommandException("MigrateCommand exception: " + s);
		}else
			((VmHostModel)model).removeVM(vmName);
	}
}