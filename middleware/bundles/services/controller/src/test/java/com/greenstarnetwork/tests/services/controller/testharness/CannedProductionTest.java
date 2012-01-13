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
package com.greenstarnetwork.tests.services.controller.testharness;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.greenstarnetwork.services.controller.core.testharness.utils.CannedDayProduction;

public class CannedProductionTest {

	@Test
	public void getProduction() throws Exception {

	      DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

	      String start = "2011-01-17-10-35";
		  Date startDate = (Date)format.parse(start);
		  long startLong = startDate.getTime();
		  
		  String end = "2011-01-17-11-35";
		  Date endDate = (Date)format.parse(end);
		  long endLong = endDate.getTime();
		  
		  System.out.println("production during period: " + CannedDayProduction.getProductionFromCsv("America/New_York", 1, startLong, endLong));
		  
	}
}
