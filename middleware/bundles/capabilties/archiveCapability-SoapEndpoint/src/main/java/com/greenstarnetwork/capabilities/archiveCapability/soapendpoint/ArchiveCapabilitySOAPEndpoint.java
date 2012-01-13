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
package com.greenstarnetwork.capabilities.archiveCapability.soapendpoint;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.greenstarnetwork.capabilities.archive.api.IArchiveClient;



@WebService
public class ArchiveCapabilitySOAPEndpoint implements IArchiveCapabilitySOAPEndpoint{
	private IArchiveClient archiveClient=null;
	
	public  ArchiveCapabilitySOAPEndpoint(IArchiveClient archiveClient) {
		this.archiveClient = archiveClient;
	}
	
	
	@WebMethod
	public String getArchiveData(String date, String resourceName) {
		String string =archiveClient.getArchiveData(date, resourceName);
		return string;
	}

	
	@WebMethod
	public String getArchiveDataByRangDate(String StartDate, String endDate,
			String resourceName) 
	{
		String string =archiveClient.getDataByRange(StartDate, endDate, resourceName);
	     return string;
	}

	
	public String getPieceOfArchiveDataString(String date, String queryXPATH,
			String resourceName) {
		String string =archiveClient.getPieceOfArchiveData(date, queryXPATH, resourceName);
		return string;
	}

	

}
