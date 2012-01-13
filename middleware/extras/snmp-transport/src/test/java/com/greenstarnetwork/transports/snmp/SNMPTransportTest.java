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
package com.greenstarnetwork.transports.snmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.iaasframework.core.transports.TransportException;

public class SNMPTransportTest {

//	@Test
	public void testConnect() {
		SNMPTransport service = new SNMPTransport("10.20.100.4");
		try {
			service.connect();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	@Test
	public void testGet() {
		SNMPTransport service = new SNMPTransport("10.20.89.2");
		try {
			service.connect();
//			service.setupPublicCommunity();
			System.err.println("System description value: " + service.get("1.3.6.1.2.1.1"));
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSend() {
		SNMPTransport service = new SNMPTransport("10.20.100.4");
		try {
			service.connect();
//			service.setupPublicCommunity();
			String command = "get 1.3.6.1.2.1.1";
			System.err.println("Sending command: " + command);
			service.send(command.toCharArray());
			InputStream in = service.getInputStream();
			byte[] buf = new byte[1024];
			in.read(buf);
			System.err.println("System description value: " + new String(buf));
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testWalk() {
//		SNMPTransport service = new SNMPTransport("10.20.89.2");
		SNMPTransport service = new SNMPTransport("10.20.100.4");
		try {
			service.connect();
//			service.setupPublicCommunity();
			String command = "walk 1.3.6.1.2.1.1";
			System.err.println("Sending command: " + command);
			service.send(command.toCharArray());

			InputStream in = service.getInputStream();
			byte[] buf = new byte[102400];
			in.read(buf);
			System.err.println("System description value: " + new String(buf));
			
			
//			InputStream in = service.getInputStream();
//			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
//			String s = bf.readLine();
//			while (!s.isEmpty())
//			{
//				System.err.println("READ: " + s);
//				s = bf.readLine();
//			}
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
