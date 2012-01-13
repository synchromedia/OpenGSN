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
 * This script will populate the vms model with all of the necessary information.
 * It will call the DomInfo.js behavior and also grab the additional context data from
 * the xml config file. The VM must exist in the model before running this behavior
 * 
 * Parameters: vmName - The name of the VM to populate
 */

var model = engine.model

var vm = getVM(parameters.vmName);
if(vm == null) {
	throw "Virtual Machine " + parameters.vmNane + " not found";
}

//run the domInfo behavior to query the VM and populate the model
run("source/engines/libvirt/execution/behaviors/DomInfo.js");

getVmContext();

/**
 * Get details about the VM that are not available from the dominfo command.
 * This function will grab the VM's xml context file to get the information
 */
function getVmContext() {
	var xmlFile = model.rootStoragePath + "/" + parameters.vmName + ".xml";

	var command = "cat " + xmlFile;
	var resp = sendCommand(command);
	var lines = resp.split("\n");
	var diskSource=false;
	for(var i = 0; i < lines.length; i++) {
		var str = lines[i];


		if(str.indexOf("disk") != -1 && str.indexOf("device='disk'") != -1) {
			diskSource = true;
		} else if((str.indexOf("<source file=") != -1) && (diskSource)) {
			vm.image = str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
			diskSource = false;
		} else if(str.indexOf("<mac") != -1) {
			vm.mac = str.substring(str.indexOf("\'") + 1, str.lastIndexOf("\'"));
		} else if(str.indexOf("<ip") != -1) {
			vm.ip = str.substring(str.indexOf(">") + 1, str.lastIndexOf("<"));
		}
	}
}