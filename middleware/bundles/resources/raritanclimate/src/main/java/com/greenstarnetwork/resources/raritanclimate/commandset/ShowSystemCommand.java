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
package com.greenstarnetwork.resources.raritanclimate.commandset;

import java.util.Map;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.climate.HumidityElement;
import com.greenstarnetwork.models.climate.TemperatureElement;

import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Show system command handler. Display Temperature sensor contained by a
 * system.
 * 
 * @author K.-K.Nguyen <synchromedia.ca>
 * @author A. Daouadji <synchromedia.ca>
 */
public class ShowSystemCommand extends AbstractCommandWithProtocol {
	// private Logger logger = LoggerFactory.getLogger(ShowSystemCommand.class);
	public static final String COMMAND = "ShowSystemCommand"; // Query command
																// ID

	public ShowSystemCommand() {
		super(COMMAND);
	}

	@Override
	public void executeCommand() throws CommandException {
		if (!initialized) {
			initializeWithModel();
		} else {
			String command = PCRCommandList.SHOW;

			Map<Object, Object> args = this.commandRequestMessage
					.getArguments();
			if (args.containsKey((String) "System")) {
				command += " " + (String) args.get((String) "System");

			} else
				throw new CommandException(
						"Invalid command argument!. Usage:ShowSystemCommand systemID");
			sendCommandToProtocol(command);
		}
	}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException {
		initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException {

		CLIResponseMessage msg = (CLIResponseMessage) (((ProtocolResponseMessage) response)
				.getProtocolMessage());
		String s = msg.getRawMessage();
		StringTokenizer tokenizer = new StringTokenizer(s, "\n");
		String line = tokenizer.nextToken();

		boolean start_targets = false;
		while (line != null) {

			line = line.trim();
			if (line.compareTo(PCRCommandList.PROMPT) == 0)
				break;
			if (start_targets) {
				boolean add_new = false;
				// line.indexOf("tempsensor")
				if ( // line.equals("/system1/ntempsensor1")||
				line.equals("/system1/ntempsensor2")
						|| line.equals("/system1/ntempsensor3")
						|| line.equals("/system1/ntempsensor4")
						|| line.equals("/system1/ntempsensor5")
						|| line.equals("/system1/ntempsensor6")
						|| line.equals("/system1/ntempsensor7")
						|| line.equals("/system1/ntempsensor8")

				) {
					TemperatureElement elem = ((Climate) model)
							.getTemperatureelement(line);
					if (elem == null) {
						elem = new TemperatureElement();
						add_new = true;
					}
					elem.setID(line);
					if (add_new)
						((Climate) model).addclimate(elem);
				} else if ((line.equals("/system1/nsensor1"))
						|| (line.equals("/system1/nsensor2"))
						|| (line.equals("/system1/nsensor3"))
						|| (line.equals("/system1/nsensor4"))
						|| (line.equals("/system1/nsensor5"))
						|| (line.equals("/system1/nsensor6"))
						|| (line.equals("/system1/nsensor7"))
						|| (line.equals("/system1/nsensor8"))

				) {
					HumidityElement elem = ((Climate) model)
							.getHumidityelement(line);
					if (elem == null) {
						elem = new HumidityElement();
						add_new = true;

					}
					elem.setID(line);
					if (add_new)
						((Climate) model).addclimate(elem);

				}

				else if ((line.equals("/system1/externalsensor1"))
						|| (line.equals("/system1/externalsensor2"))
						|| (line.equals("/system1/externalsensor3"))
						|| (line.equals("/system1/externalsensor4"))
						|| (line.equals("/system1/externalsensor5"))
						|| (line.equals("/system1/externalsensor6"))
						|| (line.equals("/system1/externalsensor7"))
						|| (line.equals("/system1/externalsensor8"))
						|| (line.equals("/system1/externalsensor9"))
						|| (line.equals("/system1/externalsensor10"))
						|| (line.equals("/system1/externalsensor11"))
						|| (line.equals("/system1/externalsensor12"))
						|| (line.equals("/system1/externalsensor13"))
						|| (line.equals("/system1/externalsensor14"))
						|| (line.equals("/system1/externalsensor15"))
						|| (line.equals("/system1/externalsensor16"))

				) {
					HumidityElement elem = ((Climate) model)
							.getHumidityelement(line);
					if (elem == null) {
						elem = new HumidityElement();
						add_new = true;

					}
					elem.setID(line);
					if (add_new) {
						((Climate) model).addclimate(elem);
						add_new = false;
					}

					TemperatureElement elem2 = ((Climate) model)
					.getTemperatureelement(line);
					if (elem2 == null) {
						elem2 = new TemperatureElement();
						add_new = true;
						}
			elem2.setID(line);
				if (add_new)
				((Climate) model).addclimate(elem2);
				
				
				}

		

			}

			else if (line.startsWith("Targets:"))
				start_targets = true;

			line = tokenizer.nextToken();
		}

	}

	@Override
	public void responseReceived(ICapabilityMessage response)
			throws CommandException {
		if (response instanceof ProtocolResponseMessage) {
			this.response = (ProtocolResponseMessage) response;
			this.requestEngineModel(false);
		} else if (response instanceof ProtocolErrorMessage) {
			this.errorMessage = (ProtocolErrorMessage) response;
			CommandException commandException = new CommandException(
					"Error executing command " + this.commandID,
					((ProtocolErrorMessage) response).getProtocolException());
			commandException.setName(this.commandID);
			commandException.setCommand(this);
			this.sendCommandErrorResponse(commandException);
		}
	}
}
