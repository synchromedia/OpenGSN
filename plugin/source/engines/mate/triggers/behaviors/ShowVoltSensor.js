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
 * Voltage Sensors (nvoltsensor) are the infeeds to the PDU.
 * This behavior will send the show sensor command on a voltage sensor to get
 * the status and voltage of the sensor
 * 
 * parameters: sensor - the id of the sensor to query
 */

var model = engine.model;

//find the outlet
var infeed = getInfeed(parameters.sensor);
if(infeed == null) {
	throw "Infeed " + parameters.sensor + " not found";
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
	infeed.type = "infeed";
	
	for(var i=0; i<lines.length; i++) {
		line = lines[i].trim();
		if(line.indexOf("Name is") == 0) { //starts with
			infeed.name = line.substring(new String("Name is ").length);	
		}
		else if(line.indexOf("CurrentState is ") == 0) {
			if(line.substring(new String("CurrentState is ").length).trim().equals("OK"))
				infeed.status = "On";
			else
				infeed.status = "Off";
		}
		else if(line.indexOf("CurrentReading is ") == 0) {
			var voltage = line.substring(new String("CurrentReading is ").length);
			infeed.voltage = Number(voltage);
		}
	}
}