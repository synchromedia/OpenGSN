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
package com.greenstarnetwork.resources.libvirt.commandset;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.vmm.VmHostModel;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;

/**
 * 
 * @author knguyen
 *
 */
public class HostRunningInfoCommand extends BasicCommand {
	public static final String COMMAND = "HostRunningInfoCommand";
	
	public HostRunningInfoCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		}else
		{
			String command = "top -n 1";
			sendCommandToProtocol(command);
		}
	
	}
	
	@Override
	public void parseResponse(IResourceModel model) throws CommandException {
			CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
			String s = msg.getRawMessage();

			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken();
			while (line != null) 
			{
				String prop = getLineProp(line);
				if (prop != null)
				{
					if (prop.compareTo("Cpu(s)") == 0)
						parseCPUTime(((VmHostModel)model), line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Mem") == 0)
						parseMem(((VmHostModel)model), line.substring(prop.length()+1).trim());
					else if (prop.compareTo("Swap") == 0)
						parseSwap(((VmHostModel)model), line.substring(prop.length()+1).trim());
				}
				
				try {
					line = tokenizer.nextToken();
					if (line.endsWith("COMMAND"))
						break;
				}catch (NoSuchElementException e) {
					break;
				}
			}			
	}

	private void parseCPUTime(VmHostModel model, String data) {
		StringTokenizer tokenizer = new StringTokenizer(data, ",");
		String token = tokenizer.nextToken().trim();
		while (token != null) 
		{
			if (token.endsWith("us"))
				model.setCPUUserTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("sy") > 0)
				model.setCPUSystemTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("ni") > 0)
				model.setCPUNiceTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("ni") > 0)
				model.setCPUNiceTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("id") > 0)
				model.setCPUIdleTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("id") > 0)
				model.setCPUIdleTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("wa") > 0)
				model.setCPUIOTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("hi") > 0)
				model.setCPUHardwareTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("si") > 0)
				model.setCPUSoftwareTime(reformatString(token.substring(0, token.length() - 2).trim()));
			else if (token.indexOf("st") > 0)
				model.setCPUStealTime(reformatString(token.substring(0, token.length() - 2).trim()));
			try {
				token = tokenizer.nextToken();
			}catch (NoSuchElementException e) {
				break;
			}
		}
	}
	
	private void parseMem(VmHostModel model, String data) {
		StringTokenizer tokenizer = new StringTokenizer(data, ",");
		String token = tokenizer.nextToken().trim();
		while (token != null) 
		{
//			if (token.endsWith("total"))
//				model.setTotalMemory(reformatString(token.substring(0, token.length() - 6).trim()));
			if (token.indexOf("free") > 0)
				model.setFreeMemory(reformatString(token.substring(0, token.length() - 5).trim()));
			else if (token.indexOf("buffers") > 0)
				model.setBufferMemory(reformatString(token.substring(0, token.length() - 8).trim()));
			try {
				token = tokenizer.nextToken();
			}catch (NoSuchElementException e) {
				break;
			}
		}
	}

	private void parseSwap(VmHostModel model, String data) {
		StringTokenizer tokenizer = new StringTokenizer(data, ",");
		String token = tokenizer.nextToken().trim();
		while (token != null) 
		{
			if (token.indexOf("total") > 0)
				model.setTotalSwapMemory(reformatString(token.substring(0, token.length() - 6).trim()));
			else if (token.indexOf("free") > 0)
				model.setFreeSwapMemory(reformatString(token.substring(0, token.length() - 5).trim()));
			else if (token.indexOf("cached") > 0)
				model.setCachedSwapMemory(reformatString(token.substring(0, token.length() - 7).trim()));
			try {
				token = tokenizer.nextToken();
			}catch (NoSuchElementException e) {
				break;
			}
		}
	}
}