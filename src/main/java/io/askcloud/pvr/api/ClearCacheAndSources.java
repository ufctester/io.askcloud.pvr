/**
 * 
 */
package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author apps
 *
 */
public class ClearCacheAndSources {

	/**
	 * 
	 */
	public ClearCacheAndSources() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlexPVRManager.getInstance().getKodiManager().clearCacheAndSources();

	}

}
