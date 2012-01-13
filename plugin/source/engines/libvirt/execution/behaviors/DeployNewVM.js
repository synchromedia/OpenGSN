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
/*
 * Carry out the various operations that are required to create a new Virtual
 * Machine.
 *
 * Sequence
 *
 *  1. CopyTemplate - Copy the given VM template.
 *  2. CreateXML - create the XMl config file that contains the VM's context
 *  3. StartVM - virsh create <vm xml file>. This will create and start the VM from the xml file.
 *  4. DomInfo - virsh dominfo <vm>.  query the Vm to populate the model
 *
 * Parameters: vmName - the name of the VM to create
 *  		  template - the name of the template file to use without the path or file extension
 *   		  memory - the amount of memory to allocate to the VM
 * 			  numCpus - the number of cores to allocate to the VM
 * 			  ipAddress - the IP address to assign to the VM
 * 			  mac - the MAC address to assign to the VM
 */

var model = engine.model;

if(!verifyParams) {
	var required = "vmName, templateName, memory, numCpus, ipAddress, mac";
	throw "Invalid parameters to perform action. \nRequired parameters: " + required + ". \nReceived: " + JSON.stringify(parameters);
}
//make sure the host has space before trying to create the VM
if(!checkHostHasVmSpace())
	throw "Maximum number of VMs reached. No more VM can be created!";

var vmTemplateImage = model.rootStoragePath + "/" + parameters.template + ".qcow2";
var vmDiskImage = model.rootStoragePath + "/" + parameters.vmName + ".qcow2";
var xmlFile = model.rootStoragePath + "/" + parameters.vmName + ".xml";
var xmlTemplateFile = model.rootStoragePath + "/" + parameters.template + ".xml";

// try {
	
copyTemplate();
createXmlConfig();
createVM();
domInfo();

// }catch(e) {
// log("info", e);
// throw e;
// }

/*
 * Create a copy of the template for the new VM
 */
function copyTemplate() {

	var command = "cp " + vmTemplateImage + " " + vmDiskImage;
	var response = sendCommand(command);

	if(response.indexOf("error") != -1)
		throw "command: " + command + " FAILED. Exception: " + response;
}

/*
 * Create the XML Config file that contains the VM context
 */
function createXmlConfig() {

	var xmlConfig = createContext();

	//initialize the command and send it to the RemoteShell Module
	var command = "echo \"" + xmlConfig + "\" > " + xmlFile;
	var response = sendCommand(command);

	if(response.indexOf("error") != -1)
		throw "command failed. Exception: " + response;
	else {
		log("info", "XML config created");
	}
}

/**
 * Using the parameters provided to this script, grab the XML config template from
 * the filesystem and replace the configuration details from the template to
 * what is given for this VM. Return the XML file as a String
 */
function createContext() {

	var command = "cat " + xmlTemplateFile;
	var resp = sendCommand(command);
	var newConfig = new String();
	var diskSource = false;
	var record = false;
	var lines = resp.split("\n");
	for(var i = 0; i < lines.length; i++) {
		var str = lines[i];
		
		//Only keep the config between the domain tags. This will prune off the cat command at the top of the config
		// and the prompt at the bottom.
		if(str.indexOf("<domain") != -1)
			record = true;
		else if(str.indexOf("</domain") != -1) {
			
			//Add the <gsn> tag with with context required for the model and put it just above the closing </domain> tag 
			//in the config file. This is needed so the context can be put back in the model when the vm is restarted
			var gsnContext = createGSNContext()
			
			newConfig += gsnContext + str;
			record = false;
		}
			
		if(str.indexOf("<name>") != -1) {
			var newStr = str.replace(/<name>.*<.name>/, "<name>" + parameters.vmName + "</name>");
		} else if(str.indexOf("<memory>") != -1) {
			var newStr = str.replace(/<memory>.*<.memory>/, "<memory>" + parameters.memory + "</memory>");
		} else if(str.indexOf("<vcpu>") != -1) {
			var newStr = str.replace(/<vcpu>.*<.vcpu>/, "<vcpu>" + parameters.numCpus + "</vcpu>");
		} else if(str.indexOf("disk type='file' device='disk'" ) != -1) {
			var newStr = str;
			diskSource = true;
		} else if((str.indexOf("<source") != -1) && (diskSource)) {
			var newStr = str.replace(/<source file=.*>/, "<source file='" + vmDiskImage + "'/>");
			diskSource = false;
		} else if(str.indexOf("<mac") != -1) {
			var newStr = str.replace(/<mac address=.*>/, "<mac address='" + parameters.mac + "'/>");
		} else
			newStr = str;
		if(record) 
			newConfig += newStr;
	}
	return newConfig;
}


/*
 * Call the virsh create command to create the VM on the host
 */
function createVM() {
	run("source/engines/libvirt/execution/behaviors/CreateVM.js");

	//populate the model
	var vm = new Object();
	vm.name = parameters.vmName;
	vm.memory = parameters.memory;
	vm.vcpu = parameters.numCpus;
	vm.ip = parameters.ipAddress;
	vm.mac = parameters.mac;
	// vm.template = parameters.template;
	vm.image = vmDiskImage;
	model.vms.push(vm);
}

/*
 * run the domInfo behavior to query the VM and populate the model
 */
function domInfo() {
	run("source/engines/libvirt/execution/behaviors/DomInfo.js");
}

function verifyParams() {
	if(parameters.vmName == null)
		return false;
	if(parameters.templateName == null)
		return false;
	if(parameters.memory == null)
		return false;
	if(parameters.numCpus == null)
		return false;
	if(parameters.ipAddress == null)
		return false;
	if(parameters.mac == null)
		return false;

	return true;
}

/**
 * Create a the gsn specific elements to place in the context file for persistence. Currently the only tag necessary is
 * the ip address of the VM.
 */
function createGSNContext() {
	var context = "  <gsn>\n    <ip>" + parameters.ipAddress + "</ip>\n  </gsn>\n"
	return context;
}
