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
package com.greenstarnetwork.models.pdu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * 
 * @author knguyen
 *
 */
public class XMLSerializer 
{
	/** Int value for valid character start */
	public static final int VALIDSTART = 31;

	/** Int value for valid character stop */
	public static final int VALIDSTOP = 127;
	
	public static final String XML_START = "<pduModel>";
	public static final String XML_END = "</pduModel>";
	
	
	public void exportModel(PDUModel model, OutputStream os) throws PDUException{
		try{
		JAXBContext context = JAXBContext.newInstance(PDUModel.class);
		context.createMarshaller().marshal(model,os);
		}
		catch(JAXBException e){
			e.printStackTrace();
			throw new PDUException(e.toString());
		}
	}

	public PDUModel importModel(InputStream is) throws PDUException{
		try{
			JAXBContext context = JAXBContext.newInstance(PDUModel.class);
			return (PDUModel) context.createUnmarshaller().unmarshal(is);
		}
		catch(JAXBException e){
			e.printStackTrace();
			throw new PDUException(e.toString());
		}
	}
	
	/**
	 * Create a SOAP message encapsulating PDUModel
	 * @param model
	 * @param soapHead
	 * @param soapTail
	 * @return
	 * @throws PDUException
	 */
	public String toSoapMessage(PDUModel model, String soapHead, String soapTail) throws PDUException{
		try {
			PipedOutputStream pout = new PipedOutputStream();
			PipedInputStream pin = new PipedInputStream(pout);
			exportModel(model, pout);
			StringBuilder buffer = new StringBuilder();
	        int c, pi, found = 0;
			while ((c = pin.read()) != -1 )
			{
				if (c > VALIDSTART && c < VALIDSTOP)
				{
					//Append the character to the buffer of received characters
					buffer.append((char) c);
					if ( (pi = buffer.indexOf(XML_START)) > -1 ) 
					{
						buffer.delete(0, pi + XML_START.length());
						found = 1;
					}
					if ( (found == 1) && ((pi = buffer.indexOf(XML_END)) > -1) )
					{
						buffer.delete(pi,buffer.length());
						found = 2;
						break;
					}
				}else
					break;
			}
			
			pin.close();
			pout.close();
			
			if (found == 2)
				return soapHead + buffer.toString() + soapTail;
			else
				return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new PDUException(e.toString());
		}
	}
}
