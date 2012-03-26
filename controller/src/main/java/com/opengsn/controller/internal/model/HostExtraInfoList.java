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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.controller.ControllerException;
import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;
import com.opengsn.services.utils.ObjectSerializer;

/**
 * This class will provide data structure for storing internal used host list informations. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

@XmlRootElement (name = "HostInternalInfoList") 
public class HostExtraInfoList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<HostExtraInfo> hosts = null;

	public HostExtraInfoList() {
	}

	public List<HostExtraInfo> getHosts() {
		return this.hosts;
	}

	public void setHosts(List<HostExtraInfo> hosts) {
		this.hosts = hosts;
	}

	public void sort() {
		java.util.Collections.sort(this.hosts);
	}

	public void addHost(HostExtraInfo h) {
		if (this.hosts == null)
			this.hosts = new ArrayList<HostExtraInfo>();
		this.hosts.add(h);
	}

	public HostExtraInfo getHost(String address) {
		if (this.hosts == null)
			return null;
		else {
			Iterator<HostExtraInfo> it = this.hosts.iterator();
			while (it.hasNext()) {
				HostExtraInfo h = (HostExtraInfo)it.next();
				if (h.getHostModel().getAddress().compareTo(address) == 0)
					return h;
			}
		}
		return null;
	}

	public void removeHost(String address) throws ControllerException {
		if (this.hosts == null)
			throw new ControllerException("ControllerException: Host: " + address + " not found.");

		Iterator<HostExtraInfo> it = this.hosts.iterator();
		while (it.hasNext()) {
			HostExtraInfo h = (HostExtraInfo)it.next();
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
	public void mergeHostList(HostExtraInfoList anotherlist) {
		if (this.hosts == null)
			this.hosts = new ArrayList<HostExtraInfo>();
		this.hosts.addAll(anotherlist.getHosts());
	}

	public String toXML() {
		return ObjectSerializer.toXml(this);
	}

	public void syncWith(HostList energySufficientHostsList) {
		if (energySufficientHostsList!=null && energySufficientHostsList.getHosts()!=null && energySufficientHostsList.getHosts().iterator()!=null){
			removeAll();
			Iterator<Host> iterator = energySufficientHostsList.getHosts().iterator();
			while (iterator.hasNext()){
				Host host = iterator.next();
				HostExtraInfo hostExtraInfo = new HostExtraInfo();
				hostExtraInfo.setHostModel(host.getHostModel());
				addHost(hostExtraInfo);
			}
		}
	}

}
