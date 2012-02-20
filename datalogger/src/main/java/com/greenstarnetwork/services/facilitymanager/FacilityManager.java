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
package com.greenstarnetwork.services.facilitymanager;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

import com.greenstarnetwork.services.facilitymanager.model.FacilityModel;

/**
 * Manager of all facility resources running on different domains
 * 
 * @author Kim Nguyen (ETS)
 * @author Mathieu Lemay (IT)
 */
public class FacilityManager implements IFacilityManager {
	Logger logger = LoggerFactory.getLogger(FacilityManager.class);

	private List<String> facilityTypes = null; // resource types
	private List<String> pduTypes = null; // resource types
	private List<String> powersourceTypes = null; // resource types
	private List<String> climateTypes = null; // resource types

	IFacilityManagerProvider provider;

	public FacilityManager(IFacilityManagerProvider prov) {
		provider = prov;
		initResourceTypes();
	}

	/**
	 * Add the types of resources managed by FacilityManager, e.g., facililty,
	 * pdu(s), powersource and climate resoures
	 */
	private void initResourceTypes() {
		// Facility resource
		facilityTypes = new ArrayList<String>();
		facilityTypes.add("facilityResource");
		// PDU resource
		pduTypes = new ArrayList<String>();
		pduTypes.add("ServerTechCWG");
		pduTypes.add("RaritanPCR8");
		// PowerSource resource
		powersourceTypes = new ArrayList<String>();
		powersourceTypes.add("OutbackMate");
		// Climate resource
		climateTypes = new ArrayList<String>();
		climateTypes.add("Climate");
	}

	/**
	 * Retrieve the model of a resource
	 * 
	 * @param resourceId
	 *            id of the resource
	 * @return Resource Model
	 */
	public String getResourceModel(String type, String id) {
		return provider.readModel(id);
	}

	/**
	 * Execute an action on a resource
	 * 
	 * @param resourceId
	 *            id of the resource
	 * @param actionName
	 *            name of the action to execute
	 * @param parameters
	 *            parameters used by the action
	 * @return Action Result
	 */
	public String executeAction(String resourceId, String actionName, String parameters)
			throws FacilityManagerException {
		Map<String, String> params = new HashMap<String, String>();
		String[] parameterElement = parameters.trim().split(" ");
		for (String el : parameterElement) {
			String[] keyValueTuple = el.split("-");
			if (keyValueTuple.length > 1)
				params.put(keyValueTuple[0], keyValueTuple[1]);
		}
		return provider.execute(resourceId, actionName, params);
	}

	/**
	 * Get models of all resources belonging to a given type (ex, Facility, PDU,
	 * PowerSource)
	 * 
	 * @param type
	 * @return
	 * @throws FacilityManagerException
	 */
	private List<String> getAllResourceByType(List<String> types) throws FacilityManagerException {
		List<String> rlist = new LinkedList<String>();
		for (int pi = 0; pi < types.size(); pi++) {
			rlist = provider.listResourcesByType(types.get(pi));
		}
		return rlist;
	}

	/**
	 * Add domainID, resourceID, name, and status of a resource to the string
	 * representing its model (FacilityModel)
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	private String addResourceInfoToModel(String model, String domainId, String id, String name, String state) {
		try {
			// Add the ressource id in the resource model
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(model));
			Document doc = db.parse(is);

			Element elmtDomainId = doc.createElement("domainId");
			elmtDomainId.appendChild(doc.createTextNode(domainId));
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
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
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
	public List<String> listAllFacilities() throws FacilityManagerException {
		return this.getAllResourceByType(this.facilityTypes);
	}

	@Override
	public List<String> listAllPDUs() throws FacilityManagerException {
		return this.getAllResourceByType(this.pduTypes);
	}

	@Override
	public List<String> listAllPowerSources() throws FacilityManagerException {
		return this.getAllResourceByType(this.powersourceTypes);
	}

	@Override
	public List<String> listAllClimates() throws FacilityManagerException {
		return this.getAllResourceByType(this.climateTypes);
	}

	@Override
	public String refreshResource(String resourceId) throws FacilityManagerException {
		return this.executeAction(resourceId, "RefreshAction", resourceId);
	}

	@Override
	public String turnOffResource(String resourceId) throws FacilityManagerException {
		return this.executeAction(resourceId, "OffAction", null);
	}

	@Override
	public String turnOnResource(String resourceId) throws FacilityManagerException {
		return this.executeAction(resourceId, "OnAction", null);
	}

	/**
	 * Calculate operating hour of a facility resource
	 * 
	 * @param resourceId
	 * @return
	 * @throws FacilityManagerException
	 */
	public String calculateOpHour(String resourceId) throws FacilityManagerException {
		return this.executeAction(resourceId, "OPHourCalculateAction", null);
	}

	/**
	 * Update data model of a given Facility
	 * 
	 * @param resourceId
	 * @param newModel
	 */
	public void updateFaciltyModel(String resourceId, String domainId, FacilityModel newModel) {
		// controllerJMS.sendModelToQueue(newModel);

	}

	/**
	 * Register a Controller with this Manager
	 * 
	 * @param address
	 * @return
	 */
	public boolean registerController(String address) {
		logger.debug("************* Register controller IP: " + address);

		return true;
	}

	/**
	 * Return the archive in a period of time
	 */
	// @WebMethod
	public String getArchiveDataByRangDate(String startDate, String endDate, String resourceID) {
		if ((startDate == null) || (endDate == null) || (resourceID == null))
			return provider.getMetricsDataByRangDate(startDate, endDate, resourceID);
		else
			return null;
	}
}
