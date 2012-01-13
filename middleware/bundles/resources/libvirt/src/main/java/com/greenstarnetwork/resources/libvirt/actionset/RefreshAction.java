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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.resources.libvirt.commandset.EchoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.GetMACCommand;
import com.greenstarnetwork.resources.libvirt.commandset.HostRunningInfoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UpdateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.VMInfoCommand;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.capability.CapabilityException;

/**
 * Refresh the list of VMs and the host information
 * @author knguyen
 *
 */
public class RefreshAction extends BasicAction{

	private Logger logger = LoggerFactory.getLogger(RefreshAction.class);
	
	public static final String ACTION = "RefreshAction";
	int index = 0;
	List<VM> vms = null;
	boolean model_initialized = false;
	
	public RefreshAction() {
		super(ACTION);
		initializeCommandsList();
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add(UpdateCommand.COMMAND);
		commandsList.add(EchoCommand.COMMAND);
		state.setSteps(commandsList);
	}
	
	@Override
	public void executeAction() throws ActionException{
		extractResourceIdFromArgument();
		if (resourceID == null) {
			Map<Object, Object> args = this.actionRequest.getArguments();
			resourceID = (String)args.get((String)"resourceID");
			if (resourceID == null)
				throw new ActionException("Need resourceID parameter for RefreshAction!");
		}

		try {
			String nextCommand = state.getNextStep();
			if (state.getState() == State.INPROGRESS) 
			{
				if (nextCommand.compareTo(EchoCommand.COMMAND) == 0) 
				{
					ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
					RequestModelResponse reqModel = modelClient.requestModel(true);
					VmHostModel m = (VmHostModel)(reqModel.getResourceModel());
					vms = m.getVMList();
					if ((vms != null) && (vms.size() > 0))
					{
						commandsList.clear();
						for (int pi=0; pi < vms.size(); pi++) 
						{//run VMInfoCommand for each VM
							commandsList.add(VMInfoCommand.COMMAND);
							if (vms.get(pi).getMac() == null)
								commandsList.add(GetMACCommand.COMMAND);
						}
						commandsList.add(HostRunningInfoCommand.COMMAND);
						state.setSteps(commandsList);
					}
					model_initialized = true;
				}
				else if ( model_initialized && (vms.size() > 0) && 
						(nextCommand.compareTo(VMInfoCommand.COMMAND) == 0) )
				{
					if (index < vms.size())
					{
						Map<Object, Object> cmd_args = new Hashtable<Object, Object>();
						cmd_args.put("vmName", vms.get(index).getName());
						this.actionRequest.setArguments(cmd_args);
					}
					index ++;
				}
				sendMessageToCommand(nextCommand);
			}
		} catch (CapabilityException e) {
			e.printStackTrace();
			logger.debug(e.toString());
			throw new ActionException("", e);
		}
	}

}
