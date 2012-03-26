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
package com.opengsn.controller.testharness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.opengsn.controller.model.Host;
import com.opengsn.controller.testharness.exceptions.FacilityRetrievalException;
import com.opengsn.controller.testharness.vmmodel.Memtest86VMHarness;
import com.opengsn.services.cloudmanager.model.VM;

/**
 * @author hzhang
 * 
 */
public class FileFacilityLocator implements IFacilityLocator {

	private static String readFileAsString(String filePath)
	throws java.io.IOException{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(
				new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead=0;
		while((numRead=reader.read(buf)) != -1){
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	private static String[] getFacilityFiles() {

		List<String> aux = new ArrayList<String>();

		File dir = new File(Constants.FACILITY_HOME);

		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i=0; i<children.length; i++) {
				if(children[i].indexOf(".txt") > -1)
					aux.add(children[i]);
			}	
		}

		String[] facilities = new String[aux.size()];
		return aux.toArray(facilities);
	}

	private Memtest86VMHarness memtest86FromGeneric(VM vm) {
		Memtest86VMHarness test86 = new Memtest86VMHarness();
		
		test86.setName(vm.getName());
		test86.setMemory(vm.getMemory());
		test86.setVcpu(vm.getVcpu());
		test86.setIp(vm.getIp());
		
		return test86;
	}
	
	private void convertFacilityVMs(IFacility f) {
		List<Host> hosts =  f.getHostList().getHosts();
	
			
		for(Host h : hosts) {
			
			if(f instanceof VirtualHydroFacility) {
				h.setEnergyPriority(Host.HYDRO_ENERGY_PRIORITY);
			}
			else if(f instanceof VirtualSolarFacility) {
				h.setEnergyPriority(Host.SUN_ENERGY_PRIORITY);
			}
			
			List<Memtest86VMHarness> memtest86VMs = new ArrayList<Memtest86VMHarness>();
			
			List<VM> vms = h.getHostModel().getVMList();
			for(VM vm : vms) {
				memtest86VMs.add(memtest86FromGeneric(vm));
			}
			
			h.getHostModel().removeVMs();
			
			for(Memtest86VMHarness test86 : memtest86VMs) {
				h.getHostModel().addVM(test86);
			}
		}
	}
	
	@Override
	public List<IFacility> getFacilities() throws FacilityRetrievalException {
		List<IFacility> gsnFacilities = new ArrayList<IFacility>();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String[] facilities = getFacilityFiles();

		if(facilities.length == 0)
			throw new FacilityRetrievalException("No virtual facility files found!");
		
		for(int i=0; i<facilities.length; i++) {

			String content = null;
			String fName = Constants.FACILITY_HOME + "/" + facilities[i];
			try {
				content = readFileAsString(fName);
			} catch (IOException e1) {
				throw new FacilityRetrievalException("Error reading virtual facility file: " + fName);
			}

			String classType = null;

			//for now the only acceptable facility types are VirtualHydroFacility & VirtualSolarFacility
			if(facilities[i].indexOf("VirtualHydroFacility") > -1)
				classType = "com.greenstarnetwork.services.controller.core.testharness.VirtualHydroFacility";	
			else if(facilities[i].indexOf("VirtualSolarFacility") > -1)
				classType = "com.greenstarnetwork.services.controller.core.testharness.VirtualSolarFacility";	
			else
				throw new FacilityRetrievalException("Unknown facility type associated with file " + fName);

			try {
				Object obj = gson.fromJson(content, Class.forName(classType));

				if(obj.getClass().getName().equalsIgnoreCase("com.greenstarnetwork.services.controller.core.testharness.VirtualSolarFacility")) {
					VirtualSolarFacility vsf = new VirtualSolarFacility( (VirtualSolarFacility)obj);
					convertFacilityVMs(vsf);
					gsnFacilities.add(vsf);
				}

				else if(obj.getClass().getName().equalsIgnoreCase("com.greenstarnetwork.services.controller.core.testharness.VirtualHydroFacility")) {
					VirtualHydroFacility vhf = new VirtualHydroFacility( (VirtualHydroFacility)obj);
					convertFacilityVMs(vhf);
					gsnFacilities.add(vhf);
				}
			}

			catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				throw new FacilityRetrievalException("Bad Json Syntax! " + e.getMessage());
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new FacilityRetrievalException("Facility Class Not Found! " + e.getMessage());
			}

		}

		return gsnFacilities;
	}

	//TODO for playback feature
	public List<IFacility> getFacilities(long startTime) {
		return null;
	}

	public FileFacilityLocator() {

	}

}
