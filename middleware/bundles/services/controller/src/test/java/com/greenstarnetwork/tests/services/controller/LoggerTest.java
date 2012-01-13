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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.greenstarnetwork.services.controller.core.LogManager;
import com.greenstarnetwork.services.controller.core.Logger;


/**
 * Test Logger
 * @author knguyen
 *
 */
public class LoggerTest 
{
	@Test
	public void testLogger() {
			Logger log = Logger.getLogger();
			log.debug("Test logger");
//			log.debug("Test logger1");
//			log.debug("Test logger2");
//			log.close();
			Assert.assertNotNull(log);
	}

	@Test
	public void testLogManager() {
			LogManager man = new LogManager();
			List<String> files = man.listLogFiles();
			if (files != null) {
				for (int pi=0; pi<files.size(); pi++)
					System.err.println(man.getLogContent(files.get(pi)));
			}
	}
}
