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
package com.opengsn.services.facilitymanager;

import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;

import com.opengsn.services.utils.ExecutionRequest;
import com.opengsn.services.utils.ExecutionResponse;
import com.opengsn.services.utils.ModelReadRequest;
import com.opengsn.services.utils.ModelReadResponse;


/**
 * Manager of all facility resources running on different domains
 * @author knguyen
 *
 */
public class DefaultFacilityManagerProvider implements IFacilityManagerProvider 
{
	Logger logger = LoggerFactory.getLogger(DefaultFacilityManagerProvider.class);		
	/**
	 * Injected AMQP template for iaasPlatform.
	 */
	private AmqpTemplate iaasTemplate;

	/**
	 * URL to database root
	 */
	private String dbUrl;

	/**
	 * Cloud Manager Constructor, a messaging template for the networkManager,
	 * and for iaasPlatform must be provided.
	 */
	public DefaultFacilityManagerProvider(AmqpTemplate iaas, String url) {
		iaasTemplate = iaas;
		dbUrl = url;
	}
	
	
	
	public DefaultFacilityManagerProvider(){
	}


	@Override
	public String readModel(String id) {
		try {
			ModelReadRequest req = new ModelReadRequest();
			ObjectMapper mapper = new ObjectMapper();
			String reqString = mapper.writeValueAsString(req);
			Message resp = iaasTemplate.sendAndReceive(new Message(reqString
					.getBytes(), null));
			ModelReadResponse execResp = mapper.readValue(resp.getBody()
					.toString(), ModelReadResponse.class);
			  String jsonData = execResp.getModel();
			XMLSerializer serializer = new XMLSerializer(); 
              JSON json = JSONSerializer.toJSON( jsonData ); 
              String xml = serializer.write( json );  
              System.out.println(xml);    
			return execResp.getModel();
		} catch (Exception e) {
			return null;
		}
	}



	@Override
	public List<String> listResourcesByType(String type) {
		try {
			HttpClient httpClient = new StdHttpClient.Builder().url(dbUrl)
					.build();

			CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
			CouchDbConnector db = new StdCouchDbConnector("domain_greenstarnetwork_com",
					dbInstance);

			db.createDatabaseIfNotExists();

			ViewQuery query = new ViewQuery()
					.designDocId("_design/resource_instances")
					.viewName("by_type").key(type);

			List<String> resources = db.queryView(query, String.class);
			return resources;
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public String execute(String resourceId, String actionName,
			Map<String, String> parameters) {
		try {
			ExecutionRequest req = new ExecutionRequest();
			ObjectMapper mapper = new ObjectMapper();
			String reqString = mapper.writeValueAsString(req);
			Message resp = iaasTemplate.sendAndReceive(new Message(reqString
					.getBytes(), null));
			ExecutionResponse execResp = mapper.readValue(resp.getBody()
					.toString(), ExecutionResponse.class);
			return execResp.getResponseDocument();
		} catch (Exception e) {
			return "";
		}

	}



	@Override
	public String getMetricsDataByRangDate(String startDate, String endDate, String metricsID) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
