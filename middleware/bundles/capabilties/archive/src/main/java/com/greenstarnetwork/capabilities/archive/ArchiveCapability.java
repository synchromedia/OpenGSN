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
package com.greenstarnetwork.capabilities.archive;

import javax.jms.JMSException;

import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenstarnetwork.capabilities.archive.ArchiveState.aState;
import com.greenstarnetwork.capabilities.archive.api.ArchiveSerializer;
import com.iaasframework.capabilities.model.IModelConstants;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.model.ModelCapabilityClient;
import com.iaasframework.capabilities.model.RequestModelResponse;
import com.iaasframework.capabilities.model.api.ModelRequestMessage;
import com.iaasframework.capabilities.model.api.ModelResponseMessage;
import com.iaasframework.resources.core.IResource;
import com.iaasframework.resources.core.IResourceManager;
import com.iaasframework.resources.core.IResourceRepository;
import com.iaasframework.resources.core.ResourceManager;
import com.iaasframework.resources.core.capability.AbstractJMSCapability;
import com.iaasframework.resources.core.capability.CapabilityException;
import com.iaasframework.resources.core.descriptor.CapabilityDescriptor;
import com.iaasframework.resources.core.descriptor.Information;
import com.iaasframework.resources.core.message.ICapabilityMessage;
import com.iaasframework.capabilities.model.Activator;
/**
 * 
 * 
 * @author abdelhamid Synchromedia
 * 
 * this class get model and archive it
 * 
 */

public class ArchiveCapability extends AbstractJMSCapability {
	Logger logger = LoggerFactory.getLogger(ArchiveCapability.class);
	private ResourceManager resourceManager;
	private Properties ArchiveContext = null;
	private int period;
	protected ArchiveState state = null;
	protected String archiveIdentifier;
	protected ArchiveSerializer archiveserializer;
	Information inf;
    boolean init=false;
    IResourceModel model;
    Timer timer;

	public ArchiveCapability(CapabilityDescriptor descriptor, String resourceId) {
     
		super(descriptor, resourceId);
		if (descriptor!= null)
      {
		inf=	getCapabilityInformation();
		this.state = new ArchiveState();
		period = Integer.parseInt(descriptor
				.getPropertyValue(IArchiveConstants.PERIOD_KEY));
			
		 timer = new Timer();
		timer.scheduleAtFixedRate(new RemindTask(), period * 60000,period * 60000);
		ArchiveContext = new Properties();
		
      }
	}



	@Override
	protected void activateCapability() throws CapabilityException {
		logger.debug("capability activate");

	}

	@Override
	protected void deactivateCapability() throws CapabilityException {
		logger.debug("capability desactivate");

	}

	@Override
	protected void initializeCapability() throws CapabilityException {
		logger.debug("capability Initialized");

	}

	@Override
	protected void shutdownCapability() throws CapabilityException {
		logger.debug("capability Shutdown");

	}

	/**
	 * @return the archive Context
	 */
	public Properties getCommandContext() {
		if (ArchiveContext == null) {
			ArchiveContext = new Properties();
		}
		return ArchiveContext;
	}

	/**
	 * Repeating task for timer 
	 *
	 */
	class RemindTask extends TimerTask {
		public void run() {
			logger.debug("Resource("+resourceId+") ");
			try {
		
	
//			model = getModel(resourceId);
//			if (model!=null){
//			    archiveModel(model);
//			}else timer.cancel();
				
 			
					requestEngineModel(true);
 			
 			    
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("*****************" +
						"Error in ControllerTimer execution********** :: "+e);

			}

		}
	}

	/**
	 * Send a message to the ModelEngineModule to get the model
	 * 
	 * @throws JMSException
	 */
	protected void requestEngineModel(boolean readOnly) throws ArchiveException {

		logger.debug("Archive Capability - attempting to get Model");
		ModelRequestMessage message = new ModelRequestMessage();
		message.setReadOnly(readOnly);

		try {
			sendMessage(message, IModelConstants.MODEL, this);
		}

		catch (CapabilityException e) {
		    timer.cancel();
		//	throw new ArchiveException(e.getMessage(), e);
		}
		state.setState(aState.WAIT_FOR_MODEL);
	}

	 /**
	  * 
	  * 
	  * 
	  * @param resourceId
	  * @return IResource Model
	  */
	 public IResourceModel getModel(String resourceId) {
			try {
				
				if (state.getState().equals(aState.READY))
				{
					state.setState( aState.WAIT_FOR_MODEL);
				ModelCapabilityClient modelClient = new ModelCapabilityClient(
						resourceId);
				RequestModelResponse reqModel = modelClient.requestModel(true);
				
				
				return reqModel.getResourceModel();
				
				}
			} catch (ArchiveException e) {
				System.err.print("************ Archive exeption \n" + e);

			} catch (CapabilityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	/**
	 * Create a resource model XML document
	 * 
	 * @param IResourceModel
	 * 
	 */
	public void archiveModel(IResourceModel model) throws ArchiveException {
		 if (state.getState() == aState.WAIT_FOR_MODEL) {
	 
		if (!init){
			
			try {
							
				init();
				archiveIdentifier=getResourceName(resourceId);
				initializeArchiveCapability(archiveIdentifier);
		    	init=true;
			    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    	}
			
		}
		
		String string = model.toXML();

		try {
			archiveserializer.addNewEelment(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state.setState(aState.READY);
		 }
	}

	/**
	 * This method initialize the archive capability
	 * 
	 * @param resourceID
	 * @throws Exception
	 */
	public void initializeArchiveCapability(String resourceID) throws Exception {

		
		archiveserializer = new ArchiveSerializer(resourceID);
		
	
	}
	
	
	/**
	 * This method is waiting for message get model response
	 * When message is received, this method call the archive model method
	 * 
	 */
	@Override
	public void handleMessage(ICapabilityMessage message) throws JMSException {
		IResourceModel model = ((ModelResponseMessage) message).getModel();

		 if (state.getState() == aState.WAIT_FOR_MODEL) {
			 try {
				archiveModel(model);
			} catch (ArchiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 timer.cancel();
			}
		
			state.setState(aState.READY);
		
		}
	}
	
	public String getResourceName(String resourceID)  {
	
	   String resourceName =null;
		Map<String, IResourceRepository> map = resourceManager.getResourceRepositories();
		for (String str:map.keySet()){
			
			IResourceRepository resourceRepository = map.get(str);
			for (IResource resource:resourceRepository.listResources()){
				
				if (resource.getResourceIdentifier().getId().equals(resourceID)){
					resourceName=resource.getResourceDescriptor().getInformation().getName();
			
				}
			}
		}
		return resourceName;
	}

	
	public void init() throws Exception {
		ServiceReference resourceServiceRef = Activator.getContext().getServiceReference(IResourceManager.class.getName());
		resourceManager = (ResourceManager)Activator.getContext().getService(resourceServiceRef);


		
		if (Activator.getContext()==null){
			throw new Exception("Activator.getContext() is null");
		}
		if (resourceManager==null){
			throw new Exception("resourceManager is null");
		}
		
	}
	

}
