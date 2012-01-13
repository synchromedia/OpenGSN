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
import com.iaasframework.capabilities.commandset.AbstractCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.CommandState.State;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Set an IP address to a given VM
 * @author knguyen
 *
 */
public class SetVMIPCommand extends AbstractCommand{

	public static final String COMMAND = "SetVMIPCommand";
	
	private VmHostModel hostmodel = null;
	
	public SetVMIPCommand() {
		super(COMMAND);
	}

	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (!args.containsKey((String)"vmName"))
				throw new CommandException("Need VM name parameter for SetIP command!");
			if (!args.containsKey((String)"ip"))
				throw new CommandException("Need ip parameter (IP address) for SetIP command!");
			
			try {
				state.setState(State.RESPONSE_RECEIVED);
				requestEngineModel(false);
			} catch (Exception e) {
				throw new CommandException(COMMAND + " " + e);
			}
			
//			if (this.model == null) {
//				throw new CommandException("Cannot get VmHostModel!");
//			}
			
			VM vm = this.hostmodel.getVM((String)args.get((String)"vmName"));
			if (vm == null) {
				throw new CommandException("VM " + (String)args.get((String)"vmName") + " is not found!");
			}
			vm.setIp((String)args.get((String)"ip"));
		}
	}

	public void setModel(VmHostModel m) {
		this.hostmodel = m;
		
	}
	
	@Override
	public void initializeCommand(IResourceModel arg0) throws CommandException {
		this.hostmodel = (VmHostModel)arg0;
	}

	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException {
		this.model = hostmodel;
	}

	@Override
	public void responseReceived(ICapabilityMessage arg0)
			throws CommandException {
		// TODO Auto-generated method stub
		
	}

}
