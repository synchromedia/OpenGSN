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

import com.greenstarnetwork.services.cloudmanager.model.VmHostModel;


/**
 * This class will provide data structure for storing internal used host informations. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */
public class HostExtraInfo implements Comparable<HostExtraInfo>
{
	private VmHostModel hostModel;
	
	private int reservedMemory = 0;
	private int reservedCore = 0;
	
	public HostExtraInfo() {
		hostModel = null;
		
	}
	
	public HostExtraInfo(VmHostModel host) {
		this.hostModel = host;
	}

	
	
	public VmHostModel getHostModel() {
		return this.hostModel;
	}
	
	public void setHostModel (VmHostModel hostModel) {
		this.hostModel = hostModel;
	}

	/**
	 * @param reservedCore the reservedCore to set
	 */
	public void setReservedCore(int reservedCore) {
		this.reservedCore = reservedCore;
	}

	/**
	 * @return the reservedCore
	 */
	public int getReservedCore() {
		return reservedCore;
	}

	/**
	 * @param reservedMemory the reservedMemory to set
	 */
	public void setReservedMemory(int reservedMemory) {
		this.reservedMemory = reservedMemory;
	}

	/**
	 * @return the reservedMemory
	 */
	public int getReservedMemory() {
		return reservedMemory;
	}

	/**
	 * 
	 */
	public int compareTo(HostExtraInfo h) {
		return 0;
	}

	@Override
	public String toString(){
		return "Host ("+hostModel.getAddress()+")";
		
	}
}