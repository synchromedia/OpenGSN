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
 * Add a VM to the the model. The VM must already exist. This behavior is typically used after a VM has been 
 * migrated to it's host and must be put in the hosts engine model.
 *
 * This behavior will add the VM name to the model and then call the GetVMInfo behavior to populate the details 
 * 
 * 
 * Parameters: vmName - the name of the VM being added.
 * 
 * @author Scott Campbell (CRC)
 */

var model = engine.model

var vm = new Object();
vm.name = parameters.vmName;
model.vms.push(vm);

// populate the model with the vm's contextual data from the xml config
run("source/engines/libvirt/execution/behaviors/GetVMInfo.js");
