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
package com.greenstarnetwork.services.facilitymanager;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

/**
 * FacilityManager function definitions
 * 
 * @author Kim Nguyen (ETS)
 * @author Mathieu Lemay (IT)
 * 
 */
@WebService
public interface IFacilityManagerProvider {

	/**
	 * Retrieve the model of a resource (in JSON format)
	 * 
	 * @param type
	 * @param id
	 * @return model of the first resource that meets type and id, regardless
	 *         its domain
	 * @throws FacilityManagerException
	 */
	public String readModel(String id);

	/**
	 * Retrieve the model of a resource (in JSON format)
	 * 
	 * @param type
	 * @param id
	 * @return model of the first resource that meets type and id, regardless
	 *         its domain
	 * @throws FacilityManagerException
	 */
	public List<String> listResourcesByType(String type);

	/**
	 * Execute a behavior on an engine
	 * 
	 * @param engineId
	 *            id of the resource
	 * @param executionName
	 *            name of the action to execute
	 * @param parameters
	 *            parameters used by the action
	 * @return Action Result
	 */
	public String execute(String engineId, String executionName, Map<String,String> parameters) throws FacilityManagerException;

	/**
	 * Return the archive in a period of time
	 * 
	 * @param startDate
	 * @param endDate
	 * @param resourceID
	 * @return
	 */
	public String getMetricsDataByRangDate(String startDate, String endDate, String metricsID);
}
