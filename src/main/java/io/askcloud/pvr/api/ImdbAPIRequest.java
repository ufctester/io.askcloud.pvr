/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import io.askcloud.pvr.api.pvr.PlexPVRManager;
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
 * @author UFCTester
 *
 */
public class ImdbAPIRequest extends PlexRequest {

	private static Set<String> findMovies = new LinkedHashSet<String>();
	ImdbApi imdbAPI = new ImdbApi();
	
    static
    {
    	findMovies = new LinkedHashSet<String>();
    	findMovies.add("A Christmas Story");
    	findMovies.add("Love Actually");
    	findMovies.add("Scrooged");
    	findMovies.add("Elf");
    	findMovies.add("Home Alone");
    	findMovies.add("Miracle on 34th Street");
    	findMovies.add("The Nightmare Before Christmas");
    	findMovies.add("The Muppet Christmas Carol");
    	findMovies.add("Christmas Vacation");
    	findMovies.add("Bad Santa");
    	findMovies.add("The Santa Clause");
    	findMovies.add("The Santa Clause 2");
    	findMovies.add("The Santa Clause 3");
    	findMovies.add("Gremlins");
    	findMovies.add("A Charlie Brown Christmas");
    	findMovies.add("The Polar Express");
    	findMovies.add("Jingle All the Way");
    	findMovies.add("How the Grinch Stole Christmas");    	
    	findMovies.add("Christmas with the Kranks");
    	findMovies.add("The Holiday");
    	findMovies.add("Arthur Christmas");
    	findMovies.add("Home Alone 2");
    	findMovies.add("Ernest Saves Christmas");
    	findMovies.add("Fred Claus");
    	findMovies.add("Happy Christmas");
    	findMovies.add("Mickey's Christmas Carol");    	
    }
    
    
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
		ImdbAPIRequest imdbAPI = new ImdbAPIRequest();
		PlexPVRManager.getInstance();
		imdbAPI.run();
	}
	
	@Override
	void run() {
		for (Iterator iterator = findMovies.iterator(); iterator.hasNext();) {
			String movie = (String) iterator.next();
			findMovie(movie);
		}
	}
	
	private void findMovie(String movieName)
	{
		try {
			Map<String, List<SearchObject>> seriesList = imdbAPI.getSearch(movieName);
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
						if(movieName.equals(imdbDetails.getTitle()))
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
	}
}
