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
package com.greenstarnetwork.resources.libvirt.core;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceBootstrapper;
import com.iaasframework.resources.core.ResourceException;

/**
 * 
 * @author knguyen
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
public class vmmBootstrapper implements IResourceBootstrapper{
	
	private Logger logger = LoggerFactory.getLogger(vmmBootstrapper.class);

	/**
	 * The Engine Model needs to be initialized here
	 */
	public void bootstrap(IResource engineIdentifier) throws ResourceException {
		
		logger.debug("********* Executing host info action *****");
		ActionSetCapabilityClient client = new ActionSetCapabilityClient(engineIdentifier.getResourceIdentifier().getId().toString());
		String response = client.executeAction("HostInfoAction", null);
		logger.debug("********* Reveived response: "+ response + "\n");

		logger.debug("********* Executing refresh action *****");
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put("resourceID", engineIdentifier.getResourceIdentifier().getId().toString());
		response = client.executeAction("RefreshAction", args);
		
		logger.debug("********* Starting Host monitor *****");
		ModelCapabilityClient modelclient = new ModelCapabilityClient(engineIdentifier.getResourceIdentifier().getId().toString());
        RequestModelResponse rModel = modelclient.requestModel(true);
        VmHostModel model = (VmHostModel)rModel.getResourceModel();
		try {
			HostMonitor monitor = new HostMonitor(model.getAddress());
//			System.err.println("HostMonitor: address: " + model.getAddress());
		} catch (JMSException e) {
			System.err.println("HostMonitor: Exception: " + e.toString());
		}
		
		logger.debug("\n\n********* Complete ***********\n\n");
	}

}
