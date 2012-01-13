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
 * Send the Show outlet command to get information about the outlet and
 * it's associated sensors
 *
 * parameters: outlet - the id of the outlet to query
 */
var model = engine.model;

var outlet = getOutlet(parameters.outlet);
if(outlet == null) {
	throw "Outlet " + parameters.outlet + " not found";
}

//prepare the message and send it to the device
var command = "show " + parameters.outlet;
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};

//send the command to the device
var resp = engine.sendMessageBody(remoteShellMessage);
var response = resp;
var lines = response.split("\n");

for(var k = 0; k < lines.length; k++) {
	var line = lines[k].trim();
	if(line.indexOf("Name is") != -1)
		outlet.name = line.substring(new String("Name is ").length);
	else if(line.indexOf("powerState is") != -1) {
		var state = line.substring(new String("powerState is ").length, line.indexOf("(")).trim();
		if(state == "1")
			outlet.state = "On";
		else if(state == "2" || state == "0")
			outlet.state = "Off";
	} else if(line.indexOf("CIM_AssociatedSensor") != -1) {
		//look in some of the associated  sensors for specific values
		var sensors = line.split("=>");
		var sensor = sensors[1].trim();
		if((sensor.indexOf("nsensor") != -1) || (sensor.indexOf("ncurrsensor") != -1)) {
			log("info", "Getting information about sensor: " + sensor);
			//run the ShowOutletSensor command to get the current
			parameters.sensor = sensor;
			run("source/engines/raritan/execution/behaviors/ShowOutletSensor.js");
		}
	}
}
//populate the voltage field
for(var k = 0; k < model.infeeds.length; k++) {
	if(model.infeeds[k].voltage != null) {
		outlet.voltage = model.infeeds[k].voltage;
		break;
	}
}