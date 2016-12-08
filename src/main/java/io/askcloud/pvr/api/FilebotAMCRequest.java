/**
 * 
 */
package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.HTPC;

/**
 * @author ufctester
 *
 */
public class FilebotAMCRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public FilebotAMCRequest() {
		super();
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
		HTPC.getInstance().recreateDirectory(HTPC.FILEBOT_AMC_DESTINATION);
		HTPC.getInstance().automatedMediaCenter(HTPC.KODI_DOWNLOAD_TVSHOWS_DIR,HTPC.FILEBOT_AMC_DESTINATION);
		HTPC.getInstance().automatedMediaCenter(HTPC.KODI_DOWNLOAD_MOVIES_DIR,HTPC.FILEBOT_AMC_DESTINATION);
	}

}
