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
/**
 * 
 */
package com.greenstarnetwork.capabilities.archive;

/**
 * Keeps the state of a Command
 *  @author Scott Campbell
 * @author Daouadji Synchromedia.ca
 *
 */
public class ArchiveState {

	/** Possible state values of a command */
	public enum aState {
		READY, INITIALIZING, RESPONSE_RECEIVED, WAIT_FOR_MODEL, COMPLETE, ERROR
	};
	
	/** Current state of the command */
	private aState state;

	
	public ArchiveState() {
		state = aState.READY;
	}

	/**
	 * return the state of the command
	 * @return
	 */
	public aState getState() {
		return state;
	}

	/** 
	 * Set the state of the command
	 * @param state
	 */
	public void setState(aState state) {
		this.state = state;
	}
}
