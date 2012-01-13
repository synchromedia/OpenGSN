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
package com.greenstarnetwork.services.controller.soapendpoint;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.greenstarnetwork.services.controller.core.ControllerException;

/**
 * 
 * @author knguyen
 * @author ffm
 *
 */
@WebService
public interface IControllerSOAPEndpoint {

	/**
	 * Randomly generate a list of host, used in SIMULATION mode
	 * @param nbrOfDataCentres  :   number of data centres in the network
	 */
	@WebMethod
	public String generateHostList(int nbrOfDataCentres);
	
	/**
	 * Get the list of hosts in the network
	 * @return
	 */
	@WebMethod
	public String getHostList();
	
	/**
	 * Get the table of links interconnecting nodes in the network
	 * @return
	 */
	@WebMethod
	public String getLinkTable();
	
	/**
	 * Generate the migration plan
	 * @throws ControllerException
	 */
	@WebMethod
	public void generatePlan() throws ControllerException; 
	
	/**
	 * Get the migration plan which has been generated previously
	 * @return
	 */
	@WebMethod
	public String getMigrationPlan();
	
	/**
	 * Execute the migration plan
	 * @throws ControllerException
	 */
	@WebMethod
	public String executePlan() throws ControllerException;

	/**
	 * Get all resources from Cloud
	 */
	@WebMethod
	public void getResourcesFromCloud(); 

	/**
	 * Connect Controller to a container hosting the Cloud and Facility managers 
	 * 
	 * @param hostID
	 */
	@WebMethod
	public void connect(String hostID);
	
	/*
	@WebMethod
	public boolean setMinAcceptableLifeTime(double minAcceptableLifeTime);
	
	@WebMethod
	public double getMinAcceptableLifeTime();
	
	@WebMethod
	public boolean setMaxNecessaryLifeTime(double maxNecessaryLifeTime);
	
	@WebMethod
	public double getMaxNecessaryLifeTime();
	*/
	
	/**
	 * Set working mode
	 * 
	 * @param mode: REAL or SIMULATION
	 */
	@WebMethod
	public void setMode(int mode);

	/**
	 * Set working mode
	 * 
	 * @return
	 */
	@WebMethod
	public int getMode();

	/**
	 * Return the list of log files
	 * @return
	 */
	@WebMethod
	public List<String> getLogFiles();

	/**
	 * Get the content of a given logfile
	 * @param filename
	 * @return
	 */
	@WebMethod
	public String getLogContent(String filename); 

	/**
	 * Force the Controller to compute migration plan with new data  
	 */
	@WebMethod
	public void refreshPlan();
}
