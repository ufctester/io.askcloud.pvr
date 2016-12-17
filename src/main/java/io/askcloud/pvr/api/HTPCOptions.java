package io.askcloud.pvr.api;
/**
 * 
 */

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
public class HTPCOptions {

	private static HTPCOptions eINSTANCE=null;

	
	private final static String CONFIG_FILE = "pvr-config.properties";
	
	private static boolean CLEAN_KODI_DOWNLOAD = false;
	
	//Logger Details
	public static final Level LOG_LEVEL = Level.INFO;
	
	public static final String CLASS_NAME = HTPCOptions.class.getName();
	public static final Logger LOG = Logger.getLogger(CLASS_NAME);
	
	public static final String LOG_DOWNLOAD_TR = "io.askcloud.pvr.api.kodi.status";
	public static final Logger LOG_DOWNLOAD = Logger.getLogger(LOG_DOWNLOAD_TR);
	public static final String LOG_LOAD_KODI_STATUS_TR = "io.askcloud.pvr.api.kodi.loadstatus";
	public static final Logger LOG_LOAD_KODI_STATUS = Logger.getLogger(LOG_LOAD_KODI_STATUS_TR);
	public static final String LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR = "io.askcloud.pvr.api.kodi.monitor";
	public static final Logger LOG_DOWNLOAD_KODI_MONITOR_THREAD = Logger.getLogger(LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR);
	public static final String LOG_FILE_BOT_TR = "io.askcloud.pvr.api.filebot";
	public static final Logger LOG_FILE_BOT = Logger.getLogger(LOG_FILE_BOT_TR);	
	
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
	
	
	
	
	
	//public static String KODI_HOST = "192.168.0.21";
	private static String KODI_HOST = "localhost";
	private static int KODI_HTTP_PORT = 9080;
	private static int KODI_SOCKET_PORT = 9090;
	
	/*
	 * There are places in the multi threaded executions where another thread might be running or there are other tasks going on.  
	 * Set this to true if you are calling the apis standalone, otherwise for AMC it should be false
	 */
	private static boolean EXIT_JAVA_PROGRAM=false;
	
	
	private static String PLEX_TVSHOWS_DIR =  "C:\\Entertainment\\TVShows";
	private static String PLEX_MOVIES_DIR =  "C:\\Entertainment\\Movies";
	//public static String PLEX_TVSHOWS_DIR =  "C:\\tmp\\TVShows";
	
	private static final int KODI_DOWNLOAD_THREADS = 10;
	
	//when there are no more downloads to be process then wait 2 mins and check again
	private static final int KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME = 120000;
	
	private static final int KODI_UPDATING_PROGRESS_SLEEP_TIME=20000;
	
	//This flag is used to tell the program to continue to run and pick up new requests as they come in.
	//This means the program will never end
	private static boolean WAIT_FOR_NEW_REQUESTS_NO_EXIT = false;
	
	
	/*
	 * Kodi Download Constants
	 */
	private static String KODI_DOWNLOAD_BASE_DIR = "C:\\gitbash\\opt\\kodi\\downloads";
	private static String KODI_DOWNLOAD_COMPLETED_DIR = "C:\\gitbash\\opt\\kodi\\completed";
	private static String KODI_DOWNLOAD_COMPLETED_AMC_DIR = "C:\\gitbash\\opt\\kodi\\amccompleted";
	private static String KODI_DOWNLOAD_COMPLETED_AMC_TVSHOW_DIR = KODI_DOWNLOAD_COMPLETED_AMC_DIR + "\\TVShows";
	private static String KODI_DOWNLOAD_COMPLETED_AMC_MOVIES_DIR = KODI_DOWNLOAD_COMPLETED_AMC_DIR + "\\Movies";
	
	private static String KODI_DOWNLOAD_TVSHOWS_DIR = KODI_DOWNLOAD_BASE_DIR + "\\tvshows";
	private static String KODI_DOWNLOAD_MOVIES_DIR = KODI_DOWNLOAD_BASE_DIR + "\\movies";

	private static String KODI_PVR_DOWNLOAD_FILE = "kodi-downloader.csv";
	private static String KDOI_PVR_DOWNLOAD_TRACKER = "C:/gitbash/opt/kodi/" + KODI_PVR_DOWNLOAD_FILE;
	private static String KDOI_PVR_DOWNLOAD_TRACKER_MASTER = "C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/kodi/plugin.video.exodus.updates/" + KODI_PVR_DOWNLOAD_FILE;

