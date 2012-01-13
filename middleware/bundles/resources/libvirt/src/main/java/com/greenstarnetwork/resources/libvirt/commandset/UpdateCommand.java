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

import java.util.ArrayList;
import java.util.List;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VMStatus;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Update the VM list of a host
 * @author knguyen
 *
 */
public class UpdateCommand extends BasicCommand 
{
	public static final String COMMAND = "UpdateCommand";
	public static final int Number_Fields = 3;	//number of fields in the response message
	
	public UpdateCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			String command1 = "virsh -c qemu:///system list --all";
			this.sendCommandToProtocol(command1);
		}
	}
	
	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		if (response == null) {
			return;
		}else
		{
			CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
//			String s = msg.getRawMessage();

			List<List<String>> result = msg.getTableResult(" ");

			//reset state of all VMs
//			((VmHostModel)model).removeVMs();

			//New list of VM
			List<String> newVMList = new ArrayList<String>();
			
			int i = 0;
			boolean start_data = false;
			boolean isNewVM = false;
			while (i<result.size())
			{
				if (result.get(i).size() == 0)	//jump over empty lines
					break;
				if (start_data) {
					if (result.get(i).size() < Number_Fields)
						break;
					String vmName = result.get(i).get(1);
					//We manage only the VMs created by ourself (e.g., the VMs whose name has a '_' character).
					isNewVM = false;
					if (vmName.indexOf("_") > 0) 
					{
						VM vm = null;
						vm = ((VmHostModel)model).getVM(vmName);
						if (vm == null)
						{
							vm = new VM();
							vm.setName(vmName);
							isNewVM = true;
						}
						
						vm.setTemplate(vmName.split("_")[1]);
						
						if (isNewVM || (vm.getStatus() != VMStatus.MIGRATING))
						{
							if (result.get(i).get(2).indexOf("running") > -1)
								vm.setStatus(VMStatus.STARTED);
							else if (result.get(i).get(2).startsWith("shut")) 
							{
								if ( (result.get(i).size() == Number_Fields+1) && 
										result.get(i).get(Number_Fields).startsWith("off"))
									vm.setStatus(VMStatus.STOPPED);
								else
									vm.setStatus(VMStatus.UNKNOWN);
							}
							else if (result.get(i).get(2).compareTo("paused") == 0)
								vm.setStatus(VMStatus.PAUSED);
							else
								vm.setStatus(VMStatus.UNKNOWN);
						}
						
						if (isNewVM)
							((VmHostModel)model).addVM(vm);
						
						newVMList.add(vmName);
					}
				}
				if (result.get(i).get(0).startsWith("----"))
				{
					start_data = true;
					i ++;
					continue;
				}
				i ++;
			}
			
			//Now clean the removed VMs
			List<VM> vms = ((VmHostModel)model).getVMList();
			for (i =0; i<vms.size(); i++)
			{
				String vmName = vms.get(i).getName();
				if (!newVMList.contains(vmName))
					((VmHostModel)model).removeVM(vmName);
			}
		}
	}
}