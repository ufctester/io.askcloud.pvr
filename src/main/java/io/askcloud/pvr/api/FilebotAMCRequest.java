/**
 * 
 */
package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author ufctester
 *
 */
public class FilebotAMCRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public FilebotAMCRequest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FilebotAMCRequest amc = new FilebotAMCRequest();
		amc.run();
	}
	
	@Override
	void run() {
		//re-create the target directory
		PlexPVRManager.getInstance().recreateDirectory(PlexPVRManager.FILEBOT_AMC_DESTINATION);
		PlexPVRManager.getInstance().automatedMediaCenter(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR,PlexPVRManager.FILEBOT_AMC_DESTINATION);
		PlexPVRManager.getInstance().automatedMediaCenter(PlexPVRManager.KODI_DOWNLOAD_MOVIES_DIR,PlexPVRManager.FILEBOT_AMC_DESTINATION);
	}

}
