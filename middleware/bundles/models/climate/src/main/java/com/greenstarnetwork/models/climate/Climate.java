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
package com.greenstarnetwork.models.climate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.ObjectSerializer;
import com.greenstarnetwork.models.common.Alarm;
/**
 *
 * 
 * @author Abdelhamid Daouadji <synchromedia.ca>
 *
 */
@XmlRootElement
public class Climate implements IResourceModel,Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 21L;
	private List <Alarm> alarms;
	
	private String humidityLowerThreshold;
	private String humidityUpperThreshold;
 
    private String temperatureLowerThreshold;
	private String temperatureUpperThreshold;
	private String Host=null;
	private String Port=null;
//	
//	public Climate(){
//	 humidityLowerThreshold=10.0;
//	 humidityUpperThreshold=90.0;
//	 
//	 temperatureLowerThreshold=14.0;
//     temperatureUpperThreshold=35.0;	
//		
//		
//	}

	private List<TemperatureElement> temperatureelement = null;

	private List<HumidityElement> humidityelement = null;
	
	public List<HumidityElement> getHumidityelement() {
		return humidityelement;
	}

	public void setHumidityelement(List<HumidityElement> humidityelement) {
		this.humidityelement = humidityelement;
	}

	public void setTemperatureelement(List<TemperatureElement> temperatureelement) {
		this.temperatureelement = temperatureelement;
	}

	public List<TemperatureElement> getTemperatureelement() {
		return temperatureelement;
	}
	
	public void addclimate(HumidityElement climate) {
		if (humidityelement == null)
			humidityelement = new ArrayList<HumidityElement>();
		humidityelement.add(climate);
	}
	
	
	/**
	 * Get an temperature element from ID
	 * @param id
	 * @return
	 */
	public HumidityElement getHumidityelement(String id)
	{
		if (humidityelement == null)
			return null;
		
		for (int pi=0; pi<humidityelement.size(); pi++)
		{
			HumidityElement elem = humidityelement.get(pi);
			if (elem.getID().compareTo(id) == 0)
			{
				return elem;
			}
		}
		return null;
	}

	
	
	/**
	 * Get an temperature element from ID
	 * @param id
	 * @return
	 */
	public TemperatureElement getTemperatureelement(String id)
	{
		if (temperatureelement == null)
			return null;
		
		for (int pi=0; pi<temperatureelement.size(); pi++)
		{
			TemperatureElement elem = temperatureelement.get(pi);
			if (elem.getID().compareTo(id) == 0)
			{
				return elem;
			}
		}
		return null;
	}
	public void setTemperaturelement(List<TemperatureElement> temperatureelement) {
		this.temperatureelement = temperatureelement;
	}
	
	
	
	public String getHost() {
		return Host;
	}
	public void setHost(String host) {
		this.Host = host;
	}
	public String getPort() {
		return Port;
	}
	public void setPort(String port) {
		this.Port = port;
	}

	
	public List<Alarm> getAlarms() {
		return alarms;
	}
	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}


	public String getHumidityLowerThreshold() {
		return humidityLowerThreshold;
	}
	public void setHumidityLowerThreshold(String humidityLowerThreshold) {
		this.humidityLowerThreshold =humidityLowerThreshold;
	}
	public String getHumidityUpperThreshold() {
		return humidityUpperThreshold;
	}
	public void setHumidityUpperThreshold(String humidityUpperThreshold) {
		this.humidityUpperThreshold = humidityUpperThreshold;
	}

	public String getTemperatureLowerThreshold() {
		return temperatureLowerThreshold;
	}
	public void setTemperatureLowerThreshold(String  temperatureLowerThreshold) {
		this.temperatureLowerThreshold =temperatureLowerThreshold; 
	}
	public String getTemperatureUpperThreshold() {
		return temperatureUpperThreshold;
	}
	public void setTemperatureUpperThreshold(String  temperatureUpperThreshold) {
		this.temperatureUpperThreshold =temperatureUpperThreshold; 
	}
	@Override
	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	public void addclimate(TemperatureElement climate) {
		if (temperatureelement == null)
			temperatureelement = new ArrayList<TemperatureElement>();
		   temperatureelement.add(climate);
	}

	public void addclimate(Alarm alarme) {
		if (alarms == null)
			alarms = new ArrayList<Alarm>();
		    alarms.add(alarme);
	}
	
	/**
	 *remove temperature element
	 * @param id
	 * 
	 */
	public void removeTemp(String id)
	{
		if (temperatureelement == null)
			return;
		
		for (int pi=0; pi<temperatureelement.size(); pi++)
		{
			TemperatureElement elem = temperatureelement.get(pi);
			if (elem.getID().compareTo(id) == 0)
			{
				temperatureelement.remove(elem);
			}
		}
	
	}
	
	
	/**
	 *remove temperature element
	 * @param id
	 * @return
	 */
	public void removeHumi(String id)
	{
		if (humidityelement == null)
			return;
		
		for (int pi=0; pi<humidityelement.size(); pi++)
		{
			HumidityElement elem = humidityelement.get(pi);
			if (elem.getID().compareTo(id) == 0)
			{
				humidityelement.remove(elem);
			}
		}
	
	}
}
