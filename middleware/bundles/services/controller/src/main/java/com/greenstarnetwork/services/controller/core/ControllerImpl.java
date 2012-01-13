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
package com.greenstarnetwork.services.controller.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.services.controller.executor.ExecutionPlan;
import com.greenstarnetwork.services.controller.executor.Executor;
import com.greenstarnetwork.services.controller.generator.HostParamGenerator;
import com.greenstarnetwork.services.controller.generator.LinkParamGenerator;
import com.greenstarnetwork.services.controller.manager.CloudManagerClient;
import com.greenstarnetwork.services.controller.manager.FacilityManagerClient;
import com.greenstarnetwork.services.controller.model.Host;
import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.LinkTable;
import com.greenstarnetwork.services.controller.model.MigrationPlan;
import com.greenstarnetwork.services.controller.plan.AggregatedPlanGenerator;

/**
 * Controller implementation
 * @author knguyen
 *
 */
public class ControllerImpl implements IController {

	public static int MAX_HOST_PER_CENTRE = 10;				//maximum number of host per data centre
	public static String DEFAULT_MASK = "255.255.0.0";		//default network mask

	private Logger log = Logger.getLogger();				//controller state logger
	
	private HostList hosts = null;							//list of hosts
	
	private LinkTable linktable = null;						//network connection table
	
	private MigrationPlan plan = null;						//migration plan
	
	private CloudManagerClient cloudManager = null;			//cloud manager
	
	private FacilityManagerClient facilityManager = null;	//facility manager
	
	//working mode: simulation or real mode
	private int mode = 0;

	private Executor executor = null;						//plan executor

	public ControllerImpl() {
		setMode(SIMULATION_MODE);
		executor = new Executor(this);
	}

