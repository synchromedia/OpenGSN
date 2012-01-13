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
package com.greenstarnetwork.resources.outbackMate.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.ServiceReference;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.actionset.Activator;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceManager;
import com.iaasframework.resources.core.IResourceRepository;
import com.iaasframework.resources.core.ResourceManager;


public class Helper {

	private static final String SWITCH_TO_GRID_HIGH = "switch-to-grid-high";
	private static final String SWITCH_TO_GRID_LOW = "switch-to-grid-low";
	private static final String GSN_VPOWER_DAY3_TXT = "/gsn/vpower/day3.txt";
	private static final String GSN_VPOWER_DAY2_TXT = "/gsn/vpower/day2.txt";
	private static final String GSN_VPOWER_DAY1_TXT = "/gsn/vpower/day1.txt";
	private static final String VPOWER_CONFIG_TXT = "vpower-config.txt";
	private static final String PDU_ALIAS = "pdu-alias";
	private static final String GMT = "gmt";
	private static final String GSN_VPOWER_CONFIG_TXT = "/gsn/vpower/vpower-config.txt";
	private static final String HOME = "HOME";
	private static final String IAAS_HOME = "IAAS_HOME";
	private ResourceManager resourceManager;


	public void init() throws Exception {
		ServiceReference helloServiceRef = Activator.getContext().getServiceReference(IResourceManager.class.getName());
		resourceManager = (ResourceManager)Activator.getContext().getService(helloServiceRef);


		//resourceManager = (ResourceManager)RegistryUtil.getServiceFromRegistry(Activator.getContext(), IResourceManager.class.getName());
		if (Activator.getContext()==null){
			throw new Exception("Activator.getContext() is null");
		}
		if (resourceManager==null){
			throw new Exception("resourceManager is null");
		}

	}

