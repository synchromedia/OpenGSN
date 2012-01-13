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

import com.greenstarnetwork.models.vmm.VMStatus;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.resources.libvirt.commandset.DestroyCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UndefineCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UpdateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.VMInfoCommand;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.capabilities.actionset.api.ActionResponseMessage;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.capability.CapabilityException;

/**
 * Shutdown a VM then undefine the domain
 * @author knguyen
 *
 */
public class DeleteAction extends BasicAction{

	public static final String ACTION = "DeleteAction";

	private String vm_orig_name = null;
	
	public DeleteAction() {
		super(ACTION);
		initializeCommandsList();
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add(DestroyCommand.COMMAND);
		commandsList.add(UpdateCommand.COMMAND);
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
				if (vm_orig_name == null) {
					Map<Object, Object> args = this.actionRequest.getArguments();
					vm_orig_name = (String)args.get((String)"vmName");
				}
//				if (m.getVM(vm_orig_name) == null) {
////					System.err.println("DeleteAction - VM: " + vm_orig_name + " failed! No VM found: " + vm_orig_name);
//					throw new ActionException("VM: " + vm_orig_name + " failed! No VM found: " + vm_orig_name);
//				}
//				
//				if (nextCommand.compareTo(DestroyCommand.COMMAND) == 0) 
//				{
//					commandsList.clear();
//					if ( m.getVM(vm_orig_name).getStatus() != VMStatus.STOPPED ) 
//					{//check if the VM is currently stopped
//						commandsList.add(VMInfoCommand.COMMAND);
//						commandsList.add(EchoCommand.COMMAND);
//					}else
//					{//VM is currently stopped
//						nextCommand = UndefineCommand.COMMAND;
//						sendResponse();
//					}
//					state.setSteps(commandsList);
//				}else if (nextCommand.compareTo(VMInfoCommand.COMMAND) == 0)
//				{
//					if (m.getVM(vm_orig_name) == null) {//already removed
//						commandsList.clear();
//						sendResponse();
//						return;
//					}
//				}
				if (nextCommand.compareTo(VMInfoCommand.COMMAND) == 0) 
				{
					if (resourceID != null)
					{
						ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
						RequestModelResponse reqModel = modelClient.requestModel(true);
						VmHostModel m = (VmHostModel)(reqModel.getResourceModel());
						
						if (m.getVM(vm_orig_name) == null) {//already removed
							commandsList.clear();
							sendResponse();
							return;
						}
						
						if ( m.getVM(vm_orig_name).getStatus() != VMStatus.STOPPED )
						{//check if the VM had been stopped
							try {//wait for VM to be completely stopped
								Thread.sleep(10000);
							}catch (Exception e) {};
							commandsList.clear();
							commandsList.add(VMInfoCommand.COMMAND);
//							commandsList.add(EchoCommand.COMMAND);
							state.setSteps(commandsList);
						}else
						{
							commandsList.clear();
							nextCommand = UndefineCommand.COMMAND;
							state.setSteps(commandsList);
							sendResponse();
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
	
	/**
	 * Send action response to caller
	 * @throws ActionException
	 */
	private void sendResponse() throws ActionException {
		ActionResponseMessage responseMessage = new ActionResponseMessage();
		responseMessage.setMessage("VM: " + vm_orig_name + " shutdown!");
		try {
			sendActionResponseMessage(responseMessage, this.requestor);
		}catch (CapabilityException e) {
			throw new ActionException("VM: " + vm_orig_name + " failed!", e);
		}
	}
}
	
