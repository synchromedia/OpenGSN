/**
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
package com.greenstarnetwork.services.controller.shell;

/**
 * This class will provide a command to stop the controller in OSGI container.
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.services.controller.core.testharness.ControllerRunner;

@Command(scope = "gsn-controller", name = "stop", description="Stop the controller")
public class StopShellCommand extends OsgiCommandSupport {

	/**
	 * 
	 */
	@Override
	protected Object doExecute() throws Exception {
		System.out.println("Stoping controller ... ");
		ControllerRunner.stop();
		System.out.print("Contorller status :: ");
		System.out.println(ControllerRunner.status());
		return null;
	}
}
