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
package com.greenstarnetwork.resources.libvirt.core;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.jms.JMSException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.greenstarnetwork.resources.libvirt.core.jms.Consumer;
import com.greenstarnetwork.resources.libvirt.core.jms.Publisher;


/**
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class HostMonitor implements Runnable {
	
	private Logger logger = LoggerFactory.getLogger(HostMonitor.class);

	//thread for physical host monitoring
	private Thread monitoringThread = null;
	
	//Lease expiry date
	private long leaseExpiryDate;
	
	//Tolerance time which trigger an HA Event if depassed
	//30000 ms is used to enable a host restart or a disc mount without triggering error
	//If this delay is depassed, the HA event is triggered
	private long expiryToleranceTime = 30000;
	
	//The lease expiry date is checked every 5000 ms
	private int monitoringInterval = 5000;
	
	//JMS parameters
	private Publisher hostPublisher;
	private Consumer hostConsumer;
	private String brokerPort = "61616";
	private String messageTopic = "channel1";
	
	private Publisher cloudManagerPublisher;
	private final String cloudManagerTopic = "channel1";
	
	//Variable used internally by the class
	private boolean hasError;
	
	private String ip;
	
	
	public HostMonitor(String hostIP) throws JMSException{
		
		hasError = false;
		ip = hostIP;
		
		File f = new File(System.getenv("IAAS_HOME")+"/gsn/libvirt.xml");
		if(f.exists()){
			
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db;
		     Document doc = null;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(f);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
			 NodeList nodes = doc.getElementsByTagName("HostMonitor");
			 
			 if(nodes.getLength() > 0){
				 
				monitoringInterval = Integer.parseInt( nodes.item(0).getChildNodes().item(1).getFirstChild().getNodeValue() );
				expiryToleranceTime = Long.parseLong( nodes.item(0).getChildNodes().item(3).getFirstChild().getNodeValue() );
				brokerPort = nodes.item(0).getChildNodes().item(5).getFirstChild().getNodeValue();
				messageTopic = nodes.item(0).getChildNodes().item(7).getFirstChild().getNodeValue();
				
				leaseExpiryDate = Calendar.getInstance().getTimeInMillis();
				leaseExpiryDate += monitoringInterval;
				
				logger.debug("********* Libvirt : HostMonitor "+ip+" : Initializing JMS for host daemon *********");
				String brokerURL = "tcp://" + hostIP + ":" + brokerPort;
				hostPublisher = new Publisher(brokerURL, messageTopic);
				hostConsumer = new Consumer(brokerURL, messageTopic, this);
				
				//monitoringInterval in ms
				logger.debug("********* Libvirt : HostMonitor "+ip+" : Handshake with daemon *********");
				hostPublisher.sendMessage("Handshake: " + monitoringInterval);
				
				monitoringThread = new Thread(this, "monitoringThread");
				monitoringThread.start();
				
			 }else{
				 
				 logger.debug("********* Libvirt: HostMonitor "+ip+" : No configuration file found... HostMonitor not started *****");
			 }
			 
		}else{
			logger.debug("********* Libvirt: HostMonitor "+ip+" : No configuration file found... HostMonitor not started *****");
		}
		
	}

	@Override
	public void run() 
	{
		boolean handshaked = false;
		while(true){
			try {
				
				Thread.sleep(monitoringInterval);
				if (!handshaked)
				{
					try 
					{
						if (cloudManagerPublisher == null)
						{
							try {
								logger.debug("********* Libvirt : HostMonitor "+ip+" : Initializing publisher for cloud manager *********");
								InetAddress thisIp =InetAddress.getLocalHost();
								cloudManagerPublisher = new Publisher("tcp://" + thisIp.getHostAddress() + ":61616", cloudManagerTopic);
							}
							catch(UnknownHostException e) {
								e.printStackTrace();
								System.err.println(e.toString());
							}
						}
						if (cloudManagerPublisher != null)
							logger.debug("********* Libvirt : HostMonitor "+ip+" : Sending handshake *********");
							cloudManagerPublisher.sendMessage("HostMonitor: Handshake");
							logger.debug("********* Libvirt : HostMonitor "+ip+" : Handshake sent! *********");
							handshaked = true;
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else 
				{
					if(!hasError && (leaseExpiryDate < Calendar.getInstance().getTimeInMillis()) ){
						
						//Lease has expired: Waiting during tolerance time
						logger.debug("********* Libvirt : HostMonitor "+ip+" :  Lease has expired. Waiting during tolerance time. *********");
						boolean repaired = false;
						int currentdelay = 0;
						while(!hasError && !repaired){
							Thread.sleep(1000);
							currentdelay += 1000;
							if(!hasError && leaseExpiryDate >= Calendar.getInstance().getTimeInMillis()){
								logger.debug("********* Libvirt : HostMonitor "+ip+" :  Error repaired after lease expiration. *********");
								repaired = true;
							}else if(currentdelay > expiryToleranceTime){
								hasError = true;
								//Error detected due to lease expiration
								try {
									logger.debug("********* Libvirt : HostMonitor "+ip+" :  Lease has expired. Sending Message to cloud manager *********");
									if (cloudManagerPublisher != null)
										cloudManagerPublisher.sendMessage("HA event: host " + ip + " disabled");
								} catch (JMSException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									System.err.println(e.toString());
								}
							}
						}
						
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onMessage(String msg){
		
		logger.debug("********* Libvirt : HostMonitor "+ip+" : Received message from daemon : "+msg+" *********");
		
		if(msg.compareTo("ok") == 0 ){
			
			leaseExpiryDate = Calendar.getInstance().getTimeInMillis();
			leaseExpiryDate += monitoringInterval;
			
			if(hasError == true){
				//Host recupered from error.
				try {
					logger.debug("********* Libvirt : HostMonitor "+ip+" : notification sent to cloud manager *********");
					if (cloudManagerPublisher != null)
						cloudManagerPublisher.sendMessage("HA event: host " + ip + " active");
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			hasError = false;
			
		}else if (msg.compareTo("error") == 0 ){
			hasError = true;
			//Host Daemon has reported an error
			try {
				logger.debug("********* Libvirt : HostMonitor "+ip+" : notification sent to cloud manager *********");
				cloudManagerPublisher.sendMessage("HA event: host " + ip + " disabled");
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
}
