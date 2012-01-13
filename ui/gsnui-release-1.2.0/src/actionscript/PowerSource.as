/*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Copyright 2009-2011 École de technologie supérieure, Communication Research Centre Canada, Inocybe Technologies Inc. and 6837247 CANADA Inc.
//
// 
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/

package actionscript
{
	public class PowerSource
	{
		private var _resourceId:String;
		private var _resourceName:String;
		private var _resourceState:String;
		private var _host:String;
		private var _port:String;
		private var _location:String;
		private var _chargerCurrent:Number;
		private var _chargerPVCurrent:Number;
		private var _chargerPVVoltage:Number;
		private var _chargerBatteryVoltage:Number;
		private var _invACMode:String;
		private var _invACInputVoltage:Number;
		private var _invACOutputVoltage:Number;
		private var _invBatteryVoltage:Number;
		
		public function PowerSource()
		{
		}

		public function get resourceId():String
		{
			return _resourceId;
		}

		public function set resourceId(value:String):void
		{
			_resourceId = value;
		}

		public function get resourceName():String
		{
			return _resourceName;
		}

		public function set resourceName(value:String):void
		{
			_resourceName = value;
		}

		public function get resourceState():String
		{
			return _resourceState;
		}

		public function set resourceState(value:String):void
		{
			_resourceState = value;
		}

		public function get host():String
		{
			return _host;
		}

		public function set host(value:String):void
		{
			_host = value;
		}

		public function get port():String
		{
			return _port;
		}

		public function set port(value:String):void
		{
			_port = value;
		}

		public function get location():String
		{
			return _location;
		}

		public function set location(value:String):void
		{
			_location = value;
		}

		public function get chargerCurrent():Number
		{
			return _chargerCurrent;
		}

		public function set chargerCurrent(value:Number):void
		{
			_chargerCurrent = value;
		}

		public function get chargerPVCurrent():Number
		{
			return _chargerPVCurrent;
		}

		public function set chargerPVCurrent(value:Number):void
		{
			_chargerPVCurrent = value;
		}

		public function get chargerPVVoltage():Number
		{
			return _chargerPVVoltage;
		}

		public function set chargerPVVoltage(value:Number):void
		{
			_chargerPVVoltage = value;
		}

		public function get chargerBatteryVoltage():Number
		{
			return _chargerBatteryVoltage;
		}

		public function set chargerBatteryVoltage(value:Number):void
		{
			_chargerBatteryVoltage = value;
		}

		public function get invACMode():String
		{
			return _invACMode;
		}

		public function set invACMode(value:String):void
		{
			_invACMode = value;
		}

		public function get invACInputVoltage():Number
		{
			return _invACInputVoltage;
		}

		public function set invACInputVoltage(value:Number):void
		{
			_invACInputVoltage = value;
		}

		public function get invACOutputVoltage():Number
		{
			return _invACOutputVoltage;
		}

		public function set invACOutputVoltage(value:Number):void
		{
			_invACOutputVoltage = value;
		}

		public function get invBatteryVoltage():Number
		{
			return _invBatteryVoltage;
		}

		public function set invBatteryVoltage(value:Number):void
		{
			_invBatteryVoltage = value;
		}


	}
}
