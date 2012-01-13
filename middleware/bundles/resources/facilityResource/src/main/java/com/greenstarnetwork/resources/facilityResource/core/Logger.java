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
package com.greenstarnetwork.resources.facilityResource.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	
	private OutputStreamWriter writer = null;
	private static Logger logger = null;	
	private static String LOG_FILE = "facility.log";
	
	
	public Logger(String log_file) throws IOException {
		writer = new OutputStreamWriter(new FileOutputStream(log_file, true));
		writer.write("********* Start logging at : " + getCurrentTime() + " *********\n");
		writer.write("Facility resource firmware version:"+FacilityResource.FIRMWARE_VERSION+"\n");
	}
	
	private Logger()  throws IOException {
		FacilityResourceToFromFile facilityResourceToFromFile = new FacilityResourceToFromFile();
		String s = facilityResourceToFromFile.getIAASPath();
		facilityResourceToFromFile.checkDir(s+"/gsn/facility");
		writer = new OutputStreamWriter(new FileOutputStream(s+"/gsn/facility/"+LOG_FILE, true));
		writer.write("********* Start logging at : " + getCurrentTime() + " *********\n");
		writer.write("Facility resource firmware version:"+FacilityResource.FIRMWARE_VERSION+"\n");
	}
	
	public void log(String msg) 
	{
		try {
			writer.write("[" + getCurrentTime() + "] " + msg + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void warning(String msg) 
	{
		try {
//			writer.write("[" + getCurrentTime() + "] " + "###############" + "\n");
			writer.write("[" + getCurrentTime() + "] " + "# WARNING # " + msg + "\n");
//			writer.write("[" + getCurrentTime() + "] " + "###############" + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void error(String msg) 
	{
		try {
			writer.write("[" + getCurrentTime() + "] " + "#############" + "\n");
			writer.write("[" + getCurrentTime() + "] " + "### ERROR ### " + msg + "\n");
			writer.write("[" + getCurrentTime() + "] " + "#############" + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void begin(String msg) 
	{
		try {
			writer.write("[" + getCurrentTime() + "] " + "///////////////////////////////" + "\n");
			writer.write("[" + getCurrentTime() + "] " + "/// BEGIN: " + msg + "\n");
			writer.write("[" + getCurrentTime() + "] " + "// \n");
			writer.write("[" + getCurrentTime() + "] " + "/ \n");

			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void end(String msg) 
	{
		try {


			writer.write("[" + getCurrentTime() + "] " + "\\ \n");
			writer.write("[" + getCurrentTime() + "] " + "\\\\ \n");
			writer.write("[" + getCurrentTime() + "] " + "\\\\\\ END: " + msg + "\n");
			writer.write("[" + getCurrentTime() + "] " + "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\" + "\n");
			

			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getCurrentTime() {
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dformat.format(Calendar.getInstance().getTime());
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized Logger getLogger() {
		if (logger == null) {
			try {
				logger = new Logger();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return logger;
	}

	public void logException(Exception e) {
		log(e.toString());
		for (StackTraceElement stackTraceElement:e.getStackTrace()){
			log(stackTraceElement.toString());				
		}
	}
	
}