	private static String DOWNLOAD_STATUS_QUEUED="QUEUED";
	private static String DOWNLOAD_STATUS_SNATCHED="SNATCHED";
	
	//This controls the number of shows to pick up per queue.  -1 is All of them
	private static int KODI_TVSHOW_QUEUE_BATCH = -1;
	private static int KODI_MOVIE_QUEUE_BATCH = -1;
	
	/*
	 * FileBot Groovy Scripts
	 */
	private static String FILEBOT_FIND_SERIES_MISSING_EPISODES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/scripts/filebot/find-series-episodes-missing.groovy";
	private static String FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/scripts/filebot/find-series-episodes-missing-excludes.txt";
	private static String FILEBOT_AMC_LOG="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/scripts/filebot/amc.log";
	private static String FILEBOT_AMC_EXCLUDE_LIST="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/scripts/filebot/amc-exclude.txt";
	
	private static String FILEBOT_FIND_SERIES_ENDED_EPISODES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/scripts/filebot/find-series-ended.groovy";
	private static String FILEBOT_FIND_SERIES_EPISODES_HAVE="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/scripts/filebot/find-series-episodes-have.groovy";
	
	
	private static String FILEBOT_SERIES_ENDED_EPISODES_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-ended.txt";
	private static String FILEBOT_SERIES_EPISODES_HAVE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-episodes-have.txt";
	
	private static String DOWNLOAD_QUEUE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue.txt";
	private static String DOWNLOAD_QUEUE_LOCK_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue.txt.lock";
	private static String DOWNLOAD_QUEUE_ACTIVE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue-active.txt";
	private static String DOWNLOAD_QUEUE_ACTIVE_FILE_LOCK="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\download-queue-active.txt.lock";
	
	private static String FILE_BOT_EXE="C:\\gitbash\\opt\\filebot\\filebot.exe";
	
	private static String BC_PUSH_AMC_TO_PLEX_BAT="C:\\gitbash\\opt\\beyondcompare\\scripts\\push-amc-to-plex.bat";
	private static String BC_VALIDATE_AMC_READY_FOR_PLEX_BAT="C:\\gitbash\\opt\\beyondcompare\\scripts\\report-amc.bat";
	
	//This will run the entire download job and validate it at the end but not push the downloads to plex
	private static boolean PUBLISH_TO_PLEX=false;
	
	private static String LOGS_DIRECTORY = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs";
	private static String LOGS_ARCHIVES_DIRECTORY = LOGS_DIRECTORY + "\\archives";
	private static String BEYOND_COMPARE_AMC_MOVIE_REPORT_FILE = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs\\report-amc-movie.xml";
	//private static String BEYOND_COMPARE_AMC_TV_REPORT_FILE = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs\\report-amc-tv.xml";
	private static String BEYOND_COMPARE_AMC_TV_REPORT_FILE = "C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\logs\\report-amc-tv.xml";
	

	//queue lock file wait time
	private static int QUEUE_LOCK_FILE_WAIT_TIME=10000; //10 seconds
	
	//Kodi Exodus Clear Cache Wait time for the thread so the cache can be completely cleared before continuing.
	private static int CLEAR_CACHE_THREAD_WAIT_TIME=10000; //10 seconds
	
	private static int KODI_MONITOR_DOWNLOAD_PERCENTAGE_CSV_FILE_LOADER=10000; //10 seconds
	
	private static int KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME=20000; //20 seconds
	
	/*
	 * The download is considered stuck if it reaches 4 minutes without any progress updates
	 * 
	 * 4 Minutes is determined by the KODI_EXODUS_DOWNLOAD_ITERATOR_COUNTER_HUNG * KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME (seconds)
	 * i.e. 20 iterations of 20 seconds (KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME)
	 */
	private static int KODI_EXODUS_DOWNLOAD_ITERATOR_COUNTER_HUNG=20; 	
	
	//PVR Configuration Options
	private Configuration options = null;
	
