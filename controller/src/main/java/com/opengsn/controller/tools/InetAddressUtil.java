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
package com.opengsn.controller.tools;

import java.net.InetAddress;

/**
 * Helper class for IP address manipulation
 * @author knguyen
 *
 */
public class InetAddressUtil {

    /**
     * Checks if a subnet contains a specific IP address.
     * @param network The network address.
     * @param netmask The subnet mask.
     * @param ip The IP address to check.
     * @return A boolean value.
     */
    public static boolean contains(InetAddress network, InetAddress netmask, InetAddress ip) {
        
        byte[] networkBytes = network.getAddress();
        byte[] netmaskBytes = netmask.getAddress();
        byte[] ipBytes = ip.getAddress();
        
        /* check IPv4/v6-compatibility or parameters: */
        if(networkBytes.length != netmaskBytes.length
            || netmaskBytes.length != ipBytes.length)
        {
            return false;
        }
        
        /* Check if the masked network and ip addresses match: */
        for(int i=0; i<netmaskBytes.length; i++) {
            int mask = netmaskBytes[i] & 0xff;
            if((networkBytes[i] & mask) != (ipBytes[i] & mask)) {
                return false;
            }
        }
        return true;
    }

	/** Convert ip to an integer
	 * @param ip InetAddress to convert
	 * @return Integer
	 */
	public static int InetAddressToInt (InetAddress ip)
	{
		if (ip==null)
			return -1;
		byte[] adr = ip.getAddress();
		
		int[] i = new int[4];
		for (int j=0;j<4;j++)
		{
			i[j] = (int) ((adr[j]<0) ? (256+adr[j]) : adr[j]);
		}
		return i[3]+(i[2]<<8)+(i[1]<<16)+(i[0]<<24);
	}
	
	/**
     * Convert an IP address stored in an int to its string representation.
	 * @param address
	 * @return
	 */
	public static String IpToString(int address) 
	{
	    StringBuffer sa = new StringBuffer();
	    for(int i=0; i<4; i++) {
	      sa.append(0xff & address >> 24);
	      address <<= 8;
	      if(i != 4 - 1)
	        sa.append('.');
	    }
	    return sa.toString();
	}

	/**
	 * Compute network address from IP and mask
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static String computeNetworkAddress(InetAddress ip, InetAddress mask) {
		int addr = InetAddressToInt(ip);
		int maskInt = InetAddressToInt(mask);
		return IpToString(addr & maskInt);
	}
}
