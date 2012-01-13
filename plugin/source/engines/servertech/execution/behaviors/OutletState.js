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
 * Show outlet state. This command only looks at the state control field for
 * each outlet. The rest of the outlet information is handled by the OutletStatus
 * behavior
 */

var model = engine.model
var outlets = model.outlets
var command = "Status";
var remoteShellMessage = {
	remoteShellRequest : {
		command : command
	}
};
var resp = engine.sendMessageBody(remoteShellMessage);
parseResponse(resp);

function parseResponse(response) {
	var lines = response.trim().split("\n");
	var oCount = 0;

	for( i = 0; i < lines.length; i++) {
		if(lines[i].trim().charAt(0) != '.')
			continue;
		var fields = lines[i].trim().split(/[\s]+/);

		var outlet = null;
		var newOutlet = false;

		if(model.outlets[oCount] != null)
			outlet = model.outlets[oCount];
		else {
			outlet = new Object();
			outlet.id = fields[0];
			newOutlet = true;
		}

		//The value we need starts at character 70
		state = lines[i].trim().substring(66, lines[i].length);
		//populate the model
		outlet.controleState = state;

		if(newOutlet)
			outlets[oCount] = outlet;
		oCount++;
	}

	model.outlets = outlets;

}