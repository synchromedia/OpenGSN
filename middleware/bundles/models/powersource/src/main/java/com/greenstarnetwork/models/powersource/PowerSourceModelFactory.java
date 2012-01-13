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
package com.greenstarnetwork.models.powersource;


import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.model.IResourceModelFactory;
import com.iaasframework.capabilities.model.ModelException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;

/**
 * PowerSource Model Factory
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class PowerSourceModelFactory implements IResourceModelFactory 
{
	public synchronized IResourceModel createResourceModelInstance(CapabilityDescriptor descriptor)
			throws ModelException {
		if (descriptor.getPropertyValue("Host") == null)
	        throw new ModelException("The Host address property" +
	          " is required to create a PowerSource model");

		if (descriptor.getPropertyValue("Port") == null)
	        throw new ModelException("The Port number property" +
	          " is required to create a PowerSource model");

		try {
			PowerSourceModel model = new PowerSourceModel();
			model.setHost(descriptor.getPropertyValue("Host"));
			model.setPort(descriptor.getPropertyValue("Port"));
			if (descriptor.getPropertyValue("location") != null)
				model.setLocation(descriptor.getPropertyValue("location"));

			return model;
		} catch (PowerSourceException e) {
			throw new ModelException("Description Port property error:" + e.toString());		
		}
	}
}