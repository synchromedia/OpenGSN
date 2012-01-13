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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Get node parameters
 * 
 * @author knguyen
 *
 */
public class NodeInfoCommand extends BasicCommand {
	public static final String COMMAND = "NodeInfoCommand";
	
	public NodeInfoCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			String command = "virsh -c qemu:///system nodeinfo";
			sendCommandToProtocol(command);
		}
	
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
			CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
			String s = msg.getRawMessage();
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken();
			while (line != null) 
			{
				String prop = getLineProp(line);
				if (prop != null)
				{
					if (prop.compareTo("CPU model") == 0)
						((VmHostModel)model).setCpu(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("CPU(s)") == 0) {
						((VmHostModel)model).setNbrCPUs(line.substring(prop.length()+1).trim());
						//currently set the maximum number of VMs be the number of CPUs
						((VmHostModel)model).setMaxVMs(line.substring(prop.length()+1).trim());
					}
					else if (prop.compareTo("CPU frequency") == 0)
						((VmHostModel)model).setSpeed(reformatString(line.substring(prop.length()+1).trim()));
					else if (prop.compareTo("CPU socket(s)") == 0)
						((VmHostModel)model).setCPUSockets(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Core(s) per socket") == 0)
						((VmHostModel)model).setCoresPerSocket(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Thread(s) per core") == 0)
						((VmHostModel)model).setThreadsPerCore(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("NUMA cell(s)") == 0)
						((VmHostModel)model).setNUMA(line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Memory size") == 0)
						((VmHostModel)model).setTotalMemory(reformatString(line.substring(prop.length()+1).trim()));
				}
				
				try {
					line = tokenizer.nextToken();
				}catch (NoSuchElementException e) {
					break;
				}
			}			
	}
}