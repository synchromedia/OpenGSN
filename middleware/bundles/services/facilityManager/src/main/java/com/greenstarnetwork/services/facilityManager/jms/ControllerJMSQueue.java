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
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.greenstarnetwork.models.facilityModel.FacilityModel;

/**
 * Handling the Controller's JMS queue.
 * The Controller's JMS queue is created by a Controller and its address is registered with the FacilityManager when
 * the Controller calls the Facility Manager's registerControllerJMS() function through WS.
 * The Facility Manager sends warning messages to the Controller through this queue
 * 
 * @author knguyen
 *
 */
public class ControllerJMSQueue implements MessageListener 
{
	private static String CONTROLLER_ID = "GSN-Controller";

	protected static transient ConnectionFactory factory;
	protected transient Connection connection;
	protected transient Session session;
	private transient MessageProducer producer = null;
	protected Destination destination = null;
	protected static final String brokerURL = "vm://localhost";

	public ControllerJMSQueue(String address) {
//		brokerURL = address;
	}

	public void initializeJMS() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(CONTROLLER_ID);

		// Create the producer
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}

	@Override
	public void onMessage(Message msg) 
	{
	}

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
	 * @param msgID
	 * @throws JMSException
	 */
	public void sendModelToQueue(FacilityModel model) throws JMSException 
	{
		Message outmessage = session.createObjectMessage(model);
//		outmessage.setStringProperty("Controller", CONTROLLER_ID);
		producer.send(outmessage);
	}
	
}
