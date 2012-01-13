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
package com.greenstarnetwork.resources.raritanclimate.actionset;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iaasframework.capabilities.actionset.AbstractActionWithCommandSet;
import com.iaasframework.capabilities.actionset.ActionException;

public class BasicAction extends AbstractActionWithCommandSet{

	Logger logger = LoggerFactory.getLogger(BasicAction.class);
	protected List<String> commandsList = null;
	protected String resourceID = null;
	
	public BasicAction(String action) {
		super(action);
	}
	
	@Override
	public void executeAction() throws ActionException
	{
	}
	
	public void extractResourceIdFromArgument() {
	
	
		if (resourceID != null) //resourceID has been extracted
			return;
		Map<Object, Object> args = this.actionRequest.getArguments();
		if (args== null)
				System.err.println("********* args null: ");
		if ((args!= null) && args.containsKey((String)"resourceID")) {
					
			System.err.println("********* shu args!= null : ");
			resourceID = (String)args.get((String)"resourceID");
			System.err.println("********* resourceID  extracted: "+resourceID);
			args.remove((String)"resourceID");
			this.actionRequest.setArguments(args);
		}
	}
}
	
