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
package com.greenstarnetwork.services.controller.manager;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.services.controller.core.IController;

/**
 * 
 * @author knguyen
 *
 */
public class FacilityManagerJMSQueue implements MessageListener 
{
	private static String CONTROLLER_ID = "GSN-Controller";

	protected static transient ConnectionFactory factory;
	protected transient Connection connection;
	protected transient Session session;
	protected Destination destination = null;
	private transient MessageConsumer messageConsumer = null;	//consumer handler
	protected static final String brokerURL = "vm://localhost";
	
	private IController parent = null;
	
	public FacilityManagerJMSQueue() {
	}

	public void initializeJMS() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(CONTROLLER_ID);

		messageConsumer = session.createConsumer(destination);
		messageConsumer.setMessageListener(this);
	}

	@Override
	public void onMessage(Message msg) 
	{
		ObjectMessage message = (ObjectMessage) msg;
		try {
			Object o = message.getObject();
			if (o instanceof FacilityModel) 
			{
				FacilityModel model = (FacilityModel)o;
//				System.err.println("Controller Receive model: " + model.toXML());
				if (this.parent != null) {
					this.parent.changeFacilityModel(model);
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * @param parent the parent to set
	 */
	public void setParent(IController parent) {
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	public IController getParent() {
		return parent;
	}

}
