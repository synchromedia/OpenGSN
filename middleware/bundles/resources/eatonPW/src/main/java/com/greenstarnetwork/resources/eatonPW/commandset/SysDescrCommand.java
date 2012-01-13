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

import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

public class SysDescrCommand extends BasicSNMPCommand {

	public static final String COMMAND = "SysDescrCommand";	//Query command ID
	public static final String OID = "1.3.6.1.2.1.1.1.0";	//Object ID

	public SysDescrCommand() {
		super(COMMAND);
		setOid(OID);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			if (this.getOid() == null)
				throw new CommandException("Invalid command argument!. BasicSNMPCommand OID");
		
			sendCommandToProtocol("get " + getOid());
		}
	}

	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException {
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		
		Pair p = parseReturnValue(s);
		if (p == null)
			throw new CommandException("No value is found for OID: " + 
					(String)this.commandRequestMessage.getArguments().get((String)"OID"));

		System.err.println("System descriptor: " + p.getValue() + " - oid: " + p.getOid());
	}

}
