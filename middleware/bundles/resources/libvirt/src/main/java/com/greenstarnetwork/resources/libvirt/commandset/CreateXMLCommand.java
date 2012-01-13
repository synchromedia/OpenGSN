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

import java.io.IOException;
import java.util.Map;

import com.greenstarnetwork.models.vmm.VMTemplate;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;


/**
 * Create an XML file from a template
 * User may customize a number of parameters, such as name, number of cpu, mac address, memory capacity
 * @author knguyen
 *
 */
public class CreateXMLCommand extends BasicCommand {
	
	public static final String COMMAND = "CreateXMLCommand";
	
	public CreateXMLCommand() {
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
			if (!args.containsKey((String)"template"))
				throw new CommandException("Need template parameter for Create command!");

			String domName = (String)args.get((String)"vmName");
			String memory = (String)args.get((String)"memory");
			String cpu = (String)args.get((String)"cpu");
			String  vmTemplate = (String)args.get((String)"template");
			String macAddress = (String)args.get((String)"mac");
			
			String 	vmDiskImagePath		= "/storage";
			String 	vmDiskImage			= vmDiskImagePath + "/" + domName + ".qcow2";					
			String xmlFile = vmDiskImagePath + "/" + domName + ".xml";

			String VMModel = null;
			try {
				VMTemplate template = new VMTemplate();
				VMModel = template.getTemplate(vmTemplate, domName, memory, cpu, vmDiskImage, macAddress);
			} catch (IOException e) {
				e.printStackTrace();
				throw new CommandException("Error while creating VM model: " + e.toString());
			}
			String command = "echo \"" + VMModel + "\" > " + xmlFile;
			sendCommandToProtocol(command);
		}
	}
	
	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if (s.indexOf("error:") > -1)
			throw new CommandException("CreateCommand exception: " + s);
	}

}