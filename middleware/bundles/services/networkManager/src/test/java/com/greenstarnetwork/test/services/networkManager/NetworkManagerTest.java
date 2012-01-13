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
package com.greenstarnetwork.test.services.networkManager;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.greenstarnetwork.services.networkManager.NetworkManager;
import com.greenstarnetwork.services.networkManager.NetworkManagerException;

/**
 * 
 * @author knguyen
 *
 */
public class NetworkManagerTest {

	private NetworkManager man = null;
	
	@Test
	public void testCreateIPTable() {
		try {
			createIPTable();
//			System.err.println(man.getIptable().toXML());
			assertNotNull(man.getIptable().getPool());
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAssignIPtoVM() {
		try {
			createIPTable();
			String address = man.assignVMAddress("vmTest").toXML();
//			System.err.println(address);
			assertNotNull(address);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.err.println(man.getIptable().toXML());
	}

	
	@Test
	public void testGetIPFromMAC() {
		try {
			createIPTable();
			String ip = man.retrieveVMAddress("54:52:00:47:53:8C", "vmTest1").getIp();
//			System.err.println(ip);
			assertEquals(ip, "10.22.101.140");
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.err.println(man.getIptable().toXML());
	}

	@Test
	public void testReleaseAssignment() {
		try {
			createIPTable();
			boolean ret = man.releaseAssignment("vmTest");
//			System.err.println(ip);
			assertNotNull(ret);
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.err.println(man.getIptable().toXML());
	}

	@Test
	public void testGenerateDHCPConfigFile() {
		try {
			createIPTable();
			man.getIptable().generateDHCPConfigFile("testDHCP.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createIPTable() throws NetworkManagerException {
		man = new NetworkManager();
	}
}
