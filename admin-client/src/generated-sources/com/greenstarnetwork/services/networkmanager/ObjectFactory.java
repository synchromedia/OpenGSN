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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.greenstarnetwork.services.networkmanager package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RetrieveVMAddress_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "retrieveVMAddress");
    private final static QName _ExecuteCommandResponse_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "executeCommandResponse");
    private final static QName _ReleaseAssignment_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "releaseAssignment");
    private final static QName _AssignVMAddressResponse_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "assignVMAddressResponse");
    private final static QName _AssignVMAddress_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "assignVMAddress");
    private final static QName _RetrieveVMAddressResponse_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "retrieveVMAddressResponse");
    private final static QName _NetworkManagerException_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "NetworkManagerException");
    private final static QName _ExecuteCommand_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "executeCommand");
    private final static QName _ReleaseAssignmentResponse_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "releaseAssignmentResponse");
    private final static QName _VmNetAddress_QNAME = new QName("http://networkmanager.services.greenstarnetwork.com/", "vmNetAddress");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.greenstarnetwork.services.networkmanager
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NetworkManagerRequest.Arguments }
     * 
     */
    public NetworkManagerRequest.Arguments createNetworkManagerRequestArguments() {
        return new NetworkManagerRequest.Arguments();
    }

    /**
     * Create an instance of {@link RetrieveVMAddress }
     * 
     */
    public RetrieveVMAddress createRetrieveVMAddress() {
        return new RetrieveVMAddress();
    }

    /**
     * Create an instance of {@link ExecuteCommand }
     * 
     */
    public ExecuteCommand createExecuteCommand() {
        return new ExecuteCommand();
    }

    /**
     * Create an instance of {@link NetworkManagerRequest }
     * 
     */
    public NetworkManagerRequest createNetworkManagerRequest() {
        return new NetworkManagerRequest();
    }

    /**
     * Create an instance of {@link NetworkManagerException }
     * 
     */
    public NetworkManagerException createNetworkManagerException() {
        return new NetworkManagerException();
    }

    /**
     * Create an instance of {@link ExecuteCommandResponse }
     * 
     */
    public ExecuteCommandResponse createExecuteCommandResponse() {
        return new ExecuteCommandResponse();
    }

    /**
     * Create an instance of {@link ReleaseAssignmentResponse }
     * 
     */
    public ReleaseAssignmentResponse createReleaseAssignmentResponse() {
        return new ReleaseAssignmentResponse();
    }

    /**
     * Create an instance of {@link AssignVMAddressResponse }
     * 
     */
    public AssignVMAddressResponse createAssignVMAddressResponse() {
        return new AssignVMAddressResponse();
    }

    /**
     * Create an instance of {@link NetworkManagerResponse }
     * 
     */
    public NetworkManagerResponse createNetworkManagerResponse() {
        return new NetworkManagerResponse();
    }

    /**
     * Create an instance of {@link RetrieveVMAddressResponse }
     * 
     */
    public RetrieveVMAddressResponse createRetrieveVMAddressResponse() {
        return new RetrieveVMAddressResponse();
    }

    /**
     * Create an instance of {@link ReleaseAssignment }
     * 
     */
    public ReleaseAssignment createReleaseAssignment() {
        return new ReleaseAssignment();
    }

    /**
     * Create an instance of {@link VmNetAddress }
     * 
     */
    public VmNetAddress createVmNetAddress() {
        return new VmNetAddress();
    }

    /**
     * Create an instance of {@link NetworkManagerRequest.Arguments.Entry }
     * 
     */
    public NetworkManagerRequest.Arguments.Entry createNetworkManagerRequestArgumentsEntry() {
        return new NetworkManagerRequest.Arguments.Entry();
    }

    /**
     * Create an instance of {@link AssignVMAddress }
     * 
     */
    public AssignVMAddress createAssignVMAddress() {
        return new AssignVMAddress();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveVMAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "retrieveVMAddress")
    public JAXBElement<RetrieveVMAddress> createRetrieveVMAddress(RetrieveVMAddress value) {
        return new JAXBElement<RetrieveVMAddress>(_RetrieveVMAddress_QNAME, RetrieveVMAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteCommandResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "executeCommandResponse")
    public JAXBElement<ExecuteCommandResponse> createExecuteCommandResponse(ExecuteCommandResponse value) {
        return new JAXBElement<ExecuteCommandResponse>(_ExecuteCommandResponse_QNAME, ExecuteCommandResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseAssignment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "releaseAssignment")
    public JAXBElement<ReleaseAssignment> createReleaseAssignment(ReleaseAssignment value) {
        return new JAXBElement<ReleaseAssignment>(_ReleaseAssignment_QNAME, ReleaseAssignment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssignVMAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "assignVMAddressResponse")
    public JAXBElement<AssignVMAddressResponse> createAssignVMAddressResponse(AssignVMAddressResponse value) {
        return new JAXBElement<AssignVMAddressResponse>(_AssignVMAddressResponse_QNAME, AssignVMAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssignVMAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "assignVMAddress")
    public JAXBElement<AssignVMAddress> createAssignVMAddress(AssignVMAddress value) {
        return new JAXBElement<AssignVMAddress>(_AssignVMAddress_QNAME, AssignVMAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveVMAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "retrieveVMAddressResponse")
    public JAXBElement<RetrieveVMAddressResponse> createRetrieveVMAddressResponse(RetrieveVMAddressResponse value) {
        return new JAXBElement<RetrieveVMAddressResponse>(_RetrieveVMAddressResponse_QNAME, RetrieveVMAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NetworkManagerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "NetworkManagerException")
    public JAXBElement<NetworkManagerException> createNetworkManagerException(NetworkManagerException value) {
        return new JAXBElement<NetworkManagerException>(_NetworkManagerException_QNAME, NetworkManagerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteCommand }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "executeCommand")
    public JAXBElement<ExecuteCommand> createExecuteCommand(ExecuteCommand value) {
        return new JAXBElement<ExecuteCommand>(_ExecuteCommand_QNAME, ExecuteCommand.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseAssignmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "releaseAssignmentResponse")
    public JAXBElement<ReleaseAssignmentResponse> createReleaseAssignmentResponse(ReleaseAssignmentResponse value) {
        return new JAXBElement<ReleaseAssignmentResponse>(_ReleaseAssignmentResponse_QNAME, ReleaseAssignmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VmNetAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://networkmanager.services.greenstarnetwork.com/", name = "vmNetAddress")
    public JAXBElement<VmNetAddress> createVmNetAddress(VmNetAddress value) {
        return new JAXBElement<VmNetAddress>(_VmNetAddress_QNAME, VmNetAddress.class, null, value);
    }

}
