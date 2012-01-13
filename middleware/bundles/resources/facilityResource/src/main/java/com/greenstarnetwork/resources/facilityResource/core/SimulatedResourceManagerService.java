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


/**
 * 
 */
public class SimulatedResourceManagerService extends ResourceManagerService {




	public SimulatedResourceManagerService() {

	}
	

	@Override
	public void init() {

	}

	@Override
	public String getResourceIDbyAliase(String aliase) throws Exception {
		if (aliase.equalsIgnoreCase("ETS pdu")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("ETS pdu1")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("ETS pdu2")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("Node pdu")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("Node pdu1")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("Node pdu2")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("crc pdu")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("cybera pdu")){
			return "09da1163-e079-4812-a5f1-333333333333";
		}
		if (aliase.equalsIgnoreCase("crc solar")){
			return "09da1163-e079-4812-a5f1-222222222222";
		}
		if (aliase.equalsIgnoreCase("Node solar")){
			return "09da1163-e079-4812-a5f1-222222222222";
		}
		if (aliase.equalsIgnoreCase("cybera solar")){
			return "09da1163-e079-4812-a5f1-222222222222";
		}
		if (aliase.equalsIgnoreCase("ets climate")){
			return "09da1163-e079-4812-a5f1-111111111111";
		}
		if (aliase.equalsIgnoreCase("Node climate")){
			return "09da1163-e079-4812-a5f1-111111111111";
		}
		if (aliase.equalsIgnoreCase("crc climate")){
			return "09da1163-e079-4812-a5f1-111111111111";
		}
		if (aliase.equalsIgnoreCase("cybera climate")){
			return "09da1163-e079-4812-a5f1-111111111111";
		}
		if (aliase.equalsIgnoreCase("cybera server")){
			return "09da1163-e079-4812-a5f1-000000000000";
		}
		if (aliase.equalsIgnoreCase("ets server1")){
			return "09da1163-e079-4812-a5f1-000000000000";
		}
		if (aliase.equalsIgnoreCase("ets server2")){
			return "09da1163-e079-4812-a5f1-000000000000";
		}
		if (aliase.equalsIgnoreCase("crc server")){
			return "09da1163-e079-4812-a5f1-000000000000";
		}
		
		return null;
	}






}
