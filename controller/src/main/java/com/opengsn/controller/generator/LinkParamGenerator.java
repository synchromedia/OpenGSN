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
package com.opengsn.controller.generator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.opengsn.controller.ControllerException;
import com.opengsn.controller.ControllerProvider;
import com.opengsn.controller.model.Host;
import com.opengsn.controller.model.Link;
import com.opengsn.controller.model.LinkTable;

/**
 * Generate links parameters
 * @author knguyen
 *
 */
public class LinkParamGenerator  extends BaseGenerator 
{
	
	public static double MAX_LINK_THROUGHPUT = 1000000;	//link throughput (MB)

	private double max_throughput = MAX_LINK_THROUGHPUT;
	
	public LinkParamGenerator() {
	}
	
	public double getMaxThroughput() {
		return this.max_throughput;
	}
	
	public void setMaxThroughput(double throughput) {
		this.max_throughput = throughput;
	}
	
	/**
	 * Generate a link connecting two network
	 * @param source
	 * @param target
	 * @return
	 */
	public Link generateFakeLink(InetAddress source, InetAddress sourceMask, InetAddress target, InetAddress targetMask) 
	{
		Link l = new Link();
		String saddr = source.toString();
		if (saddr.startsWith("/"))
			saddr = saddr.substring(1);
		l.setSourceIP(saddr);
		saddr = sourceMask.toString();
		if (saddr.startsWith("/"))
			saddr = saddr.substring(1);
		l.setSourceMask(saddr);
		
		String taddr = target.toString();
		if (taddr.startsWith("/"))
			taddr = taddr.substring(1);
		l.setTargetIP(taddr);
		taddr = targetMask.toString();
		if (taddr.startsWith("/"))
			taddr = taddr.substring(1);
		l.setTargetMask(taddr);
		
		double throughput = this.generateDouble((int)MAX_LINK_THROUGHPUT);
		l.setThroughput(throughput);
		l.setBandwidth(this.generateDouble((int)throughput));
		return l;
	}
	
	/**
	 * Randomly generate links between a set of sources and a set of targets
	 * @param sources
	 * @param targets
	 * @return
	 */
	public LinkTable generateFakeLinkTable(List<InetAddress> ips, List<InetAddress> masks) throws ControllerException
	{
		int n = ips.size();
		if (n != masks.size())
			throw new ControllerException("Failed when generating link table: Sizes of IP and mask lists are not equal.");
		List<Link> links = new ArrayList<Link>();
		for (int i=0; i<n-1; i++)
		{
			for (int j=i+1; j<n; j++)
			{
				Link li = this.generateFakeLink(ips.get(i), masks.get(i),
						ips.get(j), masks.get(j));
				links.add(li);
			}
		}
		
		LinkTable table = new LinkTable();
		table.setLinks(links);
		
		return table;
	}
	
	/**
	 * Generate a mesh network linking all hosts in a given list
	 * @param hosts
	 * @return
	 */
	public LinkTable generateMeshLinkTable(List<Host> hosts) {
		List<Link> links = new ArrayList<Link>();
		int n = hosts.size();
		try {
			for (int i=0; i<n-1; i++)
			{
				for (int j=i+1; j<n; j++)
				{
					Link li = this.generateFakeLink(InetAddress.getByName(hosts.get(i).getHostModel().getAddress()), 
							InetAddress.getByName(ControllerProvider.DEFAULT_MASK),
							InetAddress.getByName(hosts.get(j).getHostModel().getAddress()), 
							InetAddress.getByName(ControllerProvider.DEFAULT_MASK));
					links.add(li);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkTable table = new LinkTable();
		table.setLinks(links);
		return table;
	}
}
