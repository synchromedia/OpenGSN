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
package com.greenstarnetwork.services.tests.cloudManager;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.greenstarnetwork.services.cloudManager.CloudManager;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 *Cloud Manager tests
 *To test the cloud manager, The GSN project needs to be deployed in Karaf with some resources in.
 *Then, you can uncomment code below to test the cloud manager
 *
 */
public class CloudManagerTest extends TestCase{
	
	private CloudManager cloudManager;
	
	public CloudManagerTest(){
		super();
//		cloudManager = new CloudManager();
	}
	
	//Called before each test
	protected void setUp() {
    }
	
	//Called after each test
	protected void tearDown() {
    }

	public void testLoadResourceManagerData(){
		//Assert.assertNotNull( cloudManager.getManagers().getResourceManagers() );
	}
	
	public void testdescribeHosts(){
		//System.out.println( cloudManager.describeHosts().get(0) );
	}
	
	public void testdescribeHost(){
		//System.out.println( cloudManager.describeHost("5424c0a1-4ff0-43de-89b3-f6e9fcbe0617") );
	}
	
	public void testHostQuery(){
/*		List<String> list = cloudManager.hostQuery("-1", "-1", "-1", "4", "-1");
		System.out.println("\ntestHostQuery function result:\n");
		if(list != null){
			for(String s: list ){
				System.out.println("Host:\n" + s + "\n");
			}
		}
		System.out.println("End of testHostQuery function\n");
*/	}

}
