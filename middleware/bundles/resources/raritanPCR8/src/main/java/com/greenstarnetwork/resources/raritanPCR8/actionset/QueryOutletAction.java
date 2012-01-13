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
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.capability.CapabilityException;

public class QueryOutletAction extends BasicAction{

	public static final String ACTION = "QueryOutletAction";
	private List<String> sensors = null;
	int index = 0;
	boolean model_initialized = false;
	String outlet = null;
	
	public QueryOutletAction() {
		super(ACTION);
		initializeCommandsList();
		index = 0;
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add("ShowOutletCommand");
		commandsList.add("EchoCommand");
		state.setSteps(commandsList);
	}
	
	@Override
	public void executeAction() throws ActionException{
		extractResourceIdFromArgument();

		if (outlet == null) {
			Map<Object, Object> args = this.actionRequest.getArguments();
			outlet = (String)args.get((String)"Outlet");
		}
		
		try {
			String nextCommand = state.getNextStep();
			if (state.getState() == State.INPROGRESS) 
			{
				if (nextCommand.compareTo("EchoCommand") == 0) 
				{
					ModelCapabilityClient modelClient = new ModelCapabilityClient(resourceID);
					RequestModelResponse reqModel = modelClient.requestModel(true);
					PDUModel m = (PDUModel)(reqModel.getResourceModel());
					List<PDUElement> sensors_list = m.getInFeeds();
					if (sensors_list.size() > 0)
					{
						commandsList.clear();
						sensors = new ArrayList<String>();
						for (int pi=0; pi < sensors_list.size(); pi++)
						{
							String id = sensors_list.get(pi).getID();
							if (id.indexOf("ncurrsensor") > 0)
							{
								System.err.println("*************** QueryOutletAction: " + id);
								commandsList.add("ShowSensorCommand");
								sensors.add(id);
							}
						}
						state.setSteps(commandsList);
					}
					model_initialized = true;
				}
				
				if (model_initialized && (sensors.size() > 0))
				{
					if (index < sensors.size())
					{
						Map<Object, Object> cmd_args = new Hashtable<Object, Object>();
						cmd_args.put("Sensor", sensors.get(index));
						if (outlet != null)
							cmd_args.put("Outlet", outlet);
						this.actionRequest.setArguments(cmd_args);
					}
					if (index == 0)
						nextCommand = "ShowSensorCommand";
					index ++;
				}
				sendMessageToCommand(nextCommand);
			}
		} catch (CapabilityException e) {
			e.printStackTrace();
			throw new ActionException("", e);
		}
	}
	
}
