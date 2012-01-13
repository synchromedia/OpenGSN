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

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.pdu.PDUElement.Status;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Get information of all outlets
 * @author knguyen
 *
 */
public class OutletCommand extends BasicSNMPCommand {

	public static final String COMMAND = "OutletCommand";	//Query command ID
	public static final String OID = "1.3.6.1.4.1.534.6.6.6.1.2";	//Object ID
	
	//definitions of sub objects
	private String outletCount 				= "1.3.6.1.4.1.534.6.6.6.1.2.1.0";
	private String outletTable 				= "1.3.6.1.4.1.534.6.6.6.1.2.2.";
	private String outletEntry 				= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.";
	private String outletIndex 				= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.1.";
	private String outletLabel 				= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.2.";
	private String outletOperationalState 	= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.3.";
	private String outletCurrent 			= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.4.";
	private String outletMaxCurrent			= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.5.";
	private String outletVoltage 			= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.6.";
	private String outletActivePower		= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.7.";
	private String outletApparentPower		= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.8.";
	private String outletPowerFactor		= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.9.";
	private String outletCurrentUpperWarning		= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.21.";
	private String outletCurrentUpperCritical		= "1.3.6.1.4.1.534.6.6.6.1.2.2.1.23.";
	
	
	
	public OutletCommand() {
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
				if (p.getOid().compareTo(outletCount) == 0)
				{//number of outlets
					
				}
				else if (p.getOid().startsWith(outletIndex)) {
					PDUElement elem = getOutletFromOID((PDUModel)arg0, p.getOid());
					if (elem == null) {
						elem = new PDUElement();
						elem.setID(p.getValue());
						((PDUModel)arg0).addOutlet(elem);
					}
				}
				else if (p.getOid().startsWith(outletLabel)) {
					PDUElement elem = getOutletFromOID((PDUModel)arg0, p.getOid());
					if (elem != null)
						elem.setName(p.getValue());
				}
				else if (p.getOid().startsWith(outletOperationalState)) {
					PDUElement elem = getOutletFromOID((PDUModel)arg0, p.getOid());
					if (elem != null)
					{
						if (p.getValue().compareTo("1") == 0)
							elem.setStatus(Status.On);
						else
							elem.setStatus(Status.Off);
					}
				}
				else if (p.getOid().startsWith(outletCurrent)) {
					PDUElement elem = getOutletFromOID((PDUModel)arg0, p.getOid());
					if (elem != null) {
						double d = new Double(p.getValue()).doubleValue();
						elem.setLoad(new Double(d/1000).toString());
					}
				}
				else if (p.getOid().startsWith(outletVoltage)) {
					PDUElement elem = getOutletFromOID((PDUModel)arg0, p.getOid());
					if (elem != null)
						elem.setVoltage(p.getValue());
				}
				else if (p.getOid().startsWith(outletActivePower)) {
					PDUElement elem = getOutletFromOID((PDUModel)arg0, p.getOid());
					if (elem != null)
						elem.setPower(p.getValue());
				}
				line = tokenizer.nextToken().trim();
			}
		}catch (NoSuchElementException e) {
		}
	}

	/**
	 * 
	 * @param m
	 * @param oid
	 * @return
	 */
	private PDUElement getOutletFromOID(PDUModel m, String oid) 
	{
		int pi = oid.lastIndexOf('.');
		if (pi < 0)
			return null;
		String id = oid.substring(pi+1);
		return m.getOutlet(id);
	}
}