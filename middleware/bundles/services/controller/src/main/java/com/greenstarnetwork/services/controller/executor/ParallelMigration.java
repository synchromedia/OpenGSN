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
package com.greenstarnetwork.services.controller.executor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.services.controller.model.Link;
import com.greenstarnetwork.services.controller.model.VirtualMachine;

/**
 * Information Container of a Parallel migration 
 * @author knguyen
 *
 */
@XmlRootElement (name = "Migration")
@Entity
public class ParallelMigration {

	List<VirtualMachine> vms = null;
	Link link = null;
	
	public ParallelMigration() {
	}
	
	public void setLink(Link l) {
		this.link = l;
	}
	
	public Link getLink() {
		return this.link;
	}
	
	/**
	 * Add new virtual machine in order to make a parallel migration
	 * @param v
	 * @return
	 */
	public boolean addParallelVM(VirtualMachine v) 
	{
		if (link.getBandwidth() < new Double(v.getVM().getMemory()).doubleValue())
			return false;
		if (vms == null) {
			vms = new ArrayList<VirtualMachine>();
		}
		vms.add(v);
		link.setBandwidth(link.getBandwidth() - new Double(v.getVM().getMemory()).doubleValue());
		return true;
	}

	public void addSingleVM(VirtualMachine v) {
		if (vms == null) {
			vms = new ArrayList<VirtualMachine>();
		}
		vms.add(v);
		double bw = link.getBandwidth() - new Double(v.getVM().getMemory()).doubleValue();
		if (bw > 0)
			link.setBandwidth(bw);
		else
			link.setBandwidth(0);
	}
	
	public List<VirtualMachine> getVMs() {
		return this.vms;
	}
}
