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
package com.greenstarnetwork.capabilities.archive;

import com.iaasframework.resources.core.capability.CapabilityException;

/**
 * 
 * @author abdelhamid
 *
 */
public class ArchiveException extends CapabilityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an instance of ArchiveException
	 * 
	 * @param message
	 * @param e
	 */
	
	public ArchiveException(String message, CapabilityException e) {
		super(message, e);
	}

	/**
	 * Constructs an instance of ArchiveException
	 * 
	 * @param msg
	 * @param e
	 */
	
	public ArchiveException(String msg, Exception e) {
		super(msg, e);
	}
	
	/**
	 * Constructs an instance of ArchiveException
	 * 
	 * @param msg
	 */
	public ArchiveException(String msg) {
		super(msg);
	}
	
	public ArchiveException(Exception e) {
		super("",e);
	}
}
