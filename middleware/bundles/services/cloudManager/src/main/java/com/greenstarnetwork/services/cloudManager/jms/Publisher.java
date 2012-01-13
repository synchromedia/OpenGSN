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
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class Publisher {

	private static transient ConnectionFactory factory;
	private transient Connection connection;
	private transient Session session;
	private transient MessageProducer producer;
	private String topic;
	
	public Publisher(String brokerURL, String messageTopic) throws JMSException {
		topic = messageTopic;
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(null);
	}
	
	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}
	
	
	public Session getSession() {
		return session;
	}
	
	public void sendMessage(String message) throws JMSException {
		try{
			Destination destination = session.createTopic(topic);
			ObjectMessage mes = session.createObjectMessage(message);
			producer.send(destination, mes);
		}catch(JMSException e){
		} 
	}

}
