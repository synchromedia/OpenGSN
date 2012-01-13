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
	public class VM
	{
		private var _name:String;
		private var _displayedName:String;
		private var _storage:String;
		private var _network:String;
		private var _status:String;
		private var _memory:String;
		private var _vcpu:String;
		private var _os:String;
		private var _location:String;
		private var _resourceId:String;
		private var _hostName:String;
		private var _hostIp:String;
		private var _vmIp:String;
		private var _id:String;
		private var _uuid:String;
		private var _currentMemory:String;
		private var _graphics:String;
		private var _clock:String;
		private var _emulator:String;
		private var _greenPercent:int;
		
		//Used for MyVM Table progress bar : To show or hide the component
		private var _progressbarAlpha:int;
		
		
		public function VM()
		{
			_memory="----";
		}

		public function get hostIp():String
		{
			return _hostIp;
		}

		public function set hostIp(value:String):void
		{
			_hostIp = value;
		}

		public function get resourceId():String
		{
			return _resourceId;
		}

		public function set resourceId(value:String):void
		{
			_resourceId = value;
		}

		public function get greenPercent():int
		{
			return _greenPercent;
		}

		public function set greenPercent(value:int):void
		{
			_greenPercent = value;
		}

		public function get vmIp():String
		{
			return _vmIp;
		}

		public function set vmIp(value:String):void
		{
			_vmIp = value;
		}

		public function get displayedName():String
		{
			return _displayedName;
		}

		public function set displayedName(value:String):void
		{
			_displayedName = value;
		}

		public function get progressbarAlpha():int
		{
			return _progressbarAlpha;
		}

		public function set progressbarAlpha(value:int):void
		{
			_progressbarAlpha = value;
		}

		public function get emulator():String
		{
			return _emulator;
		}

		public function set emulator(value:String):void
		{
			_emulator = value;
		}

		public function get clock():String
		{
			return _clock;
		}

		public function set clock(value:String):void
		{
			_clock = value;
		}

		public function get graphics():String
		{
			return _graphics;
		}

		public function set graphics(value:String):void
		{
			_graphics = value;
		}

		public function get currentMemory():String
		{
			return _currentMemory;
		}

		public function set currentMemory(value:String):void
		{
			_currentMemory = value;
		}

		public function get uuid():String
		{
			return _uuid;
		}

		public function set uuid(value:String):void
		{
			_uuid = value;
		}

		public function get id():String
		{
			return _id;
		}

		public function set id(value:String):void
		{
			_id = value;
		}

		public function get location():String
		{
			return _location;
		}

		public function set location(value:String):void
		{
			_location = value;
		}

		public function get os():String
		{
			return _os;
		}

		public function set os(value:String):void
		{
			_os = value;
		}

		public function get vcpu():String
		{
			return _vcpu;
		}

		public function set vcpu(value:String):void
		{
			_vcpu = value;
		}

		public function get memory():String
		{
			return _memory;
		}

		public function set memory(value:String):void
		{
			_memory = value;
		}

		public function get status():String
		{
			return _status;
		}

		public function set status(value:String):void
		{
			_status = value;
		}

		public function get network():String
		{
			return _network;
		}

		public function set network(value:String):void
		{
			_network = value;
		}

		public function get storage():String
		{
			return _storage;
		}

		public function set storage(value:String):void
		{
			_storage = value;
		}

		public function get name():String
		{
			return _name;
		}

		public function set name(value:String):void
		{
			_name = value;
		}

		public function get hostName():String
		{
			return _hostName;
		}

		public function set hostName(value:String):void
		{
			_hostName = value;
		}


	}
}
