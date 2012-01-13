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
package com.greenstarnetwork.resources.raritanPCR8.actionset;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.resources.raritanPCR8.commandset.EchoCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ResetPowerCurrentCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowCurrSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowOutletCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowPowerSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowSystemCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowVoltSensorCommand;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.capability.CapabilityException;

public class RefreshAction extends BasicAction{

	public static final String ACTION = "RefreshAction";
	private List<String> sensor_args = null;
	private List<String> outlet_args = null;
	private int sensor_count = 0;
	private int outlet_count = 0;
	private boolean model_initialized = false;
	
	public RefreshAction() {
		super(ACTION);
		initializeCommandsList();
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add(ResetPowerCurrentCommand.COMMAND);
		commandsList.add(ShowSystemCommand.COMMAND);
		commandsList.add(EchoCommand.COMMAND);
		state.setSteps(commandsList);
	}
	
	@Override
	public void executeAction() throws ActionException
	{
		extractResourceIdFromArgument();
		if (resourceID == null) {
			Map<Object, Object> args = this.actionRequest.getArguments();
			resourceID = (String)args.get((String)"resourceID");
			if (resourceID == null)
				throw new ActionException("Need resourceID parameter for RefreshAction!");
		}
		
		try {
			String nextCommand = state.getNextStep();
			if (state.getState() == State.INPROGRESS) 
			{
				if (nextCommand.compareTo(ShowSystemCommand.COMMAND) == 0)
				{
					Map<Object, Object> cmd_args = new Hashtable<Object, Object>();
					cmd_args.put("System", "system1");
					this.actionRequest.setArguments(cmd_args);
				}
				else if (nextCommand.compareTo(EchoCommand.COMMAND) == 0) 
				{
					if (!model_initialized)
					{
						ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
						RequestModelResponse reqModel = modelClient.requestModel(true);
						PDUModel m = (PDUModel)(reqModel.getResourceModel());

						commandsList.clear();
						//Get sensors information
						List<PDUElement> sensors = m.getInFeeds();
						if (sensors != null)
						{
							sensor_args = new ArrayList<String>();
							for (int i=0; i<sensors.size(); i ++)
							{
								String ss = sensors.get(i).getID();
								if (ss.indexOf("nvoltsensor") > -1)
								{
									commandsList.add(ShowVoltSensorCommand.COMMAND);
									sensor_args.add(ss);
//									commandsList.add(EchoCommand.COMMAND);
								}else if (ss.indexOf("ncurrsensor") > -1)
								{
									commandsList.add(ShowCurrSensorCommand.COMMAND);
									sensor_args.add(ss);
//									commandsList.add(EchoCommand.COMMAND);
								}else if (ss.indexOf("nsensor") > -1) 
								{
									commandsList.add(ShowPowerSensorCommand.COMMAND);
									sensor_args.add(ss);
//									commandsList.add(EchoCommand.COMMAND);
								}
							}
						}

						//Get outlets information
						List<PDUElement> outlets = m.getOutlets();
						if (outlets != null)
						{
							outlet_args = new ArrayList<String>();
							for (int i=0; i<outlets.size(); i ++)
							{
								commandsList.add(ShowOutletCommand.COMMAND);
								outlet_args.add(outlets.get(i).getID());
//								commandsList.add(EchoCommand.COMMAND);
							}
						}
						state.setSteps(commandsList);
						nextCommand = state.getNextStep();
						model_initialized = true;
					}
				}//Echo command
				
				
				if (nextCommand != null)
				{
					if ( (nextCommand.compareTo(ShowPowerSensorCommand.COMMAND) == 0) ||
							  (nextCommand.compareTo(ShowCurrSensorCommand.COMMAND) == 0) ||
							  (nextCommand.compareTo(ShowVoltSensorCommand.COMMAND) == 0) )
					{
						Map<Object, Object> cmd_args = new Hashtable<Object, Object>();
						cmd_args.put("Sensor", sensor_args.get(sensor_count));
						this.actionRequest.setArguments(cmd_args);
						sensor_count ++;
					}
					else if (nextCommand.compareTo(ShowOutletCommand.COMMAND) == 0)
					{
						Map<Object, Object> cmd_args = new Hashtable<Object, Object>();
						cmd_args.put("Outlet", outlet_args.get(outlet_count));
						this.actionRequest.setArguments(cmd_args);
						outlet_count ++;
					}
					
					sendMessageToCommand(nextCommand);
				}
			}
		} catch (CapabilityException e) {
			e.printStackTrace();
			throw new ActionException("", e);
		}
	}
}
	
