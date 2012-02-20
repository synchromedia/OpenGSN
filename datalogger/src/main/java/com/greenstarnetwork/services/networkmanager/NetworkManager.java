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
package com.greenstarnetwork.services.networkmanager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.services.networkmanager.messages.NetworkManagerRequest;
import com.greenstarnetwork.services.networkmanager.messages.NetworkManagerResponse;
import com.greenstarnetwork.services.networkmanager.model.IPTable;
import com.greenstarnetwork.services.networkmanager.model.VMNetAddress;

/**
 * Network management module
 * Provide network functions, such as VM address management (MAC & IP)
 * 
 * @author knguyen
 *
 */
public class NetworkManager implements INetworkManager {
	
	private Logger logger = LoggerFactory.getLogger(NetworkManager.class);
	
	//default domain IP
	private String DEFAULT_DOMAIN = "127.0.0.1";			
	
	public static String IP_PREFIX = "10.20.101.";							//default prefix of VM's IP address
	public static String MAC_PROVIDER_PREFIX = "54:52:00:47:53:";			//default prefix of VM's MAC address

	//XML file containing IP table
	private String xmlFile = null;								
	private static NetworkManager instance = null;

	private IPTable iptable = null;					//ip table
	
	public NetworkManager(String fileLocation)
	{
		this(fileLocation, MAC_PROVIDER_PREFIX, IP_PREFIX);		
	}
	public NetworkManager(String fileLocation, String macPrefix, String ipPrefix)
	{
		xmlFile = fileLocation;
		
		try {
			//First, try to load IP table from file
			this.loadIPTable(xmlFile);
		} catch (NetworkManagerException e) {
			//If the file does not exist, create a new one
			this.createIPTable(DEFAULT_DOMAIN,macPrefix,ipPrefix);
			try {
				this.saveIPTable(xmlFile);
			} catch (IOException e1) {
				logger.error(e1.toString());
			}
		}
		
	}

