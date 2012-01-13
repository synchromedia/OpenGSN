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
package com.greenstarnetwork.resources.facilityResource.core;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;

import com.greenstarnetwork.models.facilityModel.FacilityModel;

/**
 * JMS queue manager.
 * The JMS queue contains messages sent by the Facility Resource.
 * Facility Resource dumps periodically its data model to the queue, which will then be picked up by Facility Manager
 * The queue is initialized when the resource is created (in bootstrap).
 * 
 * @author knguyen
 *
 */
public class JMSQueue implements MessageListener 
{
//	protected static transient ConnectionFactory factory;
	protected static transient PooledConnectionFactory factory;
	protected transient Connection connection;
	protected transient Session session;
	private transient MessageProducer producer = null;
	protected Destination destination = null;

	private String resourceId = null;				//resourceID used to identify the queue
	private static JMSQueue instance = null;		//Singleton instance
	private BrokerService broker = null;
	
	public JMSQueue(String resourceId) {
		this.resourceId = resourceId;
	}

	public void initializeJMS(String domainId) throws Exception {
		String brokerURL = "tcp://" + domainId + ":61616";
		broker = new BrokerService();
		broker.setUseJmx(true);
		broker.setPersistent(false);
		broker.addConnector(brokerURL);
		broker.start();

//		factory = new ActiveMQConnectionFactory(brokerURL);
		factory = new org.apache.activemq.pool.PooledConnectionFactory(brokerURL); 
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(resourceId);

		// Create the producer
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}

	@Override
	public void onMessage(Message msg) 
	{
	}

	/**
	 * Cleanup jms resources when queue is released
	 */
	public void dispose(){
		try{
			session.close();
			connection.close();
		}catch(JMSException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Send facility data model to the JMS queue, which will later be picked up by Facility Manager
	 * @param model
	 * @throws JMSException
	 */
	public void sendModelToQueue(FacilityModel model) throws JMSException 
	{
		try {
			broker.deleteAllMessages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message outmessage = session.createObjectMessage(model);
		outmessage.setStringProperty("Facility", resourceId);
		producer.send(outmessage);
	}
	
	public void sendNotification(FacilityModel model, String msg) throws JMSException 
	{
		try {
			broker.deleteAllMessages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message outmessage = session.createObjectMessage(model);
		outmessage.setStringProperty("Facility", resourceId);
		outmessage.setStringProperty("Message", msg);
		producer.send(outmessage);
	}
	
	public static JMSQueue getJMSQueue() {
		return instance;
	}
	
	public static void setJMSQueue(JMSQueue queue) {
		instance = queue;
	}
	
}
