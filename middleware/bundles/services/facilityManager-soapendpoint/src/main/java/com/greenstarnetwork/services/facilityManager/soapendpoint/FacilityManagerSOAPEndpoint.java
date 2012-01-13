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
package com.greenstarnetwork.services.facilityManager.soapendpoint;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.greenstarnetwork.services.facilityManager.FacilityManagerException;
import com.greenstarnetwork.services.facilityManager.IFacilityManager;

/**
 * 
 * @author knguyen
 *
 */
@WebService
public class FacilityManagerSOAPEndpoint implements IFacilityManagerSOAPEndpoint{

	private IFacilityManager facilityManager = null;
    
	public FacilityManagerSOAPEndpoint(IFacilityManager facilityManager) {
		this.facilityManager = facilityManager;
	}

	public FacilityManagerSOAPEndpoint(){
	}
	
	/**
	 * List of all facilities in all IAAS containers
	 * @return List of facilities
	 */
	@WebMethod
	public List<String> listAllFacilities() throws FacilityManagerException {
		return facilityManager.listAllFacilities();
	}
	
	/**
	 * Retrieve the model of a resource (in XML format)
	 * @param type
	 * @param id
	 * @return
	 * @throws FacilityManagerException
	 */
	@WebMethod
	public String getResourceModel(String type, String domainId, String id) throws FacilityManagerException {
		return facilityManager.getResourceModel(type, domainId, id);
	}
	
	/**
	 * Return all PDUs
	 * @return
	 */
	@WebMethod
	public List<String> listAllPDUs() throws FacilityManagerException {
		return facilityManager.listAllPDUs();
	}
	
	/**
	 * Return all Power Sources
	 * @return
	 */
	@WebMethod
	public List<String> listAllPowerSources()  throws FacilityManagerException {
		return facilityManager.listAllPowerSources();
	}

	/**
	 * Execute an action on a facility
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	@WebMethod
	public String executeAction(String resourceId, String actionName, String parameters) 
			throws FacilityManagerException {
		return facilityManager.executeAction(resourceId, actionName, parameters);
	}
	
	/**
	 * Refresh a facility resource
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	@WebMethod
	public String refreshResource(String resourceId)  throws FacilityManagerException {
		return facilityManager.refreshResource(resourceId);
	}

	/**
	 * Turn off a facility resource
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	@WebMethod
	public String turnOffResource(String resourceId)  throws FacilityManagerException {
		return facilityManager.turnOffResource(resourceId);
	}

	/**
	 * Turn on a facility resource
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	@WebMethod
	public String turnOnResource(String resourceId)  throws FacilityManagerException {
		return facilityManager.turnOnResource(resourceId);
	}

	@Override
	public String calculateOpHour(String resourceId) throws FacilityManagerException {
		return facilityManager.calculateOpHour(resourceId);
	}

	/**
	 * Register a Controller with this Manager
	 * @param address
	 * @return
	 */
	@WebMethod
	public boolean registerController(String address) {
		return facilityManager.registerController(address);
	}

	@WebMethod
	public String getArchiveDataByRangDate(String startDate, String endDate, String resourceID) {
		String ret = facilityManager.getArchiveDataByRangDate(startDate, endDate, resourceID);
		if (ret == null)
			return "No archive";
		return ret;
	}
}
