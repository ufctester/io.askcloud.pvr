/**
 * 
 */
package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author apps
 *
 */
public class FilebotAMC extends PlexRequest {

	
	/**
	 * 
	 */
	public FilebotAMC() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlexPVRManager.getInstance().automatedMediaCenter(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR,PlexPVRManager.FILEBOT_AMC_DESTINATION);
//		List<String> episodes = FileBotManager.getInstance().getTVEpisodes("24");
//		for (Iterator iterator = episodes.iterator(); iterator.hasNext();) {
//			String episode = (String) iterator.next();
//			
//		}	
	}
	
	@Override
	void run() {
		// TODO Auto-generated method stub
		
	}

}
