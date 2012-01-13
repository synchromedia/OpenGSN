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
 * Send the Off command to the PDU to turn off the specified outlet
 * 
 * parameters: outlet - the outlet to set
 * 			  powerState = the state to set. On or Off
 */

var model = engine.model;

//find the outlet
var outlet = getOutlet(parameters.outlet);
if(outlet == null) {
	throw "Outlet " + parameters.outlet + " not found";
}

//prepare the message and send it to the device
var command = "set " + parameters.outlet + " powerState=" + parameters.powerState;
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};

//send the command to the device
var resp = engine.sendMessageBody(remoteShellMessage);
parseResponse(resp);

//parse the response
function parseResponse(response) {
	line = resp.match(/powerState\s.*\n/);
	head = new String("powerState is").length + 1;
	field = line[0].substring(head, line[0].length);
	outlet.state = field.trim();
}