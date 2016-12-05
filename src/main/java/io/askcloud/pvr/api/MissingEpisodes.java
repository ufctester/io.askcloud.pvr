package io.askcloud.pvr.api;

import org.apache.commons.lang3.StringUtils;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author ufctester
 *
 */
public class MissingEpisodes extends PlexRequest {

	
	/**
	 * 
	 */
	public MissingEpisodes() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MissingEpisodes episodes = new MissingEpisodes();
		episodes.run();
	}
	
	@Override
	void run() {

		//PlexPVRManager.getInstance().findMissingEpisodes(PlexPVRManager.PLEX_TVSHOWS_DIR);
		PlexPVRManager.getInstance().findCompletedEpisodes(PlexPVRManager.PLEX_TVSHOWS_DIR);
//		List<String> episodes = FileBotManager.getInstance().getTVEpisodes("24");
//		for (Iterator iterator = episodes.iterator(); iterator.hasNext();) {
//			String episode = (String) iterator.next();
//			
//		}	
		
	}

}
