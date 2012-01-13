package com.greenstarnetwork.capabilities.poller;

/**
 * Poller cap class :: run an specific action in specific intervals.
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iaasframework.capabilities.actionset.ActionSetCapabilityClient;
import com.iaasframework.resources.core.capability.AbstractJMSCapability;
import com.iaasframework.resources.core.capability.CapabilityException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;
import com.iaasframework.resources.core.message.ICapabilityMessage;



public class PollerCapability extends AbstractJMSCapability {
	Logger logger = LoggerFactory.getLogger(PollerCapability.class);
	
	String action = null;
	int interval =-1;
	


	public PollerCapability(CapabilityDescriptor descriptor, String engineID) {
		super(descriptor, engineID);

		
		action = descriptor.getPropertyValue(IPollerConstants.ACTION_KEY);
		interval = Integer.parseInt(descriptor.getPropertyValue(IPollerConstants.INTERVAL_KEY));
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new RemindTask(), interval * 1000, interval * 1000);
	}
	
	/**
	 * Repeating task for timer 
	 *
	 */
	class RemindTask extends TimerTask {
		public void run() {
			logger.debug("Resource("+resourceId+") running the poller ...");
			try {
			
				execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error in ControllerTimer execution :: "+e);
			}

		}
	}

	/**
	 * Execute function for timer class
	 * @throws Exception 
	 */
	public void execute() throws Exception {
		logger.debug("running ...");
        ActionSetCapabilityClient client = new ActionSetCapabilityClient(resourceId);
        Map<Object, Object> args = new Hashtable<Object, Object>();
		args.put("resourceID", resourceId);
        
        
        logger.debug("********* Executing action *****");
		String response = client.executeAction(action, args);
		logger.debug("********* Received response: "+ response + "\n");
	}


	@Override
	protected void activateCapability() throws CapabilityException {
		logger.debug("Command capability Activated");
	}

	@Override
	protected void deactivateCapability() throws CapabilityException {
		logger.debug("CommandSet capability Deactivated");
	}

	@Override
	protected void initializeCapability() throws CapabilityException {
		logger.debug("CommandSet capability Initialized");
	}

	@Override
	protected void shutdownCapability() throws CapabilityException {
		logger.debug("CommandSet capability Shutdown");

	}

	@Override
	public String toString() {
		return "CommandSetEngineModule - " + getCapabilityInformation().toString();
	}

	
	@Override
	protected void handleMessage(ICapabilityMessage arg0) throws JMSException {
		// TODO Auto-generated method stub
		
	}
}
