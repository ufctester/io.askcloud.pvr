package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.HTPC;

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

		HTPC.getInstance().findCompletedEpisodes(HTPC.PLEX_TVSHOWS_DIR);		
	}

}
