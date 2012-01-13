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
package com.greenstarnetwork.tests.WSClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;


import com.iaasframework.capabilities.model.soapendpoint.IModelCapabilitySOAPEndpoint;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionArgumentsEntry;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionArgumentsMap;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionSetCapabilitySOAPEndpoint;
import com.iaasframework.capabilities.actionset.soapendpoint.IActionSetCapabilitySOAPEndpoint;
import com.iaasframework.core.resources.manager.soapendpoint.IResourceManagerSOAPEndpoint;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceException_Exception;
import com.iaasframework.resources.core.ResourceException;
import com.iaasframework.capabilities.model.soapendpoint.ModelException_Exception;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionException_Exception;
import com.iaasframework.capabilities.actionset.ActionException;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ICloudManagerSOAPEndpoint;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.IFacilityManagerSOAPEndpoint;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.DescribeHosts;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ListAllFacilities;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ListAllPDUs;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ListAllPowerSources;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.FacilityManagerException_Exception;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.GetArchiveDataByRangDate;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.CreateInstanceInHostResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.CreateInstanceInHost;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.DestroyInstance;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.DestroyInstanceResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.MigrateInstance;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.MigrateInstanceResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ExecuteAction;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ExecuteActionResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.RebootInstance;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.RebootInstanceResponse;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.services.controller.soapendpoint.IControllerSOAPEndpoint;
import com.greenstarnetwork.services.controller.soapendpoint.GetMigrationPlan;
//import com.iaasframework.resources.core.ResourceException_Exception;
/**
 * 
 * @author knguyen
 *
 */
public class WSClient {

	static private String RESOURCE_ID_ELEMENT = "<resourceId>";

	private IResourceManagerSOAPEndpoint client = null;
	private List<ResourceData> enginelist = null;

	private IModelCapabilitySOAPEndpoint model_client = null;
	private IActionSetCapabilitySOAPEndpoint actionset_client = null;

	private ICloudManagerSOAPEndpoint cloudMgr_client = null;
	private IFacilityManagerSOAPEndpoint facilityMgr_client = null;

	private IControllerSOAPEndpoint controller = null;

	
	private String server = "10.20.100.4";
	
	private List<String> vmList = null;
	
	public WSClient() 
	{	
//		System.setProperty("sun.net.client.defaultConnectTimeout", "100000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "100000");
		loadCXF();
//		this.createManagerService();
//		this.createModelService();
//		this.createActionSetService();
		this.createCloudManagerService();
//		this.createFacilityManagerService();
//		this.createControllerService();
	}

