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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VMStatus;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Parse virsh dominfo command and fill VMM model with acquired information
 * @author knguyen
 *
 */
public class VMInfoCommand extends BasicCommand {
	public static final String COMMAND = "VMInfoCommand";
	
	public VMInfoCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			String domName = (String)args.get((String)"vmName");
			if (domName == null)
				throw new CommandException("Need VM name parameter for VMInfo command!");

			String command = "virsh -c qemu:///system dominfo " + domName;
			sendCommandToProtocol(command);
		}
	
	}
	
	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
			CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
			String s = msg.getRawMessage();
			 if (s.indexOf("error:") > -1)
			 {
				System.err.println("VMInfoCommand error response: " + s);
				 throw new CommandException("VMInfoCommand error: " + s);
			 }

			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken();
			VM vm = ((VmHostModel)model).getVM((String) this.commandRequestMessage.getArguments().get("vmName"));
			boolean is_new = false;
			while (line != null) 
			{
				String prop = getLineProp(line);
				if (prop != null)
				{
					if (vm == null) {
						is_new = true;
						vm = new VM();
					}

					if (prop.compareTo("Id") == 0)
						vm.setId(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Name") == 0)
						vm.setName(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("UUID") == 0)
						vm.setUuid(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("OS Type") == 0)
						vm.setOs(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("State") == 0) {
						if (line.substring(prop.length()+1).trim().compareTo("running") == 0) {
							if (vm.getStatus() != VMStatus.STOPPING)
								vm.setStatus(VMStatus.STARTED);
						}else {
							if (line.substring(prop.length()+1).trim().compareTo("paused") == 0)
								vm.setStatus(VMStatus.PAUSED);
							else if (line.substring(prop.length()+1).trim().compareTo("shut off") == 0)
								vm.setStatus(VMStatus.STOPPED);
							else
								vm.setStatus(VMStatus.STOPPED);
						}
					}
					else if (prop.compareTo("CPU(s)") == 0)
						vm.setVcpu(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("CPU time") == 0)
						vm.setClock(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Max memory") == 0)
						vm.setMemory(reformatString(line.substring(prop.length()+1).trim()));
					else if (prop.compareTo("Used memory") == 0)
						vm.setCurrentMemory(reformatString(line.substring(prop.length()+1).trim()));
					else if (prop.compareTo("Autostart") == 0) {
//						vm.set(line.substring(prop.length()+1).trim());
					}
					else if (prop.compareTo("Security model") == 0) {
//						vm.set(line.substring(prop.length()+1).trim());
					}
					else if (prop.compareTo("Security DOI") == 0) {
//						vm.set(line.substring(prop.length()+1).trim());
					}
					else if (prop.compareTo("Security label") == 0) {
//						vm.set(line.substring(prop.length()+1).trim());
					}
				}
				
				try {
					line = tokenizer.nextToken();
				}catch (NoSuchElementException e) {
					break;
				}
			}			
			
			if ( (vm != null) && is_new) {
				((VmHostModel)model).addVM(vm);
			}
	}
}