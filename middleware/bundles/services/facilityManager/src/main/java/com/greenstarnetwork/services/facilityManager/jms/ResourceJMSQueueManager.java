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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.services.facilityManager.FacilityManager;

/**
 * Manage JMS queues of facility resources.
 * Each Facility resource has a JMS queue where it periodically put Facility data model or warning messages.
 * The Facility Manager has to register with jms queues of all resources that it manages.
 * When there is an update from a queue, the ResourceJMSQueueManager will call updateFaciltyModel() function of the Facility Manager
 *  
 * @author knguyen
 *
 */
public class ResourceJMSQueueManager 
{
	Logger logger = LoggerFactory.getLogger(ResourceJMSQueueManager.class);		
	
	private FacilityManager man = null;			//singleton FacilityManager instance
	
	private transient List<JMSQueue> jmsQueues = null;	//list of all JMS sessions

	
	public ResourceJMSQueueManager() {
	}

	/**
	 * Add a new consumer registered with a new Facility resource JMS queue
	 * @param resourceId
	 * @throws JMSException
	 */
	public void addQueue(String resourceId, String domainId) throws Exception 
	{
		
		JMSQueue q = new JMSQueue();
		q.setParent(this);
		q.initializeJMS(resourceId, domainId);
		
		if (jmsQueues == null)
			jmsQueues = new ArrayList<JMSQueue>();
		jmsQueues.add(q);
	}
	
	/**
	 * Get a JMSQueue in the consumer list
	 * @param resourceId
	 * @return
	 * @throws JMSException
	 */
	public JMSQueue getQueue(String resourceId, String domainId) 
	{
		if (jmsQueues != null) {
			Iterator<JMSQueue> it = jmsQueues.iterator();
			while (it.hasNext()) {
				JMSQueue q = it.next();
				if ((q.getDomainID().compareTo(domainId) == 0) && (q.getQueueID().compareTo(resourceId) == 0))
					return q;
			}
		}
		return null;
	}

	/**
	 * Remove a queue from the list
	 * @param resourceId
	 * @throws JMSException
	 */
	public void removeQueue(String resourceId, String domainId) 
	{
		JMSQueue q = getQueue(resourceId, domainId);
		if (q != null)
			this.jmsQueues.remove(q);
	}
	
	/**
	 * @param man the man to set
	 */
	public void setMan(FacilityManager man) {
		this.man = man;
	}

	/**
	 * @return the man
	 */
	public FacilityManager getMan() {
		return man;
	}

	/**
	 * Cleanup queue manager resources
	 */
	public void dispose(){
		try{
			if (jmsQueues != null) {
				Iterator<JMSQueue> it = jmsQueues.iterator();
				while (it.hasNext()) {
					JMSQueue q = it.next();
					q.getMessageConsumer().close();
					q.getSession().close();
					q.getConnection().close();
				}
				jmsQueues.clear();
			}
		}catch(JMSException ex){
			ex.printStackTrace();
		}
	}
}
