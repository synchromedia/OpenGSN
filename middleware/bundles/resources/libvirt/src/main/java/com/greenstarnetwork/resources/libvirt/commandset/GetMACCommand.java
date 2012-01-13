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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Retrieve MAC address of a VM from the XML file defining VM configuration
 * @author knguyen
 *
 */
public class GetMACCommand extends BasicCommand {

	public static final String COMMAND = "GetMACCommand";	//Query command ID
	private List<VM> vmlist = null;

	public GetMACCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			String vmName = (String)args.get((String)"vmName");
			if (vmName == null)
				throw new CommandException("Need VM name parameter for StartVM command!");
			boolean found = false;
			for (int i=0; i < vmlist.size(); i++) {
				if (vmName.equals(vmlist.get(i).getName())) {
					found = true;
					break;
				}
			}
			if (!found)
				throw new CommandException("VM: " + vmName + "not found!");
			
			sendCommandToProtocol("virsh -c qemu:///system dumpxml " + vmName + " | grep '<mac address='");
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
		String s = msg.getRawMessage().trim();

		StringTokenizer tokenizer = new StringTokenizer(s, "\n");
		String line = tokenizer.nextToken().trim();
		while (line != null) 
		{
			int pi = line.lastIndexOf(":");
			int pj = line.indexOf("<mac address=");
//			if ( (pi < 0) || (pi + 2 > line.length()) )
//				throw new CommandException("Invalid MAC addres line in XML: " + line);
			if ((pi > 0) && (pj > -1) && 
					(line.indexOf("virsh -c qemu") < 0))
			{
				pj = line.indexOf('\'');
				pi = line.lastIndexOf('\'');
				if (pi == pj)
					throw new CommandException("Invalid MAC address line in XML: " + line);
				String mac = line.substring(pj+1,pi);
				((VmHostModel)model).getVM(
						(String)this.commandRequestMessage.getArguments().get((String)"vmName")).setMac(mac);
				break;
			}
			try {
				line = tokenizer.nextToken().trim();
			}catch (NoSuchElementException e) {
				break;
			}
		}
	}
}