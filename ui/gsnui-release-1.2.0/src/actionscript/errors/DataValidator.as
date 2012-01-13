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
	import actionscript.VM;
	
	import mx.collections.ArrayCollection;
	
	public class DataValidator
	{
		
		public static function validateVMname(vmDisplayName:String,vmlist:ArrayCollection):void 
		{
			for(var i:int = 0; i < vmlist.length; i++){
				if (vmlist.getItemAt(i).displayedName == vmDisplayName  )
				{
					throw new FatalError(1000);
				}
			}
		}
		
		public static function validateVMparam(vm:VM,vmlist:ArrayCollection):void 
		{
			// checks for the VM parameters 
			for(var i:int = 0; i < vmlist.length; i++){
				 if (vmlist.getItemAt(i).displayedName == vm.displayedName  )
				 {
					 throw new FatalError(1000);
				 }
			}
		}
		
		public static function validateVMstart(status:String):void 
		{
			if (status== "STARTED" ){
				throw new FatalError(1003);
			}
		}
		
		public static function validateVMstop(status:String):void 
		{
			if (status== "STOPPED" ){
				throw new FatalError(1004);
			}
		}
		
	}
}
