package io.askcloud.pvr.api;

import java.util.ArrayList;
import java.util.List;

import io.askcloud.pvr.api.pvr.KodiExodusDownloader;
import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class TVShowRequest extends PlexRequest {
				
	public TVShowRequest() {
		super();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		TVShowRequest missingEpisodes = new TVShowRequest();
		missingEpisodes.run();
		
		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		List<KodiExodusDownloader> missingEpisodes = new ArrayList<KodiExodusDownloader>();
		missingEpisodes.add(PlexPVRManager.getInstance().createMissingEpisode("73141",null, "American Dad", "3", "17",false));
//		missingEpisodes.add(FileBotManager.getInstance().createMissingEpisode("269586", "Brooklyn Nine-Nine", "1", "2"));
//		missingEpisodes.add(FileBotManager.getInstance().createMissingEpisode("269586", "Brooklyn Nine-Nine", "1", "3"));
//		missingEpisodes.add(FileBotManager.getInstance().createMissingEpisode("269586", "Brooklyn Nine-Nine", "1", "4"));
		
		PlexPVRManager.getInstance().getKodiManager().download(missingEpisodes);
	}
}
