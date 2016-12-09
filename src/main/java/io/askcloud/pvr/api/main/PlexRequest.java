package io.askcloud.pvr.api.main;

import java.util.logging.Logger;

abstract public class PlexRequest {
	
	private static final String CLASS_NAME = PlexRequest.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);
	
	
	public PlexRequest() {
		super();
	}
	
	abstract void run();
}
