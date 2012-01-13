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
package com.greenstarnetwork.resources.facilityResource.core;

import java.util.ArrayList;
import java.util.List;

import com.greenstarnetwork.resources.facilityResource.actionset.CheckPowerAction;
import com.greenstarnetwork.resources.facilityResource.actionset.OPSpecsCalculateAction;
import com.greenstarnetwork.resources.facilityResource.actionset.PollerAction;
import com.greenstarnetwork.resources.facilityResource.actionset.RefreshAction;
import com.greenstarnetwork.resources.facilityResource.actionset.SetOPHourThresholdsAction;
import com.greenstarnetwork.resources.facilityResource.actionset.SetOPHoursAction;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.IAction;
import com.iaasframework.capabilities.actionset.IActionFactory;

/**
 * 
 * create actions instances for facility resource.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */
public class FacilityResourceActionFactory implements IActionFactory{

	public IAction createAction(String actionId) throws ActionException {
		if (actionId.equals(RefreshAction.ACTION)){
			return new RefreshAction();
		}else if (actionId.equals(OPSpecsCalculateAction.ACTION)){
			return new OPSpecsCalculateAction();
		}else if (actionId.equals(CheckPowerAction.ACTION)){
			return new CheckPowerAction();
		}else if (actionId.equals(PollerAction.ACTION)){
			return new PollerAction();
		}else if (actionId.equals(SetOPHoursAction.ACTION)){
			return new SetOPHoursAction();
		}else if (actionId.equals(SetOPHourThresholdsAction.ACTION)){
			return new SetOPHourThresholdsAction();
		}else{
			throw new ActionException("Action "+actionId+" not found");
		}
	}

	public List<String> getActionNames() {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add(RefreshAction.ACTION);
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
