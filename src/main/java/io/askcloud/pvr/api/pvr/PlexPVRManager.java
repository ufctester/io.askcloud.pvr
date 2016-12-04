package io.askcloud.pvr.api.pvr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import net.filebot.Main;

/**
 * 
 * @author UFCTester
 *
 */
public class PlexPVRManager {
	private static PlexPVRManager eINSTANCE = null;

	//public static String KODI_HOST = "192.168.0.21";
	public static String KODI_HOST = "localhost";
	public static int KODI_HTTP_PORT = 9080;
	public static int KODI_SOCKET_PORT = 9090;

	public static boolean CLEAN_KODI_DOWNLOAD = false;
	public static String KODI_DOWNLOAD_BASE_DIR = "C:\\gitbash\\opt\\kodi\\downloads";
	public static String KODI_DOWNLOAD_TVSHOWS_DIR = KODI_DOWNLOAD_BASE_DIR + "\\tvshows";
	public static String KODI_DOWNLOAD_MOVIES_DIR = KODI_DOWNLOAD_BASE_DIR + "\\movies";

	public static String KODI_PVR_DOWNLOAD_FILE = "kodi-downloader.csv";
	public static String KDOI_PVR_DOWNLOAD_TRACKER = "C:/gitbash/opt/kodi/" + KODI_PVR_DOWNLOAD_FILE;
	public static String KDOI_PVR_DOWNLOAD_TRACKER_MASTER = "C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/kodi/plugin.video.exodus.updates/" + KODI_PVR_DOWNLOAD_FILE;

	
	public static String FILE_BOT_MISSING_EPISODES_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\missing-episodes.txt";
	public static String FILE_BOT_MISSING_EPISODES_QUEUE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\missing-episodes-queue.txt";
	public static String FILE_BOT_MOVIE_QUEUE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\movie-queue.txt";
	
	public static String FILEBOT_AMC_DESTINATION="C:\\tmp\\TVShows_completed";
	
	public static String FILE_BOT_EXE="C:\\gitbash\\opt\\filebot\\filebot.exe";
	
	private Level LOG_LEVEL = Level.INFO;

	//Kodi Exodus Clear Cache Wait time for the thread so the cache can be completely cleared before continuing.
	public static int CLEAR_CACHE_THREAD_WAIT_TIME=10000; //10 seconds
	
	public static int KODI_MONITOR_DOWNLOAD_CSV_FILE_LOADER=5000; //5 seconds
	
	public static int KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME=10000; //10 seconds
	
	private static String CLASS_NAME = PlexPVRManager.class.getName();
	public static Logger log = Logger.getLogger(CLASS_NAME);

	final private SecurityManager orgSecurityManager = System.getSecurityManager();
	
	private static Set<String> excludeMissingEpisodes = new LinkedHashSet<String>();
	
	private static Set<String> excludeMovies = new LinkedHashSet<String>();
	
    static
    {
    	excludeMissingEpisodes = new LinkedHashSet<String>();
		excludeMissingEpisodes.add("73141"); //American Dad!
		excludeMissingEpisodes.add("76706"); //Big Brother
		excludeMissingEpisodes.add("72549"); //Big Brother (UK)
		excludeMissingEpisodes.add("81563"); //Border Security
		excludeMissingEpisodes.add("261999"); //Border Security: Canada's Front Line
		excludeMissingEpisodes.add("70760"); //Celebrity Big Brother
		excludeMissingEpisodes.add("84107"); //Cheaters
		excludeMissingEpisodes.add("74709"); //Cops
		excludeMissingEpisodes.add("72546"); //CSI: Crime Scene Inves	
		excludeMissingEpisodes.add("183831"); //Hardcore Pawn
		excludeMissingEpisodes.add("75692"); //Law & Order: Special Victims Unit
		excludeMissingEpisodes.add("73388"); //MythBusters
		excludeMissingEpisodes.add("75897"); //South Park
		excludeMissingEpisodes.add("77666"); //The Amazing Race
		excludeMissingEpisodes.add("71663"); //The Simpsons
		excludeMissingEpisodes.add("230121"); //The X Factor (US)
		excludeMissingEpisodes.add("97731"); //Tosh.0
    }

	protected class OneLineFormatter extends SimpleFormatter {

		// format string for printing the log record
		private final String format = "[%1$tc] %4$s: %5$s %n";//LoggingSupport.getSimpleFormat();
		private final Date dat = new Date();

		public OneLineFormatter() {
			super();
		}

