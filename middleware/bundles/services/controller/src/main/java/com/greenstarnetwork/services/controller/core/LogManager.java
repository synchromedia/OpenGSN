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
package com.greenstarnetwork.services.controller.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Managing log files produced by the Controller
 * 
 * @author knguyen
 *
 */
public class LogManager {

	/**
	 * List the name of all log files
	 * @return
	 */
	public List<String> listLogFiles() 
	{
		List<String> ret = null;
		File dir = new File(System.getenv(Logger.IAAS_HOME) + Logger.LOG_DIR);
		if (dir.exists())
		{
			File[] files = dir.listFiles();
			if (files != null) {
				for (int pi=0; pi<files.length; pi++)
				{
					String filename = files[pi].getName();
					if (checkValidLogFile(filename)) 
					{
						if (ret == null)
							ret = new ArrayList<String>();
						ret.add(files[pi].getPath());
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * Return the content of a given log file
	 * @param filename
	 * @return
	 */
	public String getLogContent(String filename) 
	{
		String ret = null;
		if (filename.indexOf(System.getenv(Logger.IAAS_HOME) + Logger.LOG_DIR) < 0)
			filename = System.getenv(Logger.IAAS_HOME) + Logger.LOG_DIR + filename;
		try {
			java.io.BufferedReader reader = new BufferedReader(new InputStreamReader(new
	                FileInputStream(filename)));
	        String s = reader.readLine();
	        while (s != null)
	        {
	        	if (ret == null)
	        		ret = s;
	        	else
	        		ret += s + "\n";
	        	s = reader.readLine();
	        }		
	        reader.close();
		} catch (IOException e) {
			return null;
		}
		return ret;
	}
	
	/**
	 * Check if the name of a given log file is valid
	 * @param filename
	 * @return
	 */
	private boolean checkValidLogFile(String filename) 
	{
	    if (filename.startsWith(Logger.LOG_FILE) && filename.endsWith(Logger.LOG_EXTENSION))
	    {
	    	String s = filename.substring(Logger.LOG_FILE.length(), filename.length() - Logger.LOG_EXTENSION.length());
	    	SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	    	try {
				dformat.parse(s);
				return true;
			} catch (ParseException e) {
				return false;
			}
	    }
	    return false;
	}
}
