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
package com.opengsn.services.networkmanager.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.opengsn.services.networkmanager.INetworkManager;

/**
 * Handling connection with other modules through a JMS queue
 * 
 * @author knguyen
 * 
 */
public class NetworkManagerListener implements MessageListener {
	Logger logger = LoggerFactory.getLogger(NetworkManagerListener.class);

	private INetworkManager man = null; // singleton NetworkManager instance

	/**
	 * @param man
	 *            the man to set
	 */
	public void setMan(INetworkManager man) {
		this.man = man;
	}

	/**
	 * @return the man
	 */
	public INetworkManager getMan() {
		return man;
	}

	@Override
	public void onMessage(Message msg) {
		String messageStr = new String(msg.getBody());
		try {
			logger.debug("Network Manager Get Request Message: "
					+ messageStr.toString());
			// TODO ResponseMessage ret = this.man.executeCommand(
			// ((ActionRequestMessage)engineMessage).getRequest() );
			msg.getMessageProperties().getCorrelationId();
			msg.getMessageProperties().getReplyTo();
			// ret.setMessageID(engineMessage.getMessageID());
		}
		catch (AmqpException e) {
			e.printStackTrace();
		}
	}

}
