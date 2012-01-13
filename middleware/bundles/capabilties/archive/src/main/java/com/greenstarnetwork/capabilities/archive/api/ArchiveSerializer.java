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
package com.greenstarnetwork.capabilities.archive.api;

import java.io.File;
import java.io.StringReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.iaasframework.capabilities.model.IResourceModel;


/**
 * 
 * 
 * 
 * @author abdelhamid
 *
 */
public class ArchiveSerializer {
	IResourceModel model;
	public String string;
	public String resourceID = null;
	public DocumentBuilderFactory domFactory = DocumentBuilderFactory
			.newInstance();

	public Transformer aTransformer;
	public TransformerFactory tranFactory;
	public DocumentBuilder domBuilder = null;
	public Document archiveDoc;
	public Source src;
	public Result dest;

	public File archiveFile;
	public String ROOT_ELEMENT = "Archive";
//	public GregorianCalendar now;
	String directory=null;
	String archiveXMLFile=null;
	/**
	 * 
	 * 
	 */
	
	public ArchiveSerializer(String resourceName){
		
		try {
			initializeArchiveSerializer(resourceName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method archive model 
	 * The model is serialized into XML file
	 * 
	 * @param model
	 */
	
	public void archiveModel(IResourceModel model) {

		string = model.toXML();
	
		try {

			addNewEelment(string);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	/**
	 * 
	 * This method add new data in archive file
	 * 
	 * @param Doc
	 * @param string
	 * @param src
	 * @param dest
	 * @param file
	 * @throws Exception
	 */
	public void addNewEelment(String string) throws Exception {
		
		
		
		GregorianCalendar timer = new GregorianCalendar();

		if (!getStrDate(timer).equals(archiveXMLFile)){
			
			
			createNewFile();}
		
	
		int hour = timer.get(Calendar.HOUR_OF_DAY);
		int minute = timer.get(Calendar.MINUTE);
		 
		timer.clear();
		timer=null;
		Document d = domFactory.newDocumentBuilder().parse(
				new InputSource(new StringReader(string)));
		Element time = d.createElement("time");

	
		String tagtime ="";
		if (minute<10)
		 tagtime=hour + ".0" + minute ;
		   else
			tagtime=hour + "." + minute ;

		d.getDocumentElement().appendChild(time);
		time.setTextContent(tagtime);
		getDOM();
		Node node = archiveDoc.importNode(d.getDocumentElement(), true);
		archiveDoc.getDocumentElement().appendChild(node);
		src = new DOMSource(archiveDoc);
		dest = new StreamResult(archiveFile);
		try {
			tranFactory = TransformerFactory.newInstance();
			aTransformer = tranFactory.newTransformer();
			aTransformer.transform(src, dest);
		
		
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		clearDOM();
	}



	public IResourceModel getModel() {
		return model;
	}

	public void setModel(IResourceModel model) {
		this.model = model;
	}

	/**
	 * This method initialize archive capability. with creating new archive file
	 * if don't exist or open exist one
	 * 
	 * @throws Exception
	 */

	public void initializeArchiveSerializer(String archiveID) throws Exception {
		
		 String iaas=System.getenv("IAAS_HOME");
		 byte[] b = iaas.getBytes();
		String dir= new String(b);
	    directory = dir+"/gsn/archive/" + archiveID;

	
		
		GregorianCalendar  now = new GregorianCalendar();

		
		
		String archiveXMLFile =getStrDate(now) + ".xml";
	
		archiveFile = new File(directory,archiveXMLFile);
		/* if an archive file does not exist for today, create a new one */
		now.clear();
		now=null;
		if (!archiveFile.exists()) {

		
			

			boolean success = (new File(directory)).mkdirs();
			
			if (success)
			archiveFile = new File(directory,archiveXMLFile);

			domBuilder = domFactory.newDocumentBuilder();
			
			archiveDoc = domBuilder.newDocument();
	
			Element rootElement = archiveDoc.createElement(ROOT_ELEMENT);
			archiveDoc.appendChild(rootElement);
			
			tranFactory = TransformerFactory.newInstance();
			aTransformer = tranFactory.newTransformer();
			src = new DOMSource(archiveDoc);
			dest = new StreamResult(archiveFile);
			aTransformer.transform(src, dest);

		} 

	}
	
	/**
	 * 
	 * This method create new archive file
	 * 
	 *  @throws Exception
	 */
	
	public void createNewFile() throws Exception {
		
		GregorianCalendar  now = new GregorianCalendar();
		archiveXMLFile =getStrDate(now) + ".xml";
		now.clear();
		now=null;
	
		archiveFile = new File(directory,archiveXMLFile);
		/* if an archive file does not exist for today, create a new one */
		if (!archiveFile.exists()) {

		

			boolean success = (new File(directory)).mkdirs();
			
			if (success)
			archiveFile = new File(directory,archiveXMLFile);

			domBuilder = domFactory.newDocumentBuilder();
			
			archiveDoc=null;
			archiveDoc = domBuilder.newDocument();
	
			Element rootElement = archiveDoc.createElement(ROOT_ELEMENT);
			archiveDoc.appendChild(rootElement);
			
			tranFactory = TransformerFactory.newInstance();
			aTransformer = tranFactory.newTransformer();
			src = new DOMSource(archiveDoc);
			dest = new StreamResult(archiveFile);
			aTransformer.transform(src, dest);

		}
	}
	
	/**
	 * This method return the name of file formated as date
	 * 
	 * 
	 * @param GregorianCalendar
	 * @return String 
	 */
	
	public  String getStrDate(GregorianCalendar c) {
		 int m = c.get(GregorianCalendar.MONTH) + 1;
		 int d = c.get(GregorianCalendar.DATE);
		 String mm = Integer.toString(m);
		 String dd = Integer.toString(d);
		 return "" + c.get(GregorianCalendar.YEAR) + (m < 10 ? "0" + mm : mm) +
		     (d < 10 ? "0" + dd : dd);
		}
	/**
	 * To load data from XML file to DOM object
	 * 
	 */
	
	void getDOM(){
		
		try{
		tranFactory = TransformerFactory.newInstance();
		aTransformer = tranFactory.newTransformer();
		archiveDoc = domFactory.newDocumentBuilder().parse(archiveFile);
		}catch (Exception e){
			
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * To remove DOM object from memory
	 * 
	 */
	
	void clearDOM(){
		
		archiveDoc=null;
	}

}
