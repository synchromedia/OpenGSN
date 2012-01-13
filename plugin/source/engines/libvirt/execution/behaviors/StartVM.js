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
 * Start an existing VM. Because vms are undefined from the host when they
 * are shutdown, this procedure involves using the virsh create command
 * and re-building the model.
 *
 * Parameters: vmName - the name of the VM to create
 */

//Call the virsh create command to create the VM on the host
run("source/engines/libvirt/execution/behaviors/CreateVM.js");
run("source/engines/libvirt/execution/behaviors/AddVMToModel.js");


