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
package com.greenstarnetwork.services.facilityManager.model;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.services.facilityManager.WSClient;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.model.ModelException;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionException_Exception;
import com.iaasframework.capabilities.model.soapendpoint.ModelException_Exception;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceException_Exception;
import com.greenstarnetwork.capabilities.archivecapability.soapendpoint.IArchiveCapabilitySOAPEndpoint;
import com.greenstarnetwork.capabilities.archivecapability.soapendpoint.GetArchiveDataByRangDate;
/**
 * Managing a list of resource managers
 * @author knguyen
 *
 */
@XmlRootElement(name="domainList")
public class ResourceManagersList {
	
	/**
	 * List of Resources Managers in the cloud with their data
	 */
	private List<ResourceManager> resourceManagers;
	
	public ResourceManagersList(){
		
	}

	public void setResourceManagers(List<ResourceManager> resourceManagers) {
		this.resourceManagers = resourceManagers;
	}

	@XmlElement(name="domain")
	public List<ResourceManager> getResourceManagers() {
		return resourceManagers;
	}

	/**
	 * Retrieve a resource from a given id
	 * @param id
	 * @return
	 */
	public ResourceData getFacilityResource(String id) 
	{
		if (this.resourceManagers == null)
			return null;
		Iterator<ResourceManager> it = this.resourceManagers.iterator();
		while (it.hasNext()) {
			ResourceManager rm = it.next();
			ResourceData r = rm.getFacility(id);
			if (r != null)
				return r;
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @param action
	 * @param param
	 * @return
	 * @throws ActionException
	 */
	public String executeAction(String id, String action, String param) throws ActionException_Exception 
	{
		Iterator<ResourceManager> it = this.resourceManagers.iterator();
		while (it.hasNext()) 
		{
			ResourceManager rm = it.next();
			try {
				ResourceData r = rm.getResource(id);
				if (r != null)
				{
					return rm.executeAction(id, action, param);
				}
			}catch (ResourceException_Exception e) {
			}
		}
		System.err.println("Facility ID: " + id + " not found!.");
		return null;
	}

	/**
	 * Get Model of a resource managed by FacilityManager.
	 * Firstly, seek for the resource manager which contains the resource,
	 * Then contact the resource manager in order to get the model
	 * @param id
	 * @return
	 * @throws ModelException
	 */
	public String getModel(String domainId, String id) {
		Iterator<ResourceManager> it = this.resourceManagers.iterator();
		while (it.hasNext()) 
		{
			ResourceManager rm = it.next();
			try {
				if (rm.getId().compareTo(domainId) == 0)
				{
					String m = rm.getModel(id);
					if (m != null)
						return m;
				}
			}catch (ModelException_Exception e) {
			}
		}
		return null;
	}

	
	/**
	 * Return description of a given resource 
	 * Firstly, seek for the resource manager which contains the resource,
	 * Then contact the resource manager in order to get the description
	 * @param type
	 * @param id
	 * @return
	 * @throws ModelException_Exception
	 */
	public ResourceData getResource(String type, String id) {
		Iterator<ResourceManager> it = this.resourceManagers.iterator();
		while (it.hasNext()) 
		{
			ResourceManager rm = it.next();
			try {
				ResourceData r = rm.getResource(type, id);
				if (r != null)
					return r;
			}catch (ResourceException_Exception e) {
			}
		}
		return null;
	}

	/**
	 * Return description of a resource given its name 
	 * Firstly, seek for the resource manager which contains the resource,
	 * Then contact the resource manager in order to get the description
	 * @param type
	 * @param id
	 * @return
	 * @throws ModelException_Exception
	 */
	public ResourceData getResourceByName(String name) {
		Iterator<ResourceManager> it = this.resourceManagers.iterator();
		while (it.hasNext()) 
		{
			ResourceManager rm = it.next();
				List<ResourceData> lr = rm.getResourceManagerClient().listResources();
				if (lr != null)
				{
					Iterator<ResourceData> ir = lr.iterator();
					while (ir.hasNext())
					{
						ResourceData r = ir.next();
						if (r.getName().compareTo(name) == 0)
							return r;
					}
				}
		}
		return null;
	}
	
	/**
	 * Return the archive in a period of time
	 * @param startDate
	 * @param endDate
	 * @param resourceID
	 * @return
	 */
	public String getArchiveDataByRangDate(String startDate, String endDate,
			String resourceID) {
		Iterator<ResourceManager> it = this.resourceManagers.iterator();
		while (it.hasNext()) 
		{
			ResourceManager rm = it.next();
			try {
				ResourceData r = rm.getResource(resourceID);
				if (r != null)
				{
//					System.err.println("Found resource: " + r.getName());
					IArchiveCapabilitySOAPEndpoint archive = WSClient.createArchiveCapabilitySOAPEndpoint(rm.getId());
					GetArchiveDataByRangDate arg = new GetArchiveDataByRangDate();
					arg.setArg0(startDate);
					arg.setArg1(endDate);
					arg.setArg2(r.getName());
					return archive.getArchiveDataByRangDate(arg).getReturn();
				}
			}catch (ResourceException_Exception e) {
			}
		}		
		return null;
	}
}
