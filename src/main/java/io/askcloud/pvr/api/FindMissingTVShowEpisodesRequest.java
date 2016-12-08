package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author ufctester
 *
 */
public class FindMissingTVShowEpisodesRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public FindMissingTVShowEpisodesRequest() {
		super();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FindMissingTVShowEpisodesRequest episodes = new FindMissingTVShowEpisodesRequest();
		episodes.run();
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().findMissingTVShowEpisodes(PlexPVRManager.PLEX_TVSHOWS_DIR);		
	}

}
