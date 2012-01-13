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
package com.greenstarnetwork.examples.libvirtClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.capabilities.actionset.IActionSetConstants;
import com.iaasframework.capabilities.commandset.ICommandSetConstants;
import com.iaasframework.capabilities.model.IModelConstants;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.capabilities.protocol.IProtocolConstants;
import com.iaasframework.core.transports.ITransportConstants;
import com.iaasframework.protocols.cli.CLIProtocolSession;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceRepository;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;
import com.iaasframework.resources.core.descriptor.CapabilityProperty;
import com.iaasframework.resources.core.descriptor.Information;
import com.iaasframework.resources.core.descriptor.ResourceDescriptor;

public class LibvirtClient {

	String ipAddress = null;
	String hostname = null;
	String resourceName = null;
	String username = "system";
	String password = "1gsn.ets";
	String prompt = "$";
	String location = "Montreal, QC, Canada";
	
	/** The logger **/
	private Logger logger = LoggerFactory.getLogger(LibvirtClient.class);
	
	/** EngineRepository reference that is wired by the blueprint */
	IResourceRepository resourceRepository = null;
	
	public LibvirtClient(){
		logger.debug("Starting Libvirt Engine Client ...");
	}

	public void runClient() throws Exception{
		System.err.println("***** Starting Libvirt Client Bundle *****");
		
		ipAddress = "10.20.100.3";
		hostname = "ets2";
		location = "Montreal, QC, Canada";
		prompt = "$";
		resourceName = "VMM Engine 1";
		
		ResourceDescriptor descriptor = createResourceDescriptor();
		IResource engine = resourceRepository.createResource(descriptor);

//		CommandSetCapabilityClient client = new CommandSetCapabilityClient(engine.getResourceIdentifier().getId().toString());
//		Map<Object, Object> args = new Hashtable<Object, Object>();
//		args.put("vmName", "vm1");
//		args.put("memory", "131072");
//		args.put("cpu", "1");
//		args.put("storagePath", "/home/vmm/vmtempl.qcow2");
//		args.put("xmlFile", "/tmp/test.xml");
//		String response = client.sendReceiveMessage("CreateCommand", args);
//		System.err.println("********* Reveived response: "+ response + "\n");

		System.err.println("********* Executing host info action *****");
		ActionSetCapabilityClient client = new ActionSetCapabilityClient(engine.getResourceIdentifier().getId().toString());
		String response = client.executeAction("HostInfoAction", null);
		System.err.println("********* Reveived response: "+ response + "\n");

		System.err.println("********* Executing refresh action *****");
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put("resourceID", engine.getResourceIdentifier().getId().toString());
		response = client.executeAction("RefreshAction", args);

		System.err.println("********* Reveived response: "+ response + "\n");
		
		
		ModelCapabilityClient modelClient = new ModelCapabilityClient(engine.getResourceIdentifier().getId().toString());
		RequestModelResponse reqModel = modelClient.requestModel(true);
		VmHostModel m = (VmHostModel)(reqModel.getResourceModel());
		System.err.println("********* ModelCapabilityClient: "+ m.toXML());
		
		ipAddress = "10.20.100.2";
		hostname = "ets1";
		location = "Montreal, QC, Canada";
		prompt = "$";
		resourceName = "VMM Engine 2";

		descriptor = createResourceDescriptor();
		IResource engine2 = resourceRepository.createResource(descriptor);

		System.err.println("********* Executing host info action *****");
		ActionSetCapabilityClient client2 = new ActionSetCapabilityClient(engine2.getResourceIdentifier().getId().toString());
		response = client2.executeAction("HostInfoAction", null);

		
		System.err.println("********* Executing refresh action *****");
		Map<Object, Object> arg1s = new Hashtable<Object, Object>();
		arg1s.put("resourceID", engine2.getResourceIdentifier().getId().toString());

		response = client2.executeAction("RefreshAction", arg1s);
		System.err.println("********* Reveived response: "+ response + "\n");
		
		modelClient = new ModelCapabilityClient(engine2.getResourceIdentifier().getId().toString());
		reqModel = modelClient.requestModel(true);
		m = (VmHostModel)(reqModel.getResourceModel());
		System.err.println("********* ModelCapabilityClient: "+ m.toXML());
		
		ipAddress = "10.20.100.23";
		hostname = "GSN-CRC-Server01";
		location = "Ottawa, ON, Canada";
		prompt = "$";
//		username = "gsn-crc1";
		password = "Greenstar123!";
		resourceName = "VMM Engine 3";

		descriptor = createResourceDescriptor();
		IResource engine3 = resourceRepository.createResource(descriptor);

		System.err.println("********* Executing host info action *****");
		ActionSetCapabilityClient client3 = new ActionSetCapabilityClient(engine3.getResourceIdentifier().getId().toString());
		response = client3.executeAction("HostInfoAction", null);

		
		System.err.println("********* Executing refresh action *****");
		Map<Object, Object> arg3s = new Hashtable<Object, Object>();
		arg3s.put("resourceID", engine3.getResourceIdentifier().getId().toString());

		response = client3.executeAction("RefreshAction", arg3s);
		System.err.println("********* Reveived response: "+ response + "\n");
		
		modelClient = new ModelCapabilityClient(engine3.getResourceIdentifier().getId().toString());
		reqModel = modelClient.requestModel(true);
		m = (VmHostModel)(reqModel.getResourceModel());
		System.err.println("********* ModelCapabilityClient: "+ m.toXML());
		
		logger.debug("\n\n********* Complete ***********\n\n");
		
		
	}

