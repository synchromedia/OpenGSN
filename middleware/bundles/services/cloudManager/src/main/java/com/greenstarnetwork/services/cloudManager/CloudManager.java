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
package com.greenstarnetwork.services.cloudManager;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.greenstarnetwork.models.vmm.VM;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.services.cloudManager.jms.Consumer;
import com.greenstarnetwork.services.cloudManager.model.Host;
import com.greenstarnetwork.services.cloudManager.model.ResourceManagerData;
import com.greenstarnetwork.services.cloudManager.model.ResourceManagersList;
import com.greenstarnetwork.services.cloudManager.model.VirtualMachine;
import com.greenstarnetwork.services.networkManager.INetworkManager;
import com.greenstarnetwork.services.networkManager.NetworkManagerException;
import com.greenstarnetwork.services.networkManager.jms.NetworkManagerJMSClient;
import com.greenstarnetwork.services.networkManager.jms.ResponseMessage;
import com.greenstarnetwork.services.networkManager.model.VMNetAddress;
import com.iaasframework.core.resources.manager.soapendpoint.ResourceData;
import com.iaasframework.capabilities.model.soapendpoint.ModelException_Exception;
import com.iaasframework.capabilities.actionset.soapendpoint.ActionException_Exception;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ICloudManagerSOAPEndpoint;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ImportVM;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.ImportVMResponse;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.GetCloudProxyIP;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.GetCloudProxyUser;
import com.greenstarnetwork.services.cloudmanager.soapendpoint.GetCloudProxyStoragePath;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 *GreenStar Network - Cloud Manager
 *In this version, The cloud manager container needs to be started after other IAAS containers has started. 
 */
public class CloudManager implements ICloudManager{
	
	private Logger logger = LoggerFactory.getLogger(CloudManager.class);
	/**
	 * Hosts model used by the cloud manager for VM provisionning
	 * First parameter = Host ID
	 * Second parameter = Host model & priority
	 */
	private Hashtable<String,Host> model = null;
	
	/**
	 * List of Resources Managers in the cloud 
	 */
	private ResourceManagersList managers;
	
	private NetworkManagerJMSClient network_manager = null;				//NetworkManager connection handler
	
