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
package com.opengsn.services.facilitymanager.model.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.services.utils.ObjectSerializer;

/**
 * 
 * maintain sink config model
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class Sink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3785700078966462589L;
	
	

	/**
	 * the port number which is connected to a power sink resource
	 */
	
	private String port = null;
	
	private String aliase = null;

	/**
	 * 
	 */
	public Sink(){

	}
	
	/**
	 * return the model in XML format
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return port;
	}

	public void setAliase(String aliase) {
		this.aliase = aliase;
	}

	public String getAliase() {
		return aliase;
	}

	
	
}
