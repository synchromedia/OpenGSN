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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.controller.ControllerException;
import com.opengsn.services.cloudmanager.model.VM;
import com.opengsn.services.utils.ObjectSerializer;

/**
 * A VMList contains a set of VirtualMachines.
 * Each VirtualMachine is identified by its IP address.
 * @author knguyen
 *
 */
@XmlRootElement (name = "VMList") 
public class VMList
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<VirtualMachine> vms = null;
	
	public VMList() {
	}
	
	public List<VirtualMachine> getVMs() {
		return this.vms;
	}
	
	public void setVMs(List<VirtualMachine> vms) {
		this.vms = vms;
	}
	
	public void sort() {
		java.util.Collections.sort(vms);
	}
	
	public void addVM(VirtualMachine v) {
		if (this.vms == null)
			this.vms = new ArrayList<VirtualMachine>();
		this.vms.add(v);
	}

	public VirtualMachine getVM(String ip) {
		if (this.vms == null)
			return null;
		
		Iterator<VirtualMachine> it = this.vms.iterator();
		while (it.hasNext()) {
			VirtualMachine v = (VirtualMachine)it.next();
			if (v.getVM().getIp().compareTo(ip) == 0)
				return v;
		}
		
		return null;
	}
	
	public void removeVM(VM vm) {
	}
	
	public void removeVM(String ip) throws ControllerException {
		if (this.vms == null)
			throw new ControllerException("ControllerException: VM: " + ip + " not found.");

		Iterator<VirtualMachine> it = this.vms.iterator();
		while (it.hasNext()) {
			VirtualMachine v = (VirtualMachine)it.next();
			if (v.getVM().getIp().compareTo(ip) == 0) {
				it.remove();
				return;
			}
		}
		throw new ControllerException("ControllerException: VM: " + ip + " not found.");
	}

	public String toXML() {
		return ObjectSerializer.toXml(this);
	}
}
