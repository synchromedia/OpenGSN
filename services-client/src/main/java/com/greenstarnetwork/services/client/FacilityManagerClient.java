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
package com.greenstarnetwork.services.client;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greenstarnetwork.services.facilitymanager.FacilityManagerException_Exception;
import com.greenstarnetwork.services.facilitymanager.IFacilityManager;

/**
 * @author Scott Campbell (CRC)
 * 
 */
public class FacilityManagerClient {

	private IFacilityManager port = null;


	public FacilityManagerClient() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		           new String[]{"spring/application-context.xml"});		
		port = (IFacilityManager) context.getBean("facilityManagerClient");
	}

	/**
	 * 
	 * @param args
	 *            String[] - empty list
	 * @return
	 * @throws Exception
	 */
	public void listAllFacilities(String[] args) {
		checkArgs(args.length, 0);

		List<String> resp;
		try {
			resp = port.listAllFacilities();

			System.out.println("Results");
			for (int i = 0; i < resp.size(); i++) {
				System.out.println(resp.get(i));
			}

		} catch (FacilityManagerException_Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 *            String[] - empty list
	 * @return
	 * @throws Exception
	 */
	public void listAllPDUs(String[] args) {
		checkArgs(args.length, 0);

		try {
			List<String> resp = port.listAllPDUs();

			System.out.println("Results");
			for (int i = 0; i < resp.size(); i++) {
				System.out.println(resp.get(i));
			}

		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 *            String[] - empty list
	 * @return
	 * @throws Exception
	 */
	public void listAllPowerSources(String[] args) {
		checkArgs(args.length, 0);

		try {
			List<String> resp = port.listAllPowerSources();

			System.out.println("Results");
			for (int i = 0; i < resp.size(); i++) {
				System.out.println(resp.get(i));
			}
		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 *            String[] - empty list
	 * @return
	 * @throws Exception
	 */
	public void listAllClimates(String[] args) throws Exception {
		checkArgs(args.length, 0);
		List<String> resp = port.listAllClimates();

		System.out.println("Results");
		for (int i = 0; i < resp.size(); i++) {
			System.out.println(resp.get(i));
		}
	}

	/**
	 * 
	 * @param args
	 *            String - resource type, resource id
	 */
	public void getResourceModel(String[] args) {
		checkArgs(args.length, 2);

		String resp = port.getResourceModel(args[0], args[1]);
		System.out.println(resp);
	}

	/**
	 * 
	 * @param args
	 *            String - resource id, action name, action parameters
	 */
	public void executeAction(String[] args) {
	
		//consolidate the action parameters into a single string
		String params = new String();
		for(int i=2; i<args.length; i++) {
			params += args[i] + " ";
		}
		
		String resp;
		try {
			resp = port.executeAction(args[0], args[1], params);
			System.out.println(resp);

		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 *            String - resourceId
	 */
	public void refreshResource(String[] args) {
		checkArgs(args.length, 1);

		String resp;
		try {
			resp = port.refreshResource(args[0]);
			System.out.println(resp);

		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param args
	 *            String - resourceId
	 */
	public void turnOffResource(String[] args) {
		checkArgs(args.length, 1);

		String resp;
		try {
			resp = port.turnOffResource(args[0]);
			System.out.println(resp);

		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 *            String - resourceId
	 */
	public void calculateOpHour(String[] args) {
		checkArgs(args.length, 1);

		String resp;
		try {
			resp = port.calculateOpHour(args[0]);
			System.out.println(resp);

		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 *            String - address
	 */
	public void registerController(String[] args) {
		checkArgs(args.length, 1);

		port.registerController(args[0]);
	}
	
	public void turnOnResource(String[] args) {
		checkArgs(args.length, 1);
		
		try {
			String resp = port.turnOnResource(args[0]);
			System.out.println(resp);
		} catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param args
	 *            String - start date, end date, resourceId
	 */
	public void getArchiveDataByRangeDate(String[] args) {
		checkArgs(args.length, 3);

		String resp = port
				.getArchiveDataByRangDate(args[0], args[1], args[2]);
		System.out.println(resp);
	}

	public void usage(String args[]) {
		System.out.println("\nFacilityManager Client Usage:");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("caclulateOpHour         Parameters: resource Id");
		System.out.println("executeAction           Parameters: resource Id, command, action parameters space delimited ");
		System.out.println("getArchivedDataByRangeDate    Parameters: start date, end date, resourceId");
		System.out.println("getResourceModel        Parameters: resource type, resource Id");
		System.out.println("listAllClimates         Parameters: ");
		System.out.println("listAllFacilities       Parameters: ");
		System.out.println("listAllPDUs             Parameters: ");
		System.out.println("listAllPowerSources     Parameters: ");
		System.out.println("refreshResource         Parameters: resource id");
		System.out.println("registerController      Parameters: controller URL ");
		System.out.println("turnOffResource         Parameters: resource id");
		System.out.println("turnOnResource           Parameters: resource id");
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
