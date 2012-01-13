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

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.facilityModel.OperationalSpecs;
import com.greenstarnetwork.services.controller.core.Activator;
import com.greenstarnetwork.services.controller.core.IController;
import com.iaasframework.resources.core.RegistryUtil;

/**
 * 
 * @author knguyen
 *
 */
@Command(scope = "gsn-controller", name = "downFacility", description="Set a value to facility model of a domain")
public class SetFacilityShellCommand extends OsgiCommandSupport {

	@Argument(index = 0, name = "domainId", description = "Id of the domain (which is actually its ip)", required = true, multiValued = false)
	private String domainId;
	private IController controller = null;
	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
		System.err.println("Change op-hour of the domain " + domainId + " to 0. Threshold is from 0.5 to 1 hour.");
		loadController();
		//controller.setMinAcceptableLifeTime(0.5);
		//controller.setMaxNecessaryLifeTime(1);
		FacilityModel model = new FacilityModel();
		model.setDomainID(domainId);
		OperationalSpecs op = new OperationalSpecs();
		op.setOpHourUnderCurrentLoad(0);
		model.setOperationalSpecs(op);
		controller.changeFacilityModel(model);
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
