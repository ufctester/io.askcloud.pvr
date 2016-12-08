package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.HTPC;

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
	}
}
