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
	import com.google.maps.LatLng;
	
	import flash.events.Event;

	public class CustomGeocoderEvent extends Event
	{
		
		private var _point:LatLng;
		private var _typePoint:String;
		private var typeEvent:String;
		private var _powerType:String;
		private var _hasVMStarted:Boolean;
		
		public function CustomGeocoderEvent(type:String)
		{
			super(type);
			typeEvent = type;
		}

		public function get point():LatLng
		{
			return _point;
		}

		public function set point(value:LatLng):void
		{
			_point = value;
		}

		public function get typePoint():String
		{
			return _typePoint;
		}

		public function set typePoint(value:String):void
		{
			_typePoint = value;
		}

		public function get powerType():String
		{
			return _powerType;
		}

		public function set powerType(value:String):void
		{
			_powerType = value;
		}

		public function get hasVMStarted():Boolean
		{
			return _hasVMStarted;
		}

		public function set hasVMStarted(value:Boolean):void
		{
			_hasVMStarted = value;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			var geoEvent:CustomGeocoderEvent = new CustomGeocoderEvent(typeEvent);
			geoEvent.point = this.point;
			geoEvent.typePoint = this.typePoint;
			geoEvent.powerType = this.powerType;
			geoEvent.hasVMStarted = this.hasVMStarted;
			return geoEvent;
		}

	}
}
