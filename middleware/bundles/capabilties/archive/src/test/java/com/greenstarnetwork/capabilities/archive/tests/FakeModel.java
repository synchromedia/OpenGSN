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
package com.greenstarnetwork.capabilities.archive.tests;

import javax.xml.bind.annotation.XmlRootElement;

import com.iaasframework.capabilities.model.IResourceModel;

/** A fake model used to test the CommandModule
 * 
 * @author Scott Campbell (CRC)
 * @author Abdelhamid Daouadji (Synchromedia)
 */
@XmlRootElement
public class FakeModel implements IResourceModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value1 = null;
	private String value2 = null;
	public FakeModel(){
		value1="test2";
		value2="test1";
	}
	public IResourceModel getModel() {
		return this;
	}
	
	/**
	 * @return the value1
	 */
	public String getValue1() {
		return value1;
	}
	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	/**
	 * @return the value2
	 */
	public String getValue2() {
		return value2;
	}
	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

}
