/**
 * 
 */
package io.askcloud.pvr.api.htpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
import io.askcloud.pvr.tvdb.model.Series;

/**
 * @author ufctester
 *
 */
public class HTPCFactory {

	private static final String CLASS_NAME = HTPCFactory.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);
	
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
   
    
    public static void main(String[] args) {
    	 Options options = new Options();
    	 options.addOption("h", "help", false, "show help.");
         options.addOption("a", "amc", false, "Run Filebot AMC");
         options.addOption("t", "tvqueue", false, "Download TV Shows from TV Queue");
         options.addOption("e", "tvmissing", false, "Download TV Shows from Series Episode Missing");
         options.addOption("m", "moviequeue", false, "Download Movie from Movie Queue");
         options.addOption("f", "find", false, "Find TV Shows or Movies from TVDB and TMDB");
         options.addOption("s", "findmissingepisodes", false, "Find searies episodes which are missing with Filebot");

         CommandLineParser parser = new DefaultParser();
         CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h"))
				help(options);
			if (cmd.hasOption("v")) {
				LOG.info("Using cli argument -v=" + cmd.getOptionValue("v"));

				// Whatever you want to do with the setting goes here
			}
			else {
				LOG.severe("MIssing v option");
				help(options);
			}
		}
		catch (ParseException e) {
			LOG.severe("Failed to parse comand line properties: exception: " + e.getMessage());
			help(options);
		}
         
	}
    
	 private static void help(Options options) {
		 // This prints out some help

	}
	 

	private static HTPCFactory eINSTANCE;
	
	public static HTPCFactory getInstance() {
		if (eINSTANCE == null) {
			eINSTANCE = new HTPCFactory();
		}
		return eINSTANCE;
	}
	
	/**
	 * 
	 */
	private HTPCFactory() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Start the TVShow Download Queue Request
	 */
	public void runDonwloadTVShowQueueMonitorRequest()
	{		
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		HTPC.getInstance().getKodiManager().downloadSeriesEpisodeQueue();
		
		//exit the java program
		System.exit(0);		
	}
	
	/**
	 * Start the Movie Download Queue Request
	 */
	public void runDownloadMovieQueueMonitorRequest()
	{
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		HTPC.getInstance().getKodiManager().downloadMoviesQueue();
		
		//exit the java program
		System.exit(0);
	}
	
	/**
	 * Start the TVShow Download for Missing Episodes
	 */
	public void runDownloadTVShowEpisodesMissingRequest()
	{
		HTPC.getInstance();
		LOG.entering(CLASS_NAME, "run");
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		LOG.info("Calling: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		HTPC.getInstance().getKodiManager().downloadSeriesEpisodeMissing();
		LOG.info("Completd: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		LOG.entering(CLASS_NAME, "run");
		
		//exit the java program
		System.exit(0);	
	}
	
	
	/**
	 * Start Filebot AMC Request
	 */
	public void runFilebotAMCRequest()
	{
		//re-create the target directory
		HTPC.getInstance().recreateDirectory(HTPC.FILEBOT_AMC_DESTINATION);
		HTPC.getInstance().automatedMediaCenter(HTPC.KODI_DOWNLOAD_BASE_DIR,HTPC.FILEBOT_AMC_DESTINATION);
		
		//Don't exit because Filebot runs on another thread FileBotExecuteResultHandler which will exit when it is done
		//System.exit(0);
	}
	
	/**
	 * Find Complete TVShow Episode Request
	 */
	public void runFindCompletedTVShowEpisodesRequest()
	{
		HTPC.getInstance().findCompletedEpisodes(HTPC.PLEX_TVSHOWS_DIR);		
		
		//Don't exit because Filebot runs on another thread FileBotExecuteResultHandler which will exit when it is done
		//System.exit(0);
	}
	
	/**
	 * 
	 */
	public void runFindHaveTVShowEpisodesRequest()
	{
		HTPC.getInstance().findTVShowEpisodesHave(HTPC.PLEX_TVSHOWS_DIR);		
		
		//Don't exit because Filebot runs on another thread FileBotExecuteResultHandler which will exit when it is done
		//System.exit(0);
	}
	
	/**
	 * Find missing TVShow Episode Request
	 */
	public void runFindMissingTVShowEpisodesRequest()
	{
		HTPC.getInstance().findMissingTVShowEpisodes(HTPC.PLEX_TVSHOWS_DIR);	
		//Don't exit because Filebot runs on another thread FileBotExecuteResultHandler which will exit when it is done
		//System.exit(0);
	}
	
	/**
	 * Find missing TVShow Episode Request
	 */
	public void runTestRequest()
	{
		HTPC.getInstance();
		LOG.entering(CLASS_NAME, "runTestRequest");
		HTPC.CLEAN_KODI_DOWNLOAD=true;
		LOG.info("Calling: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		HTPC.getInstance().getKodiManager().downloadSeriesEpisodeMissing();
		LOG.info("Completd: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
		LOG.exiting(CLASS_NAME, "runTestRequest");
		System.exit(0);
	}	
	
	/**
	 * Search for TVShow or Movie Request
	 */
	public void runFindTVShowOrMovieRequest()
	{
		findTVShows();
		
		//exit the java program
		System.exit(0);	
	}

	
	/**
	 * @param args
	 */
	private List<KodiTVShowDownloader> findTVShows() {
		
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
    private <T extends AppendToResponseMethod> String movieDBAppendToResponseBuilder(Class<T> methodClass) {
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
