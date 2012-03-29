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
package com.greenstarnetwork.services.networkmanager;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.0
 * 2011-12-19T09:20:02.835-05:00
 * Generated source version: 2.4.0
 * 
 */
 
@WebService(targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", name = "INetworkManagerPortType")
@XmlSeeAlso({ObjectFactory.class})
public interface INetworkManagerPortType {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "executeCommand", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.ExecuteCommand")
    @WebMethod
    @ResponseWrapper(localName = "executeCommandResponse", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.ExecuteCommandResponse")
    public com.greenstarnetwork.services.networkmanager.NetworkManagerResponse executeCommand(
        @WebParam(name = "arg0", targetNamespace = "")
        com.greenstarnetwork.services.networkmanager.NetworkManagerRequest arg0
    );

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "retrieveVMAddress", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.RetrieveVMAddress")
    @WebMethod
    @ResponseWrapper(localName = "retrieveVMAddressResponse", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.RetrieveVMAddressResponse")
    public com.greenstarnetwork.services.networkmanager.VmNetAddress retrieveVMAddress(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        java.lang.String arg1
    ) throws NetworkManagerException_Exception;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getJMSId", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.GetJMSId")
    @WebMethod
    @ResponseWrapper(localName = "getJMSIdResponse", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.GetJMSIdResponse")
    public java.lang.String getJMSId();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "releaseAssignment", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.ReleaseAssignment")
    @WebMethod
    @ResponseWrapper(localName = "releaseAssignmentResponse", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.ReleaseAssignmentResponse")
    public boolean releaseAssignment(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    ) throws NetworkManagerException_Exception;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "assignVMAddress", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.AssignVMAddress")
    @WebMethod
    @ResponseWrapper(localName = "assignVMAddressResponse", targetNamespace = "http://networkmanager.services.greenstarnetwork.com/", className = "com.greenstarnetwork.services.networkmanager.AssignVMAddressResponse")
    public com.greenstarnetwork.services.networkmanager.VmNetAddress assignVMAddress(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    ) throws NetworkManagerException_Exception;
}