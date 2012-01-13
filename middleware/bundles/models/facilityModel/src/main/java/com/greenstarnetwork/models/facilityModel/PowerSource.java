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
package com.greenstarnetwork.models.facilityModel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.iaasframework.resources.core.ObjectSerializer;

/**
 * 
 * maintain information of a power producer in the facility 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class PowerSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3885700078966462589L;

	//private PowerSourceModel powerSourceModel = null;

	//private PowerSourceModel powerSourceModel = null;

	/**
	 * port which is connected to a power source resource and maintain its resource id
	 */
	private Port port = null;

	/**
	 * 
	 */
	public PowerSource(){

	}

	/**
	 * 
	 * @param connectedResourceID
	 */
	public PowerSource(String connectedResourceID) {
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
	 * @param powerSourceModel
	 */
	/*public void setPowerSourceModel(PowerSourceModel powerSourceModel) {
		this.powerSourceModel = powerSourceModel;
	}*/

	/**
	 * 
	 * @return
	 */
	/*public PowerSourceModel getPowerSourceModel() {
		return powerSourceModel;
	}*/

}