	//JMS broker port
	String bropkerPort = "61616";
	//JMS message topic
	private final String messageTopic = "channel1";
	//All jms consumers of all domains. Structure: <domainID, consumer>
	private Hashtable<String,Consumer> jmsConsumers = null;
	
	
	//TODO EXPORT
	private CloudProxy proxyHost = null;	//Host used to receive VM from external clouds
	/**
	 * Cloud Manager Constructor
	 */
	public CloudManager(){
		loadCXF();
		initCloudManager();
		//TODO EXPORT
		try {
			this.proxyHost = CloudProxy.loadCloudProxyConfig(System.getenv("IAAS_HOME")+"/gsn/cloudproxy.xml");
		}catch (Exception e) {
			System.out.println ("Exception while loading cloud proxy configuration file. Will generate a new one.");
			generateProxyHost();
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
	
	/**
	 * Cloud Manager initialization
	 */
	public void initCloudManager(){
		managers = null;
		loadResourceManagerData();
		initResourceManagers();
		initNetworkManagerConnection();
		
		//Populate & priorize hosts in the cloud manager host model
		updateHostModel(null);
		updateHostPriority();
	}

	/**
	 * Initialize network manager connection
	 */
	private void initNetworkManagerConnection() {
		this.network_manager = new NetworkManagerJMSClient();
	}
	
	/**
	 * Populates managers attribute by reading data from XML File given in project resources.
	 */
	private void loadResourceManagerData(){	
		try {
			JAXBContext jc = JAXBContext.newInstance(ResourceManagersList.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			//managers = (ResourceManagersList)unmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("resourceManagers.xml"));
			File f = new File(System.getenv("IAAS_HOME")+"/gsn/domains.xml");
			if(f.exists()){
				managers = (ResourceManagersList)unmarshaller.unmarshal(f);
			}else{
				System.out.println("CloudManager: No configuration file found!");
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
	 * 
	 * Initialize also jms messaging for each domain
	 */
	private void initResourceManagers() {

		this.jmsConsumers = new Hashtable<String,Consumer>();

		if(managers != null && managers.getResourceManagers() != null){
			for(ResourceManagerData manager : managers.getResourceManagers()){
				manager.setResourceManagerClient( WSClient.createManagerService(manager.getId()) );
				manager.setModelCapabilityClient( WSClient.createModelService(manager.getId()) );
				manager.setActionSetCapabilityClient( WSClient.createActionSetService(manager.getId()) );

				manager.setResources( manager.getResourceManagerClient().listReourcesByType("vmmEngine") );

				String brokerURL = "tcp://"+ manager.getId() + ":" + this.bropkerPort;
				try{
					Consumer consumer = new Consumer(brokerURL, this.messageTopic, this);
					jmsConsumers.put(manager.getId(), consumer);
					logger.debug("Cloud Manager: Added JMS Broker URL: " + brokerURL);
//					System.err.println("Cloud Manager: Added JMS Broker URL: " + brokerURL);
				}catch(Exception ex){
					System.err.println("Cloud Manager: Added JMS Broker URL: Exception : " + ex.toString());
				}

			}
		}
	}

	/**
	 * 
	 * @return	List of all IAAS Resources Managers
	 */
	public ResourceManagersList getManagers() {
		return managers;
	}
	
	/**
	 * Populate/Update Hosts model used by the cloud manager for VM provisionning.
	 * if domainId == null, the cloud manager model will be initiliazed by the hosts from all domains.
	 * if domainId != null, The hosts in the domainId will be added to the cloud manager model.
	 * @param domainId domain ID
	 */
	private void updateHostModel(String domainId){
		List<String> hosts;
		if(domainId == null){
			model = new Hashtable<String,Host>();
			hosts = describeHosts();
		}else{
			hosts = hostQuery(domainId, "-1", "-1", "-1", "-1", "-1");
		}
		
		if(hosts != null){
			
			java.util.Iterator<String> itr = hosts.iterator();
			while(itr.hasNext()) {
	
			    String hostModel = itr.next(); 
			    
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
				    InputSource is = new InputSource();
			        is.setCharacterStream(new StringReader(hostModel));
					Document doc = db.parse(is);
					
					Host host = new Host();
					host.setHostPriority(1);
				   
					host.setTotalProcessors( Integer.parseInt( doc.getElementsByTagName("nbrCPUs").item(0).getFirstChild().getNodeValue() ) );
					host.setMaxVM( Integer.parseInt( doc.getElementsByTagName("maxVMs").item(0).getFirstChild().getNodeValue() ) );
					host.setIp( doc.getElementsByTagName("address").item(0).getFirstChild().getNodeValue() );
					host.setResourceId( doc.getElementsByTagName("resourceId").item(0).getFirstChild().getNodeValue() );
					host.setState( doc.getElementsByTagName("resourceState").item(0).getFirstChild().getNodeValue() );
					
					String memory = doc.getElementsByTagName("totalMemory").item(0).getFirstChild().getNodeValue();
//					memory = memory.substring(0, memory.length() - 3 );
					host.setTotalMemory( Integer.parseInt( memory ) );
					
					String speed = doc.getElementsByTagName("speed").item(0).getFirstChild().getNodeValue();
//					speed = speed.substring(0, speed.length() - 4 );
					host.setProcessorSpeed( Integer.parseInt( speed ) );
					
					List<VirtualMachine> VMs = new ArrayList<VirtualMachine>();
					NodeList listVM = doc.getElementsByTagName("vm");
					if(listVM != null){
						for(int i=0;i<listVM.getLength();i++){
							VirtualMachine vm = new VirtualMachine();
							NodeList vmProperties = listVM.item(i).getChildNodes();
							if(vmProperties != null){
								for(int j=0;j<vmProperties.getLength();j++){
									if(vmProperties.item(j).getNodeName().compareTo("name") == 0 ){
										vm.setName( vmProperties.item(j).getFirstChild().getNodeValue() );
									}
									if(vmProperties.item(j).getNodeName().compareTo("vcpu") == 0 ){
										vm.setCpuNumber( Integer.parseInt( vmProperties.item(j).getFirstChild().getNodeValue() ) );
									}
									if(vmProperties.item(j).getNodeName().compareTo("memory") == 0 ){
										String mem = vmProperties.item(j).getFirstChild().getNodeValue();
//										mem = mem.substring(0, mem.length() - 3 );
										vm.setMemory( Integer.parseInt(mem) );
									}
									if(vmProperties.item(j).getNodeName().compareTo("ip") == 0 ){
										vm.setIp(vmProperties.item(j).getFirstChild().getNodeValue());
									}
									if(vmProperties.item(j).getNodeName().compareTo("template") == 0 ){
										vm.setTemplate( vmProperties.item(j).getFirstChild().getNodeValue() );
									}
								}
							}
							VMs.add(vm);
						}
					}
					host.setVMs(VMs);
					
				    model.put(host.getResourceId(), host);
			    
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			} 
		}
	
	}
	
	/**
	 * Priorize Hosts in the cloud manager host model  -- used by cloud manager only
	 */
	private void updateHostPriority(){
		int priority = 0;
		Set<String> keys = model.keySet();
		if(keys != null){
			java.util.Iterator<String> itr = keys.iterator();
			while(itr.hasNext()) {
				priority++;
			    String key = itr.next();
			    Host host = model.get(key);
			    host.setHostPriority(priority);
			}
		}
		
		logger.debug("\n------------Hosts priority-----------------\n");
		keys = model.keySet();
		if(keys != null){
			java.util.Iterator<String> itr = keys.iterator();
			while(itr.hasNext()) {
			    String key = itr.next();
			    Host host = model.get(key);
			    logger.debug("\nHost id: "+host.getResourceId()+", Host priority: "+host.getHostPriority());
			}
		}
		logger.debug("\n------------End Hosts priority-----------------\n");
	}
	
	/**
	 * Override Host priorities in the cloud manager host model
	 * @param priorityTable Map with host priorities (String = host resource ID, Integer = host priority)
	 */
	public void overrideHostPriorities(Map<String,Integer> priorityTable){
		
		Set<String> keys = priorityTable.keySet();
		java.util.Iterator<String> itr = keys.iterator();
		while(itr.hasNext()) {
		    String key = itr.next();
		    
		   Host host = model.get(key);
		   host.setHostPriority(  priorityTable.get(key) );
		    model.put(key, host);
		}
		    
	}
	
	
	/**
	 * Retrieve Hosts models with their VMs
	 * @return List of Hosts Models
	 */
	public List<String> describeHosts(){
		
		List<String> list = null;
		
		if(managers != null && managers.getResourceManagers() != null){
			for(ResourceManagerData manager : managers.getResourceManagers()){
				if(manager.getResources() != null){
					for(ResourceData resource: manager.getResources() ){
						
						if(list == null){
							list = new ArrayList<String>();
						}
					
						try {
							String data = manager.getModelCapabilityClient().getResourceModel( resource.getId());
							
							data = retrieveVMIPs(data, resource.getId());
							
							//Add the ressource id in the host model
							DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						    DocumentBuilder db = dbf.newDocumentBuilder();
						    InputSource is = new InputSource();
					        is.setCharacterStream(new StringReader(data));
							Document doc = db.parse(is);

							Element elmtDomainId = doc.createElement("domainId");
							elmtDomainId.appendChild(doc.createTextNode( manager.getId() ));
							doc.getFirstChild().appendChild(elmtDomainId);
							
							Element elmtResourceId = doc.createElement("resourceId");
							elmtResourceId.appendChild(doc.createTextNode( resource.getId() ));
							doc.getFirstChild().appendChild(elmtResourceId);
							
							Element elmtResourceName = doc.createElement("resourceName");
							elmtResourceName.appendChild(doc.createTextNode( resource.getName() ));
							doc.getFirstChild().appendChild(elmtResourceName);
							
							Element elmtResourceState = doc.createElement("resourceState");
							String state = resource.getState().value();
							elmtResourceState.appendChild(doc.createTextNode( state ));
							doc.getFirstChild().appendChild(elmtResourceState);
							
							doc.normalize();
							
							
							Source source = new DOMSource(doc);
							StreamResult result = new StreamResult(new StringWriter());
							    
							Transformer xformer = TransformerFactory.newInstance().newTransformer();
							xformer.setOutputProperty(OutputKeys.INDENT, "yes");
							xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
							xformer.transform(source, result); 

							String xmlString = result.getWriter().toString();
							
							list.add( xmlString );
							
						} catch (ModelException_Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Retrieve the model of a Host
	 * @param hostId	id of the host (== id of the resource)
	 * @return	Host Model
	 */
	public String describeHost( String hostId ){
		if(managers != null && managers.getResourceManagers() != null){
			for(ResourceManagerData manager : managers.getResourceManagers()){
				if(manager.getResources() != null){
					for( ResourceData resource: manager.getResources() ){
						if(resource.getId().compareTo(hostId) == 0){
							try {
								//return manager.getModelCapabilityClient().getResourceModel(hostId);
								
								String data = manager.getModelCapabilityClient().getResourceModel( hostId );
								
								data = retrieveVMIPs(data, hostId);
								
								//Add the ressource id in the host model
								DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
							    DocumentBuilder db = dbf.newDocumentBuilder();
							    InputSource is = new InputSource();
						        is.setCharacterStream(new StringReader(data));
								Document doc = db.parse(is);
								
								Element elmtDomainId = doc.createElement("domainId");
								elmtDomainId.appendChild(doc.createTextNode( manager.getId() ));
								doc.getFirstChild().appendChild(elmtDomainId);
								
								Element elmtResourceId = doc.createElement("resourceId");
								elmtResourceId.appendChild(doc.createTextNode( hostId ));
								doc.getFirstChild().appendChild(elmtResourceId);
								
								Element elmtResourceName = doc.createElement("resourceName");
								elmtResourceName.appendChild(doc.createTextNode( resource.getName() ));
								doc.getFirstChild().appendChild(elmtResourceName);
								
								Element elmtResourceState = doc.createElement("resourceState");
								String state = resource.getState().value();
								elmtResourceState.appendChild(doc.createTextNode( state ));
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
								
							} catch (ModelException_Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Utility function used to have the most prioritary host with enough memory and cpu to deploy a VM
	 * @param memory	needed memory
	 * @param CPUNumber	needed CPUs number
	 * @return Host ID
	 */
	private String getPrioritaryHost(int memory, int CPUNumber){
		String hostId = null;
		int priority = -1;
		Set<String> keys = model.keySet();
		if(keys != null){
			java.util.Iterator<String> itr = keys.iterator();
			while(itr.hasNext()) {
			    String key = itr.next();
			    
			   Host host = model.get(key);
			   
			   if(host.getState().compareTo("ACTIVE") == 0 && 
					   host.getHostPriority() > priority &&
					   host.getIdleMemory() >= memory &&
					   host.getIdleProcessors() >= CPUNumber){
				   
				  priority = host.getHostPriority();
				  hostId = host.getResourceId(); 
			   
			   }
			}
		}
		return hostId;
	}
	
	/**
	 * Create a virtual machine by letting the cloud manager finding the most appropriate host
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	public String createInstance(String instanceName, String memory, String cpu, String template)
	{
		//Determine the host where to deploy the VM
		String hostId = getPrioritaryHost(Integer.parseInt(memory), Integer.parseInt(cpu));
		if(hostId == null){
			return "Error: No Host availaible for creating a new VM";
		}
		
		//Check if the target host has enough memory, cpu, ...
		if( model.get( hostId ).hasReachedMaxVM() ){
			return "Error: Max VM reached. cannot add VM in host: " + hostId;
		}
		
		int cpuNumber = model.get( hostId ).getIdleProcessors();
		if(  cpuNumber < Integer.parseInt(cpu) ){
			return "Error: Max CPU reached. cannot add VM in host: " + hostId;
		}
		
		int idleMemory = model.get( hostId ).getIdleMemory();
		if(  idleMemory < Integer.parseInt(memory) ){
			return "Error: Max Memory reached. cannot add VM in host: " + hostId;
		}
		
		String vmName = instanceName + "_" + template;
		String timestamp = new Long(new Date().getTime()).toString();
		vmName += "_" + timestamp;
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put((String)"vmName", vmName);
		try {
			ResponseMessage nres= network_manager.executeCommand(INetworkManager.CMD_assignVMAddress, args);
			if (nres.isError()) {
				return nres.getMessage();
			}
			VMNetAddress vmnet = VMNetAddress.fromXML(nres.getMessage()); 
			String params = "-vmName " + vmName + " -memory " + memory + " -cpu "+ cpu + 
						" -mac " + vmnet.getMac() + " -ip " + vmnet.getIp() + " -template " + template;
			String msg = executeAction(hostId, "CreateAction", params);
			
			//Update cloud manager host model
			if( msg.split(" ")[0] == "VM:" && msg.split(" ")[4] == "created!"){
				VirtualMachine vm = new VirtualMachine();
				vm.setCpuNumber(Integer.parseInt(cpu));
				vm.setMemory(Integer.parseInt(memory));
				vm.setName(msg.split(" ")[1]);
				vm.setIp(vmnet.getIp());
				vm.setTemplate(template);
				model.get( hostId ).getVMs().add(vm);
			}
			
			logger.debug("********* Cloud Manager : VM "+ vmnet.getIp() +" created!*********");
			
			return msg + "; hostId = " + hostId;
			
		}catch (NetworkManagerException e) {
			return e.toString();
		}
		
	}
	
	/**
	 * Recreate a virtual machine by letting the cloud manager finding the most appropriate host.
	 * VM image, XML template and IP addresses are all determined.
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	public String recreateInstance(String instanceName, String memory, String cpu, String template, String ip)
	{
		//Determine the host where to deploy the VM
		String hostId = getPrioritaryHost(Integer.parseInt(memory), Integer.parseInt(cpu));
		if(hostId == null){
			return "Error: No Host availaible for creating a new VM";
		}
		
		//Check if the target host has enough memory, cpu, ...
		if( model.get( hostId ).hasReachedMaxVM() ){
			return "Error: Max VM reached. cannot add VM in host: " + hostId;
		}
		
		int cpuNumber = model.get( hostId ).getIdleProcessors();
		if(  cpuNumber < Integer.parseInt(cpu) ){
			return "Error: Max CPU reached. cannot add VM in host: " + hostId;
		}
		
		int idleMemory = model.get( hostId ).getIdleMemory();
		if(  idleMemory < Integer.parseInt(memory) ){
			return "Error: Max Memory reached. cannot add VM in host: " + hostId;
		}
		
		String params = "-vmName " + instanceName + " -memory " + memory + " -cpu " + cpu + 
						" -mac 00:00:00:00:00:00 -ip " + ip + " -template " + template;
		String msg = executeAction(hostId, "ReCreateAction", params);

		//Update cloud manager host model
		if( msg.split(" ")[0] == "VM:" && msg.split(" ")[4] == "created!"){
			VirtualMachine vm = new VirtualMachine();
			vm.setCpuNumber(Integer.parseInt(cpu));
			vm.setMemory(Integer.parseInt(memory));
			vm.setName(msg.split(" ")[1]);
			vm.setIp(ip);
			vm.setTemplate(template);
			model.get( hostId ).getVMs().add(vm);
		}

		logger.debug("********* Cloud Manager : VM "+ ip +" created!*********");

		return msg + "; hostId = " + hostId;
	}

	/**
	 * Create a virtual machine in a specified host
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @param memory Memory Amount needed by the Virtual Machine
	 * @param cpu Number of CPUs needed by the Virtual Machine
	 * @param template template to use by the Virtual Machine
	 * @return String with information about the Virtual machine created (name, ip) if request successes, error message otherwise
	 */
	public String createInstanceInHost(String hostId, String instanceName, String memory, String cpu, String template)
	{
		if( model.get( hostId ).getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot add VM in host: " + hostId;
		}
		
		//Check if the target host has enough memory, cpu, ...
		if( model.get( hostId ).hasReachedMaxVM() ){
			return "Error: Max VM reached. cannot add VM in host: " + hostId;
		}
		
		int cpuNumber = model.get( hostId ).getIdleProcessors();
		if(  cpuNumber < Integer.parseInt(cpu) ){
			return "Error: Max CPU reached. cannot add VM in host: " + hostId;
		}
		
		int idleMemory = model.get( hostId ).getIdleMemory();
		if(  idleMemory < Integer.parseInt(memory) ){
			return "Error: Max Memory reached. cannot add VM in host: " + hostId;
		}
		
		String vmName = instanceName + "_" + template;
		String timestamp = new Long(new Date().getTime()).toString();
		vmName += "_" + timestamp;
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put((String)"vmName", vmName);
		try {
			ResponseMessage nres= network_manager.executeCommand(INetworkManager.CMD_assignVMAddress, args);
			if (nres.isError()) {
				return nres.getMessage();
			}
			VMNetAddress vmnet = VMNetAddress.fromXML(nres.getMessage()); 
			String params = "-vmName " + vmName + " -memory " + memory + " -cpu "+ cpu + 
						" -mac " + vmnet.getMac() + " -ip " + vmnet.getIp() + " -template " + template;
			String msg = executeAction(hostId, "CreateAction", params);
			
			//Update cloud manager host model
			if( msg.split(" ")[0] == "VM:" && msg.split(" ")[4] == "created!"){
				VirtualMachine vm = new VirtualMachine();
				vm.setCpuNumber(Integer.parseInt(cpu));
				vm.setMemory(Integer.parseInt(memory));
				vm.setName(msg.split(" ")[1]);
				vm.setIp(vmnet.getIp());
				vm.setTemplate(template);
				model.get( hostId ).getVMs().add(vm);
			}
			
			logger.debug("********* Cloud Manager : VM "+ vmnet.getIp() +" created!*********");
			
			return msg;
			
		}catch (NetworkManagerException e) {
			return e.toString();
		}
	}
	
	/**
	 * Start a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String startInstance(String hostId, String instanceName){
		
		if( model.get( hostId ).getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot start VM "+instanceName+" in host: " + hostId;
		}
		
		return executeAction(hostId, "StartAction", "vmName "+instanceName);
	}
	
	/**
	 * Stop a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String stopInstance(String hostId, String instanceName){
		
		if( model.get( hostId ).getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot stop VM "+instanceName+" in host: " + hostId;
		}
		
		return executeAction(hostId, "StopAction", "vmName "+instanceName);
	}
	
	/**
	 * Reboot a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String rebootInstance(String hostId, String instanceName){
		
		if( model.get( hostId ).getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot reboot VM "+instanceName+" in host: " + hostId;
		}
		
		return executeAction(hostId, "RebootAction", "vmName "+instanceName);
	}
	
	/**
	 * Shutdown a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String shutdownInstance(String hostId, String instanceName){
		
		if( model.get( hostId ).getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot shutdown VM "+instanceName+" in host: " + hostId;
		}
		
		return executeAction(hostId, "ShutdownAction", "vmName "+instanceName);
	}
	
	/**
	 * migrate a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @param destinationHostId id of the destination host (id of the IAAS destination resource)
	 * @return Success or error message
	 */
	public String migrateInstance(String hostId, String instanceName, String destinationHostId){
		
		if( model.get( destinationHostId ).getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot migrate VM "+instanceName+" in host: " + hostId;
		}
		
		logger.debug("CloudManager -------------------- migrateInstance called");
		VirtualMachine vm = model.get( hostId ).getVM(instanceName);
		
		//Check if the target host has enough memory, cpu, ...
//		if( model.get( destinationHostId ).hasReachedMaxVM() ){
//			return "Error: Max VM reached. cannot Migrate VM in host: " + destinationHostId;
//		}
//		
//		int cpuNumber = model.get( destinationHostId ).getIdleProcessors();
//		if(  cpuNumber < vm.getCpuNumber() ){
//			return "Error: Max CPU reached. cannot Migrate VM in host: " + destinationHostId;
//		}
//		
//		int idleMemory = model.get( destinationHostId ).getIdleMemory();
//		if(  idleMemory < vm.getMemory() ){
//			return "Error: Max Memory reached. cannot add Migrate in host: " + destinationHostId;
//		}
		
		String destHostIP = model.get(destinationHostId).getIp();
		
		String ret = executeAction(hostId, "MigrateAction", "vmName "+instanceName + " destHost " + destHostIP);
		logger.debug("MigrateAction response: " + ret);
		
		if ((ret.toLowerCase().indexOf("error") > -1) || (ret.toLowerCase().indexOf("exception") > -1))
		{
			return ret;
		}else {
			String r = executeAction(destinationHostId, "RefreshAction", "resourceID " + destinationHostId);
			logger.debug("RefreshAction response: " + r);
			r = executeAction(destinationHostId, "SetVMIPAction", "vmName " + instanceName + " ip " + vm.getIp());
			logger.debug("SetVMIPAction response: " + r);
//			//Update cloud manager host model
			model.get( hostId ).removeVM(instanceName);
			model.get( destinationHostId ).getVMs().add(vm);
		}
		
		return ret;
	}
	
	/**
	 * Destroy a virtual machine
	 * @param hostId id of the host (id of the IAAS resource)
	 * @param instanceName Virtual Machine name
	 * @return Success or error message
	 */
	public String destroyInstance(String hostId, String instanceName){
		String ret = executeAction(hostId, "DeleteAction", "vmName "+instanceName);
		if ((ret.indexOf("Error") > -1) || (ret.indexOf("Exception") > -1))
		{
			return ret;
		}
		try {
			
			//Update cloud manager host model
			model.get( hostId ).removeVM(instanceName);
			
			logger.debug("VM "+ instanceName +" has been destroyed in host: " + hostId);
			
			//release a IP - VM assignment
			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put((String)"vmName", instanceName);
			ResponseMessage nres= network_manager.executeCommand(INetworkManager.CMD_releaseAssignment, args);
			if (nres.isError()) {
				return nres.getMessage();
			}
		}catch (NetworkManagerException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * TODO EXPORT
	 * Export a virtual machine to a remote cloud
	 * @param instanceName Virtual Machine name
	 * @param targetCloud address of the remote cloud which will receives the VM 
	 * @return Success or error message
	 */
	public String exportVM(String instanceName,  String targetCloudIP)
	{
		String hostId = this.getHostForVM(instanceName);
		if (hostId == null) {
			return "Error: No host found for the VM: " + instanceName;
		}
		
		//Execute ExportAction on Libvirt resource
		String ret = executeAction(hostId, "ShutdownAction", "vmName " + instanceName);
		if ((ret.indexOf("Error") > -1) || (ret.indexOf("Exception") > -1))
		{
			return ret;
		}

		ret = this.shutdownInstance(hostId, instanceName);
		
		if ((ret.indexOf("Error") > -1) || (ret.indexOf("Exception") > -1))
		{
			return ret;
		}

		try {
			//Update cloud manager host model
			model.get( hostId ).removeVM(instanceName);
			
			//release a IP - VM assignment
			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put((String)"vmName", instanceName);
			ResponseMessage nres= network_manager.executeCommand(INetworkManager.CMD_releaseAssignment, args);
			if (nres.isError()) {
				return nres.getMessage();
			}
		}catch (NetworkManagerException e) {
			e.printStackTrace();
		}
		
		//Call remote cloud for importing the VM
		VirtualMachine vm = model.get( hostId ).getVM(instanceName);
		
		ICloudManagerSOAPEndpoint cloudMgr = CloudManagerClient.createCloudManagerService(targetCloudIP);
		ImportVM arg = new ImportVM();
		arg.setArg0(this.getCloudProxyIP());
		arg.setArg1(this.getCloudProxyStoragePath());
		arg.setArg2(this.getCloudProxyUser());
		arg.setArg3(instanceName);
		arg.setArg4(new Integer(vm.getCpuNumber()).toString());
		arg.setArg5(new Integer(vm.getMemory()).toString());
		arg.setArg6(vm.getTemplate());
		ImportVMResponse response = cloudMgr.importVM(arg);
		ret = response.getReturn();
		if ((ret.indexOf("Error") > -1) || (ret.indexOf("Exception") > -1))
		{
			return ret;
		}
		
		return "Success";
	}

	/**
	 * TODO EXPORT
	 * Receive a virtual machine from a remote cloud
	 * @param remotehostIP IP of the remote host where VM image needs to be downloaded
	 * @param remoteDir directory on remote host where VM image needs to be downloaded
	 * @param instanceName Virtual Machine name
	 * @param cpu required number of CPU 
	 * @param memory required capacity of memory 
	 * @param template template used to create the VM 
	 * @return Success or error message
	 */
	public String importVM(String remotehostIP, String remoteDir, String username, String instanceName, 
			String cpu, String memory, String template)
	{
		String hostIP = this.getCloudProxyIP();
		Host host = null;
		//Walkthrough the list of host to find the one matching IP address
		String hostId = null;
		java.util.Iterator<Entry<String, Host>> it = model.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Host> en = it.next();
			host = en.getValue();
			if (host.getIp().compareTo(hostIP) == 0)
			{
				hostId = en.getKey();
				break;
			}
		}
		
		if (hostId == null)
			return "Error: No Host found for IP: " + hostIP;
		
		
		if( host.getState().compareTo("ACTIVE") != 0 ){
			return "Error: Host in disabled status. cannot add VM in host: " + hostId;
		}
		
		//Check if the target host has enough memory, cpu, ...
		if( host.hasReachedMaxVM() ){
			return "Error: Max VM reached. cannot add VM in host: " + hostId;
		}
		
		int cpuNumber = host.getIdleProcessors();
		if(  cpuNumber < Integer.parseInt(cpu) ){
			return "Error: Max CPU reached. cannot add VM in host: " + hostId;
		}
		
		int idleMemory = host.getIdleMemory();
		if(  idleMemory < Integer.parseInt(memory) ){
			return "Error: Max Memory reached. cannot add VM in host: " + hostId;
		}
		
		Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put((String)"vmName", instanceName);
		try {
			ResponseMessage nres= network_manager.executeCommand(INetworkManager.CMD_assignVMAddress, args);
			if (nres.isError()) {
				return nres.getMessage();
			}
			VMNetAddress vmnet = VMNetAddress.fromXML(nres.getMessage()); 
			String params = "-filename " + instanceName + ".qcow2 -targethost " + remotehostIP + 
						" -remotedir " + remoteDir + " -username " + username + 
						" -vmName " + instanceName + " -memory " + memory + " -cpu " + 
						cpu + " -mac " + vmnet.getMac() + " -ip " + vmnet.getIp() + " -template " + template;
			String msg = executeAction(hostId, "ImportAction", params);
			
			//Update cloud manager host model
			if( msg.split(" ")[0] == "VM:" && msg.split(" ")[4] == "created!"){
				VirtualMachine vm = new VirtualMachine();
				vm.setCpuNumber(Integer.parseInt(cpu));
				vm.setMemory(Integer.parseInt(memory));
				vm.setName(msg.split(" ")[1]);
				vm.setIp(vmnet.getIp());
				vm.setTemplate(template);
				host.getVMs().add(vm);
			}
			
			logger.debug("********* Cloud Manager : VM "+ vmnet.getIp() +" created!*********");
			
			return msg;
			
		}catch (NetworkManagerException e) {
			return e.toString();
		}
	}
	
	
	/**
	 * Execute an action on a IAAS resource
	 * @param resourceId	id of the resource
	 * @param actionName	name of the action to execute
	 * @param parameters	parameters used by the action
	 * @return Action Result
	 */
	public String executeAction(String resourceId, String actionName, String parameters){
		if(managers.getResourceManagers() != null){
			for(ResourceManagerData manager : managers.getResourceManagers()){
				if(manager.getResources() != null){
					for(ResourceData resource: manager.getResources() ){
						if(resource.getId().compareTo(resourceId) == 0){
							try {
								logger.debug("Execute action: " + actionName + 
										" on resource " + resourceId + " at domain " + manager.getId());
								String resp = manager.getActionSetCapabilityClient().executeActionWS(resourceId, actionName, parameters);
								logger.debug("Action " + actionName + 
										" response: " + resp);
								return resp;
							} catch (ActionException_Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return e.toString();
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Request Network Manager for an IP address and then inject it into a VM sections of a Host model
	 * @param xmlModel
	 * @return
	 */
	private String retrieveVMIPs(String hostModelXml, String resourceId) 
	{
		VmHostModel hostModel = VmHostModel.fromXML(hostModelXml);
		try {
			List<VM> vms = hostModel.getVMList();
			if (vms != null)
			{
				java.util.Iterator<VM> it = vms.iterator();
				Map<Object, Object> args = new Hashtable<Object, Object>();
				while (it.hasNext()) {
					VM vm = it.next();
					if (vm.getIp() == null)
					{
						args.put((String)"vmName", vm.getName());
						args.put((String)"vmMAC", vm.getMac());
						ResponseMessage nres= network_manager.executeCommand(INetworkManager.CMD_retrieveVMAddress, args);
						if (!nres.isError()) {
							VMNetAddress vmnet = VMNetAddress.fromXML(nres.getMessage()); 
							vm.setIp(vmnet.getIp());
							//set new IP to the VM model
							this.executeAction(resourceId, "SetVMIPAction", "vmName " + vm.getName() + " ip " + vmnet.getIp());
						}
					}
				}
			}
		}catch (NetworkManagerException e) {
			e.printStackTrace();
		}
		return hostModel.toXML();
	}
	
	@Override
	public String setInstanceIPAddress(String hostId, String instanceName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Add a domain in the cloud manager
	 * @param domainId ip/id of the domain
	 */
	public void addDomain(String domainId){
		
		if(managers != null){
			
			ResourceManagerData MngData = new ResourceManagerData();
			MngData.setId(domainId);
			
			MngData.setResourceManagerClient( WSClient.createManagerService(domainId) );
			MngData.setModelCapabilityClient( WSClient.createModelService(domainId) );
			MngData.setActionSetCapabilityClient( WSClient.createActionSetService(domainId) );
			
			MngData.setResources( MngData.getResourceManagerClient().listReourcesByType("vmmEngine") );
			
			managers.getResourceManagers().add(MngData);
			
			//Suscribing to JMS
			String brokerURL = "tcp://"+ domainId + ":" + this.bropkerPort;
			try{
				Consumer consumer = new Consumer(brokerURL, this.messageTopic, this);
				jmsConsumers.put(domainId, consumer);
			} catch(JMSException jmsEx) {
				
			}
			
			updateHostModel(domainId);
			updateHostPriority();
			
		}else{
			initCloudManager();
		}
		
	}
	
	/**
	 * Remove a domain from the cloud manager
	 * @param domainId ip/id of the domain
	 */
	public void removeDomain(String domainId){
		//Update cloud manager hosts model
		if(managers.getResourceManagers() != null){
			for(ResourceManagerData manager : managers.getResourceManagers()){
				if( manager.getId().compareTo(domainId) == 0 && manager.getResources() != null){
					for(ResourceData resource: manager.getResources() ){
						model.remove( resource.getId() );
					}
				}
			}
		}
		updateHostPriority();
		
		for(ResourceManagerData mgData: managers.getResourceManagers() ){
			 if(mgData.getId().compareTo(domainId) == 0){
				 managers.getResourceManagers().remove(mgData);
				 break;
			 }
		 }
		
		//Unsuscribing from JMS
		try {
			jmsConsumers.get("domainId").close();
			jmsConsumers.remove(domainId);
		} catch(JMSException e){
		}
	}
	
	/**
	 * Force the cloud manager to load/reload domains that was updated by the domainManager
	 */
	public void initDomains(){
		initCloudManager();
	}
	
	
	/**
	 * Generic query which retrieve hosts that match the function parameters. 
	 * A parameter equal to -1 will be discarded from the research
	 * 
	 * @param domainId	domain ID (Which is the ip of the IAAS container)
	 * @param ip	Host ip
	 * @param location	Host location
	 * @param freeMemory Host freeMemory bigger than the freeMemory parameter (unit: Ko)
	 * @param availableCPU	Host available CPUs
	 * @param 	vmUUID	Enable to retrieve the Host which contains the Virtual Machine with the UUID parameter
	 * @return
	 */
	public List<String> hostQuery(String domainId, String ip, String location, String freeMemory, String availableCPU, String vmUUID){
		
		List<String> list = null;
	
		if(managers.getResourceManagers() != null){
			for(ResourceManagerData manager : managers.getResourceManagers()){
				if(manager.getResources() != null){
					for(ResourceData resource: manager.getResources() ){
					
						try {
							String data = manager.getModelCapabilityClient().getResourceModel( resource.getId());
							
							DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						    DocumentBuilder db = dbf.newDocumentBuilder();
						    InputSource is = new InputSource();
					        is.setCharacterStream(new StringReader(data));
							Document doc = db.parse(is);
							
							Element elmtResourceId = doc.createElement("resourceId");
							elmtResourceId.appendChild(doc.createTextNode( resource.getId() ));
							doc.getFirstChild().appendChild(elmtResourceId);
							
							Element elmtResourceName = doc.createElement("resourceName");
							elmtResourceName.appendChild(doc.createTextNode( resource.getName() ));
							doc.getFirstChild().appendChild(elmtResourceName);
							
							Element elmtResourceState = doc.createElement("resourceState");
							String state = resource.getState().value();
							elmtResourceState.appendChild(doc.createTextNode( state ));
							doc.getFirstChild().appendChild(elmtResourceState);
							
							doc.normalize();
							
							
							Source source = new DOMSource(doc);
							StreamResult result = new StreamResult(new StringWriter());
							    
							Transformer xformer = TransformerFactory.newInstance().newTransformer();
							xformer.setOutputProperty(OutputKeys.INDENT, "yes");
							xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
							xformer.transform(source, result); 

							String xmlString = result.getWriter().toString();
							
							if((domainId.compareTo("-1") == 0 || manager.getId().compareTo(domainId) == 0 )
									&& (ip.compareTo("-1") == 0 || areEquals("ip", ip, xmlString) )
									&& (location.compareTo("-1") == 0 ||  areEquals("location", location, xmlString) )
									&& (freeMemory.compareTo("-1") == 0 || areEquals("freeMemory", freeMemory, xmlString) )
									&& (availableCPU.compareTo("-1") == 0 || areEquals("availableCPU", availableCPU, xmlString) )
									&& (vmUUID.compareTo("-1") == 0 || areEquals("vmUUID", vmUUID, xmlString) )
									){
								
								if(list == null){
									list = new ArrayList<String>();
								}
								
								list.add( xmlString );
					
							}
							
						} catch (ModelException_Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Utility function used by hostQuery function
	 * @param key	A host attribute
	 * @param value	Value of the host attribute
	 * @param xmlData	A host XML description
	 * @return true if parameters matches, false otherwise
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private boolean areEquals(String key, String value, String xmlData) throws ParserConfigurationException, SAXException, IOException{
		
		//Parse data
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlData));
		Document doc = db.parse(is);
		
		if(key.compareTo("ip") == 0){
			
			NodeList nodes = doc.getElementsByTagName("address");
			if(nodes != null){
				for(int i=0;i<nodes.getLength();i++){
					if( nodes.item(i).getFirstChild().getNodeValue().compareTo(value) == 0){
						return true;
					}
				}
			}
		}else if(key.compareTo("location") == 0){
			
			NodeList nodes = doc.getElementsByTagName("location");
			if(nodes != null){
				for(int i=0;i<nodes.getLength();i++){
					if(nodes.item(i).getFirstChild().getNodeValue().compareTo(value) == 0){
						return true;
					}
				}
			}
			
		}else if(key.compareTo("freeMemory") == 0){
			
			NodeList nodes = doc.getElementsByTagName("freeMemory");
			if(nodes != null){
				for(int i=0;i<nodes.getLength();i++){
					String mem = nodes.item(i).getFirstChild().getNodeValue();
					if( Integer.parseInt(  mem.substring(0, mem.length() -1) ) >= Integer.parseInt(value) ){
						return true;
					}
				}
			}
			
		}else if(key.compareTo("availableCPU") == 0){
			
			NodeList nodes = doc.getElementsByTagName("nbrCPUs");
			int nbreCPU = 0;
			if(nodes != null){
				nbreCPU = Integer.parseInt( nodes.item(0).getFirstChild().getNodeValue() );
			}else{
				return false;
			}
			
			nodes = doc.getElementsByTagName("vcpu");
			int usedCPU = 0;
			if(nodes != null){
				for(int i=0;i<nodes.getLength();i++){
					usedCPU += Integer.parseInt( nodes.item(i).getFirstChild().getNodeValue() );
				}
			}
			
			int freeCPU = nbreCPU - usedCPU;
			
			if(freeCPU >= Integer.parseInt(value)){
				return true;
			}
			
		}else if(key.compareTo("vmUUID") == 0){
			
			NodeList nodes = doc.getElementsByTagName("uuid");
			for(int i=0;i<nodes.getLength();i++){
				if(nodes.item(i).getFirstChild().getNodeValue().compareTo(value) == 0){
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	/**
	 * Managing received messages from activemq
	 * @param message	Received message from activemq
	 */
	public void onMessage(String message){
		
		logger.debug("********* Cloud Manager : Received Message : "+message+" *********");
//		System.err.println("********* Cloud Manager : Received Message : "+message+" *********");
		
		if(message.compareTo("HostMonitor: Handshake") == 0){
			logger.debug("Cloud Manager: Host Monitor Handshake");
//			System.err.println("Cloud Manager: Host Monitor Handshake");
			return;
		}
		
		if( message.startsWith("HA event: host") ){
			
			String[] val = message.split(" ");
			String hostIP = val[3];
			
			
			if(val[4].compareTo("active") == 0){
				//A host has been reset to active: Reintegrating it in the cloud manager model
				//Set its status to active
				
				Host host = getHostByIP(hostIP);
				if(host != null){
					activeHostStatus( host.getResourceId() );
					logger.debug("********* Cloud Manager : Received Message from host monitor "+hostIP+". Host is active now and in start state*********");
				}else{
					logger.debug("Cloud Manager: Host Monitor message received: Host not found in model: " + hostIP);
				}
				
			}else if(val[4].compareTo("disabled") == 0){
				//A host has been disabled: Destroying its VMs and Restarting them in another in another host
				//Get Host Model from cloud manager model
				Host host = getHostByIP(hostIP);
				String resourceId = host.getResourceId();
				
				logger.debug("********* Cloud Manager : Received error Message from host monitor "+hostIP+" *********");
				
				//Destroy host instances and recreating them in other hosts
				if(host != null && resourceId != null)
				{
					for(int i = 0;i<host.getCurrentVMNumber();i++)
					{
						VirtualMachine vm = host.getVMs().get(i);
						
						this.destroyInstance(resourceId, vm.getName());
						
						this.recreateInstance(vm.getName(), Integer.toString( vm.getMemory() ), 
								Integer.toString( vm.getCpuNumber() ), vm.getTemplate(), vm.getIp());
						
						logger.debug("********* Cloud Manager : VM "+vm.getIp()+" has been destroyed and restarted in a new host *********");
					}
					
					//Set the host status to disabled
					if(model.get(resourceId).getState().compareTo("SHUTDOWN") != 0){
						inactiveHostStatus( resourceId );
					}
					logger.debug("********* Cloud Manager : Host is inactive now and in stop state*********");
					
				}else{
					logger.debug("Cloud Manager: Host Monitor message received: Host not found in model: " + hostIP);
				}
				
			}
			
		}
		
	}
	
	private Host getHostByIP(String hostIP){
		java.util.Iterator<Entry<String, Host>> it = model.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Host> en = it.next();
			Host ret = en.getValue();
			if (ret.getIp().compareTo(hostIP) == 0)
			{
				return ret;
			}
		}
		return null;
	}
	
	/**
	 * TODO EXPORT
	 * @param vmName
	 * @return the host that has a given VM
	 */
	private String getHostForVM(String vmName) 
	{
		java.util.Iterator<Entry<String, Host>> it = model.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Host> en = it.next();
			Host ret = en.getValue();
			if (ret.getVM(vmName) != null)
			{
				return en.getKey();
			}
		}
		return null;
	}
	
	private void inactiveHostStatus(String hostId){
		try{
			if(managers != null && managers.getResourceManagers() != null){
				for(ResourceManagerData manager : managers.getResourceManagers()){
					if(manager.getResources() != null){
						for(ResourceData resource: manager.getResources() ){
							if(resource.getId().compareTo(hostId) == 0){
								manager.getResourceManagerClient().stop("vmmEngine", hostId);
								model.get(hostId).setState("SHUTDOWN");
							}
						}
					}
				}
			}
		}catch(Exception e){
			
		}
	}
	
	private void activeHostStatus(String hostId){
		try{
			if(managers != null && managers.getResourceManagers() != null){
				for(ResourceManagerData manager : managers.getResourceManagers()){
					if(manager.getResources() != null){
						for(ResourceData resource: manager.getResources() ){
							if(resource.getId().compareTo(hostId) == 0){
								manager.getResourceManagerClient().start("vmmEngine", hostId);
								model.get(hostId).setState( "ACTIVE" );
							}
						}
					}
				}
			}
		}catch(Exception e){
			
		}
	}

	/**
	 * TODO EXPORT
	 * Set the proxy that receives VMs from external clouds
	 * @param cloudProxy
	 */
	public void setProxyHost(String id, String ip, String storagePath, String username, String password) {
		this.proxyHost = new CloudProxy();
		proxyHost.setId(id);
		proxyHost.setIp(ip);
		proxyHost.setStoragefolder(storagePath);
		proxyHost.setUsername(username);
		proxyHost.setPassword(password);
		try {
			proxyHost.saveCloudProxyConfig(System.getenv("IAAS_HOME")+"/gsn/cloudproxy.xml");
		}catch (IOException ioe) {
			System.out.println("Cannot save cloudproxy configuration." + ioe.toString());
		}
	}

	/**
	 * TODO EXPORT
	 * Get the proxy that receives VMs from external clouds
	 */
	private void generateProxyHost() {
		if ( proxyHost == null )
		{//if proxy host == null, return the first host in the list
			if (this.model != null)
			{
				java.util.Iterator<Entry<String, Host>> it = model.entrySet().iterator();
				while (it.hasNext()) {
					Entry en = it.next();
					if (((Host)en.getValue()).getState().compareTo("ACTIVE") == 0)
					{
						setProxyHost((String)en.getKey(), ((Host)en.getValue()).getIp(), 
								CloudProxy.DEF_STORAGE_PATH, CloudProxy.DEF_USERNAME, CloudProxy.DEF_PASSWORD);
						break;
					}
				}
			}
		}
	}

	/**
	 * TODO EXPORT
	 * Return IP address of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyIP() {
		if ( proxyHost != null )
			return proxyHost.getIp();
		return null;
	}
	
	/**
	 * TODO EXPORT
	 * Return username of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyUser() {
		if ( proxyHost != null )
			return proxyHost.getUsername();
		return null;
	}


	/**
	 * TODO EXPORT
	 * Return password of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyPassword() {
		if ( proxyHost != null )
			return proxyHost.getPassword();
		return null;
	}
	
	/**
	 * TODO EXPORT
	 * Return storage path of the proxy host that receives VMs from external clouds
	 */
	public String getCloudProxyStoragePath() {
		if ( proxyHost != null )
			return proxyHost.getStoragefolder();
		return null;
	}
}
