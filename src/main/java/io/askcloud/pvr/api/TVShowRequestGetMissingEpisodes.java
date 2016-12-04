package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class TVShowRequestGetMissingEpisodes extends TVShowRequest {
	
	//private String directory =  "C:\\Entertainment\\TVShows";
	private String directory =  "C:\\Entertainment\\TVShows";
			
	public TVShowRequestGetMissingEpisodes() {
		super();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		TVShowRequestGetMissingEpisodes missingEpisodes = new TVShowRequestGetMissingEpisodes();
		missingEpisodes.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().findMissingEpisodes(directory));
	}
}