		@Override
		public synchronized String format(LogRecord record) {
			dat.setTime(record.getMillis());
			String source;
			if (record.getSourceClassName() != null) {
				source = record.getSourceClassName();
				if (record.getSourceMethodName() != null) {
					source += " " + record.getSourceMethodName();
				}
			}
			else {
				source = record.getLoggerName();
			}
			String message = formatMessage(record);
			String throwable = "";
			if (record.getThrown() != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				pw.println();
				record.getThrown().printStackTrace(pw);
				pw.close();
				throwable = sw.toString();
			}
			return String.format(format, dat, source, record.getLoggerName(), record.getLevel().getName(), message, throwable);
		}
	}

	private PlexPVRManager() {

		super();

		init();
	}

	private void init() {
		log.entering(CLASS_NAME, "init");
		
		initLogger();

		//copy master kodi-downloader.csv download file 
		File sourceFile = new File(KDOI_PVR_DOWNLOAD_TRACKER_MASTER);
		File targetFile = new File(KDOI_PVR_DOWNLOAD_TRACKER);
		try {
			FileUtils.copyFile(sourceFile, targetFile);
		}
		catch (Exception e) {
			log.severe("ERROR copying file: " + KDOI_PVR_DOWNLOAD_TRACKER_MASTER + " to file: " + KDOI_PVR_DOWNLOAD_TRACKER);
		}

		//Kodi TVShows
		recreateKodiDownloadDirectoy(KODI_DOWNLOAD_TVSHOWS_DIR);

		//Kodi Movies
		recreateKodiDownloadDirectoy(KODI_DOWNLOAD_MOVIES_DIR);
		
		log.exiting(CLASS_NAME, "init");
	}
	

	private void recreateKodiDownloadDirectoy(String directory) {
		
		log.entering(CLASS_NAME, "recreateKodiDownloadDirectoy", new Object[] {directory});
		
		if(!CLEAN_KODI_DOWNLOAD)
		{
			return;
		}
		
		//delete old downloads directory
		try {
			File kodiDownloads = new File(directory);
			log.info("Deleting Old Kodi Download Files: " + directory);
			printFilesAndDirs(kodiDownloads);
			FileUtils.deleteQuietly(kodiDownloads);

			log.info("Old Kodi Download Files Should Be Deleted: " + directory);
			printFilesAndDirs(kodiDownloads);
		}
		catch (Exception e) {
			log.severe("ERROR deleting file: " + directory);
		}
		log.exiting(CLASS_NAME, "recreateKodiDownloadDirectoy");
	}
	
	public void recreateDirectory(String targetDirectory)
	{
		log.entering(CLASS_NAME, "recreateDirectory", new Object[] {targetDirectory});
		
		File targetDirectoryFile = new File(targetDirectory);
		FileUtils.deleteQuietly(targetDirectoryFile);
		try {
			FileUtils.forceMkdir(targetDirectoryFile);
		}
		catch (Exception e) {
			log.severe("Unable to recreate directory: " + targetDirectory);
		}
		log.exiting(CLASS_NAME, "recreateDirectory");
	}

