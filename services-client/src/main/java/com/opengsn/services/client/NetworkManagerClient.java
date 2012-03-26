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
/**
 * 
 */
package com.opengsn.services.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greenstarnetwork.services.networkmanager.INetworkManager;
import com.greenstarnetwork.services.networkmanager.NetworkManagerException_Exception;
import com.greenstarnetwork.services.networkmanager.NetworkManagerRequest;
import com.greenstarnetwork.services.networkmanager.NetworkManagerResponse;
import com.greenstarnetwork.services.networkmanager.VmNetAddress;



/**
 * @author Scott Campbell (CRC)
 *
 */
public class NetworkManagerClient {
	

	private INetworkManager port = null;
	
	public NetworkManagerClient() {
	
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		           new String[]{"spring/application-context.xml"});		
		port = (INetworkManager) context.getBean("networkManagerClient");
	}


	/**
	 * 
	 * @param args
	 *            String[] - vmName
	 * @return
	 */
	public void assignVMAddress(String[] args) {
		checkArgs(args.length, 1);

		VmNetAddress resp;
		try {
			resp = port.assignVMAddress(args[0]);
			System.out.println("Reponse:\n Name: " + resp.getName() + ", IP: " + resp.getIp() + ", Mac: " + resp.getMac());
		} catch (NetworkManagerException_Exception e) {

			e.printStackTrace();		
		}
	}
	
	/**
	 * 
	 * @param args
	 *            String[] - mac, vmName
	 * @return
	 */
	public void retrieveVMAddress(String[] args) {
		checkArgs(args.length, 2);

		VmNetAddress resp;
		try {
			resp = port.retrieveVMAddress(args[0], args[1]);
			System.out.println("Reponse:\n Name: " + resp.getName() + ", IP: " + resp.getIp() + ", Mac: " + resp.getMac());
		} catch (NetworkManagerException_Exception e) {

			e.printStackTrace();		
		}
	}
	
	/**
	 * 
	 * @param args
	 *            String[] - vmName
	 * @return
	 */
	public void releaseAssignment(String[] args) {
		checkArgs(args.length, 1);

		boolean resp;
		try {
			resp = port.releaseAssignment(args[0]);
			System.out.println("Reponse:\n Name: " + resp);
		} catch (NetworkManagerException_Exception e) {

			e.printStackTrace();		
		}
	}
	
	/**
	 * 
	 * @param args
	 *            String[] - TODO
	 * @return
	 */
	public void executeCommand(String[] args) {

		//TODO Check what arguments are needed
		checkArgs(args.length, 1);

		NetworkManagerRequest req = new NetworkManagerRequest();
		//TODO fill in fields...
		
		NetworkManagerResponse resp = null;
			resp = port.executeCommand(req);
			System.out.println("Reponse:\n Name: " + resp.getMessage());

	}
	
	
	public void usage(String[] args) {
		System.out.println("\nNetworkManager Client Usage:");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("assignVMAddress         Parameters: vmName");
		System.out.println("executeCommand          Parameters: resource Id, command, action parameters space delimited ");
		System.out.println("releaseAssignment       Parameters: vmName");
		System.out.println("retrieveVMAddress       Parameters: mac vmName");
		System.out.println("\n");
	}
	
	private void checkArgs(int argsNum, int reqNum) {
		if (argsNum != reqNum) {
			System.out.println("Invalide number of arguments");
			usage(null);
			System.exit(1);
		}
	}
}
