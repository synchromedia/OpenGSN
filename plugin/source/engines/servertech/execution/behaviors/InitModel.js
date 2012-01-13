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
 * Initialize the Servertech Model. This script will call a number
 * of commands to get all the information needed from the PDU
 *
 */

var model = engine.model

//Execute the  behaviors to populate the model
run("source/engines/servertech/execution/behaviors/InfeedStatus.js");
run("source/engines/servertech/execution/behaviors/OutletStatus.js");
run("source/engines/servertech/execution/behaviors/OutletState.js");

//Sum the outlets total current (or load) and the total power for the main model
var totalCurrent = new Number();
var totalPower = new Number();
for(var i = 0; i < model.outlets.length; i++) {
	totalCurrent += outlets[i].load;
	totalPower += outlets[i].power;
}
model.current = totalCurrent;
model.totalPower = totalPower;

//Get the voltage from the infeed. The first valid infeed is all we need.
for(var i = 0; i < model.infeeds.length; i++) {
	if(infeeds[i].voltage != null) {
		model.voltage = infeeds[i].voltage;
		break;
	}
}