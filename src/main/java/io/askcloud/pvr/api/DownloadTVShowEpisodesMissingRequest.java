package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.HTPC;

public class DownloadTVShowEpisodesMissingRequest extends DownloadTVShowRequest {
	
	private static String CLASS_NAME = DownloadTVShowEpisodesMissingRequest.class.getName();	
			
	public DownloadTVShowEpisodesMissingRequest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		DownloadTVShowEpisodesMissingRequest missingEpisodes = new DownloadTVShowEpisodesMissingRequest();
		missingEpisodes.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		log.entering(CLASS_NAME, "run");
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		log.info("Calling: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		HTPC.getInstance().getKodiManager().downloadSeriesEpisodeMissing();
		log.info("Completd: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		log.entering(CLASS_NAME, "run");
	}
}
