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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import javax.sound.sampled.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ICloudManagerSOAPEndpoint;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.IFacilityManagerSOAPEndpoint;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ExecuteAction;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ExecuteActionResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.DescribeHosts;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.ListAllFacilities;
import com.greenstarnetwork.services.facilitymanager.soapendpoint.FacilityManagerException_Exception;
import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.soapendpoint.IControllerSOAPEndpoint;
import com.greenstarnetwork.services.controller.soapendpoint.GetResourcesFromCloud;
import com.greenstarnetwork.services.controller.soapendpoint.SetMode;
import com.greenstarnetwork.services.controller.soapendpoint.RefreshPlan;


public class PowerRegulator implements Runnable {

	static private String RESOURCE_ID_ELEMENT = "<resourceId>";
	
	private List<Facility> facilityList = null;
	private int currrentLocation = 0;
	
	private ICloudManagerSOAPEndpoint cloudMgr_client = null;
	private IFacilityManagerSOAPEndpoint facilityMgr_client = null;
	private IControllerSOAPEndpoint controller = null;
	
	private String server = "207.162.8.46";
	private String vm = "10.20.101.13";
	private List<String> greenSites = null;
	private double period = 5;		//minute
	private RegulatorConfig config = null;
	
	public PowerRegulator() {
		loadCXF();
		this.createCloudManagerService();
		this.createFacilityManagerService();
		this.createControllerService();
		if (this.loadConfigData())
		{
			this.server = this.config.getServer();
			this.vm = this.config.getVm();
		}
	}
	
	public PowerRegulator(String server, String vm) {
		this.server = server;
		this.vm = vm;
		loadCXF();
		this.createCloudManagerService();
		this.createFacilityManagerService();
	}
	
	public void initializeGreenSites() {
		greenSites = new ArrayList<String>();
		greenSites.add("Ottawa");
		greenSites.add("Calgary");
	}

