/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.Iterator;
import java.util.List;

import io.askcloud.pvr.tvdb.TheTVDBApi;
import io.askcloud.pvr.tvdb.model.Banner;
import io.askcloud.pvr.tvdb.model.Banners;
import io.askcloud.pvr.tvdb.model.Episode;
import io.askcloud.pvr.tvdb.model.Series;

/**
 * @author apps
 *
 */
public class TVDBAPIRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public TVDBAPIRequest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TheTVDBApi api = new TheTVDBApi("0E6F56E34D3CC366");
		try {
			List<Series> seriesList = api.searchSeries("24", null);
			for (Iterator iterator = seriesList.iterator(); iterator.hasNext();) {
				Series series = (Series) iterator.next();
				
				Banners banners = api.getBanners(series.getId());
				for (Iterator<Banner> bannersIter = banners.getPosterList().iterator(); bannersIter.hasNext();) {
					Banner banner = bannersIter.next();
					System.out.println("banner: " + banner.getUrl());
					
				}
				System.out.println("seriesName: " + series.getSeriesName() + " id: " + series.getId());
				List<Episode> episodes = api.getAllEpisodes(series.getId(), null);
				for (Iterator iter2 = episodes.iterator(); iter2.hasNext();) {
					Episode episode = (Episode) iter2.next();
					//System.out.println("    " + episode.toString());
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
