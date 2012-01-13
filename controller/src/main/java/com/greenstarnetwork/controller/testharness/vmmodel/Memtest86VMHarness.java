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
/**
 * 
 */
package com.greenstarnetwork.controller.testharness.vmmodel;

/**
 * @author hzhang
 *
 */
public class Memtest86VMHarness extends VMHarness {

	private static final long serialVersionUID = 6108072022385407456L;
	/*
	 * 0.15 amp per 2Gb of memory, measured from the CRC facility,
	 * in theory the ratio should be 0.075; make it 1 for testing purposes
	 */
	private static final double CURRENT_TO_MEMORY_RATIO = 1;

	/*
	 * assume the peak current is 1.5 times the average
	 */
	private static final double PEAK_TO_AVERAGE_RATIO = 1.5;
	
	@Override
	public double getConsumption(long start, long end) {
		// TODO Auto-generated method stub
		double hoursElapsed = (end - start)/(3600 * 1000);
		return hoursElapsed * Double.parseDouble(this.getMemory()) * CURRENT_TO_MEMORY_RATIO / 1000;
		
	}

	@Override
	public double getAverage(long start, long end) {
		// TODO Auto-generated method stub
		return Double.parseDouble(this.getMemory()) * CURRENT_TO_MEMORY_RATIO / 1000;
	}

	@Override
	public double getPeak(long start, long end) {
		// TODO Auto-generated method stub
		return Double.parseDouble(this.getMemory()) * CURRENT_TO_MEMORY_RATIO * PEAK_TO_AVERAGE_RATIO / 1000;
	}

}
