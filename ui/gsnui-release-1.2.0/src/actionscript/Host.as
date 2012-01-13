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

	public class Host
	{
		private var _resourceId:String;
		private var _ip:String;
		private var _name:String;
		private var _state:String;
		private var _location:String;
		private var _cpu:String;
		private var _nbreCpu:String;
		private var _os:String;
		private var _speed:String;
		private var _totalMemory:String;
		private var _freeMemory:String;
		private var _cpuHardwareTime:String;
		private var _cpuIOTime:String;
		private var _cpuNiceTime:String;
		private var _cpuIdleTime:String;
		private var _cpuStealTime:String;
		private var _cpuSystemTime:String;
		private var _cpuUserTime:String;
		private var _cpuSoftwareTime:String;
		private var _percentGreen:int;
		private var _vmList:ArrayCollection = null;
		
		public function Host()
		{
		}

		public function get vmList():ArrayCollection
		{
			return _vmList;
		}

		public function set vmList(value:ArrayCollection):void
		{
			_vmList = value;
		}

		public function get ip():String
		{
			return _ip;
		}

		public function set ip(value:String):void
		{
			_ip = value;
		}

		public function get resourceId():String
		{
			return _resourceId;
		}

		public function set resourceId(value:String):void
		{
			_resourceId = value;
		}

		public function get percentGreen():int
		{
			return _percentGreen;
		}

		public function set percentGreen(value:int):void
		{
			_percentGreen = value;
		}

		public function get cpuSoftwareTime():String
		{
			return _cpuSoftwareTime;
		}

		public function set cpuSoftwareTime(value:String):void
		{
			_cpuSoftwareTime = value;
		}

		public function get cpuUserTime():String
		{
			return _cpuUserTime;
		}

		public function set cpuUserTime(value:String):void
		{
			_cpuUserTime = value;
		}

		public function get cpuSystemTime():String
		{
			return _cpuSystemTime;
		}

		public function set cpuSystemTime(value:String):void
		{
			_cpuSystemTime = value;
		}

		public function get cpuStealTime():String
		{
			return _cpuStealTime;
		}

		public function set cpuStealTime(value:String):void
		{
			_cpuStealTime = value;
		}

		public function get cpuIdleTime():String
		{
			return _cpuIdleTime;
		}

		public function set cpuIdleTime(value:String):void
		{
			_cpuIdleTime = value;
		}

		public function get cpuNiceTime():String
		{
			return _cpuNiceTime;
		}

		public function set cpuNiceTime(value:String):void
		{
			_cpuNiceTime = value;
		}

		public function get cpuIOTime():String
		{
			return _cpuIOTime;
		}

		public function set cpuIOTime(value:String):void
		{
			_cpuIOTime = value;
		}

		public function get cpuHardwareTime():String
		{
			return _cpuHardwareTime;
		}

		public function set cpuHardwareTime(value:String):void
		{
			_cpuHardwareTime = value;
		}

		public function get freeMemory():String
		{
			return _freeMemory;
		}

		public function set freeMemory(value:String):void
		{
			_freeMemory = value;
		}

		public function get totalMemory():String
		{
			return _totalMemory;
		}

		public function set totalMemory(value:String):void
		{
			_totalMemory = value;
		}

		public function get speed():String
		{
			return _speed;
		}

		public function set speed(value:String):void
		{
			_speed = value;
		}

		public function get os():String
		{
			return _os;
		}

		public function set os(value:String):void
		{
			_os = value;
		}

		public function get nbreCpu():String
		{
			return _nbreCpu;
		}

		public function set nbreCpu(value:String):void
		{
			_nbreCpu = value;
		}

		public function get cpu():String
		{
			return _cpu;
		}

		public function set cpu(value:String):void
		{
			_cpu = value;
		}

		public function get location():String
		{
			return _location;
		}

		public function set location(value:String):void
		{
			_location = value;
		}

		public function get state():String
		{
			return _state;
		}

		public function set state(value:String):void
		{
			_state = value;
		}

		public function get name():String
		{
			return _name;
		}

		public function set name(value:String):void
		{
			_name = value;
		}


	}
}
