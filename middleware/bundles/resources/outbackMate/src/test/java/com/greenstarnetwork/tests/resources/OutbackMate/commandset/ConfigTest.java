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
package com.greenstarnetwork.tests.resources.OutbackMate.commandset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenstarnetwork.models.powersource.PowerSourceException;
import com.greenstarnetwork.models.powersource.PowerSourceModel;
import com.greenstarnetwork.protocols.datastream.message.DataStreamResponseMessage;
import com.greenstarnetwork.resources.outbackMate.commandset.TraceCommand;
import com.greenstarnetwork.resources.outbackMate.core.Helper;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;

/**
 
 * @author Fereydoun
 */
public class ConfigTest {



	@Test
	public void testConfig() throws Exception {
		Helper helper = new Helper();
		//helper.init();
		System.out.println(helper.readGMT());
		System.out.println(helper.readPDUAlias());
		int[][] a = new int[7][1440];
		a=helper.readCurrentDataForAWeek();
		
		
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date 
		System.out.println(calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE));
		System.out.println((calendar.getTimeZone().getDSTSavings()+calendar.getTimeZone().getRawOffset())/1000/3600);
	}
	
}
