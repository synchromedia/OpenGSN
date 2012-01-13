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

/**
 * Refresh the Controller's computation
 * @author knguyen
 *
 */
@Command(scope = "gsn-controller", name = "refresh", description="Force the controller to get new data and then compute migarion plan")
public class RefreshShellCommand extends OsgiCommandSupport {

	private IController controller = null;
	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
		loadController();
		controller.getResourcesFromCloud();
		controller.setMode(IController.REAL_MODE);
		controller.refreshPlan();
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
