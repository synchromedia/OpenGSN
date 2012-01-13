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
package com.greenstarnetwork.resources.facilityResource.core;

import java.util.Map;

import org.osgi.framework.ServiceReference;

import com.iaasframework.capabilities.actionset.Activator;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceManager;
import com.iaasframework.resources.core.IResourceRepository;
import com.iaasframework.resources.core.ResourceManager;


/**
 * 
 */
public class ResourceManagerService {

	private final boolean DEBUG=false;
	private ResourceManager resourceManager;

	public ResourceManagerService() {

	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void checkExist(String resourceID) throws Exception {
		Boolean exist = false;
		if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" checking of "+resourceID+" started");}
		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){
			if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: "+str);}
			IResourceRepository resourceRepository = map.get(str);
			for (IResource resource:resourceRepository.listResources()){
				if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: ::: "+resource.getResourceIdentifier().getId());}
				if (resource.getResourceIdentifier().getId().equals(resourceID)){
					exist=true;
				}
			}
		}
		if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" checking of "+resourceID+" finished");}
		if (!exist){
			throw new Exception("the resource("+resourceID+") does not exist");
		}

	}

	/**
	 * @throws Exception 
	 * 
	 */
	public String getFirstResourceIDByType(String resourceType) throws Exception {
		if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" getting first of "+resourceType+" started");}
		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){
			if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: "+str);}
			IResourceRepository resourceRepository = map.get(str);
			if (str.equals(resourceType)){
				for (IResource resource:resourceRepository.listResources()){
					if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: ::: "+resource.getResourceIdentifier().getId());}
					if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" getting first of "+resourceType+" finished");}
					return resource.getResourceIdentifier().getId();
				}
			}
		}

		throw new Exception("any of resourceType ("+resourceType+") does not exist");
	}



	public void init() throws Exception {
		ServiceReference helloServiceRef = Activator.getContext().getServiceReference(IResourceManager.class.getName());
		resourceManager = (ResourceManager)Activator.getContext().getService(helloServiceRef);

		if (DEBUG){Logger.getLogger().log(helloServiceRef.toString());}

		//resourceManager = (ResourceManager)RegistryUtil.getServiceFromRegistry(Activator.getContext(), IResourceManager.class.getName());
		if (Activator.getContext()==null){
			throw new Exception("Activator.getContext() is null");
		}
		if (resourceManager==null){
			throw new Exception("resourceManager is null");
		}
		if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" init finished");}
	}

	public String getResourceIDbyAliase(String aliase) throws Exception {
		if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" getting resourceID for aliase:"+aliase);}
		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){
			if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: "+str);}
			IResourceRepository resourceRepository = map.get(str);
			for (IResource resource:resourceRepository.listResources()){
				if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: ::: "+resource.getResourceDescriptor().getInformation().getName());}
				if (resource.getResourceDescriptor().getInformation().getName().trim().equalsIgnoreCase(aliase.trim())){
					if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" getting resourceID of "+aliase+" finished");}
					return resource.getResourceIdentifier().getId();
				}
			}
		}

		return null;

	}

	public String getResourceAliaseByID(String id) throws Exception {
		if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" getting resource aliase for id:"+id);}
		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){
			if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: "+str);}
			IResourceRepository resourceRepository = map.get(str);
			for (IResource resource:resourceRepository.listResources()){
				if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" ::: ::: "+resource.getResourceDescriptor().getInformation().getName());}
				if (resource.getResourceIdentifier().getId().trim().equalsIgnoreCase(id.trim())){
					if (DEBUG){Logger.getLogger().log(this.getClass().getName()+" getting aliase of "+id+" finished");}
					return resource.getResourceDescriptor().getInformation().getName();
				}
			}
		}

		return null;

	}





}
