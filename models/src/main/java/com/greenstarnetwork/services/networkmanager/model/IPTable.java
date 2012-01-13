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
package com.greenstarnetwork.services.networkmanager.model;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.services.utils.ObjectSerializer;

/**
 * IP table implementation The table contains an IP pool assigned to VMs
 * belonging to a given domain. The table can be serialized to/from a XML file.
 * 
 * @author knguyen
 * 
 */
@XmlRootElement
public class IPTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String DATA_MARK = "$DATA"; // position in the template file
												// where data must be filled
	private String ipPrefix = "10.20.101."; // default prefix of VM's IP address
	private String macPrefix = "54:52:00:47:53:"; // default prefix of VM's MAC
													// address

	private String domainID = null; // Domain ID
	private List<VMNetAddress> pool = null; // list of available IP address

	public IPTable() {
	}

	/**
	 * @param domainID
	 *            the domainID to set
	 */
	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}

	/**
	 * @return the domainID
	 */
	public String getDomainID() {
		return domainID;
	}

	/**
	 * @param pool
	 *            the pool to set
	 */
	public void setPool(List<VMNetAddress> pool) {
		this.pool = pool;
	}

	/**
	 * @return the pool
	 */
	public List<VMNetAddress> getPool() {
		return pool;
	}

	/**
	 * Initialize the IP table with 20 predefined addresses
	 * 
	 * @throws Exception
	 */
	public void initIPTable() {
		if (this.pool != null)
			// throw new
			// RuntimeException("Error: IP table has been initialized.");
			return;

		this.pool = new ArrayList<VMNetAddress>();
		for (int ip = 1; ip < 255; ip++) {
			String ipStr = ipPrefix + Integer.toString(ip);
			String hex = Integer.toHexString(ip).toUpperCase();
			if (hex.length() == 1)
				hex = "0" + hex;
			String mac = macPrefix + hex;
			// InetAddress inet = InetAddress.getByName(ipStr);
			VMNetAddress vmip = new VMNetAddress();
			vmip.setIp(ipStr);
			vmip.setMac(mac);
			vmip.setAssigned(false);
			this.pool.add(vmip);
		}
	}

	/**
	 * Add a new IP address to pool
	 * 
	 * @param ip
	 * @return false if pool is already full
	 * @throws RuntimeException
	 */
	public synchronized boolean addIPToPool(VMNetAddress address) throws RuntimeException {
		if (this.pool == null)
			this.pool = new ArrayList<VMNetAddress>();
		if (this.getItem(address.getIp()) == null)
			this.pool.add(address);
		return true;
	}

	/**
	 * Retrieve an IP address from the pool
	 * 
	 * @param ip
	 * @return
	 * @throws RuntimeException
	 */
	public VMNetAddress getItem(String ip) throws RuntimeException {
		try {
			InetAddress inet = InetAddress.getByName(ip);
			if (inet == null)
				return null; // string represents an invalid IP address
		} catch (UnknownHostException e) {
			throw new RuntimeException("IP address invalid. Error: " + e.toString());
		}
		java.util.Iterator<VMNetAddress> it = this.pool.iterator();
		while (it.hasNext()) {
			VMNetAddress ret = (VMNetAddress) it.next();
			if ((ret.getIp() != null) && (ret.getIp().compareTo(ip) == 0))
				return ret;
		}
		return null;
	}

	/**
	 * Retrieve a VM/address record from a given VM name
	 * 
	 * @param vmName
	 * @return
	 * @throws RuntimeException
	 */
	public VMNetAddress getVMNetAddress(String vmName) throws RuntimeException {
		if (this.pool == null)
			throw new RuntimeException("Error: IP table is not initialized.");
		if (vmName == null)
			throw new RuntimeException("Error: VM name invalid.");

		java.util.Iterator<VMNetAddress> it = this.pool.iterator();
		while (it.hasNext()) {
			VMNetAddress ret = (VMNetAddress) it.next();
			if ((ret.getName() != null) && (ret.getName().compareTo(vmName) == 0)) {
				return ret;
			}
		}
		return null;
	}

	/**
	 * Retrieve the ip address that has been assigned to a given MAC address
	 * 
	 * @param vmName
	 * @return
	 * @throws RuntimeException
	 */
	public VMNetAddress getAddressFromMac(String mac, String vmName, boolean addToPool) throws RuntimeException {
		if (this.pool == null)
			throw new RuntimeException("Error: IP table is not initialized.");
		if ((mac == null) && (vmName == null))
			throw new RuntimeException("Error: MAC address or VM name is required.");

		java.util.Iterator<VMNetAddress> it = this.pool.iterator();
		while (it.hasNext()) {
			VMNetAddress ret = (VMNetAddress) it.next();
			if (mac != null) {
				if ((ret.getMac() != null) && (ret.getMac().compareToIgnoreCase(mac) == 0)) {
					if (addToPool && !ret.isAssigned()) {
						ret.setAssigned(true);
						ret.setName(vmName);
					}
					return ret;
				}
			} else {
				if ((ret.getName() != null) && (ret.getName().compareTo(vmName) == 0)) {
					ret.setAssigned(addToPool);
					return ret;
				}
			}
		}
		return null;
	}

	/**
	 * Assign an available IP/MAC address to a VM which will be later created
	 * 
	 * @param vmName
	 * @return
	 */
	public synchronized VMNetAddress assignVMIP(String vmName) throws RuntimeException {
		if (this.pool == null)
			throw new RuntimeException("Error: IP pool is not initialized.");
		if (vmName == null)
			return null;
		java.util.Iterator<VMNetAddress> it = this.pool.iterator();
		VMNetAddress assign = null;
		while (it.hasNext()) {
			VMNetAddress ret = (VMNetAddress) it.next();
			if ((ret.getName() != null) && (ret.getName().compareTo(vmName) == 0)) {
				if (ret.isAssigned())
					throw new RuntimeException("Error: VM : " + vmName + " has been assigned with IP: "
							+ ret.getIp().toString());
				else
					assign = ret;
				break;
			} else if (ret.getName() == null) {
				assign = ret;
				break;
			}
		}
		if (assign != null) {
			assign.setName(vmName);
			assign.setAssigned(true);
			return assign;
		}
		return null; // all ip addresses in the pool have been provided.
	}

	/**
	 * Release an assignment of IP/MAC and a VM
	 * 
	 * @param vmName
	 * @return
	 * @throws RuntimeException
	 */
	public synchronized boolean releaseAssignment(String vmName) throws RuntimeException {
		VMNetAddress vna = this.getVMNetAddress(vmName);
		if ((vna != null) && vna.isAssigned()) {
			vna.setAssigned(false);
			vna.setName(null);
			return true;
		}
		return false; // VM is not found in IP table or assignment is not yet
						// done
	}

	/**
	 * Serialize object to a XML string
	 * 
	 * @return
	 */
	public String toXML() {
		return ObjectSerializer.toXml(this);
	}

	/**
	 * @return the ipPrefix
	 */
	public String getIpPrefix() {
		return ipPrefix;
	}

	/**
	 * @param ipPrefix
	 *            the ipPrefix to set
	 */
	public void setIpPrefix(String ipPrefix) {
		this.ipPrefix = ipPrefix;
	}

	/**
	 * @return the macPrefix
	 */
	public String getMacPrefix() {
		return macPrefix;
	}

	/**
	 * @param macPrefix
	 *            the macPrefix to set
	 */
	public void setMacPrefix(String macPrefix) {
		this.macPrefix = macPrefix;
	}

	/**
	 * Generate a DHCP server configuration file
	 * 
	 * @throws IOException
	 */
	public void generateDHCPConfigFile(String file) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream("templates/dhcp.txt")));
		String s = null;
		while ((s = reader.readLine()) != null) {
			s = s.trim();
			if (s.compareTo(DATA_MARK) == 0) {
				for (int ip = 1; ip < 255; ip++) {
					String ipStr = ipPrefix + Integer.toString(ip);
					String hex = Integer.toHexString(ip).toUpperCase();
					if (hex.length() == 1)
						hex = "0" + hex;
					String mac = macPrefix + hex;
					writer.write("        host vm" + new Integer(ip).toString() + " { hardware ethernet " + mac
							+ "; fixed-address " + ipStr + ";}\n");
				}
			} else
				writer.write(s + "\n");
		}
		writer.flush();
		writer.close();
	}
}
