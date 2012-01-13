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
package com.greenstarnetwork.tests.resources.raritanPCR8.commandset;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenstarnetwork.models.pdu.PDUException;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.models.pdu.PDUElement.Status;
import com.greenstarnetwork.resources.raritanPCR8.commandset.OffCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.OnCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowCurrSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowOutletCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowPowerSensorCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowSystemCommand;
import com.greenstarnetwork.resources.raritanPCR8.commandset.ShowVoltSensorCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Test Raritan PDU commands 
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class CommandTest {

	private PDUModel model;
	private ProtocolResponseMessage responseMessage;
	
	@Before
	public void setup() throws CommandException {
		try {
			model = new PDUModel();
			model.setHost("10.20.100.6");
			model.setPort("22");
			
		} catch (PDUException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testShowSystemResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowSystem1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			
			ShowSystemCommand command = new ShowSystemCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			Assert.assertEquals(model.getOutlets().size(), 8);
	
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testShowOutletResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowOutlet1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			prepareModelForTest();
			
			ShowOutletCommand command = new ShowOutletCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getOutlet("/system1/outlet1").getName(), "OUTLET1");
			Assert.assertEquals(model.getOutlet("/system1/outlet1").getStatus(), Status.On);
			Assert.assertEquals(model.getOutlet("/system1/outlet1").getStatus(), Status.On);
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testShowVoltSensorResponse() {
		try {
			prepareOutletModelForTest();
			
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowVoltSensor1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareModelForTest();
			
			ShowVoltSensorCommand command = new ShowVoltSensorCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getInFeed("/system1/nvoltsensor1").getName(), "Board 0 Volt(192.0.32)");
//			Assert.assertEquals(model.getOutlet("/system1/outlet1").getVoltage(), "1075.0");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testShowCurrSensorResponse() {
		try {
			prepareOutletModelForTest();
			
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowCurrSensor1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareModelForTest();

			ShowCurrSensorCommand command = new ShowCurrSensorCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getInFeed("/system1/ncurrsensor1").getName(), "R.01 Current(0.0.32)");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testShowPowerSensorResponse() {
		try {
			prepareOutletModelForTest();
			
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowPowerSensor13.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareModelForTest();

			ShowPowerSensorCommand command = new ShowPowerSensorCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getInFeed("/system1/nsensor13").getName(), "R.01 Apt. Power(4.0.32)");
			Assert.assertEquals(model.getInFeed("/system1/nsensor13").getType(), "PowerUnit");
			Assert.assertEquals(model.getInFeed("/system1/nsensor13").getPower(), "85.822271");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testOnResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/OnOutlet1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			prepareModelForTest();
			
			OnCommand command = new OnCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getOutlet("/system1/outlet1").getStatus(), Status.On);
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testOffResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/OffOutlet1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			prepareModelForTest();
			
			OffCommand command = new OffCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getOutlet("/system1/outlet1").getStatus(), Status.Off);
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void prepareOutletModelForTest() throws CommandException {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowOutlet1.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			prepareModelForTest();
			
			ShowOutletCommand command = new ShowOutletCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void prepareModelForTest() throws CommandException {
		String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/ShowSystem1.txt"));
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
