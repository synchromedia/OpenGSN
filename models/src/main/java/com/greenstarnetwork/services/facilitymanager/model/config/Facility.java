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
package com.greenstarnetwork.services.facilitymanager.model.config;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.services.utils.ObjectSerializer;

/**
 * facility config model
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

@XmlRootElement
public class Facility implements Serializable {

	private static final long serialVersionUID = 8785712378966462589L;


	/**
	 * list of PDUs
	 */
	
	private List<PDU> pdu = null;
	
	private String climateAliase = null;

	/**
	 * constructor function
	 */
	public Facility(){
		setPdu(new ArrayList<PDU>());
	}

	/**
	 * return the model in XML format
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}


	/**
	 * Get a model from a XML string
	 * @param xml
	 * @return
	 */
	public static Facility fromXML(String xml) {
		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(Facility.class);
			Object obj = context.createUnmarshaller().unmarshal(in);
			return (Facility)obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}

	public void setClimateAliase(String climateAliase) {
		this.climateAliase = climateAliase;
	}

	public String getClimateAliase() {
		return climateAliase;
	}

	public void setPdu(List<PDU> pdu) {
		this.pdu = pdu;
	}

	public List<PDU> getPdu() {
		return pdu;
	}


}
