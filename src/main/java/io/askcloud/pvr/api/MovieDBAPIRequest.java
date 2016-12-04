/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.askcloud.pvr.api.pvr.KodiExodusMovieDownloader;
import io.askcloud.pvr.imdb.ImdbApi;
import io.askcloud.pvr.themoviedb.AppendToResponseBuilder;
import io.askcloud.pvr.themoviedb.TheMovieDbApi;
import io.askcloud.pvr.themoviedb.enumeration.MovieMethod;
import io.askcloud.pvr.themoviedb.enumeration.SearchType;
import io.askcloud.pvr.themoviedb.interfaces.AppendToResponseMethod;
import io.askcloud.pvr.themoviedb.model.movie.MovieInfo;
import io.askcloud.pvr.themoviedb.results.ResultList;
import io.askcloud.pvr.tvdb.TheTVDBApi;
import io.askcloud.pvr.tvdb.model.Banner;
import io.askcloud.pvr.tvdb.model.Banners;
import io.askcloud.pvr.tvdb.model.Episode;
import io.askcloud.pvr.tvdb.model.Series;

/**
 * @author apps
 *
 */
public class MovieDBAPIRequest extends PlexRequest {

	private static Set<String> findMovies = new LinkedHashSet<String>();
	
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
	public MovieDBAPIRequest() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MovieDBAPIRequest request = new MovieDBAPIRequest();
		request.run();
	}
	

	@Override
	void run() {
		List<KodiExodusMovieDownloader> downloads = new ArrayList<KodiExodusMovieDownloader>();
		
		for (Iterator iterator = findMovies.iterator(); iterator.hasNext();) {
			String movieName = (String) iterator.next();
			MovieInfo movieInfo = findMovie(movieName);
			if(movieInfo != null)
			{
				downloads.add(new KodiExodusMovieDownloader(movieName, movieInfo.getImdbID()));	
			}
		}
		
		System.out.println("==========================================================================");
		for (Iterator iterator = downloads.iterator(); iterator.hasNext();) {
			KodiExodusMovieDownloader kodiExodusMovieDownloader = (KodiExodusMovieDownloader) iterator.next();
			
			//System.out.println("IMDBID: " + kodiExodusMovieDownloader.getImdbID() + " movieName: " + kodiExodusMovieDownloader.getMovieName());
			System.out.println(kodiExodusMovieDownloader.getImdbID() + "," + kodiExodusMovieDownloader.getMovieName());
		}
	}
	
	private MovieInfo findMovie(String movieName)
	{
		
		try {
			TheMovieDbApi api = new TheMovieDbApi("db6327125f93ae90aa51493f1586713f");
			ResultList<MovieInfo> movieResults = api.searchMovie(movieName, 0, "", null, 0, 0, SearchType.PHRASE);
			List<MovieInfo> movies = movieResults.getResults();
			for (Iterator<MovieInfo> iterator = movies.iterator(); iterator.hasNext();) {
				MovieInfo movieInfo = iterator.next();
			
				MovieInfo movieDetailedInfo = api.getMovieInfo(movieInfo.getId(), null, appendToResponseBuilder(MovieMethod.class));
				System.out.println("imdb id: " + movieDetailedInfo.getImdbID() + " moviedbId: " + movieDetailedInfo.getId() + " title: " + movieDetailedInfo.getTitle());
				
				if(movieName.equalsIgnoreCase(movieDetailedInfo.getTitle()))
				{
					return movieDetailedInfo;
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
		
		return null;
	}

    /**
     * Build up a full list of the AppendToResponse methods into a string for
     * use in the URL
     *
     * @param <T>
     * @param methodClass
     * @return
     */
    protected static <T extends AppendToResponseMethod> String appendToResponseBuilder(Class<T> methodClass) {
        boolean first = true;
        StringBuilder atr = new StringBuilder();
        for (AppendToResponseMethod method : methodClass.getEnumConstants()) {
            if (first) {
                first = false;
            } else {
                atr.append(",");
            }
            atr.append(method.getPropertyString());
        }
        return atr.toString();
    }	
	
}
