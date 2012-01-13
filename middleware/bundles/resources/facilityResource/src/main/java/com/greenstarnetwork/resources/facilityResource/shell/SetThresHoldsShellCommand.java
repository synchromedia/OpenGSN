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
 * Karaf command to set op-hout thresholds for a given domain.
 *
 */


import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.resources.facilityResource.actionset.SetOPHourThresholdsAction;
import com.greenstarnetwork.resources.facilityResource.core.Logger;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;


@Command(scope = "gsn-facility", name = "setThresholds", description="Set OP Hours Thresholds KARAF Command")
public class SetThresHoldsShellCommand extends OsgiCommandSupport {



	@Argument(index = 0, name = "facilityResourceID", description = "facilityResourceID", required = true, multiValued = false)
	private String facilityResourceID;

	@Argument(index = 1, name = "opHourThreshold", description = "opHourThreshold", required = true, multiValued = false)
	private String opHourThreshold;

	@Argument(index = 2, name = "opHourThresholdUnderMax", description = "opHourThresholdUnderMax", required = true, multiValued = false)
	private String opHourThresholdUnderMax;



	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
		if (facilityResourceID==null){
			System.out.println("facilityResourceID is not provided.");
		}else if(Double.valueOf(opHourThresholdUnderMax)>Double.valueOf(opHourThreshold)){
			System.out.println("opHourThresholdUnderMax cannot be greater than opHourThreshold.");
		}else{

			ActionSetCapabilityClient client = new ActionSetCapabilityClient(facilityResourceID);

			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("opHourThreshold", opHourThreshold);
			args.put("opHourThresholdUnderMax", opHourThresholdUnderMax);

			args.put("resourceID", facilityResourceID);

			Logger.getLogger().begin(SetOPHourThresholdsAction.ACTION);
			String response = client.executeAction(SetOPHourThresholdsAction.ACTION, args);
			Logger.getLogger().end(SetOPHourThresholdsAction.ACTION+ " "+response );
			

		}

		return null;
	}
}
