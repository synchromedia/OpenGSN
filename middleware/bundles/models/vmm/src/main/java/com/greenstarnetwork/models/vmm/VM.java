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

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;


/**
 * this class represent the Virtual Machine
 * 
 * @author abdelhamid (adaouadji@synchromedia.ca)
 */
@Entity
public class VM implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Basic
	private String name = null;
	@Basic
	private String storage = null;
	@Basic
	private String network = null;
	@Basic
	private VMStatus status;
	@Basic
	private String memory = null;
	@Basic
	private String vcpu = null;
	@Basic
	private String os = null;
	@Basic
	private String location = null;
	@Basic
	private String ip = null;					//IP address
	@Basic
	private String mac = null;					//MAC address
	@Basic
	private String id = null;
	@Basic
	private String uuid = null;
	@Basic
	private String currentMemory = null;
	@Basic
	private String graphics = null;
	@Basic
	private String clock = null;
	@Basic
	private String emulator = null;
	@Basic
	private String template = null;				//name template file
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}

	public String getGraphics() {
		return graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
	}

	public String getEmulator() {
		return emulator;
	}

	public void setEmulator(String emulator) {
		this.emulator = emulator;
	}

	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public VMStatus getStatus() {
		return status;
	}

	public void setStatus(VMStatus status) {
		this.status = status;
	}

	public void setStatus(String status) {
		if (status.compareTo("STARTED") == 0)
			setStatus(VMStatus.STARTED);
		else if (status.compareTo("CREATING") == 0)
			setStatus(VMStatus.CREATING);
		else if (status.compareTo("PAUSED") == 0)
			setStatus(VMStatus.PAUSED);
		else if (status.compareTo("STOPPED") == 0)
			setStatus(VMStatus.STOPPED);
		else if (status.compareTo("STOPPING") == 0)
			setStatus(VMStatus.STOPPING);
		else if (status.compareTo("SHUTDOWN") == 0)
			setStatus(VMStatus.SHUTDOWN);
		else if (status.compareTo("MIGRATING") == 0)
			setStatus(VMStatus.MIGRATING);
		else
			setStatus(VMStatus.UNKNOWN);
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}


	public String getVcpu() {
		return vcpu;
	}

	public void setVcpu(String vcpu) {
		this.vcpu = vcpu;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCurrentMemory() {
		return currentMemory;
	}

	public void setCurrentMemory(String currentMemory) {
		this.currentMemory = currentMemory;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
}
