/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.askcloud.pvr.api.kodi.KodiDownloader;
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
public class Main {

	private static final String CLASS_NAME = Main.class.getName();
	private static final Logger LOG = HTPC.LOG;
	
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
    options.addOption("d", "download", false, "Download From Download Queue");
    options.addOption("f", "find", false, "Find TV Shows or Movies from TVDB and TMDB");
    options.addOption("s", "searchmissingepisode", false, "Find series missing episodes which Filebot");
    options.addOption("p", "plex", false, "Push downloads though AMC and over to Plex");
    options.addOption("test", false, "Test Method");

    CommandLineParser parser = new DefaultParser();
    org.apache.commons.cli.CommandLine cmd = null;

	try {
		cmd = parser.parse(options, args);
		if (cmd.hasOption("h"))
		{
			 HelpFormatter formater = new HelpFormatter();
			 formater.printHelp("Main", options);
			 HTPC.getInstance().exit(true);
		}
		else if (cmd.hasOption("a")) {
			 Main.getInstance().filebotAMCRequest();
		}
		else if (cmd.hasOption("d")) {
			 Main.getInstance().downloadFromDownloadQueue();
		}
		else if (cmd.hasOption("f")) {
			 Main.getInstance().findShow(true);
				//exit the java program
			 HTPC.getInstance().exit(true);
		}
		else if (cmd.hasOption("s")) {
			 Main.getInstance().findMissingTVShowEpisodesRequest();
		}
		else if (cmd.hasOption("p")) {
			 Main.getInstance().publishDownloadsToPlex();
		}		
		else if (cmd.hasOption("test")) {
			 Main.getInstance().runTestRequest();
		}				
		else {
			 HelpFormatter formater = new HelpFormatter();
			 formater.printHelp("Main", options);
			 HTPC.getInstance().exit(true);
		}
	}
	catch (ParseException e) {
		 LOG.severe("Failed to parse comand line properties: exception: " + e.getMessage());
		 HelpFormatter formater = new HelpFormatter();
		 formater.printHelp("Main", options);
		 HTPC.getInstance().exit(true);
	}
}    
    
	 private static void help(Options options) {
		 // This prints out some help

	}
	 

	private static Main eINSTANCE;
	
	public static Main getInstance() {
		if (eINSTANCE == null) {
			eINSTANCE = new Main();
		}
		return eINSTANCE;
	}
	
	/**
	 * 
	 */
	private Main() {
		super();
	}

	/**
	 * Start the TVShow Download for Missing Episodes
	 */
	@SuppressWarnings("static-access")
	public void downloadFromDownloadQueue()
	{
		HTPC.setCLEAN_KODI_DOWNLOAD(true);
		LOG.entering(CLASS_NAME, "downloadFromDownloadQueue");
		HTPC.getInstance().startDownloadFromDownloadQueue();
		
		LOG.info("Finished download from download queue.");
		LOG.info("Exiting Java Successful");
		LOG.exiting(CLASS_NAME, "downloadFromDownloadQueue");
		
		//exit the java program
		HTPC.getInstance().exit(true);
	}
	
	
	/**
	 * Start Filebot AMC Request
	 */
	public void filebotAMCRequest()
	{
		LOG.entering(CLASS_NAME, "filebotAMCRequest");
		//re-create the target directory
		HTPC.getInstance().runAutomatedMediaCenter(false);
		
		//File bot runs on a differnt thread and the FileBotExecuteResultHandler will exit the Java Program
		LOG.exiting(CLASS_NAME, "filebotAMCRequest");		
	}
	
	/**
	 * Find Complete TVShow Episode Request
	 */
	public void findCompletedTVShowEpisodesRequest()
	{
		LOG.entering(CLASS_NAME, "findCompletedTVShowEpisodesRequest");
		HTPC.getInstance().findCompletedEpisodes(HTPC.getPLEX_TVSHOWS_DIR());		
		
		//File bot runs on a differnt thread and the FileBotExecuteResultHandler will exit the Java Program
		LOG.exiting(CLASS_NAME, "findCompletedTVShowEpisodesRequest");		
	}
	
	/**
	 * 
	 */
	public void findHaveTVShowEpisodesRequest()
	{
		LOG.entering(CLASS_NAME, "findHaveTVShowEpisodesRequest");
		HTPC.getInstance().findTVShowEpisodesHave(HTPC.getPLEX_TVSHOWS_DIR());		
		
		//File bot runs on a differnt thread and the FileBotExecuteResultHandler will exit the Java Program
		LOG.exiting(CLASS_NAME, "findHaveTVShowEpisodesRequest");	
	}
	
	/**
	 * Find missing TVShow Episode Request
	 */
	public void findMissingTVShowEpisodesRequest()
	{
		LOG.entering(CLASS_NAME, "findMissingTVShowEpisodesRequest");
		HTPC.getInstance().findMissingTVShowEpisodes(HTPC.getPLEX_TVSHOWS_DIR());	
		//File bot runs on a differnt thread and the FileBotExecuteResultHandler will exit the Java Program
		LOG.exiting(CLASS_NAME, "findMissingTVShowEpisodesRequest");	
	}
	
	
	/**
	 * Find missing TVShow Episode Request
	 */
	public void publishDownloadsToPlex()
	{	
		HTPC.getInstance().LOG.entering(CLASS_NAME, "publishDownloadsToPlex");		
		HTPC.getInstance().publishDownloadsToPlex(HTPC.getInstance().loadDownloadQueue(HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE(),HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK()));
		LOG.exiting(CLASS_NAME, "publishDownloadsToPlex");
	}		
	
	/**
	 * Find missing TVShow Episode Request
	 */
	public void runTestRequest()
	{	
		HTPC.getInstance().test();
//		HTPC.getInstance().LOG.entering(CLASS_NAME, "runTestRequest");
//		LOG.info("Calling: PlexPVRManager.getInstance().getKodiManager().download(PlexPVRManager.getInstance().loadTVShowEpisodesMissing())");
//		
//		HTPC.getInstance().runAutomatedMediaCenter(true);
//		//HTPC.getInstance().publishDownloadsToPlex(HTPC.getInstance().loadDownloadQueue(HTPC.DOWNLOAD_QUEUE_FILE,HTPC.DOWNLOAD_QUEUE_LOCK_FILE));
//		try {
//			HTPC.getInstance().getGmailEmailClient().sendMessage("spriet@ca.ibm.com","Test Message from Gmail Account", "Hi Dave this is from my test gmail.");
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//		//Filebot puts TV Shows under a TV Show directory but we want it under TVShows
//		File amcDirectory=new File(HTPC.KODI_DOWNLOAD_COMPLETED_AMC_DIR + File.separator + "TV Shows");
//		File correctDirectory=new File(HTPC.KODI_DOWNLOAD_COMPLETED_AMC_DIR + File.separator + "TVShows");
//		amcDirectory.renameTo(correctDirectory);

		
//		LinkedMap<String,KodiDownloader> downloader = HTPC.getInstance().loadDownloadQueue(HTPC.DOWNLOAD_QUEUE_ACTIVE_FILE,HTPC.DOWNLOAD_QUEUE_ACTIVE_FILE_LOCK);
//		
//		HTPC.getInstance().copyCompletedKodiDownloads(downloader);
//		
//		//now run amc
//		HTPC.getInstance().automatedMediaCenter(HTPC.KODI_DOWNLOAD_COMPLETED_DIR, HTPC.KODI_DOWNLOAD_COMPLETED_AMC_DIR);
//		List<File> completedFiles = new ArrayList<>();
//		for (KodiDownloader kodiDownloader : downloader.values()) {
//			LOG.info("Downloader: " + kodiDownloader.toString());
//			if(kodiDownloader.isComplete())
//			{
//				LOG.info("Download is complete: " + kodiDownloader.toString());
//			}
//			else 
//			{
//				LOG.info("Download is not complete: " + kodiDownloader.toString());
//			}
//		}
		//LOG.exiting(CLASS_NAME, "runTestRequest");
		//HTPC.getInstance().exit(true);
	}	
	
	/**
	 * @param args
	 */
	private List<KodiDownloader> findShow(boolean isTVShow) {
		
		List<KodiDownloader> downloads = new ArrayList<KodiDownloader>();
		
		if(isTVShow)
		{
			for (String name : findTVShows) {
				Series tvshowInfo = findTVShow(name);
				if(tvshowInfo != null)
				{
					KodiDownloader downloader = KodiDownloader.createKodiDownloader(tvshowInfo.getId(),null,name,"1","1",false,HTPC.getDOWNLOAD_STATUS_QUEUED(),"0","","0","0");
					downloads.add(downloader);	
				}			
			}
		}
		else
		{
			for (String name : findMovies) {
				MovieInfo movieInfo = findMovie(name);
				if(movieInfo != null)
				{
					KodiDownloader downloader = KodiDownloader.createKodiDownloader(null,movieInfo.getImdbID(),name,"","",false,HTPC.getDOWNLOAD_STATUS_QUEUED(),"0","","0","0");
					downloads.add(downloader);	
				}			
			}
		}
		
		System.out.println("==========================================================================");
		for (KodiDownloader kodiDownloader : downloads) {
			if(kodiDownloader instanceof KodiTVShowDownloader)
			{
				KodiTVShowDownloader kodiTVShowDownloader = (KodiTVShowDownloader) kodiDownloader;
				//System.out.println("IMDBID: " + kodiExodusMovieDownloader.getImdbID() + " movieName: " + kodiExodusMovieDownloader.getMovieName());
				System.out.println(kodiTVShowDownloader.toString());
			}
			else
			{
				KodiMovieDownloader kodiMovieDownloader = (KodiMovieDownloader) kodiDownloader;
				//System.out.println("IMDBID: " + kodiExodusMovieDownloader.getImdbID() + " movieName: " + kodiExodusMovieDownloader.getMovieName());
				System.out.println(kodiMovieDownloader.toString());				
			}
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
