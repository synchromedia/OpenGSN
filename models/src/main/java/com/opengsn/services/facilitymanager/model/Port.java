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
package com.opengsn.services.facilitymanager.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.services.utils.ObjectSerializer;


/**
 * 
 * maintain port data for a power sink/source
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class Port implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3585700078966462589L;

	/**
	 * this id will point to the resource which is connected to this port 
	 */
	private String connectedResourceID = null;
	
	
	
	
	/**
	 * PDU port number
	 */
	private int portNumber = 0;
	

	/**
	 * 
	 */
	public Port(){
	}

	/**
	 * 
	 * @param connectedResourceID
	 */
	public Port(String connectedResourceID) {
		this.connectedResourceID = connectedResourceID;
	}

	
	/**
	 * 
	 * @return
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	/**
	 * 
	 * @param connectedResourceID
	 */
	public void setConnectedResourceID(String connectedResourceID) {
		this.connectedResourceID = connectedResourceID;
	}

	/**
	 * 
	 * @return
	 */
	public String getConnectedResourceID() {
		return connectedResourceID;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public int getPortNumber() {
		return portNumber;
	}

	
}
