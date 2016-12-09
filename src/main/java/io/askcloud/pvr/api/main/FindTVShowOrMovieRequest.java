/**
 * 
 */
package io.askcloud.pvr.api.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.askcloud.pvr.api.HTPC;
import io.askcloud.pvr.api.kodi.KodiMovieDownloader;
import io.askcloud.pvr.api.kodi.KodiTVShowDownloader;
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
 * @author ufctester
 *
 */
public class FindTVShowOrMovieRequest extends PlexRequest {

	private static Set<String> findMovies = new LinkedHashSet<String>();
	
	private static Set<String> findTVShows = new LinkedHashSet<String>();
	
    static
    {
    	findMovies = new LinkedHashSet<String>();
    	findMovies.add("National Lampoon's Christmas Vacation");
    	findMovies.add("The Santa Clause 3");
    	findMovies.add("Home Alone 2");
    	findMovies.add("Home Alone 3");
    	findMovies.add("Ernest Saves Christmas");
    	findMovies.add("Frosty the Snowman");    	    	
    	findMovies.add("Santa with Muscles");
    	findMovies.add("Rudolph, the Red-Nosed Reindeer");
    	findMovies.add("Trading Places");
    	findMovies.add("Arthur Christmas");
    	findMovies.add("While You Were Sleeping");
    	findMovies.add("Mixed Nuts");
    	findMovies.add("The Ref");
    	findMovies.add("The Family Stone");
    	findMovies.add("One Magic Christmas");
    	findMovies.add("Just Friends");
    	findMovies.add("The Christmas That Almost Wasn’t");
    	findMovies.add("Reindeer Games");
    }
   
    static
    {
    	findTVShows = new LinkedHashSet<String>();
    	findTVShows.add("Bates Motel");
    	findTVShows.add("Designated Survivor");
    	findTVShows.add("Lethal Weapon");
    	findTVShows.add("Bull");
    }
    	
	/**
	 * 
	 */
	public FindTVShowOrMovieRequest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FindTVShowOrMovieRequest request = new FindTVShowOrMovieRequest();
		request.run();
	}
	
	@Override
	void run() {
		findTVShows();
		
		//exit the java program
		System.exit(0);
	}
	
	
	/**
	 * @param args
	 */
	public List<KodiTVShowDownloader> findTVShows() {
		
		List<KodiTVShowDownloader> downloads = new ArrayList<KodiTVShowDownloader>();
		
		for (Iterator iterator = findTVShows.iterator(); iterator.hasNext();) {
			String tvShow = (String) iterator.next();
			Series tvshowInfo = findTVShow(tvShow);
			if(tvshowInfo != null)
			{
				downloads.add(new KodiTVShowDownloader(tvShow, null,tvshowInfo.getId(),1,1,false));	
			}
		}
		
		System.out.println("==========================================================================");
		for (Iterator iterator = downloads.iterator(); iterator.hasNext();) {
			KodiTVShowDownloader kodiExodusTVShowDownloader = (KodiTVShowDownloader) iterator.next();
			
			//System.out.println("IMDBID: " + kodiExodusMovieDownloader.getImdbID() + " movieName: " + kodiExodusMovieDownloader.getMovieName());
			System.out.println(kodiExodusTVShowDownloader.getTVDBID() + "," + kodiExodusTVShowDownloader.getShowName());
		}
		
		return downloads;
		
		

	}	
	
	
	private Series findTVShow(String name)
	{
		try {
			TheTVDBApi api = HTPC.getInstance().getTvdbAPI();
			List<Series> seriesList = api.searchSeries(name, null);
			for (Iterator iterator = seriesList.iterator(); iterator.hasNext();) {
				Series series = (Series) iterator.next();
				
//				Banners banners = api.getBanners(series.getId());
//				for (Iterator<Banner> bannersIter = banners.getPosterList().iterator(); bannersIter.hasNext();) {
//					Banner banner = bannersIter.next();
//					System.out.println("banner: " + banner.getUrl());
//					
//				}
//				System.out.println("seriesName: " + series.getSeriesName() + " id: " + series.getId());
//				List<Episode> episodes = api.getAllEpisodes(series.getId(), null);
//				for (Iterator iter2 = episodes.iterator(); iter2.hasNext();) {
//					Episode episode = (Episode) iter2.next();
//					//System.out.println("    " + episode.toString());
//				}
				if(series.getSeriesName().toLowerCase().startsWith(name.toLowerCase()))
				{
					return series;
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
	 * @return
	 */
	private List<KodiMovieDownloader> findMovies()
	{
		List<KodiMovieDownloader> downloads = new ArrayList<KodiMovieDownloader>();
		
		for (Iterator iterator = findMovies.iterator(); iterator.hasNext();) {
			String movieName = (String) iterator.next();
			MovieInfo movieInfo = findMovie(movieName);
			if(movieInfo != null)
			{
				downloads.add(new KodiMovieDownloader(movieName, movieInfo.getImdbID()));	
			}
		}
		
		System.out.println("==========================================================================");
		for (Iterator iterator = downloads.iterator(); iterator.hasNext();) {
			KodiMovieDownloader kodiExodusMovieDownloader = (KodiMovieDownloader) iterator.next();
			
			//System.out.println("IMDBID: " + kodiExodusMovieDownloader.getImdbID() + " movieName: " + kodiExodusMovieDownloader.getMovieName());
			System.out.println(kodiExodusMovieDownloader.getImdbID() + "," + kodiExodusMovieDownloader.getMovieName());
		}
		
		return downloads;
	}
	
	private MovieInfo findMovie(String movieName)
	{
		
		try {
			TheMovieDbApi api = HTPC.getInstance().getTheMovieDbAPI();
			ResultList<MovieInfo> movieResults = api.searchMovie(movieName, 0, "", null, 0, 0, SearchType.PHRASE);
			List<MovieInfo> movies = movieResults.getResults();
			for (Iterator<MovieInfo> iterator = movies.iterator(); iterator.hasNext();) {
				MovieInfo movieInfo = iterator.next();
			
				MovieInfo movieDetailedInfo = api.getMovieInfo(movieInfo.getId(), null, movieDBAppendToResponseBuilder(MovieMethod.class));
				System.out.println("imdb id: " + movieDetailedInfo.getImdbID() + " moviedbId: " + movieDetailedInfo.getId() + " title: " + movieDetailedInfo.getTitle());
				
				String movieShowTitle=movieDetailedInfo.getTitle();
				if(movieShowTitle == null)
				{
					continue;
				}
				if(movieName.equalsIgnoreCase(movieShowTitle))
				{
					return movieDetailedInfo;
				}
				else if(movieShowTitle.startsWith(movieName))
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
    protected static <T extends AppendToResponseMethod> String movieDBAppendToResponseBuilder(Class<T> methodClass) {
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
