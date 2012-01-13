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
package com.greenstarnetwork.services.networkManager;

import com.greenstarnetwork.services.networkManager.jms.ResponseMessage;
import com.greenstarnetwork.services.networkManager.model.VMNetAddress;
import com.iaasframework.capabilities.actionset.queue.ActionRequest;

/**
 * Network management module
 * Provide network functions, such as VM address management (MAC & IP)
 * 
 * @author knguyen
 *
 */
public interface INetworkManager {

	public static String IP_TABLE = "/gsn/ipTable.xml";			//path to file saving IP table
	public static String IAAS_HOME = "IAAS_HOME";				//environment variable - path to IAAS home
	public static String DEFAULT_DOMAIN = "127.0.0.1";			//default domain IP
	public static String DEFAULT_ID = "NETWORK-MANAGER-V-10";		//default ID used for creating JMS channel;

	/**
	 * List of commands (or functions) provided by Network Manager
	 */
	public static String CMD_assignVMAddress = "assignVMAddress";
	public static String CMD_retrieveVMAddress = "retrieveVMAddress";
	public static String CMD_releaseAssignment = "releaseAssignment";
	/**
	 * Assign an available IP address to a VM
	 * @param vmName
	 * @return null if all addresses in IP pool had been assigned
	 * @throws NetworkManagerException
	 */
	public VMNetAddress assignVMAddress(String vmName) throws NetworkManagerException;
	
	/**
	 * Retrieve the ip address that has been assigned to a given MAC address
	 * @param mac
	 * @param vmName
	 * @return
	 * @throws NetworkManagerException
	 */
	public VMNetAddress retrieveVMAddress(String mac, String vmName)  throws NetworkManagerException;

	/**
	 * Release an assignment of IP/MAC and a VM
	 * @param vmName
	 * @return
	 * @throws NetworkManagerException
	 */
	public boolean releaseAssignment(String vmName) throws NetworkManagerException;

	/**
	 * Parse a request sent through JMS and execute a corresponding command.
	 * @param request
	 * @return
	 */
	public ResponseMessage executeCommand(ActionRequest request);
	
	/**
	 * Return JMS ID of the network manager communication channel
	 * @return
	 */
	public String getJMSId();
}
