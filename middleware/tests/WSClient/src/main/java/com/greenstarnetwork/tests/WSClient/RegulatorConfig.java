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
package com.greenstarnetwork.tests.WSClient;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="regulatorconfig")
public class RegulatorConfig {

	private String soundfile = null;	//path to sound file
	
	private int interval = 15;			//interval to check (in minute)
	
	private String server = null;
	
	private String vm = null;

	public void setSoundfile(String soundfile) {
		this.soundfile = soundfile;
	}

	public String getSoundfile() {
		return soundfile;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getInterval() {
		return interval;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getServer() {
		return server;
	}

	public void setVm(String vm) {
		this.vm = vm;
	}

	public String getVm() {
		return vm;
	}
}
