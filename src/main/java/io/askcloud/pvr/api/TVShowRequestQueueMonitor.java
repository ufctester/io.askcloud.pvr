package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class TVShowRequestQueueMonitor extends TVShowRequest {
	
	//private String directory =  "C:\\Entertainment\\TVShows";
	private String directory =  "C:\\Entertainment\\TVShows";
			
	public TVShowRequestQueueMonitor() {
		super();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		TVShowRequestQueueMonitor missingEpisodes = new TVShowRequestQueueMonitor();
		missingEpisodes.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().findMissingEpisodes(directory));
	}
}
