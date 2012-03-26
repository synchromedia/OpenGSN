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
package com.opengsn.controller.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greenstarnetwork.services.cloudmanager.ICloudManager;
import com.greenstarnetwork.services.cloudmanager.IdPriority;
import com.greenstarnetwork.services.cloudmanager.OverrideMapType;
import com.greenstarnetwork.services.cloudmanager.OverrideMapType.PrioritiesList;
import com.opengsn.controller.model.Host;
import com.opengsn.services.cloudmanager.model.VmHostModel;

/**
 * Handling the connection with the Cloud Manager
 * @author knguyen
 *
 */
public class CloudManagerClient 
{
	Logger log = LoggerFactory.getLogger(CloudManagerClient.class);
	
	//Resource ID tag involved in a XML string representing a resource data model
	public static String RESOURCE_ID_ELEMENT = "<resourceId>";
	//Resource Name tag involved in a XML string representing a resource data model
	public static String RESOURCE_NAME_ELEMENT = "<resourceName>";
	//Domain ID tag involved in a XML string representing a resource data model
	public static String DOMAIN_ID_ELEMENT = "<domainId>";

	private ICloudManager cloudMgr = null;
	
	public CloudManagerClient() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		           new String[]{"webapp/WEB-INF/applicationContext.xml"});		
		cloudMgr = (ICloudManager) context.getBean("cloudManagerClient");
	}

	/**
	 * Get Host Resource Models of a given Cloud Manager
	 * @return
	 */
	public List<Host> getHostList() 
	{
		List<String> hostlist = cloudMgr.describeHosts();
		if (hostlist != null) {
			Iterator<String> it = hostlist.iterator();
			List<Host> ret = new ArrayList<Host>();
			while (it.hasNext()) {
				Host host = this.getHostFromXML((String)it.next());
				if (host != null)
					ret.add(host);
			}
			if (ret.size() > 0)
				return ret;
		}
		return null;
	}


	/**
	 * Execute a migration action. 
	 */
	public String migrateVM(String hostId, String vmName, String destHostId) 
	{
		String response = cloudMgr.migrateInstance(hostId, vmName, destHostId);
		log.debug("Migrate Response: " + response);
		return response;
	}

	/**
	 * Update host priority list
	 * @param list
	 */
	public void updateHostPriorityTable(Map<String, Integer> list) {
		
		PrioritiesList pList = new PrioritiesList();
		
		Set<String> keys = list.keySet();
		Iterator<String> itr = keys.iterator();
		
		while(itr.hasNext()) {
			String id = itr.next();
			Integer num = list.get(id);
			IdPriority priority = new IdPriority();
			priority.setId(id);
			priority.setPriority(num);
			pList.getPriorities().add(priority);
		}
		
		OverrideMapType overrideMap = new OverrideMapType();
		overrideMap.setPrioritiesList(pList);

		cloudMgr.overrideHostPriorities(overrideMap);
	}
	
	/**
	 * Get a Host data from a XML string containing VmHostModel and a resource ID
	 * @param xml
	 * @return
	 */
	private Host getHostFromXML(String xml) 
	{	
		Host ret = new Host();
		VmHostModel model = VmHostModel.fromXML(xml);
		ret.setHostModel(model);
		//extract resource ID
		int pi = xml.indexOf(RESOURCE_ID_ELEMENT);
		if (pi > -1)
		{
			pi += RESOURCE_ID_ELEMENT.length();
			int pj = xml.indexOf("<", pi+1);
			ret.setResourceID(xml.substring(pi, pj));
		}
		//extract resource Name
		pi = xml.indexOf(RESOURCE_NAME_ELEMENT);
		if (pi > -1)
		{
			pi += RESOURCE_NAME_ELEMENT.length();
			int pj = xml.indexOf("<", pi+1);
			ret.setResourceName(xml.substring(pi, pj));
		}
		//extract resource Domain ID
		pi = xml.indexOf(DOMAIN_ID_ELEMENT);
		if (pi > -1)
		{
			pi += DOMAIN_ID_ELEMENT.length();
			int pj = xml.indexOf("<", pi+1);
			ret.setDomainID(xml.substring(pi, pj));
		}
		return ret;
	}
	
}
