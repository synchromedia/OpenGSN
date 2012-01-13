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
package com.greenstarnetwork.resources.servertechCWG.commandset;

import java.util.List;
import java.util.Map;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUException;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.pdu.PDUElement.Status;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Toggle command handler. Switch on-off state of an outlet.
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class ToggleCommand extends AbstractCommandWithProtocol{

	public static final String COMMAND = "ToggleCommand";	//Query command ID
	public static final int Number_Fields = 6;		//number of fields in the response message

	public ToggleCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException 
	{
		if (!initialized) {
			initializeWithModel();
		}else
		{		
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (args.containsKey((String)"Outlet")) {
				String outl = (String)args.get((String)"Outlet");
				//check if the oulet has been registered in model
				PDUElement elem = ((PDUModel)model).getOutlet(outl); 
				if ( elem == null)
					throw new CommandException("Outlet name or ID not found!");
				String command = CWGCommandList.OFF;
				if (elem.getStatus() == Status.Off)
					command = CWGCommandList.ON;
				command = command + " " + outl;
				sendCommandToProtocol(command);
			}else
				new CommandException("Outlet ID or name must be provided as parameter!");
		}
	}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException{
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();

		List<List<String>> result = msg.getTableResult(" ");
		if (result.size() == 0)
			throw new CommandException("Invalid response!");
		
		if (result.get(0).size() != Number_Fields)
			throw new CommandException("Invalid response format!");
		
		if (result.size() < 3)	//only header, no return value
			return;
		
		for (int i=2; i<result.size(); i++)
		{
			if (result.get(i).size() != Number_Fields)	//jump over empty lines
				continue;
			try {
				//Set outlet status
				PDUElement elem = ((PDUModel)model).getOutlet(result.get(i).get(0));
				if (elem != null)
				{
					elem.setStatusString(result.get(i).get(2));
					elem.setControlState(result.get(i).get(5));	
				}
			} catch (PDUException e) {
				throw new CommandException("Invalid Outlet status: " +  e.toString());
			}
		}
	}

	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException{
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
