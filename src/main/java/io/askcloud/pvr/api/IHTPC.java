package io.askcloud.pvr.api;


import java.util.logging.Level;
import java.util.logging.Logger;

import io.askcloud.pvr.themoviedb.TheMovieDbApi;
import io.askcloud.pvr.tvdb.TheTVDBApi;

public interface IHTPC {

	//public static String KODI_HOST = "192.168.0.21";
	public static String KODI_HOST = "localhost";
	public static int KODI_HTTP_PORT = 9080;
	public static int KODI_SOCKET_PORT = 9090;
	
	//Logger Details
	public Level LOG_LEVEL = Level.INFO;
	
	public String LOG_DOWNLOAD_TR = "io.askcloud.pvr.api.kodi.status";
	public String LOG_LOAD_KODI_STATUS_TR = "io.askcloud.pvr.api.kodi.loadstatus";
	public String LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR = "io.askcloud.pvr.api.kodi.monitor";
	public String LOG_FILE_BOT_TR = "io.askcloud.pvr.api.filebot";
	
	public static String PLEX_TVSHOWS_DIR =  "C:\\Entertainment\\TVShows";
	//public static String PLEX_TVSHOWS_DIR =  "C:\\tmp\\TVShows";
	
	public static final int KODI_DOWNLOAD_THREADS = 10;
	
	//when there are no more downloads to be process then wait 2 mins and check again
	public static final int KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME = 120000;
	
	//This flag is used to tell the program to continue to run and pick up new requests as they come in.
	//This means the program will never end
	public boolean WAIT_FOR_NEW_REQUESTS_NO_EXIT = false;
	
	public static String KODI_DOWNLOAD_BASE_DIR = "C:\\gitbash\\opt\\kodi\\downloads";
	public static String KODI_DOWNLOAD_TVSHOWS_DIR = KODI_DOWNLOAD_BASE_DIR + "\\tvshows";
	public static String KODI_DOWNLOAD_MOVIES_DIR = KODI_DOWNLOAD_BASE_DIR + "\\movies";

	public static String KODI_PVR_DOWNLOAD_FILE = "kodi-downloader.csv";
	public static String KDOI_PVR_DOWNLOAD_TRACKER = "C:/gitbash/opt/kodi/" + KODI_PVR_DOWNLOAD_FILE;
	public static String KDOI_PVR_DOWNLOAD_TRACKER_MASTER = "C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/kodi/plugin.video.exodus.updates/" + KODI_PVR_DOWNLOAD_FILE;

	//FileBot Groovy Scripts
	public static String FILEBOT_FIND_SERIES_MISSING_EPISODES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-missing.groovy";
	public static String FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-missing-excludes.txt";
	public static String FILEBOT_AMC_LOG="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/amc.log";
	public static String FILEBOT_AMC_EXCLUDE_LIST="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/amc-exclude.txt";
	
	public static String FILEBOT_FIND_SERIES_ENDED_EPISODES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-ended.groovy";
	public static String FILEBOT_FIND_SERIES_EPISODES_HAVE="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-have.groovy";
	
	
	public static String FILEBOT_SERIES_ENDED_EPISODES_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-ended.txt";
	public static String FILEBOT_SERIES_EPISODES_MISSING_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-episodes-missing.txt";
	public static String FILEBOT_SERIES_EPISODES_MISSING_QUEUE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-episodes-missing-queue.txt";
	public static String FILEBOT_MOVIE_MISSING_QUEUE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\movie-missing-queue.txt";
	public static String FILEBOT_SERIES_EPISODES_HAVE_FILE="C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\queue\\series-episodes-have.txt";
	
	public static String FILEBOT_AMC_DESTINATION="C:\\tmp\\CompletedDownloads";
	
	public static String FILE_BOT_EXE="C:\\gitbash\\opt\\filebot\\filebot.exe";
	


	//queue lock file wait time
	public static int QUEUE_LOCK_FILE_WAIT_TIME=10000; //10 seconds
	
	//Kodi Exodus Clear Cache Wait time for the thread so the cache can be completely cleared before continuing.
	public static int CLEAR_CACHE_THREAD_WAIT_TIME=10000; //10 seconds
	
	public static int KODI_MONITOR_DOWNLOAD_PERCENTAGE_CSV_FILE_LOADER=10000; //10 seconds
	
	public static int KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME=20000; //20 seconds

	
}
