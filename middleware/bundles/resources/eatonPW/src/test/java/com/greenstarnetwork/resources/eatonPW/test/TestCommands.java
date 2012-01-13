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
package com.greenstarnetwork.resources.eatonPW.test;

import org.junit.Test;

import com.greenstarnetwork.resources.eatonPW.commandset.Pair;

public class TestCommands {

	protected Pair parseReturnValue(String s) {
		if (s == null)
			return null;
		int pi = s.indexOf('=');
		if (pi < 0)
			return null;
		Pair p = new Pair();
		p.setOid(s.substring(0, pi).trim());
		p.setValue(s.substring(pi+1).trim());
		return p;
	}

	@Test
	public void testParsePair() {
		String s = "1.3.6.1.4.1.534.6.6.6.1.2.2.1.4.10 = 325";
		Pair p = parseReturnValue(s);
		System.err.println(p.getOid());
		System.err.println(p.getValue());
	}
}
