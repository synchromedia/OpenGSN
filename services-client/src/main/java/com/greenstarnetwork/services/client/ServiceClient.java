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

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Scott Campbell (CRC)
 * 
 */
public class ServiceClient {

	private static final String CM = "cm";
	private static final String FM = "fm";
	private static final String NM = "nm";
	
	private static final String CMClient = "com.greenstarnetwork.services.client.CloudManagerClient";
	private static final String FMClient = "com.greenstarnetwork.services.client.FacilityManagerClient";
	private static final String NMClient = "com.greenstarnetwork.services.client.NetworkManagerClient";


	public ServiceClient() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServiceClient client = new ServiceClient();

		String arguments = new String();
		for(int i=0; i<args.length; i++) {
			arguments = args[i] + " ";
		}
		System.out.println("Arguments: " + arguments);
		
		if (args.length < 2) {
			client.printUsage();
			System.exit(1);
		}
		client.process(args);
	}

	/**
	 * Use the commandline args to invoke the proper service client and operation using Reflection
	 * @param args
	 */
	private void process(String[] args) {

		Class<?> c = null;
		try {

			if (args[0].equals(CM)) {
				c = Class.forName(CMClient);
			}
			else if (args[0].equals(FM)) {
				c = Class.forName(FMClient);
			}
			else if (args[0].equals(NM)) {
				c = Class.forName(NMClient);
			}
			else {
				System.out.println("Invalid Service");
				printUsage();
			}

			//Use reflection to call the service and operation that was given from the command line
			Object obj = c.newInstance();
			@SuppressWarnings("rawtypes")
			Class[] argTypes = new Class[] { String[].class };
			Method m = c.getDeclaredMethod(args[1], argTypes);

			String[] methodArgs = Arrays.copyOfRange(args, 2, args.length);
			System.out.println("invoking Operation: " + c.getName() + "." + m.getName());
			m.invoke(obj, (Object)methodArgs);


		} catch (NoSuchMethodException e) {			
			System.out.println("Invalid Operation: " + args[1]);
			System.out.println("Operations for the " + c.getName() + ": ");
			Method[] allMethods = c.getDeclaredMethods();
			for (Method m : allMethods) {
				System.out.println("  " + m.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void printUsage() {
		System.out
				.println("GSN Services Config Usage \n"
						+ "ServiceClient <service> <operation> <parameter1> <parameter2> ...\n\n"
						+ "Services: cm = Cloud Manager \n"
						+ "          fm = Facility Manager \n"
						+ "          nm = Network Manager \n\n"
						+ "See Client <service> for available operations in each ");
		
	}

}
