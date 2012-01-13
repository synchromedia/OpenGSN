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
package com.greenstarnetwork.resources.eatonPW.actionset;

import java.util.ArrayList;
import java.util.List;

import com.greenstarnetwork.resources.eatonPW.commandset.SetOutletConsumerCommand;
import com.iaasframework.capabilities.actionset.AbstractActionWithCommandSet;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionState.State;
import com.iaasframework.resources.core.capability.CapabilityException;

/**
 * Set consumer alias to a PDU's outlet
 * 
 * @author knguyen
 *
 */
public class SetOutletConsumerAction extends AbstractActionWithCommandSet{

	public static final String ACTION = "SetOutletConsumerAction";
	private List<String> commandsList = null;
	
	public SetOutletConsumerAction() {
		super(ACTION);
		initializeCommandsList();
	}
	
	private void initializeCommandsList(){
		commandsList = new ArrayList<String>();
		commandsList.add(SetOutletConsumerCommand.COMMAND);
		state.setSteps(commandsList);
	}
	
	@Override
	public void executeAction() throws ActionException
	{
		try {
			String nextCommand = state.getNextStep();
			if (state.getState() == State.INPROGRESS) 
			{
				sendMessageToCommand(nextCommand);
			}
		} catch (CapabilityException e) {
			e.printStackTrace();
			throw new ActionException("", e);
		}
	}
}
	
