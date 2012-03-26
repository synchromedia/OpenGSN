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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.controller.ControllerException;
import com.opengsn.services.utils.ObjectSerializer;

/**
 * A HostList contains a set of Hosts.
 * @author knguyen
 *
 */
@XmlRootElement (name = "HostList") 
public class HostList {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Host> hosts = null;
	
	public HostList() {
	}
	
	public List<Host> getHosts() {
		return this.hosts;
	}
	
	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}
	
	public void sort() {
		java.util.Collections.sort(this.hosts);
	}
	
	public void addHost(Host h) {
		if (this.hosts == null)
			this.hosts = new ArrayList<Host>();
		this.hosts.add(h);
	}

	public Host getHost(String address) {
		if (this.hosts == null)
			return null;
		else {
			Iterator<Host> it = this.hosts.iterator();
			while (it.hasNext()) {
				Host h = (Host)it.next();
				if (h.getHostModel().getAddress().compareTo(address) == 0)
					return h;
			}
		}
		return null;
	}
	
	public void removeHost(String address) throws ControllerException {
		if (this.hosts == null)
			throw new ControllerException("ControllerException: Host: " + address + " not found.");
		
		Iterator<Host> it = this.hosts.iterator();
		while (it.hasNext()) {
			Host h = (Host)it.next();
			if (h.getHostModel().getAddress().compareTo(address) == 0) {
				it.remove();
				return;
			}
		}
		throw new ControllerException("ControllerException: Host: " + address + " not found.");
	}

	public void removeAll() {
		if (this.hosts != null)
			this.hosts.clear();
	}
	
	/**
	 * Append a list to the end. 
	 * @param anotherlist
	 */
	public void mergeHostList(HostList anotherlist) {
		if (this.hosts == null)
			this.hosts = new ArrayList<Host>();
		this.hosts.addAll(anotherlist.getHosts());
	}

	public String toXML() {
		return ObjectSerializer.toXml(this);
	}
	
	public static HostList fromXML(String xml) {
		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(HostList.class);
			Object obj = context.createUnmarshaller().unmarshal(in);
			return (HostList)obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}
}
