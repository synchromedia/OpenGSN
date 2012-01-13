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
package com.greenstarnetwork.services.networkManager.jms;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.services.networkManager.INetworkManager;
import com.greenstarnetwork.services.networkManager.NetworkManagerException;
import com.iaasframework.capabilities.actionset.api.ActionRequestMessage;
import com.iaasframework.capabilities.actionset.queue.ActionRequest;
import com.iaasframework.resources.core.message.ICapabilityMessage;


/**
 * Helper class for the Network Manager that clients can use to call functions.
 * This class provides the JMS producer and consumer that is required to send and
 * receive messages to/from the Network Manager.
 * 
 * @author knguyen
 *
 */
public class NetworkManagerJMSClient implements MessageListener 
{
	Logger logger = LoggerFactory.getLogger(NetworkManagerJMSClient.class);		

	public static String NETWORK_MANAGER_CLIENT_ID = "Network-Manager-Client-V-10";
	protected Session jmsSession = null;
	protected MessageProducer messageProducer = null;
	protected MessageConsumer messageConsumer = null;
	private Connection connection = null;
	protected Destination destination = null;
	protected ArrayBlockingQueue<Message> receivedMessages = null;
	
	public NetworkManagerJMSClient() 
	{
		ConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
		receivedMessages = new ArrayBlockingQueue<Message>(1);
		try{
			connection = factory.createConnection();
			connection.start();
			jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = jmsSession.createTopic(INetworkManager.DEFAULT_ID);
			messageProducer = jmsSession.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}catch(JMSException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Client may call this function in order to execute a Network Manager function (e.g., IP assignment, release) 
	 * 
	 * @param command
	 * @param arguments
	 * @return
	 * @throws NetworkManagerException
	 */
	public synchronized ResponseMessage executeCommand(String command, Map<Object, Object> arguments) throws NetworkManagerException 
	{
		String messageID = UUID.randomUUID().toString();
		ActionRequest request = new ActionRequest();
		request.setActionID(command);
		request.setArguments(arguments);
		ActionRequestMessage requestMessage = new ActionRequestMessage();
		requestMessage.setRequest(request);
		requestMessage.setRequestor(messageID);
		requestMessage.setMessageID(messageID);
		try{
			messageConsumer = jmsSession.createConsumer(destination, this.getMessageSelector(messageID));
			messageConsumer.setMessageListener(this);
			Message jmsMessage = jmsSession.createObjectMessage(requestMessage);
			jmsMessage.setStringProperty("NetworkManager", INetworkManager.DEFAULT_ID);
			messageProducer.send(jmsMessage);
			Message rcvMessage = this.receive();

			messageConsumer.close();
			
			ICapabilityMessage responseMessage = (ICapabilityMessage) ((ObjectMessage)rcvMessage).getObject();
			logger.debug("Reveived message response "+ responseMessage.getMessage());
			if (responseMessage instanceof ResponseMessage) {
				return (ResponseMessage)responseMessage;
			}else
				logger.debug("Unknown message: " + responseMessage.getClass().getName());
			return null;
		}catch(Exception ex){
			throw new NetworkManagerException(ex.toString());
		}
	}

	protected String getMessageSelector(String messageID){
		return "CAPABILITY = '" + messageID + "'";
	}

	@Override
	public void onMessage(Message msg) {
		receivedMessages.add(msg);
	}
	
	protected Message receive() throws InterruptedException{
		return receivedMessages.take();
	}
	/**
	 * Just call it if you are not going to use this class any more, it will free
	 * the resources it was using
	 */
	public void dispose(){
		try{
			jmsSession.close();
			connection.close();
		}catch(JMSException ex){
			ex.printStackTrace();
		}
	}
	
}
