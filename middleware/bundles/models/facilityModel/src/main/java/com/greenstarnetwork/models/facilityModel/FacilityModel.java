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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.common.Alarm;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.ObjectSerializer;

/**
 * facilityModel main class maintaining information about the whole node
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

@XmlRootElement
public class FacilityModel implements IResourceModel, Serializable {

	private static final long serialVersionUID = 8785712378966462589L;


	/**
	 * list of PDUs and their data models
	 */
	@XmlElement(name = "powerDistributionList")
	private List<PowerDistribution> powerDistributionList = null;
	/**
	 * location of site
	 */
	private Location location = null;
	/**
	 * operational data
	 */
	private OperationalSpecs operationalSpecs = null;
	/**
	 * climate data model
	 */
	private Climate climate = null;
	/**
	 * resourceID to access climate resource
	 */
	private String climateResourceID = null;
	/**
	 * domainID to access domain
	 */
	
	private String climateDiscovery = null;
	
	private String domainID = null;
	
	private Boolean initialized = false;
	
	private List<Alarm> alarms = null;
	
	private String alarmSendTo = null;

	/**
	 * constructor function
	 */
	public FacilityModel(){
		powerDistributionList = new ArrayList<PowerDistribution>();
	}




	/**
	 * add a new PDU to facility
	 * @param powerDistribution
	 * @return
	 */
	public boolean addPowerDistribution(PowerDistribution powerDistribution){
		boolean add = true;
		if (powerDistributionList.size()==0){
			powerDistributionList.add(powerDistribution);
			return add;
		}else
			for (int i=0; i < powerDistributionList.size(); i++) {
				if (powerDistribution.getResourceID().equals(powerDistributionList.get(i).getResourceID())){
					return add=false;
				}
			}
		powerDistributionList.add(powerDistribution);	
		return add;
	}

	/**
	 * return a PDU from list
	 * @param pduResourceID
	 * @return
	 * @throws Exception 
	 */
	public PowerDistribution getPowerDistribution(String pduResourceID) throws Exception{
		PowerDistribution powerDistribution = null;
		for (int i=0; i < powerDistributionList.size(); i++) {
			if (pduResourceID.equals(powerDistributionList.get(i).getResourceID()))
				powerDistribution = (PowerDistribution) powerDistributionList.get(i);
		}
		if (powerDistribution==null){
			throw new Exception(this.getClass().getName()+" no PDU with this ID:"+pduResourceID);
		}
		return powerDistribution;
	}

	public List<PowerDistribution> getPowerDistributionList() {
		return this.powerDistributionList;
	}

	/**
	 * remove a PDU from facility
	 * @param pduResourceID
	 * @return
	 */
	public boolean removePowerDistribution(String pduResourceID){
		boolean remove = false;
		for (int i=0; i < powerDistributionList.size(); i++) {
			if (pduResourceID.equals(powerDistributionList.get(i).getResourceID()))
				powerDistributionList.remove(i);
			remove=true;
		}
		return remove;
	}

	/**
	 * remove all PDUs
	 */
	public void removePowerDistributionList(){
		powerDistributionList.clear();
	}

	/**
	 * 
	 * @return
	 */
	public IResourceModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * return the model in XML format
	 */
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	/**
	 * set the location
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * return the location instance 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * set the operational specifications instance
	 * @param operationalSpecs
	 */
	public void setOperationalSpecs(OperationalSpecs operationalSpecs) {
		this.operationalSpecs = operationalSpecs;
	}

	/**
	 * return then operational specifications instance
	 * @return
	 */
	public OperationalSpecs getOperationalSpecs() {
		if (operationalSpecs==null){
			operationalSpecs = new OperationalSpecs();
		}
		return operationalSpecs;
	}


	/**
	 * set the climate data model 
	 * @param climate
	 */
	public void setClimate(Climate climate) {
		this.climate = climate;
	}

	/**
	 * return the climate data model
	 * @return
	 */
	public Climate getClimate() {
		return climate;
	}



	/**
	 * set the climate resourceID
	 * @param climateResourceID
	 */
	public void setClimateResourceID(String climateResourceID) {
		this.climateResourceID = climateResourceID;
	}



	/**
	 * return the climate resource id
	 * @return
	 */
	public String getClimateResourceID() {
		return climateResourceID;
	}




	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}




	public String getDomainID() {
		return domainID;
	}

	/**
	 * Get a model from a XML string
	 * @param xml
	 * @return
	 */
	public static FacilityModel fromXML(String xml) {
		StringReader in = new StringReader(xml);
		try {
			JAXBContext context = JAXBContext.newInstance(FacilityModel.class);
			Object obj = context.createUnmarshaller().unmarshal(in);
			return (FacilityModel)obj;
		} catch (JAXBException e) {
			e.printStackTrace();		
		}
		return null;
	}




	public void setClimateDiscovery(String climateDiscovery) {
		this.climateDiscovery = climateDiscovery;
	}




	public String getClimateDiscovery() {
		return climateDiscovery;
	}




	public void setInitialized(Boolean initialized) {
		this.initialized = initialized;
	}




	public Boolean getInitialized() {
		return initialized;
	}




	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}




	public List<Alarm> getAlarms() {
		if (alarms==null){
			alarms = new ArrayList<Alarm>();
		}
		return alarms;
	}




	public void setAlarmSendTo(String alarmSendTo) {
		this.alarmSendTo = alarmSendTo;
	}




	public String getAlarmSendTo() {
		return alarmSendTo;
	}
}
