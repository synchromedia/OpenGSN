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

import org.junit.Test;

import com.greenstarnetwork.tests.WSClient.AePlayWave;
import com.greenstarnetwork.tests.WSClient.PowerRegulator;

public class PowerRegulatorTest {

//	@Test
	public void testPlaySound() {
		AePlayWave pr = new AePlayWave("/home/knguyen/workspace/Beethoven5s.wav");
		pr.start();
		try {
			  Thread.sleep(6000);
		} catch (InterruptedException x) {}
//		pr.playSound("/home/knguyen/Music/popeye.wav");
	}

//	@Test
	public void testGetFacilityList() {
		PowerRegulator client = new PowerRegulator();
		client.getFacilityResources();
	}

//	@Test
	public void testGetVMLocation() {
		PowerRegulator client = new PowerRegulator();
		System.err.println("**********Location of VM: " + client.getVMLocation("10.20.101.28"));
	}
	
	@Test
	public void testRun() {
		PowerRegulator client = new PowerRegulator();
		Thread t = new Thread(client);
		t.setDaemon(true);
		t.start();
		try {
			  Thread.sleep(5 * 60 * 1000);
			} catch (InterruptedException x)
			{
			}
	}
	
//	@Test
	public void testConfig() {
		PowerRegulator client = new PowerRegulator("10.20.100.4", "10.20.101.13");
		client.loadConfigData();
		System.err.println("==============>Sound file: " + client.getConfig().getSoundfile());
		System.err.println("==============>Interval: " + client.getConfig().getInterval());
	}

//	@Test
	public void testGetVMStatus() {
		PowerRegulator client = new PowerRegulator("10.20.100.4", "10.20.101.13");
		System.err.println("**********Status of VM: " + client.getVMStatus("10.20.101.13"));
	}
}
