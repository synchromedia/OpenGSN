/**
 * Copyright 2009-2011 École de technologie supérieure,
 * Communication Research Centre Canada,
 * Inocybe Technologies Inc. and 6837247 CANADA Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greenstarnetwork.models.vmm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Create a VM template from a predefined XML file
 * @author knguyen
 *
 */
public class VMTemplate {

	public static String NAME_TAG = "name";
	public static String MEMORY_TAG = "memory";
	public static String CPU_TAG = "vcpu";
	public static String DISKIMAGE_TAG = "source";
	public static String MAC_TAG = "mac";
	
	public String getTemplate(String templateName, String domName, String memory, 
			String cpu, String vmDiskImage, String macAddress) throws IOException{
		List<String> tags = new ArrayList<String>();
		tags.add(NAME_TAG);
		tags.add(MEMORY_TAG);
		tags.add(CPU_TAG);
		tags.add(DISKIMAGE_TAG);
		tags.add(MAC_TAG);

		List<String> values = new ArrayList<String>();
		values.add(domName);
		values.add(memory);
		values.add(cpu);
		String diskimage = "file='" + vmDiskImage + "'"; 
		values.add(diskimage);
		String macAddr = "address='" + macAddress + "'"; 
		values.add(macAddr);
		
		
		String ret = readTemplateFile(getClass().getClassLoader().getResourceAsStream(
				"templates/" + templateName + ".xml"), tags, values);
		
		return ret;
	}
	
	private String readTemplateFile(InputStream stream, List<String> tags, List<String> values) throws IOException{
		String answer = "";
		
		InputStreamReader streamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(streamReader);
		String s = null;
		while((s = reader.readLine()) != null){
			if (tags.size() > 0)
			{
				String tag = tags.get(0);
				String value = values.get(0);
				boolean hasCloseTag = true;
				if ( (tag.compareTo(DISKIMAGE_TAG) == 0) || (tag.compareTo(MAC_TAG) == 0) )
					hasCloseTag = false;
			    String newLine = replaceValue(s, value, tag, hasCloseTag);
			    if (newLine == null)
			    	answer += s + "\n";
			    else {
			    	answer += newLine + "\n";
			    	tags.remove(0);
			    	values.remove(0);
			    }
			}else
		    	answer += s + "\n";
		}
		reader.close();
		
		return answer;
	}
	
	private String replaceValue(String line, String value, String tag, boolean hasCloseTag) {
		String ret = null;
		String t = "<" + tag;
		if (hasCloseTag)
			t += ">";
		else
			t += " ";
		int i = line.indexOf(t); 
		if (i > -1)
		{//found tag
			i += t.length();
			ret = line.substring(0, i);
			String close = "<";
			if (hasCloseTag)
				close += "/" + tag + ">";
			else
				close = "/>";
			i = line.indexOf(close, i);
			if (i < 0)
				return null;
			ret += value + line.substring(i, line.length()); 
		}
		return ret;
	}
}
