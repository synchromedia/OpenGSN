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
package com.greenstarnetwork.resources.facilityResource.core;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.powersource.PowerSourceModel;

public class SimulatedOPSpecsCalculator extends OPSpecsCalculator{

	
	private FacilityResourceToFromFile facilityResourceToFromFile = null;
	
	public SimulatedOPSpecsCalculator(FacilityModel facilityModel) {
		super(facilityModel);
	}

	
	@Override
	public PowerSourceModel getPowerSourceModel(String resourceID) throws Exception {
		PowerSourceModel powerSourceModel = (PowerSourceModel)facilityResourceToFromFile.readPowerSourceModelFromFileByID(resourceID);
		return powerSourceModel;

	}

	@Override
	public void init() throws Exception {
		facilityResourceToFromFile = new FacilityResourceToFromFile();
	}

	@Override
	public PDUModel getPDUModel(String resourceID) throws Exception {
		PDUModel pduModel = (PDUModel)facilityResourceToFromFile.readPDUModelFromFileByID(resourceID);
		return pduModel;
		
	}

	
	
	
}
