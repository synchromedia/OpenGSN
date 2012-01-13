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
package com.greenstarnetwork.services.domainManager.shell;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.greenstarnetwork.services.cloudManager.ICloudManager;
import com.greenstarnetwork.services.domainManger.util.ManagerUtil;
import com.greenstarnetwork.services.facilityManager.IFacilityManager;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
@Command(scope = "gsn", name = "addDomain", description="Add a new domain")
public class AddDomainShellCommand extends OsgiCommandSupport {
	
	@Argument(index = 0, name = "domainId", description = "Id of the domain (which is actually its ip)", required = true, multiValued = false)
	private String domainId;
	@Argument(index = 1, name = "domainName", description = "Domain name", required = true, multiValued = false)
	private String domainName;
	
	private ManagerUtil managers;
	
	@Override
	protected Object doExecute() throws Exception {
		System.out.println("Executing addDomain Command...");
		
		managers = new ManagerUtil();
		managers.loadManagers();
		
		ICloudManager cloudMger = managers.getCloudManager();
		IFacilityManager facilityMger = managers.getFacilityManager();
		
		if(cloudMger != null && facilityMger != null){
		
			File f = new File(System.getenv("IAAS_HOME")+"/gsn/domains.xml");
			if(f.exists()){
			
				 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			     DocumentBuilder db = dbf.newDocumentBuilder();
				 Document doc = db.parse(f);
				 
				 //Verify that the domain has not been already registered
				 NodeList testNodes = doc.getElementsByTagName("id");
				 for(int i=0;i<testNodes.getLength();i++){
					 if(testNodes.item(i).getFirstChild().getNodeValue().compareTo(domainId) == 0){
						System.out.println("The domain "+ domainId +" has already been registered!");
						return null;
					 }
				 }
				 
				 Element elmtRM = doc.createElement("domain");
				 Element elmtName = doc.createElement("name");
				 elmtName.appendChild(doc.createTextNode(domainName));
				 elmtRM.appendChild(elmtName);
				 Element elmtId = doc.createElement("id");
				 elmtId.appendChild(doc.createTextNode(domainId));
				 elmtRM.appendChild(elmtId);
				 
				 NodeList nodes = doc.getElementsByTagName("domainList");
			     Element element = (Element) nodes.item(0);
				 element.appendChild(elmtRM);
				 
				 doc.normalize();
				 
				 Source source = new DOMSource(doc);
				 Result outputFile = new StreamResult(f);
				    
				 Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
				 xformer.transform(source, outputFile); 
		
			}else{
				
				System.out.println("DomainManager: No configuration file found. The file will be created automatically!");
				
				f.createNewFile();
				
				String xmlModel = "<domainList>" +
										"<domain>" +
											"<name>"+domainName+"</name>" +
											"<id>"+domainId+"</id>" +
										"</domain>" +
								  "</domainList>";
				
				InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(xmlModel));
			
			    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
			    Document doc = db.parse(is);
			    Source source = new DOMSource(doc);
			    
			    Result outputFile = new StreamResult(f);
			    
			    Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
			    xformer.transform(source, outputFile); 
			    
			}
			
			 //Notify managers that a domain was removed
			cloudMger.addDomain(domainId);
			facilityMger.addDomain(domainId);
		
		}else{
			System.out.println("Unable to load GSN Managers!");
		}
		
		System.out.println("End of addDomain Command.");
		return null;
	}


}
