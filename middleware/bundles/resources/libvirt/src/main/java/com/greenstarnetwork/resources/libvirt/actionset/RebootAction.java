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

import com.greenstarnetwork.resources.libvirt.commandset.ResumeVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.SuspendVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.VMInfoCommand;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.capabilities.actionset.api.ActionResponseMessage;
import com.iaasframework.resources.core.capability.CapabilityException;

/**
 * Shutdown and then restart a VM
 * @author knguyen
 *
 */
public class RebootAction extends BasicAction{

	public static final String ACTION = "RebootAction";
	
//	private boolean restarted = false;
	private String vm_orig_name = null;

	public RebootAction() {
		super(ACTION);
		initializeCommandsList();
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add(SuspendVMCommand.COMMAND);
		commandsList.add(ResumeVMCommand.COMMAND);
		commandsList.add(VMInfoCommand.COMMAND);
		state.setSteps(commandsList);
	}
	
	@Override
	public void executeAction() throws ActionException
	{
		extractResourceIdFromArgument();
		try {
			String nextCommand = state.getNextStep();
			if (state.getState() == State.INPROGRESS) 
			{
//				if (nextCommand.compareTo(VMInfoCommand.COMMAND) == 0) 
//				{
//					if (resourceID != null)
//					{
//						Map<Object, Object> args = this.actionRequest.getArguments();
//						if (args.containsKey((String)"vmName"))
//						{
//							ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
//							RequestModelResponse reqModel = modelClient.requestModel(true);
//							VmHostModel m = (VmHostModel)(reqModel.getResourceModel());
//							vm_orig_name = (String)args.get((String)"vmName");
//
//							if ( m.getVM(vm_orig_name).getStatus() == VMStatus.STOPPING )
//							{
//								try {
//									Thread.sleep(10000);
//								}catch (Exception e) {};
//								commandsList.clear();
//								commandsList.add(VMInfoCommand.COMMAND);
////								commandsList.add("VMInfoCommand");
//								state.setSteps(commandsList);
//							}else
//							{
//								if (!restarted)
//								{
//									commandsList.clear();
//									commandsList.add(StartVMCommand.COMMAND);
//									commandsList.add(VMInfoCommand.COMMAND);
//									state.setSteps(commandsList);
//									restarted = true;
//								}else {
//									ActionResponseMessage responseMessage = new ActionResponseMessage();
//									responseMessage.setMessage("VM: " + vm_orig_name + " restarted!");
//									try {
//										sendActionResponseMessage(responseMessage, this.requestor);
//									}catch (CapabilityException e) {
//										throw new ActionException("VM: " + vm_orig_name + " failed!", e);
//									}
//								}
//							}
//						}
//					}
//				}
				if (nextCommand.compareTo(VMInfoCommand.COMMAND) == 0)
				{
					if (resourceID != null)
					{
						Map<Object, Object> args = this.actionRequest.getArguments();
						if (args.containsKey((String)"vmName"))
						{
//							ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
//							RequestModelResponse reqModel = modelClient.requestModel(true);
//							VmHostModel m = (VmHostModel)(reqModel.getResourceModel());
							vm_orig_name = (String)args.get((String)"vmName");
							ActionResponseMessage responseMessage = new ActionResponseMessage();
							responseMessage.setMessage("VM: " + vm_orig_name + " restarted!");
							try {
								sendActionResponseMessage(responseMessage, this.requestor);
							}catch (CapabilityException e) {
								throw new ActionException("VM: " + vm_orig_name + " failed!", e);
							}
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
	
