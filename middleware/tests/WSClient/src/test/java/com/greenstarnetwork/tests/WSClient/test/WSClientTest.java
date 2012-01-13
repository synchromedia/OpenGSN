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
package com.greenstarnetwork.tests.WSClient.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import com.greenstarnetwork.tests.WSClient.WSClient;


public class WSClientTest {
	
	private WSClient client = null;
	
	@Before
	public void init(){
		client = new WSClient();
	}
	
//	@Test
//	public void testListVM(){
//		client.list();
//		client.getResourceModel();
//	}

//	@Ignore
//	@Test
	public void testRemoveAllVMs(){
		client.list();
		client.removeAllResources();
	}

//	@Test
	public void testExecute(){
		client.list();
		client.executeWS3();
//		client.getResourceModel();
	}	

	@Test
	public void testGetCloudResources(){
//		client.getCloudResources();
//		client.executeRebootInstance();
//		client.executeCloudAction();
//		client.executeMigrateSerie();
//		client.executeCloudCreateInstanceAction();
//		client.executeCloudDestroyInstanceAction();
//		client.executeCloudMigrateInstanceAction();
//		client.getCloudResources();
	}

//	@Test
	public void testRefreshCloudRssources() {
		client.refreshCloudResources();
	}
	
//	@Test
	public void testGetFacilityResources(){
		client.getFacilityResources();
	}
	
//	@Test
	public void testGetPDUResources(){
		client.getPDUResources();
	}

//	@Test
	public void testGetPowerSourceResources(){
		client.getPowerSourceResources();
	}
	
//	@Test
	public void testGetArchiveData(){
		client.getArchiveData();
	}

//	@Test
	public void testController(){
		client.getMigrationPlan();
	}
}
