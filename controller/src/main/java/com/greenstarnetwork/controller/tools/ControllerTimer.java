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
package com.greenstarnetwork.controller.tools;

import java.util.Timer;
import java.util.TimerTask;

import com.greenstarnetwork.controller.IControllerProvider;

/**
 * This class will provide tools for running the controller in periodic intervals. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

public class ControllerTimer {

	/**
	 * Time in second for periodic run of controller.
	 */
	public static double DEF_minAcceptableLifeTime = 1;
	public static double DEF_maxNecessaryLifeTime = 2;
	public static int DEF_INTERVAL = 3600;
	
	private int interval;
	private Timer timer;
	private IControllerProvider controller;
	


	public ControllerTimer(IControllerProvider controller) {
		this.controller = controller;
		/*if (this.controller.getMinAcceptableLifeTime() == 0)
			this.controller.setMinAcceptableLifeTime(DEF_minAcceptableLifeTime);
		if (this.controller.getMaxNecessaryLifeTime() == 0)
			this.controller.setMaxNecessaryLifeTime(DEF_maxNecessaryLifeTime);*/
		this.interval = DEF_INTERVAL;
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new RemindTask(), interval * 1000, interval * 1000);
		System.out.println("Controller successfully started");
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new RemindTask(), interval * 1000, interval * 1000);
		System.out.println("Controller will run every "+interval+" seconds.");
	}


	/**
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * 
	 *
	 */
	class RemindTask extends TimerTask {
		public void run() {
			System.out.println("Running the contoller every "+interval+" second.");
			try {
				execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error in ControllerTimer execution :: "+e);
			}

		}
	}

	/**
	 * Running the controller processes at each periodic interval.
	 * @throws Exception 
	 */
	public void execute() throws Exception {
		if (controller.getCloudManager() == null)
			controller.connect();
		controller.getResourcesFromCloud();
		controller.setMode(IControllerProvider.REAL_MODE);
		controller.refreshPlan();
	}

//	private void loadController(){
//		try {
//			controller = (IController) RegistryUtil.getServiceFromRegistry(
//					Activator.getContext(), IController.class.getName());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
