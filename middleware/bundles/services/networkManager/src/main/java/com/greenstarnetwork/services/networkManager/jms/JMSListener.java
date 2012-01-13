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
import com.iaasframework.capabilities.actionset.api.ActionRequestMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Handling connection with other modules through a JMS queue 
 * 
 * @author knguyen
 *
 */
public class JMSListener implements MessageListener 
{
	Logger logger = LoggerFactory.getLogger(JMSListener.class);		
	
	private INetworkManager man = null;			//singleton NetworkManager instance
	
	protected static transient ConnectionFactory factory;
	protected transient Connection connection;
	protected transient Session session;
	private transient MessageProducer producer = null;
	private transient MessageConsumer messageConsumer = null;
	protected Destination destination = null;
	protected Message newestMessage = null;
	protected static final String brokerURL = "vm://localhost";

	String resourceId = null;
	
	public JMSListener(String resourceId) {
		this.resourceId = resourceId;
	}

	public void initializeJMS() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(resourceId);

		// Create the producer
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		// create the consumer and listen for message to this module
		String selector = "NetworkManager = '" + resourceId + "'";
		messageConsumer = session.createConsumer(destination, selector);
		messageConsumer.setMessageListener(this);
	}

	/**
	 * @param man the man to set
	 */
	public void setMan(INetworkManager man) {
		this.man = man;
	}

	/**
	 * @return the man
	 */
	public INetworkManager getMan() {
		return man;
	}

	@Override
	public void onMessage(Message msg) 
	{
		ObjectMessage message = (ObjectMessage) msg;

		try {
			ICapabilityMessage engineMessage = (ICapabilityMessage) message.getObject();
			if (engineMessage instanceof ActionRequestMessage) {
				logger.debug("Network Manager Get Request Message: " + engineMessage.toString());
				engineMessage.getRequestor();
				ResponseMessage ret = this.man.executeCommand( ((ActionRequestMessage)engineMessage).getRequest() );
				ret.setMessageID(engineMessage.getMessageID());
				Message outmessage = session.createObjectMessage(ret);
				outmessage.setStringProperty("CAPABILITY", engineMessage.getMessageID());
				outmessage.setJMSCorrelationID(engineMessage.getMessageID());
				producer.send(outmessage);
			}
			else {
				logger.debug("Netwwork Manager Get Invalid Message " + msg.toString());
			}
		}
		catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Release the JMS queue
	 */
	public void dispose(){
		try{
			messageConsumer.close();
			session.close();
			connection.close();
		}catch(JMSException ex){
			ex.printStackTrace();
		}
	}
}
