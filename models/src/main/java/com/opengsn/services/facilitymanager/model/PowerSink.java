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
 * maintain information of a power consumer
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class PowerSink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3785700078966462589L;
	
	

	/**
	 * the port which is connected to a power sink resource and maintain its id
	 */
	private Port port = null;

	
	/**
	 * to maintain the power sink model locally
	 */
	//private VmHostModel vmHostModel = null;

	/**
	 * 
	 */
	public PowerSink(){

	}

	/**
	 * 
	 * @param connectedResourceID
	 */
	public PowerSink(String connectedResourceID) {
		port = new Port(connectedResourceID);
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
	 * @param port
	 */
	public void setPort(Port port) {
		this.port = port;
	}

	/**
	 * 
	 * @return
	 */
	public Port getPort() {
		return port;
	}

	/**
	 * 
	 * @param vmHostModel
	 */
	/*public void setVmHostModel(VmHostModel vmHostModel) {
		this.vmHostModel = vmHostModel;
	}*/

	/**
	 * 
	 * @return
	 */
	/*public VmHostModel getVmHostModel() {
		return vmHostModel;
	}*/


	
}
