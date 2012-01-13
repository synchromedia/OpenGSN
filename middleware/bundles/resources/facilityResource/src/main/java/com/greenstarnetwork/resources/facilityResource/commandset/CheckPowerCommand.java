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
package com.greenstarnetwork.resources.facilityResource.commandset;

import java.util.Calendar;
import java.util.Random;

import javax.mail.MessagingException;

import com.greenstarnetwork.models.common.Alarm;
import com.greenstarnetwork.resources.facilityResource.core.GSNMonitor;
import com.greenstarnetwork.resources.facilityResource.core.JMSQueue;
import com.greenstarnetwork.resources.facilityResource.core.Logger;
import com.greenstarnetwork.resources.facilityResource.core.ResourceManagerService;



/**
 *
 *This command will calculate the operating hour left on the facility according to the consumption of the facility. 
 *
 *@author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

public class CheckPowerCommand extends BasicCommand {

	private static final String FACILITY_LOW_OP_HOUR_OR_NOT_ON_GREEN_POWER = "FACILITY: LOW OP-HOUR OR SWITCHED TO GRID";
	public static final String COMMAND = "CheckPowerCommand";

	public CheckPowerCommand() {
		super(COMMAND);
	}



	public void executeCommandMain() throws Exception {
		if (facilityModel.getInitialized())
		{
			Logger.getLogger().log(
					"initialized:"+facilityModel.getInitialized()+" "+
					"status:"+facilityModel.getOperationalSpecs().getStatus()+" "+
					"on-grid:"+facilityModel.getOperationalSpecs().getOnGrid()+" "+
					"battery%:"+facilityModel.getOperationalSpecs().getBatteryChargePercentage()+" "+
					"op-hour:"+facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad()+" "+
					"op-hour-max:"+facilityModel.getOperationalSpecs().getOpHourUnderMaximumLoad()+" "+
					"op-hour-threshold:"+facilityModel.getOperationalSpecs().getOpHourThreshold()+" "+
					"op-hour-max-threshold:"+facilityModel.getOperationalSpecs().getOpHourThresholdUnderMax()+" "+
					"consumption:"+facilityModel.getOperationalSpecs().getTotalConsummingPower()+" "+
					"generation:"+facilityModel.getOperationalSpecs().getTotalGeneratingPower()
			);
			if (!facilityModel.getOperationalSpecs().getPowerSourceType().equalsIgnoreCase("HYDRO")&&
					!facilityModel.getOperationalSpecs().getPowerSourceType().equalsIgnoreCase("GRID"))
			{
				if ( ( facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad()<
						Double.valueOf(facilityModel.getOperationalSpecs().getOpHourThreshold()) ) || 
						!facilityModel.getOperationalSpecs().getStatus().equalsIgnoreCase("GREEN") )
				{
					if (notExistAlarm(FACILITY_LOW_OP_HOUR_OR_NOT_ON_GREEN_POWER)){
						if (JMSQueue.getJMSQueue()==null){
							Logger.getLogger().warning(FACILITY_LOW_OP_HOUR_OR_NOT_ON_GREEN_POWER+" :: JMSQueue.getJMSQueue() is null, no notification sent ...");
						}else{
							//					JMSQueue.getJMSQueue().sendNotification(facilityModel,"FACILITY LOW IN OP-HOUR");
							JMSQueue.getJMSQueue().sendModelToQueue(facilityModel);
							Logger.getLogger().warning(FACILITY_LOW_OP_HOUR_OR_NOT_ON_GREEN_POWER+" :: notification sent");
						}

						createAlarm();
					}
				}
				//			else if ((facilityModel.getOperationalSpecs().getOpHourUnderMaximumLoad() >
				//			Double.valueOf(facilityModel.getOperationalSpecs().getOpHourThresholdUnderMax())))
				//			{
				//				JMSQueue.getJMSQueue().sendModelToQueue(facilityModel);
				//			}
				else {
					//				Logger.getLogger().log("FACILITY GOOD ON OP-HOUR ... continue to work");
					clearAlarm();
				}
			}
		}else{
			logger.error("Power checker :: model is not initalized, nothing to check.");
		}

	}



	private void clearAlarm() throws Exception {

		for (Alarm alarm:facilityModel.getAlarms()){
			if (alarm.getAlarm().equalsIgnoreCase(FACILITY_LOW_OP_HOUR_OR_NOT_ON_GREEN_POWER)){
				String tresourceID = null; 
				try{tresourceID = (String) this.commandRequestMessage.getArguments().get("resourceID");}catch(Exception e){}
				ResourceManagerService service = new ResourceManagerService();
				service.init();
				try{
					String ts=service.getResourceAliaseByID(tresourceID);
					GSNMonitor.sendSSLMessage(facilityModel.getAlarmSendTo().split(","), ts+" ALARM :: "+alarm.getAlarm()+" (CLEAR)","Alarm at "+ts+". ResourceID: <"+tresourceID+">. Alarm ("+alarm.getDescription()+") is clear. \n\n\nFacility Model:\n\n\n"+facilityModel.toXML());
				} catch (Exception e) {
					logger.error("alarm clearer :: cannot send email to notify the admin !!!"+e.toString()+"..."+e.getStackTrace().toString());
				}
				facilityModel.getAlarms().remove(alarm);
				break;
			}
		}

	}



	private boolean notExistAlarm(String facilityLowOpHour) {
		Boolean notExist =true;

		for (Alarm alarm:facilityModel.getAlarms()){
			if (alarm.getAlarm().equalsIgnoreCase(facilityLowOpHour)){
				notExist=false;
			}
		}

		return notExist;
	}



	private void createAlarm() throws Exception {
		Alarm alarm = new Alarm();
		String desc=null;
		if ( Double.valueOf(facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad())<
				Double.valueOf(facilityModel.getOperationalSpecs().getOpHourThreshold()) )
		{
			desc = "Facility operating hour ("+facilityModel.getOperationalSpecs().getOpHourUnderCurrentLoad()+") is less than operating hour threshold ("+facilityModel.getOperationalSpecs().getOpHourThreshold()+")";
		}
		else if ( facilityModel.getOperationalSpecs().getOnGrid().equalsIgnoreCase("TRUE") )
		{
			desc = "Facility switched to grid and is not green anymore.";
		}
		else
		{
			desc = "Something happend! Check the facility model for the source of notification.";
		}
		{// adding tracking number to description of the alaram
			Random random = new Random();
			desc=desc+" (tracking#"+random.nextInt(1000000000)+")";
		}
		alarm.setDescription(desc );
		//alarm.setId("");
		alarm.setTime(Calendar.getInstance());
		String alarmName = FACILITY_LOW_OP_HOUR_OR_NOT_ON_GREEN_POWER;
		alarm.setAlarm(alarmName);
		//alarm.setType("FACILITY");
		//alarm.setValue("FACILITY LOW IN OP-HOUR");
		facilityModel.getAlarms().add(alarm);
		String tresourceID = null; 
		try{tresourceID = (String) this.commandRequestMessage.getArguments().get("resourceID");}catch(Exception e){}
		ResourceManagerService service = new ResourceManagerService();
		service.init();
		try{
			String ts=service.getResourceAliaseByID(tresourceID);
			GSNMonitor.sendSSLMessage(facilityModel.getAlarmSendTo().split(","), ts+" ALARM :: "+alarmName,"Alarm at "+ts+". ResourceID: <"+tresourceID+">:\n\n\n"+desc+"\n\n\nFacility Model:\n\n\n"+facilityModel.toXML());
		} catch (Exception e) {
			logger.error("Power checker :: cannot send email to notify the admin !!!"+e.toString()+"..."+e.getStackTrace().toString());
		}
	}


}