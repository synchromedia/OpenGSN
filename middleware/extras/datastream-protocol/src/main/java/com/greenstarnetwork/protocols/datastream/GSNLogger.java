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
package com.greenstarnetwork.protocols.datastream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Recording log information of the Controller.
 * Information is written to a simple text file.
 * Each line has the following format:  "[Time] Log information"
 * @author knguyen
 *
 */
public class GSNLogger {

	public static String IAAS_HOME = "IAAS_HOME";				//environment variable - path to IAAS home
	public static String LOG_DIR = "/gsn/";	//default log file
	public static String LOG_FILE = "outbackmate";	//default log file
	public static String LOG_EXTENSION = ".log";	//default log file
	
	private static GSNLogger logger = null;				//singleton object
	private OutputStreamWriter writer = null;
    private String curDate = null;
	
	
	private GSNLogger()  throws IOException {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dformat.format(Calendar.getInstance().getTime());
		setCurDate(today);
		openFile();
	}

	private void openFile() throws IOException {
		String ih = System.getenv(IAAS_HOME);
		if (ih.startsWith("~")){
			ih=System.getenv("HOME")+"/"+ih.substring(1);
		}
		checkDir(ih + LOG_DIR);
		String log_file = ih + LOG_DIR + LOG_FILE + this.getCurDate() + LOG_EXTENSION;
		writer = new OutputStreamWriter(new FileOutputStream(log_file, true));
		writer.write("********* Start logging at : " + this.getCurDate() + " *********\n");
	}
	
	public void debug(String msg) 
	{
		try {
			writer.write("[" + getCurrentTime() + "] " + msg + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getCurrentTime() throws IOException {
	    Date time = Calendar.getInstance().getTime();
	    SimpleDateFormat hformat = new SimpleDateFormat("HH:mm");
	    SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	    String sd = dformat.format(time);
	    if (sd.compareTo(this.getCurDate()) != 0)
	    {
	    	this.setCurDate(sd);
	    	this.close();
	    	this.openFile();
	    }
        return hformat.format(time);
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized GSNLogger getLogger() {
		try {
			if (logger == null) {
				logger = new GSNLogger();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logger;
	}

	/**
	 * @param curDate the curDate to set
	 */
	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	/**
	 * @return the curDate
	 */
	public String getCurDate() {
		return curDate;
	}
	
	public void checkDir(String s){
		File file = new File(s);
		try {
			file.mkdirs();
		} catch (Exception e) {
			// the directory may exist, which is okay.
		}
	}
	
}
