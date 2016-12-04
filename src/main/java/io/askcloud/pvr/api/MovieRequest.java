package io.askcloud.pvr.api;

public class MovieRequest extends PlexRequest {

	public MovieRequest() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		//PlexPVRManager.getInstance().getNextAvailableTVShowDownloaderThread("Brooklyn Nine-Nine", "tt2467372", 2, 1, 2);

        //PlexPVRManager.getInstance().getNextAvailableTVShowDownloaderThread("Brooklyn Nine-Nine", "tt2467372", 2, 1, 2).downloadTVShows();

		//PlexPVRManager.getInstance().getKodiManager().downloadMovieFromKodiExodus("The Doors","tt0101761");		
	}
	
	@Override
	void run() {
		// TODO Auto-generated method stub
		
	}
}
