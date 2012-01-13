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
package com.greenstarnetwork.protocols.datastream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.protocols.datastream.message.DataStreamResponseMessage;

/**
 * DataStreamReader reads the output data stream
 * @author knguyen
 *
 */
public class DataStreamReader extends Thread{
	
	/** Logger */
    static private Logger logger = LoggerFactory.getLogger(DataStreamReader.class);
    
    private GSNLogger gsnlog = GSNLogger.getLogger();
    
    /** The stream to receive characters of the Data response message **/
    private BufferedReader dataStream;
    
    /** Int value for Carriage Return */
	public static final int CR = 13;

	/** Int value for Line Feed */
	public static final int LF = 10;

	/** Int value for valid character start */
	public static final int VALIDSTART = 31;

	/** Int value for valid character stop */
	public static final int VALIDSTOP = 127;
	
	/** Number of lines to read */
	private int number_of_lines = 0;
	
	private LinkedList<String> response = null;
	
    /**
     * Creates a DataStreamReader that will listen for the characters received in the stream, assemble response messages
     * and parse them
     * @param stream
     * @param oStream
     */
    public DataStreamReader(InputStream stream, OutputStream oStream, int nbrlines){
    	dataStream = new BufferedReader(new InputStreamReader(stream));
    	this.number_of_lines = nbrlines;
    }
    
    /**
     * Return N lines of response. 
     * the response
     * @param request
     * @return
     */
    public DataStreamResponseMessage getResponse(String request)
    {
    	while (getResponseLen() < this.number_of_lines)
    	{
    		try {
    			new Thread().sleep(1000);
    		}catch (Exception e) {};
    	}
    	
    	String s = "";
    	Iterator<String> it = this.response.iterator();
    	while (it.hasNext()) {
    		s += it.next() + "\n";
    	}

//		System.err.println("********* DataStreamReader getResponse: " + s);
    	DataStreamResponseMessage resp = new DataStreamResponseMessage();
    	resp.setRawMessage(s);
    	
		gsnlog.debug("********* Sending response message *******\n" + s);
		return resp;
    }

    /**
     * Gets a response message from the input stream. 
     */
	@Override
	public void run() 
	{
		
		response = new LinkedList<String>();
    	try {
			int c;
			StringBuilder buffer = new StringBuilder();
			while (((c = dataStream.read()) != -1)) 
			{
				if ((c > VALIDSTART && c < VALIDSTOP) || c == LF || c == CR) {
					//Append the character to the buffer of received characters
					if (c == LF) {
						this.response.add(buffer.toString());
						if (getResponseLen() > this.number_of_lines)
							this.response.removeFirst();
//						System.err.println("===> read: " + buffer.toString());
//						gsnlog.debug(buffer.toString());
						buffer = new StringBuilder();
					}else
						buffer.append((char) c);
				}
			}
			
		}catch (IOException e) {
			logger.debug("Connection Lost!" + e.getMessage());
			gsnlog.debug("===========>Connection Lost!" + e.getMessage());
		}
	}
	
	private synchronized int getResponseLen() {
		if (this.response != null)
			return this.response.size();
		else
			return 0;
	}
}