	/**
	 * Populates managers attribute by reading data from XML File given in project resources.
	 */
	public boolean loadConfigData(){	
		try {
			JAXBContext jc = JAXBContext.newInstance(RegulatorConfig.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File f = new File("regulatorconfig.xml");
			if(f.exists()){
				setConfig((RegulatorConfig)unmarshaller.unmarshal(f));
				return true;
			}else{
				System.err.println("************No configuration file found!");
				return false;
			}
						
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

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
	    setHttpConduit(ClientProxy.getClient(controller));
	}

	public void playSound(String filename) 
	{
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

	private void setHttpConduit(Client client) {
		HTTPConduit http = (HTTPConduit) client.getConduit();
		  HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		  httpClientPolicy.setConnectionTimeout(360000);
		  httpClientPolicy.setAllowChunking(false);
		  httpClientPolicy.setReceiveTimeout(320000);

		  http.setClient(httpClientPolicy);
	}
	
	/**
	 * Get the list of hosts managed by IaaS Resource Manager 
	 */
	public String getVMLocation(String vmIP) {
		List<String> enginelist = cloudMgr_client.describeHosts(new DescribeHosts()).getReturn();
		if ( (enginelist != null) && (enginelist.size() > 0) )
		{
			for (int i=0; i<enginelist.size(); i++) 
			{
				VmHostModel model = VmHostModel.fromXML(enginelist.get(i));
				if (model != null)
				{
					List<VM> vmList = model.getVMList();
					if (vmList != null)
					{
						for (int pi=0; pi< vmList.size(); pi++) 
						{
//							System.err.println("================>VM: " + vmList.get(pi).getIp());
							if (vmList.get(pi).getIp().equals(vmIP)) {
								return model.getLocation();
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get the list of hosts managed by IaaS Resource Manager 
	 */
	public String getVMStatus(String vmIP) {
		List<String> enginelist = cloudMgr_client.describeHosts(new DescribeHosts()).getReturn();
		if ( (enginelist != null) && (enginelist.size() > 0) )
		{
			for (int i=0; i<enginelist.size(); i++) 
			{
				VmHostModel model = VmHostModel.fromXML(enginelist.get(i));
				if (model != null)
				{
					List<VM> vmList = model.getVMList();
					if (vmList != null)
					{
						for (int pi=0; pi< vmList.size(); pi++) 
						{
//							System.err.println("================>VM: " + vmList.get(pi).getIp());
							if (vmList.get(pi).getIp().equals(vmIP)) {
								return vmList.get(pi).getStatus().toString();
							}
						}
					}
				}
			}
		}
		return null;
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
			List<String> enginelist = facilityMgr_client.listAllFacilities(new ListAllFacilities()).getReturn();
			System.err.println("****WSClientTest: Number of facility resources: " + enginelist.size());
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
						FacilityModel model = FacilityModel.fromXML(enginelist.get(i));
						Facility fac = new Facility();
						fac.setId(id);
						fac.setModel(model);
						if (this.facilityList == null)
							this.facilityList = new ArrayList<Facility>();
						this.facilityList.add(fac);
					}
				}
			}
			System.err.println("****Facility Resource List**********");
			for (int i=0; i<this.facilityList.size(); i++) {
				System.err.println(this.facilityList.get(i).getId());
			}
		}catch (FacilityManagerException_Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void changePowerDC(List<String> DCs, String current, String max) {
		for (int pi=0; pi<DCs.size(); pi++)
		{
			for (int pj=0; pj<this.facilityList.size(); pj++) {
				if (this.facilityList.get(pj).getModel().getLocation().getAddress().indexOf(DCs.get(pi)) > -1) {
					//Do smth
					String ret = setFacilityOpHour(this.facilityList.get(pj).getId(), current, max);
					System.err.println("---->Change power level of " + DCs.get(pi) + " returns: " + ret);
					break;
				}
			}
		}
	}
	
	public String setFacilityOpHour(String id, String current, String max) {
		ExecuteAction action = new ExecuteAction();
		action.setArg0(id);
		action.setArg1("SetOPHoursAction");
		action.setArg2("opHourUnderCurrentLoad " + current + " opHourUnderMaximumLoad " + max);
		try {
			return facilityMgr_client.executeAction(action).getReturn();
		}catch (FacilityManagerException_Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void regulate(String vmIP) {
		String location = this.getVMLocation(vmIP);
		System.err.println("*********** Current location of VM: " + location);
		List<String> DCs = new ArrayList<String>();
		if ( (location.indexOf("Ottawa") > 0) ||
			 (location.indexOf("Calgary") > 0) )
		{
			DCs.add(location);
			this.changePowerDC(DCs, "0.3", "0.1");
		}else
		{
			DCs.add("Ottawa");
			this.changePowerDC(DCs, "6",  "4");
			this.refreshController();
		}
	}
	
	public void refreshController() {
		RunController rc = new RunController(this.controller);
		Thread t = new Thread(rc);
		t.start();
	}
	
	@Override
	public void run() {
		this.getFacilityResources();
		while (true) {
			this.regulate(this.vm);
			try {
				long t = 0;
				while (t < this.config.getInterval() * 60 * 1000) {
					t += 30 * 1000;
					new Thread().sleep((long)(30 * 1000)); //sleep 30s
					String stat = this.getVMStatus(this.vm);
					System.err.println("==============> STATUS: " + stat);
					if (stat.compareTo("MIGRATING") == 0)
					{//play music
						System.err.println("********** PLAYING MUSIC ****************");
						AePlayWave pr = new AePlayWave(this.config.getSoundfile());
						pr.start();
						break;
					}
				}
				new Thread().sleep((long)(this.config.getInterval() * 60 * 1000));
			}catch (Exception e) {};
		}
	}

	public void setConfig(RegulatorConfig config) {
		this.config = config;
	}

	public RegulatorConfig getConfig() {
		return config;
	}
	
	public static void main (String[] args)
	{
		PowerRegulator client = new PowerRegulator();
		Thread t = new Thread(client);
		t.setDaemon(true);
		t.start();
	}
}
