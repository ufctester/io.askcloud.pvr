package io.askcloud.pvr.api;

import java.util.logging.Logger;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

abstract public class PlexRequest {
	
	protected static Logger log = PlexPVRManager.getInstance().getLogger();
	
	public PlexRequest() {
		super();
	}
	
	abstract void run();
}
