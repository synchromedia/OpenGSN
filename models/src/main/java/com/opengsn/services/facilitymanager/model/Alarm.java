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
import java.util.Calendar;

public class Alarm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7787331676555029594L;
	private String description= null;
	private String id= null;
	private Calendar time= null;
	//private String type= null;
	//private String value= null;
	private String alarm= null;
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setTime(Calendar time) {
		this.time = time;
	}
	public Calendar getTime() {
		return time;
	}
	/*public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}*/
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	public String getAlarm() {
		return alarm;
	}

}
