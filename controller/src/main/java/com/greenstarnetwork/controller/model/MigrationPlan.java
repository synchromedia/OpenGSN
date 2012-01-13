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
package com.greenstarnetwork.controller.model;



/**
 * This class will provide data structure to store a set of migrations 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.greenstarnetwork.controller.tools.Logger;

public class MigrationPlan {

	public static String LOG_FILE = "Migrations.log";	//default log file

	/**
	 * migration plan
	 */
	private List<Migration> plan = new ArrayList<Migration>();
	Iterator<Migration> iterator = null;

	private boolean isDone = false;
	
	public MigrationPlan() {
	}

	public List<Migration> getPlan() {
		return this.plan;
	}

	/** 
	 * 
	 * @param plan
	 */
	public void setPlan(List<Migration> plan) {
		this.plan = plan;
	}

	public void sort() {
		java.util.Collections.sort(this.plan);
	}

	/**
	 * 
	 * @param migration
	 */
	public void addMigration(Migration migration) {
		if (this.plan == null)
			this.plan = new ArrayList<Migration>();
		this.plan.add(migration);
	}
	
	/**
	 * 
	 */
	public void setupIterator() {
		if (this.plan!=null && this.plan.iterator()!=null){
			iterator = this.plan.iterator();
		}else{
			iterator = null;
		}
	}


	/**
	 * 
	 * @return
	 */
	public Migration getNextMigration() {
		if (this.plan == null)
			return null;
		else {
			if (iterator.hasNext()) {
				return (Migration)iterator.next();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasNextMigration() {
		if (this.plan == null || iterator == null)
			return false;
		else {
			if (iterator.hasNext()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Append another migration plan to this migration plan.
	 * @param migrationPlan2
	 */
	public void append(MigrationPlan migrationPlan2) {
		migrationPlan2.setupIterator();
		while (migrationPlan2.hasNextMigration()){
			Migration migration = migrationPlan2.getNextMigration();
			this.plan.add(migration);
		}
	}
	
	/**
	 * Return a string representing the state of all migrations
	 */
	public String toString() {
		String ret = "";
		if (this.plan != null)
		{
			Iterator<Migration> it = this.plan.iterator();
			while (it.hasNext()) {
					ret = ret + ((Migration)it.next()).toString() + "\n";
			}
		}
		return ret;
	}
	
	/**
	 * Write the state of all migration to a file
	 * @throws IOException 
	 */
	public void logMigrations() throws IOException 
	{
		String log_file = System.getenv(Logger.IAAS_HOME) + Logger.LOG_DIR + LOG_FILE;
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(log_file, true));
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		writer.write("***** Migrations done at : " + dformat.format(Calendar.getInstance().getTime()) + " *****\n");
		writer.write(this.toString());
		writer.write("\n");
		writer.flush();
		writer.close();
	}

	/**
	 * @param isDone the isDone to set
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		return isDone;
	}
}
