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
package com.greenstarnetwork.resources.libvirt.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.iaasframework.transports.virtual.IVirtualTransportProvider;

public class VirtualTransportProvider implements IVirtualTransportProvider {
	private String createAnswer = null;
	private String destroyAnswer = null;
	private String rebootAnswer = null;
	private String shutdownAnswer = null;
	private String startAnswer = null;
	private String resumeAnswer = null;
	private String suspendAnswer = null;
	private String dominfoAnswer = null;
	private String listAnswer = null;
	private String migrateAnswer = null;
	private String setmemAnswer = null;
	private String setmaxmemAnswer = null;
	
	
	

	public VirtualTransportProvider() {
		createAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/create.txt"));
		destroyAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/destroy.txt"));
		rebootAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/reboot.txt"));
		shutdownAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/shutdown.txt"));
		startAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/start.txt"));
		resumeAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/resume.txt"));
		suspendAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/suspend.txt"));
		dominfoAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/dominfo.txt"));
		listAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/list.txt"));
		migrateAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/migrate.txt"));
		setmemAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/setmem.txt"));
		setmaxmemAnswer = readStringFromFile(this.getClass().getResourceAsStream(
		"/simulator/setmaxmem.txt"));
		
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
		
		if (telnetMessage.indexOf("create") != -1) {
			response = createAnswer;
		}
		else if (telnetMessage.indexOf("destroy") != -1) {
			response = destroyAnswer;
		}
		else if (telnetMessage.indexOf("reboot") != -1) {
			response = rebootAnswer;
		}		
		else if (telnetMessage.indexOf("shutdown") != -1) {
			response = shutdownAnswer;
		}
		else if (telnetMessage.indexOf("start") != -1) {
			response = startAnswer;
		}
		else if (telnetMessage.indexOf("resume") != -1) {
			response = resumeAnswer;
		}
		else if (telnetMessage.indexOf("suspend") != -1) {
			response = suspendAnswer;
		}
		else if (telnetMessage.indexOf("dominfo") != -1) {
			response = dominfoAnswer;
		}
		else if (telnetMessage.indexOf("list") != -1) {
			response = listAnswer;
		}
		else if (telnetMessage.indexOf("migrate") != -1) {
			response = migrateAnswer;
		}
		else if (telnetMessage.indexOf("setmem") != -1) {
			response = setmemAnswer;
		}
		else if (telnetMessage.indexOf("setmaxmem") != -1) {
			response = setmaxmemAnswer;
		}
		else {
			response = "[user@server ~]$";
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
