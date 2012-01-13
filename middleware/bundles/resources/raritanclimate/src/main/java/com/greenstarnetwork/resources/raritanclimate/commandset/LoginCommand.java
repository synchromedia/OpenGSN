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

import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.message.ICapabilityMessage;

public class LoginCommand  extends AbstractCommandWithProtocol{

	public static final String COMMAND = "LoginCommand";	//Query command ID
	public LoginCommand() {
		super(COMMAND);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void executeCommand() throws CommandException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeCommand(IResourceModel arg0) throws CommandException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void responseReceived(ICapabilityMessage arg0)
			throws CommandException {
		// TODO Auto-generated method stub
		
	}
}
