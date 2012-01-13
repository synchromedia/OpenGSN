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
package com.greenstarnetwork.tests.WSClient;

import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.soapendpoint.IControllerSOAPEndpoint;
import com.greenstarnetwork.services.controller.soapendpoint.GetResourcesFromCloud;
import com.greenstarnetwork.services.controller.soapendpoint.SetMode;
import com.greenstarnetwork.services.controller.soapendpoint.RefreshPlan;

public class RunController implements Runnable 
{
	private IControllerSOAPEndpoint controller = null;

	public RunController(IControllerSOAPEndpoint controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		controller.getResourcesFromCloud(new GetResourcesFromCloud());
		SetMode sm = new SetMode();
		sm.setArg0(IController.REAL_MODE);
		controller.setMode(sm);
		controller.refreshPlan(new RefreshPlan());
	}

}
