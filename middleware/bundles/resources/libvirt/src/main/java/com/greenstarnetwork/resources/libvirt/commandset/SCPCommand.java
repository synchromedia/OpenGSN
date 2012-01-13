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
 * TODO EXPORT
 * Send a file to remote host using scp command
 * @author knguyen
 *
 */
public class SCPCommand extends BasicCommand {
	
	public static final String COMMAND = "SCPCommand";
	
	public SCPCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (!args.containsKey((String)"filename"))
				throw new CommandException("Need filename parameter for SCP command!");
			if (!args.containsKey((String)"targethost"))
				throw new CommandException("Need targethost parameter for SCP command!");
			if (!args.containsKey((String)"username"))
				throw new CommandException("Need username parameter for SCP command!");
			if (!args.containsKey((String)"remotedir"))
				throw new CommandException("Need remotedir parameter for SCP command!");

			String filename = (String)args.get((String)"filename");
			String targethost = (String)args.get((String)"targethost");
			String username = (String)args.get((String)"username");
			String remotedir = (String)args.get((String)"remotedir");
			if (!remotedir.startsWith("/"))
				remotedir = "/" + remotedir;
			if (!remotedir.endsWith("/"))
				remotedir = remotedir + "/";
			
			String 	vmDiskImagePath		= "/storage/" + filename;
			String command = "scp " + vmDiskImagePath + " " + username + "@" + targethost + ":" + remotedir;
			sendCommandToProtocol(command);
		}
	}
	
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if ( (s.indexOf("cp:") > -1) || (s.indexOf("ssh:") > -1) )
			throw new CommandException("SCPCommand exception: " + s);
	}

}