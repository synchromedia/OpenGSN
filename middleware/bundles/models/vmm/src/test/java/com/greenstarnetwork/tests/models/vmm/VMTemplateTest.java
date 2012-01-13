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
package com.greenstarnetwork.tests.models.vmm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.greenstarnetwork.models.vmm.VMTemplate;

public class VMTemplateTest {

	@Test
	public void testGetTemplate() {
		VMTemplate temp = new VMTemplate();
		String timestamp = Long.toHexString(new Long(new Date().getTime()).longValue());
		int l = timestamp.length();
		l -= 6;
		String macAddress = "52:54:00:" + timestamp.charAt(l) + timestamp.charAt(l+1) + ":" +
				timestamp.charAt(l+2) + timestamp.charAt(l+3) + ":" + timestamp.charAt(l+4) + timestamp.charAt(l+5);

		try {
			String s = temp.getTemplate("server91064", "VMTest", "25000", "4", 
					"/home/storage/Tamplate_desktop91032-2.qcow2", macAddress);
			System.err.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
