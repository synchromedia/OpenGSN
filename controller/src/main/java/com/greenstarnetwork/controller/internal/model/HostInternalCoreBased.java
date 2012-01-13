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
package com.greenstarnetwork.controller.internal.model;

/**
 * This class will provide an internal data structure for storing Host data. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import com.greenstarnetwork.controller.model.Host;

public class HostInternalCoreBased extends AbstractHostInternal {


	public HostInternalCoreBased(Host host, HostExtraInfo hostInternalInfoRef, int coreNumber, int memorySizeMB) {
		super(host, hostInternalInfoRef,coreNumber, memorySizeMB);
	}
	
	/**
	 * Sort based on core number.
	 */
	public int compareTo(Object obj) {
		HostInternalCoreBased host = (HostInternalCoreBased)obj;
		if (this.getCore()<host.getCore())
			return -1;
		else if (this.getCore()>host.getCore())
			return 1;
		else return 0;

	}

}
