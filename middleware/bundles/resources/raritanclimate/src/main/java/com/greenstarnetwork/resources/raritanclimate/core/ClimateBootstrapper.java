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
package com.greenstarnetwork.resources.raritanclimate.core;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.climate.HumidityElement;
import com.greenstarnetwork.models.climate.TemperatureElement;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceBootstrapper;
import com.iaasframework.resources.core.ResourceException;


/**
 * @author abdelhamid daouadji (Synchromedia)
 * 
 */

public class ClimateBootstrapper implements IResourceBootstrapper {

	// public RaritanclimateBootstrapper(){
	// super();
	// }
	String resourceId;
	private Logger logger = LoggerFactory.getLogger(ClimateBootstrapper.class);
	public void bootstrap(IResource engineIdentifier) throws ResourceException {
		resourceId=engineIdentifier.getResourceIdentifier().getId();
		ActionSetCapabilityClient client = new ActionSetCapabilityClient(resourceId);
		
		Map<Object, Object> arg = new Hashtable<Object, Object>();
		arg.put("System", "system1");
	
		String response = client.executeAction("ShowSystemAction", arg);
		logger.debug("********* Received response: "+ response + "\n");
	
		ModelCapabilityClient modelClient = new ModelCapabilityClient(engineIdentifier.getResourceIdentifier().getId());
		RequestModelResponse reqModel = modelClient.requestModel(true);
		Climate m = (Climate) reqModel.getResourceModel();
	//	ResourceParameters.setResourceID(engineIdentifier.getResourceIdentifier().getId());
		arg.remove((String) "System");
	
		// Get temperature sensor information
	
		List<TemperatureElement> temps = m.getTemperatureelement();
		List<HumidityElement> humidity = m.getHumidityelement();

		if (temps != null) {
			for (int i = 0; i < temps.size(); i++) {

				arg.remove((String) "Sensor");
				String ss = temps.get(i).getID();

				
				arg.put("Sensor", ss);

				if (ss.indexOf("ntempsensor") > -1) {
					
					response = client
							.executeAction("GetTemperatureAction", arg);
				
				}
				
				if (ss.indexOf("externalsensor") > -1) {
					
				
					response = client
							.executeAction("GetTemperatureAction", arg);
				
				}

			}
		}

		
		if (humidity != null) {
		
			for (int i = 0; i < humidity.size(); i++) {

				String ss = humidity.get(i).getID();
				arg.put("Sensor", ss);

				if (ss.indexOf("nsensor") > -1) {

				
					response = client.executeAction("GetHumidityAction", arg);
				
				}
				if (ss.indexOf("externalsensor") > -1) {
	
				
					response = client.executeAction("GetHumidityAction", arg);
				
				}

			}

		}
		

	
	
	}

}
