/**
 * 
 */
package io.askcloud.pvr.api;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

/**
 * @author apps
 *
 */
public class MissingEpisodes extends PlexRequest {

	
	/**
	 * 
	 */
	public MissingEpisodes() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlexPVRManager.getInstance().findMissingEpisodes("C:/Entertainment/TVShows/American Dad");
//		List<String> episodes = FileBotManager.getInstance().getTVEpisodes("24");
//		for (Iterator iterator = episodes.iterator(); iterator.hasNext();) {
//			String episode = (String) iterator.next();
//			
//		}	
	}
	
	@Override
	void run() {
		// TODO Auto-generated method stub
		
	}

}