	/**
	 * Initialize the controller and connect it to a domain
	 * @param domainID
	 */
	public void connect(String hostID) {
		loadCXF();
		setCloudManager(new CloudManagerClient(hostID));
		getCloudManager().createCloudManagerService();
		setFacilityManager(new FacilityManagerClient(hostID));
		getFacilityManager().createFacilityManagerService();
		getFacilityManager().createJMSQueue(this);
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
	
	
	@Override
	public synchronized ExecutionPlan executePlan() throws ControllerException{
		if ( (this.plan != null) && (this.linktable != null) )
		{
			ExecutionPlan ret = null;
			try {
				executor.setExecuting(true);
				ret = executor.executeMigrationPlan(plan, linktable);
				executor.setExecuting(false);		
				plan.logMigrations();
				plan.setDone(true);
				return ret;
			} catch (UnknownHostException e) {
//				e.printStackTrace();
				log.debug(e.toString());
				throw new ControllerException("Exception while executing migration plan. " + e.toString());
			} catch (IOException e) {
				log.debug(e.toString());
				executor.setExecuting(false);		
				plan.setDone(true);
				return ret;
			}
		}else if (this.plan == null)
			throw new ControllerException("Migration plan is null");
		else
			throw new ControllerException("LinkTable is null");
	}

	/**
	 * Generate a host list containing fake data.
	 * Used in SIMULATION mode
	 */
	@Override
	public void generateHostList(int nbrOfDataCentres) 
	{
		try {
			if (hosts == null)
				hosts = new HostList();
			
			if (linktable == null)
				linktable = new LinkTable();
			
			this.hosts.removeAll();
			this.linktable.removeAll();
			
			List<InetAddress> hostIPs = new ArrayList<InetAddress>();
			List<InetAddress> hostMasks = new ArrayList<InetAddress>();
			
			HostParamGenerator gen = new HostParamGenerator(); 
			gen.setMaxCPU(HostParamGenerator.MAX_CPU);
			gen.setMaxMemory(HostParamGenerator.MAX_MEMORY);
			gen.setMaxVMsPerHost(HostParamGenerator.MAX_VM_PER_HOST);
			gen.setNeworkMask(DEFAULT_MASK);
			
			for (int i=0; i<nbrOfDataCentres; i++)
			{
				String network_address = InetAddressUtil.computeNetworkAddress(
						InetAddress.getByName(gen.generateIP()), gen.getNetworkMask());
				log.debug("==> Data Center: " + network_address);
				gen.setNetworkAddress(network_address);
				HostList genlist = gen.generateFakeHostList(MAX_HOST_PER_CENTRE);
				hosts.mergeHostList(genlist);
				hostIPs.add(gen.getNetworkAddress());
				hostMasks.add(gen.getNetworkMask());
			}
			
			LinkParamGenerator linkgen = new LinkParamGenerator();
			this.linktable = linkgen.generateFakeLinkTable(hostIPs, hostMasks);
		} catch (UnknownHostException e) {
			//			e.printStackTrace();
			log.debug(e.toString());
		} catch (ControllerException e) {
			//			e.printStackTrace();
			log.debug(e.toString());
		}
	}

	/**
	 * Get the list of hosts from the cloud manager.
	 * Get energy values from the facility manager.
	 * Then assign a energy value to each host
	 * Used in REAL mode
	 */
	public synchronized void getResourcesFromCloud() 
	{
		System.err.println("Getting host list from cloud manager ...");
		//Get the list of hosts from the Cloud Manager
		List<Host> hl = this.getCloudManager().getHostList();
		if (hl == null)
			return;
		//Get facility information
		List<FacilityModel> fl = this.getFacilityManager().getFacilityList();
		if (fl != null) {
			Iterator<FacilityModel> it = fl.iterator();
			while (it.hasNext()) {
				FacilityModel fm = (FacilityModel)it.next();
				//Display info into console
				System.err.println("Domain : " + fm.getDomainID() + ". Location: " + fm.getLocation().getAddress());
				for (int pi=0; pi<hl.size(); pi++)
				{
					if (fm.getDomainID().compareTo(hl.get(pi).getDomainID()) == 0)
					{
						hl.get(pi).setPriorityBasedOnSourceType(fm.getOperationalSpecs().getPowerSourceType());
						hl.get(pi).setLifeTime(fm.getOperationalSpecs().getOpHourUnderCurrentLoad());
						hl.get(pi).setLifetimeUnderMaxLoad(fm.getOperationalSpecs().getOpHourUnderMaximumLoad());
						hl.get(pi).setThreshold(Double.valueOf(fm.getOperationalSpecs().getOpHourThreshold()));
						hl.get(pi).setThresholdUnderMax(Double.valueOf(fm.getOperationalSpecs().getOpHourThresholdUnderMax()));
						hl.get(pi).setStatus(String.valueOf(fm.getOperationalSpecs().getStatus()));
						//Display info into console
						System.err.println(" - Host : " + hl.get(pi).getResourceName() + ". IP address: " + 
								hl.get(pi).getHostModel().getAddress());
						System.err.println("          Energy type: " + fm.getOperationalSpecs().getPowerSourceType() + 
								". Op-hour: " + fm.getOperationalSpecs().getOpHourUnderCurrentLoad());
						List<VM> vmlist = hl.get(pi).getHostModel().getVMList();
						if (vmlist != null) {
							for (int pj=0; pj<vmlist.size(); pj++)
							{
								System.err.println("   + VM : " + vmlist.get(pj).getName());
							}
						}
					}
				}
			}
		}

		if (this.hosts == null)
			this.hosts = new HostList();
		else
			this.hosts.removeAll();
		this.hosts.setHosts(hl);

//		System.err.println(this.hosts.toXML());
		//Generate a mesh network interconnecting all hosts
		LinkParamGenerator linkgen = new LinkParamGenerator();
		this.linktable = linkgen.generateMeshLinkTable(hl);
		System.err.println("A full mesh network connecting all hosts has been generated.");
	}
	
	@Override
	public synchronized void generatePlan() throws ControllerException
	//public synchronized void generatePlan(double minAcceptableLifeTime, double maxNecessaryLifeTime) throws ControllerException 
	{
		if (this.hosts == null)
			throw new ControllerException("Host list is empty.");

		AggregatedPlanGenerator aggregatedPlanGenerator = new AggregatedPlanGenerator();
		try {
			//aggregatedPlanGenerator.execute(this.hosts, minAcceptableLifeTime, maxNecessaryLifeTime);
			aggregatedPlanGenerator.execute(this.hosts);
			//			HostList energySufficientHosts = aggregatedPlanGenerator.getEnergySufficientHostsList();
			//			if (energySufficientHosts != null) {
			//				if (this.cloudManager != null)
			//				{
			//					Map<String, Integer> priorities = new Hashtable<String, Integer>();
			//					Iterator<Host> it = energySufficientHosts.getHosts().iterator();
			//					int c = 1;
			//					while (it.hasNext()) {
			//						Host h = it.next();
			//						priorities.put(h.getResourceID(), c);
			//						c ++;
			//					}
			//					this.cloudManager.updateHostPriorityTable(priorities);
			//				}
			//			}

			this.plan = aggregatedPlanGenerator.getMigrationPlan();
			if (this.plan != null)
				this.plan.setDone(false);

			//Set new energy thresholds
			//			this.hosts = energySufficientHosts;
			//			System.err.println("New hostlist: " + energySufficientHosts.toXML());
		} catch (Exception e) {
			throw new ControllerException("error executing aggregatedPlanGenerator :: "+e);
		}
	}

	/**
	 * Controller behavior when a Facility Model of a domain changes
	 * @param domainID
	 * @throws ControllerException 
	 */
	public synchronized void changeFacilityModel(FacilityModel model) 
	{
		double newlifetime = model.getOperationalSpecs().getOpHourUnderCurrentLoad();
		double newlifetimeUnderMaxLoad = model.getOperationalSpecs().getOpHourUnderMaximumLoad();
		double newthreshold = Double.valueOf(model.getOperationalSpecs().getOpHourThreshold());
		double newthresholdUnderMax = Double.valueOf(model.getOperationalSpecs().getOpHourThresholdUnderMax());
		String newStatus = String.valueOf(model.getOperationalSpecs().getStatus());

		/*if ( (newlifetime < this.minAcceptableLifeTime) || 
				((newlifetime > this.maxNecessaryLifeTime) && (this.maxNecessaryLifeTime > 0)) )*/ 
		if ( (newlifetime < newthreshold) || 
				((newlifetime > newthresholdUnderMax) && (newthresholdUnderMax > 0)) )
		{
			String domainId = model.getDomainID();
			System.err.println("******* Op-hour of domain " + domainId + " is changed. Trigger migration ... ******");
			if (this.executor.isExecuting())
			{
				System.err.println("******* A previous plan is being executed. This update is discarded. ******");
			}else
			{
				this.getResourcesFromCloud();
				if (this.hosts != null) 
				{
					Iterator<Host> it = hosts.getHosts().iterator();
					while (it.hasNext()) {
						Host h = it.next();
						if (h.getDomainID().compareTo(domainId) == 0) {
							h.setLifeTime(newlifetime);
							h.setLifetimeUnderMaxLoad(newlifetimeUnderMaxLoad);
							h.setThreshold(newthreshold);
							h.setThresholdUnderMax(newthresholdUnderMax);
							h.setStatus(newStatus);
						}
					}
				}
				this.setMode(REAL_MODE);
				this.refreshPlan();
			}
		}
	}

	/**
	 * Force the Controller to compute migration plan with new data  
	 */
	public synchronized void refreshPlan()
	{
		if (this.hosts != null) 
		{
			try {
				this.generatePlan();
				if (this.getMigrationPlan() != null)
				{
					ExecutionPlan exPlan = this.executePlan();
					exPlan.toXML();
				}else
					System.err.println("No migration plan is generated.");
			} catch (ControllerException e) {
				log.debug(e.toString());
			}
		}else
			System.err.println("No migration plan is generated.");
	}

	@Override
	public HostList getHostList() {
		return this.hosts;
	}

	@Override
	public synchronized void setHostList(HostList hosts) {
		this.hosts = hosts;
	}

	@Override
	public MigrationPlan getMigrationPlan() {
		return this.plan;
	}

	@Override
	public LinkTable getLinkTable() {
		return this.linktable;
	}

	@Override
	public synchronized void setLinkTable(LinkTable linkTable) {
		this.linktable = linkTable;
	}

	/**
	 * @param cloudManager the cloudManager to set
	 */
	public void setCloudManager(CloudManagerClient cloudManager) {
		this.cloudManager = cloudManager;
	}

	/**
	 * @return the cloudManager
	 */
	public CloudManagerClient getCloudManager() {
		return cloudManager;
	}

	/**
	 * @param facilityManager the facilityManager to set
	 */
	public void setFacilityManager(FacilityManagerClient facilityManager) {
		this.facilityManager = facilityManager;
	}

	/**
	 * @return the facilityManager
	 */
	public FacilityManagerClient getFacilityManager() {
		return facilityManager;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Return the list of log files
	 */
	@Override
	public String getLogContent(String filename) {
		return new LogManager().getLogContent(filename);
	}

	/**
	 * Get the content of a given logfile
	 */
	@Override
	public List<String> getLogFiles() {
		return new LogManager().listLogFiles();
	}
}