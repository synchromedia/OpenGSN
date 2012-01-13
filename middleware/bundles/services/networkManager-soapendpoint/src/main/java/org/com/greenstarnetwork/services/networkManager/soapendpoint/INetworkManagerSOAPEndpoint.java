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
package org.com.greenstarnetwork.services.networkManager.soapendpoint;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.greenstarnetwork.services.networkManager.NetworkManagerException;
import com.greenstarnetwork.services.networkManager.model.VMNetAddress;


/**
 * NetworkManager webservice
 * 
 * @author knguyen
 *
 */
@WebService
public interface INetworkManagerSOAPEndpoint 
{
	/**
	 * Assign an available IP address to a VM
	 * @param vmName
	 * @return null if all addresses in IP pool had been assigned
	 * @throws NetworkManagerException
	 */
	@WebMethod
	public VMNetAddress assignVMAddress(String vmName);
	
	/**
	 * Retrieve the ip address that has been assigned to a given MAC address
	 * @param mac
	 * @param vmName
	 * @return
	 * @throws NetworkManagerException
	 */
	@WebMethod
	public VMNetAddress retrieveVMAddress(String mac, String vmName);

	/**
	 * Release an assignment of IP/MAC and a VM
	 * @param vmName
	 * @return
	 * @throws NetworkManagerException
	 */
	@WebMethod
	public boolean releaseAssignment(String vmName);
}
