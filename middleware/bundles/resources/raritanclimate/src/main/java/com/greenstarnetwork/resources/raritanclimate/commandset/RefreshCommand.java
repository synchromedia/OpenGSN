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
package com.greenstarnetwork.resources.raritanclimate.commandset;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.climate.HumidityElement;
import com.greenstarnetwork.models.common.Alarm;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;



public class RefreshCommand extends BasicCommand {
	
	public static final String COMMAND = "RefreshCommand";	//Query command ID

	public RefreshCommand() {
		super(COMMAND);
		}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException {
    	initialized = true;
	}


	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException {

	
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		
		try {
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken().trim();	//first line is the command
			while (!line.startsWith("/"))
				line = tokenizer.nextToken().trim();
			HumidityElement elem = ((Climate)model).getHumidityelement(line);
		
			if (elem == null)
				throw new CommandException("Sensor not found: " + line);
			line = tokenizer.nextToken().trim();
			
			while (line != null)
			{
						
				if (line.startsWith("Name is"))
					elem.setName(line.substring(new String("Name is").length() + 1));
				

				else if (line.startsWith("CurrentReading is")) 
				{
					String ls = line.substring(new String("CurrentReading is").length() + 1);
				
					elem.setHumidity(ls);
					 String hl = climate.getHumidityLowerThreshold();
					    String hu = climate.getHumidityUpperThreshold();
						 if (Float.valueOf(ls.trim()).floatValue()<=Float.valueOf(hl.trim()).floatValue() ){
		                    	Alarm alarm = new Alarm();
		                    	alarm.setDescription("the current Humidity ("+ ls+"%) is less than threshold ("+hl+"%)");
		                    	alarm.setAlarm("HUMIDITY LOW");
		                    	alarm.setTime(Calendar.getInstance());
		                    	//alarm.setValue(ls);
		                    	((Climate) model).addclimate(alarm);
		                    }
		                    if (Float.valueOf(ls.trim()).floatValue()>= Float.valueOf(hu.trim()).floatValue()){
		                    	
		                    	                    	
		                    	Alarm alarm = new Alarm();
		                    	alarm.setDescription("the current Humidity ("+ ls+"%) is high than threshold ("+hu+"%)");
		                        alarm.setAlarm("HUMIDITY HIGH");
		                    //	alarm.setValue(ls);
		                    	alarm.setTime(Calendar.getInstance());
		                    	((Climate) model).addclimate(alarm);
		                    }
				}	else if (line.startsWith("CurrentState is")) 
				{
					String ls = line.substring(new String("CurrentState is").length() + 1);
					elem.setCurrentState(ls);
			
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

	@Override
	public void executeRefreshCommand() throws Exception {

		
		if (!initialized) {
			initializeWithModel();
		}else
		{
			String command = PCRCommandList.SHOW;
			
			if (arg_hum.containsKey((String)"Sensor"))
				command += " " + (String)arg_hum.get((String)"Sensor");
			else
				throw new CommandException("Invalid command argument!. Usage:Get Humidity Command sensor_ID");
		
			sendCommandToProtocol(command);
		}
		
	}

	@Override
	public void executeRefreshCommandTem() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
