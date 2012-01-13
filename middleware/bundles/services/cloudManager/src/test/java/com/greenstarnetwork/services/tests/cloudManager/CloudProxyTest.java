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
package com.greenstarnetwork.services.tests.cloudManager;

import junit.framework.TestCase;

import com.greenstarnetwork.services.cloudManager.CloudProxy;

/**
 * 
 * @author knguyen
 *
 */
public class CloudProxyTest extends TestCase{
	
	private CloudProxy proxy;
	
	public CloudProxyTest(){
		super();
	}
	
	//Called before each test
	protected void setUp() {
    }
	
	//Called after each test
	protected void tearDown() {
    }

	public void testToXML(){
		proxy = new CloudProxy();
		proxy.setId("123-234-345");
		proxy.setIp("120.0.0.1");
		System.err.println(proxy.toXML());
	}
	
}
