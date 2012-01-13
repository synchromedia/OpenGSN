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
package com.greenstarnetwork.services.facilityManager;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.facilityModel.OperationalSpecs;
import com.greenstarnetwork.services.facilityManager.jms.ControllerJMSQueue;
import com.greenstarnetwork.services.facilityManager.jms.ResourceJMSQueueManager;
import com.greenstarnetwork.services.facilityManager.model.ResourceManager;
import com.greenstarnetwork.services.facilityManager.model.ResourceManagersList;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionException_Exception;
import com.iaasframework.capabilities.model.soapendpoint.ModelException_Exception;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceException_Exception;

/**
 * Manager of all facility resources running on different domains
 * @author knguyen
 *
 */
public class FacilityManager implements IFacilityManager 
{
	Logger logger = LoggerFactory.getLogger(FacilityManager.class);		
	
	private List<String> facilityTypes = null;			//resource types
	private List<String> pduTypes = null;				//resource types
	private List<String> powersourceTypes = null;		//resource types
	private List<String> climateTypes = null;			//resource types
	
	private ResourceJMSQueueManager jmsManager = null;			//Manager of all resource jms queues
	
	private OperationalSpecs warningThreshold = null;	//Threshold by which the Facility Manager has to send warning messages
	
	private ControllerJMSQueue controllerJMS = null;	//Controller JMS queue handler
	
	/**
	 * List of Resources Managers 
	 */
	private ResourceManagersList managers = null;
	
	public FacilityManager(){
		initResourceTypes();
		loadCXF();
		initFacilityManager();
	}
	
