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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
/**
 * 
 * 
 * 
 * @author abdelhamid
 *
 */
public class ArchiveClient implements IArchiveClient {
	
	
/**
 * this method return the archive data between two dates 
 * it merge files and return as result a String
 * @param  String startDate
 * @param  String endDate
 * @param  String resourceID
 * @return String result
 * 
 */
	
	boolean find=false;
	
	public String getDataByRange(String startDat, String endDate, String resourceName) {
		find=false;
		File folder ;
		File[] listOfFiles = null;
		
		String iaas=System.getenv("IAAS_HOME");
		byte[] b = iaas.getBytes();
		String dir= new String(b);
		String srcDir =dir+"/gsn/archive/" +resourceName;
        int begin = Integer.parseInt (startDat);
        int end = Integer.parseInt (endDate);
        InputStream input = null;
        String result="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> "+"<Archive>";
		String strData="<empty>There is no data</empty>";	
        
        folder = new File(srcDir);
		 
		 if (folder.exists())
		 {
	
		listOfFiles = folder.listFiles();
		 
		if (listOfFiles.length > 0)
		{	
			for (int i = 0; i < listOfFiles.length; i++)
			   {
						   
			      if (listOfFiles[i].exists())
			      {
			    	
			         int day = Integer.parseInt(removeExtention(listOfFiles[i]));
			  
			    	if( (day>=begin)&& (day<=end))
			    	
			    	{find=true;
			    		strData = "";
			    		try {
							 input = new  FileInputStream(listOfFiles[i]);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						strData = readStringFromFile(input);
					
						strData = strData. replaceFirst("\\<.*?\\>", "");
						
						strData=strData.replace("<Archive>","<"+day+">");
						strData=strData.replace("</Archive>", "</"+day+">");
				
					 
						 result=result+   strData;
			    	}   
			   			    	
			     
			      }
			
			   
			   }
		    if (find)
			  return result+"</Archive>";
		      else
			   return result+"<Archive> <empty>There is no data</empty> "+"</Archive>";
		}
	
		 
		 }
		 
		 
	
		return result+"<Archive> <empty>There is no data</empty> "+"</Archive>";
	}
	
	
/**
 * this method return archive data of one day
 * 
 * @param String day
 * @param resourceId
 * @return String result
 * 
 */
	
	

	public String getArchiveData(String date, String resourceName){
         String datefile=date+".xml";
		 String iaas=System.getenv("IAAS_HOME");
		 byte[] b = iaas.getBytes();
			String dir= new String(b);
	
		String srcDir =dir+"/gsn/archive/" +resourceName;
		
		
	    String result="";
		
		   try{
			  
			    FileInputStream fstream = new
			    FileInputStream(srcDir+"/"+datefile);
			    result = readStringFromFile( fstream);
		
		
			   
			
			    }catch (Exception e){
			      System.err.println("Error: " + e.getMessage());
			    }
			 
		
			  	
			return result;

	};
	
	/**
	 * 
	 * 
	 * @param String date
	 * @param String QueryXPATH
	 * @param String resourceId
	 * @return String result
	 */
	
	//@WebMethod
	public String getPieceOfArchiveData(String date, String queryXPATH,
		String resourceName) {
	
		 String iaas=System.getenv("IAAS_HOME");
		 byte[] b = iaas.getBytes();
		String dir= new String(b);
		
		String srcDir =dir+"/gsn/archive/" +resourceName;
		String filelocation=srcDir+"/"+date+".xml";
		File file = new File(filelocation);
		String resultfinal="";
		if (file.exists())
		System.err.println("receivinf this request  "+queryXPATH);
		
		{
			 DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
		     DocumentBuilder builder;
			try {
				builder = Factory.newDocumentBuilder();
				Document doc = builder.parse(filelocation);
				//creating an XPathFactory:
		        XPathFactory factory = XPathFactory.newInstance();
		        //using this factory to create an XPath object: 
		        XPath xpath = factory.newXPath()
		        ;
		        //XPath object created compiles the XPath expression: 
		        XPathExpression expr = xpath.compile(queryXPATH);
			
		      //expression is evaluated with respect to a certain context node which is doc.
		        Object result = expr.evaluate(doc, XPathConstants.NODESET);
		        NodeList nodeList = (NodeList) result;
		      
		        for (int i = 0; i < nodeList.getLength(); i++) {
		       		       
		        resultfinal=resultfinal+","+ nodeList.item(i).getNodeValue();
		        }
			
			
			
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			
			
		}
		
	  
		return resultfinal;
	}
	   
	
	
	
	
	/**
	 * This method remove the XML extension of file in order to compare dates
	 * @param file
	 * @return
	 */
	 public  String removeExtention(File file) {
	
		    String name = file.getName();
		 
		    // if there is no extention, don't do anything
		    if (!name.contains(".")) return name;
		    // Otherwise, remove the last 'extension type thing'
		    return name.substring(0, name.lastIndexOf('.'));
		}


	 
	 private String readStringFromFile(InputStream stream){
			String answer = null;
			
			try {
				InputStreamReader streamReader = new InputStreamReader(stream);
				StringBuffer fileData = new StringBuffer(10000);
				BufferedReader reader = new BufferedReader(streamReader);
				char[] buf = new char[1024];
				int numRead=0;
				while((numRead=reader.read(buf)) != -1){
				    String readData = String.valueOf(buf, 0, numRead);
				    fileData.append(readData);
				    buf = new char[1024];
				}
				reader.close();
				answer = fileData.toString();
				fileData = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return answer;
		}


}
