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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
@XmlRootElement(name="domainList")
public class ResourceManagersList {
	
	/**
	 * List of Resources Managers in the cloud with their data
	 */
	private List<ResourceManagerData> resourceManagers;
	
	public ResourceManagersList(){
		
	}

	public void setResourceManagers(List<ResourceManagerData> resourceMgers) {
		resourceManagers = resourceMgers;
	}

	@XmlElement(name="domain")
	public List<ResourceManagerData> getResourceManagers() {
		return resourceManagers;
	}
}
