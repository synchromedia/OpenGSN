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
package com.greenstarnetwork.resources.libvirt.core.jms;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage; 

import org.apache.activemq.command.ActiveMQTextMessage;

import com.greenstarnetwork.resources.libvirt.core.HostMonitor;

/**
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class Listener implements MessageListener{
	
	private HostMonitor monitor;
	
	public Listener(HostMonitor hostMonitr){
		monitor = hostMonitr;
	}

	public void onMessage(Message message) {
	
		try{
			
			if(message.getClass().getName().compareTo("org.apache.activemq.command.ActiveMQTextMessage") == 0 ){
				ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
				monitor.onMessage(msg.getText());
			}
			
			if(message.getClass().getName().compareTo("org.apache.activemq.command.ActiveMQObjectMessage") == 0 ){
				ObjectMessage msg = (ObjectMessage) message;
				if(msg.getObject().getClass().getName().compareTo("java.lang.String") == 0 ){
					String ms = (String)msg.getObject();
					monitor.onMessage(ms);
				}
			}
	
		} catch (JMSException e) {
			e.printStackTrace();
		}
	} 
	
}

