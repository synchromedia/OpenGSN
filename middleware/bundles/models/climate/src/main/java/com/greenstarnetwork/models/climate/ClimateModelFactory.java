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
package com.greenstarnetwork.models.climate;


import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.model.IResourceModelFactory;
import com.iaasframework.capabilities.model.ModelException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;

public class ClimateModelFactory implements IResourceModelFactory 
{
	public synchronized IResourceModel createResourceModelInstance(CapabilityDescriptor descriptor)
			throws ModelException {
		if (descriptor.getPropertyValue("Host") == null)
	        throw new ModelException("The Host address property" +
	          " is required to create a Raritan Climate model");

		if (descriptor.getPropertyValue("Port") == null)
	        throw new ModelException("The Port number property" +
	          " is required to create Raritan Climate model");

		Climate model = new Climate();
		model.setHost(descriptor.getPropertyValue("Host"));
		model.setPort(descriptor.getPropertyValue("Port"));
		model.setHumidityLowerThreshold(descriptor.getPropertyValue("humidityLowerThreshold"));
		model.setHumidityUpperThreshold(descriptor.getPropertyValue("HumidityUpperThreshold"));
		model.setTemperatureLowerThreshold(descriptor.getPropertyValue("TemperatureLowerThreshold"));
		model.setTemperatureUpperThreshold(descriptor.getPropertyValue("TemperatureUpperThreshold"));
		return model;
		
		
	}

}
