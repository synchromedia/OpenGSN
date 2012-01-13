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

package actionscript.errors
{
	public class CodeError extends Error 
	{
		internal static const WARNING:int = 0;
		internal static const FATAL:int = 1;
		public var id:int;
		public var severity:int;
		private static var messages:XML;
		
		
		public function CodeError()
		{
			messages = <errors>
								<error code="1000"><![CDATA[the NAME of Virtual machine is alrady used.]]></error>
								<error code="1001"><![CDATA[Invalid Memory Value choose value from 128000k to 2048000k.]]></error>
								<error code="1002"><![CDATA[Invalid CPU number.]]></error>
                                <error code="1003"><![CDATA[VM already started.]]></error>
                                <error code="1004"><![CDATA[VM already stopped.]]></error>
                                <error code="1111"><![CDATA[The Operation has been dropped.]]></error>
							</errors>;
			
		}
	
		public function getMessageText(id:int):String {
			var message:XMLList = messages.error.(@code == id);
			return message[0].text();
		}
		
		public function getTitle():String {
			return "Error #" + id;
		}
		
		public function toString():String {
			return "[APPLICATION ERROR #" + id + "] " + message;
		}
	
	}
}