	public void stopClient() throws Exception {
		logger.debug("Stopping Libvirt Client Bundle");
	}
	
	public ResourceDescriptor createResourceDescriptor() {
		
		List<CapabilityDescriptor> moduleDescriptors = new ArrayList<CapabilityDescriptor>();
		moduleDescriptors.add(createActionSetCapabilityDescriptor());
		moduleDescriptors.add(createCommandSetCapabilityDescriptor());
		moduleDescriptors.add(createModelCapabilityDescriptor());
		moduleDescriptors.add(createProtocolCapabilityDescriptor());
		
		Information engineInformation = new Information("vmmEngine", resourceName, "1.0.0");
		
		ResourceDescriptor engineDescriptor = new ResourceDescriptor();
		engineDescriptor.setInformation(engineInformation);
		engineDescriptor.setCapabilityDescriptors(moduleDescriptors);
		
		return engineDescriptor;
	}

	private CapabilityDescriptor createActionSetCapabilityDescriptor() {
		Information information = new Information(IActionSetConstants.ACTIONSET, "Generic ActionSet", "1.0.0");
		CapabilityProperty nameProperty = new CapabilityProperty(IActionSetConstants.ACTIONSET_NAME, "vmmEngine");
		CapabilityProperty versionProperty = new CapabilityProperty(IActionSetConstants.ACTIONSET_VERSION, "1.0.0");

		List<CapabilityProperty> moduleProperties = new ArrayList<CapabilityProperty>();
		moduleProperties.add(nameProperty);
		moduleProperties.add(versionProperty);

		CapabilityDescriptor actionSetDescriptor = new CapabilityDescriptor();
		actionSetDescriptor.setCapabilityProperties(moduleProperties);
		actionSetDescriptor.setCapabilityInformation(information);
		
		return actionSetDescriptor;
	}
	
	private CapabilityDescriptor createCommandSetCapabilityDescriptor() {
		Information information = new Information(ICommandSetConstants.COMMANDSET, "Generic CommandSet", "1.0.0");
		CapabilityProperty nameProperty = new CapabilityProperty(ICommandSetConstants.COMMANDSET_NAME, "vmmEngine");
		CapabilityProperty versionProperty = new CapabilityProperty(ICommandSetConstants.COMMANDSET_VERSION, "1.0.0");

		List<CapabilityProperty> moduleProperties = new ArrayList<CapabilityProperty>();
		moduleProperties.add(nameProperty);
		moduleProperties.add(versionProperty);

		CapabilityDescriptor commandSetDescriptor = new CapabilityDescriptor();
		commandSetDescriptor.setCapabilityProperties(moduleProperties);
		commandSetDescriptor.setCapabilityInformation(information);
		
		return commandSetDescriptor;
	}
	

