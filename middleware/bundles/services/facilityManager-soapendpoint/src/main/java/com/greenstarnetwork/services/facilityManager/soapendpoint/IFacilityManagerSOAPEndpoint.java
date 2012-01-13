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

/**
 * 
 * @author knguyen
 *
 */
@WebService
public interface IFacilityManagerSOAPEndpoint {
	/**
	 * List of all facilities in all IAAS containers
	 * @return List of facilities
	 */
	@WebMethod
	public List<String> listAllFacilities()  throws FacilityManagerException;
	
	/**
	 * Return all PDUs
	 * @return
	 */
	@WebMethod
	public List<String> listAllPDUs()  throws FacilityManagerException;

	/**
	 * Return all Power Sources
	 * @return
	 */
	@WebMethod
	public List<String> listAllPowerSources()  throws FacilityManagerException;
	
	/**
	 * Retrieve the model of a resource (in XML format)
	 * @param type
	 * @param id
	 * @return
	 * @throws FacilityManagerException
	 */
	@WebMethod
	public String getResourceModel(String type, String domainId, String id) throws FacilityManagerException;
	
	/**
	 * Execute an action on a facility
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	@WebMethod
	public String executeAction(String resourceId, String actionName, String parameters) throws FacilityManagerException;

	/**
	 * Refresh a facility resource
	 * @param resourceId
	 * @return
	 * @throws ActionException_Exception
	 */
	@WebMethod
	public String refreshResource(String resourceId)  throws FacilityManagerException;

	/**
	 * Turn off a facility resource
	 * @param resourceId
	 * @return
	 * @throws ActionException_Exception
	 */
	@WebMethod
	public String turnOffResource(String resourceId)  throws FacilityManagerException;

	/**
	 * Turn on a facility resource
	 * @param resourceId
	 * @return
	 * @throws ActionException_Exception
	 */
	@WebMethod
	public String turnOnResource(String resourceId)  throws FacilityManagerException;

	/**
	 * Calculate the remaining operating hour of a data center
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	@WebMethod
	public String calculateOpHour(String resourceId)  throws FacilityManagerException;
	
	/**
	 * Register a Controller with this Manager
	 * @param address
	 * @return
	 */
	@WebMethod
	public boolean registerController(String address);

	/**
	 * Return the archive in a period of time
	 * @param startDate
	 * @param endDate
	 * @param resourceID
	 * @return
	 */
	@WebMethod
	public String getArchiveDataByRangDate(String startDate, String endDate, String resourceID);
}
