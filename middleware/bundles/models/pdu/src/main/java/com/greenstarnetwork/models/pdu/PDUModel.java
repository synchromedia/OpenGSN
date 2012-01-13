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
package com.greenstarnetwork.models.pdu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.ObjectSerializer;

/**
 * PDUModel implementation
 * A model contains PDU identifications (IP, port, location), login information (username, password),
 * a list of infeeds and a list of outlets
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
@XmlRootElement
public class PDUModel implements IResourceModel, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8571009012048021984L;

	//Geographical location
	private String location = null;
	
	//Host address
	private String host = null;
	
	//Command port
	private String port = null;
	
	//PDU current
	private String current = null;
	
	//PDU voltage
	private String voltage = null;
	
	//PDU total power
	private String totalPower = null;
	
	//InFeeds
	private List<PDUElement> infeeds = null;
	
	//Outlets
	private List<PDUElement> outlets = null;
	
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return this.port;
	}
	
	public void setPort(String p) throws PDUException{
		try {
			int i = Integer.parseInt(p);
			this.port = new Integer(i).toString();
		}catch (NumberFormatException e) {
			throw new PDUException("Port number invalid!");
		}
	}
	
	public String getCurrent() {
		return this.current;
	}
	
	public void setCurrent(String c) {
		this.current = c;
	}
	
	public String getVoltage() {
		return this.voltage;
	}
	
	public void setVoltage(String v) {
		this.voltage = v;
	}
	
	public void setInFeeds(List<PDUElement> l) {
		this.infeeds = l;
	}
	
	public List<PDUElement> getInFeeds() {
		return this.infeeds;
	}

	public void setOutlets(List<PDUElement> l) {
		this.outlets = l;
	}
	
	public List<PDUElement> getOutlets() {
		return this.outlets;
	}

	public IResourceModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Get an outlet from ID
	 * @param id
	 * @return
	 */
	public PDUElement getOutlet(String id)
	{
		if (outlets == null)
			return null;
		
		for (int pi=0; pi<outlets.size(); pi++)
		{
			PDUElement elem = outlets.get(pi);
			if (elem.getID().compareTo(id) == 0)
			{
				return elem;
			}
		}
		return null;
	}

	/**
	 * Get an infeed from ID
	 * @param id
	 * @return
	 */
	public PDUElement getInFeed(String id)
	{
		if (infeeds == null)
			return null;
		
		for (int pi=0; pi<infeeds.size(); pi++)
		{
			PDUElement elem = infeeds.get(pi); 
			if (elem.getID().compareTo(id) == 0)
			{
				return elem;
			}
		}
		return null;
	}

	/**
	 * Add an outlet
	 * @param outlet
	 */
	public void addOutlet(PDUElement outlet) {
		if (outlets == null)
			outlets = new ArrayList<PDUElement>();
		outlets.add(outlet);
	}
	
	/**
	 * Add an infeed
	 * @param infeed
	 */
	public void addInfeed(PDUElement infeed) {
		if (infeeds == null)
			infeeds = new ArrayList<PDUElement>();
		infeeds.add(infeed);
	}

	/**
	 * Remove an outlet
	 * @param id
	 * @return
	 */
	public PDUElement removeOutlet(String id) {
		if (outlets == null)
			return null;
		
		for (int pi=0; pi<outlets.size(); pi++)
		{
			if (outlets.get(pi).getID().compareTo(id) == 0)
			{
				PDUElement elem = outlets.get(pi);
				outlets.remove(pi);
				return elem;
			}
		}
		return null;
	}

	/**
	 * Remove all outlets
	 */
	public void removeOutlets() {
		if (outlets == null)
			return;
		outlets.clear();
	}

	/**
	 * Remove all infeeds
	 */
	public void removeInFeeds() {
		if (infeeds == null)
			return;
		infeeds.clear();
	}
	
	/**
	 * Remove an infeed
	 * @param id
	 * @return
	 */
	public PDUElement removeInFeed(String id) {
		if (infeeds == null)
			return null;
		
		for (int pi=0; pi<infeeds.size(); pi++)
		{
			if (infeeds.get(pi).getID().compareTo(id) == 0)
			{
				PDUElement elem = infeeds.get(pi);
				infeeds.remove(pi);
				return elem;
			}
		}
		return null;
	}
	
	/**
	 * @param totalPower the totalPower to set
	 */
	public void setTotalPower(String totalPower) {
		this.totalPower = totalPower;
	}

	/**
	 * @return the totalPower
	 */
	public String getTotalPower() {
		return totalPower;
	}

	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}
}
