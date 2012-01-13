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
package com.greenstarnetwork.services.facilityManager.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.facilityModel.FacilityModel;

/**
 * Data model of a JMS Queue
 * A queue is represented by a domain ID and a queue ID.
 * Domain ID is the IP address of the host where queue is created.
 * 
 * @author knguyen
 *
 */
public class JMSQueue  implements MessageListener {

	Logger logger = LoggerFactory.getLogger(JMSQueue.class);
	
	private String queueID = null;								//Queue ID
	private String domainID = null;								//Domain ID
	private transient Connection connection = null;				//JMS connection handler
	private transient MessageConsumer messageConsumer = null;	//consumer handler
	private transient Session session = null;					//JMS session handler
	
	private ResourceJMSQueueManager parent = null;
	
	public JMSQueue() {
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param messageConsumer the messageConsumer to set
	 */
	public void setMessageConsumer(MessageConsumer messageConsumer) {
		this.messageConsumer = messageConsumer;
	}

	/**
	 * @return the messageConsumer
	 */
	public MessageConsumer getMessageConsumer() {
		return messageConsumer;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param queueID the queueID to set
	 */
	public void setQueueID(String queueID) {
		this.queueID = queueID;
	}

	/**
	 * @return the queueID
	 */
	public String getQueueID() {
		return queueID;
	}

	/**
	 * @param domainID the domainID to set
	 */
	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}

	/**
	 * @return the domainID
	 */
	public String getDomainID() {
		return domainID;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ResourceJMSQueueManager parent) {
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	public ResourceJMSQueueManager getParent() {
		return parent;
	}

	/**
	 * Connect to a JMS queue created by a resource on a given domain
	 * @param resourceId
	 * @param domainId
	 * @throws Exception 
	 */
	public void initializeJMS(String resourceId, String domainId) throws Exception 
	{
		this.setQueueID(resourceId);
		this.setDomainID(domainId);
		
		String brokerURL = "tcp://" + domainId + ":61616";
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination destination = session.createQueue(resourceId);

		// create the consumer and listen for message to this module
		String selector = "Facility = '" + resourceId + "'";
		
		messageConsumer = session.createConsumer(destination, selector);
		messageConsumer.setMessageListener(this);
	}
	
	/**
	 * JMS message handling
	 * FacilityManager is called when a new model received.
	 */
	@Override
	public void onMessage(Message msg) 
	{
		ObjectMessage message = (ObjectMessage) msg;
		try {
			Object o = message.getObject();
			if (o instanceof FacilityModel) 
			{
				FacilityModel model = (FacilityModel)o;
				logger.debug("FacilityManager - Update model: " + model.toXML());
				this.parent.getMan().updateFaciltyModel(this.queueID, this.domainID, model);
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
