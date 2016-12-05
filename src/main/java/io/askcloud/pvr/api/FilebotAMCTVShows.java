/**
 * 
 */
package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author ufctester
 *
 */
public class FilebotAMCTVShows extends PlexRequest {

	
	/**
	 * 
	 */
	public FilebotAMCTVShows() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FilebotAMCTVShows amc = new FilebotAMCTVShows();
		amc.run();
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().automatedMediaCenter(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR,PlexPVRManager.FILEBOT_AMC_DESTINATION);
		PlexPVRManager.getInstance().automatedMediaCenter(PlexPVRManager.KODI_DOWNLOAD_MOVIES_DIR,PlexPVRManager.FILEBOT_AMC_DESTINATION);
	}

}
