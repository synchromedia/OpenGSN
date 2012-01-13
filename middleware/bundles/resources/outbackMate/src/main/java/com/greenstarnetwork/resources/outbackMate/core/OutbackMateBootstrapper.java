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
package com.greenstarnetwork.resources.outbackMate.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.resources.outbackMate.actionset.RefreshAction;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceBootstrapper;
import com.iaasframework.resources.core.ResourceException;

/**
 * 
 * @author knguyen
 *
 */
public class OutbackMateBootstrapper implements IResourceBootstrapper{

	private Logger logger = LoggerFactory.getLogger(OutbackMateBootstrapper.class);
	/**
	 * The Engine Model needs to be initialized here
	 */
	public void bootstrap(IResource engineIdentifier) throws ResourceException {

		ActionSetCapabilityClient client = new ActionSetCapabilityClient(engineIdentifier.getResourceIdentifier().getId());
		String response = client.executeAction(RefreshAction.ACTION, null);
		logger.debug("********* Reveived response: "+ response + "\n");
		logger.debug("\n\n********* Complete ***********\n\n");
	}
}
