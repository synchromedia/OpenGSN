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
 * Get version details about the hypervisor
 *
 * @author Scott Campbell (CRC)
 *
 */
var model = engine.model;

//initialize the command and send it to the RemoteShell Module
var command = "virsh version";
var resp = sendCommand(command);
parseResp(resp);
function parseResp(response) {

	var lines = response.trim().split("\n");

	for(var i = 0; i < lines.length; i++) {
		var fields = lines[i].split(":");

		//skip lines that don't have a ":"
		if(fields.length != 2)
			continue;

		var fieldName = fields[0].trim();
		var fieldValue = fields[1].trim();

		if(fieldName.equals("Running hypervisor")) {
			//split the value again to get the name and version
			var type = fieldValue.substring(0, fieldValue.indexOf(" "));
			if(type == "QEMU")
				model.hypervisorName = "qemu";
			else
				model.hypervisorName = type;
			model.hypervisorVersion = fieldValue.substring(fieldValue.indexOf(" ") + 1, fieldValue.length);
		}
	}
}