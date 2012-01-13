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
package com.greenstarnetwork.services.controller.internal.model;

/**
 * This class will provide an internal data structure for storing VM data. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import com.greenstarnetwork.services.controller.model.Host;
import com.greenstarnetwork.services.controller.model.VirtualMachine;

public class VMInternalCoreBased extends AbstractVMInternal {


	public VMInternalCoreBased(VirtualMachine vm, int coreNumber, int memorySizeMB, Host sourceHost) {
		super(vm, coreNumber, memorySizeMB, sourceHost);
	}

	/**
	 * Sort based on core number.
	 */
	public int compareTo(Object obj) {
		VMInternalCoreBased vm = (VMInternalCoreBased)obj;
		if (this.getCore()<vm.getCore())
			return -1;
		else if (this.getCore()>vm.getCore())
			return 1;
		else return 0;

	}

}
