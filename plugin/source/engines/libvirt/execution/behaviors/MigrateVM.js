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
 * Migrate a VM to another host
 * 
 * Parameters: vmName - The name of the VM to migrate
 *             destHostIP - The IP address of the destination host
 */

var model = engine.model;
var vm = getVM(parameters.vmName);

if(vm == null) {
	throw "Virtual Machine: " + parameters.vmName + " not found";
}

//initialize the command and send it to the RemoteShell Module
var command = "virsh migrate --live " + parameters.vmName + " " + model.hypervisorName + 
					"+ssh://$USER@" + parameters.destHostIP + "/system";
var resp = sendCommand(command);
parseResponse(resp);

function parseResponse (response) {
	//remove the VM from the model if the migration succeeded
	if(response.indexOf("error") != -1)
		throw "command failed. Exception: " + response;
	else
		removeVM(parameters.vmName)
}