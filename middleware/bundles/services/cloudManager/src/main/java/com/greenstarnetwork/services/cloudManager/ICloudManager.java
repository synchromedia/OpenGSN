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
package com.greenstarnetwork.services.cloudManager;

import java.util.List;
import java.util.Map;

import com.greenstarnetwork.services.cloudManager.model.Host;
import com.greenstarnetwork.services.cloudManager.model.ResourceManagersList;

public interface ICloudManager {
	
	/**
	 * Retrieve Hosts models with their VMs
	 * @return List of Hosts Models
	 */
	public List<String> describeHosts();
	
	/**
	 * Retrieve the model of a Host
	 * @param hostId	id of the host (== id of the resource)
	 * @return	Host Model
	 */
	public String describeHost( String hostId );
	
	/**
	 * Create a virtual machine in a specified host
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	public String createInstanceInHost(String hostId, String instanceName, String memory, String cpu, String template);
	
	/**
	 * Start a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String startInstance(String hostId, String instanceName);
	
	/**
	 * Stop a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String stopInstance(String hostId, String instanceName);
	
	/**
	 * Reboot a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String rebootInstance(String hostId, String instanceName);
	
	/**
	 * Shutdown a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String shutdownInstance(String hostId, String instanceName);
	
	/**
	 * migrate a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @param destinationHostId id of the destination host (id of the IAAS destination resource)
	 * @return Success or error message
	 */
	public String migrateInstance(String hostId, String instanceName, String destinationHostId);
	
	/**
	 * Destroy a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String destroyInstance(String hostId, String instanceName);
	
	/**
	 * TODO EXPORT
	 * Export a virtual machine to a remote cloud
	 * @param instanceName Virtual Machine name
	 * @param targetCloudIP address of the remote cloud which will receives the VM 
	 * @return Success or error message
	 */
	public String exportVM(String instanceName,  String targetCloudIP);

	/**
	 * TODO EXPORT
	 * Receive a virtual machine from a remote cloud
	 * @param remotehostIP IP of the remote host where VM image needs to be downloaded
	 * @param remoteDir directory on remote host where VM image needs to be downloaded
	 * @param instanceName Virtual Machine name
	 * @param cpu required number of CPU 
	 * @param memory required capacity of memory 
	 * @param template template used to create the VM 
	 * @return Success or error message
	 */
	public String importVM(String remotehostIP, String remoteDir, String username, String instanceName, 
			String cpu, String memory, String template);
	
	/**
	 * Set IP address to a virtual machine
	 * @param hostId
	 * @param instanceName
	 * @return
	 */
	public String setInstanceIPAddress(String hostId, String instanceName);
	
	/**
	 * 
	 * @return	List of all IAAS Resources Managers
	 */
	public ResourceManagersList getManagers();
	
	/**
	 * Cloud Manager initialization
	 */
	public void initCloudManager();
	
	/**
	 * Add a domain in the cloud manager
	 * @param domainId ip/id of the domain
	 */
	public void addDomain(String domainId);
	
	/**
	 * Remove a domain from the cloud manager
	 * @param domainId ip/id of the domain
	 */
	public void removeDomain(String domainId);
	
	/**
	 * Force the cloud manager to load/reload domains that was updated by the domainManager
	 */
	public void initDomains();
	
	/**
	 * Generic query which retrieve hosts that match the function parameters. 
	 * A parameter equal to -1 will be discarded from the research
	 * 
 	 * @param domainId	domain ID (Which is the ip of the IAAS container)
	 * @param ip	Host ip
	 * @param location	Host location
	 * @param freeMemory Host freeMemory bigger than the freeMemory parameter (unit: Ko)
	 * @param availableCPU	Host available CPUs
	 * @param 	vmUUID	Enable to retrieve the Host which contains the Virtual Machine with the UUID parameter
	 * @return
	 */
	public List<String> hostQuery(String domainId, String ip, String location, String freeMemory, String availableCPU, String vmUUID);
	
	/**
	 * Override Host priorities in the cloud manager host model
	 * @param priorityTable Map with host priorities (String = host resource ID, Integer = host priority)
	 */
	public void overrideHostPriorities(Map<String,Integer> priorityTable);
	
	/**
	 * Create a virtual machine by letting the cloud manager finding the most appropriate host
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	public String createInstance(String instanceName, String memory, String cpu, String template);
	
	
	/**
	 * Execute an action on a IAAS resource
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	public String executeAction(String resourceId, String actionName, String parameters);
	
	/**
	 * Managing received messages from activemq
	 * @param message	Received message from activemq
	 */
	public void onMessage(String message);

	/**
	 * TODO EXPORT
	 * Set the proxy host that receives VMs from external clouds
	 * @param cloudProxy
	 */
	public void setProxyHost(String id, String ip, String storagePath, String username, String password);

	/**
	 * TODO EXPORT
	 * Return IP address of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyIP();

	/**
	 * TODO EXPORT
	 * Return username of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyUser();
	
	/**
	 * TODO EXPORT
	 * Return password of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyPassword();

	/**
	 * TODO EXPORT
	 * Return storage path of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyStoragePath();
}
