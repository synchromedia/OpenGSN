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

import com.greenstarnetwork.resources.facilityResource.actionset.SetOPHoursAction;
import com.greenstarnetwork.resources.facilityResource.core.Logger;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;


@Command(scope = "gsn-facility", name = "setOPHours", description="Set OP Hours KARAF Command")
public class SetOPHoursShellCommand extends OsgiCommandSupport {



	@Argument(index = 0, name = "facilityResourceID", description = "facilityResourceID", required = true, multiValued = false)
	private String facilityResourceID;

	@Argument(index = 1, name = "opHourUnderCurrentLoad", description = "opHourUnderCurrentLoad", required = true, multiValued = false)
	private String opHourUnderCurrentLoad;

	@Argument(index = 2, name = "opHourUnderMaximumLoad", description = "opHourUnderMaximumLoad", required = true, multiValued = false)
	private String opHourUnderMaximumLoad;



	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
//		System.out.println("Command syntax: setOPHours <facilityResourceID> <opHourUnderCurrentLoad> <opHourUnderMaximumLoad> ");
		if (facilityResourceID==null){
			System.out.println("facilityResourceID is not provided.");
		}else if(Double.valueOf(opHourUnderMaximumLoad)>Double.valueOf(opHourUnderCurrentLoad)){
			System.out.println("opHourUnderMaximumLoad cannot be greater than opHourUnderCurrentLoad.");
		}else{

			ActionSetCapabilityClient client = new ActionSetCapabilityClient(facilityResourceID);

			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("opHourUnderCurrentLoad", opHourUnderCurrentLoad);
			args.put("opHourUnderMaximumLoad", opHourUnderMaximumLoad);

			args.put("resourceID", facilityResourceID);

			Logger.getLogger().begin(SetOPHoursAction.ACTION);
			String response = client.executeAction(SetOPHoursAction.ACTION, args);
			Logger.getLogger().end(SetOPHoursAction.ACTION+ " "+response );
			

		}

		return null;
	}
}
