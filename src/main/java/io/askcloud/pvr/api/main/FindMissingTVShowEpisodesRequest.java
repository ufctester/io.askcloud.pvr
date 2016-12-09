package io.askcloud.pvr.api.main;

import io.askcloud.pvr.api.HTPC;

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
		HTPC.getInstance().findMissingTVShowEpisodes(HTPC.PLEX_TVSHOWS_DIR);	
		
		//exit the java program
		System.exit(0);
	}

}
