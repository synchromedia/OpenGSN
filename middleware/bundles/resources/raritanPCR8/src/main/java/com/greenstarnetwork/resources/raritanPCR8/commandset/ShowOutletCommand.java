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

import java.util.ArrayList;
import java.util.Iterator;
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

/**
 * Show outlet command handler. Display outlet information
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class ShowOutletCommand extends AbstractCommandWithProtocol {
	public static final String COMMAND = "ShowOutletCommand";	//Query command ID
	
	public ShowOutletCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (args.containsKey((String)"Outlet"))
			{
				sendCommandToProtocol("show " + (String)args.get((String)"Outlet"));
			}
			else
				throw new CommandException("Invalid command argument!. Usage:ShowOutletCommand out_let_ID");
		
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
		
		List<String> associated_sensors = null;
		PDUElement elem = null;
		try {
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken().trim();	//first line is the command
			line = tokenizer.nextToken().trim();

			elem = ((PDUModel)model).getOutlet(line);
			if (elem == null)
				throw new CommandException("Outlet not found: " + line);
			line = tokenizer.nextToken().trim();
			while (line != null)
			{
//				if (line.startsWith("Associations:"))
//					break;
				
				if (line.startsWith("Name is"))
					elem.setName(line.substring(new String("Name is").length() + 1));
				else if (line.startsWith("powerState is"))
				{
					int head = new String("powerState is").length() + 1;
					line = line.substring(head, head +1);
					if (line.compareTo("1") == 0)
						elem.setStatus(Status.On);
					else if ( (line.compareTo("2") == 0) || (line.compareTo("0") == 0) )
						elem.setStatus(Status.Off);
					else
						throw new CommandException("Invalid Outlet status: " +  line);
				}
				else if (line.startsWith("CIM_AssociatedSensor =>"))
				{
					line = line.substring(new String("CIM_AssociatedSensor =>").length() + 1).trim();
					if (line.indexOf("sensor") > 0)
					{
						if (associated_sensors == null)
							associated_sensors = new ArrayList<String>();
						associated_sensors.add(line);
					}
				}
				
				line = tokenizer.nextToken().trim();
				while (line.isEmpty()) {
					line = tokenizer.nextToken().trim();
				}
			}
			
		}catch (NoSuchElementException e) {
		}
		
		if (associated_sensors != null)
		{
			int sensor_param_set = 0;
			Iterator<String> it = associated_sensors.iterator();
			while ( it.hasNext() && (sensor_param_set < 2) ) {
				String sensor = it.next();
				if (sensor.indexOf("ncurrsensor") > -1)
				{
					PDUElement in = ((PDUModel)model).getInFeed(sensor);
					elem.setLoad(in.getLoad());
					sensor_param_set = 1;
				}else if (sensor.indexOf("nsensor") > -1) {
					PDUElement in = ((PDUModel)model).getInFeed(sensor);
					if ((in.getType() != null) && (in.getType().compareTo("PowerUnit") == 0)) {
						elem.setPower(in.getPower());
						sensor_param_set = 2;
					}
				}
			}
		}
		
		if (elem != null)
		{
			List<PDUElement> sensors = ((PDUModel)model).getInFeeds();
			Iterator<PDUElement> it = sensors.iterator();
			while (it.hasNext()) {
				PDUElement in = it.next();
				if (in.getID().indexOf("nvolt") > -1) {
					elem.setVoltage(in.getVoltage());
					break;
				}
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
