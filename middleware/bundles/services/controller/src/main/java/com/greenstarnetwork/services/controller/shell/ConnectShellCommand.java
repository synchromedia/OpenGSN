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
package com.greenstarnetwork.services.controller.shell;

/**
 * This class will provide a command to get the status of the controller in OSGI container.
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.services.controller.core.Activator;
import com.greenstarnetwork.services.controller.core.IController;
import com.iaasframework.resources.core.RegistryUtil;

@Command(scope = "gsn-controller", name = "connect", description="Connect the controller to a host where cloud & facility managers are running")
public class ConnectShellCommand extends OsgiCommandSupport {

//	@Argument(index = 0, name = "hostId", description = "Id of the host", required = true, multiValued = false)
//	private String hostId;
//	@Argument(index = 1, name = "vmName", description = "VM to move", required = true, multiValued = false)
//	private String vmName;
//	@Argument(index = 2, name = "targetID", description = "Id of the target host", required = true, multiValued = false)
//	private String targetID;

	private String hostId = "127.0.0.1";
	private IController controller = null;
	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
		System.err.println("Contorller is connecting to the local cloud & facility managers.");
		loadController();
		controller.connect(hostId);
		controller.getResourcesFromCloud();
//		System.err.println(controller.getHostList().toXML());

//		System.err.println("Migrating VM ...");
//		
//		String ret = controller.getCloudManager().migrateVM(sourceID, vmName, targetID);
//		System.err.println("Response: " + ret);
		return null;
	}

	private void loadController(){
		try {
			controller = (IController) RegistryUtil.getServiceFromRegistry(
					Activator.getContext(), IController.class.getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
