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
 * This class will provide an abstract internal data structure for storing VM data. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import com.greenstarnetwork.controller.model.Host;
import com.greenstarnetwork.controller.model.VirtualMachine;

public abstract class AbstractVMInternal implements Comparable{

	private VirtualMachine virtualMachineRef;
	private int core;
	private int memory;
	private Host destinatinHost;
	private Host sourceHost;

	public AbstractVMInternal(VirtualMachine virtualMachineRef, int core, int memory, Host sourceHost){
		this.setVirtualMachineRef(virtualMachineRef);
		this.setCore(core);
		this.setMemory(memory);
		this.setDestinatinHost(null);
		this.setSourceHost(sourceHost);
	}

	abstract public int compareTo(Object obj);

	/**
	 * @param destinatinHost the destinatinHost to set
	 */
	public void setDestinatinHost(Host destinatinHost) {
		this.destinatinHost = destinatinHost;
	}

	/**
	 * @return the destinatinHost
	 */
	public Host getDestinatinHost() {
		return destinatinHost;
	}

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
	 * @param sourceHost the sourceHost to set
	 */
	public void setSourceHost(Host sourceHost) {
		this.sourceHost = sourceHost;
	}

	/**
	 * @return the sourceHost
	 */
	public Host getSourceHost() {
		return sourceHost;
	}

	/**
	 * @param virtualMachineRef the virtualMachineRef to set
	 */
	public void setVirtualMachineRef(VirtualMachine virtualMachineRef) {
		this.virtualMachineRef = virtualMachineRef;
	}

	/**
	 * @return the virtualMachineRef
	 */
	public VirtualMachine getVirtualMachineRef() {
		return virtualMachineRef;
	}

}
