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

/**
 * This class will test the AggregatedPlanGenerator class with some fake data. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import com.greenstarnetwork.services.controller.model.HostList;
import com.greenstarnetwork.services.controller.model.MigrationPlan;
import com.greenstarnetwork.services.controller.plan.AggregatedPlanGenerator;


public class AggregatedPlanGeneratorTest extends TestCase {

	/**
	 * 
	 */
	protected void setUp() throws Exception {
		
	}

	/**
	 * 
	 */
	public void testAggregatedPlanGenerator() throws Exception {
		
		String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/hostlist.txt"));

		HostList hostList = HostList.fromXML(s);
		
//		BasicGenerator basicGenerator = new BasicGenerator();
		
//		HostList hostList = basicGenerator.getBasicHosts();
		
		AggregatedPlanGenerator aggregatedPlanGenerator = new AggregatedPlanGenerator();
		aggregatedPlanGenerator.execute(hostList);
		MigrationPlan migrationPlan = aggregatedPlanGenerator.getMigrationPlan();
		HostList esHostList = aggregatedPlanGenerator.getEnergySufficientHostsList();
		
		System.err.println(esHostList.toXML());

		if (migrationPlan == null)
			System.err.println("******************migration plan is null");
//		Assert.assertEquals(migrationPlan, "migration plan is null");
	}
	
	
	private String readStringFromFile(InputStream stream){
		String answer = null;
		
		try {
			InputStreamReader streamReader = new InputStreamReader(stream);
			StringBuffer fileData = new StringBuffer(10000);
			BufferedReader reader = new BufferedReader(streamReader);
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
			    String readData = String.valueOf(buf, 0, numRead);
			    fileData.append(readData);
			    buf = new char[1024];
			}
			reader.close();
			answer = fileData.toString();
			fileData = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return answer;
	}
}
