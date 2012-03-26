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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.opengsn.services.utils.ObjectSerializer;

/**
 * 
 * PDU config model
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class PDU implements Serializable {

	private static final long serialVersionUID = 3685700078966462589L;

	/**
	 * resource id of the PDU resource
	 */
	
	private String aliase = null;
	
	/**
	 * list of sinks
	 */
	
	private List<Sink>sink = null;
	
	/**
	 * list of sources
	 */
	
	private List<Source>source = null;

	/**
	 * 
	 */
	public PDU(){
		setSink(new ArrayList<Sink>());
		setSource(new ArrayList<Source>());
	}



	public void setAliase(String aliase) {
		this.aliase = aliase;
	}

	public String getAliase() {
		return aliase;
	}
	
	/**
	 * return the model in XML format
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}



	public void setSink(List<Sink> sink) {
		this.sink = sink;
	}



	public List<Sink> getSink() {
		return sink;
	}



	public void setSource(List<Source> source) {
		this.source = source;
	}



	public List<Source> getSource() {
		return source;
	}


}
