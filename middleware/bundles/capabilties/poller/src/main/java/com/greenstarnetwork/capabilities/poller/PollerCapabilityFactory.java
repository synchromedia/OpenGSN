package com.greenstarnetwork.capabilities.poller;


/**
 * Poller cap factory class.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iaasframework.resources.core.capability.AbstractCapabilityFactory;
import com.iaasframework.resources.core.capability.CapabilityException;
import com.iaasframework.resources.core.capability.ICapability;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;


public class PollerCapabilityFactory extends AbstractCapabilityFactory
{
	/** Logger */
	private Logger logger = LoggerFactory.getLogger(PollerCapabilityFactory.class);

	public PollerCapabilityFactory() {
		logger.debug(IPollerConstants.POLLER+" Capability Factory created");
	}
	
	/**
	 * instantiating new cap. 
	 */
	@Override
	public synchronized ICapability createCapability(CapabilityDescriptor capabilityDescriptor, String resourceId) throws CapabilityException{
		PollerCapability capability = null;


		logger.info("Create "+IPollerConstants.POLLER+" Capability requested for module : " 
				+ capabilityDescriptor.getCapabilityInformation().toString() + " with CapabilityProperties: " + capabilityDescriptor.capabilityPropertiesToString());
		
	
		capability = new PollerCapability(capabilityDescriptor, resourceId);
		capabilityDescriptor.setEnabled(true);
		
		logger.debug(IPollerConstants.POLLER+capability);
		logger.debug(IPollerConstants.POLLER+"poller cap created");
		
		
		return capability;
	}
	
}
