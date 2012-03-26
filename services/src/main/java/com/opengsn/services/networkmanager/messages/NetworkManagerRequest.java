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
package com.opengsn.services.networkmanager.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NetworkManagerRequest implements Serializable {
	public static String CMD_assignVMAddress = "assignVMAddress";
	public static String CMD_retrieveVMAddress = "retrieveVMAddress";
	public static String CMD_releaseAssignment = "releaseAssignment";

	private String commandId;
	private Map<String, String> arguments;

	public NetworkManagerRequest() {
		commandId = "None";
		arguments = new HashMap<String, String>();
	}
	public NetworkManagerRequest(String command, Map<String,String> args) {
		commandId = command;
		arguments = args;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public Map<String, String> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, String> arguments) {
		this.arguments = arguments;
	}

}
