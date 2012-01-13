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
package com.greenstarnetwork.tests.resources.OutbackMate.commandset;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenstarnetwork.models.powersource.PowerSourceException;
import com.greenstarnetwork.models.powersource.PowerSourceModel;
import com.greenstarnetwork.protocols.datastream.message.DataStreamResponseMessage;
import com.greenstarnetwork.resources.outbackMate.commandset.TraceCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;

/**
 * Test Outback Mate Power Source commands 
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class CommandTest {

	private PowerSourceModel model;
	private ProtocolResponseMessage responseMessage;
	
	@Before
	public void setup() throws CommandException {
			try {
				model = new PowerSourceModel();
				model.setHost("10.20.100.21");
				model.setPort("22");
			} catch (PowerSourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


	@Test
	public void testTraceResponse() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/trace.txt"));
			DataStreamResponseMessage resp = new DataStreamResponseMessage();
			resp.setRawMessage(s);
			
			TraceCommand command = new TraceCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
//			System.err.println(model.toXML());
			Assert.assertEquals(model.getChargerCurrent(), "5.0");
			Assert.assertEquals(model.getInvBatteryVoltage(), "25.2");
	
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
