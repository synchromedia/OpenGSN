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
package com.greenstarnetwork.services.domainManager.shell;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.services.cloudManager.ICloudManager;
import com.greenstarnetwork.services.domainManger.util.ManagerUtil;
import com.greenstarnetwork.services.facilityManager.IFacilityManager;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
@Command(scope = "gsn", name = "loadDomains", description="Force managers to load/reload domains")
public class LoadDomainsShellCommand extends OsgiCommandSupport {

	private ManagerUtil managers;
	
	@Override
	protected Object doExecute() throws Exception {
		System.out.println("Executing loadDomains Command...");
		
		managers = new ManagerUtil();
		managers.loadManagers();
		
		ICloudManager cloudMger = managers.getCloudManager();
		IFacilityManager facilityMger = managers.getFacilityManager();
		
		
		if(cloudMger != null && facilityMger != null){
			cloudMger.initDomains();
			facilityMger.initDomains();
		}
		
		System.out.println("End of loadDomains Command.");
		return null;
	}

}