	/**
	 * Populates IPTable attribute by reading data from a given XML File.
	 * @throws NetworkManagerException 
	 */
	private void loadIPTable(String XMLfile) throws NetworkManagerException{	
		try {
			JAXBContext jc = JAXBContext.newInstance(IPTable.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File f = new File(XMLfile);
			if(f.exists()){
				setIptable((IPTable)unmarshaller.unmarshal(f));
			}else{
				throw new NetworkManagerException("File : " + XMLfile + " not found!");
			}
						
		} catch (JAXBException e1) {
			throw new NetworkManagerException("File : " + XMLfile + " does not have valid format. Error: " + e1.toString());
		}

	}

	/**
	 * Save IP table to a XML file
	 * @param filename
	 * @throws IOException
	 */
	private void saveIPTable(String filename) throws IOException
	{
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filename));
		bufferedOutputStream.write(this.iptable.toXML().getBytes());
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
	}
	
	/**
	 * Create a new IP table containing a pool of available IP addresses
	 * @param domainID
	 * @throws NetworkManagerException
	 */
	private void createIPTable(String domainID,String ipPrefix, String macPrefix){
		this.iptable = new IPTable();
		iptable.setIpPrefix(ipPrefix);
		iptable.setMacPrefix(macPrefix);
		iptable.setDomainID(domainID);
		iptable.initIPTable();
	}
	
	/**
	 * Assign an available IP address to a VM
	 * Return an XML string containing VM name/IP/MAC 
	 * @param vmName
	 * @return null if all addresses in IP pool had been assigned
	 * @throws NetworkManagerException
	 */
	public VMNetAddress assignVMAddress(String vmName) throws NetworkManagerException {
		if (this.iptable == null)
			return null;
		VMNetAddress ret = this.iptable.assignVMIP(vmName);
		try {
			this.saveIPTable(xmlFile);
		} catch (IOException e) {
			logger.error(e.toString());
		}
		return ret;
	}
	
	
	/**
	 * Retrieve the ip address that has been assigned to a given MAC address
	 * @param mac
	 * @param vmName
	 * @return
	 * @throws NetworkManagerException
	 */
	public VMNetAddress retrieveVMAddress(String mac, String vmName)  throws NetworkManagerException {
		if (this.iptable == null)
			 throw new NetworkManagerException("Error: IPTable is not initialized.");
		VMNetAddress ret = this.iptable.getAddressFromMac(mac, vmName, true);
		try {
			this.saveIPTable(xmlFile);
		} catch (IOException e) {
			logger.error(e.toString());
		}
		return ret;
		
	}
	
	/**
	 * Release an assignment of IP/MAC and a VM
	 * @param vmName
	 * @return
	 * @throws NetworkManagerException
	 */
	public boolean releaseAssignment(String vmName) throws NetworkManagerException {
		if (this.iptable == null)
			 throw new NetworkManagerException("Error: IPTable is not initialized.");
		boolean ret = this.iptable.releaseAssignment(vmName);
		try {
			this.saveIPTable(xmlFile);
		} catch (IOException e) {
			logger.error(e.toString());
		}
		return ret;
	}

	/**
	 * Parse a request sent through JMS and execute a corresponding command.
	 * @param request
	 * @return
	 */
	public NetworkManagerResponse executeCommand(NetworkManagerRequest req) 
	{
		NetworkManagerResponse ret = new NetworkManagerResponse();
		String cmd = req.getCommandId();
		Map<String,String> args = req.getArguments();
		if (cmd == null) {
			ret.setErrorMessage("Error: Command null.");
			return ret;
		}
		
		try {
			if (cmd.compareTo(INetworkManager.CMD_assignVMAddress) == 0) {
				if (args.containsKey((String)"vmName")) {
					VMNetAddress vna = this.assignVMAddress((String)args.get((String)"vmName"));
					if (vna != null)
						ret.setMessage(vna.toXML());
					else
						ret.setErrorMessage("Error: no assignment found for VM: " + (String)args.get((String)"vmName"));
				}else
					ret.setErrorMessage("Error: Command syntax " + getCommandSyntax(cmd));
			}else if (cmd.compareTo(INetworkManager.CMD_retrieveVMAddress) == 0) {
				if (args.containsKey((String)"vmName") && args.containsKey((String)"vmMAC")) {
					VMNetAddress vna = this.retrieveVMAddress((String)args.get((String)"vmMAC"), (String)args.get((String)"vmName"));
					if (vna != null)
						ret.setMessage(vna.toXML());
					else
						ret.setErrorMessage("Error: no address found for VM: " + (String)args.get((String)"vmName") + " , MAC: " + (String)args.get((String)"vmMAC"));
				}else
					ret.setErrorMessage("Error: Command syntax " + getCommandSyntax(cmd));
			}else if (cmd.compareTo(INetworkManager.CMD_releaseAssignment) == 0) {
				if (args.containsKey((String)"vmName")) {
					if ( !this.releaseAssignment((String)args.get((String)"vmName")) )
						ret.setErrorMessage("Error: Release VM");
				}else
					ret.setMessage("Error: Command syntax " + getCommandSyntax(cmd));
			}else {
				ret.setErrorMessage("Error: Invalid command " + cmd);
			}
		} catch (NetworkManagerException e) {
			ret.setErrorMessage("Error: Exception " + e.toString());
		}
		return ret;
	}
	
	/**
	 * Return a string describing a command syntax
	 * @return
	 */
	private String getCommandSyntax(String cmd) {
		if (cmd.compareTo(INetworkManager.CMD_assignVMAddress) == 0) {
			return "assignVMAddress vmName";
		}else if (cmd.compareTo(INetworkManager.CMD_retrieveVMAddress) == 0) {
			return "retrieveVMAddress vmMAC vmName";
		}else if (cmd.compareTo(INetworkManager.CMD_releaseAssignment) == 0) {
			return "releaseAssignment vmName";
		}else
			return null;
	}
	
	/**
	 * @param iptable the iptable to set
	 */
	public void setIptable(IPTable iptable) {
		this.iptable = iptable;
	}

	/**
	 * @return the iptable
	 */
	public IPTable getIptable() {
		return iptable;
	}

	/**
	 * @return the instance
	 */
	public static NetworkManager getInstance() {
		return instance;
	}


}
