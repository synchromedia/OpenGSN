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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.pdu.PDUElement.Status;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

public class ShowVoltSensorCommand extends AbstractCommandWithProtocol {
	public static final String COMMAND = "ShowVoltSensorCommand";	//Query command ID
	
	public ShowVoltSensorCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (args.containsKey((String)"Sensor"))
			{
				sendCommandToProtocol("show " + (String)args.get((String)"Sensor"));
			}
			else
				throw new CommandException("Invalid command argument!. Usage:ShowVoltSensorCommand sensor_ID");
		}
	}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException {
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		if (msg == null)
			return;
		String s = msg.getRawMessage();
		
		try {
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken().trim();	//first line is the command
			while (!line.startsWith("/"))
				line = tokenizer.nextToken().trim();
			PDUElement elem = ((PDUModel)model).getInFeed(line);
			if (elem == null)
				throw new CommandException("Sensor not found: " + line);
			line = tokenizer.nextToken().trim();
			while (line != null)
			{
				if (line.startsWith("Name is"))
					elem.setName(line.substring(new String("Name is").length() + 1));
				else if (line.startsWith("CurrentState is"))
				{
					if (line.substring(new String("Name is").length() + 1).trim().equals("OK"))
						elem.setStatus(Status.On);
					else
						elem.setStatus(Status.Off);
				}
				else if (line.startsWith("CurrentReading is")) 
				{
					if (line.length() > new String("CurrentReading is").length())
					{
						String ls = line.substring(new String("CurrentReading is").length() + 1);
						elem.setVoltage(ls);
					}
				}
				line = tokenizer.nextToken().trim();
			}
		}catch (NoSuchElementException e) {
		}
	}

	/**
	 * Set voltage value to all outlets
	 * @param model
	 * @param voltage
	 */
	private void setSystemVoltage(PDUModel model, String voltage) {
		List<PDUElement> outlets = model.getOutlets();
		for (int pi=0; pi < outlets.size(); pi++)
			outlets.get(pi).setVoltage(voltage);
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