	private CapabilityDescriptor createModelCapabilityDescriptor() {
		Information information = new Information(IModelConstants.MODEL, "Generic Model", "1.0.0");
		CapabilityProperty nameProperty = new CapabilityProperty(IModelConstants.MODEL_NAME, "vmmModel");
		CapabilityProperty versionProperty = new CapabilityProperty(IModelConstants.MODEL_VERSION, "1.0.0");
		CapabilityProperty addressProperty = new CapabilityProperty("address", ipAddress);
		CapabilityProperty osProperty = new CapabilityProperty("os", "ubuntu");
		CapabilityProperty locationProperty = new CapabilityProperty("location", location);

		List<CapabilityProperty> moduleProperties = new ArrayList<CapabilityProperty>();
		moduleProperties.add(nameProperty);
		moduleProperties.add(versionProperty);
		moduleProperties.add(addressProperty);
		moduleProperties.add(osProperty);
		moduleProperties.add(locationProperty);

		CapabilityDescriptor modelDescriptor = new CapabilityDescriptor();
		modelDescriptor.setCapabilityProperties(moduleProperties);
		modelDescriptor.setCapabilityInformation(information);
		
		return modelDescriptor;
	}
	
	private CapabilityDescriptor createProtocolCapabilityDescriptor(){
		Information information = new Information(IProtocolConstants.PROTOCOL, "Generic Protocol", "1.0.0");
		
		List<CapabilityProperty> moduleProperties =  new ArrayList<CapabilityProperty>();
		moduleProperties.add(new CapabilityProperty(IProtocolConstants.PROTOCOL, "CLI"));
		moduleProperties.add(new CapabilityProperty(IProtocolConstants.PROTOCOL_VERSION, "1.0.0"));
//		moduleProperties.add(new CapabilityProperty(IProtocolConstants.PROTOCOL_USERNAME, "username"));
//		moduleProperties.add(new CapabilityProperty(IProtocolConstants.PROTOCOL_PASSWORD, "password"));

		moduleProperties.add(new CapabilityProperty(CLIProtocolSession.HAS_GREETING, "Yes"));
		moduleProperties.add(new CapabilityProperty(CLIProtocolSession.PROMPT, username + "@" + hostname + ":~" + prompt));
		moduleProperties.add(new CapabilityProperty(CLIProtocolSession.ERROR_MESSAGE_PATTERNS, "error"));
		moduleProperties.add(new CapabilityProperty(CLIProtocolSession.INTERMEDIATE_PROMPT, username + "@" + hostname + ":~" + prompt));
		moduleProperties.add(new CapabilityProperty(CLIProtocolSession.INTERMEDIATE_PROMPT_COMMAND, username + "@" + hostname + ":~" + prompt));

		moduleProperties.add(new CapabilityProperty(IProtocolConstants.PROTOCOL_SESSIONS_NUMBER, "1"));
		moduleProperties.add(new CapabilityProperty("Transport Identifier", "vmmEngine"));
//		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT, "Virtual"));
		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT, "SSH"));
		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT_VERSION, "1.0.0"));
		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT_HOST, ipAddress));
		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT_PORT, "22"));
		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT_USERNAME, username));
		moduleProperties.add(new CapabilityProperty(ITransportConstants.TRANSPORT_PASSWORD, password));
//		moduleProperties.add(new CapabilityProperty(IVirtualTransportProvider.TRANSPORT_VIRTUALTRANSPORTPROVIDER, "vmmEngine"));
			
		CapabilityDescriptor protocolDescriptor = new CapabilityDescriptor();
		protocolDescriptor.setCapabilityProperties(moduleProperties);
		protocolDescriptor.setCapabilityInformation(information);
		
		return protocolDescriptor;
	}

	
	/**
	 * @return the engineRepository
	 */
	public IResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	/**
	 * @param engineRepository the engineRepository to set
	 */
	public void setResourceRepository(IResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}
}
