/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author ufctester
 *
 */
abstract public class HTPCConfig {

	//public static String KODI_HOST = "192.168.0.21";
	public static String KODI_HOST = "localhost";
	public static int KODI_HTTP_PORT = 9080;
	public static int KODI_SOCKET_PORT = 9090;
	public static boolean CLEAN_KODI_DOWNLOAD = false;
	
	/*
	 * There are places in the multi threaded executions where another thread might be running or there are other tasks going on.  
	 * Set this to true if you are calling the apis standalone, otherwise for AMC it should be false
	 */
	public static boolean EXIT_JAVA_PROGRAM=false;
	
	//PVR Configuration Options
	Configuration options = null;
	
	private final static String CONFIG_FILE = "pvr-config.properties";
	
	
	
	//Logger Details
	public Level LOG_LEVEL = Level.INFO;
	
	protected static final String CLASS_NAME = HTPC.class.getName();
	public static final Logger LOG = Logger.getLogger(CLASS_NAME);
	
	public static final String LOG_DOWNLOAD_TR = "io.askcloud.pvr.api.kodi.status";
	public static final Logger LOG_DOWNLOAD = Logger.getLogger(LOG_DOWNLOAD_TR);
	public static final String LOG_LOAD_KODI_STATUS_TR = "io.askcloud.pvr.api.kodi.loadstatus";
	public static final Logger LOG_LOAD_KODI_STATUS = Logger.getLogger(LOG_LOAD_KODI_STATUS_TR);
	public static final String LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR = "io.askcloud.pvr.api.kodi.monitor";
	public static final Logger LOG_DOWNLOAD_KODI_MONITOR_THREAD = Logger.getLogger(LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR);
	public static final String LOG_FILE_BOT_TR = "io.askcloud.pvr.api.filebot";
	public static final Logger LOG_FILE_BOT = Logger.getLogger(LOG_FILE_BOT_TR);	
	
	
	public static String PLEX_TVSHOWS_DIR =  "C:\\Entertainment\\TVShows";
	public static String PLEX_MOVIES_DIR =  "C:\\Entertainment\\Movies";
	//public static String PLEX_TVSHOWS_DIR =  "C:\\tmp\\TVShows";
	
	public static final int KODI_DOWNLOAD_THREADS = 10;
	
	//when there are no more downloads to be process then wait 2 mins and check again
	public static final int KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME = 120000;
	
	public static final int KODI_UPDATING_PROGRESS_SLEEP_TIME=20000;
	
	//This flag is used to tell the program to continue to run and pick up new requests as they come in.
	//This means the program will never end
	public static boolean WAIT_FOR_NEW_REQUESTS_NO_EXIT = false;
	
	
	/*
	 * Kodi Download Constants
	 */
	public static String KODI_DOWNLOAD_BASE_DIR = "C:\\gitbash\\opt\\kodi\\downloads";
	public static String KODI_DOWNLOAD_COMPLETED_DIR = "C:\\gitbash\\opt\\kodi\\completed";
	public static String KODI_DOWNLOAD_COMPLETED_AMC_DIR = "C:\\gitbash\\opt\\kodi\\amccompleted";
	public static String KODI_DOWNLOAD_COMPLETED_AMC_TVSHOW_DIR = KODI_DOWNLOAD_COMPLETED_AMC_DIR + "\\TVShows";
	public static String KODI_DOWNLOAD_COMPLETED_AMC_MOVIES_DIR = KODI_DOWNLOAD_COMPLETED_AMC_DIR + "\\Movies";
	
	public static String KODI_DOWNLOAD_TVSHOWS_DIR = KODI_DOWNLOAD_BASE_DIR + "\\tvshows";
	public static String KODI_DOWNLOAD_MOVIES_DIR = KODI_DOWNLOAD_BASE_DIR + "\\movies";

	public static String KODI_PVR_DOWNLOAD_FILE = "kodi-downloader.csv";
	public static String KDOI_PVR_DOWNLOAD_TRACKER = "C:/gitbash/opt/kodi/" + KODI_PVR_DOWNLOAD_FILE;
	public static String KDOI_PVR_DOWNLOAD_TRACKER_MASTER = "C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/kodi/plugin.video.exodus.updates/" + KODI_PVR_DOWNLOAD_FILE;

	public static String DOWNLOAD_STATUS_QUEUED="QUEUED";
	public static String DOWNLOAD_STATUS_SNATCHED="SNATCHED";
	
	//This controls the number of shows to pick up per queue.  -1 is All of them
	public static int KODI_TVSHOW_QUEUE_BATCH = -1;
	public static int KODI_MOVIE_QUEUE_BATCH = -1;
	
	/*
	 * FileBot Groovy Scripts
	 */
	public static String FILEBOT_FIND_SERIES_MISSING_EPISODES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-missing.groovy";
	public static String FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-missing-excludes.txt";
	public static String FILEBOT_AMC_LOG="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/amc.log";
	public static String FILEBOT_AMC_EXCLUDE_LIST="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/amc-exclude.txt";
	
