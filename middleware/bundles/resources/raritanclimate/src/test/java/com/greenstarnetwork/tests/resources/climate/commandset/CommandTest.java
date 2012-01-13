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
package com.greenstarnetwork.tests.resources.climate.commandset;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.resources.raritanclimate.commandset.GetHumidityCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.GetTemperatureCommand;
import com.greenstarnetwork.resources.raritanclimate.commandset.ShowSystemCommand;

import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Test Raritan PDU commands 
 * @author K.-K.Nguyen <synchromedia.ca>
 * @author A.Daouadji <synchromedia.ca>
 */
public class CommandTest {

	private Climate model;
	private ProtocolResponseMessage responseMessage;
	
	@Before
	public void setup() throws CommandException {
		model = new Climate();
		model.setHost("10.20.100.6");
		model.setPort("22");
		model.setHumidityLowerThreshold("10.0");
		model.setHumidityUpperThreshold("90.0");
		model.setTemperatureLowerThreshold("14.0");
		model.setTemperatureUpperThreshold("35.0");
	}


//	@Test
//	public void testShowSystemResponse1() {
//		try {
//			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowSystem1.txt"));
//			ArrayList<String> stringsToRemove = new ArrayList<String>();
//			ArrayList<String> errorPatterns = new ArrayList<String>();
//			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
//			
//			
//			ShowSystemCommand command = new ShowSystemCommand();
//			command.setModel(model, null);
//			responseMessage = new ProtocolResponseMessage();
//			responseMessage.setProtocolMessage(resp);
//			command.setResponse(responseMessage);
//			command.parseResponse(model);
//			Assert.assertEquals(model.getTemperatureelement().size(), 2);
//			Assert.assertEquals(model.getHumidityelement().size(), 2);
//		
//		} 
//		catch (CommandException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}

	
	//@Test
//	public void testGetTemperatureCommand() {
//		try {
//	
//			
//			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowNTempSensor1.txt"));
//			ArrayList<String> stringsToRemove = new ArrayList<String>();
//			ArrayList<String> errorPatterns = new ArrayList<String>();
//			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
//
//			prepareModelForTest();
//			
//			GetTemperatureCommand command = new GetTemperatureCommand();
//			command.setModel(model, null);
//			responseMessage = new ProtocolResponseMessage();
//			responseMessage.setProtocolMessage(resp);
//			command.setResponse(responseMessage);
//			command.parseResponse(model);
//
//			Assert.assertEquals(model.getTemperatureelement("/system1/ntempsensor1").getName(), "CPU Temp.(244.0.32)");
//			Assert.assertEquals(model.getTemperatureelement("/system1/ntempsensor1").getTemperature(), "40.000000");
//			Assert.assertEquals(model.getTemperatureelement("/system1/ntempsensor1").getCurrentState(), "OK");
//		} 
//		catch (CommandException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}
	

	
	//@Test
//	public void testGetHumidityCommand1() {
//		try {
//	
//			
//			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/Shownsensor1.txt"));
//			ArrayList<String> stringsToRemove = new ArrayList<String>();
//			ArrayList<String> errorPatterns = new ArrayList<String>();
//			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
//
//			prepareModelForTest();
//			
//			GetHumidityCommand command = new GetHumidityCommand();
//			command.setModel(model, null);
//			responseMessage = new ProtocolResponseMessage();
//			responseMessage.setProtocolMessage(resp);
//			command.setResponse(responseMessage);
//			command.parseResponse(model);
//
//			Assert.assertEquals(model.getHumidityelement("/system1/nsensor1").getName(), "Env Humidity 1(207.0.32)");
//			Assert.assertEquals(model.getHumidityelement("/system1/nsensor1").getHumidity(), "0.000000");
//			Assert.assertEquals(model.getHumidityelement("/system1/nsensor1").getCurrentState(), "OK");
//		} 
//		catch (CommandException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}
	

	@Test
	public void testGetTemperatureCommand() {
		try {
	
			
			//String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowNTempSensor1.txt"));
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowExternal4.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareModelForTest();
			
			GetTemperatureCommand command = new GetTemperatureCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			System.err.println("*** getting tmeperature ***");
			System.err.println("******"+model.getTemperatureelement("/system1/externalsensor4").getName());
			Assert.assertEquals(model.getTemperatureelement("/system1/externalsensor4").getName(), "CRC SPN Enclosure Temp Sensor(199.0.32)");
			Assert.assertEquals(model.getTemperatureelement("/system1/externalsensor4").getTemperature(), "18.000000");
			Assert.assertEquals(model.getTemperatureelement("/system1/externalsensor4").getCurrentState(), "OK");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void prepareModelForTest() throws CommandException {
		String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowCRCSystem.txt"));
		ArrayList<String> stringsToRemove = new ArrayList<String>();
		ArrayList<String> errorPatterns = new ArrayList<String>();
		CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
		
		
		ShowSystemCommand command = new ShowSystemCommand();
		command.setModel(model, null);
		responseMessage = new ProtocolResponseMessage();
		responseMessage.setProtocolMessage(resp);
		command.setResponse(responseMessage);
		command.parseResponse(model);
	}

	/**
	 * 
	 * 
	 */
@Test
	public void testShowSystemResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowCRCSystem.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			
			ShowSystemCommand command = new ShowSystemCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			Assert.assertEquals(model.getTemperatureelement().size(),16);
			Assert.assertEquals(model.getHumidityelement().size(), 24);
		
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetHumidityCommand() {
		try {
	
			
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowExternal1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareModelForTest();
			
			GetHumidityCommand command = new GetHumidityCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getHumidityelement("/system1/externalsensor1").getName(), "CRC SPN Enclosure Hum. Sensor(196.0.32)");
			Assert.assertEquals(model.getHumidityelement("/system1/externalsensor1").getHumidity(), "42.000000");
			Assert.assertEquals(model.getHumidityelement("/system1/externalsensor1").getCurrentState(), "OK");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
