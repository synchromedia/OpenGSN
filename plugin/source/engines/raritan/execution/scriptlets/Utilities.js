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
function getOutlet(id) {
	var outlet = null;
	for(var i=0; i<model.outlets.length; i++) {
		if(model.outlets[i].id.equals(id)) {
			outlet = model.outlets[i];
			break;
		}
	}
	return outlet;
}

function getInfeed(id) {
	var infeed = null;
	for( var i=0; i<model.infeeds.length; i++) {
		if(model.infeeds[i].id.equals(id)) {
			infeed = model.infeeds[i];
			break;
		}
	}
	return infeed;
}

function getSensor(id) {
	var sensor = null;
	for( var i=0; i<model.sensors.length; i++) {
		if(model.sensors[i].id.equals(id)) {
			sensor = model.sensors[i];
			break;
		}
	}
	return sensor;
}
