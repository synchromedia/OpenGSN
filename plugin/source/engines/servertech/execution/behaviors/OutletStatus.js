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
 * Send the OStat command to the PDU to collect the status information
 * about all of the outlets on the PDU
 */

var model = engine.model;
var outlets = model.outlets;

var command = "OStat";
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};
var resp = engine.sendMessageBody(remoteShellMessage);
parseResp(resp);

function parseResp(response) {

	if(outlets == null) {
		outlets = new Array();
		model.outlets = outlets;
	}

	var lines = response.trim().split("\n");
	var oCount = 0;

	for( i = 0; i < lines.length; i++) {
		if(lines[i].trim().length == 0)
			continue;
		if(lines[i].trim().charAt(0) != '.')
			continue;
		fields = lines[i].trim().split(/[\s]+/);

		var outlet;
		var newOutlet = false;

		if(model.outlets[oCount] != null)
			outlet = model.outlets[oCount];
		else {
			outlet = new Object();
			newOutlet = true;
		}

		//populate the model
		outlet.id = fields[0];
		outlet.name = fields[1]
		outlet.status = fields[2];
		outlet.load = Number(fields[3]);
		outlet.voltage = Number(fields[4]);
		outlet.power = Number(fields[5]);
		outlet.type = "outlet";

		if(newOutlet)
			outlets[oCount] = outlet;
		oCount++;
	}
}