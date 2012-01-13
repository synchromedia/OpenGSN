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

import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.commandset.AbstractCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.CommandState.State;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Reset power and load of the PDU to 0.
 * @author knguyen
 *
 */
public class ResetPowerCurrentCommand extends AbstractCommand{

	public static final String COMMAND = "ResetPowerCurrentCommand";	//Query command ID
	
	private PDUModel pduModel = null;
	
	public ResetPowerCurrentCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException 
	{
		if (!initialized) {
			initializeWithModel();
		}else
		{
			this.pduModel.setCurrent(null);
			this.pduModel.setTotalPower(null);
			state.setState(State.RESPONSE_RECEIVED);
			requestEngineModel(false);
		}
	}
	
	@Override
	public synchronized void initializeCommand(IResourceModel model) throws CommandException {
		this.pduModel = (PDUModel)model;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		this.model = pduModel;
	}
	
	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException {
	}
}
