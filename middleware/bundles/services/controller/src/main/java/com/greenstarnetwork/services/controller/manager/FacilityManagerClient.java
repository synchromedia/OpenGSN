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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.core.Logger;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.IFacilityManagerSOAPEndpoint;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.FacilityManagerException_Exception;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ListAllFacilities;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.RegisterController;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.RegisterControllerResponse;
/**
 * Handling a list of Facility Managers
 * @author knguyen
 *
 */
public class FacilityManagerClient {

	private Logger log = Logger.getLogger();				//controller state logger
	
	private String domainId = null;							//the domain where facility manager is running
	private IFacilityManagerSOAPEndpoint facilityMgr = null;
	
	FacilityManagerJMSQueue queue = null;
	
	public FacilityManagerClient(String domain) {
		this.domainId = domain;
	}
	
	public void createFacilityManagerService() {
		QName serviceName = new QName("http://soapendpoint.facilityManager.services.greenstarnetwork.com/", "FacilityManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.facilityManager.services.greenstarnetwork.com/", "FacilityManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + domainId + ":8181/cxf/FacilityManagerSOAPEndpoint"); 
	    facilityMgr = service.getPort(portName,  IFacilityManagerSOAPEndpoint.class);
	}

	public boolean createJMSQueue(IController controller) {
		try {
			queue = new FacilityManagerJMSQueue();
			queue.setParent(controller);
			queue.initializeJMS();
		} catch (javax.jms.JMSException e) {
			log.debug(e.toString());
			e.printStackTrace();
			return false;
		}
		RegisterController func = new RegisterController();
		func.setArg0("vm://localhost");
		if (facilityMgr != null) {
			RegisterControllerResponse res = facilityMgr.registerController(func);
			return res.isReturn();
		}
		return false;
	}
	
	/**
	 * Get Facility Models of a given Facility Manager
	 * @return
	 */
	public List<FacilityModel> getFacilityList() 
	{
		List<FacilityModel> ret = null;
		try {
			List<String> xmlModels = facilityMgr.listAllFacilities(new ListAllFacilities()).getReturn();
			if (xmlModels != null) {
				ret = new ArrayList<FacilityModel>();
				Iterator<String> it = xmlModels.iterator();
				while (it.hasNext()) {
					FacilityModel m = FacilityModel.fromXML((String)it.next());
					ret.add(m);
				}
			}
		} catch (FacilityManagerException_Exception e) {
			log.debug(e.toString());
		}
		return ret;
	}
}
