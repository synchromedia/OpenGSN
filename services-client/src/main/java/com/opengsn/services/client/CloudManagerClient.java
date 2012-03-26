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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greenstarnetwork.services.cloudmanager.ICloudManager;
import com.greenstarnetwork.services.cloudmanager.IdPriority;
import com.greenstarnetwork.services.cloudmanager.OverrideMapType;
import com.greenstarnetwork.services.cloudmanager.OverrideMapType.PrioritiesList;

/**
 * Client to talk to the CloudManagerService
 * @author Scott Campbell (CRC)
 * 
 */
public class CloudManagerClient {

	private ICloudManager port = null;

	public CloudManagerClient() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
		           new String[]{"spring/application-context.xml"});		
		port = (ICloudManager) context.getBean("cloudManagerClient");
		
	}

	/**
	 * @param args
	 *            String[] - instanceName, memory, cpu, template
	 * @return
	 */
	public void createInstance(String[] args) {
		checkArgs(args.length, 4);

		String resp = port.createInstance(args[0], args[1], args[2],
				args[3]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * 
	 * @param args
	 *            String[] - hostId, instanceName, memory, cpu, template
	 * @return
	 */
	public void createInstanceInHost(String[] args) {
		checkArgs(args.length, 5);

		String resp = port.createInstanceInHost(args[0], args[1], args[2],
				args[3], args[4]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName
	 * @return
	 */
	public void destroyInstance(String[] args) {
		checkArgs(args.length, 2);

		String resp = port.destroyInstance(args[0], args[1]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * 
	 * @param args
	 *            String[] - An empty String
	 * 
	 */
	public void describeHosts(String[] args) {
		checkArgs(args.length, 0);

		List<String> resp = port.describeHosts();

		System.out.println("Results");
		for (int i = 0; i < resp.size(); i++) {
			System.out.println(resp.get(i));
		}

	}

	/**
	 * 
	 * @param args
	 *            String[] - hostId
	 */
	public void describeHost(String[] args) {
		checkArgs(args.length, 1);

		String resp = port.describeHost(args[0]);
		System.out.println(resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName
	 * @return
	 */
	public void startInstance(String[] args) {
		checkArgs(args.length, 2);
		String resp = port.startInstance(args[0], args[1]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName
	 * @return
	 */
	public void stopInstance(String[] args) {
		checkArgs(args.length, 2);

		String resp = port.stopInstance(args[0], args[1]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName
	 * @return
	 */
	public void rebootInstance(String[] args) {
		checkArgs(args.length, 2);

		String resp = port.rebootInstance(args[0], args[1]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName
	 * @return
	 */
	public void shutdownInstance(String[] args) {
		checkArgs(args.length, 2);

		String resp = port.shutdownInstance(args[0], args[1]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName, destinationHostId
	 * @return
	 */
	public void migrateInstance(String[] args) {
		checkArgs(args.length, 3);

		String resp = port.migrateInstance(args[0], args[1], args[2]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - hostId, instanceName
	 * @return
	 */
	public void setInstanceIPAddress(String[] args) {
		checkArgs(args.length, 2);

		String resp = port.setInstanceIPAddress(args[0], args[1]);
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * 
	 * @param args
	 *            String[] - domainId, ip, location, freeMemory, availableCPU,
	 *            vmUUID
	 * @return
	 */
	public void hostQuery(String[] args) {
		checkArgs(args.length, 6);

		List<String> resp = port.hostQuery(args[0], args[1], args[2],
				args[3], args[4], args[5]);
		System.out.println("Results");
		for (int i = 0; i < resp.size(); i++) {
			System.out.println(resp.get(i));
		}
	}

	/**
	 * 
	 *	@param args
	 *            String[] - resource id, new priority number, resource id2, priority num 2, etc.
	 */
	public void overrideHostPriories(String[] args) {
		
		PrioritiesList pList = new PrioritiesList();
		
		for(int i=0; i<args.length; i+=2){
			IdPriority priority = new IdPriority();
			priority.setId(args[i]);
			priority.setPriority(Integer.getInteger(args[i+1]));
			pList.getPriorities().add(priority);
		}
		
		OverrideMapType overrideMap = new OverrideMapType();
		overrideMap.setPrioritiesList(pList);

		port.overrideHostPriorities(overrideMap);
	}

	/**
	 * 
	 * @param args
	 *            String[] - resourceID, command, action parameters
	 *           
	 * @return
	 */
	public void executeAction(String[] args) {
		//consolidate the action parameters into a single string
		String params = new String();
		for(int i=2; i<args.length; i++) {
			params += args[i] + " ";
		}
		
		port.executeAction(args[0], args[1], params);
	}
	
	/**
	 * Print methods and their parameters
	 */
	public void usage(String[] args) {
		System.out.println("\nCloudManager Client Usage:");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("createInstance          Parameters: instanceName, memory, cpu, template ");
		System.out.println("createInstanceInHost    Parameters: hostId, instanceName, memory, cpu, template");
		System.out.println("describeHost            Parameters: hostId");
		System.out.println("describeHosts           Parameters: ");
		System.out.println("destroyInstance         Parameters: hostId, instanceName");
		System.out.println("executeAction           Parameters: resourceID, command, action parameters space delimited ");
		System.out.println("hostQuery               Parameters: domainId, ip, location, freeMemory, availableCPU, vmUUID");
		System.out.println("migrateInstance         Parameters: hostId, instanceName, destinationHostId");
		System.out.println("overrideHostPriorities  Parameters: resource id, new priority number, resource id2, priority num 2, etc.");
		System.out.println("setInstanceIPAddress    Parameters: hostId, instanceName ");
		System.out.println("shutdownInstance        Parameters: hostId, instanceName");
		System.out.println("startInstance           Parameters: hostId, instanceName");
		System.out.println("stopInstance            Parameters: hostId, instanceName");
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