	private void printFilesAndDirs(File directory) {
		if ((directory == null) || (!directory.exists())) {
			return;
		}
		Collection<File> files = FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.TRUE);

		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if (file.isDirectory()) {
				log.info("Directory Found: " + file.toString());
			}
			else {
				log.info("     File Found: " + file.toString());
			}
		}
	}
	
	public void printFileSizes(File directory) {
		if ((directory == null) || (!directory.exists())) {
			return;
		}
		Collection<File> files = FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.TRUE);

		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if (file.isDirectory()) {
				//log.finest("Directory Found: " + file.toString());
			}
			else {
				log.info(" Size: " + file.length() + "  File Found: " + file.toString());
			}
		}
	}

	private void initLogger() {

		//  //Handler handler = new ConsoleHandler();
		//  SimpleFormatter formatter = new SimpleFormatter();
		//  
		//  Handler handler = new StreamHandler(System.out,formatter);
		//  handler.setLevel(LOG_LEVEL);
		//  log.setLevel(LOG_LEVEL);
		//  //log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		//  log.addHandler(handler);

		//get the top Logger
		Logger system = Logger.getLogger("");

		// Handler for console (reuse it if it already exists)
		Handler consoleHandler = null;
		//see if there is already a console handler
		for (Handler handler : system.getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				//found the console handler
				consoleHandler = handler;
				break;
			}
		}
		if (consoleHandler == null) {
			//there was no console handler found, create a new one
			consoleHandler = new ConsoleHandler();
			system.addHandler(consoleHandler);
		}
		OneLineFormatter formatter = new OneLineFormatter();
		consoleHandler.setFormatter(formatter);
		log.setLevel(LOG_LEVEL);

		//Logger.getLogger("").setLevel( Level.OFF ); // Solution 2

		//set the console handler to fine:
		consoleHandler.setLevel(LOG_LEVEL);
	}

	public static PlexPVRManager getInstance() {
		if (eINSTANCE == null) {
			eINSTANCE = new PlexPVRManager();
		}
		return eINSTANCE;
	}

	/**
	 * 
	 * @return
	 */
	public KodiDownloadManager getKodiManager() {
		return KodiDownloadManager.getInstance();
	}
	
	
	public String getSeason(String season) {
		return (season.length() == 1) ? "0" + season : season;
	}

	public String getEpisode(String episode) {
		return (episode.length() == 1) ? "0" + episode : episode;
	}

	public int getSeasonInt(String season) {
		try {
			return Integer.valueOf(season).intValue();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}

	public int getEpisodeInt(String episode) {
		try {
			return Integer.valueOf(episode).intValue();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}	
	
	
	
	public void unsetFilebotSecurityManager() {
		System.setSecurityManager(orgSecurityManager);
	}

	//Main.main(new String[]{"-script","C:/gitbash/opt/filebot/scripts/find-missing-episodes.groovy", directory});
	//Main.main(new String[]{"--format", "{plex}", "-script","C:/gitbash/opt/eclipse/workspace-odc/kodi-exodus-json-rpc/filebot/find-missing-episodes.groovy", directory});
	//Main.main(new String[]{"-list", "--db", "thetvdb", "--q", "Dexter","--format", "{plex}"});
	//

	
	private void callFileBot(List<String> args)
	{
		args.add(0, FILE_BOT_EXE);
		String[] cmd = args.toArray(new String[args.size()]);
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * @param showName
	 */
	public void getTVEpisodes(String showName) {
		Main.main(new String[] { "-list", "--db", "thetvdb", "--q", showName, "--format", "{n} - {s00e00} - {t}" });
	}

	/**
	 * @param name
	 */
	public void findTVShowPlex(String name) {
		Main.main(new String[] { "-list", "--db", "thetvdb", "--q", "Dexter", "--format", "{plex}" });
	}

	/**
	 * @param directory
	 * @return
	 */
	public List<KodiExodusDownloader> findMissingEpisodes(String directory) {
		log.entering(CLASS_NAME, "findMissingEpisodes", new Object[] {directory});
		File missingEpisodeFile = new File(PlexPVRManager.FILE_BOT_MISSING_EPISODES_FILE);
		try {
			log.info("Deleting old missing episode file: " + missingEpisodeFile);
			FileUtils.deleteQuietly(missingEpisodeFile);
		}
		catch (Exception e) {
			log.severe("ERROR deleting file: " + missingEpisodeFile);
		}
		
		try {			
			Main.main(new String[] { "-script", "C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-missing-episodes.groovy", directory, "--output",
					PlexPVRManager.FILE_BOT_MISSING_EPISODES_FILE });
		}
		catch(SecurityException e)
		{
			log.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			unsetFilebotSecurityManager();
		}
		
		List<KodiExodusDownloader> missingEpisodes = loadMissingEpisodes(PlexPVRManager.FILE_BOT_MISSING_EPISODES_FILE);
		
		log.exiting(CLASS_NAME, "findMissingEpisodes");
		
		return missingEpisodes;
	}
	
	/**
	 * @return
	 */
	public List<KodiExodusDownloader> loadMovieQueue()
	{
		return loadMovieQueue(PlexPVRManager.FILE_BOT_MOVIE_QUEUE_FILE);
	}

	/**
	 * @param directory
	 */
	public void findMissingEpisodesPlex(String directory) {
		Main.main(new String[] { "-script", "C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-missing-episodes-plex.groovy", directory });

	}

	/**
	 * @param sourceDirectory
	 * @param targetDirectory
	 */
	public void automatedMediaCenter(String sourceDirectory, String targetDirectory) {
		log.entering(CLASS_NAME, "findMissingEpisodes", new Object[] {sourceDirectory,targetDirectory});
		
		PlexPVRManager.getInstance().recreateDirectory(targetDirectory);
		
		try {
			Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "copy", "-non-strict", sourceDirectory, "--conflict", "override", "--def",
					"movieFormat=\"G:/Movies/{fn}\"", "subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "ecludeList", "amc-exclude.txt", "--def", "--log", "all" });			
		}
		catch(SecurityException e)
		{
			log.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			unsetFilebotSecurityManager();
		}
		
		log.exiting(CLASS_NAME, "findMissingEpisodes");		
	}

	/**
	 * @param sourceDirectory
	 * @param targetDirectory
	 */
	public void automatedMediaCenterTest(String sourceDirectory, String targetDirectory) {
		Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "test", "-non-strict", sourceDirectory, "--conflict", "override", "--def",
				"movieFormat=\"G:/Movies/{fn}\"", "subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "--log", "all" });
	}
	
	/**
	 * @param missingEpisodeFile
	 * @return
	 */
	private List<KodiExodusDownloader> loadMovieQueue(String movieQueueFile) {
		List<KodiExodusDownloader> movies = new ArrayList<KodiExodusDownloader>();
		if(movieQueueFile == null)
		{
			return movies;
		}
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        try {
        	inputStream = new FileInputStream(movieQueueFile);
            reader = new InputStreamReader(inputStream, "UTF-8");
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            try {
            	//"TVDB_ID,SERIES_NAME,SEASON,EPISODE"
                for (final CSVRecord record : parser) {
                	final String imdbid = record.get("IMDB_ID");
                	final String movieName = record.get("MOVIE_NAME");
                	
                	if(!excludeMovies.contains(imdbid))
                	{
                    	KodiExodusDownloader movie = createMissingMovie(imdbid,movieName);
                    	movies.add(movie);
                	}
                }
            } finally {
                try {
                	parser.close();	
				}
				catch (Exception e) {
					log.severe("Unable to close " + movieQueueFile + " Parser: " + e.getMessage());
				}
                try {
                	reader.close();
				}
				catch (Exception e) {
					log.severe("Unable to close " + movieQueueFile + " Reader: " + e.getMessage());
				}
            }	
		}
		catch (Exception e) {
			// TODO: handle exception
		}
        finally {
            try {
            	parser.close();	
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFile + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFile + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFile + " InputStream: " + e.getMessage());
			}            
        }
        
    	return movies;        
	}	
	
	
	/**
	 * @param missingEpisodeFile
	 * @return
	 */
	private List<KodiExodusDownloader> loadMissingEpisodes(String missingEpisodeFile) {
		List<KodiExodusDownloader> missingEpisodes = new ArrayList<KodiExodusDownloader>();
		if(missingEpisodeFile == null)
		{
			return missingEpisodes;
		}
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        try {
        	inputStream = new FileInputStream(missingEpisodeFile);
            reader = new InputStreamReader(inputStream, "UTF-8");
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            try {
            	//"TVDB_ID,SERIES_NAME,SEASON,EPISODE"
                for (final CSVRecord record : parser) {
                	final String tvdbid = record.get("TVDB_ID");
                	final String imdbid = record.get("IMDB_ID");
                	final String seriesName = record.get("SERIES_NAME");
                	final String season = record.get("SEASON");
                	final String episode = record.get("EPISODE");
                	
                	if(!excludeMissingEpisodes.contains(tvdbid))
                	{
                    	KodiExodusDownloader missingEpisode = createMissingEpisode(tvdbid,imdbid,seriesName,season,episode);
                    	missingEpisodes.add(missingEpisode);
                	}
                }
            } finally {
                try {
                	parser.close();	
				}
				catch (Exception e) {
					log.severe("Unable to close " + missingEpisodeFile + " Parser: " + e.getMessage());
				}
                try {
                	reader.close();
				}
				catch (Exception e) {
					log.severe("Unable to close " + missingEpisodeFile + " Reader: " + e.getMessage());
				}
            }	
		}
		catch (Exception e) {
			// TODO: handle exception
		}
        finally {
            try {
            	parser.close();	
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeFile + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeFile + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeFile + " InputStream: " + e.getMessage());
			}            
        }
        
    	return missingEpisodes;        
	}	
	
	/**
	 * @param tvdbid
	 * @param seriesName
	 * @param season
	 * @param episode
	 * @return
	 */
	public KodiExodusDownloader createMissingEpisode(String tvdbid, String imdbid, String seriesName, String season, String episode)
	{
		return new KodiExodusTVShowDownloader(seriesName,imdbid,tvdbid,PlexPVRManager.getInstance().getSeasonInt(season),PlexPVRManager.getInstance().getEpisodeInt(episode));
	}
	
	/**
	 * @param imdbid
	 * @param movieName
	 * @return
	 */
	public KodiExodusDownloader createMissingMovie(String imdbid, String movieName)
	{
		return new KodiExodusMovieDownloader(movieName,imdbid);
	}
	
	
}
