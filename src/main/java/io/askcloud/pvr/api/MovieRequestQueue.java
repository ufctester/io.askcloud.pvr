package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class MovieRequestQueue extends PlexRequest {

	public MovieRequestQueue() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		MovieRequestQueue requestMovie = new MovieRequestQueue();
		requestMovie.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadMovieQueue());
	}
}
