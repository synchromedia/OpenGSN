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
package com.greenstarnetwork.capabilities.archive.api;

import java.util.Map;

import com.iaasframework.resources.core.message.CapabilityMessage;

public class ArchiveRequestMessage  extends CapabilityMessage{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** The arguments of the command to execute **/
	private Map<Object, Object> arguments = null;
	/** ID of the command to execute */
	private String archiveID = null;	
	
	public ArchiveRequestMessage() {
	}
	
	public ArchiveRequestMessage(String archiveID) {
	this.archiveID=archiveID;
	}
	
	public String getArchiveID() {
		return archiveID;
	}

	public Map<Object, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<Object, Object> arguments) {
		this.arguments = arguments;
	}

	public void setArchiveID(String archiveID) {
		this.archiveID = archiveID;
	}


	
}
