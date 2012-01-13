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
package com.greenstarnetwork.transports.snmp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.smi.Null;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.iaasframework.core.transports.IStreamTransport;
import com.iaasframework.core.transports.TransportException;

/**
 * 
 * @author knguyen
 *
 */
public class SNMPTransport implements IStreamTransport {
	
	public static final String SNMP = "SNMP";
	public static final String PROMPT = "snmp>";
	public static final String ERROR_PROMPT = "error>";
	
	/** SNMPTransport Logger */
	static private Logger logger = LoggerFactory.getLogger(SNMPTransport.class);
	
	/** The snmp client library **/
	private Snmp snmp = null;
	
	/** The host to connect */
	private String host = null;
	
	/** The port to connect */
	private String port = null;
	
	/** The read time out **/
	private static final int READ_TIMEOUT= 300000;
	
	private Address targetAddress = null;
	
	private Target target = null;
	
	//Piped stream to read commands from upper layers
	private PipedOutputStream writeout = null;				
	private PipedInputStream inputStream = null;
	
	//Empty piped to write to device
	private PipedOutputStream deviceout = null;
	
	public SNMPTransport(String host, String port){
		this.host = host;
		this.port = port;
		targetAddress = GenericAddress.parse("udp:" + host + "/" + port);
		try {
			writeout = new PipedOutputStream();
			inputStream = new PipedInputStream(writeout, 100000);
			deviceout = new PipedOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SNMPTransport(String host){
		this.host = host;
		this.port = "161";
		targetAddress = GenericAddress.parse("udp:" + host + "/" + port);
		try {
			writeout = new PipedOutputStream(); 
			inputStream = new PipedInputStream(writeout, 100000);
			deviceout = new PipedOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setupPublicCommunity() {
		target = getPublicCommunityTarget();
	}
	
	/**
	 * Create a SNMP session
	 */
	public void connect() throws TransportException {
		logger.info("SNMP Transport trying to connect...");
		System.err.println("SNMP Transport trying to connect...");

		try {
			TransportMapping transport = new DefaultUdpTransportMapping();
			//Create a new snmp client
			snmp = new Snmp(transport);
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			
			this.setupPublicCommunity();
			
			transport.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new TransportException("Could not connect, unable to open SNMP session "+ e.getMessage());
		}
	}

	/**
	 * Close SNMP session
	 */
	public void disconnect() throws TransportException {
		try{
			snmp.close();
			if (this.writeout != null)
				writeout.close();
			if (this.inputStream != null)
				inputStream.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new TransportException("Problems when disconnecting: "+e.getMessage());
		}
	}
	
	/**
	 * Execute GET command
	 * @param oid
	 * @return
	 * @throws TransportException
	 */
	public boolean get(String oid) throws TransportException {
		try{
			List<String> oids = new ArrayList<String>();
			oids.add(oid);
			PDU pdu = createPDU(oids, PDU.GETNEXT);
			return this.send(pdu);
		}catch(Exception e){
			e.printStackTrace();
			throw new TransportException("Problems when sending message: "+e.getMessage());
		}
	}

	/**
	 * Create a PDU
	 * @param oid
	 * @param type
	 * @return
	 */
	private PDU createPDU(List<String> oids, int type) 
	{
		PDU pdu = new PDU();
		Iterator<String> it = oids.iterator();
		while (it.hasNext()) 
		{
			String oid = it.next();
			String[] sids = oid.split(".");
			int[] ids = new int[sids.length];
			for (int pi=0; pi < sids.length; pi++)
				ids[pi] = new Integer(sids[pi]).intValue();
			
			pdu.add(new VariableBinding(new OID(ids)));
		}
		pdu.setType(type);
		return pdu;
	}
	
	/**
	 * Create a community target for this SNMP session
	 * @return
	 */
	public Target getPublicCommunityTarget() {
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(targetAddress);
		target.setRetries(2);
	    target.setTimeout(1500);
		target.setVersion(SnmpConstants.version1);
		return target;
	}
	
	/**
	 * Sent out a PDU and wait for response
	 * @param request
	 * @return
	 * @throws TransportException
	 */
	public boolean send(PDU request) throws TransportException {
		try{
			logger.debug("Message to be sent: " + request.toString());
			ResponseEvent response = snmp.send(request, target);
			if (response.getResponse() == null) {
			    // request timed out
				logger.debug("Request timeout while querying object.");
			}
			else {
				this.writeout.write((response.getResponse().get(0).toString() + "\n").getBytes());
				return true;
			}
		}catch(IOException e){
			e.printStackTrace();
			throw new TransportException("Problems when sending message: "+e.getMessage());
		}
		return false;
	}

	
	/**
	 * Walk all branches starting from a given oid
	 * @param oid
	 * @return
	 * @throws TransportException 
	 */
	public boolean walk(String soid) throws TransportException 
	{
//		System.err.println("Walking: " + soid);
		OID oid = new OID(soid);
		PDU requestPDU = new PDU();
        requestPDU.add(new VariableBinding(oid));
        requestPDU.setType(PDU.GETNEXT);

		boolean finished = false;

		try {
	        while (!finished) 
	        {
	            VariableBinding vb = null;

	            ResponseEvent respEvt;
				respEvt = snmp.send(requestPDU, target);
	            PDU responsePDU = respEvt.getResponse();
	            if (responsePDU != null) {
	                vb = responsePDU.get(0);
	            }

	            if (responsePDU == null) {
	                finished = true;
	            } else if (responsePDU.getErrorStatus() != 0) {
	                finished = true;
	            } else if (vb.getOid() == null) {
	                finished = true;
	            } else if (vb.getOid().size() < oid.size()) {
	                finished = true;
	            } else if (oid.leftMostCompare(oid.size(), vb.getOid()) != 0) {
	                finished = true;
	            } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
	                finished = true;
	            } else if (vb.getOid().compareTo(oid) <= 0) {
	                finished = true;
	            } else {
//	        		System.err.println("Got reply: " + vb.toString());
	        		this.writeout.write((vb.toString() + "\n").getBytes());
//	        		this.writeout.write(vb.toString().getBytes());
//	        		this.writeout.flush();
	                // Set up the variable binding for the next entry.
	                requestPDU.setRequestID(new Integer32(0));
	                requestPDU.set(0, vb);
	            }
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new TransportException("Problems when sending message: "+e.getMessage());
		}
		return true;
	}
	
	/**
	 * Write data to output stream, which will be read by upper layer
	 * A prompt will be added to the end of data stream
	 * @param data
	 */
	public void writeOutputStream(String data) throws TransportException {
		try {
			this.writeout.write(data.getBytes());
			this.writeout.write(new String("\n").getBytes());
			this.writeout.write(PROMPT.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException("Piped stream error: " + e.toString());
		}
	}
	
	@Override
	public InputStream getInputStream() throws TransportException {
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws TransportException {
		return deviceout;
	}

	@Override
	public void send(byte[] arg0) throws TransportException {
		send(new String(arg0));
	}

	@Override
	public void send(char[] arg0) throws TransportException {
		send(new String(arg0));
	}

	private void send(String s) throws TransportException {
		String[] parts = s.split(" ");
		String cmd = parts[0].toLowerCase();
		String param = parts[1].trim();
		boolean res = false;
		if (cmd.compareTo("get") == 0)
			res = get(param);
		else if (cmd.compareTo("walk") == 0)
			res = walk(param);
		else
			throw new TransportException("Invalid command: " + cmd);

		try {
			if (res == false)
				this.writeout.write(ERROR_PROMPT.getBytes());
			else
				this.writeout.write(PROMPT.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException("Piped stream error: " + e.toString());
		}
	}
}
