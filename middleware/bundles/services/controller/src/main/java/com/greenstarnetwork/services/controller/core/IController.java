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

import java.util.List;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.services.controller.executor.ExecutionPlan;
import com.greenstarnetwork.services.controller.manager.CloudManagerClient;
import com.greenstarnetwork.services.controller.manager.FacilityManagerClient;
import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.LinkTable;
import com.greenstarnetwork.services.controller.model.MigrationPlan;

/**
 * Controller interface
 * @author knguyen
 *
 */
/**
 * @author hzhang
 *
 */
public interface IController {

	/**
	 * Working mode 
	 */
	public static int SIMULATION_MODE = 0;
	public static int REAL_MODE = 1;
	
	/**
	 * Randomly generate a list of host
	 * @param nbrOfDataCentres  :   number of data centres in the network
	 */
	public void generateHostList(int nbrOfDataCentres);
	
	/**
	 * Get the list of hosts in the network, which has been generated previously
	 * @return
	 */
	public HostList getHostList();
	
	/**
	 * set the HostList
	 * @param hosts  :  a list of hosts retrieved from facility instances
	 */
	public void setHostList(HostList hosts);
	
	/**
	 * Get the table of links interconnecting nodes in the network
	 * @return
	 */
	public LinkTable getLinkTable();
	
	/**
	 * set the LinkTable
	 * @param linkTable	: a LinkTable
	 */
	public void setLinkTable(LinkTable linkTable);
	
	/**
	 * Generate the migration plan
	 * @param minAcceptableLifeTime			: minimal acceptable lifetime of hosts, at which the VMs should be moved out
	 * @param maxNecessaryLifeTime          : maximal acceptable lifetime of hosts, at which VMs can be moved in
	 * @throws ControllerException
	 */
	public void generatePlan() throws ControllerException; 
	//public void generatePlan(double minAcceptableLifeTime, double maxNecessaryLifeTime) throws ControllerException;
	
	/**
	 * Get the migration plan which has been generated previiously
	 * @return
	 */
	public MigrationPlan getMigrationPlan();
	
	/**
	 * Execute the migration plan
	 * @throws ControllerException
	 */
	public ExecutionPlan executePlan() throws ControllerException;

	/**
	 * Get all resources from Cloud
	 */
	public void getResourcesFromCloud(); 

	/**
	 * Connect Controller to a container hosting the Cloud and Facility managers 
	 * 
	 * @param hostID
	 */
	public void connect(String hostID);
	
	public void changeFacilityModel(FacilityModel model);

	/*
	public boolean setMinAcceptableLifeTime(double minAcceptableLifeTime);
	
	
	public double getMinAcceptableLifeTime();
	
	
	public boolean setMaxNecessaryLifeTime(double maxNecessaryLifeTime);
	
	
	public double getMaxNecessaryLifeTime();
	*/
	
	
	/**
	 * Set working mode
	 * 
	 * @param mode: REAL or SIMULATION
	 */
	public void setMode(int mode);

	/**
	 * Set working mode
	 * 
	 * @return
	 */
	public int getMode();
	
	/**
	 * Return the list of log files
	 * @return
	 */
	public List<String> getLogFiles();

	/**
	 * Get the content of a given logfile
	 * @param filename
	 * @return
	 */
	public String getLogContent(String filename); 
	

	/**
	 * Force the Controller to compute migration plan with new data  
	 */
	public void refreshPlan();
	
	/**
	 * Get current Cloud manager which is connected to the Controller
	 * @return
	 */
	public CloudManagerClient getCloudManager();
	
	/**
	 * Get current Facility manager which is connected to the Controller
	 * @return
	 */
	public FacilityManagerClient getFacilityManager();
}