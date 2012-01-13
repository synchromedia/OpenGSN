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
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @author knguyen
 *
 */
@Entity
public class PDUElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Status {On, Off};

	@Id
	private String ID = null;
	
	@Basic
	private String name = null;
	
	@Basic
	private Status status;
	
	@Basic
	private String load = null;				//Amps
	
	@Basic
	private String voltage = null;			//Volts
	
	@Basic
	private String power = null;			//Watts
	
	@Basic
	private String controlState = null;
	
	private String consumer = null;				//IP address of consumer		
	
	private String type = null;				//element type: Voltage, Current, Power

	public PDUElement() {
	}
	
	public void setID(String id) {
		this.ID = id;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setStatus(Status s){
		this.status = s;
	}
	
	public void setStatusString(String status) throws PDUException {
		if (status.toLowerCase().compareTo("on") == 0)
			this.status = Status.On;
		else if (status.toLowerCase().compareTo("off") == 0)
			this.status = Status.Off;
		else
			throw new PDUException("Invalid status!");
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public String getStatusString() {
		if (this.status == Status.On)
			return "On";
		else if (this.status == Status.Off)
			return "Off";
		else
			return null;
	}
	
	public void setLoad(String load) {
		this.load = load;
	}
	
	public String getLoad() {
		return this.load;
	}
	
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	
	public String getVoltage() {
		return this.voltage;
	}
	
	public void setPower(String power) {
		this.power = power;
	}
	
	public String getPower() {
		return this.power;
	}
	
	public void setControlState(String s) {
		this.controlState = s;
	}
	
	public String getControlState() {
		return this.controlState;
	}
	
	public String getConsumer() {
		return this.consumer;
	}
	
	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}