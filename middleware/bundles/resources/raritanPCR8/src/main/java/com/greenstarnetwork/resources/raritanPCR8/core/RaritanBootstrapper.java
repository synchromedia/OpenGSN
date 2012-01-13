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
package com.greenstarnetwork.resources.raritanPCR8.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.facilityModel.config.Facility;
import com.greenstarnetwork.models.facilityModel.config.PDU;
import com.greenstarnetwork.models.facilityModel.config.Sink;
import com.greenstarnetwork.models.facilityModel.config.Source;
import com.greenstarnetwork.resources.raritanPCR8.actionset.RefreshAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.SetOutletConsumerAction;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceBootstrapper;
import com.iaasframework.resources.core.ResourceException;

/**
 * 
 * @author knguyen
 *
 */
public class RaritanBootstrapper implements IResourceBootstrapper
{
	private static final String IAAS_HOME = "IAAS_HOME";
	public static final String GSN_FACILITY_FACILITY_XML = "/gsn/facility/facility.xml";

	private Logger logger = LoggerFactory.getLogger(RaritanBootstrapper.class);
	
	/**
	 * The Engine Model needs to be initialized here
	 */
	public void bootstrap(IResource engineIdentifier) throws ResourceException 
	{
		ActionSetCapabilityClient client = new ActionSetCapabilityClient(engineIdentifier.getResourceIdentifier().getId());
		Map<Object, Object> arg = new Hashtable<Object, Object>();
		arg.put("resourceID", engineIdentifier.getResourceIdentifier().getId());
		String response = client.executeAction(RefreshAction.ACTION, arg);
		logger.debug("********* Received response: "+ response + "\n");

		//Associate PDU's outlet with the consumers
		try {
			Facility facility = this.readFacilityConfigFile(System.getenv(IAAS_HOME) + GSN_FACILITY_FACILITY_XML);
			if (facility != null)
			{
				List<PDU> pdus = facility.getPdu();
				if (pdus != null) {
					java.util.Iterator<PDU> it = pdus.iterator();
					String myName = engineIdentifier.getResourceDescriptor().getInformation().getName();
					while (it.hasNext()) {
						PDU item = it.next();
						if ( item.getAliase().compareTo(myName) == 0 )
						{
							List<Sink> sinks = item.getSink();
							if (sinks != null)
							{
								java.util.Iterator<Sink> sit = sinks.iterator();
								while (sit.hasNext()) {
									Sink s = sit.next();
									response = setPDUOutletConsumer(client, s.getPort(), s.getAliase());
									logger.debug("********* Received response: "+ response);
								}
							}
							List<Source> sources = item.getSource();
							if (sources != null)
							{
								java.util.Iterator<Source> suit = sources.iterator();
								while (suit.hasNext()) {
									Source s = suit.next();
									response = setPDUOutletConsumer(client, s.getPort(), s.getAliase());
									logger.debug("********* Received response: "+ response);
								}
							}
						}
					}
				}
				
			}
		} catch (IOException e) {
			logger.debug(e.toString());
		}
		logger.debug("\n\n********* Complete ***********\n\n");
	}

	/**
	 * Manually link an outlet to a device
	 * @param client
	 * @param outletID
	 * @param consummerIP
	 * @return
	 * @throws ActionException
	 */
	private String setPDUOutletConsumer(ActionSetCapabilityClient client, String port, String consummerIP) 
		throws ActionException 
	{
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put("Outlet", "/system1/outlet" + port);
		args.put("Consumer", consummerIP);
		return client.executeAction(SetOutletConsumerAction.ACTION, args);
	}

	/**
	 * Read facility topology from configuration file
	 * @param pathFileName
	 * @return
	 * @throws IOException
	 */
	public Facility readFacilityConfigFile(String pathFileName) throws IOException {
		File file = new File(pathFileName);
		if (!file.exists()){
			logger.debug("Facility config file does not exist! in $IAAS_HOME" + GSN_FACILITY_FACILITY_XML);
			return null;
		}
		
		FileInputStream fin = new FileInputStream(pathFileName);
		BufferedInputStream bis = new BufferedInputStream(fin);
		String result = "";
		while (bis.available()>0){
			result+=(char)bis.read();
		}
		return Facility.fromXML(result);
	}
}