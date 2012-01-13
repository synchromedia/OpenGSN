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
package com.greenstarnetwork.resources.raritanPCR8.commandset;

import java.util.Map;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUException;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Off command handler. Turn off an outlet.
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class OffCommand extends AbstractCommandWithProtocol {

	public static final String COMMAND = "OffCommand";	//Query command ID

	public OffCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (args.containsKey((String)"Outlet")) {
				String outl = (String)args.get((String)"Outlet");
				//	check if the oulet has been registered in model
				if (((PDUModel)model).getOutlet(outl) == null)
					throw new CommandException("Outlet name or ID not found!");
				String command = PCRCommandList.SET + " " + outl + " powerState=off";
				sendCommandToProtocol(command );
			}else
				throw new CommandException("Invalid command argument!. Usage:Off out_let_ID_or_name");
		}
	}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException {
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		
		StringTokenizer tokenizer = new StringTokenizer(s, "\n");
		String line = tokenizer.nextToken().trim();	//first line is the command
		line = tokenizer.nextToken().trim();
		
		PDUElement elem = ((PDUModel)model).getOutlet(line);
		if (elem != null)
		{
			line = tokenizer.nextToken().trim();
			if (!line.startsWith("Properties:"))
				throw new CommandException("Invalid response format! " + s);
			
			line = tokenizer.nextToken().trim();
			if (!line.startsWith("powerState is"))
				throw new CommandException("Invalid response format! " + s);
			int head = new String("powerState is").length() + 1;
			line = line.substring(head, line.length());
			try {
				elem.setStatusString(line);
			} catch (PDUException e) {
				e.printStackTrace();
				throw new CommandException("Invalid Outlet status: " +  e.toString());
			}
		}
	}

	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException {
		if (response instanceof ProtocolResponseMessage) {
			this.response = (ProtocolResponseMessage) response;
			this.requestEngineModel(false);
		}
		else if (response instanceof ProtocolErrorMessage) {
			this.errorMessage = (ProtocolErrorMessage) response;
			CommandException commandException = new CommandException("Error executing command "
					+ this.commandID, ((ProtocolErrorMessage) response)
					.getProtocolException());
			commandException.setName(this.commandID);
			commandException.setCommand(this);
			this.sendCommandErrorResponse(commandException);
		}
	}
}
