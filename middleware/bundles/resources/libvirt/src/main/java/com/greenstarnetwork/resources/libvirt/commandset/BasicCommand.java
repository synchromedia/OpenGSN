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
package com.greenstarnetwork.resources.libvirt.commandset;

import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Basic command contains elementary functions
 * 
 * @author knguyen
 *
 */
public abstract class BasicCommand extends AbstractCommandWithProtocol {

	public BasicCommand(String commandID) {
		super(commandID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeCommand(IResourceModel arg0) throws CommandException {
    	initialized = true;
	}

	@Override
	public void responseReceived(ICapabilityMessage response)
			throws CommandException {
		if (response instanceof ProtocolResponseMessage) {
			this.response = (ProtocolResponseMessage) response;
			this.requestEngineModel(false);
		}
		else if (response instanceof ProtocolErrorMessage) {
			this.errorMessage = (ProtocolErrorMessage) response;
			CommandException commandException = new CommandException("Error executing command "
					+ commandID, ((ProtocolErrorMessage) response)
					.getProtocolException());
			commandException.setName(commandID);
			commandException.setCommand(this);
			this.sendCommandErrorResponse(commandException);
		}
	}

	/**
	 * Get a property parameter from a line. The value of the parameter can be found right after ':'
	 * @param line
	 * @return
	 */
	protected String getLineProp(String line) {
		if (line.indexOf(":") > -1)
			return line.substring(0, line.indexOf(":"));
		return null;
	}

	/**
	 * Remove special character from a string representing a param's value
	 * @param source
	 * @return
	 */
	protected String reformatString(String source) {
		int i = 0;
		while (i < source.length()) {
			if (Character.isDigit(source.charAt(i)))
				break;
			i ++;
		}
		int j = i;
		while (j < source.length()) {
			char ch = source.charAt(j);
			if ( !Character.isDigit(ch) && (ch != '.') ) //!Character.isLetter(ch) 
				break;
			j ++;
		}
		
		return source.substring(i, j);
	}
}
