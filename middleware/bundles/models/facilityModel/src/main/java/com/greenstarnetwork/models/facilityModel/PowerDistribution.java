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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.iaasframework.resources.core.ObjectSerializer;

/**
 * 
 * maintain PDU information in a facility
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

@XmlRootElement
public class PowerDistribution implements Serializable {

	private static final long serialVersionUID = 3685700078966462589L;

	/**
	 * to maintain the pdu model locally
	 */
	//private PDUModel pduModel = null; 
	
	/**
	 * resource id of the PDU resource
	 */
	private String resourceID = null;
	

	
	/**
	 * list of power consumers connected to this pdu
	 */
	@XmlElement(name = "powerSinkList")
	private List<PowerSink>powerSinkList = null;
	
	/**
	 * list of power generators connected to this pdu
	 */
	@XmlElement(name = "powerSourceList")
	private List<PowerSource>powerSourceList = null;

	/**
	 * 
	 */
	public PowerDistribution(){
		powerSinkList = new ArrayList<PowerSink>();
		powerSourceList = new ArrayList<PowerSource>();
	}

	/**
	 * 
	 * @param string
	 */
	public PowerDistribution(String string) {
		powerSinkList = new ArrayList<PowerSink>();
		powerSourceList = new ArrayList<PowerSource>();
		resourceID = string;
	}

	
	
	
	
	/**
	 * 
	 * @param powerSink
	 * @return
	 */
	public boolean addPowerSink(PowerSink powerSink){
		boolean add = true;
		if (powerSinkList.size()==0){
			powerSinkList.add(powerSink);
			return add;
		}else
			for (int i=0; i < powerSinkList.size(); i++) {
				if (powerSink.getPort().getConnectedResourceID().equals(powerSinkList.get(i).getPort().getConnectedResourceID())){
					return add=false;
				}
			}
		powerSinkList.add(powerSink);	
		return add;
	}

	/**
	 * 
	 * @param connectedResourceID
	 * @return
	 */
	public PowerSink getPowerSink(String connectedResourceID){
		PowerSink powerSink = null;
		for (int i=0; i < powerSinkList.size(); i++) {
			if (connectedResourceID.equals(powerSinkList.get(i).getPort().getConnectedResourceID()))
				powerSink = (PowerSink) powerSinkList.get(i);
		}
		return powerSink;
	}

	/**
	 * 
	 * @return
	 */
	public List<PowerSink> getPowerSinkList() {
		return this.powerSinkList;
	}

	/**
	 * 
	 * @param connectedResourceID
	 * @return
	 */
	public boolean removePowerSink(String connectedResourceID){
		boolean remove = false;
		for (int i=0; i < powerSinkList.size(); i++) {
			if (connectedResourceID.equals(powerSinkList.get(i).getPort().getConnectedResourceID()))
				powerSinkList.remove(i);
			remove=true;
		}
		return remove;
	}

	/**
	 * 
	 */
	public void removePowerSinkList(){
		powerSinkList.clear();
	}

	/**
	 * 
	 * @param powerSource
	 * @return
	 */
	public boolean addPowerSource(PowerSource powerSource){
		boolean add = true;
		if (powerSourceList.size()==0){
			powerSourceList.add(powerSource);
			return add;
		}else
			for (int i=0; i < powerSourceList.size(); i++) {
				if (powerSource.getPort().getConnectedResourceID().equals(powerSourceList.get(i).getPort().getConnectedResourceID())){
					return add=false;
				}
			}
		powerSourceList.add(powerSource);	
		return add;
	}

	/**
	 * 
	 * @param connectedResourceID
	 * @return
	 */
	public PowerSource getPowerSource(String connectedResourceID){
		PowerSource powerSource = null;
		for (int i=0; i < powerSourceList.size(); i++) {
			if (connectedResourceID.equals(powerSourceList.get(i).getPort().getConnectedResourceID()))
				powerSource = (PowerSource) powerSourceList.get(i);
		}
		return powerSource;
	}

	/**
	 * 
	 * @return
	 */
	public List<PowerSource> getPowerSourceList() {
		return this.powerSourceList;
	}

	/**
	 * 
	 * @param connectedResourceID
	 * @return
	 */
	public boolean removePowerSource(String connectedResourceID){
		boolean remove = false;
		for (int i=0; i < powerSourceList.size(); i++) {
			if (connectedResourceID.equals(powerSourceList.get(i).getPort().getConnectedResourceID()))
				powerSourceList.remove(i);
			remove=true;
		}
		return remove;
	}

	/**
	 * 
	 */
	public void removePowerSourceList(){
		powerSourceList.clear();
	}

	/**
	 * 
	 * @return
	 */
	/*public IResourceModel getModel() {
		
		return pduModel;
	}*/
	
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
	 * @param resourceID
	 */
	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	/**
	 * 
	 * @return
	 */
	public String getResourceID() {
		return resourceID;
	}

	


	

	/*public void setPduModel(PDUModel pduModel) {
		this.pduModel = pduModel;
	}

	public PDUModel getPduModel() {
		return pduModel;
	}*/

}
