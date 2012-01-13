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
 * Initialze the libvirt (VMM) model by calling the series of commands to
 * get the required information from the host server
 */

var model = engine.model
var parameters = new Object();

//Get information about the host
run("source/engines/libvirt/execution/behaviors/NodeInfo.js");
run("source/engines/libvirt/execution/behaviors/Version.js");
run("source/engines/libvirt/execution/behaviors/HostRunningInfo.js");

//Get the list of active VMs
listVMs();

//loop over each VM to get more information about them
for(var x = 0; x < model.vms.length; x++) {
	parameters.vmName = model.vms[x].name;
	run("source/engines/libvirt/execution/behaviors/GetVMInfo.js");
}

function listVMs() {

	//Initialize the VMs array.
    engine.model.vms = new Array();

	var command = "virsh -c qemu:///system list --all";
	var response = sendCommand(command);

	var lines = response.split("\n");

	var startData = false;
	for(var i = 0; i < lines.length; i++) {
		try {
			var line = lines[i].trim();
			if(line.indexOf("-------") == 0) {
				startData = true;
				continue;
			}

			if(startData) {
				var fields = line.split(" ");
				var name = fields[1].trim();
				vm = new Object();
				vm.name = name;

				model.vms.push(vm);
			}
		} catch(e) {
			//skip lines that cause parsing issues.
			continue;
		}
	}

}