package io.askcloud.pvr.api.main;

import io.askcloud.pvr.api.HTPC;

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
		HTPC.getInstance().findTVShowEpisodesHave(HTPC.PLEX_TVSHOWS_DIR);		
		
		//exit the java program
		System.exit(0);
	}

}