	public static String FILEBOT_FIND_SERIES_ENDED_EPISODES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-ended.groovy";
	public static String FILEBOT_FIND_SERIES_EPISODES_HAVE="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-have.groovy";
	
	
	public static String FILEBOT_SERIES_ENDED_EPISODES_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-ended.txt";
	public static String FILEBOT_SERIES_EPISODES_HAVE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-episodes-have.txt";
	
	public static String DOWNLOAD_QUEUE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue.txt";
	public static String DOWNLOAD_QUEUE_LOCK_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue.txt.lock";
	public static String DOWNLOAD_QUEUE_ACTIVE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue-active.txt";
	public static String DOWNLOAD_QUEUE_ACTIVE_FILE_LOCK="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue-active.txt.lock";
	
	/*
	 * download-queue.txt csv header
	 * TVDB_ID,IMDB_ID,NAME,SEASON,EPISODE,ENDED,STATUS,DOWNLOAD_PERCENT,FILE,DOWNLOADSIZE,TOTALSIZE
	 */
	public static final String CSV_TVDB_ID = "TVDB_ID";
	public static final String CSV_IMDB_ID = "IMDB_ID";
	public static final String CSV_NAME = "NAME";
	public static final String CSV_SEASON = "SEASON";
	public static final String CSV_EPISODE = "EPISODE";
	public static final String CSV_ENDED = "ENDED";
	public static final String CSV_STATUS = "STATUS";
	public static final String CSV_DOWNLOAD_PERCENT = "DOWNLOAD_PERCENT";
	public static final String CSV_FILE = "FILE";
	public static final String CSV_DOWNLOADSIZE = "DOWNLOADSIZE";
	public static final String CSV_TOTALSIZE = "TOTALSIZE";
	public static final String[] CSV_HEADER = {CSV_TVDB_ID,CSV_IMDB_ID,CSV_NAME,CSV_SEASON,CSV_EPISODE,CSV_ENDED,CSV_STATUS,CSV_DOWNLOAD_PERCENT,CSV_FILE,CSV_DOWNLOADSIZE,CSV_TOTALSIZE};
	

	public static String FILEBOT_AMC_DESTINATION="C:\\tmp\\CompletedDownloads";
	
	public static String FILE_BOT_EXE="C:\\gitbash\\opt\\filebot\\filebot.exe";
	
	public static String BC_PUSH_AMC_TO_PLEX_BAT="C:\\gitbash\\opt\\beyondcompare\\scripts\\push-amc-to-plex.bat";
	public static String BC_VALIDATE_AMC_READY_FOR_PLEX_BAT="C:\\gitbash\\opt\\beyondcompare\\scripts\\report-amc.bat";
	
	//This will run the entire download job and validate it at the end but not push the downloads to plex
	public static boolean PUBLISH_TO_PLEX=false;
	
	public static String LOGS_DIRECTORY = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs";
	public static String LOGS_ARCHIVES_DIRECTORY = LOGS_DIRECTORY + "\\archives";
	public static String BEYOND_COMPARE_AMC_MOVIE_REPORT_FILE = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs\\report-amc-movie.xml";
	//public static String BEYOND_COMPARE_AMC_TV_REPORT_FILE = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs\\report-amc-tv.xml";
	public static String BEYOND_COMPARE_AMC_TV_REPORT_FILE = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs\\report-amc-tv.xml";
	

	//queue lock file wait time
	public static int QUEUE_LOCK_FILE_WAIT_TIME=10000; //10 seconds
	
	//Kodi Exodus Clear Cache Wait time for the thread so the cache can be completely cleared before continuing.
	public static int CLEAR_CACHE_THREAD_WAIT_TIME=10000; //10 seconds
	
	public static int KODI_MONITOR_DOWNLOAD_PERCENTAGE_CSV_FILE_LOADER=10000; //10 seconds
	
	public static int KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME=20000; //20 seconds
	
	/*
	 * The download is considered stuck if it reaches 4 minutes without any progress updates
	 * 
	 * 4 Minutes is determined by the KODI_EXODUS_DOWNLOAD_ITERATOR_COUNTER_HUNG * KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME (seconds)
	 * i.e. 20 iterations of 20 seconds (KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME)
	 */
	public static int KODI_EXODUS_DOWNLOAD_ITERATOR_COUNTER_HUNG=20; 
	
	
	/**
	 * 
	 */
	public HTPCConfig() {
		super();
	}
	
	public void getBG()
	{
		LOG.entering(CLASS_NAME, "getBG");
		LOG.info(getOptions().getString("colors.background"));
		LOG.info(String.valueOf(getOptions().getInt("window.width")));
	}
	
	/*
	 * load pvr-config.properties file
	 */
	private Configuration getOptions() {
		if(options == null){
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties()
			        .setFileName(CONFIG_FILE));
			try
			{
				options = builder.getConfiguration();
			    
			}
			catch(ConfigurationException cex)
			{
			    // loading of the configuration file failed
				cex.printStackTrace();
			}
		}	
		return options;
	}
}
