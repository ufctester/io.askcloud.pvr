package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class DownloadTVShowEpisodesMissingRequest extends DownloadTVShowRequest {
	
	
			
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
		PlexPVRManager.CLEAN_KODI_DOWNLOAD=true;
		PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing());
	}
}
