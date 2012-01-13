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
package com.greenstarnetwork.models.facilityModel;

import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.model.IResourceModelFactory;
import com.iaasframework.capabilities.model.ModelException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;


/**
 * 
 * create a facility model from description
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca) - Synchromedia lab.
 *
 */

public class FacilityModelFactory implements IResourceModelFactory {

	private String propertyStr ;
	final private static String modelStr = "facility model";

	public IResourceModel createResourceModelInstance(CapabilityDescriptor descriptor) throws ModelException {


		FacilityModel facilityModel = new FacilityModel();

		propertyStr = "location.address";
		if (descriptor.getPropertyValue(propertyStr) ==null){
			throw new ModelException("The "+propertyStr+" property" +
					" is required to create a(n) "+modelStr);
		};

		facilityModel.setLocation(new Location(descriptor.getPropertyValue(propertyStr)));

		propertyStr = "domainID";
		if (descriptor.getPropertyValue(propertyStr) ==null){
			throw new ModelException("The "+propertyStr+" property" +
					" is required to create a(n) "+modelStr);
		};

		facilityModel.setDomainID(descriptor.getPropertyValue(propertyStr));
		
		propertyStr = "alarmSendTo";
		if (descriptor.getPropertyValue(propertyStr) ==null){
			throw new ModelException("The "+propertyStr+" property" +
					" is required to create a(n) "+modelStr);
		};

		facilityModel.setAlarmSendTo(descriptor.getPropertyValue(propertyStr));

		{
			OperationalSpecs operationalSpecs = new OperationalSpecs();

			propertyStr = "spec.powerSourceGreenPercentage";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};
			operationalSpecs.setPowerSourceGreenPercentage(descriptor.getPropertyValue(propertyStr));

			propertyStr = "spec.gridGreenPercentage";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};
			operationalSpecs.setGridGreenPercentage(descriptor.getPropertyValue(propertyStr));

			propertyStr = "spec.powerSourceType";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};
			operationalSpecs.setPowerSourceType(descriptor.getPropertyValue(propertyStr));
			propertyStr = "spec.opHourThreshold";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};
			operationalSpecs.setOpHourThreshold(descriptor.getPropertyValue(propertyStr));
			propertyStr = "spec.opHourThresholdUnderMax";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};
			operationalSpecs.setOpHourThresholdUnderMax(descriptor.getPropertyValue(propertyStr));
			
			facilityModel.setOperationalSpecs(operationalSpecs);
		}

		/*
		propertyStr = "climate.discovery";
		if (descriptor.getPropertyValue(propertyStr) ==null){
			throw new ModelException("The "+propertyStr+" property" +
					" is required to create a(n) "+modelStr);
		};

		facilityModel.setClimateDiscovery(descriptor.getPropertyValue(propertyStr));

		if (!facilityModel.getClimateDiscovery().equalsIgnoreCase("AUTO")){

			propertyStr = "climate.resourceID";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};

			facilityModel.setClimateResourceID(descriptor.getPropertyValue(propertyStr));
		}
		*/
		/*
		propertyStr = "pdu.total";
		if (descriptor.getPropertyValue(propertyStr) ==null){
			throw new ModelException("The "+propertyStr+" property" +
					" is required to create a(n) "+modelStr);
		};

		int pduTotal = Integer.parseInt(descriptor.getPropertyValue(propertyStr));

		for (int i=1;i<=pduTotal;i++){
			propertyStr = "pdu"+String.valueOf(i)+".resourceID";
			if (descriptor.getPropertyValue(propertyStr) ==null){
				throw new ModelException("The "+propertyStr+" property" +
						" is required to create a(n) "+modelStr);
			};
			PowerDistribution powerDistribution= new PowerDistribution();
			powerDistribution.setResourceID(descriptor.getPropertyValue(propertyStr));

			{//adding power sources to the pdu

				propertyStr = "pdu"+String.valueOf(i)+".source.total";
				if (descriptor.getPropertyValue(propertyStr) ==null){
					throw new ModelException("The "+propertyStr+" property" +
							" is required to create a(n) "+modelStr);
				};

				int pduSourceTotal = Integer.parseInt(descriptor.getPropertyValue(propertyStr));
				for (int j=1;j<=pduSourceTotal;j++){
					propertyStr = "pdu"+String.valueOf(i)+".source"+String.valueOf(j)+".resourceID";
					if (descriptor.getPropertyValue(propertyStr) ==null){
						throw new ModelException("The "+propertyStr+" property" +
								" is required to create a(n) "+modelStr);
					};
					PowerSource powerSource = new PowerSource(descriptor.getPropertyValue(propertyStr));
					propertyStr = "pdu"+String.valueOf(i)+".source"+String.valueOf(j)+".portNumber";
					if (descriptor.getPropertyValue(propertyStr) ==null){
						throw new ModelException("The "+propertyStr+" property" +
								" is required to create a(n) "+modelStr);
					};
					powerSource.getPort().setPortNumber(Integer.parseInt(descriptor.getPropertyValue(propertyStr)));
					powerDistribution.addPowerSource(powerSource);

				}

			}

			{//adding power sinks to the PDU

				propertyStr = "pdu"+String.valueOf(i)+".sink.total";
				if (descriptor.getPropertyValue(propertyStr) ==null){
					throw new ModelException("The "+propertyStr+" property" +
							" is required to create a(n) "+modelStr);
				};

				int pduSinkTotal = Integer.parseInt(descriptor.getPropertyValue(propertyStr));
				for (int j=1;j<=pduSinkTotal;j++){
					propertyStr = "pdu"+String.valueOf(i)+".sink"+String.valueOf(j)+".resourceID";
					if (descriptor.getPropertyValue(propertyStr) ==null){
						throw new ModelException("The "+propertyStr+" property" +
								" is required to create a(n) "+modelStr);
					};
					PowerSink powerSink = new PowerSink(descriptor.getPropertyValue(propertyStr));
					propertyStr = "pdu"+String.valueOf(i)+".sink"+String.valueOf(j)+".portNumber";
					if (descriptor.getPropertyValue(propertyStr) ==null){
						throw new ModelException("The "+propertyStr+" property" +
								" is required to create a(n) "+modelStr);
					};
					powerSink.getPort().setPortNumber(Integer.parseInt(descriptor.getPropertyValue(propertyStr)));
					powerDistribution.addPowerSink(powerSink);

				}

			}


			facilityModel.addPowerDistribution(powerDistribution);
			
		}*/


		return facilityModel;
	}

}
