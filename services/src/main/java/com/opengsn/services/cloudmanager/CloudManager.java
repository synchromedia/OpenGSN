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
package com.opengsn.services.cloudmanager;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.opengsn.services.cloudmanager.model.Host;
import com.opengsn.services.cloudmanager.model.VM;
import com.opengsn.services.cloudmanager.model.VirtualMachine;
import com.opengsn.services.cloudmanager.model.VmHostModel;
import com.opengsn.services.networkmanager.model.VMNetAddress;
import com.opengsn.services.utils.ObjectSerializer;

@WebService
public class CloudManager implements ICloudManager {
	/**
	 * Provider backend to the cloud manager service.
	 */
	private ICloudManagerProvider provider;
	/**
	 * Store Hosts in a LinkedList, this list is not thread-safe and cannot be
	 * accessed concurrently. The best would be a TreeMap but using a list will
	 * have less impact on code changes.
	 */
	private List<Host> hostsList = new LinkedList<Host>();
	/**
	 * Logger for the Cloud Manager Service
	 */
	private Logger logger = LoggerFactory.getLogger(CloudManager.class);
	/**
	 * Host Priority Comparator
	 */
	private HostPriorityComparator priorityComparator = new HostPriorityComparator();

	public CloudManager(ICloudManagerProvider cloudManager) {
		provider = cloudManager;
		initCloudManager();
	}

	/**
	 * Cloud Manager initialization
	 */
	public void initCloudManager() {
		// Populate & priorize hosts in the cloud manager host model
		updateHostModel();
		updateHostPriority();
	}

	/**
	 * Priorize Hosts in the cloud manager host model -- used by cloud manager
	 * only
	 */
	private void updateHostPriority() {
		int priority = 0;
		Iterator<Host> itr = hostsList.iterator();
		logger.debug("\n------------Hosts priority-----------------\n");
		while (itr.hasNext()) {
			priority++;
			Host host = itr.next();
			host.setHostPriority(priority);
			logger.debug("\nHost id: " + host.getResourceId() + ", Host priority: " + host.getHostPriority());
		}
		Collections.sort(hostsList, priorityComparator);
		logger.debug("\n------------End Hosts priority-----------------\n");
	}

	/**
	 * Utility function used to have the most prioritary host with enough memory
	 * and cpu to deploy a VM
	 * 
	 * @param memory
	 *            needed memory
	 * @param CPUNumber
	 *            needed CPUs number
	 * @return Host ID
	 */
	private String getPrioritaryHost(int memory, int CPUNumber) {
		String hostId = null;
		boolean found = false;
		Iterator<Host> itr = hostsList.iterator();
		while (itr.hasNext() && (found != true)) {
			Host host = itr.next();
			if (host.getIdleMemory() >= memory && host.getIdleProcessors() >= CPUNumber) {
				hostId = host.getResourceId();
				found = true;
			}
		}
		return hostId;
	}

