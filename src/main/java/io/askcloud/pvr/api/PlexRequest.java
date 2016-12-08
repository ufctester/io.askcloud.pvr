package io.askcloud.pvr.api;

import java.util.logging.Logger;

import io.askcloud.pvr.api.pvr.HTPC;

abstract public class PlexRequest {
	
	protected static Logger log = HTPC.getInstance().getLogger();
	
	public PlexRequest() {
		super();
	}
	
	abstract void run();
}
