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
	import com.google.maps.services.ClientGeocoder;
	import com.google.maps.services.GeocodingEvent;
	
	import flash.events.EventDispatcher;
	
	
	[Event(name="ON_NEW_GEOCODE_POINT", type="actionscript.CustomGeocoderEvent")]
	
	public class CustomGeocoder extends EventDispatcher
	{
		private var coder:ClientGeocoder;
		private var callerType:String;
		private var powerType:String;
		private var hasVMStarted:Boolean;
		
		public function CustomGeocoder(type:String, pwrType:String, areVMsStarted:Boolean)
		{
			this.callerType = type;
			this.powerType = pwrType;
			this.hasVMStarted = areVMsStarted;
			coder = new ClientGeocoder();
			coder.addEventListener( GeocodingEvent.GEOCODING_SUCCESS, onGeocodeSuccess);
			coder.addEventListener( GeocodingEvent.GEOCODING_FAILURE, onGeocodeFault );
		}
		
		public function geocode(address:String):void{
			coder.geocode(address);
		}
		
		private function onGeocodeSuccess( event:GeocodingEvent ) : void
		{
			var placemarks:Array = event.response.placemarks;
			if (placemarks.length > 0){
				var geoEvent:CustomGeocoderEvent = new CustomGeocoderEvent("ON_NEW_GEOCODE_POINT");
				geoEvent.point = placemarks[0].point;
				geoEvent.typePoint = this.callerType;
				geoEvent.powerType = this.powerType;
				geoEvent.hasVMStarted = this.hasVMStarted;
				this.dispatchEvent(geoEvent);
			}
		}
		
		private function onGeocodeFault( event:GeocodingEvent ) : void
		{
			trace(event);
			trace(event.status);
		}


	}
}
