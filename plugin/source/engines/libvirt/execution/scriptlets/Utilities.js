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
 * Some utility functions
 */


//VM State constants
var CREATING = "CREATING";
var MIGRATING = "MIGRATING";
var PAUSED = "PAUSED";
var SHUTDOWN = "SHUTDOWN";
var STARTED = "STARTED";
var STOPPED = "STOPPED";
var UNKNOWN = "UNKNOWN";

/**
 * Search the model for a VM with the given name. Return null
 * if the VM was not found
 */
function getVM(vmName) {
	var vm = null;
	for(var i = 0; i < model.vms.length; i++) {
		if(model.vms[i].name == vmName) {
			vm = model.vms[i];
			break;
		}
	}
	return vm;
}

/**
 * Remove an item from the list of VMs in the model
 */
function removeVM(name) {
    var index = -1;
	for(var i = 0; i < model.vms.length; i++) {
		if(model.vms[i].name == name) {
			index = i;
			break;
		}
	}
	if(index != -1) {
		log("info", "removing VM " + model.vms[index].name + " from model");
		model.vms.splice(index, 1);
	}
}

/**
 * Do a check that the Host has available VM space
 */
function checkHostHasVmSpace() {
	var curVMs = 0;

	if(model.vms != null)
		curVMs = model.vms.length;

	if(curVMs >= model.vms.maxVMs)
		return false;
	else
		return true;
}

/**
 * send a command to the device and return the response
 */
function sendCommand(cmd) {
	var remoteShellMessage = {
		remoteShellRequest : {
			command : cmd
		}
	};
	//log("info", "Sending Command: " + cmd);
	var resp = engine.sendMessageBody(remoteShellMessage);
	// log("info", "Received Response: " + resp);
	return resp;
}
