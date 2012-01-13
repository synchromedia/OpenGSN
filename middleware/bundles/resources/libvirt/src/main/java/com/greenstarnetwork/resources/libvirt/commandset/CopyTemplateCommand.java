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

import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;


/**
 * Create a VM image from a given template.
 * CP command is called in order to create a copy of the template
 * @author knguyen
 *
 */
public class CopyTemplateCommand extends BasicCommand {
	
	public static final String COMMAND = "CopyTemplateCommand";
	
	public CopyTemplateCommand() {
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
			if (!args.containsKey((String)"template"))
				throw new CommandException("Need template parameter for Create command!");

			String domName = (String)args.get((String)"vmName");
			String  vmTemplate = (String)args.get((String)"template");
			
			String 	vmDiskImagePath		= "/storage";
			String 	vmDiskImage			= vmDiskImagePath + "/" + domName + ".qcow2";					
			String 	vmTemplateDiskImage	= vmDiskImagePath + "/Template_"+vmTemplate+".qcow2";			

			String command = "cp " + vmTemplateDiskImage + " " + vmDiskImage;
			sendCommandToProtocol(command);
		}
	}
	
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if (s.indexOf("error:") > -1)
			throw new CommandException("CreateCommand exception: " + s);
	}

}