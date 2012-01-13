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
package com.greenstarnetwork.test.services.cloudmanager;

import java.util.HashMap;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greenstarnetwork.services.cloudmanager.ICloudManager;
import com.greenstarnetwork.services.cloudmanager.ICloudManagerProvider;

/**
 * Unit Test for the Cloud Manager Service
 * 
 * @author Mathieu Lemay (IT)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CloudManagerTest {
	@Autowired
	private ICloudManager cloudManager;
	@Autowired
	private ICloudManagerProvider provider;
	@Test
	public void testCreateInstance() {
		cloudManager.createInstance("sample-vm", "512", "1", "template");
	}

	@Test
	public void testCreateInstanceInHost() {
		cloudManager.createInstanceInHost("host1", "sample-vm", "512", "1", "template");
	}

	@Test
	public void testDescribeHost() {
		String ret = cloudManager.describeHost("host1");
		System.out.println(ret);
	}

	@Test
	public void testDescribeHosts() {
		List<String> retList = cloudManager.describeHosts();
		for (String ret : retList) {
			System.out.println(ret);
		}
	}

	@Test
	public void testDestroyInstance() {
		cloudManager.destroyInstance("host1", "sample");
	}
	@Test
	public void startInstance() {
		cloudManager.startInstance("host1", "sample");
	}
	@Test
	public void stopInstance() {
		cloudManager.stopInstance("host1", "sample");
	}
	@Test
	public void rebootInstance() {
		cloudManager.rebootInstance("host1", "sample");
	}
	@Test
	@Ignore
	public void testHostQuery() {
		cloudManager.hostQuery("","","","","");
	}
	@Test
	public void testOther(){
		cloudManager.executeAction("host1", "SampleAction", "test-value test1-value1");
	}
	
}
