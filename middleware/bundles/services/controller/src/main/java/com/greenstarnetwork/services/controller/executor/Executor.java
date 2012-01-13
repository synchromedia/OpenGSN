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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.services.controller.core.IController;
import com.greenstarnetwork.services.controller.core.InetAddressUtil;
import com.greenstarnetwork.services.controller.core.Logger;
import com.greenstarnetwork.services.controller.model.Host;
import com.greenstarnetwork.services.controller.model.Link;
import com.greenstarnetwork.services.controller.model.LinkTable;
import com.greenstarnetwork.services.controller.model.Migration;
import com.greenstarnetwork.services.controller.model.MigrationPlan;

/**
 * Execute commands in order to achieve migration plans
 * @author knguyen
 *
 */
public class Executor {

	private Logger log = Logger.getLogger();
	
	private IController controller = null;
	
	private boolean executing = false;			//0: idle; 1: executing 
	
	public Executor(IController parent) {
		this.controller = parent;
	}
	
	/**
	 * Send a webservice command to IaaS
	 * @param host
	 * @param action
	 * @param args
	 */
	public synchronized String migrateVM(String hostId, String vmName, String destinationHostId) 
	{
		if (controller.getMode() == IController.REAL_MODE) 
		{
			try {//wait for previous synchronization
				Thread.sleep(10000);
			}catch (Exception e) {};
			
			ExecutorResolver resolver = new ExecutorResolver(this);
			Thread t = new Thread(resolver);
			t.start();
			String migRes = this.controller.getCloudManager().migrateVM(hostId, vmName, destinationHostId);
			if ((t != null) && t.isAlive())
			{
				resolver.setStopped(true);
			}
			return migRes;
		}
		return "VM: " + vmName + " removed!";
	}
	
	/**
	 * Execute a fake action (do nothing)
	 * @param host
	 * @param action
	 * @param args
	 */
	public synchronized String fakeExecuteActionWS(Host host, String action, String args) {
		return "Excute action: " + action + " on host " + host.getResourceID() + " IP: " + host.getHostModel().getAddress();
	}
	
	/**
	 * Execute a migration plan taking into account a table of available links
	 * @param plan
	 * @throws UnknownHostException 
	 */
	public synchronized ExecutionPlan executeMigrationPlan(MigrationPlan plan, LinkTable links) throws UnknownHostException 
	{
		List<ParallelMigration> parallelActions = new ArrayList<ParallelMigration>();
			
		String res = null;
		plan.setupIterator();
		while (plan.hasNextMigration()) 
		{
			Migration mig = plan.getNextMigration();
		
			//Sort linktable
			links.sort();
			//get link from table
			Iterator<Link> it = links.getLinks().iterator();
			boolean found_link = false;
			Link l = null;
			while (it.hasNext()) 
			{
				l = it.next();
				InetAddress ip = InetAddress.getByName(mig.getSourceHost().getHostModel().getAddress());
				InetAddress network = InetAddress.getByName(l.getSourceIP());
				InetAddress mask = InetAddress.getByName(l.getSourceMask());
				if (InetAddressUtil.contains(network, mask, ip))		
				{//check if source host of the migration is one of the link ends
					ip = InetAddress.getByName(mig.getDestinationHost().getHostModel().getAddress());
					network = InetAddress.getByName(l.getTargetIP());
					mask = InetAddress.getByName(l.getTargetMask());
					if (InetAddressUtil.contains(network, mask, ip)) {
						found_link = true;
						break;
					}
				}else 
				{//check if the target host of the migration is one of the link ends
					ip = InetAddress.getByName(mig.getDestinationHost().getHostModel().getAddress());
					if (InetAddressUtil.contains(network, mask, ip)) {
						ip = InetAddress.getByName(mig.getSourceHost().getHostModel().getAddress());
						network = InetAddress.getByName(l.getTargetIP());
						mask = InetAddress.getByName(l.getTargetMask());
						if (InetAddressUtil.contains(network, mask, ip)) {
							found_link = true;
							break;
						}
					}					
				}
			}
			if (found_link) 
			{
				res = this.fakeExecuteActionWS(mig.getSourceHost(), "MigrationAction", "-vm " 
						+ mig.getVirtualMachine().getVM().getName() 
						+ " -desthost " +	mig.getDestinationHost().getHostModel().getAddress());
				boolean found_action = false;
				for (int pi=parallelActions.size()-1; pi>-1; pi--)
				{
					ParallelMigration par = parallelActions.get(pi); 
					if (par.getLink().equals(l)) {
						if (par.addParallelVM(mig.getVirtualMachine())) 
						{
							found_action = true;
							break;
						}
					}
				}
				
				if (!found_action)
				{
					ParallelMigration par = new ParallelMigration();
					par.setLink(l);
					if (!par.addParallelVM(mig.getVirtualMachine())) 
						par.addSingleVM(mig.getVirtualMachine());
					parallelActions.add(par);
				}
			} else
				res = "No link found for migration from Source: " + mig.getSourceHost().getHostModel().getAddress() +
						" to Destination: " + mig.getDestinationHost().getHostModel().getAddress();
			log.debug(res);
		}

		//Log parallel migrations
		for (int pi=0; pi<parallelActions.size(); pi++)
		{
			ParallelMigration par = parallelActions.get(pi); 
			log.debug("Parallel Migration - Link: " + par.getLink().getSourceIP() 
					+ " <--> " + par.getLink().getTargetIP());
			for (int pj=0; pj<par.getVMs().size(); pj++)
			{
				log.debug("                   - VM: " + par.getVMs().get(pj).getVM().getIp()); 
			}
		}
		
		
		/*
		 * update the src and dest VmHostModels after all migration executions are successful
		 */
		plan.setupIterator();
		while (plan.hasNextMigration()) 
		{
			Migration mig = plan.getNextMigration();
			
			VM vm = mig.getVirtualMachine().getVM();
			
			//update the models
			mig.getDestinationHost().getHostModel().addVM(vm);
			mig.getSourceHost().getHostModel().removeVM(vm.getName());
			
			String status = "Executor :: " + mig.toString();
			System.err.println(status);
			status = this.migrateVM(mig.getSourceHost().getResourceID(), vm.getName(), mig.getDestinationHost().getResourceID());
			System.err.println(status);
			if ((status.toLowerCase().indexOf("error") != -1) || (status.toLowerCase().indexOf("exception") != -1))
			{
				mig.setState("FAILED. ");
			}else
				mig.setState("DONE. ");
		}
		System.err.println("Executor :: FINISH");
		
		/**
		 * Record migration actions to an execution plan which can be exported to web client
		 */
		ExecutionPlan ret = new ExecutionPlan();
		ret.setParallelActions(parallelActions);
		return ret;
	}

	/**
	 * @param executing the executing to set
	 */
	public synchronized void setExecuting(boolean executing) {
		this.executing = executing;
	}

	/**
	 * @return the executing
	 */
	public synchronized boolean isExecuting() {
		return executing;
	}

}
