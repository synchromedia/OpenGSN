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
package com.greenstarnetwork.services.controller.executor;


/**
 * Resolver of migration deadlock when executing migration plans
 * 
 * @author knguyen
 *
 */
public class ExecutorResolver implements Runnable 
{
	
	private Executor executor = null;
	private int timeout = 300;		//default timeout of a migration is 300 seconds (5 minutes)
	private int checkperiod = 30;
	private boolean isStopped = false;
	
	public ExecutorResolver(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public void run() {
		int pi = 0;
		while (!isStopped && (pi < timeout)) 
		{
			try {
				Thread.sleep(checkperiod * 1000);//sleep 30 second
			}catch (Exception e) {};
			pi += 30;
		}
		if (!isStopped)
			executor.setExecuting(false);	//timeout, cancel the current migration
		else
			System.err.println("===>Migration successful");
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public synchronized void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}

	public boolean isStopped() {
		return isStopped;
	}
}
