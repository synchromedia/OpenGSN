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
package com.opengsn.controller.client;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greenstarnetwork.controller.ControllerException_Exception;
import com.greenstarnetwork.controller.IController;

/**
 * @author Scott Campbell (CRC)
 * 
 */
public class ControllerClient {

	IController controller = null;

	public ControllerClient() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring/application-context.xml" });
		controller = (IController) context.getBean("client");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ControllerClient client = new ControllerClient();

		String arguments = new String();
		for (int i = 0; i < args.length; i++) {
			arguments = args[i] + " ";
		}
		System.out.println("Arguments: " + arguments);

		if (args.length < 1) {
			client.usage(null);
			System.exit(1);
		}
		client.process(args);
	}
	
	/**
	 * Use the commandline args to invoke the proper service client and
	 * operation using Reflection
	 * 
	 * @param args
	 */
	private void process(String[] args) {

		Class<?> c = this.getClass();
		try {
			// Class<?> c =
			// Class.forName("com.greenstarnetwork.controller.client.ControllerWrapper");

			// Use reflection to call the service and operation that was given
			// from the command line
			Object obj = c.newInstance();
			@SuppressWarnings("rawtypes")
			Class[] argTypes = new Class[] { String[].class };
			Method m = c.getDeclaredMethod(args[0], argTypes);

			String[] methodArgs = Arrays.copyOfRange(args, 1, args.length);
			System.out.println("invoking Operation: " + c.getName() + "."
					+ m.getName());
			m.invoke(obj, (Object) methodArgs);

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

	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void connect(String[] args) {
		controller.connect();
	}

	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void executePlan(String[] args) {
		try {
			String result = controller.executePlan();
			System.out.println("Result: " + result);
		} catch (ControllerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Randomly generate a list of hosts for use in simulation mode only
	 * 
	 * @param args
	 *            String[] - number of hosts
	 * @return
	 */
	public void generateHostList(String[] args) {
		checkArgs(args.length, 1);

		String resp = controller.generateHostList(Integer.parseInt(args[0]));
		System.out.println("Reponse:\n" + resp);
	}

	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void generatePlan(String[] args) {
		try {
			controller.generatePlan();
		} catch (ControllerException_Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void getHostList(String[] args) {
		String result = controller.getHostList();
		System.out.println("Result: " + result);
	}
	
	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void getLinkTable(String[] args) {
		String result = controller.getLinkTable();
		System.out.println("Result: " + result);
	}
	
	/**
	 * @param args
	 *            String[] - filename 
	 * @return
	 */
	public void getLogContent(String[] args) {
		checkArgs(args.length, 1);

		String resp = controller.getLogContent(args[0]);
		System.out.println("Reponse:\n" + resp);
	}
	
	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void getLogFiles(String[] args) {
		List<String> result = controller.getLogFiles();
		
		System.out.println("Results:");
		for(int i=0; i<result.size(); i++) {
			System.out.println("File " + i + ": " + result.get(i));
		}
	}

	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void getMigrationPlan(String[] args) {

		String resp = controller.getMigrationPlan();
		System.out.println("Reponse:\n" + resp);
	}
	

	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void getMode(String[] args) {

		int resp = controller.getMode();
		System.out.println("Reponse:\n" + resp);
	}
	
	/**
	 * @param args
	 *            String[] - empty arguments
	 * @return
	 */
	public void getResourcesFromCloud(String[] args) {
		controller.getResourcesFromCloud();
	}
	
	/**
	 * @param args
	 *            String[] - mode 
	 * @return
	 */
	public void setMode(String[] args) {
		checkArgs(args.length, 1);
		controller.setMode(Integer.parseInt(args[0]));
	}
	
	/**
	 * Print methods and their parameters
	 */
	public void usage(String[] args) {
		System.out.println("\nController Client Usage:");
		System.out.println("---------------------------------------");
		System.out.println("connect                Parameters: ");
		System.out.println("executePlan            Parameters: ");
		System.out.println("generateHostList       Parameters: number of hosts");
		System.out.println("generatePlan           Parameters: ");
		System.out.println("getHostList            Parameters: ");
		System.out.println("getLinkTable           Parameters:");
		System.out.println("getLogContent          Parameters: filename");
		System.out.println("getLogFiles            Parameters: ");
		System.out.println("getMode                Parameters: ");
		System.out.println("getResourcesFromCloud  Parameters: ");
		System.out.println("setMode                Parameters: mode");
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
