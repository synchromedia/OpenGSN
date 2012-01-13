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
 * Send the show sensor command to the PDU. There a number of different sensors
 * on the device differentiated by the sensor name. Based on the sensor name, 
 * different parts of the model will be populated.
 * 
 * Sensors:
 * -----------
 * Name = Current - RMS Current
 * Name = PwrFactor - Power Factor
 * Name = Max Curr - Maximum Current
 * Name = Act. Power - Active Power
 * Name = Apt. Power - Apparent Power
 * Name = Volt - Voltage
 * 
 * This script supports only the Current and Act. Power sensors
 * 
 * parameters: sensor - The id of the sensor to query
 *            outlet - The outlet the sensor belongs to
 */


var CURRENT = "Current";
var POWER = "Power";

var model = engine.model;

//find the outlet
var outlet = getOutlet(parameters.outlet);
if(outlet == null) {
	throw "Outlet " + parameters.outlet + " not found";
}

//prepare the message and send it to the device
var command = "show " + parameters.sensor;
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};

//send the command to the device
var resp = engine.sendMessageBody(remoteShellMessage);
parseResponse(resp);

function parseResponse (response) {
	var lines = response.split("\n");
	var name = null;
	var reading;
	
	for(var j=0; j<lines.length; j++) {
		var line = lines[j].trim();
		if(line.indexOf("Name is") == 0) { //starts with
			var name = getSensorName(line);
			if(name == null) //Not interested in this sensor 
				break;
		}
		else if(line.indexOf("CurrentReading is ") == 0) {
			var reading = line.substring(new String("CurrentReading is ").length);
			processReading(name, reading);
		}
	}
}

/** 
 * Match the sensor name to a known constant 
 */
function getSensorName(name) {
	if(name.indexOf("Current") != -1)
		return CURRENT;
	else if(name.indexOf("Act. Power") != -1)
	    return POWER;
	
}

/** 
 * Populate the model respectively depending on the sensor type
 */
function processReading(name, reading) {
	log("info", "Outlet Sensor Name: " + name + " Has reading " + reading);
	
	if(name == CURRENT) {
		outlet.load = Number(reading);
	}
	else if(name == POWER) {
		outlet.power = Number(reading);
	}
}
