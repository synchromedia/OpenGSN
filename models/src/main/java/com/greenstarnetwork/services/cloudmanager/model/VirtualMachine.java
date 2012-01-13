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
package com.greenstarnetwork.services.cloudmanager.model;

public class VirtualMachine {
	
	private String name;
	private int memory;
	private int cpuNumber;
	private String ip;
	
	public void setCpuNumber(int cpuNumber) {
		this.cpuNumber = cpuNumber;
	}
	
	public int getCpuNumber() {
		return cpuNumber;
	}
	
	public void setMemory(int memory) {
		this.memory = memory;
	}
	
	public int getMemory() {
		return memory;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

}
