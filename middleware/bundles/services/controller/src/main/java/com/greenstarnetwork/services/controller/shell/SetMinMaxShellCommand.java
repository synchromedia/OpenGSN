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
 */

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.services.controller.core.Activator;
import com.greenstarnetwork.services.controller.core.IController;
import com.iaasframework.resources.core.RegistryUtil;

/**
 * 
 * @author knguyen
 *
 */
@Command(scope = "gsn-controller", name = "setMinMax", description="Set Min Max Acceptable Life Time")
public class SetMinMaxShellCommand extends OsgiCommandSupport {

	@Argument(index = 0, name = "min", description = "Min Acceptable Life Time", required = true, multiValued = false)
	private String min;
	@Argument(index = 1, name = "max", description = "Max Necessary Life Time", required = true, multiValued = false)
	private String max;
	private IController controller = null;
	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
		loadController();
		//controller.setMaxNecessaryLifeTime(new Double(max).doubleValue());
		//controller.setMinAcceptableLifeTime(new Double(min).doubleValue());
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
