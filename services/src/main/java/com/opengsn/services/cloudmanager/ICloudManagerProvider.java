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
package com.opengsn.services.cloudmanager;

import java.util.List;
import java.util.Map;

import com.opengsn.services.cloudmanager.model.Host;
import com.opengsn.services.networkmanager.model.VMNetAddress;

/**
 * 
 * @author Mathieu Lemay
 * 
 */
public interface ICloudManagerProvider {
	/**
	 * Retrieve a Network Address given a VM name
	 * 
	 * @param vmName
	 *            Name of the virtual machine
	 * @return Address JSON Document
	 */
	public VMNetAddress getNetworkAddress(String vmName);

	/**
	 * Lists all the available hosts which you can use
	 * 
	 * @return Results Document of Hosts
	 */
	public List<Host> listAllHosts();

	/**
	 * Gets information about a host
	 * 
	 * @param id
	 *            ID of the host to query
	 * @return
	 */
	public Host getHostInformation(String id);

	/**
	 * Execute an action on an underlying resource
	 * 
	 * @param resourceId
	 *            id of the resource
	 * @param actionName
	 *            name of the action to execute
	 * @param payload
	 *            parameters used by the action
	 * @return Action Result
	 */
	public String execute(String resourceId, String actionName, Map<String, String> parameters);

	public List<Host> hostQuery(String ip, String location, String freeMemory, String availableCPU, String vmUUID);
}
