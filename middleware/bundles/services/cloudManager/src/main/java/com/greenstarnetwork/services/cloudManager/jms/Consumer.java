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
package com.greenstarnetwork.services.cloudManager.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import org.apache.activemq.ActiveMQConnectionFactory; 

import com.greenstarnetwork.services.cloudManager.ICloudManager;

/**
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class Consumer {
	
	private static transient ConnectionFactory factory;
	private transient Connection connection;
	private transient Session session;
	
	public Consumer(String brokerURL, String messageTopic, ICloudManager cloudManager) throws JMSException {
		
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Listener listener = new Listener(cloudManager);
		Destination destination = session.createTopic(messageTopic);
		MessageConsumer messageConsumer = session.createConsumer(destination);
		messageConsumer.setMessageListener(listener); 
	
	}
	
	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}
	
}
