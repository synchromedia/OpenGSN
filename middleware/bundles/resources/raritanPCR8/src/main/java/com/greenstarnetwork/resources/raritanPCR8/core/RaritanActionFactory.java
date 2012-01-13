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
package com.greenstarnetwork.resources.raritanPCR8.core;

import java.util.ArrayList;
import java.util.List;

import com.greenstarnetwork.resources.raritanPCR8.actionset.OffAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.OnAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.QueryOutletAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.RefreshAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.SetOutletConsumerAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.ShowCurrSensorAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.ShowOutletAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.ShowPowerSensorAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.ShowSystemAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.ShowVoltSensorAction;
import com.greenstarnetwork.resources.raritanPCR8.actionset.ToggleAction;
import com.iaasframework.capabilities.actionset.IAction;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.IActionFactory;

/**
 * 
 * @author knguyen
 *
 */
public class RaritanActionFactory implements IActionFactory{

	public IAction createAction(String actionId) throws ActionException {
		if (actionId.equals(ShowSystemAction.ACTION)){
			return new ShowSystemAction();
		}else if (actionId.equals(ShowOutletAction.ACTION)){
			return new ShowOutletAction();
		}else if (actionId.equals(ShowCurrSensorAction.ACTION)){
			return new ShowCurrSensorAction();
		}else if (actionId.equals(ShowVoltSensorAction.ACTION)){
			return new ShowVoltSensorAction();
		}else if (actionId.equals(ShowPowerSensorAction.ACTION)){
			return new ShowPowerSensorAction();
		}else if (actionId.equals(QueryOutletAction.ACTION)){
			return new QueryOutletAction();
		}else if (actionId.equals(OnAction.ACTION)){
			return new OnAction();
		}else if (actionId.equals(OffAction.ACTION)){
			return new OffAction();
		}else if (actionId.equals(ToggleAction.ACTION)){
			return new ToggleAction();
		}else if (actionId.equals(RefreshAction.ACTION)){
			return new RefreshAction();
		}else if (actionId.equals(SetOutletConsumerAction.ACTION)){
			return new SetOutletConsumerAction();
		}else{
			throw new ActionException("Action "+actionId+" not found");
		}
	}

	public List<String> getActionNames() {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("ShowSystemAction");
		actions.add("ShowOutletAction");
		actions.add("ShowCurrSensorAction");
		actions.add("ShowVoltSensorAction");
		actions.add("ShowPowerSensorAction");
		actions.add("QueryOutletAction");
		actions.add("OnAction");
		actions.add("OffAction");
		actions.add("ToggleAction");
		actions.add("RefreshAction");
		actions.add("SetOutletConsumerAction");
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
