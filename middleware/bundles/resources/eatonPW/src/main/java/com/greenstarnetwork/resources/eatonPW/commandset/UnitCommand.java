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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Get information of PDU unit
 * @author knguyen
 *
 */
public class UnitCommand extends BasicSNMPCommand {

	public static final String COMMAND = "UnitCommand";	//Query command ID
	public static final String OID = "1.3.6.1.4.1.534.6.6.6.1.3.1";	//Object ID
	
	//definitions of sub objects
	private String unitCurrent 				= "1.3.6.1.4.1.534.6.6.6.1.3.1.1.0";
	private String unitVoltage 				= "1.3.6.1.4.1.534.6.6.6.1.3.1.2.0";
	private String unitActivePower			= "1.3.6.1.4.1.534.6.6.6.1.3.1.3.0";
	
	
	
	public UnitCommand() {
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
		
			sendCommandToProtocol("walk " + getOid());
		}
	}

	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException 
	{
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		
		try{
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken().trim();	//first line is the command
			while (line != null)
			{
				Pair p = parseReturnValue(line);
				if (p == null)
				{
//					throw new CommandException("No value is found for OID: " + (String)this.commandRequestMessage.getArguments().get((String)"OID"));
					break;
				}
				if (p.getOid().startsWith(unitCurrent)) {
					double d = new Double(p.getValue()).doubleValue();
					((PDUModel)arg0).setCurrent(new Double(d/1000).toString());
				}
				else if (p.getOid().startsWith(unitVoltage)) {
					double d = new Double(p.getValue()).doubleValue();
					((PDUModel)arg0).setVoltage(new Double(d/1000).toString());
				}
				else if (p.getOid().startsWith(unitActivePower)) {
					((PDUModel)arg0).setTotalPower(p.getValue());
				}
				line = tokenizer.nextToken().trim();
			}
		}catch (NoSuchElementException e) {
		}
	}
}