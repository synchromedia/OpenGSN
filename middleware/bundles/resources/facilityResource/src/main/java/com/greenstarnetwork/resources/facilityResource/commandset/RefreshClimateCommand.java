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

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.common.Alarm;
import com.greenstarnetwork.resources.facilityResource.core.GSNMonitor;
import com.greenstarnetwork.resources.facilityResource.core.ResourceManagerService;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;


/**
 * 
 * This command will update all the connected resources to the facility resource.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public class RefreshClimateCommand extends BasicCommand {

	public static final String COMMAND = "RefreshClimateCommand";


	public RefreshClimateCommand() {
		super(COMMAND);
	}


	public void executeCommandMain() throws Exception {
		if (facilityModel.getInitialized()){
			facilityModel.setClimate(getClimateModel());
			if (facilityModel.getClimate()!=null){
				addAlarms();
			}
		}else{
			logger.log("Climate refresher :: model is not initalized, no resourceID to load the model.");
		}
	}


	private void addAlarms() throws Exception {

		List<Alarm> climateAlarms = facilityModel.getClimate().getAlarms();
		List<Alarm> facilityAlarms = facilityModel.getAlarms();
		List<Alarm> newAlarms = inAnotinB(climateAlarms,facilityAlarms);
		for (Alarm alarm:newAlarms){
			facilityModel.getAlarms().add(alarm);
			String tresourceID = null; 
			try{tresourceID = (String) this.commandRequestMessage.getArguments().get("resourceID");}catch(Exception e){}
			ResourceManagerService service = new ResourceManagerService();
			service.init();
			try{
				String ts=service.getResourceAliaseByID(tresourceID);
				GSNMonitor.sendSSLMessage(facilityModel.getAlarmSendTo().split(","), ts+" ALARM :: Climate ::"+alarm.getAlarm(),"Alarm at "+ts+". ResourceID: <"+tresourceID+">:\n\n\n"+alarm.getDescription()+"\n\n\nFacility Model:\n\n\n"+facilityModel.toXML());
			} catch (Exception e) {
				logger.error("Climate refresher :: cannot send email to notify the admin !!!"+e.toString()+"..."+e.getStackTrace().toString());
			}
		}

	}


	private List<Alarm> inAnotinB(List<Alarm> climateAlarms,
			List<Alarm> facilityAlarms) {
		List<Alarm> newAlarms = new ArrayList<Alarm>();
		if (!(climateAlarms==null || facilityAlarms==null)){
			for (Alarm climateAlarm:climateAlarms){
				boolean exist = false;
				for (Alarm facilityAlarm:facilityAlarms){
					if (climateAlarm.getTime().getTime().toString().equalsIgnoreCase(facilityAlarm.getTime().getTime().toString())){
						exist = true;
					}
				}
				if (!exist){
					newAlarms.add(climateAlarm);
				}
			}
		}
		return newAlarms;
	}


	public  Climate getClimateModel() throws Exception{
		ResourceManagerService resourceManagerService = new ResourceManagerService();
		resourceManagerService.init();
		ModelCapabilityClient modelClient=null;
		RequestModelResponse reqModel=null;
		if (facilityModel.getClimateResourceID()!=null){
			resourceManagerService.checkExist(facilityModel.getClimateResourceID());
			modelClient = new ModelCapabilityClient(facilityModel.getClimateResourceID());
			reqModel = modelClient.requestModel(true);
			return (Climate)(reqModel.getResourceModel());}
		else{
			return null;
		}
	}


}