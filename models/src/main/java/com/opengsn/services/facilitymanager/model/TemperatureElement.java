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

//@Entity
@XmlRootElement
public class TemperatureElement  implements Serializable{
	
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@Id
	private String ID = null;
	
	//@Basic
	private String name = null;
	//@Basic
	private String temperature;
	private String CurrentState;
	
	public String getCurrentState() {
		return CurrentState;
	}
	public void setCurrentState(String currentState) {
		CurrentState = currentState;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	
}
