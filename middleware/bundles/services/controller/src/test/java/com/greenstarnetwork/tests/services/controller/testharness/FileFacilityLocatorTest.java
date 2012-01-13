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
package com.greenstarnetwork.tests.services.controller.testharness;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.services.controller.core.testharness.FileFacilityLocator;
import com.greenstarnetwork.services.controller.core.testharness.IFacility;
import com.greenstarnetwork.services.controller.core.testharness.VirtualHydroFacility;
import com.greenstarnetwork.services.controller.core.testharness.VirtualSolarFacility;
import com.greenstarnetwork.services.controller.core.testharness.exceptions.FacilityRetrievalException;
import com.greenstarnetwork.services.controller.core.testharness.utils.TestHarnessLinkGenerator;
import com.greenstarnetwork.services.controller.core.testharness.vmmodel.Memtest86VMHarness;
import com.greenstarnetwork.services.controller.model.Host;
import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.LinkTable;

/**
 * Test loading virtual facilities using the FileFacilityLocator
 * make sure the loaded List<IFacility> is correct, and a fake LinkTable
 * can be generated out of it
 * 
 * @author hzhang
 *
 */
public class FileFacilityLocatorTest {

	@Test
	public void testFileFacilityLocator() throws FacilityRetrievalException {

		FileFacilityLocator ffl = new FileFacilityLocator();
		
		List<IFacility> facilities = ffl.getFacilities();
		
		Assert.assertNotNull(facilities);
		
		int vsfCount = 0;
		int vhfCount = 0;
		
		HostList hostList = new HostList();
		
		for(IFacility f : facilities) {
			if(f instanceof VirtualSolarFacility) {
				vsfCount++;
				
				List<Host> hosts = f.getHostList().getHosts();
				
				for(Host h : hosts) {
					int vmCount = h.getHostModel().getVMList().size();
					
					Assert.assertNotNull(vmCount);
					Assert.assertTrue("Solar Host has no vm hosted!", vmCount >= 1);
					
					hostList.addHost(h);
					List<VM> vms = h.getHostModel().getVMList();
					for(VM vm : vms) {
						Assert.assertTrue("Only Memtest86VMHarness VM type is supported", (vm instanceof Memtest86VMHarness) );
					}
				}
			}
			if(f instanceof VirtualHydroFacility) {
				vhfCount++;
				
				List<Host> hosts = f.getHostList().getHosts();
				
				for(Host h : hosts) {
					int vmCount = h.getHostModel().getVMList().size();
					
					Assert.assertNotNull(vmCount);
					Assert.assertTrue("Hydro Host has no vm hosted!", vmCount >= 1);
					
					hostList.addHost(h);
					List<VM> vms = h.getHostModel().getVMList();
					for(VM vm : vms) {
						Assert.assertTrue("Only Memtest86VMHarness VM type is supported", (vm instanceof Memtest86VMHarness) );
					}
				}
			}
		}
		
		//CRC and Cybera
		Assert.assertTrue("VirtualSolarFacility count not 2!", vsfCount == 2);
		
		//ETS
		Assert.assertTrue("VirtualHydroFacility count not 1!", vhfCount == 1);
		
		LinkTable linkTable = TestHarnessLinkGenerator.getLinkTable(hostList);
		
		Assert.assertNotNull(linkTable);
		
		String toXML = linkTable.toXML();
		Assert.assertNotNull(toXML);
		
		System.out.println(toXML);
	}

}
