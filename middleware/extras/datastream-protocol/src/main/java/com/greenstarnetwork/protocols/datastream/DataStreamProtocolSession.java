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
package com.greenstarnetwork.protocols.datastream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.protocols.datastream.message.DataStreamInputMessage;
import com.greenstarnetwork.protocols.datastream.message.DataStreamResponseMessage;
import com.iaasframework.capabilities.protocol.AbstractProtocolSession;
import com.iaasframework.capabilities.protocol.IProtocolConstants;
import com.iaasframework.capabilities.protocol.ProtocolException;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolRequestMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.core.transports.IStreamTransport;
import com.iaasframework.core.transports.ITransport;
import com.iaasframework.core.transports.TransportException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;
import com.iaasframework.resources.core.descriptor.CapabilityProperty;

/**
 * Implementation of a DataStream protocol, which is reading a dumping stream from transport
 * User may define the number of lines of data the protocol will receive from the output stream.
 * 
 * @author knguyen
 *
 */
public class DataStreamProtocolSession extends AbstractProtocolSession{
	
	public static final String MODULE_NAME="DataStream Protocol Session";
	public static final String MODULE_DESCRIPTION="DataStream Protocol library";
	public static final String MODULE_VERSION="1.0.0";
	
	/** DataStream Session Log */
    static private Logger logger = LoggerFactory.getLogger(DataStreamProtocolSession.class);
    
    /** Some constants **/
    public static final String HAS_GREETING = "Greeting";  //DataStream has to wait for a greeting message from transport
    public static final String NUMBER_OF_LINES = "lines";
    
	/** Number of lines to read */
	private int number_of_lines = 0;
    
    /** The class that will listen for response messages **/
    private DataStreamReader cliStreamReader;
    
    /** Contains information about the protocol capability configuration: transport, host, port, ... **/
    private CapabilityDescriptor capabilityDescriptor;
    
    /** the transport to communicate with the managed entity **/
    private IStreamTransport transport;
    
    private ProtocolRequestMessage currentProtocolMessage = null;
    
    public DataStreamProtocolSession(CapabilityDescriptor capabilityDescriptor) throws ProtocolException{
        this.capabilityDescriptor = capabilityDescriptor;
        setNumberOfLine(capabilityDescriptor.getCapabilityProperties());
    }

    private void setNumberOfLine(List<CapabilityProperty> capabilityProperties) throws ProtocolException {
    	CapabilityProperty currentProperty = null;
        for(int i=0; i<capabilityProperties.size(); i++){
        	currentProperty = capabilityProperties.get(i);
        	if (currentProperty.getName().equals(NUMBER_OF_LINES)){
        		try {
        			number_of_lines = Integer.parseInt(currentProperty.getValue());
        		}catch (NumberFormatException e) {
        			throw new ProtocolException("Number of line parameter is invalid! " + e.toString());
        		}
        		break;
        	}
        }
    }
    
    public void wireTransport(ITransport transport) throws ProtocolException{
    	if  (!(transport instanceof IStreamTransport)){
    		throw new ProtocolException("DataStream transports must be stream transports");
    	}
    	
    	this.transport = (IStreamTransport) transport;
    }
    
    public void run(){
    	try{
    		startSession();

    		while (!isDisposed()){
    			try{
    				currentProtocolMessage = incomingRequestsQueue.take();
    				if (!currentProtocolMessage.isLastMessage()){
    					try{
    						Object deviceResponse = sendWaitResponse(currentProtocolMessage.getMessage());
    						sendResponseToProtocolCapability(deviceResponse, currentProtocolMessage.getMessageID());
    					}catch(ProtocolException ex){
    						sendErrorResponseToProtocolCapability(ex, currentProtocolMessage.getMessageID());
    					}
    				}
    			}catch(InterruptedException ex){
    				ex.printStackTrace();
    			}
    		}

    		stopSession();
    	}catch(ProtocolException ex){
    		ex.printStackTrace();
    		//TODO tell the protocol engineModule we're in trouble
    	}
    }
    
    /**
     * Opens a connection to the specific host/port
     */
    private void startSession() throws ProtocolException{
    	logger.info("Starting datastream protocol session");
    	try{
    		transport.connect();
    	}catch(TransportException ex){
    		throw new ProtocolException("Problems connecting to the managed device", ex);
    	}
    	createDataStreamStreamReader();
    	loginToDevice(capabilityDescriptor);
    }
    
