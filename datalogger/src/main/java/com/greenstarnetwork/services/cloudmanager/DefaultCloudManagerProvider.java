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
package com.greenstarnetwork.services.cloudmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;

import com.greenstarnetwork.services.cloudmanager.model.Host;
import com.greenstarnetwork.services.networkmanager.messages.NetworkManagerRequest;
import com.greenstarnetwork.services.networkmanager.messages.NetworkManagerResponse;
import com.greenstarnetwork.services.networkmanager.model.VMNetAddress;
import com.greenstarnetwork.services.utils.ExecutionRequest;
import com.greenstarnetwork.services.utils.ExecutionResponse;
import com.greenstarnetwork.services.utils.ModelReadRequest;
import com.greenstarnetwork.services.utils.ModelReadResponse;

/**
 * 
 * @author Ali LAHLOU (Synchromedia, ETS)
 * @author Mathieu Lemay (Inocybe Technologies inc.)
 * 
 *         The AMQP cloud manager sends messages to the AMQP queues of the IaaS
 *         Framework platform to communicate with engines and resources. As such
 *         will work only in a single deployment domain.
 * 
 */

public class DefaultCloudManagerProvider implements ICloudManagerProvider {

	/**
	 * Injected AMQP template for network manager.
	 */
	private AmqpTemplate networkManagerTemplate;

	/**
	 * Injected AMQP template for iaasPlatform.
	 */
	private AmqpTemplate iaasTemplate;

	/**
	 * URL to database root
	 */
	private String dbUrl;
	CouchDbConnector db ;
	
	/**
	 * Cloud Manager Constructor, a messaging template for the networkManager,
	 * and for iaasPlatform must be provided.
	 */
	public DefaultCloudManagerProvider(AmqpTemplate networkManager, AmqpTemplate iaas, String dburl, String dbName) {
		networkManagerTemplate = networkManager;
		iaasTemplate = iaas;
		dbUrl = dburl; 
		try{
		HttpClient httpClient = new StdHttpClient.Builder().url(dbUrl).build();

		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		 db = new StdCouchDbConnector(dbName, dbInstance);
		}
		catch(Exception e){e.printStackTrace();}
	}

	public VMNetAddress getNetworkAddress(String vmName) {
		try {
			Map<String, String> args = new HashMap<String, String>();
			args.put("vmName", vmName);
			NetworkManagerRequest req = new NetworkManagerRequest(NetworkManagerRequest.CMD_assignVMAddress, args);
			ObjectMapper mapper = new ObjectMapper();
			String reqString = mapper.writeValueAsString(req);
			Message resp = networkManagerTemplate.sendAndReceive(new Message(reqString.getBytes(), null));
			NetworkManagerResponse nres = mapper.readValue(resp.getBody().toString(), NetworkManagerResponse.class);
			return VMNetAddress.fromXML(nres.getMessage());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Host> listAllHosts() {
		try {
			HttpClient httpClient = new StdHttpClient.Builder().url(dbUrl).build();

			CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
			CouchDbConnector db = new StdCouchDbConnector("domain_greenstarnetwork_com", dbInstance);

			db.createDatabaseIfNotExists();

			ViewQuery query = new ViewQuery().designDocId("_design/resource_instances").viewName("by_type").key("Host");

			List<Host> hosts = db.queryView(query, Host.class);
			return hosts;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Host getHostInformation(String id) {
		try {
			ModelReadRequest req = new ModelReadRequest();
			ObjectMapper mapper = new ObjectMapper();
			String reqString = mapper.writeValueAsString(req);
			Message resp = iaasTemplate.sendAndReceive(new Message(reqString.getBytes(), null));
			ModelReadResponse execResp = mapper.readValue(resp.getBody().toString(), ModelReadResponse.class);
			Host host = new Host();
			return host;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String execute(String resourceId, String actionName, Map<String, String> parameters) {
		try {
			ExecutionRequest req = new ExecutionRequest();
			ObjectMapper mapper = new ObjectMapper();
			String reqString = mapper.writeValueAsString(req);
			Message resp = iaasTemplate.sendAndReceive(new Message(reqString.getBytes(), null));
			ExecutionResponse execResp = mapper.readValue(resp.getBody().toString(), ExecutionResponse.class);
			return execResp.getResponseDocument();
		} catch (Exception e) {
			return "";
		}

	}

	@Override
	public List<Host> hostQuery(String ip, String location, String freeMemory, String availableCPU, String vmUUID) {
		ViewQuery query = new ViewQuery().designDocId("_design/resource_instances").viewName("by_type").key("ip");

		db.createDatabaseIfNotExists();

		List<Host> hosts = db.queryView(query, Host.class);
		return hosts;
	}

}
