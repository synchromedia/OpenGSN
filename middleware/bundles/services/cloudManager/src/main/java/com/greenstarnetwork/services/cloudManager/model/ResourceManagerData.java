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
package com.greenstarnetwork.services.cloudManager.model;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.iaasframework.capabilities.actionset.soapendpoint.IActionSetCapabilitySOAPEndpoint;
import com.iaasframework.capabilities.model.soapendpoint.IModelCapabilitySOAPEndpoint;
import com.iaasframework.core.resources.manager.soapendpoint.IResourceManagerSOAPEndpoint;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class ResourceManagerData {
	
	/**
	 * id of the Resource Manager container (Actually, this attribute contains the Resource Manager IP)
	 */
	private String id;
	
	/**
	 * Soap client to the Resource Manager
	 */
	private IResourceManagerSOAPEndpoint ResourceManagerClient;
	
	/**
	 * Soap client to the Model Capability
	 */
	private IModelCapabilitySOAPEndpoint ModelCapabilityClient;
	
	/**
	 * Soap client to the ActionSetCapability
	 */
	private IActionSetCapabilitySOAPEndpoint ActionSetCapabilityClient;
	
	/**
	 * List of Resources Data managed by the Resource Manager and their associated ip
	 */
	private List<ResourceData> resources;
	
	public ResourceManagerData(){
	}

	@XmlTransient
	public void setResources(List<ResourceData> resources) {
		this.resources = resources;
	}

	public List<ResourceData> getResources() {
		return resources;
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

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
