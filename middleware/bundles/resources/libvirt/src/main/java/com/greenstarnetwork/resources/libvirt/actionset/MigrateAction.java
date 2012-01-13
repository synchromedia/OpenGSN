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
package com.greenstarnetwork.resources.libvirt.actionset;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VMStatus;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.resources.libvirt.commandset.EchoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.MigrateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.SetVMStatusCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UndefineCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UpdateCommand;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.capabilities.actionset.api.ActionResponseMessage;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.resources.core.capability.CapabilityException;

/**
 * Migrate a VM to a remote host
 * This action includes two steps:
 *    - Send migration command to the source host
 *    - Update the data model of the target host 
 * @author knguyen
 *
 */
public class MigrateAction extends BasicAction{

	public static final String ACTION = "MigrateAction";
	public static final int MAX_RETRY = 10;					//maximum number of retries
	
	private Logger logger = LoggerFactory.getLogger(MigrateAction.class);
	
	private String vm_orig_name = null;
	
	private int retry = 0;

	public MigrateAction() {
		super(ACTION);
		initializeCommandsList();
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add(SetVMStatusCommand.COMMAND);
		commandsList.add(MigrateCommand.COMMAND);
		commandsList.add(UpdateCommand.COMMAND);
		commandsList.add(EchoCommand.COMMAND);
		state.setSteps(commandsList);
	}
	
	@Override
	public void executeAction() throws ActionException
	{
		extractResourceIdFromArgument();
		try {
			if (vm_orig_name == null)
			{
				Map<Object, Object> args = this.actionRequest.getArguments();
				vm_orig_name = (String)args.get((String)"vmName");
				args.put("status", "MIGRATING");
				this.actionRequest.setArguments(args);
			}
			String nextCommand = state.getNextStep();
			if (state.getState() == State.INPROGRESS) 
			{
//				if (nextCommand.compareTo(SetVMStatusCommand.COMMAND) == 0) 
//				{ 
//					System.err.println("Call SetVMStatusCommand");
//					if (vm_orig_name == null)
//					{
//						Map<Object, Object> args = this.actionRequest.getArguments();
//						vm_orig_name = (String)args.get((String)"vmName");
//						args.put("Status", "MIGRATING");
//						this.actionRequest.setArguments(args);
//					}
//				}
				if (nextCommand.compareTo(EchoCommand.COMMAND) == 0) 
				{
					ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
					VmHostModel m = (VmHostModel)(modelClient.requestModel(true).getResourceModel());
					VM vm = m.getVM(vm_orig_name);
					if (vm != null)
					{
//						if (vm.getStatus() == VMStatus.STOPPED)//VM trace is still on the memory with "shut off" status
//						{
//							nextCommand = UndefineCommand.COMMAND;
//							ActionResponseMessage responseMessage = new ActionResponseMessage();
//							responseMessage.setMessage("VM: " + vm_orig_name + " removed!");
//							try {
//								sendActionResponseMessage(responseMessage, this.requestor);
//							}catch (CapabilityException e) {
//								throw new ActionException("VM: " + vm_orig_name + " failed!", e);
//							}
//						}else
						if (this.retry < MAX_RETRY)
						{//migration unsuccessful, try it again
							nextCommand = MigrateCommand.COMMAND;
							commandsList.clear();
							commandsList.add(UpdateCommand.COMMAND);
							commandsList.add(EchoCommand.COMMAND);
							state.setSteps(commandsList);
							this.retry ++;
							logger.debug("====================> MigrateAction: retry " + this.retry + " time.");
						}else
							throw new ActionException("VM: " + vm_orig_name + " failed!");
					}else
					{
						ActionResponseMessage responseMessage = new ActionResponseMessage();
						responseMessage.setMessage("VM: " + vm_orig_name + " removed!");
						try {
							sendActionResponseMessage(responseMessage, this.requestor);
						}catch (CapabilityException e) {
							throw new ActionException("VM: " + vm_orig_name + " failed!", e);
						}
					}
				}
				sendMessageToCommand(nextCommand);
			}
		} catch (CapabilityException e) {
			e.printStackTrace();
			throw new ActionException("VM: " + vm_orig_name + " failed!", e);
		}
	}
}
	
