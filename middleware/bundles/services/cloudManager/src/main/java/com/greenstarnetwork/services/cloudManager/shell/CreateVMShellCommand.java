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
package com.greenstarnetwork.services.cloudManager.shell;

import java.util.Iterator;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.services.cloudManager.ICloudManager;
import com.greenstarnetwork.services.cloudManager.model.ResourceManagerData;
import com.iaasframework.resources.core.RegistryUtil;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
/**
 * 
 * @author knguyen
 *
 */
@Command(scope = "gsn", name = "createVM", description="Create a VM on the cloud")
public class CreateVMShellCommand extends OsgiCommandSupport {

	private ICloudManager manager;
	
	@Argument(index = 0, name = "resourceName", description = "Host name", required = true, multiValued = false)
	private String resourceName;
	@Argument(index = 1, name = "vmName", description = "Name of VM to be created", required = true, multiValued = false)
	private String vmName;
	@Argument(index = 2, name = "memory", description = "Memory capacity of VM", required = true, multiValued = false)
	private String memory;
	@Argument(index = 3, name = "cpu", description = "Number of CPU of VM", required = true, multiValued = false)
	private String cpu;
	@Argument(index = 4, name = "template", description = "Template of VM", required = true, multiValued = false)
	private String template;
	
	@Override
	protected Object doExecute() throws Exception {
		loadFacilityManager();
		loadCXF();
		if(manager != null){
			ResourceData r = getResourceByName(resourceName);
			if (r != null) {
				System.out.println(manager.createInstanceInHost(r.getId(), vmName, memory, cpu, template));
			}else
				System.out.println("No resource found for name " + resourceName);
		}
		return null;
	}

	private ResourceData getResourceByName(String name) {
		for(ResourceManagerData man : manager.getManagers().getResourceManagers())
		{
			List<ResourceData> rlist = man.getResources();
			if (rlist != null){
				Iterator<ResourceData> it = rlist.iterator();
				while (it.hasNext()) {
					ResourceData r = it.next();
					if (r.getName().compareTo(name) == 0)
						return r;
				}
			}
		}
		return null;
	}
			

	
	private void loadFacilityManager(){
		try {
			manager = (ICloudManager) RegistryUtil.getServiceFromRegistry(
					getBundleContext(), ICloudManager.class.getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load CXF transport files
	 */
	private void loadCXF() {
		ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
			BusFactory.class.getClassLoader());
			SpringBusFactory springBusFactory = new SpringBusFactory();
			Bus bus = springBusFactory.createBus(new String[]
				{ "META-INF/cxf/cxf.xml", "META-INF/cxf/cxf-extension-soap.xml",
				  "META-INF/cxf/cxf-extension-http-jetty.xml" }, false);
			// The last parameter is telling CXF not to load the cxf-extension-*.xml from META-INF/cxf
			// You can set the bus the normal CXF endpoint or other camel-cxf endpoint
		} finally {
			Thread.currentThread().setContextClassLoader(oldCL);
		}
	}

}
