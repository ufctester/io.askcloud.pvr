package io.askcloud.pvr.api.main;

import java.util.logging.Logger;

import io.askcloud.pvr.api.HTPC;

public class DownloadTVShowEpisodesMissingRequest extends DownloadTVShowRequest {
	
	private static final String CLASS_NAME = DownloadTVShowEpisodesMissingRequest.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);
			
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
		HTPC.getInstance();
		LOG.entering(CLASS_NAME, "run");
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		LOG.info("Calling: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		HTPC.getInstance().getKodiManager().downloadSeriesEpisodeMissing();
		LOG.info("Completd: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		LOG.entering(CLASS_NAME, "run");
		
		//exit the java program
		System.exit(0);
	}
}
