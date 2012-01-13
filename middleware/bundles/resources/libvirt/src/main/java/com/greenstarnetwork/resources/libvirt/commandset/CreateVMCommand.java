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
 * Create a VM from a XML file defining the VM configuration
* @author Kim
* @author Fereydoun
*
*/
public class CreateVMCommand extends BasicCommand {
	
	public static final String COMMAND = "CreateVMCommand";
	
	private boolean createAllowed = false;		//determine if host can accept a new creation of VM
	
	private String ip = null;					//IP address of the newly created VM
	private String macAddress = null;			//MAC address of the newly created VM
		
	public CreateVMCommand() {
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
				throw new CommandException("Need VM name parameter for Create command!");
			if (!args.containsKey((String)"memory"))
				throw new CommandException("Need memory parameter for Create command!");
			if (!args.containsKey((String)"cpu"))
				throw new CommandException("Need cpu parameter for Create command!");
			if (!args.containsKey((String)"mac"))
				throw new CommandException("Need mac parameter (MAC address) for Create command!");
			if (!args.containsKey((String)"ip"))
				throw new CommandException("Need ip parameter (IP address) for Create command!");
			if (!args.containsKey((String)"template"))
				throw new CommandException("Need template parameter for Create command!");

			if (!createAllowed)
				throw new CommandException("Maximum number of VMs reached. No more VM can be created!");
				
			String domName = (String)args.get((String)"vmName");
			macAddress = (String)args.get((String)"mac");
			ip = (String)args.get((String)"ip");
			
			String 	vmDiskImagePath		= "/storage";
			String xmlFile = vmDiskImagePath + "/" + domName + ".xml";

//			String command = "virsh define " + xmlFile + " ; " + "virsh start " + domName;
			String command = "virsh -c qemu:///system create " + xmlFile;
			sendCommandToProtocol(command);
		}
	}
	
	@Override
	public synchronized void initializeCommand(IResourceModel model) throws CommandException {
		List<VM> listVMs = ((VmHostModel)model).getVMList();
		int curVMs = 0, maxVMs = 0;
		if (listVMs != null)
			curVMs = listVMs.size();
		
		String maxVM_str = ((VmHostModel)model).getMaxVMs();
		if (maxVM_str != null)
			maxVMs = new Integer(maxVM_str).intValue();
		
		if (curVMs < maxVMs)
			this.createAllowed = true;
		
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if (s.indexOf("error:") > -1)
			throw new CommandException("CreateCommand exception: " + s);

		VM vm = new VM();
		vm.setName( (String) this.commandRequestMessage.getArguments().get("vmName"));
		vm.setMemory( (String) this.commandRequestMessage.getArguments().get("memory") );
		vm.setVcpu( (String) this.commandRequestMessage.getArguments().get("cpu") );
		vm.setStorage( (String) this.commandRequestMessage.getArguments().get("storagePath") );
		vm.setStatus( VMStatus.STARTED );
		vm.setIp(ip);
		vm.setMac(macAddress);
		((VmHostModel)model).addVM(vm);
	}

}