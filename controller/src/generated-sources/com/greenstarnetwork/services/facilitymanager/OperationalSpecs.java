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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for operationalSpecs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="operationalSpecs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="batteryChargePercentage" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="domainGreenPercentage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gridGreenPercentage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="onGrid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opHourThreshold" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opHourThresholdUnderMax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opHourUnderCurrentLoad" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="opHourUnderMaximumLoad" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="powerSourceGreenPercentage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="powerSourceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalConsummingPower" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="totalGeneratingPower" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operationalSpecs", propOrder = {
    "batteryChargePercentage",
    "domainGreenPercentage",
    "gridGreenPercentage",
    "onGrid",
    "opHourThreshold",
    "opHourThresholdUnderMax",
    "opHourUnderCurrentLoad",
    "opHourUnderMaximumLoad",
    "powerSourceGreenPercentage",
    "powerSourceType",
    "totalConsummingPower",
    "totalGeneratingPower"
})
public class OperationalSpecs {

    protected double batteryChargePercentage;
    protected String domainGreenPercentage;
    protected String gridGreenPercentage;
    protected String onGrid;
    protected String opHourThreshold;
    protected String opHourThresholdUnderMax;
    protected double opHourUnderCurrentLoad;
    protected double opHourUnderMaximumLoad;
    protected String powerSourceGreenPercentage;
    protected String powerSourceType;
    protected double totalConsummingPower;
    protected double totalGeneratingPower;

    /**
     * Gets the value of the batteryChargePercentage property.
     * 
     */
    public double getBatteryChargePercentage() {
        return batteryChargePercentage;
    }

    /**
     * Sets the value of the batteryChargePercentage property.
     * 
     */
    public void setBatteryChargePercentage(double value) {
        this.batteryChargePercentage = value;
    }

    /**
     * Gets the value of the domainGreenPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainGreenPercentage() {
        return domainGreenPercentage;
    }

    /**
     * Sets the value of the domainGreenPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainGreenPercentage(String value) {
        this.domainGreenPercentage = value;
    }

    /**
     * Gets the value of the gridGreenPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGridGreenPercentage() {
        return gridGreenPercentage;
    }

    /**
     * Sets the value of the gridGreenPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGridGreenPercentage(String value) {
        this.gridGreenPercentage = value;
    }

    /**
     * Gets the value of the onGrid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnGrid() {
        return onGrid;
    }

    /**
     * Sets the value of the onGrid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnGrid(String value) {
        this.onGrid = value;
    }

    /**
     * Gets the value of the opHourThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpHourThreshold() {
        return opHourThreshold;
    }

    /**
     * Sets the value of the opHourThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpHourThreshold(String value) {
        this.opHourThreshold = value;
    }

    /**
     * Gets the value of the opHourThresholdUnderMax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpHourThresholdUnderMax() {
        return opHourThresholdUnderMax;
    }

    /**
     * Sets the value of the opHourThresholdUnderMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpHourThresholdUnderMax(String value) {
        this.opHourThresholdUnderMax = value;
    }

    /**
     * Gets the value of the opHourUnderCurrentLoad property.
     * 
     */
    public double getOpHourUnderCurrentLoad() {
        return opHourUnderCurrentLoad;
    }

    /**
     * Sets the value of the opHourUnderCurrentLoad property.
     * 
     */
    public void setOpHourUnderCurrentLoad(double value) {
        this.opHourUnderCurrentLoad = value;
    }

    /**
     * Gets the value of the opHourUnderMaximumLoad property.
     * 
     */
    public double getOpHourUnderMaximumLoad() {
        return opHourUnderMaximumLoad;
    }

    /**
     * Sets the value of the opHourUnderMaximumLoad property.
     * 
     */
    public void setOpHourUnderMaximumLoad(double value) {
        this.opHourUnderMaximumLoad = value;
    }

    /**
     * Gets the value of the powerSourceGreenPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerSourceGreenPercentage() {
        return powerSourceGreenPercentage;
    }

    /**
     * Sets the value of the powerSourceGreenPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerSourceGreenPercentage(String value) {
        this.powerSourceGreenPercentage = value;
    }

    /**
     * Gets the value of the powerSourceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerSourceType() {
        return powerSourceType;
    }

    /**
     * Sets the value of the powerSourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerSourceType(String value) {
        this.powerSourceType = value;
    }

    /**
     * Gets the value of the totalConsummingPower property.
     * 
     */
    public double getTotalConsummingPower() {
        return totalConsummingPower;
    }

    /**
     * Sets the value of the totalConsummingPower property.
     * 
     */
    public void setTotalConsummingPower(double value) {
        this.totalConsummingPower = value;
    }

    /**
     * Gets the value of the totalGeneratingPower property.
     * 
     */
    public double getTotalGeneratingPower() {
        return totalGeneratingPower;
    }

    /**
     * Sets the value of the totalGeneratingPower property.
     * 
     */
    public void setTotalGeneratingPower(double value) {
        this.totalGeneratingPower = value;
    }

}
