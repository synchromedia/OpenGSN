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

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlTransient;

import com.iaasframework.capabilities.actionset.soapendpoint.ActionException_Exception;
import com.iaasframework.capabilities.actionset.soapendpoint.IActionSetCapabilitySOAPEndpoint;
import com.iaasframework.capabilities.model.soapendpoint.ModelException_Exception;
import com.iaasframework.capabilities.model.soapendpoint.IModelCapabilitySOAPEndpoint;
import com.iaasframework.core.resources.manager.soapendpoint.IResourceManagerSOAPEndpoint;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceException_Exception;

/**
 * Create and manage connection to a given resource manager which manages a domain.
 * All facility resources in that domain will be stored in a repository.
 * The resource manager also executes action on facility resources.
 * @author knguyen
 *
 */
public class ResourceManager {
	
	/**
	 * ID (IP address) of the Resource Manager container
	 */
	private String RM_ID = null;
	
	/**
	 * Soap client to the Resource Manager
	 */
	private IResourceManagerSOAPEndpoint ResourceManagerClient = null;
	
	/**
	 * Soap client to the Model Capability
	 */
	private IModelCapabilitySOAPEndpoint ModelCapabilityClient = null;
	
	/**
	 * Soap client to the ActionSetCapability
	 */
	private IActionSetCapabilitySOAPEndpoint ActionSetCapabilityClient = null;
	
	/**
	 * List of Facililty Resources managed by the Resource Manager
	 */
	private List<ResourceData> repository = null;
	
	public ResourceManager(){
	}

	public void setId(String ip) {
		this.RM_ID = ip;
	}

	public String getId() {
		return RM_ID;
	}

	@XmlTransient
	public void setRepository(List<ResourceData> resources) {
		this.repository = resources;
	}

	public List<ResourceData> getRepository() {
		return repository;
	}

	@XmlTransient
	public void setResourceManagerClient(IResourceManagerSOAPEndpoint resourceManagerClient) {
		ResourceManagerClient = resourceManagerClient;
	}

	public IResourceManagerSOAPEndpoint getResourceManagerClient() {
		return ResourceManagerClient;
	}

	@XmlTransient
	public void setModelCapabilityClient(IModelCapabilitySOAPEndpoint modelCapabilityClient) {
		ModelCapabilityClient = modelCapabilityClient;
	}

	public IModelCapabilitySOAPEndpoint getModelCapabilityClient() {
		return ModelCapabilityClient;
	}

	@XmlTransient
	public void setActionSetCapabilityClient(IActionSetCapabilitySOAPEndpoint actionSetCapabilityClient) {
		ActionSetCapabilityClient = actionSetCapabilityClient;
	}

	public IActionSetCapabilitySOAPEndpoint getActionSetCapabilityClient() {
		return ActionSetCapabilityClient;
	}
	
	/**
	 * Retrieve a resource from a given id
	 * @param id
	 * @return
	 */
	public ResourceData getFacility(String id) {
		if (this.repository == null)
			return null;
		
		Iterator<ResourceData> it = this.repository.iterator();
		while (it.hasNext()) {
			ResourceData r = it.next();
			if (r.getId().compareTo(id) == 0)
				return r;
		}
		return null;
	}
	
	/**
	 * Add a facility to repository
	 * @param resource
	 * @throws FacilityManagerException
	 */
//	public void addFacility(ResourceData resource) throws FacilityManagerException{
//		if (this.repository == null)
//			this.repository = new ArrayList<ResourceData>();
//		if (resource.getType().compareTo(FacilityManager.FACILITY_TYPE) != 0)
//			throw new FacilityManagerException("FacilityManager accepts FacilityResource only!");
//		this.repository.add(resource);
//	}
	
	/**
	 * Get data model for a given resource id (in XML format)
	 * @param id
	 * @return
	 * @throws ModelException_Exception
	 */
	public String getModel(String id) throws ModelException_Exception {
		return this.ModelCapabilityClient.getResourceModel(id);
	}

	/**
	 * Return description of a given resource 
	 * @param type
	 * @param id
	 * @return
	 * @throws ResourceException_Exception
	 */
	public ResourceData getResource(String type, String id) throws ResourceException_Exception {
		return this.ResourceManagerClient.getResource(type, id);
	}

	/**
	 * Return description of a given resource 
	 * @param type
	 * @param id
	 * @return
	 * @throws ResourceException_Exception
	 */
	public ResourceData getResource(String id) throws ResourceException_Exception 
	{
		List<ResourceData> lr = this.ResourceManagerClient.listResources();
		if (lr != null)
		{
			Iterator<ResourceData> it = lr.iterator();
			while (it.hasNext())
			{
				ResourceData r = it.next();
				if (r.getId().compareTo(id) == 0)
					return r;
			}
		}
		return null;
	}

	/**
	 * Execute an action on a given resource
	 * @param id
	 * @param action
	 * @param param
	 * @return
	 * @throws ActionException
	 */
	public String executeAction(String id, String action, String param) throws ActionException_Exception {
		return this.ActionSetCapabilityClient.executeActionWS(id, action, param);
	}
	
	/**
	 * Create object from xml string
	 * @param xml
	 * @return
	 */
	public Object fromXml(String xml) {	

		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(com.greenstarnetwork.models.facilityModel.FacilityModel.class);
			Object obj = context
			.createUnmarshaller().unmarshal(in);
			return obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}
}
