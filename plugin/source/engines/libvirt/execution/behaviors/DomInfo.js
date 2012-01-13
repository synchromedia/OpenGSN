/*
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
/**
 * Parse the virsh dominfo command to populate the model with the acquired information
 *
 * parameters: vmName = The name of the VM to query
 *
 * @author Scott Campbell (CRC)
 *
 */
var model = engine.model;

var vm = getVM(parameters.vmName);
if(vm == null) {
	throw "Virtual Machine " + parameters.vmNane + " not found";
}

//initialize the command and send it to the RemoteShell Module
var command = "virsh dominfo " + parameters.vmName;
var resp = sendCommand(command);
parseResponse(resp);

function parseResponse(response) {
	if(response.indexOf("error") != -1)
		throw "command failed. Exception: " + response;

	var lines = response.trim().split("\n");

	for(var i = 0; i < lines.length; i++) {
		var fields = lines[i].split(":");

		//skip lines that don't have a ":"
		if(fields.length != 2)
			continue;

		var fieldName = fields[0].trim();
		var fieldValue = fields[1].trim();

		if(fieldName.equals("Id"))
			vm.id = fieldValue;
		else if(fieldName.equals("UUID"))
			vm.uuid = fieldValue;
		else if(fieldName.equals("OS Type"))
			vm.os = fieldValue;
		else if(fieldName.equals("State"))
			vm.state = setVmState(fieldValue);
		else if(fieldName.equals("CPU(s)"))
			vm.vcpu = new Number(fieldValue);
		else if(fieldName.equals("CPU time"))
			vm.clock = fieldValue;
		else if(fieldName.equals("Max memory"))
			vm.memory = new Number(fieldValue.substring(0, fieldValue.indexOf(" ")));
		else if(fieldName.equals("Used memory"))
			vm.currentMemory = new Number(fieldValue.substring(0, fieldValue.indexOf(" ")));
	}
}

function setVmState(fieldValue) {
	var state;
	
	if(fieldValue.equals("running")) {
		state = STARTED;
	} else {
		if(fieldValue.equals("paused"))
			state = PAUSED;
		else if(fieldValue.equals("Shut off"))
			state = STOPPED;
	}
	return state;
}