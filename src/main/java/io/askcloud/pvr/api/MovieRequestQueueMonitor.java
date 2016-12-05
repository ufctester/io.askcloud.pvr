package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class MovieRequestQueueMonitor extends PlexRequest {

	public MovieRequestQueueMonitor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		MovieRequestQueueMonitor requestMovie = new MovieRequestQueueMonitor();
		requestMovie.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadMovieQueue());
	}
}
