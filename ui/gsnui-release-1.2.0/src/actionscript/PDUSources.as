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

	public class PDUSources
	{
		private var _pduResourceId:String;
		private var _pduSources:ArrayCollection;
		
		public function PDUSources()
		{
			pduSources = new ArrayCollection();
		}

		public function get pduResourceId():String
		{
			return _pduResourceId;
		}

		public function set pduResourceId(value:String):void
		{
			_pduResourceId = value;
		}

		public function get pduSources():ArrayCollection
		{
			return _pduSources;
		}

		public function set pduSources(value:ArrayCollection):void
		{
			_pduSources = value;
		}


	}
}
