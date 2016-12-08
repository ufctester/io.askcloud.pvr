package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.HTPC;

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
		HTPC.getInstance().getKodiManager().downloadMoviesQueue();;
	}
}
