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
package com.greenstarnetwork.tests.resources.servertechCWG.commandset;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenstarnetwork.models.pdu.PDUException;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.greenstarnetwork.resources.servertechCWG.commandset.CWGCommandList;
import com.greenstarnetwork.resources.servertechCWG.commandset.IStatCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.OStatCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.OffCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.OnCommand;
import com.greenstarnetwork.resources.servertechCWG.commandset.StatusCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * Test ServerTech PDU commands 
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
			model.setHost("10.20.100.5");
			model.setPort("20");
			
		} catch (PDUException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testStatusResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/StatusCommand.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add(CWGCommandList.SUCCESS);
			stringsToRemove.add(CWGCommandList.PROMPT);

			errorPatterns.add("error:");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			
			StatusCommand statuscommand = new StatusCommand();
			statuscommand.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			statuscommand.setResponse(responseMessage);
			statuscommand.parseResponse(model);
			Assert.assertEquals(model.getOutlets().get(13).getLoad(), "0.39");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testOStatResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/OStatCommand.txt"));
			
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add(CWGCommandList.SUCCESS);
			stringsToRemove.add(CWGCommandList.PROMPT);

			errorPatterns.add("error:");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			OStatCommand command = new OStatCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			Assert.assertEquals(model.getOutlets().get(1).getLoad(), "0.31");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testIStatResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/IStatCommand.txt"));
			
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add(CWGCommandList.SUCCESS);
			stringsToRemove.add(CWGCommandList.PROMPT);

			errorPatterns.add("error:");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			
			
			IStatCommand istatCommand = new IStatCommand();
			istatCommand.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			istatCommand.setResponse(responseMessage);
			istatCommand.parseResponse(model);
			Assert.assertEquals(model.getInFeeds().get(0).getLoad(), "2.54");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testOnSuccessResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/OnCommandSuccess.txt"));

			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add(CWGCommandList.SUCCESS);
			stringsToRemove.add(CWGCommandList.PROMPT);

			errorPatterns.add(CWGCommandList.ERROR_NOT_FOUND);
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			//Prepare model for ON command test
			prepareModelForTest();
			
			OnCommand command = new OnCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			Assert.assertEquals(model.getOutlets().get(0).getStatusString(), "On");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testOffSuccessResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/OffCommandSuccess.txt"));

			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add(CWGCommandList.SUCCESS);
			stringsToRemove.add(CWGCommandList.PROMPT);

			errorPatterns.add(CWGCommandList.ERROR_NOT_FOUND);
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			//Prepare model for ON command test
			prepareModelForTest();
			
			OffCommand command = new OffCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			Assert.assertEquals(model.getOutlets().get(0).getStatusString(), "Off");
		} 
		catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void prepareModelForTest() throws CommandException {
		String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/StatusCommand.txt"));
		ArrayList<String> stringsToRemove = new ArrayList<String>();
		ArrayList<String> errorPatterns = new ArrayList<String>();
		stringsToRemove.add(CWGCommandList.SUCCESS);
		stringsToRemove.add(CWGCommandList.PROMPT);

		errorPatterns.add("error:");
		CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
		
		
		StatusCommand statuscommand = new StatusCommand();
		statuscommand.setModel(model, null);
		responseMessage = new ProtocolResponseMessage();
		responseMessage.setProtocolMessage(resp);
		statuscommand.setResponse(responseMessage);
		statuscommand.parseResponse(model);
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
