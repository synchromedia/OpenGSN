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

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 *
 */
@Command(scope = "gsn", name = "listDomains", description="List all registered domains")
public class ListDomainsShellCommand extends OsgiCommandSupport {
	
	@Override
	protected Object doExecute() throws Exception {
		System.out.println("Executing listDomains Command...");
			
		File f = new File(System.getenv("IAAS_HOME")+"/gsn/domains.xml");
		if(f.exists()){
		
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse(f);
			 
			 NodeList nodes = doc.getElementsByTagName("domain");
			 
			 if(nodes.getLength() > 0){
				 System.out.println("---- Domain List ----");
			 }else{
				 System.out.println("There is no domain registered...");
				 return null;
			 }
			 
			 for(int i=0;i<nodes.getLength();i++){
				 System.out.println("Domain name: " +  nodes.item(i).getChildNodes().item(0).getFirstChild().getNodeValue() + " - Domain id: " +  nodes.item(i).getChildNodes().item(1).getFirstChild().getNodeValue() );
			 }
						
		}else{
			System.out.println("There is no domain registered...");
		}
			
		
		System.out.println("End of listDomains Command.");
		return null;
	}

}
