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
package com.greenstarnetwork.resources.raritanclimate.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.iaasframework.transports.virtual.IVirtualTransportProvider;

public class VirtualTransportProvider implements IVirtualTransportProvider {
	private String getHumidity = null;
	private String getTemperature = null;

	private String showSystemAnswer = null;

	public VirtualTransportProvider() {
		
//		getHumidity = readStringFromFile(this.getClass().getResourceAsStream(
//				"/simulator/Shownsensor1.txt"));
//		getTemperature = readStringFromFile(this.getClass().getResourceAsStream(
//				"/simulator/ShowNTempSensor1.txt"));
//		showSystemAnswer = readStringFromFile(this.getClass().getResourceAsStream(
//		"/simulator/ShowSystem1.txt"));
//	
		getHumidity = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/ShowExternal1.txt"));
		getTemperature = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/ShowExternal4.txt"));
		showSystemAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/ShowCRCSystem.txt"));
		
	}

	public Object getMessageTransportResponse(Object arg0) {
		return null;
	}

	public byte[] getStreamTransportReponse(byte[] request) {
		System.out.print("Received message: ");
		for (int i = 0; i < request.length; i++) {
			System.out.print((char) request[i]);
		}
		System.out.println();
		String telnetMessage = new String(request);
		String response = null;
		
		if (telnetMessage.indexOf("username") != -1) {
			response = "clp:/->";
		}
		else if (telnetMessage.indexOf("password") != -1) {
			response = "clp:/->";
		}
		else if ((telnetMessage.indexOf("Show") != -1) && (telnetMessage.indexOf("sensor") != -1)){
			response = getHumidity;
		}
	
		else if ( (telnetMessage.indexOf("Show") != -1) && (telnetMessage.indexOf("tempsensor") != -1) ) {
			response = getTemperature;
		}
		else if (telnetMessage.indexOf("Show system") != -1) {
			response = showSystemAnswer;
		}
		else {
			response = "clp:/->";
//			response = "error";
		}

		return response.getBytes();
	}

	public String readStringFromFile(InputStream stream) {
		String answer = null;

		try {
			InputStreamReader streamReader = new InputStreamReader(stream);
			StringBuffer fileData = new StringBuffer(10000);
			BufferedReader reader = new BufferedReader(streamReader);
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			answer = fileData.toString();
			fileData = null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return answer;
	}

}
