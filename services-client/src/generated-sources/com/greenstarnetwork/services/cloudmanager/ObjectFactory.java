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
package com.greenstarnetwork.services.cloudmanager;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.greenstarnetwork.services.cloudmanager package. 
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

    private final static QName _InitCloudManagerResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "initCloudManagerResponse");
    private final static QName _MigrateInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "migrateInstance");
    private final static QName _StartInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "startInstanceResponse");
    private final static QName _HostQuery_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "hostQuery");
    private final static QName _CreateInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "createInstanceResponse");
    private final static QName _HostQueryResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "hostQueryResponse");
    private final static QName _StopInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "stopInstance");
    private final static QName _StopInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "stopInstanceResponse");
    private final static QName _DestroyInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "destroyInstanceResponse");
    private final static QName _CreateInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "createInstance");
    private final static QName _StartInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "startInstance");
    private final static QName _ShutdownInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "shutdownInstance");
    private final static QName _CreateInstanceInHostResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "createInstanceInHostResponse");
    private final static QName _DescribeHostsResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "describeHostsResponse");
    private final static QName _InitDomains_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "initDomains");
    private final static QName _AddDomain_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "addDomain");
    private final static QName _AddDomainResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "addDomainResponse");
    private final static QName _DescribeHostResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "describeHostResponse");
    private final static QName _RemoveDomainResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "removeDomainResponse");
    private final static QName _RebootInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "rebootInstance");
    private final static QName _RemoveDomain_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "removeDomain");
    private final static QName _DescribeHost_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "describeHost");
    private final static QName _ShutdownInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "shutdownInstanceResponse");
    private final static QName _ExecuteActionResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "executeActionResponse");
    private final static QName _SetInstanceIPAddress_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "setInstanceIPAddress");
    private final static QName _InitDomainsResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "initDomainsResponse");
    private final static QName _DestroyInstance_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "destroyInstance");
    private final static QName _ExecuteAction_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "executeAction");
    private final static QName _InitCloudManager_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "initCloudManager");
    private final static QName _DescribeHosts_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "describeHosts");
    private final static QName _CreateInstanceInHost_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "createInstanceInHost");
    private final static QName _SetInstanceIPAddressResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "setInstanceIPAddressResponse");
    private final static QName _MigrateInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "migrateInstanceResponse");
    private final static QName _RebootInstanceResponse_QNAME = new QName("http://cloudmanager.services.greenstarnetwork.com/", "rebootInstanceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.greenstarnetwork.services.cloudmanager
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RemoveDomainResponse }
     * 
     */
    public RemoveDomainResponse createRemoveDomainResponse() {
        return new RemoveDomainResponse();
    }

    /**
     * Create an instance of {@link RebootInstanceResponse }
     * 
     */
    public RebootInstanceResponse createRebootInstanceResponse() {
        return new RebootInstanceResponse();
    }

    /**
     * Create an instance of {@link StopInstanceResponse }
     * 
     */
    public StopInstanceResponse createStopInstanceResponse() {
        return new StopInstanceResponse();
    }

    /**
     * Create an instance of {@link CreateInstanceInHostResponse }
     * 
     */
    public CreateInstanceInHostResponse createCreateInstanceInHostResponse() {
        return new CreateInstanceInHostResponse();
    }

    /**
     * Create an instance of {@link SetInstanceIPAddress }
     * 
     */
    public SetInstanceIPAddress createSetInstanceIPAddress() {
        return new SetInstanceIPAddress();
    }

    /**
     * Create an instance of {@link ShutdownInstance }
     * 
     */
    public ShutdownInstance createShutdownInstance() {
        return new ShutdownInstance();
    }

    /**
     * Create an instance of {@link AddDomainResponse }
     * 
     */
    public AddDomainResponse createAddDomainResponse() {
        return new AddDomainResponse();
    }

    /**
     * Create an instance of {@link CreateInstanceInHost }
     * 
     */
    public CreateInstanceInHost createCreateInstanceInHost() {
        return new CreateInstanceInHost();
    }

    /**
     * Create an instance of {@link StopInstance }
     * 
     */
    public StopInstance createStopInstance() {
        return new StopInstance();
    }

    /**
     * Create an instance of {@link StartInstance }
     * 
     */
    public StartInstance createStartInstance() {
        return new StartInstance();
    }

    /**
     * Create an instance of {@link DestroyInstanceResponse }
     * 
     */
    public DestroyInstanceResponse createDestroyInstanceResponse() {
        return new DestroyInstanceResponse();
    }

    /**
     * Create an instance of {@link StartInstanceResponse }
     * 
     */
    public StartInstanceResponse createStartInstanceResponse() {
        return new StartInstanceResponse();
    }

    /**
     * Create an instance of {@link InitDomains }
     * 
     */
    public InitDomains createInitDomains() {
        return new InitDomains();
    }

    /**
     * Create an instance of {@link RemoveDomain }
     * 
     */
    public RemoveDomain createRemoveDomain() {
        return new RemoveDomain();
    }

    /**
     * Create an instance of {@link InitDomainsResponse }
     * 
     */
    public InitDomainsResponse createInitDomainsResponse() {
        return new InitDomainsResponse();
    }

    /**
     * Create an instance of {@link AddDomain }
     * 
     */
    public AddDomain createAddDomain() {
        return new AddDomain();
    }

    /**
     * Create an instance of {@link DescribeHostResponse }
     * 
     */
    public DescribeHostResponse createDescribeHostResponse() {
        return new DescribeHostResponse();
    }

    /**
     * Create an instance of {@link InitCloudManagerResponse }
     * 
     */
    public InitCloudManagerResponse createInitCloudManagerResponse() {
        return new InitCloudManagerResponse();
    }

    /**
     * Create an instance of {@link CreateInstance }
     * 
     */
    public CreateInstance createCreateInstance() {
        return new CreateInstance();
    }

    /**
     * Create an instance of {@link InitCloudManager }
     * 
     */
    public InitCloudManager createInitCloudManager() {
        return new InitCloudManager();
    }

    /**
     * Create an instance of {@link SetInstanceIPAddressResponse }
     * 
     */
    public SetInstanceIPAddressResponse createSetInstanceIPAddressResponse() {
        return new SetInstanceIPAddressResponse();
    }

    /**
     * Create an instance of {@link ExecuteAction }
     * 
     */
    public ExecuteAction createExecuteAction() {
        return new ExecuteAction();
    }

    /**
     * Create an instance of {@link DestroyInstance }
     * 
     */
    public DestroyInstance createDestroyInstance() {
        return new DestroyInstance();
    }

    /**
     * Create an instance of {@link HostQuery }
     * 
     */
    public HostQuery createHostQuery() {
        return new HostQuery();
    }

    /**
     * Create an instance of {@link DescribeHostsResponse }
     * 
     */
    public DescribeHostsResponse createDescribeHostsResponse() {
        return new DescribeHostsResponse();
    }

    /**
     * Create an instance of {@link CreateInstanceResponse }
     * 
     */
    public CreateInstanceResponse createCreateInstanceResponse() {
        return new CreateInstanceResponse();
    }

    /**
     * Create an instance of {@link HostQueryResponse }
     * 
     */
    public HostQueryResponse createHostQueryResponse() {
        return new HostQueryResponse();
    }

    /**
     * Create an instance of {@link ShutdownInstanceResponse }
     * 
     */
    public ShutdownInstanceResponse createShutdownInstanceResponse() {
        return new ShutdownInstanceResponse();
    }

    /**
     * Create an instance of {@link MigrateInstanceResponse }
     * 
     */
    public MigrateInstanceResponse createMigrateInstanceResponse() {
        return new MigrateInstanceResponse();
    }

    /**
     * Create an instance of {@link MigrateInstance }
     * 
     */
    public MigrateInstance createMigrateInstance() {
        return new MigrateInstance();
    }

    /**
     * Create an instance of {@link ExecuteActionResponse }
     * 
     */
    public ExecuteActionResponse createExecuteActionResponse() {
        return new ExecuteActionResponse();
    }

    /**
     * Create an instance of {@link DescribeHosts }
     * 
     */
    public DescribeHosts createDescribeHosts() {
        return new DescribeHosts();
    }

    /**
     * Create an instance of {@link RebootInstance }
     * 
     */
    public RebootInstance createRebootInstance() {
        return new RebootInstance();
    }

    /**
     * Create an instance of {@link DescribeHost }
     * 
     */
    public DescribeHost createDescribeHost() {
        return new DescribeHost();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitCloudManagerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "initCloudManagerResponse")
    public JAXBElement<InitCloudManagerResponse> createInitCloudManagerResponse(InitCloudManagerResponse value) {
        return new JAXBElement<InitCloudManagerResponse>(_InitCloudManagerResponse_QNAME, InitCloudManagerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MigrateInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "migrateInstance")
    public JAXBElement<MigrateInstance> createMigrateInstance(MigrateInstance value) {
        return new JAXBElement<MigrateInstance>(_MigrateInstance_QNAME, MigrateInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "startInstanceResponse")
    public JAXBElement<StartInstanceResponse> createStartInstanceResponse(StartInstanceResponse value) {
        return new JAXBElement<StartInstanceResponse>(_StartInstanceResponse_QNAME, StartInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HostQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "hostQuery")
    public JAXBElement<HostQuery> createHostQuery(HostQuery value) {
        return new JAXBElement<HostQuery>(_HostQuery_QNAME, HostQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "createInstanceResponse")
    public JAXBElement<CreateInstanceResponse> createCreateInstanceResponse(CreateInstanceResponse value) {
        return new JAXBElement<CreateInstanceResponse>(_CreateInstanceResponse_QNAME, CreateInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HostQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "hostQueryResponse")
    public JAXBElement<HostQueryResponse> createHostQueryResponse(HostQueryResponse value) {
        return new JAXBElement<HostQueryResponse>(_HostQueryResponse_QNAME, HostQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "stopInstance")
    public JAXBElement<StopInstance> createStopInstance(StopInstance value) {
        return new JAXBElement<StopInstance>(_StopInstance_QNAME, StopInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "stopInstanceResponse")
    public JAXBElement<StopInstanceResponse> createStopInstanceResponse(StopInstanceResponse value) {
        return new JAXBElement<StopInstanceResponse>(_StopInstanceResponse_QNAME, StopInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DestroyInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "destroyInstanceResponse")
    public JAXBElement<DestroyInstanceResponse> createDestroyInstanceResponse(DestroyInstanceResponse value) {
        return new JAXBElement<DestroyInstanceResponse>(_DestroyInstanceResponse_QNAME, DestroyInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "createInstance")
    public JAXBElement<CreateInstance> createCreateInstance(CreateInstance value) {
        return new JAXBElement<CreateInstance>(_CreateInstance_QNAME, CreateInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "startInstance")
    public JAXBElement<StartInstance> createStartInstance(StartInstance value) {
        return new JAXBElement<StartInstance>(_StartInstance_QNAME, StartInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShutdownInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "shutdownInstance")
    public JAXBElement<ShutdownInstance> createShutdownInstance(ShutdownInstance value) {
        return new JAXBElement<ShutdownInstance>(_ShutdownInstance_QNAME, ShutdownInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInstanceInHostResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "createInstanceInHostResponse")
    public JAXBElement<CreateInstanceInHostResponse> createCreateInstanceInHostResponse(CreateInstanceInHostResponse value) {
        return new JAXBElement<CreateInstanceInHostResponse>(_CreateInstanceInHostResponse_QNAME, CreateInstanceInHostResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescribeHostsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "describeHostsResponse")
    public JAXBElement<DescribeHostsResponse> createDescribeHostsResponse(DescribeHostsResponse value) {
        return new JAXBElement<DescribeHostsResponse>(_DescribeHostsResponse_QNAME, DescribeHostsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitDomains }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "initDomains")
    public JAXBElement<InitDomains> createInitDomains(InitDomains value) {
        return new JAXBElement<InitDomains>(_InitDomains_QNAME, InitDomains.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "addDomain")
    public JAXBElement<AddDomain> createAddDomain(AddDomain value) {
        return new JAXBElement<AddDomain>(_AddDomain_QNAME, AddDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddDomainResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "addDomainResponse")
    public JAXBElement<AddDomainResponse> createAddDomainResponse(AddDomainResponse value) {
        return new JAXBElement<AddDomainResponse>(_AddDomainResponse_QNAME, AddDomainResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescribeHostResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "describeHostResponse")
    public JAXBElement<DescribeHostResponse> createDescribeHostResponse(DescribeHostResponse value) {
        return new JAXBElement<DescribeHostResponse>(_DescribeHostResponse_QNAME, DescribeHostResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveDomainResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "removeDomainResponse")
    public JAXBElement<RemoveDomainResponse> createRemoveDomainResponse(RemoveDomainResponse value) {
        return new JAXBElement<RemoveDomainResponse>(_RemoveDomainResponse_QNAME, RemoveDomainResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RebootInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "rebootInstance")
    public JAXBElement<RebootInstance> createRebootInstance(RebootInstance value) {
        return new JAXBElement<RebootInstance>(_RebootInstance_QNAME, RebootInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "removeDomain")
    public JAXBElement<RemoveDomain> createRemoveDomain(RemoveDomain value) {
        return new JAXBElement<RemoveDomain>(_RemoveDomain_QNAME, RemoveDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescribeHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "describeHost")
    public JAXBElement<DescribeHost> createDescribeHost(DescribeHost value) {
        return new JAXBElement<DescribeHost>(_DescribeHost_QNAME, DescribeHost.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShutdownInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "shutdownInstanceResponse")
    public JAXBElement<ShutdownInstanceResponse> createShutdownInstanceResponse(ShutdownInstanceResponse value) {
        return new JAXBElement<ShutdownInstanceResponse>(_ShutdownInstanceResponse_QNAME, ShutdownInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteActionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "executeActionResponse")
    public JAXBElement<ExecuteActionResponse> createExecuteActionResponse(ExecuteActionResponse value) {
        return new JAXBElement<ExecuteActionResponse>(_ExecuteActionResponse_QNAME, ExecuteActionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetInstanceIPAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "setInstanceIPAddress")
    public JAXBElement<SetInstanceIPAddress> createSetInstanceIPAddress(SetInstanceIPAddress value) {
        return new JAXBElement<SetInstanceIPAddress>(_SetInstanceIPAddress_QNAME, SetInstanceIPAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitDomainsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "initDomainsResponse")
    public JAXBElement<InitDomainsResponse> createInitDomainsResponse(InitDomainsResponse value) {
        return new JAXBElement<InitDomainsResponse>(_InitDomainsResponse_QNAME, InitDomainsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DestroyInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "destroyInstance")
    public JAXBElement<DestroyInstance> createDestroyInstance(DestroyInstance value) {
        return new JAXBElement<DestroyInstance>(_DestroyInstance_QNAME, DestroyInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteAction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "executeAction")
    public JAXBElement<ExecuteAction> createExecuteAction(ExecuteAction value) {
        return new JAXBElement<ExecuteAction>(_ExecuteAction_QNAME, ExecuteAction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitCloudManager }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "initCloudManager")
    public JAXBElement<InitCloudManager> createInitCloudManager(InitCloudManager value) {
        return new JAXBElement<InitCloudManager>(_InitCloudManager_QNAME, InitCloudManager.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescribeHosts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "describeHosts")
    public JAXBElement<DescribeHosts> createDescribeHosts(DescribeHosts value) {
        return new JAXBElement<DescribeHosts>(_DescribeHosts_QNAME, DescribeHosts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInstanceInHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "createInstanceInHost")
    public JAXBElement<CreateInstanceInHost> createCreateInstanceInHost(CreateInstanceInHost value) {
        return new JAXBElement<CreateInstanceInHost>(_CreateInstanceInHost_QNAME, CreateInstanceInHost.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetInstanceIPAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "setInstanceIPAddressResponse")
    public JAXBElement<SetInstanceIPAddressResponse> createSetInstanceIPAddressResponse(SetInstanceIPAddressResponse value) {
        return new JAXBElement<SetInstanceIPAddressResponse>(_SetInstanceIPAddressResponse_QNAME, SetInstanceIPAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MigrateInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "migrateInstanceResponse")
    public JAXBElement<MigrateInstanceResponse> createMigrateInstanceResponse(MigrateInstanceResponse value) {
        return new JAXBElement<MigrateInstanceResponse>(_MigrateInstanceResponse_QNAME, MigrateInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RebootInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudmanager.services.greenstarnetwork.com/", name = "rebootInstanceResponse")
    public JAXBElement<RebootInstanceResponse> createRebootInstanceResponse(RebootInstanceResponse value) {
        return new JAXBElement<RebootInstanceResponse>(_RebootInstanceResponse_QNAME, RebootInstanceResponse.class, null, value);
    }

}
