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
 * Get computing and memory information from the host. Expected output formatted
 * is:
 *
 * virsh nodeinfo CPU model: x86_64
 * CPU(s): 16 CPU frequency: 2394 MHz
 * CPU socket(s): 4
 * Core(s) per socket: 4
 * Thread(s) per core: 1
 * NUMA cell(s): 1
 * Memory size: 49553192 kB
 *
 * @author Scott Campbell (CRC)
 *
 */
var model = engine.model;

//initialize the command and send it to the RemoteShell Module
var command = "virsh nodeinfo";
var resp = sendCommand(command);
parseResponse(resp);
function parseResponse(response) {

	var lines = response.trim().split("\n");

	for(var i = 0; i < lines.length; i++) {
		var fields = lines[i].split(":");

		//skip lines that don't have a ":"
		if(fields.length != 2)
			continue;

		var fieldName = fields[0].trim();
		var fieldValue = fields[1].trim();

		if(fieldName.equals("CPU model"))
			model.cpu = fieldValue;
		else if(fieldName.equals("CPU(s)")) {
			model.numCpus = new Number(fieldValue);
			// set the maximum number of VMs be the number of CPUs
			model.maxVMs = new Number(fieldValue);
		} else if(fieldName.equals("CPU frequency")) {
			// Crop the units off the end of the line.
			var str = fieldValue.substring(0, fieldValue.indexOf(" "));
			model.cpuSpeed = new Number(str);
		} else if(fieldName.equals("CPU socket(s)"))
			model.cpuSockets = new Number(fieldValue);
		else if(fieldName.equals("Core(s) per socket"))
			model.coresPerSocket = new Number(fieldValue);
		else if(fieldName.equals("Thread(s) per core"))
			model.threadsPerCore = new Number(fieldValue);
		else if(fieldName.equals("NUMA cell(s)"))
			model.numaCells = new Number(fieldValue);
		else if(fieldName.equals("Memory size")) {
			var str = fieldValue.substring(0, fieldValue.indexOf(" "));
			model.totalMemory = new Number(str);
		}
	}
}