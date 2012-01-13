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
 * Initialize the Raritan Model. This script will call a number
 * of commands to get all the information needed from the PDU
 *
 */

var model = engine.model
var parameters = new Object();

//Execute the ShowSystem behavior
run("source/engines/raritan/execution/behaviors/ShowSystem.js");

//Get the infeeds first.
for(var i = 0; i < model.infeeds.length; i++) {
	parameters.sensor = model.infeeds[i].id;
	run("source/engines/raritan/execution/behaviors/ShowVoltSensor.js");
}

//Get the outlets.
for(var i = 0; i < model.outlets.length; i++) {
	parameters.outlet = model.outlets[i].id;
	run("source/engines/raritan/execution/behaviors/ShowOutlet.js");
}

//Get the temperature and humidity readings
for(var i = 0; i < model.sensors.length; i++) {
	parameters.sensor = model.sensors[i].id;
	run("source/engines/raritan/execution/behaviors/ShowExternalSensor.js");
}

//prune out the unconnected environment sensors
model.sensors = pruneSensors(model.sensors);

//Sum the outlets total current (or load) and the total power for the main model
var totalCurrent = new Number();
var totalPower = new Number();
for(var i = 0; i < model.outlets.length; i++) {
	totalCurrent += model.outlets[i].load;
	totalPower += model.outlets[i].power;
}
model.current = totalCurrent;
model.totalPower = totalPower;

//Get the voltage from the infeed. The first valid infeed is all we need.
for(var i = 0; i < model.infeeds.length; i++) {
	if(model.infeeds[i].voltage != null) {
		model.voltage = model.infeeds[i].voltage;
		break;
	}
}


function pruneSensors(array) {
	var newArray = new Array();
	
	for(var i=0; i<array.length; i++) {
		if(array[i].remove != true) {
			newArray.push(array[i]);
		}
	}
	return newArray;
}
