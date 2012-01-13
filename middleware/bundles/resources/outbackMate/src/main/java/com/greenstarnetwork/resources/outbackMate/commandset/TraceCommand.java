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
package com.greenstarnetwork.resources.outbackMate.commandset;

import java.util.StringTokenizer;

import com.greenstarnetwork.models.powersource.PowerSourceModel;
import com.greenstarnetwork.protocols.datastream.message.DataStreamResponseMessage;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Send trace command to Outback Mate in order to get information about charger, battery and inverter.
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class TraceCommand extends AbstractCommandWithProtocol {

	public static final String COMMAND = "TraceCommand";	//Query command ID

	public TraceCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException 
	{
		if (!initialized) {
			initializeWithModel();
		}else
		{
			sendCommandToProtocol("");
		}
	}

	/**
	 * Get the string representing inverter operational mode
	 * @param code
	 */
	static public String decodeInverterOpMode(String code) {
		if (code.compareTo("00") == 0)
			return "Inv Off";
		else if (code.compareTo("01") == 0)
			return "Search";
		else if (code.compareTo("02") == 0)
			return "Inv On";
		else if (code.compareTo("03") == 0)
			return "Charge";
		else if (code.compareTo("04") == 0)
			return "Silent";
		else if (code.compareTo("05") == 0)
			return "Float";
		else if (code.compareTo("06") == 0)
			return "EQ";
		else if (code.compareTo("07") == 0)
			return "Charger Off";
		else if (code.compareTo("08") == 0)
			return "Support";
		else if (code.compareTo("09") == 0)
			return "Sell Enabled";
		else if (code.compareTo("10") == 0)
			return "Pass Thru";
		else if (code.compareTo("90") == 0)
			return "FX Error";
		else if (code.compareTo("91") == 0)
			return "AGS Error";
		else if (code.compareTo("92") == 0)
			return "Com Error";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing inverter AC mode
	 * @param code
	 */
	static public String decodeInverterACMode(String code) {
		if (code.compareTo("00") == 0)
			return "No AC";
		else if (code.compareTo("01") == 0)
			return "AC Drop";
		else if (code.compareTo("02") == 0)
			return "AC Use";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing inverter error
	 * @param code
	 */
	static public String decodeInverterErrormode(int code) 
	{
		String ret = null;
		
		if (code == 0)
			return null;
		
		if ((code & 1) > 0)
		{
			if (ret == null) 
				ret = "Low VAC output";
			else 
				ret = ret + ";Low VAC output";
		}
		
		if ((code & 2) > 0)
		{
			if (ret == null) 
				ret = "Stacking Error";
			else 
				ret = ret + ";Stacking Error";
		}

		if ((code & 4) > 0)
		{
			if (ret == null) 
				ret = "Over Temp";
			else 
				ret = ret + ";Over Temp";
		}
		
		if ((code & 8) > 0)
		{
			if (ret == null) 
				ret = "Low Battery";
			else 
				ret = ret + ";Low Battery";
		}


		if ((code & 16) > 0)
		{
			if (ret == null) 
				ret = "Phase Loss";
			else 
				ret = ret + ";Phase Loss";
		}

		if ((code & 32) > 0)
		{
			if (ret == null) 
				ret = "High Battery";
			else 
				ret = ret + ";High Battery";
		}

		if ((code & 64) > 0)
		{
			if (ret == null) 
				ret = "Shorted output";
			else 
				ret = ret + ";Shorted output";
		}

		if ((code & 128) > 0)
		{
			if (ret == null) 
				ret = "Back feed";
			else 
				ret = ret + ";Back feed";
		}
		
		return ret;
	}
	
	/**
	 * Get the string representing inverter warning
	 * @param code
	 */
	/**
	 * Get the string representing inverter error
	 * @param code
	 */
	static public String decodeInverterWarningmode(int code) 
	{
		String ret = null;
		
		if (code == 0)
			return null;
		
		if ((code & 1) > 0)
		{
			if (ret == null) 
				ret = "AC Input Freq High";
			else 
				ret = ret + ";AC Input Freq High";
		}
		
		if ((code & 2) > 0)
		{
			if (ret == null) 
				ret = "AC Input Freq Low";
			else 
				ret = ret + ";AC Input Freq Low";
		}

		if ((code & 4) > 0)
		{
			if (ret == null) 
				ret = "Input VAC High";
			else 
				ret = ret + ";Input VAC High";
		}
		
		if ((code & 8) > 0)
		{
			if (ret == null) 
				ret = "Input VAC Low";
			else 
				ret = ret + ";Input VAC Low";
		}
		
		if ((code & 16) > 0)
		{
			if (ret == null) 
				ret = "Buy Amps > Input size";
			else 
				ret = ret + ";Buy Amps > Input size";
		}

		if ((code & 32) > 0)
		{
			if (ret == null) 
				ret = "Temp sensor failed";
			else 
				ret = ret + ";Temp sensor failed";
		}

		if ((code & 64) > 0)
		{
			if (ret == null) 
				ret = "Comm Error";
			else 
				ret = ret + ";Comm Error";
		}

		if ((code & 128) > 0)
		{
			if (ret == null) 
				ret = "Fan Failure";
			else 
				ret = ret + ";Fan Failure";
		}
		
		return ret;
	}
	
	/**
	 * Get the string representing charger AUX mode
	 * @param code
	 */
	static public String decodeChargerAUXMode(String code) {
		if (code.compareTo("00") == 0)
			return "Disabled";
		else if (code.compareTo("01") == 0)
			return "Diversion";
		else if (code.compareTo("02") == 0)
			return "Remote";
		else if (code.compareTo("03") == 0)
			return "Manual";
		else if (code.compareTo("04") == 0)
			return "Vent Fan";
		else if (code.compareTo("05") == 0)
			return "PV Trigger";
		else if (code.compareTo("06") == 0)
			return "Float";
		else if (code.compareTo("07") == 0)
			return "ERROR Output";
		else if (code.compareTo("08") == 0)
			return "Night Light";
		else if (code.compareTo("09") == 0)
			return "PWM Diversion";
		else if (code.compareTo("10") == 0)
			return "Low Battery";
		else
			return "Unknown";
	}
	
	/**
	 * Get the string representing charger MX charger mode
	 * @param code
	 */
	static public String decodeMXChargerMode(String code) {
		if (code.compareTo("00") == 0)
			return "Silent";
		else if (code.compareTo("01") == 0)
			return "Float";
		else if (code.compareTo("02") == 0)
			return "Bulk";
		else if (code.compareTo("03") == 0)
			return "Absorb";
		else if (code.compareTo("04") == 0)
			return "EQ";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing charger error
	 * @param code
	 */
	static public String decodeChargerErrormode(int code) 
	{
		String ret = null;
		
		if (code == 0)
			return null;
		
		if ((code & 32) > 0)
		{
			if (ret == null) 
				ret = "Shorted Battery Sensor";
			else 
				ret = ret + ";Shorted Battery Sensor";
		}

		if ((code & 64) > 0)
		{
			if (ret == null) 
				ret = "Too Hot";
			else 
				ret = ret + ";Too Hot";
		}

		if ((code & 128) > 0)
		{
			if (ret == null) 
				ret = "High VOC";
			else 
				ret = ret + ";High VOC";
		}
		
		return ret;
	}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException {
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException 
	{
		DataStreamResponseMessage msg = (DataStreamResponseMessage) ((ProtocolResponseMessage) response).getProtocolMessage();
		String s = msg.getRawMessage();
		
		StringTokenizer tokenizer = new StringTokenizer(s, "\n");
		String line = tokenizer.nextToken().trim();	//first line is the command
		line = tokenizer.nextToken().trim();
		
		//Inverter parameters
		float invBuyCurrent = 0;
		float invACInputVoltage = 0;
		float invACOutputVoltage = 0;
		float invSellCurrent = 0;
		String invOpMode   = null;
		int invErrorMode = 0;
		String invACMode = null;
		float invBatteryVoltage = 0;
		int invMisc = 0;
		int invWarningMode = 0;
		
		//Chager parameters
		float chargerCurrent = 0;
		float chargerPVCurrent = 0;
		float chargerPVVoltage = 0;
		float chargerDailyKWH = 0;
		float chargerCurrentTenths = 0;
		int chargerErrorMode = 0;
		String chargerAUXMode = null;
		String chargerMXChargerMode = null;
		float chargerBatteryVoltage = 0;
		float chargerDailyAH = 0;

		while ( ((chargerMXChargerMode == null) || (invOpMode == null)) && (line != null) ) 
		{
			if( line.startsWith("1,") || line.startsWith("C,") ) 
			{
				if((invOpMode == null) && line.startsWith("1,")) 
				{//Inverter line
					invBuyCurrent = Float.valueOf(line.substring(8,10)).floatValue();
					invSellCurrent = Float.valueOf(line.substring(19,21)).floatValue();
					invACInputVoltage = Float.valueOf(line.substring(11,14)).floatValue();
					invACOutputVoltage = Float.valueOf(line.substring(15,18)).floatValue();
					invOpMode = line.substring(22,24);
					invErrorMode = Integer.valueOf(line.substring(25,28)).intValue();
					invACMode = line.substring(29,31);
					invBatteryVoltage = Float.valueOf(line.substring(32,35).trim()).floatValue()/10;

					invMisc = Integer.valueOf(line.substring(36,39)).intValue();
					//If bit 1 of Misc filed is set, then AC input and output voltages must be  
					//multiplied by 2 and all currents must be divided by 2
					if (invMisc == 1) {
						invACInputVoltage = invACInputVoltage * 2;
						invACOutputVoltage = invACOutputVoltage * 2;
						invBuyCurrent = invBuyCurrent / 2;
						invSellCurrent = invSellCurrent / 2;
					}

					invWarningMode = Integer.valueOf(line.substring(40,43)).intValue();
					
					((PowerSourceModel)model).setInvBuyCurrent(Float.toString(invBuyCurrent));
					((PowerSourceModel)model).setInvSellCurrent(Float.toString(invSellCurrent));
					((PowerSourceModel)model).setInvACInputVoltage(Float.toString(invACInputVoltage));
					((PowerSourceModel)model).setInvACOutputVoltage(Float.toString(invACOutputVoltage));
					((PowerSourceModel)model).setInvOperationalMode(decodeInverterOpMode(invOpMode));
					((PowerSourceModel)model).setInvErrorMode(decodeInverterErrormode(invErrorMode));
					((PowerSourceModel)model).setInvACMode(decodeInverterACMode(invACMode));
					((PowerSourceModel)model).setInvBatteryVoltage(Float.toString(invBatteryVoltage));
					if (invMisc > 127) //bit 8 is set
						((PowerSourceModel)model).setInvAUXOutputState("ON");
					else
						((PowerSourceModel)model).setInvAUXOutputState("OFF");
					((PowerSourceModel)model).setInvWarningMode(decodeInverterWarningmode(invWarningMode));
				}
				else if((chargerMXChargerMode == null) && line.startsWith("C,"))
				{//Charger line
					chargerCurrent = Float.valueOf(line.substring(5,7).trim()).floatValue();
					chargerPVCurrent = Float.valueOf(line.substring(8,10).trim()).floatValue();
					chargerPVVoltage = Float.valueOf(line.substring(11,14).trim()).floatValue();
					chargerDailyKWH = Float.valueOf(line.substring(15,18).trim()).floatValue();
					chargerCurrentTenths = Float.valueOf(line.substring(19,21).trim()).floatValue();
					chargerAUXMode = line.substring(22,24);
					chargerErrorMode = Integer.valueOf(line.substring(25,28)).intValue();
					chargerMXChargerMode = line.substring(29,31);
					chargerBatteryVoltage = Float.valueOf(line.substring(32,35).trim()).floatValue()/10;
					chargerDailyAH = Float.valueOf(line.substring(36,40).trim()).floatValue();
										
					((PowerSourceModel)model).setChargerCurrent(Float.toString(chargerCurrent));
					((PowerSourceModel)model).setChargerPVCurrent(Float.toString(chargerPVCurrent));
					((PowerSourceModel)model).setChargerPVVoltage(Float.toString(chargerPVVoltage));
					((PowerSourceModel)model).setChargerDailyKWH(Float.toString(chargerDailyKWH));
					((PowerSourceModel)model).setChargerCurrentTenths(Float.toString(chargerCurrentTenths));
					((PowerSourceModel)model).setChargerAUXMode(decodeChargerAUXMode(chargerAUXMode));
					((PowerSourceModel)model).setChargerErrorMode(decodeChargerErrormode(chargerErrorMode));
					((PowerSourceModel)model).setChargerMXChargerMode(decodeMXChargerMode(chargerMXChargerMode));
					((PowerSourceModel)model).setChargerBatteryVoltage(Float.toString(chargerBatteryVoltage));
					((PowerSourceModel)model).setChargerDailyAH(Float.toString(chargerDailyAH));
				}
			}
			line = tokenizer.nextToken().trim();
		}
//		System.err.println(((PowerSourceModel)model).toXML());
	}

	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException 
	{
		if (response instanceof ProtocolResponseMessage) {
			this.response = (ProtocolResponseMessage) response;
			this.requestEngineModel(false);
		}
		else if (response instanceof ProtocolErrorMessage) {
			this.errorMessage = (ProtocolErrorMessage) response;
			CommandException commandException = new CommandException("Error executing command "
					+ this.commandID, ((ProtocolErrorMessage) response)
					.getProtocolException());
			commandException.setName(this.commandID);
			commandException.setCommand(this);
			this.sendCommandErrorResponse(commandException);
		}
	}
}
