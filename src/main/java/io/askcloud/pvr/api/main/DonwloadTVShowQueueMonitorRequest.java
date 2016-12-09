package io.askcloud.pvr.api.main;

import io.askcloud.pvr.api.HTPC;

public class DonwloadTVShowQueueMonitorRequest extends DownloadTVShowRequest {
	
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
		
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		HTPC.getInstance().getKodiManager().downloadSeriesEpisodeQueue();
		
		//exit the java program
		System.exit(0);
	}
}
