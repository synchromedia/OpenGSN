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


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.climate.HumidityElement;
import com.greenstarnetwork.models.climate.TemperatureElement;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;



public abstract class BasicCommand extends AbstractCommandWithProtocol {

Climate climate ;
	private String COMMAND;
	protected Logger logger = null;
	Map<Object, Object> arg_tem = new Hashtable<Object, Object>();
	Map<Object, Object> arg_hum = new Hashtable<Object, Object>();
	Map<Object, Object>	arg_sen = new Hashtable<Object, Object>();
	String resourceId;
	public BasicCommand(String command) {
		super(command);
		COMMAND = command;
		}

	@Override
	public void executeCommand() throws CommandException {
		
		
		if (!initialized) {
			initializeWithModel();
			climate = (Climate) model;
			
		}else{
			try {
				
				
				Map<Object, Object> args = this.commandRequestMessage.getArguments();
				if (args.containsKey((String)"resourceID"))
				 resourceId = (String)args.get((String)"resourceID");
				
				/**/
				
				ActionSetCapabilityClient client = new ActionSetCapabilityClient(
						resourceId);
				
				
					
				//requestEngineModel(false);
				ModelCapabilityClient modelClient = new ModelCapabilityClient(
						resourceId);
				RequestModelResponse reqModel = modelClient.requestModel(true);
				climate = (Climate) reqModel.getResourceModel();
		
				
				//climate = (Climate) model;
				List<TemperatureElement> temps = climate.getTemperatureelement();
				List<HumidityElement> humidity =climate.getHumidityelement();
			
				if (humidity != null) {
			
					for (int i = 0; i < humidity.size(); i++) {
						arg_hum.remove((String) "Sensor");
						String ss = humidity.get(i).getID();
				
						
						arg_hum.put("Sensor", ss);

						if ((ss.indexOf("nsensor") > -1)||(ss.indexOf("externalsensor") > -1)) {
						
							// client.executeAction("GetHumidityAction",arg_hum);
							 executeRefreshCommand();
						
						}

					}

				}
				
		
				if (temps != null) {
			
				
					for (int i = 0; i < temps.size(); i++) {
						arg_tem.remove((String) "Sensor");
					String ss = temps.get(i).getID();
				
						
						arg_tem.put("Sensor", ss);

						if ((ss.indexOf("ntempsensor") > -1)||(ss.indexOf("externalsensor") > -1)) {
						
							//client.executeAction("GetTemperatureAction",arg_tem);
							 executeRefreshCommandTem();
						
						}

					}

				}
		
			
			
			
			} catch (Exception e) {
				
				throw new CommandException(COMMAND+" :: "+e);
			}
		}
	}


	public abstract void executeRefreshCommand() throws Exception;
	public abstract void  executeRefreshCommandTem() throws Exception;
	
	
	
	
}