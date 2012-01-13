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
package com.greenstarnetwork.resources.raritanclimate.core;

import java.util.ArrayList;
import java.util.List;


import com.greenstarnetwork.resources.raritanclimate.actionset.RefreshAction;
import com.greenstarnetwork.resources.raritanclimate.actionset.ShowSystemAction;
import com.greenstarnetwork.resources.raritanclimate.actionset.GetHumidityAction;
import com.greenstarnetwork.resources.raritanclimate.actionset.GetTemperatureAction;
import com.greenstarnetwork.resources.raritanclimate.actionset.LoginAction;
import com.greenstarnetwork.resources.raritanclimate.actionset.LogoutAction;
import com.iaasframework.capabilities.actionset.IAction;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.IActionFactory;

/**
 * 
 * @author knguyen
 * @author Abdelhamid Synchromedia.ca
 */
public class ClimateActionFactory implements IActionFactory{

	public IAction createAction(String actionId) throws ActionException {
		if (actionId.equals(LogoutAction.ACTION)){
			 
		}else if (actionId.equals(LoginAction.ACTION)){
			 
		}else if (actionId.equals(GetHumidityAction.ACTION)){
			return new GetHumidityAction();
		}else if (actionId.equals(GetTemperatureAction.ACTION)){
			return new GetTemperatureAction();
		}else 	if (actionId.equals(ShowSystemAction.ACTION)){
			return new ShowSystemAction();
		}else 	if (actionId.equals(RefreshAction.ACTION)){
			return new RefreshAction();
		}
		return null;
	}

	public List<String> getActionNames() {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("GetTemperatureAction");
		actions.add("GetHumidityAction");
		actions.add("LoginAction");
		actions.add("LogoutAction");
		actions.add("ShowSystemAction");
		actions.add("RefreshAction");
			return actions;
	}

	public IAction getCommitAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAction getRollbackAction() {
		// TODO Auto-generated method stub
		return null;
	}
}
