package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author ufctester
 *
 */
public class FindHaveTVShowEpisodesRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public FindHaveTVShowEpisodesRequest() {
		super();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FindHaveTVShowEpisodesRequest episodes = new FindHaveTVShowEpisodesRequest();
		episodes.run();
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().findTVShowEpisodesHave(PlexPVRManager.PLEX_TVSHOWS_DIR);		
	}

}
