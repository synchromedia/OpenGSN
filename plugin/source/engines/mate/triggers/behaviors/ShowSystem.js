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
 * Send the show <system> command to the PDU. This will initialize the oulets
 *  and infeeds lists. The outlets are identified by "outlet". Infeeds are "nvoltsensor"s.
 * 
 */


var model = engine.model;

if(model.outlets == null) {
	model.outlets = new Array;
}
if(model.infeeds == null) {
	model.infeeds = new Array;
}
if(model.sensors == null) {
	model.sensors = new Array();
}

var command = "show system1";

//prepare the message and send it to the device
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};
//send the command to the device
var resp = engine.sendMessageBody(remoteShellMessage);
parseResponse(resp);

function parseResponse(response) {
	var lines = response.split("\n");
	
	//Don't start parsing until we've seen the string "Targets:"
	var parse = false;
	for(var i=0; i<lines.length; i++) {
		var line=lines[i].trim();
		if(line.indexOf("Targets:") != -1)
			parse = true;
		if(parse) {
			//treat outlets
			if(line.indexOf("outlet") != -1) {
				var outlet = getOutlet(line);
				var newOutlet = false;
				if(outlet == null) {
					outlet = new Object();
					newOutlet = true;
				}
				outlet.id = line;
				outlet.type = "outlet";
				
				if(newOutlet)
					model.outlets.push(outlet);
			}
			else if(line.indexOf("nvoltsensor") != -1) {
				var infeed = getInfeed(line);
				var newInfeed = false;
				if(infeed == null) {
					infeed = new Object();
					newInfeed = true;
				}
				infeed.id = line;
				infeed.type = "infeed";
				
				if(newInfeed)
					model.infeeds.push(infeed);
			}

			//Humidity and Temperature Sensors.
			//Note not all sensors ports have sensors on them. They will
			//be filtered out by the ShowExternalSensor command
			else if(line.indexOf("externalsensor") != -1) {
				var sensor = getSensor(line);
				var newSensor= false;
				if(sensor == null) {
					sensor = new Object();
					newSensor = true;
				}
				sensor.id = line;
				
				if(newSensor) {
					model.sensors.push(sensor);
				}
			}
		}
	}
}