	/**
	 * Add the types of resources managed by FacilityManager, e.g., facililty, pdu(s), powersource and climate resoures 
	 */
	private void initResourceTypes() 
	{
		//Facility resource
		facilityTypes = new ArrayList<String>();
		facilityTypes.add("facilityResource");
		//PDU resource
		pduTypes = new ArrayList<String>();
		pduTypes.add("ServerTechCWG");
		pduTypes.add("RaritanPCR8");
		pduTypes.add("eatonPW");
		//PowerSource resource
		powersourceTypes = new ArrayList<String>();
		powersourceTypes.add("OutbackMate");
		//Climate resource
		climateTypes = new ArrayList<String>();
		climateTypes.add("Climate");
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

	/**
	 * Initialize JMS queue manager
	 */
	private void initJMSManager() 
	{
		this.jmsManager = new ResourceJMSQueueManager();
		this.jmsManager.setMan(this);
	}
	
	/**
	 * Manager initialization
	 */
	public void initFacilityManager(){
		if (managers != null) {
			managers.getResourceManagers().clear();
			managers = null;
		}
		loadResourceManagerData();
		if (jmsManager != null) {
			jmsManager.dispose();
			jmsManager = null;
		}
		initJMSManager();
		initResourceManagers();
	}
	
	/**
	 * Populates managers attribute by reading data from XML File given in project resources.
	 */
	private void loadResourceManagerData(){	
		try {
			JAXBContext jc = JAXBContext.newInstance(ResourceManagersList.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File f = new File(System.getenv("IAAS_HOME")+"/gsn/domains.xml");
			if(f.exists()){
				managers = (ResourceManagersList)unmarshaller.unmarshal(f);
			}else{
				System.out.println("FacilityManager: No configuration file found!");
			}
						
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	/**
	 * Initializes soap endpoint clients for all Resources Managers and 
	 * populates each ResourceManagerData in ResourceManagersList 
	 * having that way all information about Resource Managers and their resources (with their id and ip) 
	 */
	public void initResourceManagers() {
	
		if((getManagers() != null) && (getManagers().getResourceManagers() != null)){
			for(ResourceManager manager : getManagers().getResourceManagers()){
				manager.setResourceManagerClient( WSClient.createManagerService(manager.getId()));
				manager.setModelCapabilityClient( WSClient.createModelService(manager.getId()));
				manager.setActionSetCapabilityClient( WSClient.createActionSetService(manager.getId()));
				
				//Initialize JMS queue of each Facility resource in the domain
				for (int pi=0; pi < this.facilityTypes.size(); pi++)
				{
					List<ResourceData> resourcelist = manager.getResourceManagerClient().listReourcesByType(
							facilityTypes.get(pi));
					if (resourcelist != null) 
					{
						for (int pj=0; pj<resourcelist.size(); pj++) {
							try {
								this.jmsManager.addQueue(resourcelist.get(pj).getId(), manager.getId());
								logger.debug("Facility Manager is listening to queue: " + 
										resourcelist.get(pj).getId() + " of domain: " + manager.getId());
							}catch(Exception ex){
								logger.debug(ex.toString());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Retrieve the model of a resource
	 * @param resourceId	id of the resource
	 * @return	Resource Model
	 */
	@WebMethod
	public String getResourceModel(String type, String domainId, String id) {
		if(managers != null) {
				String model = managers.getModel(domainId, id);
				if (model != null) {
					ResourceData r = managers.getResource(type, id);
					return this.addResourceInfoToModel(model, domainId, id, r.getName(), r.getState().toString());
				}
		}
		return null;
	}

	/**
	 * Retrieve the model of a resource
	 * @param resourceId	id of the resource
	 * @return	Resource Model
	 */
	@WebMethod
	public String getResourceModel(String type, String id) {
		if(managers != null) {
			Iterator<ResourceManager> it = managers.getResourceManagers().iterator();
			while (it.hasNext()) 
			{
				ResourceManager rm = it.next();
				try {
					ResourceData r = rm.getResource(type, id);
					if (r != null)
					{
						return rm.getModel(id);
					}
				}catch (ResourceException_Exception e) {
				}catch (ModelException_Exception e) {
				}
			}
		}
		return null;
	}
	
	/**
	 * Execute an action on a resource
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	@WebMethod
	public String executeAction(String resourceId, String actionName, String parameters) throws FacilityManagerException
	{
		if(getManagers() != null) {
			try {
				return getManagers().executeAction(resourceId, actionName, parameters);
			}catch (ActionException_Exception e) {
				throw new FacilityManagerException("Exception while executing action. " + e.toString());
			}
		}
		return null;
	}

	/**
	 * Get models of all resources belonging to a given type (ex, Facility, PDU, PowerSource)
	 * @param type
	 * @return
	 * @throws FacilityManagerException
	 */
	private List<String> getAllResourceByType(List<String> types) throws FacilityManagerException 
	{
		if (this.getManagers() == null)
		{
			return null;
		}
		try {
			List<String> ret = new ArrayList<String>();
			for(ResourceManager manager : getManagers().getResourceManagers())
			{
				List<ResourceData> rlist = null;
				for (int pi = 0; pi < types.size(); pi++) 
				{
					if (rlist == null)
						rlist = manager.getResourceManagerClient().listReourcesByType(types.get(pi));
					else
						rlist.addAll(manager.getResourceManagerClient().listReourcesByType(types.get(pi)));
				}
				
				Iterator<ResourceData> it = rlist.iterator();
				while (it.hasNext()) {
					ResourceData r = it.next();
					String rmodel = manager.getModelCapabilityClient().getResourceModel(r.getId());
					ret.add(this.addResourceInfoToModel(rmodel, manager.getId(), r.getId(), r.getName(), r.getState().toString()));
				}
			}
			if(ret.size() == 0)
				return null;
			return ret;
		}catch (ModelException_Exception e) {
			throw new FacilityManagerException("Exception while getting resources. " + e.toString());
		}
	}

	/**
	 * Add domainID, resourceID, name, and status of a resource to the string representing its model (FacilityModel)
	 * @param model
	 * @param id
	 * @return
	 */
	private String addResourceInfoToModel(String model, String domainId, String id, String name, String state) 
	{
		try {
			//Add the ressource id in the resource model
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(model));
			Document doc = db.parse(is);

			Element elmtDomainId = doc.createElement("domainId");
			elmtDomainId.appendChild(doc.createTextNode( domainId ));
			doc.getFirstChild().appendChild(elmtDomainId);
			
			Element elmtResourceId = doc.createElement("resourceId");
			elmtResourceId.appendChild(doc.createTextNode(id));
			doc.getFirstChild().appendChild(elmtResourceId);

			Element elmtResourceName = doc.createElement("resourceName");
			elmtResourceName.appendChild(doc.createTextNode(name));
			doc.getFirstChild().appendChild(elmtResourceName);

			Element elmtResourceState = doc.createElement("resourceState");
			elmtResourceState.appendChild(doc.createTextNode(state));
			doc.getFirstChild().appendChild(elmtResourceState);

			doc.normalize();

			Source source = new DOMSource(doc);
			StreamResult result = new StreamResult(new StringWriter());
			    
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			xformer.transform(source, result); 

			return result.getWriter().toString();
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<String> listAllFacilities()  throws FacilityManagerException {
		return this.getAllResourceByType(this.facilityTypes);
	}

	@Override
	public List<String> listAllPDUs()  throws FacilityManagerException {
		return this.getAllResourceByType(this.pduTypes);
	}

	@Override
	public List<String> listAllPowerSources()  throws FacilityManagerException {
		return this.getAllResourceByType(this.powersourceTypes);
	}
	
	@Override
	public List<String> listAllClimates() throws FacilityManagerException {
		return this.getAllResourceByType(this.climateTypes);
	}

	@Override
	public String refreshResource(String resourceId)
			throws FacilityManagerException {
		return this.executeAction(resourceId, "RefreshAction", resourceId);
	}

	@Override
	public String turnOffResource(String resourceId)
			throws FacilityManagerException {
		return this.executeAction(resourceId, "OffAction", null);
	}

	@Override
	public String turnOnResource(String resourceId)
			throws FacilityManagerException {
		return this.executeAction(resourceId, "OnAction", null);
	}

	/**
	 * Calculate operating hour of a facility resource
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	public String calculateOpHour(String resourceId)  throws FacilityManagerException {
		return this.executeAction(resourceId, "OPHourCalculateAction", null);
	}

	/**
	 * Add a domain in the facility manager
	 * @param domainId ip/id of the domain
	 */
	public void addDomain(String domainId)
	{
		ResourceManager MngData = new ResourceManager();
		MngData.setId(domainId);
		
		MngData.setResourceManagerClient( WSClient.createManagerService(domainId) );
		MngData.setModelCapabilityClient( WSClient.createModelService(domainId) );
		MngData.setActionSetCapabilityClient( WSClient.createActionSetService(domainId) );

		if (getManagers() == null)
			managers = new ResourceManagersList();
		removeDomain(domainId);
		
		getManagers().getResourceManagers().add(MngData);
	}
	
	/**
	 * Remove a domain from the facility manager
	 * @param domainId ip/id of the domain
	 */
	public void removeDomain(String domainId){
		 for(ResourceManager mgData: this.getManagers().getResourceManagers() ){
			 if(mgData.getId().compareTo(domainId) == 0){
				 this.getManagers().getResourceManagers().remove(mgData);
				 break;
			 }
		 }
	}
	
	/**
	 * Force the facility manager to load/reload domains that was updated by the domainManager
	 */
	public void initDomains(){
		this.initFacilityManager();
	}

	/**
	 * @return the managers
	 */
	public ResourceManagersList getManagers() {
		return managers;
	}
	
	/**
	 * Update data model of a given Facility
	 * @param resourceId
	 * @param newModel
	 */
	public void updateFaciltyModel(String resourceId, String domainId, FacilityModel newModel) {
		if (controllerJMS != null) {
			try {
				controllerJMS.sendModelToQueue(newModel);
			}catch(JMSException ex){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Set a operational threshold by which the Facility Manager has to send a warning message
	 * @param threshold
	 */
	public void setThreshold(OperationalSpecs threshold) {
		this.warningThreshold = threshold;
	}

	/**
	 * Register a Controller with this Manager
	 * @param address
	 * @return
	 */
	public boolean registerController(String address) 
	{
		logger.debug("************* Register controller IP: " + address);
		try {
			if (controllerJMS != null) {
				controllerJMS.dispose();
			}
			controllerJMS = new ControllerJMSQueue(address);
			controllerJMS.initializeJMS();
		}catch(JMSException ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Return the archive in a period of time
	 */
	@WebMethod
	public String getArchiveDataByRangDate(String startDate, String endDate,
			String resourceID) {
		if ( (startDate == null) || (endDate == null) || (resourceID == null))
			return null;
		return this.managers.getArchiveDataByRangDate(startDate, endDate, resourceID);
	}
}
