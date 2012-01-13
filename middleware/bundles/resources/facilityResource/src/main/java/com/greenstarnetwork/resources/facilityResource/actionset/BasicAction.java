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
package com.greenstarnetwork.resources.facilityResource.actionset;

/**
 * 
 * Base action for facility resource actions.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 * 
 */

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iaasframework.capabilities.actionset.AbstractActionWithCommandSet;
import com.iaasframework.resources.core.capability.CapabilityException;
import com.iaasframework.resources.core.message.ICapabilityMessage;

public abstract class BasicAction extends AbstractActionWithCommandSet{

	Logger logger = LoggerFactory.getLogger(BasicAction.class);
	protected List<String> commandsList = null;
	protected String resourceID = null;
	
	public BasicAction(String action) {
		super(action);
	}
	
	
	public void extractResourceIdFromArgument() {
		if (resourceID != null) //resourceID has been extracted
			return;
		Map<Object, Object> args = this.actionRequest.getArguments();
		if ((args!= null) && args.containsKey((String)"default")) {
			resourceID = (String)args.get((String)"default");
			args.remove((String)"default");
			this.actionRequest.setArguments(args);
		}
	}

	protected void sendActionResponseMessage(ICapabilityMessage message, String requestor) throws CapabilityException
	{
		Properties properties = new Properties();
		properties.put("CAPABILITY", requestor);
		this.capability.sendMessage(message, properties);
	}
}
	
