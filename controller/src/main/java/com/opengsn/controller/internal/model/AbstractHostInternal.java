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
package com.opengsn.controller.internal.model;

/**
 * This class will provide an abstract internal data structure for storing Host data.
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import com.opengsn.controller.model.Host;

public abstract class AbstractHostInternal implements Comparable{

	private Host hostRef;
	private HostExtraInfo hostInternalInfoRef;
	private int core;
	private int memory;

	public AbstractHostInternal(Host hostRef, HostExtraInfo hostInternalInfoRef, int core, int memory){
		this.setHostRef(hostRef);
		this.setHostInternalInfoRef(hostInternalInfoRef);
		this.setCore(core);
		this.setMemory(memory);
	}

	abstract public int compareTo(Object obj);

	/**
	 * @param core the core to set
	 */
	public void setCore(int core) {
		this.core = core;
	}

	/**
	 * @return the core
	 */
	public int getCore() {
		return core;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(int memory) {
		this.memory = memory;
	}

	/**
	 * @return the memory
	 */
	public int getMemory() {
		return memory;
	}

	/**
	 * @param hostRef the hostRef to set
	 */
	public void setHostRef(Host hostRef) {
		this.hostRef = hostRef;
	}

	/**
	 * @return the hostRef
	 */
	public Host getHostRef() {
		return hostRef;
	}

	/**
	 * @param hostInternalInfoRef the hostInternalInfoRef to set
	 */
	public void setHostInternalInfoRef(HostExtraInfo hostInternalInfoRef) {
		this.hostInternalInfoRef = hostInternalInfoRef;
	}

	/**
	 * @return the hostInternalInfoRef
	 */
	public HostExtraInfo getHostInternalInfoRef() {
		return hostInternalInfoRef;
	}

}
