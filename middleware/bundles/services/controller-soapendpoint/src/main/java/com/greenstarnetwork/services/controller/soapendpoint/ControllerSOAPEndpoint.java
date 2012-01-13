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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.greenstarnetwork.services.controller.core.ControllerException;
import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.core.Logger;
import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.LinkTable;
import com.greenstarnetwork.services.controller.model.MigrationPlan;

/**
 * @author knguyen
 * @author ffm
 *
 */
@WebService
public class ControllerSOAPEndpoint implements IControllerSOAPEndpoint {

	private IController controller = null;
	
	public ControllerSOAPEndpoint(IController controller) {
		this.controller = controller;
	}
	
	/* (non-Javadoc)
	 * @see com.greenstarnetwork.services.controller.soapendpoint.IControllerSOAPEndpoint#executePlan()
	 */
	@WebMethod
	public String executePlan() {
		try {
			return this.controller.executePlan().toXML();
		} catch (ControllerException e) {
			e.printStackTrace();
			return "Error: " + e.toString();
		}
	}

	/* (non-Javadoc)
	 * Randomly generate a list of host
	 * @param nbrOfDataCentres  :   number of data centres in the network
	 * @return :	list of generated hosts in XML format
	 */
	@WebMethod
	public String generateHostList(int nbrOfDataCentres) {
		this.controller.generateHostList(nbrOfDataCentres);
		return this.controller.getHostList().toXML();
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.services.controller.soapendpoint.IControllerSOAPEndpoint#generatePlan(double, double)
	 */
	@WebMethod
	public void generatePlan() throws ControllerException {
		//this.controller.generatePlan(minAcceptableLifeTime, maxNecessaryLifeTime);
		this.controller.generatePlan();
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.services.controller.soapendpoint.IControllerSOAPEndpoint#getLinkTable()
	 */
	@WebMethod
	public String getLinkTable() {
		LinkTable lt = this.controller.getLinkTable();
		if (lt == null)
			return "No link table.";
		return lt.toXML();
	}

	/**
	 * Return the current migration plan which is being executed and the history of previous migration plan being done.
	 */
	@WebMethod
	public String getMigrationPlan() {
		MigrationPlan plan = this.controller.getMigrationPlan();
		String log_file = System.getenv(Logger.IAAS_HOME) + Logger.LOG_DIR + MigrationPlan.LOG_FILE;
        String log_content = "";
		try {
			java.io.BufferedReader reader = new BufferedReader(new InputStreamReader(new
	                FileInputStream(log_file)));
	        String s = reader.readLine();
	        while (s != null)
	        {
//	        	System.err.println(s);
        		log_content += s + "\n";
	        	s = reader.readLine();
	        }		
	        reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (plan == null) {
			if (log_content.isEmpty())
				return "No migration plans.";
			else
				return log_content;
		}else if (plan.isDone())
		{
			return log_content;
		}else {
			return log_content + "\n" + plan.toString();
		}
	}

	@WebMethod
	public void connect(String hostID) {
		this.controller.connect(hostID);
	}

	@WebMethod
	public String getHostList() {
		HostList hl = this.controller.getHostList();
		if (hl == null)
			return "No host list.";
		return hl.toXML();
	}

	/*
	@WebMethod
	public double getMaxNecessaryLifeTime() {
		return this.controller.getMaxNecessaryLifeTime();
	}

	@WebMethod
	public double getMinAcceptableLifeTime() {
		return this.controller.getMinAcceptableLifeTime();
	}
	*/

	@WebMethod
	public int getMode() {
		return this.controller.getMode();
	}

	@WebMethod
	public void getResourcesFromCloud() {
		this.controller.getResourcesFromCloud();
	}

	/*
	@WebMethod
	public boolean setMaxNecessaryLifeTime(double maxNecessaryLifeTime) {
		return this.controller.setMaxNecessaryLifeTime(maxNecessaryLifeTime);
	}

	@WebMethod
	public boolean setMinAcceptableLifeTime(double minAcceptableLifeTime) {
		return this.controller.setMinAcceptableLifeTime(minAcceptableLifeTime);
	}
	*/

	@WebMethod
	public void setMode(int mode) {
		this.controller.setMode(mode);
	}

	@WebMethod
	public String getLogContent(String filename) {
		return this.controller.getLogContent(filename);
	}

	@WebMethod
	public List<String> getLogFiles() {
		return this.controller.getLogFiles();
	}

	@WebMethod
	public void refreshPlan() {
		this.controller.refreshPlan();
	}
}