    public void restartSession(){
    	if (!isDisposed()){
    		try{
    			stopSession();
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}

    		try{
    			transport.connect();
    			loginToDevice(capabilityDescriptor);
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
    	}
    }
    
    private void sendResponseToProtocolCapability(Object responseMessage, String correlation){
    	ProtocolResponseMessage protocolResponseMessage = new ProtocolResponseMessage();
    	protocolResponseMessage.setProtocolMessage(responseMessage);
    	protocolCapability.sendSessionResponse(protocolResponseMessage, correlation);
    }
    
    private void sendErrorResponseToProtocolCapability(ProtocolException protocolException, String correlation){
    	ProtocolErrorMessage protocolErrorMessage = new ProtocolErrorMessage();
    	protocolErrorMessage.setException(protocolException);
    	protocolCapability.sendSessionErrorResponse(protocolErrorMessage, correlation);
    }
    
    
    private void createDataStreamStreamReader() throws ProtocolException{
    	try{
    		cliStreamReader = new DataStreamReader(getTransport().getInputStream(), 
    				getTransport().getOutputStream(), number_of_lines);
    		cliStreamReader.start();
    	}catch(TransportException ex){
    		throw new ProtocolException("Problems getting the transport input stream", ex);
    	}
    }
    
    private IStreamTransport getTransport(){
    	return transport;
    }
    
    private void loginToDevice(CapabilityDescriptor capabilityDescriptor) throws ProtocolException{
    	if (capabilityDescriptor.getProperty(DataStreamProtocolSession.HAS_GREETING)!=null){
    			cliStreamReader.getResponse("");//wait for greeting message
    	}
    	if (capabilityDescriptor.getProperty(IProtocolConstants.PROTOCOL_USERNAME)!=null){
    		String username = capabilityDescriptor.getProperty(IProtocolConstants.PROTOCOL_USERNAME).getValue();
    		sendWaitResponse(username);
    	}
 		
    	if (capabilityDescriptor.getProperty(IProtocolConstants.PROTOCOL_PASSWORD)!=null){
    		String password = capabilityDescriptor.getProperty(IProtocolConstants.PROTOCOL_PASSWORD).getValue();
    		sendWaitResponse(password);
    	}
    }

    /**
     * Stop the protocol session: log out, stop the session keep-alive thread, disconnect the transport
     */
    public void stopSession() throws ProtocolException {
    	try{
    		getTransport().disconnect();
    	}catch(TransportException ex){
    		throw new ProtocolException("Problems disconnecting the transport", ex);
    	}
    }

	/**
     * Send a message without waiting for the response
     */
	public synchronized void sendDontWaitResponse(Object message) throws ProtocolException {
		if (message instanceof DataStreamInputMessage) {
			DataStreamInputMessage request = (DataStreamInputMessage) message;
			this.sendCmdNoWait(request.toString());
		}else{
			this.sendCmdNoWait((String) message);
		}
	}

	/**
	 * Sends an array of messages without waiting for the response
	 */
	public synchronized void sendDontWaitResponse(Object[] messageList) throws ProtocolException {
		for (int i = 0; i < messageList.length; i++) {
            sendDontWaitResponse(messageList[i]);
        }
	}

	/**
     * Sends out DataStream command to the agent.
     * 
     * @param message
     *            Command to send in DataStreamInputMessage Format
     * @return DataStreamResponseMesssage
     * @throws FailedCmdException
     *             Exception thrown if command failed
     */
	public synchronized Object sendWaitResponse(Object message) throws ProtocolException {
		Object msg;
		if (message instanceof DataStreamInputMessage) {
			DataStreamInputMessage request = (DataStreamInputMessage) message;
			msg = (Object) this.sendCmdWait(request.toString());
		}else{
			msg = (Object) this.sendCmdWait((String) message);
		}
		return msg;
	}

	/**
     * Sends an array of DataStream commands to the device
     */
	public synchronized Object[] sendWaitResponse(Object[] messageList) throws ProtocolException {
		Object[] msg = new Object[messageList.length];
        for (int i = 0; i < messageList.length; i++) {
            msg[i] = sendWaitResponse(messageList[i]);
        }
        return msg;
	}
	
	/**
	 * Sends a DataStream message and waits for the response
	 * @param message
	 * @return
	 */
	private synchronized DataStreamResponseMessage sendCmdWait(String message) throws ProtocolException{
		return cliStreamReader.getResponse(message);
	}
	
	 /**
     * Sends a command to the stream transport, without waiting for the request
     * @param request
     * @throws ProtocolException
     */
	private void sendCmdNoWait(String request) throws ProtocolException{
	}
}