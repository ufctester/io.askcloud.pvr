package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author ufctester
 *
 */
public class FindCompletedTVShowEpisodesRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public FindCompletedTVShowEpisodesRequest() {
		super();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FindCompletedTVShowEpisodesRequest episodes = new FindCompletedTVShowEpisodesRequest();
		episodes.run();
	}
	
	@Override
	void run() {

		PlexPVRManager.getInstance().findCompletedEpisodes(PlexPVRManager.PLEX_TVSHOWS_DIR);		
	}

}