	public void copyVPower(String pathFileName) throws IOException {

		//read this file into InputStream
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream(VPOWER_CONFIG_TXT);
		
		checkDir(getIAASPath()+"/gsn/vpower");
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

	public int[][] readCurrentDataForAWeek() throws IOException{
		int[][] result =new int[7][1440];
		for (int i=0;i<7;i++){
			int[] temp = readCurrentDataForADay(i);
			for (int j=0;j<1440;j++){
				result[i][j]=temp[j];
			}

		}
		return result; 
	}

	private int[] readCurrentDataForADay(int day) throws IOException {
		int [] result =new int[1440];

		String pathFileName=getIAASPath()+"/gsn/vpower/day"+Integer.toString(day+1)+".txt";
		File file = new File(pathFileName);
		if (!file.exists()){
			copyDay1to7();
		}
		int i=0;
		FileInputStream fstream = new FileInputStream(pathFileName);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] str;
			str = strLine.trim().split("\t");
			String strTime=str[0];
			//System.out.println(strTime);
			String strCurrent=str[1];
			//System.out.println(strCurrent);
			str = strTime.split(":");
			String strHour=str[0];
			String strMin=str[1];
			int minOfDay = Integer.valueOf(strHour)*60+Integer.valueOf(strMin);
			int cur = Integer.valueOf(strCurrent);
			//System.out.println(minOfDay);
			//System.out.println(cur);
			if (minOfDay>=i){
				for (int j=i;j<=minOfDay;j++){
					result[j]=cur;
				}
				i=minOfDay+1;
			}
		}
		if (i<1439){
			for (int j=i;j<=1439;j++){
				result[j]=0;
			}
		}
		in.close();
		return result;
	}

	private void copyDay1to7() throws IOException {
		copyFromTo("day1.txt",getIAASPath()+GSN_VPOWER_DAY1_TXT);
		copyFromTo("day2.txt",getIAASPath()+GSN_VPOWER_DAY2_TXT);
		copyFromTo("day3.txt",getIAASPath()+GSN_VPOWER_DAY3_TXT);
		copyFromTo("day4.txt",getIAASPath()+"/gsn/vpower/day4.txt");
		copyFromTo("day5.txt",getIAASPath()+"/gsn/vpower/day5.txt");
		copyFromTo("day6.txt",getIAASPath()+"/gsn/vpower/day6.txt");
		copyFromTo("day7.txt",getIAASPath()+"/gsn/vpower/day7.txt");
	}

	private void copyFromTo(String from1, String to2) throws IOException {
		//read this file into InputStream
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream(from1);

		checkDir(getIAASPath()+"/gsn/vpower");
		FileOutputStream fileOutputStream = new FileOutputStream(to2);
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


	public String readKey(String pathFileName, String key) throws IOException {
		File file = new File(pathFileName);
		if (!file.exists()){
			copyVPower(pathFileName);
		}
		Properties pro = new Properties();
		File f = new File(pathFileName);
		FileInputStream in = new FileInputStream(f);
		pro.load(in);
		return pro.getProperty(key);
	}

	public Double readGMT() throws IOException {
		return Double.valueOf(readKey(getIAASPath()+GSN_VPOWER_CONFIG_TXT, GMT));
	}
	
	public Double readSwitchToGridLowMargin() throws IOException {
		return Double.valueOf(readKey(getIAASPath()+GSN_VPOWER_CONFIG_TXT, SWITCH_TO_GRID_LOW));
	}
	
	public Double readSwitchToGridHighMargin() throws IOException {
		return Double.valueOf(readKey(getIAASPath()+GSN_VPOWER_CONFIG_TXT, SWITCH_TO_GRID_HIGH));
	}

	public String readPDUAlias() throws IOException {
		return readKey(getIAASPath()+GSN_VPOWER_CONFIG_TXT, PDU_ALIAS);
	}

	public Double readPDUCurrentVoltage() throws Exception{
		return readPDUCurrentVoltage(readPDUAlias());
	}

	public Double readPDUCurrentVoltage(String pduAlias) throws Exception{
		Double result =2.0*110;
		if (pduAlias!=null){
			String pduResourceID= getResourceIDbyAliase(pduAlias);
			if (pduResourceID!=null){
				try{
					checkExist(pduResourceID);
					ModelCapabilityClient modelClient = new ModelCapabilityClient(pduResourceID);
					RequestModelResponse reqModel = modelClient.requestModel(true);
					PDUModel pduModel = (PDUModel)(reqModel.getResourceModel());
					if (pduModel==null){throw new Exception("pduModel("+pduResourceID+") is null");}
					double totalConsumtionACCurrent=0;
					double averageConsumtionACVoltage = 0;

					{// calculating averageConsumtionACVoltage and totalConsumtionACCurrent 
						List<PDUElement> list = pduModel.getOutlets();
						for (PDUElement outlet:list){
							totalConsumtionACCurrent = totalConsumtionACCurrent + Double.valueOf(outlet.getLoad());
							averageConsumtionACVoltage = averageConsumtionACVoltage + Double.valueOf(outlet.getVoltage());
						}
						averageConsumtionACVoltage = averageConsumtionACVoltage / list.size();
					}
					result=totalConsumtionACCurrent*averageConsumtionACVoltage;
				}
				catch (Exception e) {}	
			}
		}
		return result;
	}


	/**
	 * @throws Exception 
	 * 
	 */
	public void checkExist(String resourceID) throws Exception {
		Boolean exist = false;

		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){

			IResourceRepository resourceRepository = map.get(str);
			for (IResource resource:resourceRepository.listResources()){

				if (resource.getResourceIdentifier().getId().equals(resourceID)){
					exist=true;
				}
			}
		}

		if (!exist){
			throw new Exception("the resource("+resourceID+") does not exist");
		}

	}

	public String getResourceIDbyAliase(String aliase) throws Exception {
		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){
			IResourceRepository resourceRepository = map.get(str);
			for (IResource resource:resourceRepository.listResources()){
				if (resource.getResourceDescriptor().getInformation().getName().trim().equalsIgnoreCase(aliase.trim())){
					return resource.getResourceIdentifier().getId();
				}
			}
		}

		return null;

	}


}
