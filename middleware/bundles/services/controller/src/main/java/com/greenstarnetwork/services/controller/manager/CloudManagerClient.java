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

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.DescribeHosts;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ICloudManagerSOAPEndpoint;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.MigrateInstance;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.MigrateInstanceResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.OverrideHostPriorities;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.OverrideHostPriorities.Arg0;
import com.greenstarnetwork.services.controller.model.Host;

/**
 * Handling the connection with the Cloud Manager
 * @author knguyen
 *
 */
public class CloudManagerClient 
{
	//Resource ID tag involved in a XML string representing a resource data model
	public static String RESOURCE_ID_ELEMENT = "<resourceId>";
	//Resource Name tag involved in a XML string representing a resource data model
	public static String RESOURCE_NAME_ELEMENT = "<resourceName>";
	//Domain ID tag involved in a XML string representing a resource data model
	public static String DOMAIN_ID_ELEMENT = "<domainId>";

	private String domainId = null;				//the domain where the cloud manager is running
	private ICloudManagerSOAPEndpoint cloudMgr = null;
	
	public CloudManagerClient(String domain) {
		this.domainId = domain;
	}
	
	/**
	 * Create webservices handlers
	 */
	public void createCloudManagerService() 
	{
		QName serviceName = new QName("http://soapendpoint.cloudManager.services.greenstarnetwork.com/", "CloudManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.cloudManager.services.greenstarnetwork.com/", "CloudManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + domainId + ":8181/cxf/CloudManagerSOAPEndpoint"); 
	    cloudMgr = service.getPort(portName,  ICloudManagerSOAPEndpoint.class);
	    setHttpConduit(ClientProxy.getClient(cloudMgr));
	}
	
	private void setHttpConduit(Client client) {
		HTTPConduit http = (HTTPConduit) client.getConduit();
		  HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		  httpClientPolicy.setConnectionTimeout(36000000);
		  httpClientPolicy.setAllowChunking(false);
		  httpClientPolicy.setReceiveTimeout(32000000);

		  http.setClient(httpClientPolicy);
	}

	/**
	 * Get Host Resource Models of a given Cloud Manager
	 * @return
	 */
	public List<Host> getHostList() 
	{
		List<String> hostlist = cloudMgr.describeHosts(new DescribeHosts()).getReturn();
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
	public String migrateVM(String hostId, String vmName, String destinationHostId) 
	{
		MigrateInstance inst = new MigrateInstance();
		inst.setArg0(hostId);
		inst.setArg1(vmName);
		inst.setArg2(destinationHostId);
		
		MigrateInstanceResponse response = cloudMgr.migrateInstance(inst);
		System.err.println("  --> CloudManager: " + response.getReturn());
		return response.getReturn();
	}

	/**
	 * Update host priority list
	 * @param list
	 */
	public void updateHostPriorityTable(Map<String, Integer> list) {
		OverrideHostPriorities arg = new OverrideHostPriorities();
		OverrideHostPriorities.Arg0 aa = new Arg0();
		Iterator<Entry<String, Integer>> it = list.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> e = it.next();
			OverrideHostPriorities.Arg0 .Entry ews = new OverrideHostPriorities.Arg0 .Entry();
			ews.setKey(e.getKey());
			ews.setValue(e.getValue());
			aa.getEntry().add(ews);
		}
		
		arg.setArg0(aa);
		this.cloudMgr.overrideHostPriorities(arg);
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
