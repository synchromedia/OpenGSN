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
package com.greenstarnetwork.resources.outbackMate.commandset;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import com.greenstarnetwork.models.powersource.PowerSourceModel;
import com.greenstarnetwork.resources.outbackMate.core.Helper;
import com.iaasframework.capabilities.commandset.AbstractCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.CommandState.State;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * 
 * @author Fereydoun
 *
 */
public class VirtualTraceCommand extends AbstractCommand {

	static int updateTime =-1;
	static int currentDay=-1;
	static Double savedEnergyAMPHOUR=50.0;
	static boolean currentLoaded=false;
	static int[][] current = new int[7][1440];
	protected PowerSourceModel psmodel;
	public static final String COMMAND = "TraceCommand";	//Query command ID

	public VirtualTraceCommand(){
		super(COMMAND);
		initializeValues();
	}


	private void initializeValues(){
		if (currentDay==-1){
			currentDay=(new Random()).nextInt(7);
		}
		//		for(int i=0;i<7;i++)
		//			for(int j=0;j<1440;j++)
		//			{
		//				current[i][j]=(new Random()).nextInt(40);
		//			}
		Helper helper = new Helper();
		try {
			if (!currentLoaded){
				current= helper.readCurrentDataForAWeek();
				currentLoaded=true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (updateTime==-1){
			try {
				updateTime=getTime();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	private int getTime() throws IOException {
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date 
		Double hereOffset=(Double.valueOf(calendar.getTimeZone().getDSTSavings())+Double.valueOf(calendar.getTimeZone().getRawOffset()))/1000/3600;
		Helper helper = new Helper();
		Double gmt=helper.readGMT();
		int result =calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE)-(int)(hereOffset*60)+(int)(gmt*60);
		while (result<0)
		{
			result=result+1440;
		}
		while (result>=1440)
		{
			result=result-1440;
		}	
		print("time",result);
		return result;


	}


	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else{
			try {
				executeCommandMain();
				state.setState(State.RESPONSE_RECEIVED);
				requestEngineModel(false);
			} catch (Exception e) {
				throw new CommandException(COMMAND+" :: "+e);
			}
		}
	}


	public void executeCommandMain() throws Exception{
		Helper helper = new Helper();
		helper.init();
		int currentTime = getTime();
		int updatingPeriodMIN=0;
		if (currentTime>=updateTime){
			updatingPeriodMIN=currentTime-updateTime;
		}else{
			{//Switching day
				currentDay=(new Random()).nextInt(7);
			}
			updatingPeriodMIN=1440+currentTime-updateTime;
		}

		print("currentDay",currentDay);
		print("currentTime",currentTime);
		print("updatingPeriodMIN",updatingPeriodMIN);

		if (updatingPeriodMIN<30)
		{
			double chargingCurrent=0;

			if (psmodel.getInvACMode()==null){
				psmodel.setInvACMode("AC USE");
			}

			print("V*I",helper.readPDUCurrentVoltage());

			if (psmodel.getInvACMode().equalsIgnoreCase("AC USE")){
				chargingCurrent=current[currentDay][currentTime];
			}else{

				chargingCurrent=current[currentDay][currentTime]-
				helper.readPDUCurrentVoltage()/26;
				print("V*I",helper.readPDUCurrentVoltage());
			}

			print("chargingCurrent",chargingCurrent);

			savedEnergyAMPHOUR=savedEnergyAMPHOUR+(updatingPeriodMIN*(chargingCurrent)/60);

			if (savedEnergyAMPHOUR>240){savedEnergyAMPHOUR=240.0;}
			if (savedEnergyAMPHOUR<0){savedEnergyAMPHOUR=0.0;}

			print("savedEnergyAMPHOUR",savedEnergyAMPHOUR);

			double batteryChargePercentage=savedEnergyAMPHOUR*100/240;

			print("batteryChargePercentage",batteryChargePercentage);
			print("low",helper.readSwitchToGridLowMargin());
			print("high",helper.readSwitchToGridHighMargin());

			if (batteryChargePercentage<helper.readSwitchToGridLowMargin())
			{
				psmodel.setInvACMode("AC USE");
			}
			if (batteryChargePercentage>helper.readSwitchToGridHighMargin())
			{
				psmodel.setInvACMode("AC DROP");
			}

			double batteryVoltage=23+(batteryChargePercentage*6/100);

			print("batteryVoltage",batteryVoltage);

			psmodel.setChargerBatteryVoltage(Double.toString(batteryVoltage));
			psmodel.setChargerCurrent(Double.toString(current[currentDay][currentTime]));
			//psmodel.setInvACMode("PS");
			//psmodel.setInvACMode("AC USE");
		}
		updateTime=currentTime;
	}

	private void print(String string, double d) {
//		System.out.println(string+": "+Double.valueOf(d));

	}


	@Override
	public synchronized void initializeCommand(IResourceModel model) throws CommandException {
		psmodel = (PowerSourceModel)model;
	}

	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException {
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
		this.model = psmodel;
	}


}