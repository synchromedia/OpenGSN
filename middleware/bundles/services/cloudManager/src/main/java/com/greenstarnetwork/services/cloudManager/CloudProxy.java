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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import com.iaasframework.resources.core.ObjectSerializer;

/**
 * TODO EXPORT
 * Data structure representing a proxy of a cloud.
 * The proxy receives VMs from external clouds.
 * 
 * @author knguyen
 *
 */
@XmlRootElement(name="cloudproxy")
public class CloudProxy implements Serializable
{
	public static String DEF_STORAGE_PATH = "/storage";		//default storage path of the cloud proxy
	public static String DEF_USERNAME = "user";				//default username of the cloud proxy
	public static String DEF_PASSWORD = "password";			//default password of the cloud proxy
	
	private static final long serialVersionUID = 1L;
	private String id = null;						//id of the proxy
	private String ip = null;						//ip of the proxy (it's the storage server)
	private String username = null;					//user account on the proxy
	private String password = null;					//password of the proxy
	private String storagefolder = null;			//storage folder that contains VM images
	
	public CloudProxy () {
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setStoragefolder(String storagefolder) {
		this.storagefolder = storagefolder;
	}
	public String getStoragefolder() {
		return storagefolder;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String toXML() {
		return ObjectSerializer.toXml(this);
	}

	
	/**
	 * Save the cloud proxy configuration to the end of domains.xml file
	 * @param filename
	 * @throws IOException
	 */
	public void saveCloudProxyConfig(String filename) throws IOException
	{
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filename));
		bufferedOutputStream.write(this.toXML().getBytes());
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
	}

	/**
	 * Populates CloudProxy attribute by reading data from a given XML File.
	 * @throws Exception 
	 */
	public static CloudProxy loadCloudProxyConfig(String XMLfile) throws Exception{	
		try {
			JAXBContext jc = JAXBContext.newInstance(CloudProxy.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File f = new File(XMLfile);
			if(f.exists()){
				return ((CloudProxy)unmarshaller.unmarshal(f));
			}else{
				throw new Exception("File : " + XMLfile + " not found!");
			}
						
		} catch (JAXBException e1) {
			throw new Exception("File : " + XMLfile + " does not have valid format. Error: " + e1.toString());
		}

	}

}
