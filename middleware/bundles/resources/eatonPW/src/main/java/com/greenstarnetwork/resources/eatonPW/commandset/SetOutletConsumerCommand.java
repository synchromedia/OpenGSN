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
package com.greenstarnetwork.resources.eatonPW.commandset;

import java.util.Map;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.commandset.AbstractCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.CommandState.State;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Manually link a consumer to an outlet
 * @author knguyen
 *
 */
public class SetOutletConsumerCommand extends AbstractCommand {

	public static final String COMMAND = "SetOutletConsumerCommand";	//Query command ID
	private PDUModel pdumodel = null;

	public SetOutletConsumerCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException{
		if (!initialized) {
			initializeWithModel();
		}else
		{		
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (!args.containsKey((String)"Outlet"))
				throw new CommandException("Need Outlet parameter for SetOutletConsumerCommand!");
			if (!args.containsKey((String)"Consumer"))
				throw new CommandException("Need Consumer parameter for SetOutletConsumerCommand!");
			
			String outl = (String)args.get((String)"Outlet");

			String consumer = (String)args.get((String)"Consumer");

			try {
				state.setState(State.RESPONSE_RECEIVED);
				requestEngineModel(false);
			} catch (Exception e) {
				throw new CommandException(COMMAND + " " + e);
			}
			
			//check if the oulet has been registered in model
			PDUElement elem = this.pdumodel.getOutlet(outl);
			if (elem == null)
				throw new CommandException("Outlet ID not found!");
			
			elem.setConsumer(consumer);
		}
	}


	@Override
	public void initializeCommand(IResourceModel arg0) throws CommandException {
		this.pdumodel = (PDUModel)arg0;
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException {
		this.model = pdumodel;
	}

	@Override
	public void responseReceived(ICapabilityMessage arg0)
			throws CommandException {
		// TODO Auto-generated method stub
		
	}
}
