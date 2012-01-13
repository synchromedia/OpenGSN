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
package com.greenstarnetwork.resources.facilityResource.core;

import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.resources.facilityResource.actionset.OPSpecsCalculateAction;
import com.greenstarnetwork.resources.facilityResource.actionset.RefreshAction;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceBootstrapper;
import com.iaasframework.resources.core.ResourceException;

/**
 * 
 * will run refresh action for facility resource in the boot phase of the resource.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

 
public class FacilityResourceBootstrapper implements IResourceBootstrapper{
	
	org.slf4j.Logger log = LoggerFactory.getLogger(FacilityResourceBootstrapper.class);

	/**
	 * The Engine Model needs to be initialized here
	 */
	public void bootstrap(IResource engineIdentifier) throws ResourceException {
	
		ModelCapabilityClient modelClient = new ModelCapabilityClient(engineIdentifier.getResourceIdentifier().getId().toString());
		FacilityModel model = (FacilityModel)(modelClient.requestModel(true).getResourceModel());
		try {
			JMSQueue q = new JMSQueue(engineIdentifier.getResourceIdentifier().getId().toString());
			q.initializeJMS(model.getDomainID());
			q.sendModelToQueue(model);
			JMSQueue.setJMSQueue(q);
		} catch (Exception e) {
			log.debug(e.toString());
//			e.printStackTrace();
		}
		
		
		ActionSetCapabilityClient client = new ActionSetCapabilityClient(engineIdentifier.getResourceIdentifier().getId().toString());
		
		/*Logger.getLogger().log("********* Executing refresh action *****");
		String response = client.executeAction(RefreshAction.ACTION, null);
		Logger.getLogger().log("********* Reveived response: "+ response + "\n");
		response = client.executeAction(OPSpecsCalculateAction.ACTION, null);
		Logger.getLogger().log("********* Reveived response: "+ response + "\n");
		*/
		
	}

}
