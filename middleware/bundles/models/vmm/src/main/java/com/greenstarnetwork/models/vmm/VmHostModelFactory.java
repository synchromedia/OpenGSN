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
package com.greenstarnetwork.models.vmm;

import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.model.IResourceModelFactory;
import com.iaasframework.capabilities.model.ModelException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;


/**
 * this class is used to create the VM Host model
 * 
 * @author adaouadji (adaouadji@synchromedia.ca)
 *
 */
public class VmHostModelFactory implements IResourceModelFactory {

	public IResourceModel createResourceModelInstance(CapabilityDescriptor descriptor) throws ModelException {
		
		if (descriptor.getPropertyValue("os") ==null){
			throw new ModelException("The operating system Identifier property" +
			  		" is required to create an Host model");
		};
			
		if (descriptor.getPropertyValue("address") ==null){
			throw new ModelException("The operating system Identifier property" +
				  " is required to create an Host model");
		};
		
		VmHostModel hostvm = new VmHostModel();
		hostvm.setOs(descriptor.getPropertyValue("os"));
		hostvm.setAddress(descriptor.getPropertyValue("address"));
		hostvm.setLocation(descriptor.getPropertyValue("location"));
//		hostvm.setCpu(descriptor.getPropertyValue("cpu"));
//		hostvm.setFreeMemory(descriptor.getPropertyValue("freeMemory"));
//		hostvm.setTotalMemory(descriptor.getPropertyValue("totalMemory"));
//		hostvm.setSpeed(descriptor.getPropertyValue("speed"));
	
		return hostvm;
	}

}
