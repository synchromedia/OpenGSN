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
 * Send the show sensor command to the PDU on an external sensor. External sensors
 * measure things like temperature and humidity
 *
 * This script supports only the Current and Act. Power sensors
 *
 * parameters: sensor - The id of the sensor to query
 */

var TEMP = "Temperature";
var HUM = "Humidity";

var sensorId = parameters.sensor;

var sensor;
for(var index = 0; index < engine.model.sensors.length; index++) {
	if(engine.model.sensors[index].id == sensorId) {
		sensor = engine.model.sensors[index];
		break;
	}
}
if(sensor == null) {
	log("info", "Sensor " + parameters.sensor + " not found");
	throw "Sensor " + parameters.sensor + " not found";
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

function parseResponse(response) {
	//Check if there is a sensor attached
	var line = response.match("Sensor is available");

	if(line == null) {
		//no sensor connected. remove it from the sensors list
		// engine.model.sensors.splice(index, 1);
		engine.model.sensors[index].remove=true;
	} else {

		if(response.match(TEMP) != null)
			sensor.type = TEMP;
		else if(response.match(HUM) != null)
			sensor.type = HUM;

		//grab the reading
		var line = response.match(/CurrentReading is.*\n/);
		var reading = line[0].trim().substring(new String("CurrentReading is ").length);
		sensor.reading = new Number(reading);

		//grab the name
		var line = response.match(/\bName is.*\n/);
		sensor.name = line[0].trim().substring(new String("Name is ").length);
	}
}