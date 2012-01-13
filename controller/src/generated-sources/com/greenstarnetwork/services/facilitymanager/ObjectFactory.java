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
package com.greenstarnetwork.services.facilitymanager;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.greenstarnetwork.services.facilitymanager package. 
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

    private final static QName _GetArchiveDataByRangDate_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "getArchiveDataByRangDate");
    private final static QName _GetArchiveDataByRangDateResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "getArchiveDataByRangDateResponse");
    private final static QName _GetResourceModel_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "getResourceModel");
    private final static QName _CalculateOpHourResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "calculateOpHourResponse");
    private final static QName _ExecuteAction_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "executeAction");
    private final static QName _ListAllClimatesResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllClimatesResponse");
    private final static QName _CalculateOpHour_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "calculateOpHour");
    private final static QName _GetResourceModel1Response_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "getResourceModel1Response");
    private final static QName _ListAllClimates_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllClimates");
    private final static QName _RefreshResource_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "refreshResource");
    private final static QName _SetThreshold_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "setThreshold");
    private final static QName _TurnOffResource_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "turnOffResource");
    private final static QName _ListAllFacilities_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllFacilities");
    private final static QName _ListAllPDUs_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllPDUs");
    private final static QName _RefreshResourceResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "refreshResourceResponse");
    private final static QName _ExecuteActionResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "executeActionResponse");
    private final static QName _ListAllPowerSources_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllPowerSources");
    private final static QName _TurnOffResourceResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "turnOffResourceResponse");
    private final static QName _GetResourceModel1_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "getResourceModel1");
    private final static QName _RegisterControllerResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "registerControllerResponse");
    private final static QName _GetResourceModelResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "getResourceModelResponse");
    private final static QName _ListAllPowerSourcesResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllPowerSourcesResponse");
    private final static QName _RegisterController_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "registerController");
    private final static QName _TurnOnResource_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "turnOnResource");
    private final static QName _FacilityManagerException_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "FacilityManagerException");
    private final static QName _OperationalSpecs_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "operationalSpecs");
    private final static QName _ListAllPDUsResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllPDUsResponse");
    private final static QName _ListAllFacilitiesResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "listAllFacilitiesResponse");
    private final static QName _TurnOnResourceResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "turnOnResourceResponse");
    private final static QName _SetThresholdResponse_QNAME = new QName("http://facilitymanager.services.greenstarnetwork.com/", "setThresholdResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.greenstarnetwork.services.facilitymanager
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RegisterController }
     * 
     */
    public RegisterController createRegisterController() {
        return new RegisterController();
    }

    /**
     * Create an instance of {@link GetArchiveDataByRangDate }
     * 
     */
    public GetArchiveDataByRangDate createGetArchiveDataByRangDate() {
        return new GetArchiveDataByRangDate();
    }

    /**
     * Create an instance of {@link GetArchiveDataByRangDateResponse }
     * 
     */
    public GetArchiveDataByRangDateResponse createGetArchiveDataByRangDateResponse() {
        return new GetArchiveDataByRangDateResponse();
    }

    /**
     * Create an instance of {@link ListAllFacilitiesResponse }
     * 
     */
    public ListAllFacilitiesResponse createListAllFacilitiesResponse() {
        return new ListAllFacilitiesResponse();
    }

    /**
     * Create an instance of {@link ListAllPowerSourcesResponse }
     * 
     */
    public ListAllPowerSourcesResponse createListAllPowerSourcesResponse() {
        return new ListAllPowerSourcesResponse();
    }

    /**
     * Create an instance of {@link SetThresholdResponse }
     * 
     */
    public SetThresholdResponse createSetThresholdResponse() {
        return new SetThresholdResponse();
    }

    /**
     * Create an instance of {@link TurnOnResource }
     * 
     */
    public TurnOnResource createTurnOnResource() {
        return new TurnOnResource();
    }

    /**
     * Create an instance of {@link FacilityManagerException }
     * 
     */
    public FacilityManagerException createFacilityManagerException() {
        return new FacilityManagerException();
    }

    /**
     * Create an instance of {@link ExecuteActionResponse }
     * 
     */
    public ExecuteActionResponse createExecuteActionResponse() {
        return new ExecuteActionResponse();
    }

    /**
     * Create an instance of {@link CalculateOpHour }
     * 
     */
    public CalculateOpHour createCalculateOpHour() {
        return new CalculateOpHour();
    }

    /**
     * Create an instance of {@link GetResourceModelResponse }
     * 
     */
    public GetResourceModelResponse createGetResourceModelResponse() {
        return new GetResourceModelResponse();
    }

    /**
     * Create an instance of {@link GetResourceModel1 }
     * 
     */
    public GetResourceModel1 createGetResourceModel1() {
        return new GetResourceModel1();
    }

    /**
     * Create an instance of {@link GetResourceModel }
     * 
     */
    public GetResourceModel createGetResourceModel() {
        return new GetResourceModel();
    }

    /**
     * Create an instance of {@link OperationalSpecs }
     * 
     */
    public OperationalSpecs createOperationalSpecs() {
        return new OperationalSpecs();
    }

    /**
     * Create an instance of {@link ListAllPDUs }
     * 
     */
    public ListAllPDUs createListAllPDUs() {
        return new ListAllPDUs();
    }

    /**
     * Create an instance of {@link ExecuteAction }
     * 
     */
    public ExecuteAction createExecuteAction() {
        return new ExecuteAction();
    }

    /**
     * Create an instance of {@link RefreshResource }
     * 
     */
    public RefreshResource createRefreshResource() {
        return new RefreshResource();
    }

    /**
     * Create an instance of {@link RegisterControllerResponse }
     * 
     */
    public RegisterControllerResponse createRegisterControllerResponse() {
        return new RegisterControllerResponse();
    }

    /**
     * Create an instance of {@link RefreshResourceResponse }
     * 
     */
    public RefreshResourceResponse createRefreshResourceResponse() {
        return new RefreshResourceResponse();
    }

    /**
     * Create an instance of {@link TurnOnResourceResponse }
     * 
     */
    public TurnOnResourceResponse createTurnOnResourceResponse() {
        return new TurnOnResourceResponse();
    }

    /**
     * Create an instance of {@link ListAllPowerSources }
     * 
     */
    public ListAllPowerSources createListAllPowerSources() {
        return new ListAllPowerSources();
    }

    /**
     * Create an instance of {@link CalculateOpHourResponse }
     * 
     */
    public CalculateOpHourResponse createCalculateOpHourResponse() {
        return new CalculateOpHourResponse();
    }

    /**
     * Create an instance of {@link SetThreshold }
     * 
     */
    public SetThreshold createSetThreshold() {
        return new SetThreshold();
    }

    /**
     * Create an instance of {@link TurnOffResourceResponse }
     * 
     */
    public TurnOffResourceResponse createTurnOffResourceResponse() {
        return new TurnOffResourceResponse();
    }

    /**
     * Create an instance of {@link ListAllClimates }
     * 
     */
    public ListAllClimates createListAllClimates() {
        return new ListAllClimates();
    }

    /**
     * Create an instance of {@link ListAllPDUsResponse }
     * 
     */
    public ListAllPDUsResponse createListAllPDUsResponse() {
        return new ListAllPDUsResponse();
    }

    /**
     * Create an instance of {@link ListAllFacilities }
     * 
     */
    public ListAllFacilities createListAllFacilities() {
        return new ListAllFacilities();
    }

    /**
     * Create an instance of {@link TurnOffResource }
     * 
     */
    public TurnOffResource createTurnOffResource() {
        return new TurnOffResource();
    }

    /**
     * Create an instance of {@link ListAllClimatesResponse }
     * 
     */
    public ListAllClimatesResponse createListAllClimatesResponse() {
        return new ListAllClimatesResponse();
    }

    /**
     * Create an instance of {@link GetResourceModel1Response }
     * 
     */
    public GetResourceModel1Response createGetResourceModel1Response() {
        return new GetResourceModel1Response();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetArchiveDataByRangDate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "getArchiveDataByRangDate")
    public JAXBElement<GetArchiveDataByRangDate> createGetArchiveDataByRangDate(GetArchiveDataByRangDate value) {
        return new JAXBElement<GetArchiveDataByRangDate>(_GetArchiveDataByRangDate_QNAME, GetArchiveDataByRangDate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetArchiveDataByRangDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "getArchiveDataByRangDateResponse")
    public JAXBElement<GetArchiveDataByRangDateResponse> createGetArchiveDataByRangDateResponse(GetArchiveDataByRangDateResponse value) {
        return new JAXBElement<GetArchiveDataByRangDateResponse>(_GetArchiveDataByRangDateResponse_QNAME, GetArchiveDataByRangDateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "getResourceModel")
    public JAXBElement<GetResourceModel> createGetResourceModel(GetResourceModel value) {
        return new JAXBElement<GetResourceModel>(_GetResourceModel_QNAME, GetResourceModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateOpHourResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "calculateOpHourResponse")
    public JAXBElement<CalculateOpHourResponse> createCalculateOpHourResponse(CalculateOpHourResponse value) {
        return new JAXBElement<CalculateOpHourResponse>(_CalculateOpHourResponse_QNAME, CalculateOpHourResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteAction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "executeAction")
    public JAXBElement<ExecuteAction> createExecuteAction(ExecuteAction value) {
        return new JAXBElement<ExecuteAction>(_ExecuteAction_QNAME, ExecuteAction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllClimatesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllClimatesResponse")
    public JAXBElement<ListAllClimatesResponse> createListAllClimatesResponse(ListAllClimatesResponse value) {
        return new JAXBElement<ListAllClimatesResponse>(_ListAllClimatesResponse_QNAME, ListAllClimatesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateOpHour }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "calculateOpHour")
    public JAXBElement<CalculateOpHour> createCalculateOpHour(CalculateOpHour value) {
        return new JAXBElement<CalculateOpHour>(_CalculateOpHour_QNAME, CalculateOpHour.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceModel1Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "getResourceModel1Response")
    public JAXBElement<GetResourceModel1Response> createGetResourceModel1Response(GetResourceModel1Response value) {
        return new JAXBElement<GetResourceModel1Response>(_GetResourceModel1Response_QNAME, GetResourceModel1Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllClimates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllClimates")
    public JAXBElement<ListAllClimates> createListAllClimates(ListAllClimates value) {
        return new JAXBElement<ListAllClimates>(_ListAllClimates_QNAME, ListAllClimates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefreshResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "refreshResource")
    public JAXBElement<RefreshResource> createRefreshResource(RefreshResource value) {
        return new JAXBElement<RefreshResource>(_RefreshResource_QNAME, RefreshResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetThreshold }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "setThreshold")
    public JAXBElement<SetThreshold> createSetThreshold(SetThreshold value) {
        return new JAXBElement<SetThreshold>(_SetThreshold_QNAME, SetThreshold.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TurnOffResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "turnOffResource")
    public JAXBElement<TurnOffResource> createTurnOffResource(TurnOffResource value) {
        return new JAXBElement<TurnOffResource>(_TurnOffResource_QNAME, TurnOffResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllFacilities }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllFacilities")
    public JAXBElement<ListAllFacilities> createListAllFacilities(ListAllFacilities value) {
        return new JAXBElement<ListAllFacilities>(_ListAllFacilities_QNAME, ListAllFacilities.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllPDUs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllPDUs")
    public JAXBElement<ListAllPDUs> createListAllPDUs(ListAllPDUs value) {
        return new JAXBElement<ListAllPDUs>(_ListAllPDUs_QNAME, ListAllPDUs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefreshResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "refreshResourceResponse")
    public JAXBElement<RefreshResourceResponse> createRefreshResourceResponse(RefreshResourceResponse value) {
        return new JAXBElement<RefreshResourceResponse>(_RefreshResourceResponse_QNAME, RefreshResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteActionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "executeActionResponse")
    public JAXBElement<ExecuteActionResponse> createExecuteActionResponse(ExecuteActionResponse value) {
        return new JAXBElement<ExecuteActionResponse>(_ExecuteActionResponse_QNAME, ExecuteActionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllPowerSources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllPowerSources")
    public JAXBElement<ListAllPowerSources> createListAllPowerSources(ListAllPowerSources value) {
        return new JAXBElement<ListAllPowerSources>(_ListAllPowerSources_QNAME, ListAllPowerSources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TurnOffResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "turnOffResourceResponse")
    public JAXBElement<TurnOffResourceResponse> createTurnOffResourceResponse(TurnOffResourceResponse value) {
        return new JAXBElement<TurnOffResourceResponse>(_TurnOffResourceResponse_QNAME, TurnOffResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceModel1 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "getResourceModel1")
    public JAXBElement<GetResourceModel1> createGetResourceModel1(GetResourceModel1 value) {
        return new JAXBElement<GetResourceModel1>(_GetResourceModel1_QNAME, GetResourceModel1 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterControllerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "registerControllerResponse")
    public JAXBElement<RegisterControllerResponse> createRegisterControllerResponse(RegisterControllerResponse value) {
        return new JAXBElement<RegisterControllerResponse>(_RegisterControllerResponse_QNAME, RegisterControllerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceModelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "getResourceModelResponse")
    public JAXBElement<GetResourceModelResponse> createGetResourceModelResponse(GetResourceModelResponse value) {
        return new JAXBElement<GetResourceModelResponse>(_GetResourceModelResponse_QNAME, GetResourceModelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllPowerSourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllPowerSourcesResponse")
    public JAXBElement<ListAllPowerSourcesResponse> createListAllPowerSourcesResponse(ListAllPowerSourcesResponse value) {
        return new JAXBElement<ListAllPowerSourcesResponse>(_ListAllPowerSourcesResponse_QNAME, ListAllPowerSourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterController }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "registerController")
    public JAXBElement<RegisterController> createRegisterController(RegisterController value) {
        return new JAXBElement<RegisterController>(_RegisterController_QNAME, RegisterController.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TurnOnResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "turnOnResource")
    public JAXBElement<TurnOnResource> createTurnOnResource(TurnOnResource value) {
        return new JAXBElement<TurnOnResource>(_TurnOnResource_QNAME, TurnOnResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacilityManagerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "FacilityManagerException")
    public JAXBElement<FacilityManagerException> createFacilityManagerException(FacilityManagerException value) {
        return new JAXBElement<FacilityManagerException>(_FacilityManagerException_QNAME, FacilityManagerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationalSpecs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "operationalSpecs")
    public JAXBElement<OperationalSpecs> createOperationalSpecs(OperationalSpecs value) {
        return new JAXBElement<OperationalSpecs>(_OperationalSpecs_QNAME, OperationalSpecs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllPDUsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllPDUsResponse")
    public JAXBElement<ListAllPDUsResponse> createListAllPDUsResponse(ListAllPDUsResponse value) {
        return new JAXBElement<ListAllPDUsResponse>(_ListAllPDUsResponse_QNAME, ListAllPDUsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllFacilitiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "listAllFacilitiesResponse")
    public JAXBElement<ListAllFacilitiesResponse> createListAllFacilitiesResponse(ListAllFacilitiesResponse value) {
        return new JAXBElement<ListAllFacilitiesResponse>(_ListAllFacilitiesResponse_QNAME, ListAllFacilitiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TurnOnResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "turnOnResourceResponse")
    public JAXBElement<TurnOnResourceResponse> createTurnOnResourceResponse(TurnOnResourceResponse value) {
        return new JAXBElement<TurnOnResourceResponse>(_TurnOnResourceResponse_QNAME, TurnOnResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetThresholdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://facilitymanager.services.greenstarnetwork.com/", name = "setThresholdResponse")
    public JAXBElement<SetThresholdResponse> createSetThresholdResponse(SetThresholdResponse value) {
        return new JAXBElement<SetThresholdResponse>(_SetThresholdResponse_QNAME, SetThresholdResponse.class, null, value);
    }

}
