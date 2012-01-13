/*
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
/**
 * Connect to the Outback Mate and grab the latest state of the Solar Power System.
 * The response from the device will look like:
 *  1,00,00,01,117,117,00,07,000,02,252,008,129,046
 *  C,00,05,01,092,005,09,03,000,02,249,0022,00,074
 */

var model = engine.model;

//TODO - what will this look like? There is no command. just connect,
//       read and disconnect
var command = "";
var remoteShellMessage = {
	remoteShellRequest : {command:command}
};

//send the command to the device
var resp = engine.sendMessageBody(remoteShellMessage);
parseResponse(resp);

function parseResponse(response) {

	var lines = response.split("\n");
	for(var i = 0; i < lines.length; i++) {
		line = lines[i].trim();

		if(line.indexOf("1,") == 0)//Starts with
		{
			//Inverter line
			var invBuyCurrent = new Number(line.substring(8, 10));
			var invSellCurrent = new Number(line.substring(19, 21));
			var invACInputVoltage = new Number(line.substring(11, 14));
			var invACOutputVoltage = new Number(line.substring(15, 18));
			var invOpMode = line.substring(22, 24);
			var invErrorMode = new Number(line.substring(25, 28));
			var invACMode = line.substring(29, 31);
			var invBatteryVoltage = new Number(line.substring(32, 35)) / 10;
			var invMisc = new Number(line.substring(36, 39));
			//If bit 1 of invMisc filed is set, then AC input and output voltages must be
			//multiplied by 2 and all currents must be divided by 2
			if(invMisc == 1) {
				invACInputVoltage = invACInputVoltage * 2;
				invACOutputVoltage = invACOutputVoltage * 2;
				invBuyCurrent = invBuyCurrent / 2;
				invSellCurrent = invSellCurrent / 2;
			}
			invWarningMode = new Number(line.substring(40, 43));

			model.invBuyCurrent = invBuyCurrent;
			model.invSellCurrent = invSellCurrent;
			model.invACInputVoltage = invACInputVoltage;
			model.invACOutputVoltage = invACOutputVoltage;
			model.invOperationalMode = decodeInverterOpMode(invOpMode);
			model.invErrorMode = decodeInverterErrormode(invErrorMode);
			model.invACMode = decodeInverterACMode(invACMode);
			model.invBatteryVoltage = invBatteryVoltage;

			if(invMisc > 127)//bit 8 is set
				model.invAUXOutputState = "ON";
			else
				model.invAUXOutputState = "OFF";

			model.invWarningMode = decodeInverterWarningmode(invWarningMode);
		} else if(line.indexOf("C,") == 0) {
			//Charger line
			var chargerCurrent = new Number(line.substring(5, 7));
			var chargerPVCurrent = new Number(line.substring(8, 10));
			var chargerPVVoltage = new Number(line.substring(11, 14));
			var chargerDailyKWH = new Number(line.substring(15, 18));
			var chargerCurrentTenths = new Number(line.substring(19, 21));
			var chargerAUXMode = line.substring(22, 24);
			var chargerErrorMode = new Number(line.substring(25, 28));
			var chargerMXChargerMode = line.substring(29, 31);
			var chargerBatteryVoltage = new Number(line.substring(32, 35)) / 10;
			var chargerDailyAH = new Number(line.substring(36, 40));

			model.chargerCurrent = chargerCurrent;
			model.chargerPVCurrent = chargerPVCurrent;
			model.chargerPVVoltage = chargerPVVoltage;
			model.chargerDailyKWH = chargerDailyKWH;
			model.chargerCurrentTenths = chargerCurrentTenths;
			model.chargerAUXMode = decodeChargerAUXMode(chargerAUXMode);
			model.chargerErrorMode = decodeChargerErrormode(chargerErrorMode);
			model.chargerMXChargerMode = decodeMXChargerMode(chargerMXChargerMode);
			model.chargerBatteryVoltage = chargerBatteryVoltage;
			model.chargerDailyAH = chargerDailyAH;
		}
	}

	/**
	 * Get the string representing inverter operational mode
	 * @param code
	 */
	function decodeInverterOpMode(code) {
		if(code == "00")
			return "Inv Off";
		else if(code == "01")
			return "Search";
		else if(code == "02")
			return "Inv On";
		else if(code == "03")
			return "Charge";
		else if(code == "04")
			return "Silent";
		else if(code == "05")
			return "Float";
		else if(code == "06")
			return "EQ";
		else if(code == "07")
			return "Charger Off";
		else if(code == "08")
			return "Support";
		else if(code == "09")
			return "Sell Enabled";
		else if(code == "10")
			return "Pass Thru";
		else if(code == "90")
			return "FX Error";
		else if(code == "91")
			return "AGS Error";
		else if(code == "92")
			return "Com Error";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing inverter AC mode
	 * @param code
	 */
	function decodeInverterACMode(code) {
		if(code == "00")
			return "No AC";
		else if(code == "01")
			return "AC Drop";
		else if(code == "02")
			return "AC Use";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing inverter error
	 * @param code
	 */
	function decodeInverterErrormode(code) {
		var ret;

		if(code == 0)
			return null;
		if((code & 1) > 0) {
			if(ret == null)
				ret = "Low VAC output";
			else
				ret = ret + ";Low VAC output";
		}
		if((code & 2) > 0) {
			if(ret == null)
				ret = "Stacking Error";
			else
				ret = ret + ";Stacking Error";
		}
		if((code & 4) > 0) {
			if(ret == null)
				ret = "Over Temp";
			else
				ret = ret + ";Over Temp";
		}
		if((code & 8) > 0) {
			if(ret == null)
				ret = "Low Battery";
			else
				ret = ret + ";Low Battery";
		}
		if((code & 16) > 0) {
			if(ret == null)
				ret = "Phase Loss";
			else
				ret = ret + ";Phase Loss";
		}
		if((code & 32) > 0) {
			if(ret == null)
				ret = "High Battery";
			else
				ret = ret + ";High Battery";
		}
		if((code & 64) > 0) {
			if(ret == null)
				ret = "Shorted output";
			else
				ret = ret + ";Shorted output";
		}
		if((code & 128) > 0) {
			if(ret == null)
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
	function decodeInverterWarningmode(code) {
		var ret;

		if(code == 0)
			return null;
		if((code & 1) > 0) {
			if(ret == null)
				ret = "AC Input Freq High";
			else
				ret = ret + ";AC Input Freq High";
		}
		if((code & 2) > 0) {
			if(ret == null)
				ret = "AC Input Freq Low";
			else
				ret = ret + ";AC Input Freq Low";
		}
		if((code & 4) > 0) {
			if(ret == null)
				ret = "Input VAC High";
			else
				ret = ret + ";Input VAC High";
		}
		if((code & 8) > 0) {
			if(ret == null)
				ret = "Input VAC Low";
			else
				ret = ret + ";Input VAC Low";
		}
		if((code & 16) > 0) {
			if(ret == null)
				ret = "Buy Amps > Input size";
			else
				ret = ret + ";Buy Amps > Input size";
		}
		if((code & 32) > 0) {
			if(ret == null)
				ret = "Temp sensor failed";
			else
				ret = ret + ";Temp sensor failed";
		}
		if((code & 64) > 0) {
			if(ret == null)
				ret = "Comm Error";
			else
				ret = ret + ";Comm Error";
		}
		if((code & 128) > 0) {
			if(ret == null)
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
	function decodeChargerAUXMode(code) {
		if(code == "00")
			return "Disabled";
		else if(code == "01")
			return "Diversion";
		else if(code == "02")
			return "Remote";
		else if(code == "03")
			return "Manual";
		else if(code == "04")
			return "Vent Fan";
		else if(code == "05")
			return "PV Trigger";
		else if(code == "06")
			return "Float";
		else if(code == "07")
			return "ERROR Output";
		else if(code == "08")
			return "Night Light";
		else if(code == "09")
			return "PWM Diversion";
		else if(code == "10")
			return "Low Battery";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing charger MX charger mode
	 * @param code
	 */
	function decodeMXChargerMode(code) {
		if(code == "00")
			return "Silent";
		else if(code == "01")
			return "Float";
		else if(code == "02")
			return "Bulk";
		else if(code == "03")
			return "Absorb";
		else if(code == "04")
			return "EQ";
		else
			return "Unknown";
	}

	/**
	 * Get the string representing charger error
	 * @param code
	 */
	function decodeChargerErrormode(code) {
		var ret;

		if(code == 0)
			return null;

		if((code & 32) > 0) {
			if(ret == null)
				ret = "Shorted Battery Sensor";
			else
				ret = ret + ";Shorted Battery Sensor";
		}

		if((code & 64) > 0) {
			if(ret == null)
				ret = "Too Hot";
			else
				ret = ret + ";Too Hot";
		}

		if((code & 128) > 0) {
			if(ret == null)
				ret = "High VOC";
			else
				ret = ret + ";High VOC";
		}

		return ret;
	}

}