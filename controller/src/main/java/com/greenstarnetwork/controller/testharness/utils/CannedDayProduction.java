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
package com.greenstarnetwork.controller.testharness.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.greenstarnetwork.controller.testharness.Constants;
import com.greenstarnetwork.controller.testharness.exceptions.CannedProductionProcessingException;

public class CannedDayProduction {
			
	public static double getProductionFromCsv(String timezone, int id, long start, long end) throws CannedProductionProcessingException {
		
		double ampHours = 0.0;
		
		String file = Constants.CSV_HOME + "/day" + Integer.toString(id) + ".csv";
		FileInputStream in;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			throw new CannedProductionProcessingException("Error opening file " + file); 
		}
		BufferedReader dis = new BufferedReader (new InputStreamReader(in));

		String line = "";
		
		try {
			while ( (line = dis.readLine ()) != null ) 
			{				
				if(line.length() == 0)
					continue;
				
				String[] temp = line.trim().split(",");
				
			    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				formatter.setTimeZone(TimeZone.getTimeZone(timezone)); //TODO validate 
				
			    Date date;
				try {
					date = formatter.parse(temp[0]);
					long dateLong = date.getTime();

					if(compareDates(dateLong, start, timezone))
						continue;
					
					if(compareDates(end, dateLong, timezone))
						break;
										
					//1 minute = 1/60 hour
					ampHours +=  Double.parseDouble(temp[1])/60;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new CannedProductionProcessingException("Error parsing date in " + temp[0] + " in file " + file); 
				}
			    
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CannedProductionProcessingException("Error reading file " + file); 
		}
		
		try {
			in.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CannedProductionProcessingException("Error closing file " + file); 
		}
				
		return ampHours;
	}
	
	/*
	 * returns true if d1 < d2 regardless of year and month;
	 * returns false otherwise
	 */
	private static boolean compareDates(long d1, long d2, String timezone) {
		Calendar cal1 = new GregorianCalendar(TimeZone.getTimeZone(timezone));
		cal1.setTimeInMillis(d1);
		int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal1.get(Calendar.MINUTE);
		
		Calendar cal2 = new GregorianCalendar(TimeZone.getTimeZone(timezone));
		cal2.setTimeInMillis(d2);
		int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal2.get(Calendar.MINUTE);
		
		if(hour2 > hour1 || (hour2 == hour1 && minute2 > minute1))
			return true;
		else 
			return false;
	}
}
