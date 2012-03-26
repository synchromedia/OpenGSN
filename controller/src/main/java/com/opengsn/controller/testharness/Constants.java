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
package com.opengsn.controller.testharness;

public interface Constants {

	/*
	 * where default facility .txt files are loaded from, and where runtime facility
	 * snapshots will be put under folders named by yyyy-MM-dd-HH-mm
	 */
	public static final String FACILITY_HOME = "controller-run/snapshots";
	
	/*
	 * where canned daily production files are stored, i.e., day1.csv ... dayn.csv
	 */
	public static final String CSV_HOME = "controller-run/dailyproductions";
	
	// In the case of running against virtual facilities, set the interval to 30 seconds
	public static final int SIMULATOR_INTERVAL = 30;
}
