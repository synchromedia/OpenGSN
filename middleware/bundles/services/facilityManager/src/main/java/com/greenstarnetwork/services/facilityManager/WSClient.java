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
package com.greenstarnetwork.services.facilityManager;


import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;


import com.iaasframework.core.resources.manager.soapendpoint.IResourceManagerSOAPEndpoint;
import com.iaasframework.capabilities.model.soapendpoint.IModelCapabilitySOAPEndpoint;
import com.iaasframework.capabilities.actionset.soapendpoint.IActionSetCapabilitySOAPEndpoint;
import com.greenstarnetwork.capabilities.archivecapability.soapendpoint.IArchiveCapabilitySOAPEndpoint;


/**
 * The WSClient class inits The Webservice clients to interact with the containers
 * @author knguyen
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class WSClient {
	
	public WSClient() {
	}
	
	/**
	 * Initialize soap endpoint client accessing a Resource Manager
	 * @param ip	The ip of the Resource Manager container
	 * @return	The soap endpoint client
	 */
	public static IResourceManagerSOAPEndpoint createManagerService(String server) {
		QName serviceName = new QName("http://soapendpoint.manager.resource.core.iaasframework.com/", "ResourceManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.manager.resource.core.iaasframework.com/", "ResourceManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ResourceManagerSOAPEndpoint"); 
	    return service.getPort(portName,  IResourceManagerSOAPEndpoint.class);
	}
	
	/**
	 * Initialize soap endpoint client accessing a Model Capability
	 * @param ip	The ip of the Model Capability container
	 * @return	The soap endpoint client
	 */
	public static IModelCapabilitySOAPEndpoint createModelService(String server) {
		QName serviceName = new QName("http://soapendpoint.model.capabilities.iaasframework.com/", "ModelCapabilitySOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.model.capabilities.iaasframework.com/", "ModelCapabilitySOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ModelCapabilitySOAPEndpoint"); 
	    return service.getPort(portName,  IModelCapabilitySOAPEndpoint.class);
	}

	/**
	 * Initialize soap endpoint client accessing an ActionSet Capability
	 * @param ip	The ip of the ActionSet Capability container
	 * @return	The soap endpoint client
	 */
	public static IActionSetCapabilitySOAPEndpoint createActionSetService(String server) {
		QName serviceName = new QName("http://soapendpoint.actionset.capabilities.iaasframework.com/", "ActionSetCapabilitySOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.actionset.capabilities.iaasframework.com/", "ActionSetCapabilitySOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ActionSetCapabilitySOAPEndpoint"); 
	    return service.getPort(portName,  IActionSetCapabilitySOAPEndpoint.class);
	}
	
	public static IArchiveCapabilitySOAPEndpoint createArchiveCapabilitySOAPEndpoint(String server) {
		QName serviceName = new QName("http://soapendpoint.archiveCapability.capabilities.greenstarnetwork.com/", "ArchiveCapabilitySOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.actionset.capabilities.iaasframework.com/", "ArchiveCapabilitySOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ArchiveCapabilitySOAPEndpoint"); 
	    return service.getPort(portName,  IArchiveCapabilitySOAPEndpoint.class);
	}
}
