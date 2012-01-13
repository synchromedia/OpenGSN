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
package com.greenstarnetwork.resources.facilityResource.commandset;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.resources.facilityResource.core.Logger;
import com.iaasframework.capabilities.commandset.AbstractCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.CommandState.State;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.message.ICapabilityMessage;


/**
 * 
 * This command will update all the connected resources to the facility resource.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public abstract class BasicCommand extends AbstractCommand {

	protected FacilityModel facilityModel;
	private String COMMAND;
	protected Logger logger = null;

	public BasicCommand(String command2) {
		super(command2);
		COMMAND = command2;
		logger=Logger.getLogger();
	}

	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else{
			try {
				executeCommandMain();
				state.setState(State.RESPONSE_RECEIVED);
				requestEngineModel(false);
			} catch (Exception e) {
				logger.error("Something happend while executing "+COMMAND+" ...");
				logger.logException(e);
//preventing facility crash by exceptions
//				throw new CommandException(COMMAND+" :: "+e);
				facilityModel.setInitialized(false);
				facilityModel.getOperationalSpecs().setBatteryChargePercentage(-2);
				facilityModel.getOperationalSpecs().setOnGrid("TRUE");
				facilityModel.getOperationalSpecs().setOpHourUnderCurrentLoad(-2);
				facilityModel.getOperationalSpecs().setOpHourUnderMaximumLoad(-2);
				facilityModel.getOperationalSpecs().setStatus("EXCEPTION");
				facilityModel.getOperationalSpecs().setTotalConsummingPower(-2);
				facilityModel.getOperationalSpecs().setTotalGeneratingPower(-2);
				state.setState(State.RESPONSE_RECEIVED);
				requestEngineModel(false);
			}
		}
	}


	public abstract void executeCommandMain() throws Exception;

	@Override
	public synchronized void initializeCommand(IResourceModel model) throws CommandException {
		facilityModel = (FacilityModel)model;
	}

	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException {
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		this.model = facilityModel;
	}
	
	public void setModel(FacilityModel facilityModel) {
		this.facilityModel=facilityModel;
		
	}

}