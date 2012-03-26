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
package com.opengsn.services.networkmanager.model;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.services.utils.ObjectSerializer;

/**
 * Data structure containing a binding IP/MAC addresses and a VM
 * 
 * @author knguyen
 *
 */
@XmlRootElement
public class VMNetAddress 
{
	private String name = null;						//name of the VM
	private String ip = null;						//IP address
	private String mac = null;						//MAC address 
	private boolean assigned = false;				//IP address has been assigned to VM
	
	public VMNetAddress() {
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public boolean isAssigned() {
		return assigned;
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
	 * Serialize object to a XML string
	 * @return
	 */
	public String toXML() {
		return ObjectSerializer.toXml(this);
	}
	
	/**
	 * Create an object from the XML string
	 * @param xml
	 * @return
	 */
	public static VMNetAddress fromXML(String xml) {
		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(VMNetAddress.class);
			Object obj = context.createUnmarshaller().unmarshal(in);
			return (VMNetAddress)obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}
}
