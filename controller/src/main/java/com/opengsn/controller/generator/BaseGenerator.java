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
import java.util.Random;

import com.opengsn.controller.tools.InetAddressUtil;

/**
 * Base class for random number generator.
 * @author knguyen
 * @author Ali LAHLOU (Synchromedia, ETS)
 */
public class BaseGenerator {

	public BaseGenerator() {
	}
	
	/**
	 * Generate n double numbers, each < max
	 * @param max
	 * @param n
	 * @return
	 */
	protected double[] generateDoubleRange(int max, int n) {
		double[] ret = new double[n];
		Random rand = new Random();
		for (int i=0; i < n; i++)
			ret[i] = rand.nextDouble() * max;
		return ret;
	}
	
	/**
	 * Randomly generate a double number whose value < max
	 * @param max
	 * @return
	 */
	protected double generateDouble(int max) {
		Random rand = new Random();
		return rand.nextDouble() * max;
	}
	
	/**
	 * Randomly generate an ASCII string with a predefined length.
	 * @param length
	 * @return
	 */
	public String generateString(int length) {
		String ret = "";
		int c = (int)((Math.random() * 9) + 48);

		for(int i = 0; i<length; i++)
		{
			c = (int)((Math.random() * 9) + 48);
			ret += Character.toString((char)c);
		}	
		return ret;
	}
	
	/**
	 * Randomly generate an integer number whose 0 < value < max
	 * @param max
	 * @return
	 */
	protected int generateIntegerNotNull(int max)
	{ 
		Random rand = new Random();
		while (true)
		{
			int ret = rand.nextInt(max+1);
			if (ret > 0)
				return ret;
		}
	}
	
	/**
	 * Randomly generate an integer between min and max
	 * @param max
	 * @return
	 */
	protected int generateIntegerWithMin(int min, int max)
	{	
		int val = max - min + 1;
		val = generateInteger(val);
		int v = val + min;
		return val + min;
	}
	
	
	/**
	 * Randomly generate an integer number whose 0 <= value < max
	 * @param max
	 * @return
	 */
	protected int generateInteger(int max)
	{ 
		Random rand = new Random();
		return rand.nextInt(max);
	}
	
	/**
	 * Randomly generate an ID for a resource in IaaS's id format
	 * @return
	 */
	protected String generateID() {
		return generateString(8) + "-" + generateString(4) + "-" + generateString(4) + "-" + generateString(8);
	}
	
	/**
	 * Randomly generate an IP address
	 * @return: IP address, such as  "192.168.216.38"
	 */
	public String generateIP() {
		return new Integer(this.generateIntegerNotNull(255)).toString() + "." + 
		new Integer(this.generateInteger(255)).toString() + "." +
		new Integer(this.generateInteger(255)).toString() + "." +
		new Integer(this.generateIntegerNotNull(255)).toString();
	}
	
	/**
	 * Randomly generate an IP address within a range of addresses
	 * @param networkaddress  : IP network address, i.e., "207.162.8.0";
	 * @param mask            : IP mask, i.e., "255.255.255.0"
	 * @return
	 * @throws UnknownHostException 
	 */
	protected String generateIP(InetAddress networkAddress, InetAddress mask) 
	{
		int addr = InetAddressUtil.InetAddressToInt(networkAddress);
		int maskInt = InetAddressUtil.InetAddressToInt(mask);
		
		int rbits = ~maskInt;

		int random = addr + (int)(rbits * Math.random()) + 1;

	    return InetAddressUtil.IpToString(random);
	}
	
}
