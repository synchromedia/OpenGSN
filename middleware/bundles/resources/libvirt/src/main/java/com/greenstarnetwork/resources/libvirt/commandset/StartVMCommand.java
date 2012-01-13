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

import java.util.List;
import java.util.Map;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VMStatus;
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
public class StartVMCommand extends BasicCommand {
	
	public static final String COMMAND = "StartVMCommand";
	private String vmName = null;
	private List<VM> vmlist = null;
	
	public StartVMCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			vmName = (String)args.get((String)"vmName");
			if (vmName == null)
				throw new CommandException("Need VM name parameter for StartVM command!");
			boolean found = false;
			VMStatus vmState = VMStatus.STOPPED;
			for (int i=0; i < vmlist.size(); i++) {
				if (vmName.equals(vmlist.get(i).getName())) {
					found = true;
					vmState = vmlist.get(i).getStatus();
					break;
				}
			}
			if (!found)
				throw new CommandException("VM: " + vmName + "not found!");
			
			if (vmState != VMStatus.STOPPED){
//				throw new CommandException("VM: " + vmName + " is running!");
			}

			sendCommandToProtocol("virsh -c qemu:///system start " + vmName);
		}
	
	}
	
	@Override
	public void initializeCommand(IResourceModel model) throws CommandException {
		vmlist = ((VmHostModel)model).getVMList();
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if (s.indexOf("error:") > -1)
		{
			System.err.println("StartVMCommand: " + s);
			if (s.indexOf("error: Domain is already active") < -1)
				throw new CommandException("StartVMCommand exception: " + s);
		}
		
		((VmHostModel)model).getVM(vmName).setStatus(VMStatus.STARTED);
	}

}