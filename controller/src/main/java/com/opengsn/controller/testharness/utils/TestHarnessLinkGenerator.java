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
package com.opengsn.controller.testharness.utils;

import java.util.ArrayList;
import java.util.List;

import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.HostList;
import com.opengsn.controller.model.Link;
import com.opengsn.controller.model.LinkTable;

public class TestHarnessLinkGenerator {

	public static String DEFAULT_MASK = "255.255.255.0";	//default networkmask

	public static LinkTable getLinkTable(HostList hostList) {
		List<Host> hosts = hostList.getHosts();
		int count = hosts.size();

		List<Link> links = new ArrayList<Link>();

		for(int i=0; i<count-1; i++) {

			for (int j=i+1; j<count; j++) {
				
				Link l = new Link();

				l.setSourceIP(hosts.get(i).getHostModel().getAddress());
				l.setSourceMask(TestHarnessLinkGenerator.DEFAULT_MASK);
				l.setTargetIP(hosts.get(j).getHostModel().getAddress());
				l.setTargetMask(TestHarnessLinkGenerator .DEFAULT_MASK);
				l.setBandwidth(10000.0);
				l.setThroughput(10000.0);

				links.add(l);
			}
		}

		LinkTable table = new LinkTable();
		table.setLinks(links);

		return table;
	}

}
