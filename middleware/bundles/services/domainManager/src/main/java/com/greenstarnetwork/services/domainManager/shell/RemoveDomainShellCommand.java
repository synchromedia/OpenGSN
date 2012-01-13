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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.greenstarnetwork.services.domainManger.util.ManagerUtil;
import com.greenstarnetwork.services.cloudManager.ICloudManager;
import com.greenstarnetwork.services.facilityManager.IFacilityManager;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
@Command(scope = "gsn", name = "removeDomain", description="Unregister a domain")
public class RemoveDomainShellCommand extends OsgiCommandSupport {
	
	@Argument(index = 0, name = "domainId", description = "Id of the domain (which is actually its ip)", required = true, multiValued = false)
	private String domainId;
	
	private ManagerUtil managers;
	
	@Override
	protected Object doExecute() throws Exception {
		System.out.println("Executing removeDomain Command...");
			
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
				 
				 NodeList nodes = doc.getElementsByTagName("id");
				 for(int i=0;i<nodes.getLength();i++){
					 if(nodes.item(i).getFirstChild().getNodeValue().compareTo(domainId) == 0){
						Node cible = nodes.item(i).getParentNode();
						cible.getParentNode().removeChild(cible);
						break;
					 }
				 }
				 
				 doc.normalize();
				 
				 Source source = new DOMSource(doc);
				 Result outputFile = new StreamResult(f);
				    
				 Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
				 xformer.transform(source, outputFile);
				 
				 //Notify managers that a domain was removed
				 cloudMger.removeDomain(domainId);
				 facilityMger.removeDomain(domainId);
		
			}else{
				System.out.println("DomainManager: No configuration file found. There is no domain to unregister!");
			}
		}else{
			System.out.println("Unable to load GSN Managers!");
		}
		
		System.out.println("End of removeDomain Command.");
		return null;
	}

}
