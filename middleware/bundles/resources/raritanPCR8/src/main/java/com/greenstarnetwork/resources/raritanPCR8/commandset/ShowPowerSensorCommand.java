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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

public class ShowPowerSensorCommand extends AbstractCommandWithProtocol 
{
	public static final String COMMAND = "ShowPowerSensorCommand";	//Query command ID
	
	public ShowPowerSensorCommand() {
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
				throw new CommandException("Invalid command argument!. ShowPowerSensorCommand sensor_ID");
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
		
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken().trim();	//first line is the command
			while (!line.startsWith("/"))
				line = tokenizer.nextToken().trim();
			PDUElement elem = ((PDUModel)model).getInFeed(line);
			if (elem == null)
				throw new CommandException("Sensor not found: " + line);
			line = tokenizer.nextToken().trim();
			try {
				int pi = 0;
				while (line != null)
				{
					if (line.startsWith("Name is"))
						elem.setName(line.substring(new String("Name is").length() + 1));
					else if (line.startsWith("SensorType is"))
					{
						if (line.indexOf("(Other)") < 0)
							break;		//not power type
					}else if (line.startsWith("OtherSensorTypeDescription")) {
						pi = line.indexOf("Power");
						if (pi < 0)
							break;		//not power type
						elem.setType(line.substring(pi));
					}else if (line.startsWith("CurrentReading is")) {
						if (line.length() > new String("CurrentReading is").length())
						{
							String power = line.substring(new String("CurrentReading is").length() + 1); 
							elem.setPower(power);
							String pduPower = ((PDUModel)model).getTotalPower();
							if (pduPower == null)
								((PDUModel)model).setTotalPower(power);
							else {
								((PDUModel)model).setTotalPower(new Double(new Double(pduPower).doubleValue() + 
									new Double(power).doubleValue()).toString());
							}
						}
					}
					line = tokenizer.nextToken().trim();
				}
			}catch (NoSuchElementException e) {
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
