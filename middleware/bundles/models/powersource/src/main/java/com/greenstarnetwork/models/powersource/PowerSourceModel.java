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
package com.greenstarnetwork.models.powersource;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;


import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.ObjectSerializer;

/**
 * A model contains power facility identifications (IP, port, location) and power information
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
@XmlRootElement
public class PowerSourceModel implements IResourceModel, Serializable 
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
	
	/** Inverter information */
	//Inverter Buy current
	private String invBuyCurrent = null;

	//Inverter AC input voltage
	private String invACInputVoltage = null;
	
	//Inverter AC output voltage
	private String invACOutputVoltage = null;

	//Inverter Sell current
	private String invSellCurrent = null;
	
	//Inverter operational mode
	private String invOperationalMode = null;

	//Inverter error mode
	private String invErrorMode = null;

	//Inverter AC mode
	private String invACMode = null;

	//Inverter battery voltage
	private String invBatteryVoltage = null;
	
	//Inverter AUX output state
	private String invAUXOutputState = null;

	//Inverter warning mode
	private String invWarningMode = null;

	/** Charger information */

	//Charger current
	private String chargerCurrent = null;
	
	//Charger PV current
	private String chargerPVCurrent = null;

	//Charger PV voltage
	private String chargerPVVoltage = null;
	
	//Charger PV voltage
	private String chargerDailyKWH = null;

	//Charger current tenths (FlexMAX 80 and FlexMAX 60 only) 
	//(Add Charger current plus tenths to equal charger current displayed on FM80 or FM60)
	private String chargerCurrentTenths = null;
	
	//Charger AUX mode
	private String chargerAUXMode = null;

	//Charger error mode
	private String chargerErrorMode = null;
	
	//Charger MX Charger mode
	private String chargerMXChargerMode = null;
	
	//Charger battery voltage
	private String chargerBatteryVoltage = null;

	//Charger battery voltage
	private String chargerDailyAH = null;
	
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
	
	public void setPort(String p) throws PowerSourceException{
		try {
			int i = Integer.parseInt(p);
			this.port = new Integer(i).toString();
		}catch (NumberFormatException e) {
			throw new PowerSourceException("Port number invalid!");
		}
	}
	
	public void setInvBuyCurrent(String invBuyCurrent) {
		this.invBuyCurrent = invBuyCurrent;
	}

	public String getInvBuyCurrent() {
		return invBuyCurrent;
	}

	public void setInvACInputVoltage(String aCInputVoltage) {
		invACInputVoltage = aCInputVoltage;
	}

	public String getInvACInputVoltage() {
		return invACInputVoltage;
	}

	public void setInvACOutputVoltage(String aCOutputVoltage) {
		invACOutputVoltage = aCOutputVoltage;
	}

	public void setInvSellCurrent(String invSellCurrent) {
		this.invSellCurrent = invSellCurrent;
	}

	public String getInvSellCurrent() {
		return invSellCurrent;
	}

	public String getInvACOutputVoltage() {
		return invACOutputVoltage;
	}

	public String getInvBatteryVoltage() {
		return this.invBatteryVoltage;
	}
	
	public void setInvBatteryVoltage(String v) {
		this.invBatteryVoltage = v;
	}
	
	public String getInvOperationalMode() {
		return this.invOperationalMode;
	}
	
	public void setInvErrorMode(String invErrorMode) {
		this.invErrorMode = invErrorMode;
	}

	public String getInvErrorMode() {
		return invErrorMode;
	}

	public void setInvOperationalMode(String s) {
		this.invOperationalMode = s;
	}

	public String getInvACMode() {
		return this.invACMode;
	}
	
	public void setInvACMode(String m) {
		this.invACMode = m;
	}
	
	public void setInvAUXOutputState(String invAUXOutputState) {
		this.invAUXOutputState = invAUXOutputState;
	}

	public String getInvAUXOutputState() {
		return invAUXOutputState;
	}

	public void setInvWarningMode(String invWarningMode) {
		this.invWarningMode = invWarningMode;
	}

	public String getInvWarningMode() {
		return invWarningMode;
	}

	public String toXML() {
		String result = ObjectSerializer.toXml(this);
		return result;
	}

	public String getChargerCurrent() {
		return this.chargerCurrent;
	}
	
	public void setChargerCurrent(String c) {
		this.chargerCurrent = c;
	}

	public void setChargerPVCurrent(String chargerPVCurrent) {
		this.chargerPVCurrent = chargerPVCurrent;
	}

	public String getChargerPVCurrent() {
		return chargerPVCurrent;
	}

	public void setChargerPVVoltage(String chargerPVVoltage) {
		this.chargerPVVoltage = chargerPVVoltage;
	}

	public String getChargerPVVoltage() {
		return chargerPVVoltage;
	}

	public void setChargerDailyKWH(String chargerDailyKWH) {
		this.chargerDailyKWH = chargerDailyKWH;
	}

	public String getChargerDailyKWH() {
		return chargerDailyKWH;
	}

	public void setChargerCurrentTenths(String chargerCurrentTenths) {
		this.chargerCurrentTenths = chargerCurrentTenths;
	}

	public String getChargerCurrentTenths() {
		return chargerCurrentTenths;
	}

	public void setChargerAUXMode(String chargerAUXMode) {
		this.chargerAUXMode = chargerAUXMode;
	}

	public String getChargerAUXMode() {
		return chargerAUXMode;
	}

	public void setChargerErrorMode(String chargerErrorMode) {
		this.chargerErrorMode = chargerErrorMode;
	}

	public String getChargerErrorMode() {
		return chargerErrorMode;
	}

	public void setChargerMXChargerMode(String chargerMXChargerMode) {
		this.chargerMXChargerMode = chargerMXChargerMode;
	}

	public String getChargerMXChargerMode() {
		return chargerMXChargerMode;
	}

	public void setChargerBatteryVoltage(String chargerBatteryVoltage) {
		this.chargerBatteryVoltage = chargerBatteryVoltage;
	}

	public String getChargerBatteryVoltage() {
		return chargerBatteryVoltage;
	}

	public void setChargerDailyAH(String chargerDailyAH) {
		this.chargerDailyAH = chargerDailyAH;
	}

	public String getChargerDailyAH() {
		return chargerDailyAH;
	}
}
