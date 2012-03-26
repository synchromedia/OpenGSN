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

/**
 * @author hzhang
 *
 */

public class FacilityOpHourSpec {

	private double maxMoveFromHours;
	private double minMoveToHours;
	
	public double getMaxMoveFromHours() {
		return maxMoveFromHours;
	}
	public void setMaxMoveFromHours(double maxMoveFromHours) {
		this.maxMoveFromHours = maxMoveFromHours;
	}
	public double getMinMoveToHours() {
		return minMoveToHours;
	}
	public void setMinMoveToHours(double minMoveToHours) {
		this.minMoveToHours = minMoveToHours;
	}
	public FacilityOpHourSpec(double maxMoveFromHours, double minMoveToHours) {
		super();
		this.maxMoveFromHours = maxMoveFromHours;
		this.minMoveToHours = minMoveToHours;
	}

}
