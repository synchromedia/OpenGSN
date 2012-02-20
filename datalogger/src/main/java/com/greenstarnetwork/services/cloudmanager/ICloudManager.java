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
package com.greenstarnetwork.services.cloudmanager;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@WebService
public interface ICloudManager {

	/**
	 * Retrieve Hosts models with their VMs
	 * 
	 * @return List of Hosts Models
	 */
	public List<String> describeHosts();

	/**
	 * Retrieve the model of a Host
	 * 
	 * @param hostId
	 *            id of the host (== id of the resource)
	 * @return Host Model
	 */
	public String describeHost(String hostId);

	/**
	 * Create a virtual machine in a specified host
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @param memory
	 *            Memory Amount needed by the Virtual Machine
	 * @param cpu
	 *            Number of CPUs needed by the Virtual Machine
	 * @param template
	 *            template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name,
	 *         ip) if request successes, error message otherwise
	 */
	public String createInstanceInHost(String hostId, String instanceName, String memory, String cpu, String template);

	/**
	 * Start a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String startInstance(String hostId, String instanceName);

	/**
	 * Stop a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String stopInstance(String hostId, String instanceName);

	/**
	 * Reboot a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String rebootInstance(String hostId, String instanceName);

	/**
	 * Shutdown a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String shutdownInstance(String hostId, String instanceName);

	/**
	 * migrate a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @param destinationHostId
	 *            id of the destination host (id of the IAAS destination
	 *            resource)
	 * @return Success or error message
	 */
	public String migrateInstance(String hostId, String instanceName, String destinationHostId);

	/**
	 * Destroy a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String destroyInstance(String hostId, String instanceName);

	/**
	 * Set IP address to a virtual machine
	 * 
	 * @param hostId
	 * @param instanceName
	 * @return
	 */
	public String setInstanceIPAddress(String hostId, String instanceName);

	/**
	 * Generic query which retrieve hosts that match the function parameters. A
	 * parameter equal to -1 will be discarded from the research
	 * 
	 * @param domainId
	 *            domain ID (Which is the ip of the IAAS container)
	 * @param ip
	 *            Host ip
	 * @param location
	 *            Host location
	 * @param freeMemory
	 *            Host freeMemory bigger than the freeMemory parameter (unit:
	 *            Ko)
	 * @param availableCPU
	 *            Host available CPUs
	 * @param vmUUID
	 *            Enable to retrieve the Host which contains the Virtual Machine
	 *            with the UUID parameter
	 * @return
	 */
	public List<String> hostQuery(String ip, String location, String freeMemory, String availableCPU,
			String vmUUID);

	/**
	 * Override Host priorities in the cloud manager host model
	 * 
	 * @param priorityTable
	 *            Map with host priorities (Key= host resource ID, Integer =
	 *            host priority)
	 */
	public void overrideHostPriorities(
			@XmlJavaTypeAdapter(OverridePrioritiesMapAdapter.class) HashMap<String, Integer> priorityTable);

	/**
	 * Create a virtual machine by letting the cloud manager finding the most
	 * appropriate host
	 * 
	 * @param instanceName
	 *            Virtual Machine name
	 * @param memory
	 *            Memory Amount needed by the Virtual Machine
	 * @param cpu
	 *            Number of CPUs needed by the Virtual Machine
	 * @param template
	 *            template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name,
	 *         ip) if request successes, error message otherwise
	 */
	public String createInstance(String instanceName, String memory, String cpu, String template);

	/**
	 * Execute an action on a IAAS resource
	 * 
	 * @param resourceId
	 *            id of the resource
	 * @param actionName
	 *            name of the action to execute
	 * @param parameters
	 *            parameters used by the action
	 * @return Action Result
	 */
	public String executeAction(String resourceId, String actionName, String parameters);
}
