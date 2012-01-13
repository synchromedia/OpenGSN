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
package com.greenstarnetwork.tests.models.facilityModel;

/**
 * 
 * test facilityModel class
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */


import org.junit.Test;
import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.common.Alarm;
import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.facilityModel.Location;
import com.greenstarnetwork.models.facilityModel.OperationalSpecs;
import com.greenstarnetwork.models.facilityModel.PowerDistribution;
import com.greenstarnetwork.models.facilityModel.PowerSink;
import com.greenstarnetwork.models.facilityModel.PowerSource;
import com.greenstarnetwork.models.facilityModel.config.Facility;
import com.iaasframework.core.internal.persistence.utilities.Assert;



public class FacilityModelTest {

	@Test
	public void testFacilityModel() {
		FacilityModel facilityModel = new FacilityModel();
		facilityModel.setLocation(new Location("here"));
		facilityModel.setClimate(new Climate());
		facilityModel.setOperationalSpecs(new OperationalSpecs());
		PowerDistribution powerDistribution1 = new PowerDistribution("001");
		powerDistribution1.addPowerSink(new PowerSink("00111"));
		powerDistribution1.addPowerSink(new PowerSink("00112"));
		powerDistribution1.addPowerSink(new PowerSink("00113"));
		powerDistribution1.addPowerSource(new PowerSource("00121"));
		powerDistribution1.addPowerSource(new PowerSource("00122"));
		powerDistribution1.addPowerSource(new PowerSource("00123"));
		facilityModel.addPowerDistribution(powerDistribution1);
		PowerDistribution powerDistribution2 = new PowerDistribution("002");
		powerDistribution2.addPowerSink(new PowerSink("00211"));
		powerDistribution2.addPowerSink(new PowerSink("00212"));
		powerDistribution2.addPowerSink(new PowerSink("00213"));
		powerDistribution2.addPowerSource(new PowerSource("00221"));
		powerDistribution2.addPowerSource(new PowerSource("00222"));
		powerDistribution2.addPowerSource(new PowerSource("00223"));
		facilityModel.addPowerDistribution(powerDistribution2);
		facilityModel.getAlarms().add(new Alarm());
		facilityModel.getOperationalSpecs().setStatus("GREEN");
		System.out.println(facilityModel.toXML());
		Assert.notNull(facilityModel, "facilityModel is null!");
	}
	
	@Test
	public void testFacilityConfig() {
		Facility facility = Facility.fromXML(
				"<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"+
				"<facility>"+
				"	<climateAliase>ETS climate</climateAliase>"+
				"	<pdu>"+
				"		<aliase>ETS PDU</aliase>"+
				"		<sink>"+
				"			<port>3</port>"+
				"			<aliase>ETS server 1</aliase>"+
				"		</sink>"+
				"		<sink>"+
				"			<port>2</port>"+
				"			<aliase>ETS server 2</aliase>"+
				"		</sink>"+
				"	</pdu>"+
				"	<pdu>"+
				"		<aliase>CRC PDU</aliase>"+
				"		<source>"+
				"			<port>12</port>"+
				"			<aliase>CRC solar</aliase>"+
				"		</source>"+
				"		<sink>"+
				"			<port>12</port>"+
				"			<aliase>CRC server</aliase>"+
				"		</sink>"+
				"	</pdu>"+
				"</facility>");
		System.out.println(facility.toXML());
		Assert.notNull(facility, "facilityModel is null!");
	}
	
	
	
}
