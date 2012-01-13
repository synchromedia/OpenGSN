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
package com.greenstarnetwork.controller.model;


/**
 * Network link model
 * A link has two ends: source and target, each is represented by an IP network address.
 * A link has an amount of available bandwidth < throughput
 * @author knguyen
 *
 */
public class Link  implements Comparable<Link>
{
	private String sourceIP = null;			//Source Network IP
	private String sourceMask = null;		//Source IP Mask
	private String targetIP = null;			//Target Network IP
	private String targetMask = null;		//Target IP Mask
	private double throughput = 0;		//capacity  (MB)
	private double bandwidth = 0;		//available bandwidth  (MB)
	
	public Link() {
	}

	public String getSourceIP() {
		return this.sourceIP;
	}
	
	public void setSourceIP(String ip) {
		this.sourceIP = ip;
	}
	
	public String getSourceMask() {
		return this.sourceMask;
	}
	
	public void setSourceMask(String mask) {
		this.sourceMask = mask;
	}
	
	public String getTargetIP() {
		return this.targetIP;
	}
	
	public void setTargetIP(String ip) {
		this.targetIP = ip;
	}
	
	public String getTargetMask() {
		return this.targetMask;
	}
	
	public void setTargetMask(String mask) {
		this.targetMask = mask;
	}
	
	public double getThroughput() {
		return this.throughput;
	}
	
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	
	public double getBandwidth() {
		return this.bandwidth;
	}
	
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

	@Override
	public int compareTo(Link l) {
		if (this.bandwidth == l.bandwidth)
			return 0;
		return (this.bandwidth > l.bandwidth) ? 1 : -1; 
	}
	
	public boolean equals(Link l) {
		if ( (this.sourceIP == l.getSourceIP()) && (this.targetIP == l.getTargetIP())
			|| ((this.targetIP == l.getSourceIP()) && (this.sourceIP == l.getTargetIP())) )
			return true;
		return false;
	}
}
