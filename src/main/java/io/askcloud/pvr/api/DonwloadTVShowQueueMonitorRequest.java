package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class DonwloadTVShowQueueMonitorRequest extends DownloadTVShowRequest {
	
	//private String directory =  "C:\\Entertainment\\TVShows";
	private String directory =  "C:\\Entertainment\\TVShows";
			
	public DonwloadTVShowQueueMonitorRequest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		DonwloadTVShowQueueMonitorRequest missingEpisodes = new DonwloadTVShowQueueMonitorRequest();
		missingEpisodes.run();	
	}
	
	@Override
	void run() {
		
		PlexPVRManager.CLEAN_KODI_DOWNLOAD=true;
		PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowQueue());
	}
}
