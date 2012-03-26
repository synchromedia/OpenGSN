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
package com.opengsn.services.facilitymanager;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.opengsn.services.facilitymanager.model.FacilityModel;
import com.opengsn.services.facilitymanager.model.OperationalSpecs;


/**
 * Manager of all facility resources running on different domains
 * @author knguyen
 *
 */
public class MockFacilityManagerProvider implements IFacilityManagerProvider 
{
	Logger logger = LoggerFactory.getLogger(MockFacilityManagerProvider.class);		
	
	private List<String> facilityTypes = null;			//resource types
	private List<String> pduTypes = null;				//resource types
	private List<String> powersourceTypes = null;		//resource types
	private List<String> climateTypes = null;			//resource types
		
	private OperationalSpecs warningThreshold = null;	//Threshold by which the Facility Manager has to send warning messages
	
	
	public MockFacilityManagerProvider(){
		initResourceTypes();
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
		//PowerSource resource
		powersourceTypes = new ArrayList<String>();
		powersourceTypes.add("OutbackMate");
		//Climate resource
		climateTypes = new ArrayList<String>();
		climateTypes.add("Climate");
	}
	

	/**
	 * Retrieve the model of a resource
	 * @param resourceId	id of the resource
	 * @return	Resource Model
	 */
//	public String getResourceModel(String type, String domainId, String id) {
	/*	if(managers != null) {
				String model = managers.getModel(domainId, id);
				if (model != null) {
					ResourceData r = managers.getResource(type, id);
					return this.addResourceInfoToModel(model, domainId, id, r.getName(), r.getState().toString());
				}
		} */
//		return null; 
//	}

	/**
	 * Retrieve the model of a resource
	 * @param resourceId	id of the resource
	 * @return	Resource Model
	 */
//	@WebMethod
	public String getResourceModel(String type, String id) {
	/*	if(managers != null) {
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
		*/return null;
	}
	
	/**
	 * Execute an action on a resource
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
//	@WebMethod
	public String executeAction(String resourceId, String actionName, String parameters) throws FacilityManagerException
	{
	/*	if(getManagers() != null) {
			try {
				return getManagers().executeAction(resourceId, actionName, parameters);
			}catch (ActionException_Exception e) {
				throw new FacilityManagerException("Exception while executing action. " + e.toString());
			}
		}
		*/return null;
	}

	/**
	 * Get models of all resources belonging to a given type (ex, Facility, PDU, PowerSource)
	 * @param type
	 * @return
	 * @throws FacilityManagerException
	 */
	private List<String> getAllResourceByType(List<String> types) throws FacilityManagerException 
	{
	/*	if (this.getManagers() == null)
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
	*/ return null;}

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
	
	
	
	
//	@Override
//	public List<String> listAllFacilities()  throws FacilityManagerException {
//		return this.getAllResourceByType(this.facilityTypes);
//	}
//
//	@Override
//	public List<String> listAllPDUs()  throws FacilityManagerException {
//		return this.getAllResourceByType(this.pduTypes);
//	}
//
//	@Override
//	public List<String> listAllPowerSources()  throws FacilityManagerException {
//		return this.getAllResourceByType(this.powersourceTypes);
//	}
//	
//	@Override
//	public List<String> listAllClimates() throws FacilityManagerException {
//		return this.getAllResourceByType(this.climateTypes);
//	}
//
//	@Override
//	public String refreshResource(String resourceId)
//			throws FacilityManagerException {
//		return this.executeAction(resourceId, "RefreshAction", resourceId);
//	}
//
//	@Override
//	public String turnOffResource(String resourceId)
//			throws FacilityManagerException {
//		return this.executeAction(resourceId, "OffAction", null);
//	}
//
//	@Override
//	public String turnOnResource(String resourceId)
//			throws FacilityManagerException {
//		return this.executeAction(resourceId, "OnAction", null);
//	}

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
	 * Update data model of a given Facility
	 * @param resourceId
	 * @param newModel
	 */
	public void updateFaciltyModel(String resourceId, String domainId, FacilityModel newModel) {
		//		controllerJMS.sendModelToQueue(newModel);
		
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
		
		return true;
	}

	/**
	 * Return the archive in a period of time
	 */
//	@WebMethod
	public String getArchiveDataByRangDate(String startDate, String endDate,
			String resourceID) {
		if ( (startDate == null) || (endDate == null) || (resourceID == null))
			return null;
	//	return this.managers.getArchiveDataByRangDate(startDate, endDate, resourceID);
	return null;
	}

	@Override
	public String readModel(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listResourcesByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(String engineId, String executionName,
			Map<String, String> parameters) throws FacilityManagerException {
		System.out.println("Executing Engine "+engineId+" Command: " + executionName + " with arguments: "+parameters.toString());
		return "";
	}

	@Override
	public String getMetricsDataByRangDate(String startDate, String endDate,
			String metricsID) {
		// TODO Auto-generated method stub
		return null;
	}
}