	/**
	 * Load CXF transport files
	 */
	private void loadCXF() {
		ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
			BusFactory.class.getClassLoader());
			SpringBusFactory springBusFactory = new SpringBusFactory();
			Bus bus = springBusFactory.createBus(new String[]
				{ "META-INF/cxf/cxf.xml", "META-INF/cxf/cxf-extension-soap.xml",
				  "META-INF/cxf/cxf-extension-http-jetty.xml" }, false);
			// The last parameter is telling CXF not to load the cxf-extension-*.xml from META-INF/cxf
			// You can set the bus the normal CXF endpoint or other camel-cxf endpoint
		} finally {
			Thread.currentThread().setContextClassLoader(oldCL);
		}
	}
	
	public void createManagerService() {
		QName serviceName = new QName("http://soapendpoint.manager.resource.core.iaasframework.com/", "ResourceManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.manager.resource.core.iaasframework.com/", "ResourceManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ResourceManagerSOAPEndpoint"); 
	    client = service.getPort(portName,  IResourceManagerSOAPEndpoint.class);
	}

	public void createModelService() {
		QName serviceName = new QName("http://soapendpoint.model.capabilities.iaasframework.com/", "ModelCapabilitySOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.model.capabilities.iaasframework.com/", "ModelCapabilitySOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ModelCapabilitySOAPEndpoint"); 
	    model_client = service.getPort(portName,  IModelCapabilitySOAPEndpoint.class);
	}
	
	public void createActionSetService() {
		QName serviceName = new QName("http://soapendpoint.actionset.capabilities.iaasframework.com/", "ActionSetCapabilitySOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.actionset.capabilities.iaasframework.com/", "ActionSetCapabilitySOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ActionSetCapabilitySOAPEndpoint"); 
	    actionset_client = service.getPort(portName,  IActionSetCapabilitySOAPEndpoint.class);
	}
	
	public void createCloudManagerService() {
		QName serviceName = new QName("http://soapendpoint.cloudManager.services.greenstarnetwork.com/", "CloudManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.cloudManager.services.greenstarnetwork.com/", "CloudManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/CloudManagerSOAPEndpoint"); 
	    cloudMgr_client = service.getPort(portName,  ICloudManagerSOAPEndpoint.class);
	    setHttpConduit(ClientProxy.getClient(cloudMgr_client));
	}
	
	public void createFacilityManagerService() {
		QName serviceName = new QName("http://soapendpoint.facilityManager.services.greenstarnetwork.com/", "FacilityManagerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.facilityManager.services.greenstarnetwork.com/", "FacilityManagerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/FacilityManagerSOAPEndpoint"); 
	    facilityMgr_client = service.getPort(portName,  IFacilityManagerSOAPEndpoint.class);
	    setHttpConduit(ClientProxy.getClient(facilityMgr_client));
	}
	
	public void createControllerService() {
		QName serviceName = new QName("http://soapendpoint.controller.services.greenstarnetwork.com/", "ControllerSOAPEndpointService");
	    QName portName = new QName("http://soapendpoint.controller.services.greenstarnetwork.com/", "ControllerSOAPEndpointPort");
	    Service service = Service.create(serviceName);
	    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,"http://" + server + ":8181/cxf/ControllerSOAPEndpoint"); 
	    controller = service.getPort(portName,  IControllerSOAPEndpoint.class);
	}

	
	private void setHttpConduit(Client client) {
		HTTPConduit http = (HTTPConduit) client.getConduit();
		  HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		  httpClientPolicy.setConnectionTimeout(360000);
		  httpClientPolicy.setAllowChunking(false);
		  httpClientPolicy.setReceiveTimeout(320000);

		  http.setClient(httpClientPolicy);
	}
	
	public void list() {
		System.err.println("****WSClientTest List**********");
		enginelist = client.listResources();
		System.err.println("****WSClientTest List: Number of engines: " + enginelist.size());
		for (int i=0; i<enginelist.size(); i++) {
			System.err.println("****WSClientTest List: Engine: " + 
					" ,Type: " + enginelist.get(i).getType() + 
					" ,Name: " + enginelist.get(i).getName() + 
					" ,ID: " + enginelist.get(i).getId());
		}
	}
	
	public void listPDU() {
		System.err.println("****WSClientTest List**********");
		enginelist = client.listReourcesByType("PDU");
		System.err.println("****WSClientTest List: Number of PDUs: " + enginelist.size());
		for (int i=0; i<enginelist.size(); i++) {
			System.err.println("****WSClientTest List: Engine: " + 
					" ,Type: " + enginelist.get(i).getType() + 
					" ,Name: " + enginelist.get(i).getName() + 
					" ,ID: " + enginelist.get(i).getId());
		}
	}

	public void removeAllResources() {
		System.err.println("****WSClientTest RemoveAll Resources**********");
		for (int i=0; i<enginelist.size(); i++) {
			try {
				client.removeResource(enginelist.get(i).getType(), enginelist.get(i).getId());
			} catch (ResourceException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getResourceModel() {
		for (int i=0; i<enginelist.size(); i++) {
			try {
				System.err.println(model_client.getResourceModel(enginelist.get(i).getId()));
			} catch (ModelException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void execute() 
	{
//		try {
			int i = 0;
			boolean found = false;
			while (i < enginelist.size())
			{
				System.err.println("****WSClientTest execute: Engine: " + 
						" ,Type: " + enginelist.get(i).getType() + 
						" ,Name: " + enginelist.get(i).getName() + 
						" ,ID: " + enginelist.get(i).getId());
				if (enginelist.get(i).getType().compareTo("RaritanPCR8") == 0)
//				if (enginelist.get(i).getType().compareTo("vmmEngine") == 0)
				{
					found = true;
					break;
				}
				
				i ++;
			}
			if (!found)
				System.err.println("****WSClient execute: NO vmmEngine found**********");
			else {
				System.err.println("****WSClient execute: " + enginelist.get(i).getName());
//				String command = "CreateCommand -vmName vm1 -memory 131072 -cpu 1 -storagePath /home/vmm/vmtempl.qcow2 -xmlFile /tmp/test.xml"; 
//				String command = "VMInfoCommand -vmName vm1"; 
//				String command = "UpdateCommand"; 
//				String response = client.executeCommand(enginelist.get(i).getId().toString(), command);
//				System.err.println("****WSClientTest Execute: " + response);
				
				String action = "RefreshAction"; 
				ActionArgumentsEntry e = new ActionArgumentsEntry();
				e.setKey("resourceID");
				e.setValue(enginelist.get(i).getId().toString());
				ActionArgumentsMap actionargs = new ActionArgumentsMap();
				actionargs.getEntries().add(e);

//				String action = "MigrateAction"; 
//				ActionArgumentsEntry e = new ActionArgumentsEntry();
//				e.setKey("vmName");
//				e.setValue("vm1");
//				ActionArgumentsEntry e1 = new ActionArgumentsEntry();
//				e1.setKey("destHost");
//				e1.setValue("10.20.100.3");

				
//				ActionArgumentsMap actionargs = new ActionArgumentsMap();
//				actionargs.getEntries().add(e);
//				actionargs.getEntries().add(e1);

//				String action = "QueryOutletAction";
//				String arguments = "Outlet /system1/outlet1";
				
//				String response = (String) actionset_client.executeAction(enginelist.get(i).getId(), action, actionargs);
//				String response = (String) actionset_client.executeActionWS(enginelist.get(i).getId(), action, arguments);
//				System.err.println("****WSClientTest Execute: " + response);
			}
//		}catch (ActionException_Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	public void executeWS() 
	{
//		try {
			int i = 0;
			boolean found = false;
			while (i < enginelist.size())
			{
				if (enginelist.get(i).getType().compareTo("vmmEngine") == 0)
				{
					found = true;
					break;
				}
				
				i ++;
			}
			if (!found)
				System.err.println("****WSClient execute: NO vmmEngine found**********");
			else {
				System.err.println("****WSClient execute: " + enginelist.get(i).getName());
				
				String action = "RefreshAction";
				String argument = "resourceID " + enginelist.get(i).getId();
//				String action = "DeleteAction";
//				String action = "StartAction";
//				String argument = "vmName vm3";

//				String action = "CreateAction";
//				String argument = "-vmName serverTest64 -memory 131072 -cpu 1 -template server91064";
				
//				String response = (String) actionset_client.executeActionWS(enginelist.get(i).getId(), action, argument);
//				System.err.println("****WSClientTest Execute: " + response);
			}
//		}catch (ActionException_Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	public void executeWS2() {
		try {
			int i = 0;
			boolean found = false;
			while (i < enginelist.size())
			{
				if (enginelist.get(i).getType().compareTo("vmmEngine") == 0)
				{
					String xml = model_client.getResourceModel(enginelist.get(i).getId());
					if (xml.indexOf("VLCServer_server91064_1281721932278") > 0) {
						found = true;
						break;
					}
				}
				
				i ++;
			}
			if (!found)
				System.err.println("****WSClient execute: NO vmmEngine found**********");
			else 
			{
				System.err.println("****WSClient execute: " + enginelist.get(i).getName());
				
//				String action = "RefreshAction";
//				String argument = "-resourceID " + enginelist.get(i).getId();
//				String action = "MigrateAction";
//				String argument = "vmName vmSamir_desktop91032_1281645645872 destHost 10.20.100.2";

//				String action = "StopAction";
				String action = "StartAction";
				String argument = "vmName VLCServer_server91064_1281721932278";
				
//				String response = (String) actionset_client.executeActionWS(enginelist.get(i).getId(), action, argument);
//				System.err.println("****WSClientTest Execute: " + response);
			}
//		}catch (ActionException_Exception e)
//		{
//			e.printStackTrace();
		} catch (ModelException_Exception e) {
			e.printStackTrace();
		}
	}

	public void executeWS3() {
		try {
			String response = (String) actionset_client.executeActionWS("b93f862b-625d-4c3d-aac9-ccaf1fc77955", 
					"RefreshAction", 
					"resourceID b93f862b-625d-4c3d-aac9-ccaf1fc77955");
			System.err.println("****WSClientTest Execute: " + response);
		}catch (ActionException_Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void executeCloudCreateInstanceAction() {
		CreateInstanceInHost arg = new CreateInstanceInHost();
		arg.setArg0("151248fc-51a3-4d04-bf2e-64fea74e05bf");
		arg.setArg1("CRC110215");
		arg.setArg2("512000");
		arg.setArg3("1");
		arg.setArg4("server91032");
		CreateInstanceInHostResponse response = cloudMgr_client.createInstanceInHost(arg);
		System.err.println("****WSClientTest Execute: " + response.getReturn());
	}
	
	public void executeCloudDestroyInstanceAction() 
	{
		DestroyInstance arg = new DestroyInstance();
		arg.setArg0("bea0dacc-b950-4846-8b62-6c92e3708828");
		arg.setArg1("googledemo_server91064_1292442421869");
		DestroyInstanceResponse response = cloudMgr_client.destroyInstance(arg);
		System.err.println("****WSClientTest Execute: " + response.getReturn());
	}

	public void executeCloudMigrateInstanceAction() {
		MigrateInstance arg = new MigrateInstance();
		arg.setArg0("55f8ae76-0620-4d78-a889-30eb154e7cda");
		arg.setArg1("controller1_server91064_1304607805094");
		arg.setArg2("36ab88ac-a4a9-4c99-b841-e4894ed04bff");
		MigrateInstanceResponse response = cloudMgr_client.migrateInstance(arg);
		System.err.println("****WSClientTest Execute: " + response.getReturn());
	}

	public void executeMigrateSerie() {
		MigrateInstance arg = new MigrateInstance();
		arg.setArg0("8161a970-f4d5-4c58-8d2b-e281ee397afc");
		arg.setArg2("7ceec2ea-2555-4374-8dc8-1b229ab606e4");
		if (this.vmList != null) {
			for (int pi=0; pi<vmList.size(); pi++)
			{
				System.err.println("****WSClientTest Execute: " + vmList.get(pi));
				arg.setArg1(vmList.get(pi));
				MigrateInstanceResponse response = cloudMgr_client.migrateInstance(arg);
				System.err.println("****WSClientTest Execute: " + response.getReturn());
			}
		}
	}

	public void executeCloudAction() {
		ExecuteAction arg = new ExecuteAction();
		arg.setArg0("55f8ae76-0620-4d78-a889-30eb154e7cda");
//		arg.setArg1("RefreshAction");
//		arg.setArg2("resourceID 55f8ae76-0620-4d78-a889-30eb154e7cda");
		arg.setArg1("SetVMStatusAction");
		arg.setArg2("vmName controller1_server91064_1304607805094 status MIGRATING");
//		arg.setArg1("SetVMIPAction");
//		arg.setArg2("vmName controller1_server91064_1304607805094 ip 10.20.101.24");
		ExecuteActionResponse response = cloudMgr_client.executeAction(arg);
		System.err.println("****WSClientTest Execute: " + response.getReturn());
	}

	public void executeRebootInstance() {
		RebootInstance arg = new RebootInstance();
		arg.setArg0("bea0dacc-b950-4846-8b62-6c92e3708828");
		arg.setArg1("vmTest8_server91032_1281979889003");
		RebootInstanceResponse response = cloudMgr_client.rebootInstance(arg);
		System.err.println("****WSClientTest Execute: " + response.getReturn());
	}

	/**
	 * Get the list of hosts managed by IaaS Resource Manager 
	 */
	public void getCloudResources() {
		System.err.println("****Cloud Resource List**********");
		List<String> enginelist = cloudMgr_client.describeHosts(new DescribeHosts()).getReturn();
		System.err.println("****WSClientTest: Number of Cloud resources: " + enginelist.size());
		if ( (enginelist != null) && (enginelist.size() > 0) )
		{
			for (int i=0; i<enginelist.size(); i++) 
			{
				System.err.println("Resource : \n" + enginelist.get(i));
				VmHostModel model = checkHostID(enginelist.get(i), "e5495167-bb8d-49ae-8fb2-03b3cd86ef66");
				if (model != null)
				{
//					System.err.println("Resource : \n" + enginelist.get(i));
					if (this.vmList == null)
						vmList = new ArrayList<String>();
					for (int pi=0; pi<model.getVMList().size(); pi++)
					{
						vmList.add(model.getVMList().get(pi).getName());
						System.err.println("VM : " + model.getVMList().get(pi).getName());
					}
				}
			}
		}
	}

	public void refreshCloudResources() {
		List<String> enginelist = cloudMgr_client.describeHosts(new DescribeHosts()).getReturn();
		System.err.println("****WSClientTest: Number of Cloud resources: " + enginelist.size());
		if ( (enginelist != null) && (enginelist.size() > 0) )
		{
			for (int i=0; i<enginelist.size(); i++) 
			{
				int pi = enginelist.get(i).indexOf(RESOURCE_ID_ELEMENT);
				if (pi > -1)
				{
					pi += RESOURCE_ID_ELEMENT.length();
					int pj = enginelist.get(i).indexOf("<", pi+1);
					String id = enginelist.get(i).substring(pi, pj);
					System.err.println("****Refreshing resource : " + id);
					ExecuteAction arg = new ExecuteAction();
					arg.setArg0(id);
					arg.setArg1("RefreshAction");
					arg.setArg2("resourceID " + id);
					ExecuteActionResponse response = cloudMgr_client.executeAction(arg);
					System.err.println("****RefreshAction response: " + response.getReturn());
				}
			}
		}
	}
	
	
	public VmHostModel checkHostID(String xml, String hostId) 
	{
		int pi = xml.indexOf(RESOURCE_ID_ELEMENT);
		if (pi > -1)
		{
			pi += RESOURCE_ID_ELEMENT.length();
			int pj = xml.indexOf("<", pi+1);
			String id = xml.substring(pi, pj);
//			System.err.println("Resource : \n" + id);
			if (id.compareTo(hostId) == 0)
				return VmHostModel.fromXML(xml);
		}
		return null;
	}
	
	
	/**
	 * Get the list of hosts managed by IaaS Resource Manager 
	 */
	public void getFacilityResources() {
		try {
			System.err.println("****Facility Resource List**********");
			List<String> enginelist = facilityMgr_client.listAllFacilities(new ListAllFacilities()).getReturn();
			System.err.println("****WSClientTest: Number of facility resources: " + enginelist.size());
			if ( (enginelist != null) && (enginelist.size() > 0) )
			{
				for (int i=0; i<enginelist.size(); i++) 
				{
					System.err.println("Resource : \n" + enginelist.get(i));
				}
			}
		}catch (FacilityManagerException_Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getPDUResources() {
		try {
			System.err.println("****PDU Resource List**********");
			List<String> enginelist = facilityMgr_client.listAllPDUs(new ListAllPDUs()).getReturn();
			System.err.println("****WSClientTest: Number of PDU resources: " + enginelist.size());
			if ( (enginelist != null) && (enginelist.size() > 0) )
			{
				for (int i=0; i<enginelist.size(); i++) 
				{
					System.err.println("Resource : \n" + enginelist.get(i));
				}
			}
		}catch (FacilityManagerException_Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getPowerSourceResources() {
		try {
			System.err.println("****PowerSource Resource List**********");
			List<String> enginelist = facilityMgr_client.listAllPowerSources(new ListAllPowerSources()).getReturn();
			System.err.println("****WSClientTest: Number of PowerSource resources: " + enginelist.size());
			if ( (enginelist != null) && (enginelist.size() > 0) )
			{
				for (int i=0; i<enginelist.size(); i++) 
				{
					System.err.println("Resource : \n" + enginelist.get(i));
				}
			}
		}catch (FacilityManagerException_Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getArchiveData() {
		System.err.println("****Archive data**********");
		GetArchiveDataByRangDate arg = new GetArchiveDataByRangDate();
		arg.setArg0("20110306");
		arg.setArg1("20110306");
		arg.setArg2("ce0608e0-1191-4ddb-bfed-80878608f602");
		System.err.println("getArchiveData: " + facilityMgr_client.getArchiveDataByRangDate(arg).getReturn());
	}
	
	public void getMigrationPlan() {
		System.err.println("****Migration plan**********");
		GetMigrationPlan arg = new GetMigrationPlan();
		System.err.println("getMigrationPlan: " + controller.getMigrationPlan(arg).getReturn());
	}	
}