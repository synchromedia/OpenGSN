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
package com.greenstarnetwork.tests.protocols.datastream;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenstarnetwork.protocols.datastream.DataStreamProtocolSession;
import com.greenstarnetwork.protocols.datastream.DataStreamProtocolSessionFactory;
import com.iaasframework.capabilities.protocol.IProtocolSession;
import com.iaasframework.capabilities.protocol.ProtocolException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;
import com.iaasframework.resources.core.descriptor.CapabilityProperty;

public class DataStreamProtocolSessionFactoryTest {

	private DataStreamProtocolSessionFactory cliProtocolSessionFactory = null;
	private CapabilityDescriptor capabilityDescriptor = null;
	int counter = 0;

	@Before
	public void setup() {
		cliProtocolSessionFactory = new DataStreamProtocolSessionFactory();
	}

	@Test
	public void testCorrectModuleDescriptor() throws ProtocolException {
		capabilityDescriptor = getCorrectModuleDescriptor();
		IProtocolSession protocolSession = null;
		protocolSession = cliProtocolSessionFactory
				.createProtocolSessionInstance(capabilityDescriptor);
		Assert.assertNotNull(protocolSession);
	}


	private CapabilityDescriptor getCorrectModuleDescriptor() {
		List<CapabilityProperty> capabilityProperties = new ArrayList<CapabilityProperty>();
		capabilityProperties.add(getCapabilityProperty(DataStreamProtocolSession.NUMBER_OF_LINES, "10"));
		CapabilityDescriptor capabilityDescriptor = new CapabilityDescriptor();
		capabilityDescriptor.setCapabilityProperties(capabilityProperties);
		return capabilityDescriptor;
	}

	private CapabilityProperty getCapabilityProperty(String propertyName, String propertyValue) {
		CapabilityProperty capabilityProperty = new CapabilityProperty();
		capabilityProperty.setName(propertyName);
		capabilityProperty.setValue(propertyValue);
		return capabilityProperty;
	}
}
