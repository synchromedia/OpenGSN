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

import java.util.List;

import com.greenstarnetwork.models.facilityModel.OperationalSpecs;
import com.greenstarnetwork.services.facilityManager.model.ResourceManagersList;

/**
 * FacilityManager function definitions
 * 
 * @author knguyen
 *
 */
public interface IFacilityManager {

	/**
	 * List of all facilities in all IAAS containers
	 * @return List of facilities
	 */
	public List<String> listAllFacilities()  throws FacilityManagerException;
	
	/**
	 * Return all PDUs
	 * @return
	 */
	public List<String> listAllPDUs()  throws FacilityManagerException;

	/**
	 * Return all Power Sources
	 * @return
	 */
	public List<String> listAllPowerSources() throws FacilityManagerException;

	/**
	 * Return all Climate resources
	 * @return
	 */
	public List<String> listAllClimates() throws FacilityManagerException;
	

	/**
	 * Retrieve the model of a resource (in XML format)
	 * @param type
	 * @param id
	 * @return
	 * @throws FacilityManagerException
	 */
	public String getResourceModel(String type, String domainId, String id);

	/**
	 * Retrieve the model of a resource (in XML format)
	 * @param type
	 * @param id
	 * @return model of the first resource that meets type and id, regardless its domain
	 * @throws FacilityManagerException
	 */
	public String getResourceModel(String type, String id);
	
	/**
	 * Execute an action on a facility
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	public String executeAction(String resourceId, String actionName, String parameters) throws FacilityManagerException;

	/**
	 * Refresh a facility resource
	 * @param resourceId
	 * @return
	 * @throws ActionException_Exception
	 */
	public String refreshResource(String resourceId)  throws FacilityManagerException;

	/**
	 * Turn off a facility resource
	 * @param resourceId
	 * @return
	 * @throws ActionException_Exception
	 */
	public String turnOffResource(String resourceId)  throws FacilityManagerException;

	/**
	 * Turn on a facility resource
	 * @param resourceId
	 * @return
	 * @throws ActionException_Exception
	 */
	public String turnOnResource(String resourceId)  throws FacilityManagerException;
	
	/**
	 * Calculate operating hour of a facility resource
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	public String calculateOpHour(String resourceId)  throws FacilityManagerException;
	
	/**
	 * Set a operational threshold by which the Facility Manager has to send a warning message
	 * @param threshold
	 */
	public void setThreshold(OperationalSpecs threshold);
	
	/**
	 * Register a Controller with this Manager
	 * @param address
	 * @return
	 */
	public boolean registerController(String address);
	
	/**
	 * Fill in resource data
	 */
	public void initResourceManagers();

	/**
	 * Initialize facility manager
	 */
	public void initFacilityManager();
	
	/**
	 * Add a domain in the facility manager
	 * @param domainId ip/id of the domain
	 */
	public void addDomain(String domainId);
	
	/**
	 * Remove a domain from the facility manager
	 * @param domainId ip/id of the domain
	 */
	public void removeDomain(String domainId);
	
	/**
	 * Force the facility manager to load/reload domains that was updated by the domainManager
	 */
	public void initDomains();

	/**
	 * @return the managers
	 */
	public ResourceManagersList getManagers();
	
	/**
	 * Return the archive in a period of time
	 * @param startDate
	 * @param endDate
	 * @param resourceID
	 * @return
	 */
	public String getArchiveDataByRangDate(String startDate, String endDate, String resourceID);
}
