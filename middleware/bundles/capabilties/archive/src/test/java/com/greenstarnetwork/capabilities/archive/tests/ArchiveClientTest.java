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

import org.junit.Test;

import com.greenstarnetwork.capabilities.archive.api.ArchiveClient;
/**
 * 
 * @author abdelhamid
 *
 */

public class ArchiveClientTest {
	/**
	 * 
	 */
	
	//@Test 
	public void testgetArchiveData(){
		
		 ArchiveClient archive= new ArchiveClient();
	
		String string= archive.getArchiveData("2011126", "climateets");
		
	   System.out.println(string);
	
	}
	
	/**
	 * 
	 */
	
	
	//@Test 
	public void testgetDataByRange(){
		
		 ArchiveClient archive= new ArchiveClient();
	
		String string= archive.getDataByRange("20110228","20110301", "climateets");
		
	   System.out.println(string);
	
	}
	
	/**
	 * 
	 */
	
	
	@Test 
	public void testgetPieceOfArchiveData(){
//		
//	ArchiveClient archive= new ArchiveClient();
//	String query="(//climate[time<=11.32]/temperatureelement/temperature/text()) && (//climate[time<=11.32]/time/text())";
//	
//	String string= archive.getPieceOfArchiveData("201127",query, "climateets");
//		
//	   System.out.println(string);
	
	}
}
