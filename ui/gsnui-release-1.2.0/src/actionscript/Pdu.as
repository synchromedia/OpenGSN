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

	public class Pdu
	{
		
		private var _ip:String;
		private var _outlets:ArrayCollection;
		private var _location:String;
		private var _resourceId:String;
		private var _name:String;
		private var _state:String;
		
		public function Pdu()
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
		
		public function get name():String
		{
			return _name;
		}
		
		public function set name(value:String):void
		{
			_name = value;
		}
		
		public function get state():String
		{
			return _state;
		}
		
		public function set state(value:String):void
		{
			_state = value;
		}

		public function get location():String
		{
			return _location;
		}

		public function set location(value:String):void
		{
			_location = value;
		}

		public function get outlets():ArrayCollection
		{
			return _outlets;
		}

		public function set outlets(value:ArrayCollection):void
		{
			_outlets = value;
		}

		public function get ip():String
		{
			return _ip;
		}

		public function set ip(value:String):void
		{
			_ip = value;
		}

	}
}
