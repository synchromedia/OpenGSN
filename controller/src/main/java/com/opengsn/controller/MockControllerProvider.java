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
package com.opengsn.controller;

import java.util.List;

import com.opengsn.controller.executor.ExecutionPlan;
import com.opengsn.controller.manager.CloudManagerClient;
import com.opengsn.controller.manager.FacilityManagerClient;
import com.opengsn.controller.model.HostList;
import com.opengsn.controller.model.LinkTable;
import com.opengsn.controller.model.MigrationPlan;
import com.opengsn.services.facilitymanager.model.FacilityModel;

/**
 * @author Scott Campbell (CRC)
 *
 */
public class MockControllerProvider implements IControllerProvider {

//	Logger log = LoggerFactory.getLogger(MockControllerProvider.class);
	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#generateHostList(int)
	 */
	@Override
	public void generateHostList(int nbrOfDataCentres) {
//		log.info("GenerateHostList Called");

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getHostList()
	 */
	@Override
	public HostList getHostList() {
//		log.info("getHostList Called");
		return new HostList();
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#setHostList(com.greenstarnetwork.controller.model.HostList)
	 */
	@Override
	public void setHostList(HostList hosts) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getLinkTable()
	 */
	@Override
	public LinkTable getLinkTable() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#setLinkTable(com.greenstarnetwork.controller.model.LinkTable)
	 */
	@Override
	public void setLinkTable(LinkTable linkTable) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#generatePlan()
	 */
	@Override
	public void generatePlan() throws ControllerException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getMigrationPlan()
	 */
	@Override
	public MigrationPlan getMigrationPlan() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#executePlan()
	 */
	@Override
	public ExecutionPlan executePlan() throws ControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getResourcesFromCloud()
	 */
	@Override
	public void getResourcesFromCloud() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#connect()
	 */
	@Override
	public void connect() {
//		log.info("connect Called");
		System.out.println("Connect Called");

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#changeFacilityModel(com.greenstarnetwork.services.facilitymanager.model.FacilityModel)
	 */
	@Override
	public void changeFacilityModel(FacilityModel model) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#setMode(int)
	 */
	@Override
	public void setMode(int mode) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getMode()
	 */
	@Override
	public int getMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getLogFiles()
	 */
	@Override
	public List<String> getLogFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getLogContent(java.lang.String)
	 */
	@Override
	public String getLogContent(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#refreshPlan()
	 */
	@Override
	public void refreshPlan() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getCloudManager()
	 */
	@Override
	public CloudManagerClient getCloudManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.greenstarnetwork.controller.IControllerProvider#getFacilityManager()
	 */
	@Override
	public FacilityManagerClient getFacilityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
