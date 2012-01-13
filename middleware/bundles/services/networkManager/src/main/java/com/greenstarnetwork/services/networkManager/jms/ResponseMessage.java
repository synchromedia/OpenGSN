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
package com.greenstarnetwork.services.networkManager.jms;

import com.iaasframework.capabilities.actionset.api.ActionResponseMessage;

/**
 * This ResponseMessage is sent by the NetworkManager to a caller in response to receiving an ActionRequestMessage
 *
 */
public class ResponseMessage extends ActionResponseMessage {
	private static final long serialVersionUID = 1L;
	
	private boolean isError = false;

	/**
	 * @param isError the isError to set
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}

	/**
	 * @return the isError
	 */
	public boolean isError() {
		return isError;
	}
	
	public void setErrorMessage(String msg) {
		this.setMessage(msg);
		this.setError(true);
	}
}
