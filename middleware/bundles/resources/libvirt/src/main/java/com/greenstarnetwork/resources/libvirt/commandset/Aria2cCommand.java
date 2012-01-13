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
 * Download a file from a remote host using aria2c command
 * @author knguyen
 *
 */
public class Aria2cCommand extends BasicCommand {
	
	public static final String COMMAND = "Aria2cCommand";
	
	public Aria2cCommand() {
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
				throw new CommandException("Need filename parameter for Aria2c command!");
			if (!args.containsKey((String)"targethost"))
				throw new CommandException("Need targethost parameter for Aria2c command!");
			if (!args.containsKey((String)"username"))
				throw new CommandException("Need username parameter for Aria2c command!");
			if (!args.containsKey((String)"remotedir"))
				throw new CommandException("Need remotedir parameter for Aria2c command!");

			String filename = (String)args.get((String)"filename");
			String targethost = (String)args.get((String)"targethost");
			String username = (String)args.get((String)"username");
			String remotedir = (String)args.get((String)"remotedir");
			if (!remotedir.startsWith("/"))
				remotedir = "/" + remotedir;
			if (!remotedir.endsWith("/"))
				remotedir = remotedir + "/";
			
			String command = "aria2c -s 5 -j 100 --ftp-user=" + username + " --ftp-passwd=GSN-M8n8g3r! ftp://" + 
				targethost + remotedir + filename;

			sendCommandToProtocol(command);
		}
	}
	
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		if (s.indexOf("(ERR):error occurred.") > -1)
			throw new CommandException("Aria2c exception: " + s);
	}

}