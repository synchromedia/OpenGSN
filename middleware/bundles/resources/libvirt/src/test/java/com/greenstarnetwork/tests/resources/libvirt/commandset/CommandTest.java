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
package com.greenstarnetwork.tests.resources.libvirt.commandset;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.greenstarnetwork.models.vmm.VMStatus;
import com.greenstarnetwork.models.vmm.VmHostModel;
import com.greenstarnetwork.resources.libvirt.commandset.CreateVMCommand;
import com.greenstarnetwork.resources.libvirt.commandset.DestroyCommand;
import com.greenstarnetwork.resources.libvirt.commandset.EchoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.GetMACCommand;
import com.greenstarnetwork.resources.libvirt.commandset.HostRunningInfoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.NodeInfoCommand;
import com.greenstarnetwork.resources.libvirt.commandset.UpdateCommand;
import com.greenstarnetwork.resources.libvirt.commandset.VMInfoCommand;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.commandset.api.CommandRequestMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * 
 * @author knguyen
 *
 */
public class CommandTest {

	private VmHostModel model;
	private ProtocolResponseMessage responseMessage;
	
	@Before
	public void setup() throws CommandException {
		try {
			model = new VmHostModel();
			model.setAddress("10.20.100.2");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testNodeInfoCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/nodeinfo.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			NodeInfoCommand command = new NodeInfoCommand();
			command.setModel(model, null);

			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getCpu(), "x86_64");
			Assert.assertEquals(model.getNbrCPUs(), "16");
			Assert.assertEquals(model.getSpeed(), "1596");
//			System.err.println(model.toXML());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testHostRunningfoCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/hostrunning.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			HostRunningInfoCommand command = new HostRunningInfoCommand();
			command.setModel(model, null);

			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

//			Assert.assertEquals(model.getCPUIdleTime(), "92.8");
//			Assert.assertEquals(model.getCPUStealTime(), "0.0");
//			Assert.assertEquals(model.getBufferMemory(), "372304");
//			Assert.assertEquals(model.getTotalSwapMemory(), "4321444");
			Assert.assertEquals(model.getCachedSwapMemory(), "313972");
			
//			System.err.println(model.toXML());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
//	@Test
	public void testDestroyCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/destroy.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareTestModel();
			DestroyCommand command = new DestroyCommand();
			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("vmName", "vm1");
			
			CommandRequestMessage request = new CommandRequestMessage();
			request.setArguments(args);
			command.setCommandRequestMessage(request);

			
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getVMList().size(), 0);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Ignore
	public void testEchoCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/start.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
//			stringsToRemove.add("[user@server ~]$");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);
			EchoCommand command = new EchoCommand();
			command.setModel(model, null);
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertTrue(true);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testCreateCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/create.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			CreateVMCommand command = new CreateVMCommand();
			command.setModel(model, null);
			
			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("vmName", "vm1");
			args.put("memory", "131072");
			args.put("cpu", "1");
			args.put("mac", "54:52:00:47:53:7D");
			args.put("ip", "10.20.100.125");
			args.put("template", "/home/vmm/vmtempl.qcow2");
			
			CommandRequestMessage request = new CommandRequestMessage();
			request.setArguments(args);
			command.setCommandRequestMessage(request);
			
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertEquals(model.getVM("vm1").getName(), "vm1");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testUpdateCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/list.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareTestModel();
			UpdateCommand command = new UpdateCommand();
			command.setModel(model, null);
			
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			
			Assert.assertEquals(model.getVM("Instance110214_server91032_1297700647050").getStatus(), VMStatus.STOPPED);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testVMInfoCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/dominfo.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareTestModel();
			VMInfoCommand command = new VMInfoCommand();
			command.setModel(model, null);

			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("vmName", "vm1");
			
			CommandRequestMessage request = new CommandRequestMessage();
			request.setArguments(args);
			command.setCommandRequestMessage(request);
			
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);

			Assert.assertNotNull(model);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
//	@Test
//	public void testVMModelBuilder() {
//		Map<Object, Object> args = new Hashtable<Object, Object>();
//		args.put("vmName", "vm1");
//		args.put("memory", "512");
//		args.put("cpu", "Intel");
//		args.put("storagePath", "vm1");
//		args.put("xmlFile", "/tmp/test.xml");
//
//		String domName = (String)args.get((String)"vmName");
//		String memory = (String)args.get((String)"memory");
//		String cpu = (String)args.get((String)"cpu");
//		String image = (String)args.get((String)"storagePath");
//		String xmlFile = (String)args.get((String)"xmlFile");
//
//		
//		VMModelBuilder vmModelBuilder = new VMModelBuilder();
//		String SimpleVMModel = vmModelBuilder.createSimpleVMModel(domName, memory, image, cpu);
//		Assert.assertNotNull(SimpleVMModel);
//	}
	
//	@Test
	public void testVmmModel() {
		prepareTestModel();
		System.err.println("*********Test model ToXML: " + model.toXML());
	}

	@Test
	public void testGetMACCommand() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/vmmac.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			prepareTestModel();
			GetMACCommand command = new GetMACCommand();
			command.setModel(model, null);

			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("vmName", "vm1");
			
			CommandRequestMessage request = new CommandRequestMessage();
			request.setArguments(args);
			command.setCommandRequestMessage(request);
			
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
			
			Assert.assertNotNull(model.getVM("vm1"));
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public void prepareTestModel() {
		try {
			String s = readStringFromFile(getClass().getClassLoader().getResourceAsStream("simulator/create.txt"));
			ArrayList<String> stringsToRemove = new ArrayList<String>();
			ArrayList<String> errorPatterns = new ArrayList<String>();
			stringsToRemove.add("[user@server ~]$");
			errorPatterns.add("error");
			CLIResponseMessage resp = CLIResponseMessage.parse(s, stringsToRemove, errorPatterns);

			CreateVMCommand command = new CreateVMCommand();
			command.setModel(model, null);
			
			Map<Object, Object> args = new Hashtable<Object, Object>();
			args.put("vmName", "vm1");
			args.put("memory", "131072");
			args.put("cpu", "1");
//			args.put("mac", "54:52:00:47:53:7D");
			args.put("ip", "10.20.100.125");
			args.put("template", "/home/vmm/vmtempl.qcow2");
			
			CommandRequestMessage request = new CommandRequestMessage();
			request.setArguments(args);
			command.setCommandRequestMessage(request);
			
			responseMessage = new ProtocolResponseMessage();
			responseMessage.setProtocolMessage(resp);
			command.setResponse(responseMessage);
			command.parseResponse(model);
		}
		catch (Exception e) {
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
