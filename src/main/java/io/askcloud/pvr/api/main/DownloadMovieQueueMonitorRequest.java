package io.askcloud.pvr.api.main;

import io.askcloud.pvr.api.HTPC;

public class DownloadMovieQueueMonitorRequest extends PlexRequest {

	public DownloadMovieQueueMonitorRequest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		DownloadMovieQueueMonitorRequest requestMovie = new DownloadMovieQueueMonitorRequest();
		requestMovie.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		HTPC.getInstance().getKodiManager().downloadMoviesQueue();
		
		//exit the java program
		System.exit(0);
	}
}
