package com.greenstarnetwork.capabilities.poller;

/**
 * Poller cap activator
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;




public class Activator implements BundleActivator{
	
	private static BundleContext context;

	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public static BundleContext getContext(){
		return context;
	}

}
