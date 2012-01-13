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
package com.greenstarnetwork.tests.services.controller;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

import com.greenstarnetwork.services.controller.generator.HostParamGenerator;
import com.greenstarnetwork.services.controller.model.HostList;

/**
 * Test HostParamGenerator
 * @author knguyen
 *
 */
public class HostParamGeneratorTest {

	@Test
	public void testHostParamGenerator() {
//		HostMonitor mon = new HostMonitor();
//		List<Host> resources = mon.getHostList();
//		
//		HostParamGenerator gen = new HostParamGenerator();
//		HostList list = gen.generateHostList(resources);
//		
//		System.err.println(list.toXML());
	}

	@Test
	public void testGenerateFakeHostList() {
		for (int i=0; i< 1; i++){
			try {
				HostParamGenerator gen = new HostParamGenerator();
				gen.setMaxCPU(16);
				gen.setMaxMemory(50000);
				gen.setMaxLifeTime(100);
				gen.setMaxEnergy(10);
				gen.setMaxVMsPerHost(10);
				gen.setNetworkAddress("10.20.100.0");
				gen.setNeworkMask("255.255.255.0");
				gen.setMin_VM_Memory(256);
				HostList list = gen.generateFakeHostList(10);
//				Assert.assertNotNull(list.toXML());
				System.err.println(list.toXML());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
//	@Test 
//	public void testStringGenerator() {
//		BaseGenerator b = new BaseGenerator();
//		String s = b.generateString(10);
//		System.err.println(s);
//	}

//	@Test 
//	public void testIPGenerator() {
//		try {
//			BaseGenerator b = new BaseGenerator();
//			String s = b.generateIP("207.162.8.0", "255.255.255.0");
//			System.err.println("***********InetAddress:" + s);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
