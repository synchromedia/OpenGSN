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
package com.greenstarnetwork.transports.snmp;

import com.iaasframework.core.transports.ITransport;
import com.iaasframework.core.transports.ITransportConstants;
import com.iaasframework.core.transports.ITransportFactory;
import com.iaasframework.core.transports.TransportException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;

/** 
 * Provides a factory method to create SSH transport instances. The following properties need to 
 * be present in the capability descriptor:
 * transport = SSH
 * transport.host = <ip_address or hostname>
 * transport.port = <port number> (optional, default is 22)
 * transport.username = <username of the SSH account>
 * transport.password = <password of the SSH account>
 */
public class SNMPTransportFactory implements ITransportFactory {
	
	public ITransport createTransportInstance(CapabilityDescriptor capabilityDescriptor) throws TransportException {
		String transportId = capabilityDescriptor.getPropertyValue(ITransportConstants.TRANSPORT);
		if (transportId == null){
			//We ignore it or throw an exception?
			throw new TransportException("No transport module has been specified at the resource configuration");
		}else if (transportId.equals(SNMPTransport.SNMP)){
			return createTransport(capabilityDescriptor);
		}else{
			throw new TransportException("Transport "+transportId+" cannot be created by this factory");
		}
	}
	
	private ITransport createTransport(CapabilityDescriptor capabilityDescriptor) throws TransportException{
		String host = getAndValidateProperty(capabilityDescriptor, ITransportConstants.TRANSPORT_HOST);
		String port = null;
		try{
			port = getAndValidateProperty(capabilityDescriptor, ITransportConstants.TRANSPORT_PORT);
		}catch(TransportException ex){
			//if there is no port, we'll use the default one: 23
			port = "161";
		}
//		String username = getAndValidateProperty(capabilityDescriptor, ITransportConstants.TRANSPORT_USERNAME);
//		String password = getAndValidateProperty(capabilityDescriptor, ITransportConstants.TRANSPORT_PASSWORD);
		
		return new SNMPTransport(host, port);
	}
	
	private String getAndValidateProperty(CapabilityDescriptor capabilityDescriptor, String propertyName) throws TransportException{
		String property = capabilityDescriptor.getPropertyValue(propertyName);
		if (property ==  null){
			throw new TransportException("Could not create an instance of transport. Property "+propertyName+" has not been specified");
		}
		
		return property;
	}
}
