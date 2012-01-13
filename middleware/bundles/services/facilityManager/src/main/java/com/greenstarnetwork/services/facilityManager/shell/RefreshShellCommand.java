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
package com.greenstarnetwork.services.facilityManager.shell;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.greenstarnetwork.services.facilityManager.IFacilityManager;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
import com.iaasframework.resources.core.RegistryUtil;

/**
 * 
 * @author knguyen
 *
 */
@Command(scope = "gsn-facilityManager", name = "refresh", description="Refresh a resource data")
public class RefreshShellCommand extends OsgiCommandSupport {

	private IFacilityManager manager;
	
	@Argument(index = 0, name = "resourceName", description = "resourceName", required = true, multiValued = false)
	private String resourceName;
	
	@Override
	protected Object doExecute() throws Exception {
		loadFacilityManager();
		loadCXF();
		if(manager != null){
			ResourceData r = manager.getManagers().getResourceByName(resourceName);
			if (r != null) {
				System.out.println(manager.executeAction(r.getId(), "RefreshAction", "resourceID " + r.getId()));
				System.out.println(manager.getResourceModel(r.getType(), r.getId()));
			}else
				System.out.println("No resource found for name " + resourceName);
		}
		return null;
	}
	
	private void loadFacilityManager(){
		try {
			manager = (IFacilityManager) RegistryUtil.getServiceFromRegistry(
					getBundleContext(), IFacilityManager.class.getName());
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
