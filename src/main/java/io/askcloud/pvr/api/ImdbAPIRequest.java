/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import io.askcloud.pvr.imdb.ImdbApi;
import io.askcloud.pvr.imdb.model.ImdbMovieDetails;
import io.askcloud.pvr.imdb.model.ImdbPerson;
import io.askcloud.pvr.imdb.search.SearchObject;
import io.askcloud.pvr.tvdb.TheTVDBApi;
import io.askcloud.pvr.tvdb.model.Banner;
import io.askcloud.pvr.tvdb.model.Banners;
import io.askcloud.pvr.tvdb.model.Episode;
import io.askcloud.pvr.tvdb.model.Series;

/**
 * @author apps
 *
 */
public class ImdbAPIRequest extends PlexRequest {

	
	/**
	 * 
	 */
	public ImdbAPIRequest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImdbApi api = new ImdbApi();
		try {
			String name = "The Doors";
			Map<String, List<SearchObject>> seriesList = api.getSearch(name);
			for (Iterator<String> iterator = seriesList.keySet().iterator(); iterator.hasNext();) {
				String series = iterator.next();
				List<SearchObject> searchObects = seriesList.get(series);
				
				for (Iterator searchIter = searchObects.iterator(); searchIter.hasNext();) {
					SearchObject searchObject = (SearchObject) searchIter.next();
					if(searchObject instanceof ImdbPerson)
					{
						ImdbPerson imdbPerson = (ImdbPerson)searchObject;
						//System.out.println("IMDB Person: " + imdbPerson.getName() + " id: " + imdbPerson.getActorId());
					}
					else
					{
						ImdbMovieDetails imdbDetails = (ImdbMovieDetails)searchObject;
						if(name.equals(imdbDetails.getTitle()))
						{
							System.out.println("seriesName: " + imdbDetails.getTitle() + " id: " + imdbDetails.getImdbId());
						}
					}
					//if(name.equals(imdbDetails.getTitle()))
					//{
						
					//}
					
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
