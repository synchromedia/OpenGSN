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
package com.greenstarnetwork.capabilities.archive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.iaasframework.resources.core.capability.AbstractCapabilityFactory;
import com.iaasframework.resources.core.capability.CapabilityException;
import com.iaasframework.resources.core.capability.ICapability;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;

/**
 * 
 * This class is responsible for creating the Archive capabilities.
 * 
 * @author abdelhamid Synchromedia
 * 
 */
public class ArchiveCapabilityFactory extends AbstractCapabilityFactory {

	/* Logger */
	private Logger logger = LoggerFactory
			.getLogger(ArchiveCapabilityFactory.class);

	public ArchiveCapabilityFactory() {
		logger.info("****************************   Archive capability created ");
	}

	@Override
	public ICapability createCapability(CapabilityDescriptor capabilityDescriptor, String resourceId)
			throws CapabilityException {
		ArchiveCapability capability = null;
	
		capability= new ArchiveCapability(capabilityDescriptor, resourceId);
		capabilityDescriptor.setEnabled(true);

		logger.info("****************************   Archive capability created ");
		return capability;
	}

	
}
