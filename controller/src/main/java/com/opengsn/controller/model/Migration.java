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
package com.opengsn.controller.model;





/**
 * This class will provide data structure to store a migration 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


public class Migration implements Comparable<Migration>{

	private VirtualMachine virtualMachine;
	private Host destinationHost;
	private Host sourceHost;
	private String state = null;
	
	public Migration() {
		state = "Migrating";
	}
	
	/**
     * @param migration
     */
	public int compareTo(Migration migration) {
		return 0;
	}
	
	
	/**
     * @param virtualMachine
     */
	public void setVirtualMachine(VirtualMachine virtualMachine){
		this.virtualMachine = virtualMachine;
	}
	
	/**
     */
	public VirtualMachine getVirtualMachine(){
		return this.virtualMachine;
	}

	/**
     * @param destinationHost
     */
	public void setDestinationHost(Host destinationHost){
		this.destinationHost = destinationHost;
	}
	
	/**
	 * 
	 * @return
	 */
	public Host getDestinationHost(){
		return this.destinationHost;
	}

	/**
	 * 
	 * @return
	 */
	public Host getSourceHost() {
		return this.sourceHost;
	}
	
	public void setSourceHost(Host sourceHost){
		this.sourceHost = sourceHost;
	}


	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}


	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	public String toString() {
		return state + " VM " + virtualMachine.getVM().getName().split("_")[0] + " From " + sourceHost.getResourceName() + 
		" To " + destinationHost.getResourceName();		
	}
	
}
