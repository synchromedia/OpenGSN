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
package com.greenstarnetwork.services.domainManger.util;

import com.greenstarnetwork.services.cloudManager.ICloudManager;
import com.greenstarnetwork.services.domainManager.core.Activator;
import com.iaasframework.resources.core.RegistryUtil;
import com.greenstarnetwork.services.facilityManager.IFacilityManager;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 *Utility class used to load GSN managers as services
 *
 */
public class ManagerUtil {
	
	/**
	 * Cloud manager service
	 */
	private ICloudManager cloudMger;
	
	/**
	 * Facility manager service
	 */
	private IFacilityManager facilityMger;
	
	public ManagerUtil(){
	}
	
	/**
	 * Load facility manager and cloud manager
	 */
	public void loadManagers(){
		loadCloudManager();
		loadFacilityManager();
	}
	
	/**
	 * 
	 * @return Cloud manager
	 */
	public ICloudManager getCloudManager() {
		return cloudMger;
	}

	public void setCloudManager(ICloudManager cloudMger) {
		this.cloudMger = cloudMger;
	}

	/**
	 * 
	 * @return Facility manager
	 */
	public IFacilityManager getFacilityManager() {
		return facilityMger;
	}

	public void setFacilityManager(IFacilityManager facilityMger) {
		this.facilityMger = facilityMger;
	}
	
	private void loadCloudManager(){
		try {
			cloudMger = (ICloudManager) RegistryUtil.getServiceFromRegistry(
					Activator.getContext(), ICloudManager.class.getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadFacilityManager(){
		try {
			facilityMger = (IFacilityManager) RegistryUtil.getServiceFromRegistry(
					Activator.getContext(), IFacilityManager.class.getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