	/**
	 * Create a virtual machine by letting the cloud manager finding the most
	 * appropriate host
	 * 
	 * @param instanceName
	 *            Virtual Machine name
	 * @param memory
	 *            Memory Amount needed by the Virtual Machine
	 * @param cpu
	 *            Number of CPUs needed by the Virtual Machine
	 * @param template
	 *            template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name,
	 *         ip) if request successes, error message otherwise
	 */
	public String createInstance(String instanceName, String memory, String cpu, String template) {
		// Determine the host where to deploy the VM
		String hostId = getPrioritaryHost(Integer.parseInt(memory), Integer.parseInt(cpu));

		if (hostId == null) {
			return "Error: No Host availaible for creating a new VM";
		}

		// Check if the target host has enough memory, cpu, ...
		if (getHost(hostId).hasReachedMaxVM()) {
			return "Error: Max VM reached. cannot add VM in host: " + hostId;
		}

		int cpuNumber = getHost(hostId).getIdleProcessors();
		if (cpuNumber < Integer.parseInt(cpu)) {
			return "Error: Max CPU reached. cannot add VM in host: " + hostId;
		}

		int idleMemory = getHost(hostId).getIdleMemory();
		if (idleMemory < Integer.parseInt(memory)) {
			return "Error: Max Memory reached. cannot add VM in host: " + hostId;
		}

		String vmName = instanceName + "_" + template;
		String timestamp = new Long(new Date().getTime()).toString();
		vmName += "_" + timestamp;

		try {
			VMNetAddress vmnet = provider.getNetworkAddress(vmName);
			/*
			 * --------------------DEPRECATED-----------------------------
			 * String params = "-vmName " + vmName + " -memory " + memory +
			 * " -cpu " + cpu + " -mac " + vmnet.getMac() + " -ip " +
			 * vmnet.getIp() + " -template " + template;
			 * --------------------------------------------------------------
			 */
			Map<String, String> params = new HashMap<String, String>();
			params.put("vmName", vmName);
			params.put("memory", memory);
			params.put("numCpus", cpu);
			params.put("ipAddress", vmnet.getIp());
			params.put("mac", vmnet.getMac());
			params.put("template", template);

			String msg = provider.execute(hostId, "DeployNewVM", params);
			// Update cloud manager host model

			if (msg.split(" ")[0] == "VM:" && msg.split(" ")[4] == "created!") {
				VirtualMachine vm = new VirtualMachine();
				vm.setCpuNumber(Integer.parseInt(cpu));
				vm.setMemory(Integer.parseInt(memory));
				vm.setName(msg.split(" ")[1]);
				vm.setIp(vmnet.getIp());
				getHost(hostId).getVMs().add(vm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msg = ""; // Compile Hack
		return msg + "; hostId = " + hostId;

	}

	/**
	 * Populate/Update Hosts model used by the cloud manager for VM
	 * provisionning. if domainId == null, the cloud manager model will be
	 * initiliazed by the hosts from all domains. if domainId != null, The hosts
	 * in the domainId will be added to the cloud manager model.
	 * 
	 * @param domainId
	 *            domain ID
	 */
	private void updateHostModel() {
		List<Host> hosts;
		hostsList = provider.listAllHosts();
	}

	/**
	 * Retrieve the model of a Host
	 * 
	 * @param hostId
	 *            id of the host (== id of the resource)
	 * @return Host Model
	 */
	public String fetchHost(String hostId) {

		try {
			// String data = iaasTemplate.sendAndReceive(arg0);
			String data = "";
			data = retrieveVMIPs(data, hostId);

			// Add the ressource id in the host model
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(data));
			Document doc = db.parse(is);

			Element elmtDomainId = doc.createElement("domainId");
			// elmtDomainId.appendChild(doc.createTextNode(manager.getId()));
			doc.getFirstChild().appendChild(elmtDomainId);

			Element elmtResourceId = doc.createElement("resourceId");
			elmtResourceId.appendChild(doc.createTextNode(hostId));
			doc.getFirstChild().appendChild(elmtResourceId);

			Element elmtResourceName = doc.createElement("resourceName");
			// elmtResourceName.appendChild(doc.createTextNode(resource.getName()));
			doc.getFirstChild().appendChild(elmtResourceName);

			Element elmtResourceState = doc.createElement("resourceState");
			// String state = resource.getState().value();

			// elmtResourceState.appendChild(doc.createTextNode(state));

			doc.getFirstChild().appendChild(elmtResourceState);
			doc.normalize();

			Source source = new DOMSource(doc);
			StreamResult result = new StreamResult(new StringWriter());
			Transformer xformer = TransformerFactory.newInstance().newTransformer();

			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			xformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			return xmlString;
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String describeHost(String hostId) {
		Host host = provider.getHostInformation(hostId);
		return ObjectSerializer.toXml(host);
	}

	/**
	 * Override Host priorities in the cloud manager host model
	 * 
	 * @param priorityTable
	 *            Map with host priorities (String = host resource ID, Integer =
	 *            host priority)
	 */
	@Override
	public void overrideHostPriorities(HashMap<String, Integer> priorityTable) {
		Iterator<Host> itr = hostsList.iterator();
		while (itr.hasNext()) {
			Host host = itr.next();
			host.setHostPriority(priorityTable.get(host.getResourceId()));
		}
		Collections.sort(hostsList, priorityComparator);
	}

	/**
	 * Start a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String startInstance(String hostId, String instanceName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", instanceName);
		return provider.execute(hostId, "StartAction", params);
	}

	/**
	 * Stop a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String stopInstance(String hostId, String instanceName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", instanceName);
		return provider.execute(hostId, "StopAction", params);
	}

	/**
	 * Reboot a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String rebootInstance(String hostId, String instanceName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", instanceName);
		return provider.execute(hostId, "RebootAction", params);
	}

	/**
	 * Shutdown a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String shutdownInstance(String hostId, String instanceName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", instanceName);
		return provider.execute(hostId, "ShutdownAction", params);
	}

	/**
	 * Request Network Manager for an IP address and then inject it into a VM
	 * sections of a Host model
	 * 
	 * @param xmlModel
	 * @return
	 */
	private String retrieveVMIPs(String hostModelXml, String resourceId) {
		VmHostModel hostModel = VmHostModel.fromXML(hostModelXml);
		List<VM> vms = hostModel.getVMList();
		if (vms != null) {
			java.util.Iterator<VM> it = vms.iterator();
			Map<Object, Object> args = new Hashtable<Object, Object>();
			while (it.hasNext()) {
				VM vm = it.next();
				if (vm.getIp() == null) {
					args.put((String) "vmName", vm.getName());
					args.put((String) "vmMAC", vm.getMac());
					VMNetAddress vmnet = provider.getNetworkAddress(vm.getName());
					vm.setIp(vmnet.getIp()); // set new IP to the VM model
					Map<String, String> params = new HashMap<String, String>();
					params.put("vmName", vm.getName());
					params.put("ip", vmnet.getIp());
					provider.execute(resourceId, "SetVMIP", params);
				}
			}
		}
		return hostModel.toXML();
	}

	/**
	 * Utility function used by hostQuery function
	 * 
	 * @param key
	 *            A host attribute
	 * @param value
	 *            Value of the host attribute
	 * @param xmlData
	 *            A host XML description
	 * @return true if parameters matches, false otherwise
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private boolean areEquals(String key, String value, String xmlData) throws ParserConfigurationException,
			SAXException, IOException {

		// Parse data
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlData));
		Document doc = db.parse(is);

		if (key.compareTo("ip") == 0) {

			NodeList nodes = doc.getElementsByTagName("address");
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					if (nodes.item(i).getFirstChild().getNodeValue().compareTo(value) == 0) {
						return true;
					}
				}
			}
		} else if (key.compareTo("location") == 0) {

			NodeList nodes = doc.getElementsByTagName("location");
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					if (nodes.item(i).getFirstChild().getNodeValue().compareTo(value) == 0) {
						return true;
					}
				}
			}

		} else if (key.compareTo("freeMemory") == 0) {

			NodeList nodes = doc.getElementsByTagName("freeMemory");
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					String mem = nodes.item(i).getFirstChild().getNodeValue();
					if (Integer.parseInt(mem.substring(0, mem.length() - 1)) >= Integer.parseInt(value)) {
						return true;
					}
				}
			}

		} else if (key.compareTo("availableCPU") == 0) {

			NodeList nodes = doc.getElementsByTagName("nbrCPUs");
			int nbreCPU = 0;
			if (nodes != null) {
				nbreCPU = Integer.parseInt(nodes.item(0).getFirstChild().getNodeValue());
			} else {
				return false;
			}

			nodes = doc.getElementsByTagName("vcpu");
			int usedCPU = 0;
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					usedCPU += Integer.parseInt(nodes.item(i).getFirstChild().getNodeValue());
				}
			}

			int freeCPU = nbreCPU - usedCPU;

			if (freeCPU >= Integer.parseInt(value)) {
				return true;
			}

		} else if (key.compareTo("vmUUID") == 0) {

			NodeList nodes = doc.getElementsByTagName("uuid");
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getFirstChild().getNodeValue().compareTo(value) == 0) {
					return true;
				}
			}

		}

		return false;
	}

	/**
	 * Destroy a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @return Success or error message
	 */
	public String destroyInstance(String hostId, String instanceName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", instanceName);
		String ret = provider.execute(hostId, "DeleteAction", params);
		if ((ret.indexOf("Error") > -1) || (ret.indexOf("Exception") > -1)) {
			return ret;
		}

		// Update cloud manager host model
		getHost(hostId).removeVM(instanceName);

		// release a IP - VM assignment
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put((String) "vmName", instanceName);
		/**
		 * ResponseMessage nres = network_manager.executeCommand(
		 * INetworkManager.CMD_releaseAssignment, args); if (nres.isError()) {
		 * return nres.getMessage(); } } catch (NetworkManagerException e) {
		 * e.printStackTrace(); }
		 **/
		return ret;
	}

	/**
	 * migrate a virtual machine
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @param destinationHostId
	 *            id of the destination host (id of the IAAS destination
	 *            resource)
	 * @return Success or error message
	 */
	public String migrateInstance(String hostId, String instanceName, String destinationHostId) {

		logger.debug("CloudManager -------------------- migrateInstance called");
		VirtualMachine vm = getHost(hostId).getVM(instanceName);

		// Check if the target host has enough memory, cpu, ...
		// if( model.get( destinationHostId ).hasReachedMaxVM() ){
		// return "Error: Max VM reached. cannot Migrate VM in host: " +
		// destinationHostId;
		// }
		//
		// int cpuNumber = model.get( destinationHostId ).getIdleProcessors();
		// if( cpuNumber < vm.getCpuNumber() ){
		// return "Error: Max CPU reached. cannot Migrate VM in host: " +
		// destinationHostId;
		// }
		//
		// int idleMemory = model.get( destinationHostId ).getIdleMemory();
		// if( idleMemory < vm.getMemory() ){
		// return "Error: Max Memory reached. cannot add Migrate in host: " +
		// destinationHostId;
		// }

		String destHostIP = getHost(destinationHostId).getIp();
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", instanceName);
		params.put("destHost", destHostIP);
		String ret = provider.execute(hostId, "MigrateAction", params);
		logger.debug("MigrateAction response: " + ret);

		if ((ret.toLowerCase().indexOf("error") > -1) || (ret.toLowerCase().indexOf("exception") > -1)) {
			return ret;
		} else {
			params.clear();
			params.put("vmName", instanceName);

			String r = provider.execute(destinationHostId, "RefreshAction", params);
			logger.debug("RefreshAction response: " + r);
			r = provider.execute(destinationHostId, "SetVMIPAction", params);
			logger.debug("SetVMIPAction response: " + r);
			// //Update cloud manager host model
			getHost(hostId).removeVM(instanceName);
			getHost(destinationHostId).getVMs().add(vm);
		}

		return ret;
	}

	/**
	 * Create a virtual machine in a specified host
	 * 
	 * @param hostId
	 *            id of the host (id of the IAAS resource)
	 * @param instanceName
	 *            Virtual Machine name
	 * @param memory
	 *            Memory Amount needed by the Virtual Machine
	 * @param cpu
	 *            Number of CPUs needed by the Virtual Machine
	 * @param template
	 *            template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name,
	 *         ip) if request successes, error message otherwise
	 */
	public String createInstanceInHost(String hostId, String instanceName, String memory, String cpu, String template) {
		// Check if the target host has enough memory, cpu, ...
		if (getHost(hostId).hasReachedMaxVM()) {
			return "Error: Max VM reached. cannot add VM in host: " + hostId;
		}

		int cpuNumber = getHost(hostId).getIdleProcessors();
		if (cpuNumber < Integer.parseInt(cpu)) {
			return "Error: Max CPU reached. cannot add VM in host: " + hostId;
		}

		int idleMemory = getHost(hostId).getIdleMemory();
		if (idleMemory < Integer.parseInt(memory)) {
			return "Error: Max Memory reached. cannot add VM in host: " + hostId;
		}

		String vmName = instanceName + "_" + template;
		String timestamp = new Long(new Date().getTime()).toString();
		vmName += "_" + timestamp;
		Map<String, String> args = new Hashtable<String, String>();
		args.put((String) "vmName", vmName);
		VMNetAddress vmnet = provider.getNetworkAddress(vmName);

		Map<String, String> params = new HashMap<String, String>();
		params.put("vmName", vmName);
		params.put("memory", memory);
		params.put("numCpus", cpu);
		params.put("ipAddress", vmnet.getIp());
		params.put("mac", vmnet.getMac());
		params.put("template", template);

		String msg = provider.execute(hostId, "DeployNewVM", params);
		return msg;
	}

	private Host getHost(String hostId) {
		Iterator<Host> itr = hostsList.iterator();
		while (itr.hasNext()) {
			Host host = itr.next();
			if (host.getResourceId() == hostId)
				return host;
		}
		return null;
	}

	@Override
	public String setInstanceIPAddress(String hostId, String instanceName) {
		VMNetAddress vmnet = provider.getNetworkAddress(instanceName);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", vmnet.getIp());
		provider.execute(hostId, "AssignVMIP", params);
		return vmnet.getIp();
	}

	@Override
	public List<String> hostQuery(String ip, String location, String freeMemory, String availableCPU,
			String vmUUID) {
		List<Host> hostsObj = provider.hostQuery(ip, location, freeMemory, availableCPU, vmUUID);
		List<String> hostStr = new LinkedList<String>();
		for (Host host : hostsObj) {
			hostStr.add(ObjectSerializer.toXml(host));
		}
		return hostStr;
	}

	@Override
	public List<String> describeHosts() {
		List<Host> hostsObj = provider.listAllHosts();
		List<String> hostStr = new LinkedList<String>();
		for (Host host : hostsObj) {
			hostStr.add(ObjectSerializer.toXml(host));
		}
		return hostStr;
	}

	@Override
	public String executeAction(String resourceId, String actionName, String parameters) {
		Map<String, String> params = new HashMap<String, String>();
		String[] parameterElement = parameters.trim().split(" ");
		for (String el : parameterElement) {
			String[] keyValueTuple = el.split("-");
			if (keyValueTuple.length > 1)
				params.put(keyValueTuple[0], keyValueTuple[1]);
		}
		return provider.execute(resourceId, actionName, params);
	}

}
