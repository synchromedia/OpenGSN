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
package com.opengsn.test.services.networkmanager;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opengsn.services.networkmanager.NetworkManager;
import com.opengsn.services.networkmanager.NetworkManagerException;

/**
 * 
 * @author Kim Nguyen (ETS)
 * @author Mathieu Lemay (IT)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class NetworkManagerTest {
	@Autowired
	private NetworkManager man = null;

	@Test
	public void testCreateIPTable() {
		System.out.println(man.getIptable().toXML());
		assertNotNull(man.getIptable().getPool());
	}

	@Test
	public void testAssignIPtoVM() {
		try {
			String address = man.assignVMAddress("vmTest").toXML();
			System.out.println(address);
			assertNotNull(address);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetIPFromMAC() {
		try {
			String ip = man.retrieveVMAddress("54:52:00:47:53:8C", "vmTest1").getIp();
			System.out.println(ip);
//			assertEquals(ip, "10.20.101.140");
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testReleaseAssignment() {
		try {
			boolean ret = man.releaseAssignment("vmTest");
			System.out.println("10.20.101.140");
			assertNotNull(ret);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Test
	// public void testGenerateDHCPConfigFile() {
	// try {
	// createIPTable();
	// man.getIptable().generateDHCPConfigFile("testDHCP.txt");
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NetworkManagerException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
