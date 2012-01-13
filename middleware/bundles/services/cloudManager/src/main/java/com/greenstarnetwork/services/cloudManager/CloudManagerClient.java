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
package com.greenstarnetwork.services.cloudManager;


import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.greenstarnetwork.services.cloudmanager.soapendpoint.ICloudManagerSOAPEndpoint;

/**
 * CloudManager webservice client initializes webservice clients to interact with other cloud managers
 * @author knguyen
 *
 */
public class CloudManagerClient {
	
	public CloudManagerClient() {
	}

	public static ICloudManagerSOAPEndpoint createCloudManagerService(String server) {
		QName serviceName = new QName("http://soapendpoint.cloudManager.services.greenstarnetwork.com/", "CloudManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.cloudManager.services.greenstarnetwork.com/", "CloudManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/CloudManagerSOAPEndpoint"); 
	    ICloudManagerSOAPEndpoint ret = service.getPort(portName,  ICloudManagerSOAPEndpoint.class);
	    
	    setHttpConduit(ClientProxy.getClient(ret));
	    return ret;
	}
	
	private static void setHttpConduit(Client client) {
		HTTPConduit http = (HTTPConduit) client.getConduit();
		  HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		  httpClientPolicy.setConnectionTimeout(360000);
		  httpClientPolicy.setAllowChunking(false);
		  httpClientPolicy.setReceiveTimeout(320000);

		  http.setClient(httpClientPolicy);
	}
}