	/**
	 * 
	 */
	public HTPCOptions() {
		super();
	}
	
	
	public static HTPCOptions getInstance() {
		if(eINSTANCE == null)
		{
			eINSTANCE = new HTPCOptions();
		}
		return eINSTANCE;
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

	//FIXME DSP - Clean up from here down
		
	public static String getBC_PUSH_AMC_TO_PLEX_BAT() {
		LOG.severe(getInstance().getOptions().getString("BC_PUSH_AMC_TO_PLEX_BAT"));
		LOG.severe(BC_PUSH_AMC_TO_PLEX_BAT);
		return getInstance().BC_PUSH_AMC_TO_PLEX_BAT;
	}
	
	
	public static String getBC_VALIDATE_AMC_READY_FOR_PLEX_BAT() {
		LOG.severe(getInstance().getOptions().getString("BC_VALIDATE_AMC_READY_FOR_PLEX_BAT"));
		LOG.severe(BC_VALIDATE_AMC_READY_FOR_PLEX_BAT);
		return getInstance().BC_VALIDATE_AMC_READY_FOR_PLEX_BAT;
	}
	
	public static String getBEYOND_COMPARE_AMC_MOVIE_REPORT_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_MOVIE_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_MOVIE_REPORT_FILE);		
		return getInstance().BEYOND_COMPARE_AMC_MOVIE_REPORT_FILE;
	}
	
	public static String getBEYOND_COMPARE_AMC_TV_REPORT_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);			
		return getInstance().BEYOND_COMPARE_AMC_TV_REPORT_FILE;
	}
	
	
	//FIXME DSP - Clean up from here down
	public static int getCLEAR_CACHE_THREAD_WAIT_TIME() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().CLEAR_CACHE_THREAD_WAIT_TIME;
	}
	
	public static String getDOWNLOAD_QUEUE_ACTIVE_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().DOWNLOAD_QUEUE_ACTIVE_FILE;
	}
	
	public static String getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().DOWNLOAD_QUEUE_ACTIVE_FILE_LOCK;
	}
	
	public static String getDOWNLOAD_QUEUE_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().DOWNLOAD_QUEUE_FILE;
	}
	
	public static String getDOWNLOAD_QUEUE_LOCK_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().DOWNLOAD_QUEUE_LOCK_FILE;
	}
	
	public static String getDOWNLOAD_STATUS_QUEUED() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().DOWNLOAD_STATUS_QUEUED;
	}
	
	public static String getDOWNLOAD_STATUS_SNATCHED() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().DOWNLOAD_STATUS_SNATCHED;
	}
	
	public static String getFILE_BOT_EXE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILE_BOT_EXE;
	}
	
	public static String getFILEBOT_AMC_EXCLUDE_LIST() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_AMC_EXCLUDE_LIST;
	}
	
	public static String getFILEBOT_AMC_LOG() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_AMC_LOG;
	}
	
	public static String getFILEBOT_FIND_SERIES_ENDED_EPISODES() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_FIND_SERIES_ENDED_EPISODES;
	}
	
	public static String getFILEBOT_FIND_SERIES_EPISODES_HAVE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_FIND_SERIES_EPISODES_HAVE;
	}
	
	public static String getFILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES;
	}
	
	public static String getFILEBOT_FIND_SERIES_MISSING_EPISODES() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_FIND_SERIES_MISSING_EPISODES;
	}
	
	public static String getFILEBOT_SERIES_ENDED_EPISODES_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_SERIES_ENDED_EPISODES_FILE;
	}
	
	public static String getFILEBOT_SERIES_EPISODES_HAVE_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().FILEBOT_SERIES_EPISODES_HAVE_FILE;
	}
	
	public static String getKDOI_PVR_DOWNLOAD_TRACKER() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KDOI_PVR_DOWNLOAD_TRACKER;
	}
	
	public static String getKDOI_PVR_DOWNLOAD_TRACKER_MASTER() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KDOI_PVR_DOWNLOAD_TRACKER_MASTER;
	}
	
	public static String getKODI_DOWNLOAD_BASE_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_BASE_DIR;
	}
	
	public static String getKODI_DOWNLOAD_COMPLETED_AMC_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_COMPLETED_AMC_DIR;
	}
	
	public static String getKODI_DOWNLOAD_COMPLETED_AMC_MOVIES_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_COMPLETED_AMC_MOVIES_DIR;
	}
	
	public static String getKODI_DOWNLOAD_COMPLETED_AMC_TVSHOW_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_COMPLETED_AMC_TVSHOW_DIR;
	}
	
	public static String getKODI_DOWNLOAD_COMPLETED_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_COMPLETED_DIR;
	}
	
	public static String getKODI_DOWNLOAD_MOVIES_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_MOVIES_DIR;
	}
	
	public static String getKODI_DOWNLOAD_TVSHOWS_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_TVSHOWS_DIR;
	}
	
	public static int getKODI_DOWNLOAD_THREADS() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_THREADS;
	}
	
	public static int getKODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME;
	}
	
	public static int getKODI_UPDATING_PROGRESS_SLEEP_TIME() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_UPDATING_PROGRESS_SLEEP_TIME;
	}
	
	
	public static int getKODI_EXODUS_DOWNLOAD_ITERATOR_COUNTER_HUNG() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_EXODUS_DOWNLOAD_ITERATOR_COUNTER_HUNG;
	}
	
	public static int getKODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME;
	}
	public static String getKODI_HOST() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_HOST;
	}
	
	public static int getKODI_HTTP_PORT() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_HTTP_PORT;
	}
	
	public static int getKODI_MONITOR_DOWNLOAD_PERCENTAGE_CSV_FILE_LOADER() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_MONITOR_DOWNLOAD_PERCENTAGE_CSV_FILE_LOADER;
	}
	
	public static int getKODI_MOVIE_QUEUE_BATCH() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_MOVIE_QUEUE_BATCH;
	}
	
	public static String getKODI_PVR_DOWNLOAD_FILE() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_PVR_DOWNLOAD_FILE;
	}
	
	public static int getKODI_SOCKET_PORT() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_SOCKET_PORT;
	}
	
	public static Level getLOG_LEVEL() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().LOG_LEVEL;
	}
	
	public static int getKODI_TVSHOW_QUEUE_BATCH() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_TVSHOW_QUEUE_BATCH;
	}
	
	
	public static String getLOGS_ARCHIVES_DIRECTORY() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().LOGS_ARCHIVES_DIRECTORY;
	}
	
	public static String getLOGS_DIRECTORY() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().LOGS_DIRECTORY;
	}
	
	
	public static String getPLEX_MOVIES_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().PLEX_MOVIES_DIR;
	}
	
	public static String getPLEX_TVSHOWS_DIR() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().PLEX_TVSHOWS_DIR;
	}
	
	public static int getQUEUE_LOCK_FILE_WAIT_TIME() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().QUEUE_LOCK_FILE_WAIT_TIME;
	}
	
	public static String getConfigFile() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().CONFIG_FILE;
	}

	
	public static int getKodiDownloadThreads() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_DOWNLOAD_THREADS;
	}
	
	public static int getKodiSeriesMissingEpisodesEmptySleepTime() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME;
	}
	
	public static int getKodiUpdatingProgressSleepTime() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().KODI_UPDATING_PROGRESS_SLEEP_TIME;
	}
	
	public static boolean isEXIT_JAVA_PROGRAM() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().EXIT_JAVA_PROGRAM;
	}
	
	public static boolean isPUBLISH_TO_PLEX() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().PUBLISH_TO_PLEX;
	}
	
	
	public static boolean isWAIT_FOR_NEW_REQUESTS_NO_EXIT() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().WAIT_FOR_NEW_REQUESTS_NO_EXIT;
	}

	public static void setEXIT_JAVA_PROGRAM(boolean eXIT_JAVA_PROGRAM) {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		getInstance().EXIT_JAVA_PROGRAM = eXIT_JAVA_PROGRAM;
	}
	
	public static void setCLEAN_KODI_DOWNLOAD(boolean cLEAN_KODI_DOWNLOAD) {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		getInstance().CLEAN_KODI_DOWNLOAD = cLEAN_KODI_DOWNLOAD;
	}
	
	public static boolean isCLEAN_KODI_DOWNLOAD() {
		LOG.severe(getInstance().getOptions().getString("BEYOND_COMPARE_AMC_TV_REPORT_FILE"));
		LOG.severe(BEYOND_COMPARE_AMC_TV_REPORT_FILE);	
		return getInstance().CLEAN_KODI_DOWNLOAD;
	}
	

}
