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
package com.greenstarnetwork.resources.facilityResource.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;

import com.greenstarnetwork.models.facilityModel.FacilityModel;
import com.greenstarnetwork.models.facilityModel.config.Facility;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.powersource.PowerSourceModel;
import com.greenstarnetwork.resources.facilityResource.commandset.InitializeCommand;


public class FacilityResourceToFromFile {

	private static final String VIRTUAL_FACILITY_MODEL_XML = "virtualFacilityModel.xml";
	private static final String OBJECT_IS_NULL = "Object is null.";
	private static final String HOME = "HOME";
	private static final String IAAS_HOME = "IAAS_HOME";
	private static final String XML = ".xml";
	private static final String GSN_FACILITY = "gsn/facility";
	private static final boolean OVERWRITE = true;

	public static void writeToFile(FacilityModel facilityModel, String pathFileName) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(pathFileName);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		bufferedOutputStream.write(facilityModel.toXML().getBytes());
		bufferedOutputStream.close();
		fileOutputStream.close();

	}
	
	public FacilityModel readFacilityModelFromFile() throws Exception{
		
		String s = getIAASPath();
		checkDir(s+"/"+GSN_FACILITY);
		return readFacilityModelFromFile(s+"/"+GSN_FACILITY+"/"+VIRTUAL_FACILITY_MODEL_XML);
		
	}

	public Object readPDUModelFromFileByID(String resourceID) throws Exception {
		String s = getIAASPath();
		checkDir(s+"/"+GSN_FACILITY);
		return readPDUModelFromFile(s+"/"+GSN_FACILITY+"/"+resourceID+XML, resourceID);
	}

	
	public Object readPDUModelFromFile(String pathFileName, String resourceID) throws Exception{

		File file = new File(pathFileName);
		if (!file.exists()||OVERWRITE){
			copyPDUModelToFile(pathFileName, resourceID);
		}
		
		FileInputStream fin = new FileInputStream(pathFileName);
		BufferedInputStream bis = new BufferedInputStream(fin);
		String result = "";
		while (bis.available()>0){
			result+=(char)bis.read();
		}
		
		PDUModel pduModel = new PDUModel();
		JAXBContext context = JAXBContext.newInstance(pduModel.getClass());
		StringReader in = new StringReader(result);
		Object obj = context.createUnmarshaller().unmarshal(in);
		
		if (obj==null){
			throw new Exception(OBJECT_IS_NULL);
		}
				
		return obj;
		
	}

	public void copyPDUModelToFile(String pathFileName, String resourceID) throws IOException {
		
		//read this file into InputStream
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream(resourceID+XML);
 
		FileOutputStream fileOutputStream = new FileOutputStream(pathFileName);
		BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
 
		int read=0;
		byte[] bytes = new byte[1024];
 
		while((read = inputStream.read(bytes))!= -1){
			out.write(bytes, 0, read);
		}
 
		inputStream.close();
		out.flush();
		out.close();	

		
	}

	
	public FacilityModel readFacilityModelFromFile(String pathFileName) throws Exception{

		File file = new File(pathFileName);
		if (!file.exists()||OVERWRITE){
			copyFacilityResourceModelToFile(pathFileName);
		}
		
		FileInputStream fin = new FileInputStream(pathFileName);
		BufferedInputStream bis = new BufferedInputStream(fin);
		String result = "";
		while (bis.available()>0){
			result+=(char)bis.read();
		}
		
		FacilityModel facilityModel = new FacilityModel();
		
		JAXBContext context = JAXBContext.newInstance(facilityModel.getClass());
		StringReader in = new StringReader(result);
		Object obj = context.createUnmarshaller().unmarshal(in);
		
		if (obj==null){
			throw new Exception(OBJECT_IS_NULL);
		}
		
		return (FacilityModel)obj;

	}


	/*public void createToFile(String pathFileName) throws IOException {
		FacilityModel facilityModel = new FacilityModel();
		facilityModel.setLocation(new Location("here"));
		facilityModel.setClimate(new Climate());
		facilityModel.setOperationalSpecs(new OperationalSpecs());
		PowerDistribution powerDistribution1 = new PowerDistribution("001");
		powerDistribution1.addPowerSink(new PowerSink("00111"));
		powerDistribution1.addPowerSink(new PowerSink("00112"));
		powerDistribution1.addPowerSink(new PowerSink("00113"));
		PDUModel pduModel1 = new PDUModel();
		pduModel1.setCurrent("1");
		powerDistribution1.setPduModel(pduModel1);
		facilityModel.addPowerDistribution(powerDistribution1);
		PowerDistribution powerDistribution2 = new PowerDistribution("002");
		powerDistribution2.addPowerSink(new PowerSink("00211"));
		powerDistribution2.addPowerSink(new PowerSink("00212"));
		powerDistribution2.addPowerSink(new PowerSink("00213"));
		PowerSource powerSource21 = new PowerSource("00221");
		PowerSourceModel powerSourceModel21 = new PowerSourceModel();
		powerSourceModel21.setChargerBatteryVoltage("23.333333333333333");
		powerSourceModel21.setInvACOutputVoltage("100");
		powerSource21.setPowerSourceModel(powerSourceModel21);
		powerDistribution2.addPowerSource(powerSource21);
		PDUModel pduModel2 = new PDUModel();
		pduModel2.setCurrent("5");
		powerDistribution2.setPduModel(pduModel2);
		facilityModel.addPowerDistribution(powerDistribution2);
		writeToFile(facilityModel, "virtualFacilityModel.txt");

		
	}*/

	
	public void copyVFacilityXML(String pathFileName) throws IOException {
		
		//read this file into InputStream
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream("vfacility.xml");
 
		FileOutputStream fileOutputStream = new FileOutputStream(pathFileName);
		BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
 
		int read=0;
		byte[] bytes = new byte[1024];
 
		while((read = inputStream.read(bytes))!= -1){
			out.write(bytes, 0, read);
		}
 
		inputStream.close();
		out.flush();
		out.close();	

		
	}
	
	public void copyFacilityResourceModelToFile(String pathFileName) throws IOException {
		
		//read this file into InputStream
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream(VIRTUAL_FACILITY_MODEL_XML);
 
		FileOutputStream fileOutputStream = new FileOutputStream(pathFileName);
		BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
 
		int read=0;
		byte[] bytes = new byte[1024];
 
		while((read = inputStream.read(bytes))!= -1){
			out.write(bytes, 0, read);
		}
 
		inputStream.close();
		out.flush();
		out.close();	

		
	}
	
	
	public String getIAASPath(){
		String s=System.getenv(IAAS_HOME);
		String s2=System.getenv(HOME);
		byte[] b = s.getBytes();
		if (b[0]=='~'){
			byte[] d = new byte[b.length-1];
			for (int i=0;i<=d.length-1;i++){
				d[i]=b[i+1];
			}
			String s3= new String(d);
			s=s2+s3;
		}
		return s;
	}

	
	public void checkDir(String s){
		File file = new File(s);
		try {
			file.mkdirs();
		} catch (Exception e) {
			// the directory may exist, which is okay.
		}
	}

	public Object readPowerSourceModelFromFileByID(String resourceID) throws Exception {
		String s = getIAASPath();
		checkDir(s+"/"+GSN_FACILITY);
		return readPowerSourceModelFromFile(s+"/"+GSN_FACILITY+"/"+resourceID+XML, resourceID);
	}

	
	public Object readPowerSourceModelFromFile(String pathFileName, String resourceID) throws Exception{

		File file = new File(pathFileName);
		if (!file.exists()||OVERWRITE){
			copyPowerSourceModelToFile(pathFileName, resourceID);
		}
		
		FileInputStream fin = new FileInputStream(pathFileName);
		BufferedInputStream bis = new BufferedInputStream(fin);
		String result = "";
		while (bis.available()>0){
			result+=(char)bis.read();
		}
		
		PowerSourceModel powerSourceModel = new PowerSourceModel();
		JAXBContext context = JAXBContext.newInstance(powerSourceModel.getClass());
		StringReader in = new StringReader(result);
		Object obj = context.createUnmarshaller().unmarshal(in);
		
		if (obj==null){
			throw new Exception(OBJECT_IS_NULL);
		}
				
		return obj;
		
	}

	public void copyPowerSourceModelToFile(String pathFileName, String resourceID) throws IOException {
		
		//read this file into InputStream
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream(resourceID+XML);
 
		FileOutputStream fileOutputStream = new FileOutputStream(pathFileName);
		BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
 
		int read=0;
		byte[] bytes = new byte[1024];
 
		while((read = inputStream.read(bytes))!= -1){
			out.write(bytes, 0, read);
		}
 
		inputStream.close();
		out.flush();
		out.close();	

		
	}

	public Facility readFacilityConfigFile(String pathFileName) throws IOException {
		File file = new File(pathFileName);
		if (!file.exists()){
			Logger.getLogger().error("Facility config file does not exist! in $IAAS_HOME"+InitializeCommand.GSN_FACILITY_FACILITY_XML);
			return null;
		}
		
		FileInputStream fin = new FileInputStream(pathFileName);
		BufferedInputStream bis = new BufferedInputStream(fin);
		String result = "";
		while (bis.available()>0){
			result+=(char)bis.read();
		}
		
		return Facility.fromXML(result);
		
	}

	
	

}
