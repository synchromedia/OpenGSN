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
package com.opengsn.controller.testharness.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.opengsn.controller.testharness.Constants;
import com.opengsn.controller.testharness.IFacility;
import com.opengsn.controller.testharness.VirtualHydroFacility;
import com.opengsn.controller.testharness.VirtualSolarFacility;
import com.opengsn.controller.testharness.exceptions.FacilityRetrievalException;
import com.opengsn.services.cloudmanager.model.VM;

public class FacilityDeserializer {

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
			System.out.println("folder does not exist or is empty");
		} else {
			for (int i=0; i<children.length; i++) {
				if(children[i].indexOf(".txt") > -1)
					aux.add(children[i]);
			}	
		}

		String[] facilities = new String[aux.size()];
		return aux.toArray(facilities);
	}

	public static void main(String[] args) throws IOException, FacilityRetrievalException {

		List<IFacility> gsnFacilities = new ArrayList<IFacility>();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String[] facilities = getFacilityFiles();

		for(int i=0; i<facilities.length; i++) {

			String content = readFileAsString(Constants.FACILITY_HOME + "/" + facilities[i]);

			//default to VirtualSolarFacility
			String classType = null;

			//for now the only other facility type is VirtualHydroFacility
			if(facilities[i].indexOf("VirtualHydroFacility") > -1)
				classType = "com.greenstarnetwork.services.controller.core.testharness.VirtualHydroFacility";	
			else if(facilities[i].indexOf("VirtualSolarFacility") > -1)
				classType = "com.greenstarnetwork.services.controller.core.testharness.VirtualSolarFacility";	
			else
				throw new FacilityRetrievalException("Unknown facility type associated with file " + facilities[i]);

			try {
				Object obj = gson.fromJson(content, Class.forName(classType));

				if(obj.getClass().getName().equalsIgnoreCase("com.greenstarnetwork.services.controller.core.testharness.VirtualSolarFacility")) {
					VirtualSolarFacility vsf = new VirtualSolarFacility( (VirtualSolarFacility)obj);
					gsnFacilities.add(vsf);
					
					List<VM> vms = vsf.getHostList().getHosts().get(0).getHostModel().getVMList();
					for(VM vm : vms) {
						System.out.println(vm.getName());
					}
				}

				else if(obj.getClass().getName().equalsIgnoreCase("com.greenstarnetwork.services.controller.core.testharness.VirtualHydroFacility")) {
					VirtualHydroFacility vhf = new VirtualHydroFacility( (VirtualHydroFacility)obj);
					gsnFacilities.add(vhf);

					List<VM> vms = vhf.getHostList().getHosts().get(0).getHostModel().getVMList();
					for(VM vm : vms) {
						System.out.println(vm.getName());
					}
				}

			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// return gsnFacilities;
	}
}
