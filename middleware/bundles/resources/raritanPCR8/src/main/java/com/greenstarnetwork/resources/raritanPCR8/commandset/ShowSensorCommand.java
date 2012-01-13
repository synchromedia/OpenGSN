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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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

public class ShowSensorCommand extends AbstractCommandWithProtocol 
{
	public static final String COMMAND = "ShowSensorCommand";	//Query command ID
	
	public ShowSensorCommand() {
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
				throw new CommandException("Invalid command argument!. ShowSensorCommand sensor_ID");
		
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
			double base = 0;		//base value
			double load = 0;		//load value
			List<String> associated_outlets = null;
			try {
				while (line != null)
				{
					if (line.startsWith("Name is"))
						elem.setName(line.substring(new String("Name is").length() + 1));
					else if (line.startsWith("BaseUnits is")) {
						String bs = line.substring(new String("BaseUnits is").length() + 1);
						int pi = bs.indexOf("(");
						if (pi > 0)
							base = new Double(bs.substring(0,pi).trim()).doubleValue();
					}
					else if (line.startsWith("CurrentReading is")) {
						String ls = line.substring(new String("CurrentReading is").length() + 1);
						load = new Double(ls.trim()).doubleValue() * base;
						//set six digits precision for double value
						java.text.DecimalFormat df = new java.text.DecimalFormat("###.#######");
						try {
							load = df.parse(df.format(load)).doubleValue();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						elem.setLoad(new Double(load).toString());
					}
					else if (line.startsWith("CIM_AssociatedSensor =>"))
					{
						line = line.substring(new String("CIM_AssociatedSensor =>").length() + 1);
						if (line.indexOf("outlet") > 0)
						{
							if (associated_outlets == null)
								associated_outlets = new ArrayList<String>();
							associated_outlets.add(line);
						}
					}
						
					line = tokenizer.nextToken().trim();
				}
			}catch (NoSuchElementException e) {
			}
			if (associated_outlets != null)
				setOutletCurrent((PDUModel)model, associated_outlets, load);
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

	/**
	 * Set current value to associated outlets
	 * @param model
	 * @param voltage
	 */
	private void setOutletCurrent(PDUModel model, List<String> outlets, double load) {
		for (int pi=0; pi < outlets.size(); pi++)
		{
			PDUElement elem = model.getOutlet(outlets.get(pi));
			if (elem != null)
			{
				double l = load;
				String curload = elem.getLoad();
				if (curload != null)
					l += new Double(curload).doubleValue();
				elem.setLoad(new Double(l).toString());
			}
		}
	}
}
