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
package com.greenstarnetwork.resources.facilityResource.shell;

/**
 * This class will provide a command to get the status of the controller in OSGI container.
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.resources.facilityResource.actionset.CheckPowerAction;
import com.greenstarnetwork.resources.facilityResource.actionset.OPSpecsCalculateAction;
import com.greenstarnetwork.resources.facilityResource.core.Logger;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;


@Command(scope = "gsn-facility", name = "checkPower", description="Check the op hour to be above threshold.")
public class CheckPowerShellCommand extends OsgiCommandSupport {


	@Argument(index = 0, name = "facilityResourceID", description = "facilityResourceID", required = false, multiValued = false)
	private String facilityResourceID;

	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {

		System.out.println("Command syntax: checkPower <facilityResourceID> ");
		if (facilityResourceID==null){
			System.out.println("facilityResourceID is not provided.");
		}else{

			ActionSetCapabilityClient client = new ActionSetCapabilityClient(facilityResourceID);
			Map<Object, Object> args = new Hashtable<Object, Object>();

			args.put("resourceID", facilityResourceID);

			Logger.getLogger().begin(CheckPowerAction.ACTION);
			String response = client.executeAction(CheckPowerAction.ACTION, args);
			Logger.getLogger().end(CheckPowerAction.ACTION+ " "+response );



			
		}
		return null;
	}
}
