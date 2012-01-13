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
 * Get the infeed status of the PDU. 
 */

var model = engine.model;
var infeeds = model.infeeds;
var command = "IStat";
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};
var resp = engine.sendMessageBody(remoteShellMessage);
parseResponse(resp)

function parseResponse (response) {
	if(infeeds == null) {
		infeeds = new Array();
		model.infeeds = infeeds;
	}

	var lines = response.trim().split("\n");
	var count = 0;

	for(var i = 0; i < lines.length; i++) {
		if(lines[i].trim().charAt(0) != '.')
			continue;
		fields = lines[i].trim().split(/[\s]+/);

		var infeed;
		var newInfeed = false;

		if(model.infeeds[count] != null)
			infeed = model.infeeds[count];
		else {
			infeed = new Object();
			newInfeed = true;
		}

		//populate the model
		infeed.id = fields[0];
		infeed.name = fields[1]
		infeed.status = fields[2];
		infeed.load = Number(fields[3]);
		infeed.voltage = Number(fields[4]);
		infeed.power = Number(fields[5]);
		infeed.type = "infeed";

		if(newInfeed)
			infeeds[count] = infeed;
		count++;
	}
}