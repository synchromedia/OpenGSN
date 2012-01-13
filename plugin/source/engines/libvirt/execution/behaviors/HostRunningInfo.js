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
 * Use the top command to get the runtime information about the host
 */

var model = engine.getModel();

//initialize the command and send it to the RemoteShell Module
var command = "top -n 1";
var resp = sendCommand(command);
parseResponse(cleanResp(resp));

function parseResponse(response) {

	var lines = response.trim().split("\n");

	for(var i = 0; i < lines.length; i++) {
		if(lines[i].indexOf("Cpu(s)") != -1)
			parseCpuTime(lines[i]);
		else if(lines[i].indexOf("Mem") != -1)
			parseMemory(lines[i]);
		else if(lines[i].indexOf("Swap") != -1)
			parseSwap(lines[i]);
		else if(lines[i].indexOf("PID") != -1)
			break;
	}
}

/**
 * parse the line that contains the CPU runtime information
 *
 * @param line
 */
function parseCpuTime(line) {
	//split the line twice, removing the line header
	var fields = line.split(":")[1].split(",");
	//field = us
	model.cpuUserTime = new Number(fields[0].substring(0, fields[0].indexOf("%")).trim());
	//field = sy
	model.cpuSystemTime = new Number(fields[1].substring(0, fields[1].indexOf("%")).trim());
	//field = ni
	model.cpuNiceTime = new Number(fields[2].substring(0, fields[2].indexOf("%")).trim());
	//field = id
	model.cpuIdleTime = new Number(fields[3].substring(0, fields[3].indexOf("%")).trim());
	//field = wa
	model.cpuIOTime = new Number(fields[4].substring(0, fields[4].indexOf("%")).trim());
	//field = hi
	model.cpuHardwareTime = new Number(fields[5].substring(0, fields[5].indexOf("%")).trim());
	//field = si
	model.cpuSoftwareTime = new Number(fields[6].substring(0, fields[6].indexOf("%")).trim());
	//field = st
	model.cpuStealTime = new Number(fields[6].substring(0, fields[7].indexOf("%")).trim());
}

/**
 * parse the line that contains the Memory runtime information
 *
 * @param line
 */
function parseMemory(line) {
	//split the line twice, removing the line header
	var fields = line.split(":")[1].split(",");

	for(var i = 0; i < fields.length; i++) {
		if(fields[i].indexOf("free") != null)
			model.freeMemory = new Number(fields[i].substring(0, fields[i].indexOf("k")).trim());
		else if(field[i].indexOf("buffers") != null)
			model.bufferMemory = new Number(fields[i].substring(0, fields[i].indexOf("k")).trim());
	}
}

/**
 * parse the line that contains the Swap Memory runtime information
 *
 * @param line
 */
function parseSwap(line) {
	//split the line twice, removing the line header
	var fields = line.split(":")[1].split(",");

	for(var i = 0; i < fields.length; i++) {
		if(fields[i].indexOf("total") != -1)
			model.totalSwapMemory = new Number(fields[i].substring(0, fields[i].indexOf("k")).trim());
		else if(fields[i].indexOf("free") != -1)
			model.freeSwapMemory = new Number(fields[i].substring(0, fields[i].indexOf("k")).trim());
		else if(fields[i].indexOf("cached") != -1)
			model.cachedSwapMemory = new Number(fields[i].substring(0, fields[i].indexOf("k")).trim());
	}
}


/** 
 * The response from the server has a number of unwanted characters such as [m, [K. This method will 
 * remove those.
 */
function cleanResp(response) {
	var patt = /(\[m|\[K)/gm;
	var clean = response.replace(patt,"");
	
	return clean;
}
