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

import com.greenstarnetwork.services.networkManager.INetworkManager;
import com.greenstarnetwork.services.networkManager.NetworkManagerException;
import com.greenstarnetwork.services.networkManager.model.VMNetAddress;

/**
 * NetworkManager webservice
 * 
 * @author knguyen
 *
 */
public class NetworkManagerSOAPEndpoint implements INetworkManagerSOAPEndpoint 
{
	private INetworkManager man = null; 
	
	public NetworkManagerSOAPEndpoint() {
	}
	
	@Override
	public VMNetAddress assignVMAddress(String vmName) {
		try {
			return this.man.assignVMAddress(vmName);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean releaseAssignment(String vmName){
		try {
			return this.man.releaseAssignment(vmName);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public VMNetAddress retrieveVMAddress(String mac, String vmName) {
		try {
			return this.man.retrieveVMAddress(mac, vmName);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param man the man to set
	 */
	public void setMan(INetworkManager man) {
		this.man = man;
	}

	/**
	 * @return the man
	 */
	public INetworkManager getMan() {
		return man;
	}

}
