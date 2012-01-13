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
package com.greenstarnetwork.services.controller.core.testharness.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greenstarnetwork.services.controller.core.testharness.Constants;
import com.greenstarnetwork.services.controller.core.testharness.IFacility;
import com.greenstarnetwork.services.controller.core.testharness.exceptions.FacilityPersistenceException;

public class FacilitySerializer {

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static String timeToFolder(long time) {
		Calendar cal = new GregorianCalendar(); //use the controller's own time zone
		cal.setTimeInMillis(time);
		Date date = cal.getTime();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

		return formatter.format(date);
	}

	protected static void persist(String content, String path) throws IOException {
		File outFile = new File(path);
		FileWriter out = new FileWriter(outFile);
		out.write(content);
		out.close();
	}

	protected static String getShortName(String fullPackage) {

		String delimiter = "\\.";
		String[] temp = fullPackage.split(delimiter);

		if(temp != null && temp.length != 0)
			return temp[temp.length - 1];
		else 
			return null;
	}

	public static void saveFacilities(List<IFacility> facilities, long currentTime) throws FacilityPersistenceException {

		String folder = null;

		if(currentTime >0) {
			folder = Constants.FACILITY_HOME + "/" + timeToFolder(currentTime) + "/";

			new File(folder).mkdirs();
			
			/*
			File f = new File(folder); 
			if(!f.exists())
				throw new FacilityPersistenceException("Cannot create folder " + folder + " when saving facilities!");
			*/
		}
		else
			folder = "";

		for(IFacility f : facilities) {
			String json = gson.toJson(f);
			String name = getShortName(f.getClass().getName());

			try {
				persist(json, folder + name + "-" + f.getFacilityId() + ".txt");
			} catch (IOException e) {
				throw new FacilityPersistenceException("Cannot persist facility file " + e.getMessage());
			}
		}
	}
}
