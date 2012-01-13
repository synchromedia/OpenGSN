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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.greenstarnetwork.services.controller.core.ControllerException;
import com.greenstarnetwork.services.controller.core.InetAddressUtil;
import com.greenstarnetwork.services.controller.generator.LinkParamGenerator;
import com.greenstarnetwork.services.controller.model.LinkTable;

/**
 * Test link param generator
 * @author knguyen
 *
 */
public class LinkParamGeneratorTest {

	@Test
	public void testLinkParamGenerator() {
		int nhosts = 4;
		
		LinkParamGenerator gen = new LinkParamGenerator();
		List<InetAddress> sources = new ArrayList<InetAddress>();
		List<InetAddress> sourceMasks = new ArrayList<InetAddress>();

		for (int pi=0; pi < nhosts; pi++) {
			String ip = gen.generateIP();
			try {
				InetAddress adr = InetAddress.getByName(ip);
//				System.err.println("--> IP: " + ip);
//				System.err.println("--> Network: " + gen.getNetworkAddress(adr, InetAddress.getByName("255.255.255.0")));
				sources.add(adr);
				sourceMasks.add(InetAddress.getByName("255.255.255.0"));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		LinkTable table;
		try {
			table = gen.generateFakeLinkTable(sources, sourceMasks);
			Assert.assertNotNull(table.toXML());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInetAddressUtil() {
		try {
			InetAddress network = InetAddress.getByName("207.162.8.0");
			InetAddress mask = InetAddress.getByName("255.255.255.0");
			InetAddress ip = InetAddress.getByName("207.162.8.10");
			Assert.assertTrue(InetAddressUtil.contains(network, mask, ip));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
