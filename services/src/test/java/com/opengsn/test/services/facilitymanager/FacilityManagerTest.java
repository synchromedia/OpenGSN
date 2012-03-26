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
package com.opengsn.test.services.facilitymanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opengsn.services.facilitymanager.FacilityManagerException;
import com.opengsn.services.facilitymanager.IFacilityManager;

/**
 * 
 * @author knguyen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FacilityManagerTest {
	@Autowired
	private IFacilityManager man = null;
	
	@Test
	public void testExecuteAction() {
		try {
			man.executeAction("res1", "SampleAction", "test-value test1-value1");
		} catch (FacilityManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
