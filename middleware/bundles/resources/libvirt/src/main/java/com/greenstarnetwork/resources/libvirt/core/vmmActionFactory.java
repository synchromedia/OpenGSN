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
package com.greenstarnetwork.resources.libvirt.core;

import java.util.ArrayList;
import java.util.List;

import com.greenstarnetwork.resources.libvirt.actionset.CreateAction;
import com.greenstarnetwork.resources.libvirt.actionset.CreateFromImageAction;
import com.greenstarnetwork.resources.libvirt.actionset.DeleteAction;
import com.greenstarnetwork.resources.libvirt.actionset.HostInfoAction;
import com.greenstarnetwork.resources.libvirt.actionset.HostStatisticalAction;
import com.greenstarnetwork.resources.libvirt.actionset.MigrateAction;
import com.greenstarnetwork.resources.libvirt.actionset.ReCreateAction;
import com.greenstarnetwork.resources.libvirt.actionset.RebootAction;
import com.greenstarnetwork.resources.libvirt.actionset.RefreshAction;
import com.greenstarnetwork.resources.libvirt.actionset.SCPAction;
import com.greenstarnetwork.resources.libvirt.actionset.SetVMIPAction;
import com.greenstarnetwork.resources.libvirt.actionset.SetVMStatusAction;
import com.greenstarnetwork.resources.libvirt.actionset.ShutdownAction;
import com.greenstarnetwork.resources.libvirt.actionset.StartAction;
import com.greenstarnetwork.resources.libvirt.actionset.StopAction;
import com.iaasframework.capabilities.actionset.ActionException;
import com.iaasframework.capabilities.actionset.IAction;
import com.iaasframework.capabilities.actionset.IActionFactory;

/**
 * 
 * @author knguyen
 *
 */
public class vmmActionFactory implements IActionFactory{

	public IAction createAction(String actionId) throws ActionException {
		if (actionId.equals(CreateAction.ACTION)){
			return new CreateAction();
		}else if (actionId.equals(MigrateAction.ACTION)){
			return new MigrateAction();
		}else if (actionId.equals(RefreshAction.ACTION)){
			return new RefreshAction();
		}else if (actionId.equals(DeleteAction.ACTION)){
			return new DeleteAction();
		}else if (actionId.equals(StartAction.ACTION)){
			return new StartAction();
		}else if (actionId.equals(StopAction.ACTION)){
			return new StopAction();
		}else if (actionId.equals(HostInfoAction.ACTION)){
			return new HostInfoAction();
		}else if (actionId.equals(HostStatisticalAction.ACTION)){
			return new HostStatisticalAction();
		}else if (actionId.equals(ShutdownAction.ACTION)){
			return new ShutdownAction();
		}else if (actionId.equals(RebootAction.ACTION)){
			return new RebootAction();
		}else if (actionId.equals(SetVMIPAction.ACTION)){
			return new SetVMIPAction();
		}else if (actionId.equals(SetVMStatusAction.ACTION)){
			return new SetVMStatusAction();
		}else if (actionId.equals(ReCreateAction.ACTION)){
			return new ReCreateAction();
		}else if (actionId.equals(SCPAction.ACTION)){//TODO EXPORT
			return new SCPAction();
		}else if (actionId.equals(CreateFromImageAction.ACTION)){//TODO EXPORT
			return new CreateFromImageAction();
		}else{
			throw new ActionException("Action "+actionId+" not found");
		}
	}

	public List<String> getActionNames() {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add(CreateAction.ACTION);
		actions.add(MigrateAction.ACTION);
		actions.add(RefreshAction.ACTION);
		actions.add(DeleteAction.ACTION);
		actions.add(StartAction.ACTION);
		actions.add(StopAction.ACTION);
		actions.add(ShutdownAction.ACTION);
		actions.add(HostInfoAction.ACTION);
		actions.add(HostStatisticalAction.ACTION);
		actions.add(RebootAction.ACTION);
		actions.add(SetVMIPAction.ACTION);
		actions.add(SetVMStatusAction.ACTION);
		actions.add(ReCreateAction.ACTION);
		actions.add(SCPAction.ACTION);//TODO EXPORT
		actions.add(CreateFromImageAction.ACTION);//TODO EXPORT
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
