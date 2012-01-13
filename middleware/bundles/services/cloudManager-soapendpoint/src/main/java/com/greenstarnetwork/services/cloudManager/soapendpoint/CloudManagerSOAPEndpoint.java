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
package com.greenstarnetwork.services.cloudManager.soapendpoint;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.greenstarnetwork.services.cloudManager.ICloudManager;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
@WebService
public class CloudManagerSOAPEndpoint implements ICloudManagerSOAPEndpoint{

	private ICloudManager cloudManager = null;
    
	public CloudManagerSOAPEndpoint(ICloudManager cloudManager) {
		this.cloudManager = cloudManager;
	}

	public CloudManagerSOAPEndpoint(){
	}
	
	/**
	 * Retrieve Hosts models with their VMs
	 * @return List of Hosts Models
	 */
	@WebMethod
	public List<String> describeHosts(){
		return cloudManager.describeHosts();
	}
	
	/**
	 * Retrieve the model of a Host
	 * @param hostId	id of the host (== id of the resource)
	 * @return	Host Model
	 */
	@WebMethod
	public String describeHost( String hostId ){
		return cloudManager.describeHost(hostId);
	}
	
	/**
	 * Create a virtual machine in a specified host
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	@WebMethod
	public String createInstanceInHost(String hostId, String instanceName, String memory, String cpu, String template){
		return cloudManager.createInstanceInHost(hostId, instanceName, memory, cpu, template);
	}
	
	/**
	 * Start a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	@WebMethod
	public String startInstance(String hostId, String instanceName){
		return cloudManager.startInstance(hostId, instanceName);
	}
	
	/**
	 * Stop a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	@WebMethod
	public String stopInstance(String hostId, String instanceName){
		return cloudManager.stopInstance(hostId, instanceName);
	}
	
	/**
	 * Reboot a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	@WebMethod
	public String rebootInstance(String hostId, String instanceName){
		return cloudManager.rebootInstance(hostId, instanceName);
	}
	
	/**
	 * Shutdown a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	@WebMethod
	public String shutdownInstance(String hostId, String instanceName){
		return cloudManager.shutdownInstance(hostId, instanceName);
	}
	
	/**
	 * migrate a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @param destinationHostId id of the destination host (id of the IAAS destination resource)
	 * @return Success or error message
	 */
	@WebMethod
	public String migrateInstance(String hostId, String instanceName, String destinationHostId){
		return cloudManager.migrateInstance(hostId, instanceName, destinationHostId);
	}
	
	/**
	 * Destroy a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	@WebMethod
	public String destroyInstance(String hostId, String instanceName){
		return cloudManager.destroyInstance(hostId, instanceName);
	}
	
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
	public List<String> hostQuery(String domainId, String ip, String location, String freeMemory, String availableCPU, String vmUUID){
		return cloudManager.hostQuery(domainId, ip, location, freeMemory, availableCPU, vmUUID);
	}
	
	/**
	 * Create a virtual machine by letting the cloud manager finding the most appropriate host
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	@WebMethod
	public String createInstance(String instanceName, String memory, String cpu, String template){
		return cloudManager.createInstance(instanceName, memory, cpu, template);
	}
	
	/**
	 * Override Host priorities in the cloud manager host model
	 * @param priorityTable Map with host priorities (String = host resource ID, Integer = host priority)
	 */
	@WebMethod
	public void overrideHostPriorities(HashMap<String,Integer> priorityTable) {
		cloudManager.overrideHostPriorities(priorityTable);
	}
	

	/**
	 * Execute an action on a IAAS resource
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	@WebMethod
	public String executeAction(String resourceId, String actionName, String parameters) {
		return cloudManager.executeAction(resourceId, actionName, parameters);
	}

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
			String cpu, String memory, String template)
	{
		return cloudManager.importVM(remotehostIP, remoteDir, username, instanceName, cpu, memory, template);
	}

	/**
	 * TODO EXPORT
	 * Export a virtual machine to a remote cloud
	 * @param instanceName Virtual Machine name
	 * @param targetCloud address of the remote cloud which will receives the VM 
	 * @return Success or error message
	 */
	@WebMethod
	public String exportVM(String instanceName, String targetCloud) {
		return cloudManager.exportVM(instanceName, targetCloud);
	}

	/**
	 * TODO EXPORT
	 * Return IP address of the proxy host that receives VMs from external clouds
	 */
	@WebMethod
	public String getCloudProxyIP() {
		return cloudManager.getCloudProxyIP();
	}

	/**
	 * TODO EXPORT
	 * Return username of the proxy host that receives VMs from external clouds
	 */
	@WebMethod
	public String getCloudProxyUser() {
		return cloudManager.getCloudProxyUser();
	}

	/**
	 * TODO EXPORT
	 * Return password of the proxy host that receives VMs from external clouds
	 */
	@WebMethod
	public String getCloudProxyPassword() {
		return cloudManager.getCloudProxyPassword();
	}

	/**
	 * TODO EXPORT
	 * Return storage path of the proxy host that receives VMs from external clouds
	 */
	@WebMethod
	public String getCloudProxyStoragePath() {
		return cloudManager.getCloudProxyStoragePath();
	}

	/**
	 * TODO EXPORT
	 * Set the proxy host that receives VMs from external clouds
	 * @param cloudProxy
	 */
	@WebMethod
	public void setProxyHost(String id, String ip, String storagePath, String username, String password) {
		cloudManager.setProxyHost(id, ip, storagePath, username, password);
	}
}
