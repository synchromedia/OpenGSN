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
package com.greenstarnetwork.resources.facilityResource.commandset;

import java.io.IOException;

import com.greenstarnetwork.models.facilityModel.Port;
import com.greenstarnetwork.models.facilityModel.PowerDistribution;
import com.greenstarnetwork.models.facilityModel.PowerSink;
import com.greenstarnetwork.models.facilityModel.PowerSource;
import com.greenstarnetwork.models.facilityModel.config.Facility;
import com.greenstarnetwork.models.facilityModel.config.PDU;
import com.greenstarnetwork.models.facilityModel.config.Sink;
import com.greenstarnetwork.models.facilityModel.config.Source;
import com.greenstarnetwork.resources.facilityResource.core.FacilityResourceToFromFile;
import com.greenstarnetwork.resources.facilityResource.core.ResourceManagerService;


/**
 * 
 * This command will update all the connected resources to the facility resource.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public class InitializeCommand extends BasicCommand {

	public static final String GSN_FACILITY_FACILITY_XML = "/gsn/facility/facility.xml";
	public static final String COMMAND = "InitializeCommand";
	protected ResourceManagerService service;
	protected FacilityResourceToFromFile facilityResourceToFromFile;
	protected String file_name = null;
	public InitializeCommand() {
		super(COMMAND);
	}


	public void executeCommandMain() throws Exception
	{
		init();
		service.init();
		facilityResourceToFromFile= new FacilityResourceToFromFile();
		//		if (!facilityModel.getInitialized()){
		if (true)//initialize all the time
		{
			Facility facility = facilityResourceToFromFile.readFacilityConfigFile(facilityResourceToFromFile.getIAASPath()+file_name);
			if (facility==null)
			{
				logger.error("facility config model is null. Facility model did not initialized!!!");
			}
			else
			{
				Boolean initializeSuccessfull = true;
				facilityModel.getPowerDistributionList().clear();
				for (PDU pdu:facility.getPdu())
				{
					String pduResourceID = null;
					if (pdu.getAliase()!=null)
					{
						pduResourceID = service.getResourceIDbyAliase(pdu.getAliase());
					}
					if (pduResourceID==null)
					{
						logger.error("no resource found for pdu aliase:"+pdu.getAliase());
						initializeSuccessfull = false;
					}
					else
					{
						PowerDistribution powerDistribution = new PowerDistribution();
						powerDistribution.setResourceID(pduResourceID);
						for (Source source:pdu.getSource())
						{
							String sourceResourceID = null;
							if (source.getAliase()!=null)
							{
								sourceResourceID = service.getResourceIDbyAliase(source.getAliase());
							}
							if (sourceResourceID==null)
							{
								logger.error("no resource found for source aliase:"+source.getAliase());
								initializeSuccessfull = false;
							}
							else
							{
								PowerSource powerSource = new PowerSource();
								Port port = new Port();
								port.setConnectedResourceID(sourceResourceID);
								port.setPortNumber(Integer.valueOf(source.getPort()));
								powerSource.setPort(port);
								powerDistribution.addPowerSource(powerSource);
							}
						}
						for (Sink sink:pdu.getSink())
						{
							String sinkResourceID = null;
							if (sink.getAliase()!=null)
							{
								sinkResourceID = service.getResourceIDbyAliase(sink.getAliase());
							}
							if (sinkResourceID==null)
							{
								logger.warning("no resource found for sink aliase:"+sink.getAliase());
								//initializeSuccessfull = false;
							}
							else
							{
								PowerSink powerSink = new PowerSink();
								Port port = new Port();
								port.setConnectedResourceID(sinkResourceID);
								port.setPortNumber(Integer.valueOf(sink.getPort()));
								powerSink.setPort(port);
								powerDistribution.addPowerSink(powerSink);
							}
						}
						facilityModel.addPowerDistribution(powerDistribution);	
					}
				}
				String climateResourceID = null;
				if (facility.getClimateAliase()!=null){
					climateResourceID = service.getResourceIDbyAliase(facility.getClimateAliase());
				}
				if (climateResourceID==null){
					logger.warning("no resource found for climate aliase:"+facility.getClimateAliase());
					//initializeSuccessfull = false;
				}else{
					facilityModel.setClimateResourceID(climateResourceID);
				}

				if (initializeSuccessfull)
				{ 
					facilityModel.setInitialized(true);
				}
				else
				{
					facilityModel.setInitialized(false);
					facilityModel.getOperationalSpecs().setBatteryChargePercentage(-1);
					facilityModel.getOperationalSpecs().setOnGrid("TRUE");
					facilityModel.getOperationalSpecs().setOpHourUnderCurrentLoad(-1);
					facilityModel.getOperationalSpecs().setOpHourUnderMaximumLoad(-1);
					facilityModel.getOperationalSpecs().setStatus("NOTINIT");
					facilityModel.getOperationalSpecs().setTotalConsummingPower(-1);
					facilityModel.getOperationalSpecs().setTotalGeneratingPower(-1);
				}
			}
		}
	}

	public void init() throws IOException{
		file_name=GSN_FACILITY_FACILITY_XML;
		service = new ResourceManagerService();

	}


}