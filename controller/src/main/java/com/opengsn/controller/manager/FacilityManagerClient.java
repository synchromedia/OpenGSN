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
package com.opengsn.controller.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greenstarnetwork.services.facilitymanager.FacilityManagerException_Exception;
import com.greenstarnetwork.services.facilitymanager.IFacilityManager;
import com.opengsn.controller.tools.Logger;
import com.opengsn.services.facilitymanager.model.FacilityModel;
/**
 * Handling a list of Facility Managers
 * @author knguyen
 *
 */
public class FacilityManagerClient {

	//controller state logger
	private Logger log = Logger.getLogger();
	
	IFacilityManager facilityMgr = null;
	
	public FacilityManagerClient() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		           new String[]{"webapp/WEB-INF/applicationContext.xml"});		
		facilityMgr = (IFacilityManager) context.getBean("facilityManagerClient");
	}

	/**
	 * Get Facility Models of a given Facility Manager
	 * @return
	 */
	public List<FacilityModel> getFacilityList() 
	{
	   List<FacilityModel> ret = null;
		try {
			List<String> xmlModels = facilityMgr.listAllFacilities();
			if (xmlModels != null) {
				ret = new ArrayList<FacilityModel>();
				Iterator<String> it = xmlModels.iterator();
				while (it.hasNext()) {
					FacilityModel m = FacilityModel.fromXML((String)it.next());
					ret.add(m);
				}
			}
		} catch (FacilityManagerException_Exception e) {
			log.debug(e.toString());
		}
		return ret;
	}
}
