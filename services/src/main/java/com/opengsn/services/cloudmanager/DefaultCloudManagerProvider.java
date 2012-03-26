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
package com.opengsn.services.cloudmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.lightcouch.CouchDbClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;

import com.opengsn.services.cloudmanager.model.Host;
import com.opengsn.services.networkmanager.messages.NetworkManagerRequest;
import com.opengsn.services.networkmanager.messages.NetworkManagerResponse;
import com.opengsn.services.networkmanager.model.VMNetAddress;
import com.opengsn.services.utils.ExecutionRequest;
import com.opengsn.services.utils.ExecutionResponse;
import com.opengsn.services.utils.ModelReadRequest;
import com.opengsn.services.utils.ModelReadResponse;

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

	CouchDbClient dbClient;

	/**
	 * Cloud Manager Constructor, a messaging template for the networkManager,
	 * and for iaasPlatform must be provided.
	 */
	public DefaultCloudManagerProvider(AmqpTemplate networkManager, AmqpTemplate iaas) {
		networkManagerTemplate = networkManager;
		iaasTemplate = iaas;
		try {
			dbClient = new CouchDbClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			List<HostEngine> hostEngines = dbClient.view("engine_instances/by_type")
					.key("libvirt")
					.includeDocs(true)
					.query(HostEngine.class);
			List<Host> hosts = new ArrayList<Host>();
			/**for(int i=0; i<hostEngines.size(); i++){
				hosts.add(i,getHostInformation(hostEngines.get(i).get_id()));
			}**/
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
		return null;//hosts;
	}

}
