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
	import mx.collections.ArrayCollection;

	public class Facility
	{
		private var _resourceId:String;
		private var _resourceName:String;
		private var _resourceState:String;
		private var _domainId:String;
		private var _climateResourceId:String;
		private var _location:String;
		private var _domainGreenPercentage:Number;
		private var _opHourThreshold:Number;
		private var _opHourUnderCurrentLoad:Number;
		private var _opHourUnderMaximumLoad:Number;
		private var _totalConsummingPower:Number;
		private var _totalGeneratingPower:Number;
		private var _temperature:Number;
		private var _humidity:Number;
		private var _pduList:ArrayCollection;
		private var _alarms:String;
		private var _batteryChargePercentage:Number;
		private var _powerSourceType:String;
		private var _onGrid:String;
		
		
		public function Facility()
		{
			pduList = new ArrayCollection();
		}

		public function get domainId():String
		{
			return _domainId;
		}

		public function set domainId(value:String):void
		{
			_domainId = value;
		}

		public function get climateResourceId():String
		{
			return _climateResourceId;
		}

		public function set climateResourceId(value:String):void
		{
			_climateResourceId = value;
		}

		public function get location():String
		{
			return _location;
		}

		public function set location(value:String):void
		{
			_location = value;
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

		public function get resourceId():String
		{
			return _resourceId;
		}

		public function set resourceId(value:String):void
		{
			_resourceId = value;
		}

		public function get domainGreenPercentage():Number
		{
			return _domainGreenPercentage;
		}

		public function set domainGreenPercentage(value:Number):void
		{
			_domainGreenPercentage = value;
		}

		public function get opHourThreshold():Number
		{
			return _opHourThreshold;
		}

		public function set opHourThreshold(value:Number):void
		{
			_opHourThreshold = value;
		}

		public function get opHourUnderCurrentLoad():Number
		{
			return _opHourUnderCurrentLoad;
		}

		public function set opHourUnderCurrentLoad(value:Number):void
		{
			_opHourUnderCurrentLoad = value;
		}

		public function get opHourUnderMaximumLoad():Number
		{
			return _opHourUnderMaximumLoad;
		}

		public function set opHourUnderMaximumLoad(value:Number):void
		{
			_opHourUnderMaximumLoad = value;
		}

		public function get totalConsummingPower():Number
		{
			return _totalConsummingPower;
		}

		public function set totalConsummingPower(value:Number):void
		{
			_totalConsummingPower = value;
		}

		public function get totalGeneratingPower():Number
		{
			return _totalGeneratingPower;
		}

		public function set totalGeneratingPower(value:Number):void
		{
			_totalGeneratingPower = value;
		}

		public function get temperature():Number
		{
			return _temperature;
		}

		public function set temperature(value:Number):void
		{
			_temperature = value;
		}

		public function get humidity():Number
		{
			return _humidity;
		}

		public function set humidity(value:Number):void
		{
			_humidity = value;
		}

		public function get pduList():ArrayCollection
		{
			return _pduList;
		}

		public function set pduList(value:ArrayCollection):void
		{
			_pduList = value;
		}

		public function get alarms():String
		{
			return _alarms;
		}

		public function set alarms(value:String):void
		{
			_alarms = value;
		}

		public function get batteryChargePercentage():Number
		{
			return _batteryChargePercentage;
		}

		public function set batteryChargePercentage(value:Number):void
		{
			_batteryChargePercentage = value;
		}

		public function get powerSourceType():String
		{
			return _powerSourceType;
		}

		public function set powerSourceType(value:String):void
		{
			_powerSourceType = value;
		}

		public function get onGrid():String
		{
			return _onGrid;
		}

		public function set onGrid(value:String):void
		{
			_onGrid = value;
		}


	}
}
