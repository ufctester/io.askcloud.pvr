package io.askcloud.pvr.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import java.util.Properties;  
import javax.mail.*;  
import javax.mail.internet.*;  

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import io.askcloud.pvr.api.kodi.KodiDownloader;
import io.askcloud.pvr.api.kodi.KodiDownloader.KodiDownloaderDetails;
import io.askcloud.pvr.api.kodi.KodiTVShowDownloader;
import io.askcloud.pvr.api.utils.AMCBeyondCompareReportReviewer;
import io.askcloud.pvr.api.utils.GmailEmailClient;
import io.askcloud.pvr.kodi.jsonrpc.api.AbstractCall;
import io.askcloud.pvr.kodi.jsonrpc.api.call.Addons;
import io.askcloud.pvr.kodi.jsonrpc.config.HostConfig;
import io.askcloud.pvr.kodi.jsonrpc.io.ApiCallback;
import io.askcloud.pvr.kodi.jsonrpc.io.ConnectionListener;
import io.askcloud.pvr.kodi.jsonrpc.io.JavaConnectionManager;
import io.askcloud.pvr.kodi.jsonrpc.notification.AbstractEvent;
import io.askcloud.pvr.themoviedb.MovieDbException;
import io.askcloud.pvr.themoviedb.TheMovieDbApi;
import io.askcloud.pvr.tvdb.TheTVDBApi;

/**
 * 
 * @author UFCTester
 *
 */
public class HTPC extends HTPCOptions {
	private static HTPC eINSTANCE = null;
	
	public static final String CLASS_NAME = HTPC.class.getName();
	public static final Logger LOG = Logger.getLogger(CLASS_NAME);	

	private String CURRENT_DATE = null;
	
	//Kodi connection manager
	private JavaConnectionManager conMgr;
		
	private LinkedMap<String, KodiDownloader> kodiDownloaders = new LinkedMap<String, KodiDownloader>();
	
	private static Set<String> excludeMissingEpisodes = new LinkedHashSet<String>();
	
	//default is everything except the exlude list.  This can be overwritten
	private static Set<String> includeMissingEpisodes = new LinkedHashSet<String>();
	
    static
    {
		includeMissingEpisodes = new LinkedHashSet<String>();
		//includeMissingEpisodes.add("311946"); //Man with a Plan
    }
	
	private static Set<String> excludeMovies = new LinkedHashSet<String>();
	
    static
    {
    	excludeMissingEpisodes = new LinkedHashSet<String>();
//		//excludeMissingEpisodes.add("73141"); //American Dad!
//		excludeMissingEpisodes.add("76706"); //Big Brother
//		excludeMissingEpisodes.add("72549"); //Big Brother (UK)
//		excludeMissingEpisodes.add("81563"); //Border Security
//		excludeMissingEpisodes.add("261999"); //Border Security: Canada's Front Line
//		excludeMissingEpisodes.add("70760"); //Celebrity Big Brother
//		excludeMissingEpisodes.add("84107"); //Cheaters
//		excludeMissingEpisodes.add("74709"); //Cops
//		excludeMissingEpisodes.add("72546"); //CSI: Crime Scene Inves	
//		excludeMissingEpisodes.add("261190");// Decending
//		excludeMissingEpisodes.add("183831"); //Hardcore Pawn
//		excludeMissingEpisodes.add("75692"); //Law & Order: Special Victims Unit
//		//excludeMissingEpisodes.add("73388"); //MythBusters
//		excludeMissingEpisodes.add("75897"); //South Park
//		excludeMissingEpisodes.add("77666"); //The Amazing Race
//		excludeMissingEpisodes.add("71663"); //The Simpsons
//		excludeMissingEpisodes.add("230121"); //The X Factor (US)
//		excludeMissingEpisodes.add("97731"); //Tosh.0
    }
    	
	
	//TV and Movie DB API
	public TheTVDBApi tvdbAPI=null;
	public TheMovieDbApi moviedbAPI=null;
	
	
	public static void sleep(int milliseconds,String errorMsg)
	{
		errorMsg=(StringUtils.isNotBlank(errorMsg))?errorMsg:"";
		//sleap  
		try {
			Thread.sleep(milliseconds);
		}
		catch (Exception e) {
			long seconds = milliseconds / DateUtils.MILLIS_PER_SECOND;
			HTPC.LOG_DOWNLOAD_KODI_MONITOR_THREAD.severe("ERROR SLEEPING (" + seconds + "seconds). " + errorMsg + " Thread: " + Thread.currentThread().getName() + " exception: " + e.getMessage());
		}			
	}
	/**
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static long toSec(int milliseconds)
	{
		return milliseconds / DateUtils.MILLIS_PER_SECOND;
	}
	
	//delegate to Main.main
    public static void main(String[] args) {
    	Main.main(args);
	}    
    
    /**
     * @author ufctester
     *
     */
    public class BeyondCompareIsReadyToPublishToPlex extends DefaultExecuteResultHandler {
    	private String CN = BeyondComparePublishTOPlex.class.getName();
    	
    	
    	/**
    	 * BC_PUSH_AMC_TO_PLEX_BAT
    	 * BC_VALIDATE_AMC_READY_FOR_PLEX_BAT
    	 */
    	public BeyondCompareIsReadyToPublishToPlex() {
    		super();
    		
    		LOG.entering(CLASS_NAME, "BeyondCompareIsReadyToPublishToPlex");
    		
    		//Backup the previous report before the next one runs
    		String backupFileString = "";
    		try {
    			backupFileString = HTPC.getBEYOND_COMPARE_AMC_TV_REPORT_FILE().replace(".xml", "_" + getRunningDateId() + ".xml").replace(getLOGS_DIRECTORY(), getLOGS_ARCHIVES_DIRECTORY());
    			File sourceFile = new File(HTPC.getBEYOND_COMPARE_AMC_TV_REPORT_FILE());
    			if(sourceFile.exists())
    			{
        			File backupFile = new File(backupFileString);
        			FileUtils.copyFile(sourceFile, backupFile);
    			}    			
    		}
    		catch (Exception e) {
    			LOG.warning("Unable to backup AMC Beyond Compare Reort file: " + HTPC.getBEYOND_COMPARE_AMC_TV_REPORT_FILE() + " backupFile: " + backupFileString);
    		}
    		
    		try {
    			backupFileString = HTPC.getBEYOND_COMPARE_AMC_MOVIE_REPORT_FILE().replace(".xml", "_" + getRunningDateId() + ".xml").replace(getLOGS_DIRECTORY(), getLOGS_ARCHIVES_DIRECTORY());
    			File sourceFile = new File(HTPC.getBEYOND_COMPARE_AMC_MOVIE_REPORT_FILE());
    			if(sourceFile.exists())
    			{
        			File backupFile = new File(backupFileString);
        			FileUtils.copyFile(sourceFile, backupFile);
    			}
    		}
    		catch (Exception e) {
    			LOG.warning("Unable to backup AMC Beyond Compare Reort file: " + HTPC.getBEYOND_COMPARE_AMC_TV_REPORT_FILE() + " backupFile: " + backupFileString);
    		}
    		    		
    		LOG.exiting(CLASS_NAME, "BeyondCompareIsReadyToPublishToPlex");    		
    	}
    	
    	@Override
    	public void onProcessComplete(int exitValue) {
    		LOG.entering(CN, "onProcessComplete");
    		
    		LOG.info("Beyond Compare PASSED during Checking Is Ready To Publish To Plex.");
    		
    		
    		//View the Beyond Compare reports
    		LOG.info("Checking for conflicts with Plex TVShows");
    		AMCBeyondCompareReportReviewer reviewer = new AMCBeyondCompareReportReviewer(new File(HTPC.getBEYOND_COMPARE_AMC_TV_REPORT_FILE()));
    		reviewer.walkDocument();
    		
    		//conflicts so stop
    		if(reviewer.containsConflicts())
    		{
    			LOG.severe("AMC Beyond Compare Report contains conflicts with Plex TV Shows.");
        		LOG.exiting(CN, "onProcessComplete");
        		super.onProcessComplete(exitValue);
        		
        		HTPC.getInstance().exit(true);
        		return;
    		}
    		LOG.info("AMC Beyond Compare Report does not contain conflicts with Plex TV Shows.");
    		

    		LOG.info("Checking for conflicts with Plex Movies");
      		reviewer = new AMCBeyondCompareReportReviewer(new File(HTPC.getBEYOND_COMPARE_AMC_MOVIE_REPORT_FILE()));
       		reviewer.walkDocument();    	
    		
       		//conflicts so stop
    		if(reviewer.containsConflicts())
    		{
    			LOG.severe("AMC Beyond Compare Report contains conflicts with Plex TV Shows.");
        		LOG.exiting(CN, "onProcessComplete");
        		super.onProcessComplete(exitValue);
        		
        		HTPC.getInstance().exit(true);
        		return;
    		}    		
    		
    		LOG.info("AMC Beyond Compare Report does not contain conflicts with Plex Movies.");
    		
    		//if we got this far we have no conflicts
   			if(isPUBLISH_TO_PLEX())
   			{
   				pushDownloadsToPlex();
   			}    		
   			
    		LOG.exiting(CN, "onProcessComplete");
    		super.onProcessComplete(exitValue);
    		
    		HTPC.getInstance().exit(true);
    	
    	}
    	
    	@Override
    	public void onProcessFailed(ExecuteException e) {
    		LOG.entering(CN, "onProcessFailed",e);
 		
    		LOG.severe("Beyond Compare Failed during Checking Is Ready To Publish To Plex.");
    		LOG.exiting(CN, "onProcessFailed");
    		super.onProcessFailed(e);
    		HTPC.getInstance().exit(false);
    	}    	

    }
    
    /**
     * @author ufctester
     *
     */
    public class BeyondComparePublishTOPlex extends DefaultExecuteResultHandler {
    	private String CN = BeyondComparePublishTOPlex.class.getName();
    	
    	
    	/**
    	 * BC_PUSH_AMC_TO_PLEX_BAT
    	 * BC_VALIDATE_AMC_READY_FOR_PLEX_BAT
    	 */
    	public BeyondComparePublishTOPlex() {
    		super();
    	}
    	
    	@Override
    	public void onProcessComplete(int exitValue) {
    		LOG.entering(CN, "onProcessComplete");
    		
    		LOG.info("Beyond Compare push AMC to Plex has Completed.");
    		
    		LOG.exiting(CN, "onProcessComplete");
    		super.onProcessComplete(exitValue);
    		
    		HTPC.getInstance().exit(true);
    	
    	}
    	
    	@Override
    	public void onProcessFailed(ExecuteException e) {
    		LOG.entering(CN, "onProcessFailed",e);   		
    		LOG.info("Beyond Compare push AMC to Plex has Failed.");
    		LOG.exiting(CN, "onProcessFailed");
    		super.onProcessFailed(e);
    		HTPC.getInstance().exit(false);
    	}    	

    }    
    
    /**
     * @author ufctester
     *
     */
    public class FileBotExecuteResultHandler extends DefaultExecuteResultHandler {
    	private boolean publishToPlex = false;
    	private String CL = FileBotExecuteResultHandler.class.getName();
    	/**
    	 * 
    	 */
    	public FileBotExecuteResultHandler(boolean publishToPlex) {
    		super();
    		this.publishToPlex=publishToPlex;
    	}
    	
    	@Override
    	public void onProcessComplete(int exitValue) {
    		LOG.entering(CL, "onProcessComplete");
    		LOG.info("FileBot has Completed.");
    		
    		//next check if we are ready to publish to plex and if so then publish
    		if(publishToPlex)
    		{
    			isReadyToPublishToPlex();
    		}
    		
    		LOG.exiting(CL, "onProcessComplete");
    		super.onProcessComplete(exitValue);
    		HTPC.getInstance().exit(true);
    	}
    	
    	@Override
    	public void onProcessFailed(ExecuteException e) {
    		LOG.entering(CL, "onProcessFailed",e);
    		LOG.info("FileBot has Failed.");
    		LOG.exiting(CL, "onProcessFailed");
    		super.onProcessFailed(e);
    		HTPC.getInstance().exit(false);
    	}

    }

	/**
	 * @author ufctester
	 *
	 */
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
	
	//default is everything except the exlude list.  This can be overwritten
	private static Set<String> ENABLED_LOGGERS = new LinkedHashSet<String>();
	
    static
    {
    	ENABLED_LOGGERS = new LinkedHashSet<String>();
//    	ENABLED_LOGGERS.add("io.askcloud.pvr.api");
    	ENABLED_LOGGERS.add(HTPC.CLASS_NAME);
    	//ENABLED_LOGGERS.add(HTPC.LOG_OPTIONS_TR);
    	//ENABLED_LOGGERS.add(HTPCOptions.CLASS_NAME);
//    	ENABLED_LOGGERS.add("io.askcloud.pvr.api.main");
//    	ENABLED_LOGGERS.add("io.askcloud.pvr.api.kodi");
    	
    	ENABLED_LOGGERS.add(HTPC.LOG_DOWNLOAD_TR);
    	ENABLED_LOGGERS.add(HTPC.LOG_LOAD_KODI_STATUS_TR);
    	ENABLED_LOGGERS.add(HTPC.LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR);
    	ENABLED_LOGGERS.add(LOG_FILE_BOT_TR);
    }
    
	public class HTPCLogFilter implements Filter {

		/**
		 * 
		 */
		public HTPCLogFilter() {
			super();
		}    

		/* (non-Javadoc)
		 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
		 */
		@Override
		public boolean isLoggable(LogRecord record) {
			if (record == null)
				return false;
			
			Level level = record.getLevel();
			String loggerName=record.getLoggerName();
			String message = record.getMessage() == null ? "" : record.getMessage();
			
			if(ENABLED_LOGGERS.isEmpty())
			{
				return true;
			}
			else
			{
				for (String pattern : ENABLED_LOGGERS) {
					if (loggerName.contains(pattern))
						return true;				
				}
			}
			return false;
		}

	}
	

	private HTPC() {

		super();
		init();
	}

	private void init() {
		LOG.entering(CLASS_NAME, "init");
				
		//Init Logger
		initLogger();
		
		//Delete any lock files in case the previous run did not complete and left the lock file behind
		deleteQuietly(HTPC.getDOWNLOAD_QUEUE_LOCK_FILE());
		deleteQuietly(HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK());
		clearCacheAndSources();
		
		//copy master kodi-downloader.csv download file 
		File sourceFile = new File(HTPC.getKDOI_PVR_DOWNLOAD_TRACKER_MASTER());
		File targetFile = new File(HTPC.getKDOI_PVR_DOWNLOAD_TRACKER());
		try {
			FileUtils.copyFile(sourceFile, targetFile);
		}
		catch (Exception e) {
			LOG.severe("ERROR copying file: " + HTPC.getKDOI_PVR_DOWNLOAD_TRACKER_MASTER() + " to file: " + HTPC.getKDOI_PVR_DOWNLOAD_TRACKER());
		}

		if(HTPC.isCLEAN_KODI_DOWNLOAD())
		{
			//Kodi TVShows
			recreateDirectory(HTPC.getKODI_DOWNLOAD_TVSHOWS_DIR());

			//Kodi Movies
			recreateDirectory(HTPC.getKODI_DOWNLOAD_MOVIES_DIR());
			
			//remove old amc folder
			deleteQuietly(HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR());
			
			//remove old completed folder
			deleteQuietly(HTPC.getKODI_DOWNLOAD_COMPLETED_DIR());
		}			
		
		LOG.exiting(CLASS_NAME, "init");
	}	


	private void printFileSizes(File directory) {
		if ((directory == null) || (!directory.exists())) {
			return;
		}
		Collection<File> files = FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.TRUE);

		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if (file.isDirectory()) {
				//LOG.finest("Directory Found: " + file.toString());
			}
			else {
				LOG.info("Size: " + file.length() + "  File Found: " + file.toString());
			}
		}
	}

	private void initLogger() {

		//  //Handler handler = new ConsoleHandler();
		//  SimpleFormatter formatter = new SimpleFormatter();
		//  
		//  Handler handler = new StreamHandler(System.out,formatter);
		//  handler.setLevel(LOG_LEVEL);
		//  LOG.setLevel(LOG_LEVEL);
		//  //LOG.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		//  LOG.addHandler(handler);

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
		consoleHandler.setFilter(new HTPCLogFilter());
		LOG.setLevel(LOG_LEVEL);

		//Logger.getLogger("").setLevel( Level.OFF ); // Solution 2

		//set the console handler to fine:
		consoleHandler.setLevel(LOG_LEVEL);
	}

	public static HTPC getInstance() {
		if (eINSTANCE == null) {
			eINSTANCE = new HTPC();
		}
		return eINSTANCE;
	}

	/**
	 * @param passed
	 */
	public void exit(boolean passed)
	{
		if(isEXIT_JAVA_PROGRAM())
		{
			System.exit(0);
		}		
	}

	
	public String getRunningDateId() {
		if(CURRENT_DATE == null)
		{
			Date myDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
			CURRENT_DATE = sdf.format(myDate);
		}
		return CURRENT_DATE;
	}
	
	/**
	 * Runs AMC and then publishes to plex if the publishToPlex argument is set to true
	 * @param args
	 */
	private void callFileBotAMC(String[] args,boolean publishToPlex)
	{
//		LOG.entering(CLASS_NAME, "callFileBot",args);
//		try {
//			net.filebot.Main.main(args);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			LOG.severe("Error calling Filebot with args: "  + args);
//		}

		StringBuffer commandArgs = new StringBuffer();
        for (String arg : args)
        	commandArgs.append(" " + arg);
                
        try {
        	DefaultExecuteResultHandler rh = new FileBotExecuteResultHandler(publishToPlex);
            String line = getFILE_BOT_EXE() + commandArgs;
        	LOG.info("Calling Filebot: " + line);
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine,rh);   
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LOG.exiting(CLASS_NAME, "callFileBot");
	}
	
	/**
	 * Main.main(new String[]{"-list", "--db", "thetvdb", "--q", "Dexter","--format", "{plex}"});
	 * @param args
	 */
	private void callFileBot(String[] args)
	{
//		LOG.entering(CLASS_NAME, "callFileBot",args);
//		try {
//			net.filebot.Main.main(args);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			LOG.severe("Error calling Filebot with args: "  + args);
//		}

		StringBuffer commandArgs = new StringBuffer();
        for (String arg : args)
        	commandArgs.append(" " + arg);
                
        try {
        	DefaultExecuteResultHandler rh = new FileBotExecuteResultHandler(false);
            String line = getFILE_BOT_EXE() + commandArgs;
        	LOG.info("Calling Filebot: " + line);
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine,rh);   
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LOG.exiting(CLASS_NAME, "callFileBot");
	}
	
	/**
	 * 
	 */
	public void publishDownloadsToPlex(LinkedMap<String, KodiDownloader> downloader)
	{
		LOG.entering(CLASS_NAME, "publishDownloadsToPlex");
		
		LOG.info("Copying Files to the Completed folder: " + HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR());
		for (KodiDownloader downloadItem : downloader.values()) {
			
			//Copy all of the completed downloads to the completed folder for AMC to pick up
			if(downloadItem.isComplete())
			{
				LOG.info("Completd items: " + downloadItem.toString());
				
				try {
					//copy the file to the target destination
					//C:\gitbash\opt\kodi\downloads\tvshows\Tosh.0\Season 8\Tosh.0 S08E01.mp4
					String sourceFile = downloadItem.getLatestKodiDownloadDetails().getFile();
					String targetFile = sourceFile.replace(HTPC.getKODI_DOWNLOAD_BASE_DIR(), HTPC.getKODI_DOWNLOAD_COMPLETED_DIR());
					FileUtils.copyFile(new File(sourceFile), new File(targetFile));
				}
				catch (Exception e) {
					LOG.severe("Error occured copying completed download: " + e.getMessage());
				}
			}
		}
		
		//next run amc with the 
		runAutomatedMediaCenter(true);
		LOG.exiting(CLASS_NAME, "publishDownloadsToPlex");		
	}
	
	/**
	 * This method used beyond compare and pushes the changes to plex
	 * @param args
	 */
	private void pushDownloadsToPlex()
	{ 
		LOG.entering(CLASS_NAME, "pushDownloadsToPlex");
        try {
        	DefaultExecuteResultHandler rh = new BeyondComparePublishTOPlex();
            String line = getBC_PUSH_AMC_TO_PLEX_BAT();
        	LOG.info("Calling Beyond Compare to Push AMC Updates to Plex");
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine,rh);   
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LOG.exiting(CLASS_NAME, "pushDownloadsToPlex");
	}	
	
	/**
	 * This method used beyond compare and pushes the changes to plex
	 * @param args
	 */
	public void isReadyToPublishToPlex()
	{ 
		LOG.entering(CLASS_NAME, "isReadyToPublishToPlex");
		
		/*
		 * we need to ensure that the AMC Completed folder exists and that it contains the 
		 * right directories.  
		 * 
		 * HTPC.KODI_DOWNLOAD_COMPLETED_AMC_TVSHOW_DIR - C:\\gitbash\\opt\\kodi\\amccompleted\\TVShows
		 * HTPC.KODI_DOWNLOAD_COMPLETED_AMC_MOVIES_DIR - C:\\gitbash\\opt\\kodi\\amccompleted\\Movies
		 * 
		 */
		
		//amc folder should exist
		File amcDirectory = new File(HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR());
		
		File amcMoviesDirectory = new File(HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_MOVIES_DIR());
		File amcTVShowsDirectory = new File(HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_TVSHOW_DIR());
		File tvShowsIncorrectDirectory=new File(getKODI_DOWNLOAD_COMPLETED_AMC_DIR() + File.separator + "TV Shows");
		
		if(!amcDirectory.exists())
		{
			LOG.severe("The Kodi AMC folder does not even exist.  AMC has not run on it yet.");
			LOG.exiting(CLASS_NAME, "isReadyToPublishToPlex");
			return;
		}
		else
		{
			/*
			 * In here we can have different scenarios
			 *   1) AMC creates tv show directory <AMC_DIR>/TV Shows and we need it to be TVShows
			 *   2) Ensure that the Movie and TVShow directory exists and they should have at least 1 file to process it
			 */
			//AMC creates a directory <AMC_DIR>/TV Shows but Plex needs it to be TVShows so we need to check and rename
			//Filebot puts TV Shows under a TV Show directory but we want it under TVShows

			if(tvShowsIncorrectDirectory.exists())
			{
				tvShowsIncorrectDirectory.renameTo(amcTVShowsDirectory);				
			}

			try {
				/*
				 * Ensure the directory is created.  In this case if it creates it then there must be no tv shows to process 
				 * but we need the directory to do the compare as there  might be movies
				 */
				FileUtils.forceMkdir(amcTVShowsDirectory);
				
				//the directory is already renamed if it gets here.
				FileUtils.forceMkdir(amcMoviesDirectory);
			}
			catch (Exception e) {
				LOG.severe("Error occured when creating AMC Movies or TVShows directory.  Exception: " + e.getMessage());
				LOG.exiting(CLASS_NAME, "isReadyToPublishToPlex");
				return;
			}
		}
		

		//Run the Beyond Compare report generator
        try {
        	DefaultExecuteResultHandler rh = new BeyondCompareIsReadyToPublishToPlex();
            String line = getBC_VALIDATE_AMC_READY_FOR_PLEX_BAT();
        	LOG.info("Calling Beyond Compare ensure what we downloaded does not conflict with anything already on plex");
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine,rh);   
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
        LOG.exiting(CLASS_NAME, "isReadyToPublishToPlex");
	}	
	/**
	 * @param showName
	 */
	public void getTVEpisodes(String showName) {
		//Main.main(new String[] { "-list", "--db", "thetvdb", "--q", showName, "--format", "{n} - {s00e00} - {t}" });
	}

	/**
	 * @param name
	 */
	public void findTVShowPlex(String name) {
		//Main.main(new String[] { "-list", "--db", "thetvdb", "--q", "Dexter", "--format", "{plex}" });
	}


	/**
	 * @param directory
	 * @return
	 */
	public void findTVShowEpisodesHave(String directory) {
		LOG.entering(CLASS_NAME, "findTVShowEpisodesHave", new Object[] {directory});
		File missingEpisodeFile = new File(HTPC.getFILEBOT_SERIES_EPISODES_HAVE_FILE());
		try {
			LOG.info("Deleting old missing episode file: " + missingEpisodeFile);
			FileUtils.deleteQuietly(missingEpisodeFile);
		}
		catch (Exception e) {
			LOG.severe("ERROR deleting file: " + missingEpisodeFile);
		}
		
		try {		
			directory="\"" + directory + "\"";
			callFileBot(new String[] { "-script", HTPC.getFILEBOT_FIND_SERIES_EPISODES_HAVE(), directory,"--output",getFILEBOT_SERIES_EPISODES_HAVE_FILE() , "--log", "info"});
		}
		catch(SecurityException e)
		{
			LOG.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		LOG.exiting(CLASS_NAME, "findTVShowEpisodesHave");
	}
	
	/**
	 * @param directory
	 */
	public void findMissingTVShowEpisodes(String directory) {
		LOG.entering(CLASS_NAME, "findMissingEpisodes", new Object[] {directory});
		deleteQuietly(HTPC.getDOWNLOAD_QUEUE_FILE());
		
		try {		
			
			//Main.main(new String[] { "-script", PlexPVRManager.FILE_BOT_FIND_MISSING_EPISODES, directory,"--output",PlexPVRManager.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILE_BOT_FIND_MISSING_EPISODES_EXCLUDES,"--log", "all"});
			//callFileBot(new String[] { "-script", PlexPVRManager.FILE_BOT_FIND_MISSING_EPISODES, directory,"--output",PlexPVRManager.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILE_BOT_FIND_MISSING_EPISODES_EXCLUDES,"--log", "info"});
			//Main.main(new String[] { "-list", "--db", "thetvdb", "--q", "Dexter", "--format", "{plex}" });
			directory="\"" + directory + "\"";
			callFileBot(new String[] { "-script", HTPC.getFILEBOT_FIND_SERIES_MISSING_EPISODES(), directory,"--output",HTPC.getDOWNLOAD_QUEUE_FILE() , "--def", "excludeList=" + getFILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES(),"--log", "info"});
		}
		catch(SecurityException e)
		{
			LOG.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		LOG.exiting(CLASS_NAME, "findMissingEpisodes");
	}
	
	/**
	 * @param directory
	 * @return
	 */
	public void findCompletedEpisodes(String directory) {
		LOG.entering(CLASS_NAME, "findCompletedEpisodes", new Object[] {directory});
		deleteQuietly(HTPC.getFILEBOT_SERIES_ENDED_EPISODES_FILE());

		try {			
			//Main.main(new String[] { "-script", PlexPVRManager.FILEBOT_FIND_SERIES_ENDED_EPISODES, directory, "--output",PlexPVRManager.FILEBOT_SERIES_ENDED_EPISODES_FILE });
			callFileBot(new String[] { "-script", HTPC.getFILEBOT_FIND_SERIES_ENDED_EPISODES(), directory, "--output",HTPC.getFILEBOT_SERIES_ENDED_EPISODES_FILE() });
		}
		catch(SecurityException e)
		{
			LOG.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		LOG.exiting(CLASS_NAME, "findCompletedEpisodes");
	}	
	
	/**
	 * 
	 */
	public void runAutomatedMediaCenter(boolean publishToPlex) {
		LOG.entering(CLASS_NAME, "automatedMediaCenter");
		 
		File targetDirectoryFile = new File(HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR());
		if(!targetDirectoryFile.exists())
		{
			try {
				//FIXME Add the mkdir to tvshows and movies
				FileUtils.forceMkdir(targetDirectoryFile);
			}
			catch (Exception e) {
				LOG.severe("Error trying to create directory: " + HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR());
			}
		}
				
		String fileBotDestForwardSlashes=HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR().replace("\\\\", "/");
		try {
			//Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "copy", "-non-strict", sourceDirectory, "--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
			//		"subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "ecludeList", "amc-exclude.txt", "--def", "--log", "all" });
			
			String[] args = new String[] { "-script", "fn:amc", "--output", HTPC.getKODI_DOWNLOAD_COMPLETED_AMC_DIR(), "--action", "copy", "-non-strict", HTPC.getKODI_DOWNLOAD_COMPLETED_DIR(), 
					"--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
					"subtitles", "en", "music", "y", "artwork", "n", "--log-file", getFILEBOT_AMC_LOG(), "--def", "excludeList=" + getFILEBOT_AMC_EXCLUDE_LIST(), "--log", "all" };					
					
			callFileBotAMC(args,publishToPlex);
		}
		catch(SecurityException e)
		{
			LOG.severe("Error Handling Filebot AMC: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		LOG.exiting(CLASS_NAME, "automatedMediaCenter");		
	}

	/**
	 * @param sourceDirectory
	 * @param targetDirectory
	 */
	public void automatedMediaCenterTest(String sourceDirectory, String targetDirectory) {
		
		
		String fileBotDestForwardSlashes=targetDirectory.replace("\\\\", "/");
		try {
			//Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "test", "-non-strict", sourceDirectory, "--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
			//		"subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "ecludeList", "amc-exclude.txt", "--def", "--log", "all" });
			
			String[] args = new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "test", "-non-strict", sourceDirectory, 
					"--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
					"subtitles", "en", "music", "y", "artwork", "n", "--log-file", getFILEBOT_AMC_LOG(), "--def", "excludeList=" + getFILEBOT_AMC_EXCLUDE_LIST(), "--log", "all" };					
					
			callFileBot(args);
		}
		catch(SecurityException e)
		{
			LOG.severe("Error Handling Filebot AMC: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}					
	}

	/**
	 * @return
	 */
	public TheTVDBApi getTvdbAPI()
	{
		if(tvdbAPI == null)
		{
			tvdbAPI = new TheTVDBApi("0E6F56E34D3CC366");
		}
		return tvdbAPI;
	}
	
	/**
	 * @return
	 */
	public TheMovieDbApi getTheMovieDbAPI()
	{
		if(moviedbAPI == null)
		{
			try {
				moviedbAPI = new TheMovieDbApi("db6327125f93ae90aa51493f1586713f");
			}
			catch (MovieDbException e) {
				LOG.severe("Error trying to get the movie db api: " + e.getMessage());
			}
		}
		return moviedbAPI;
	}
	
	
	public void recreateDirectory(String directory)
	{
		LOG.entering(CLASS_NAME, "recreateDirectory", new Object[] {directory});
		
		try {
			
			File targetDirectoryFile = new File(directory);
			LOG.info("Deleting Old Directory and Files: " + directory);
			printFilesAndDirs(targetDirectoryFile);
			FileUtils.deleteQuietly(targetDirectoryFile);
			
			FileUtils.forceMkdir(targetDirectoryFile);
			
			LOG.info("Old Files Should Be Gone: " + directory);
			printFilesAndDirs(targetDirectoryFile);			
		}
		catch (Exception e) {
			LOG.severe("Unable to recreate directory: " + directory + " exception: " + e.getMessage());
		}
		LOG.exiting(CLASS_NAME, "recreateDirectory");
	}
	
	private void printFilesAndDirs(File directory) {
		if ((directory == null) || (!directory.exists())) {
			return;
		}
		Collection<File> files = FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.TRUE);

		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if (file.isDirectory()) {
				LOG.info("Directory Found: " + file.toString());
			}
			else {
				LOG.info("     File Found: " + file.toString());
			}
		}
	}		

	public JavaConnectionManager getConMgr() {
		if (conMgr == null) {
			HostConfig config = new HostConfig(HTPC.getKODI_HOST(), HTPC.getKODI_HTTP_PORT(), HTPC.getKODI_SOCKET_PORT());
			conMgr = new JavaConnectionManager();
			conMgr.registerConnectionListener(new ConnectionListener() {

				@Override
				public void notificationReceived(AbstractEvent event) {
					LOG.fine("Event received: " + event.getClass().getCanonicalName());
				}

				@Override
				public void disconnected() {
					LOG.fine("Event: Disconnected");

				}

				@Override
				public void connected() {
					LOG.fine("Event: Connected");

				}
			});
			LOG.info("Connecting...");
			conMgr.connect(config);
		}
		return conMgr;
	}
	
	/**
	 * plugin://plugin.video.exodus/?action=clearCacheAndSources
	 */
	public void clearCacheAndSources() {
		LOG.entering(CLASS_NAME, "clearCacheAndSources");
		String clearCacheAndSourcesCacheURL="?action=clearCacheAndSources";

		LOG.info("url: " + clearCacheAndSourcesCacheURL);
		
		Addons.ExecuteAddon exodus = new Addons.ExecuteAddon("plugin.video.exodus",clearCacheAndSourcesCacheURL);

		try {
			getConMgr().call(exodus, new ApiCallback<String>() {

				@Override
				public void onResponse(AbstractCall<String> call) {
					LOG.entering(CLASS_NAME, "clearCacheAndSources::onResponse", new Object[] { call });
				}

				@Override
				public void onError(int code, String message, String hint) {
					LOG.severe("ERROR " + code + ": " + message);
				}
			});
		}
		catch (Exception e) {
			LOG.severe("Unable to connect to Kodi: " + e.getMessage());
		}
		
		//sleap 
		HTPC.sleep(HTPC.getCLEAR_CACHE_THREAD_WAIT_TIME(), "Error while waiting on for kodi cache to be cleard");
		LOG.exiting(CLASS_NAME, "clearCacheAndSources");
	}
	
	/**
	 * 
	 */
	synchronized private void loadKodiDownloadStatusUpdates() {
		LOG.entering(CLASS_NAME, "loadKodiDownloadStatusUpdates");
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        
        try {
        	inputStream = new FileInputStream(getKDOI_PVR_DOWNLOAD_TRACKER());
            reader = new InputStreamReader(inputStream, "UTF-8");
            CSVFormat format = CSVFormat.EXCEL.withHeader().withQuoteMode(QuoteMode.MINIMAL);
            parser = new CSVParser(reader, format);
            
            for (final CSVRecord record : parser) {
            	final String percent = record.get("PERCENT").replaceAll("%","");
            	final String file = record.get("FILE");
            	final String totalDownloadSize = record.get("DOWNLOADSIZE");
            	final String totalsize = record.get("TOTALSIZE");
            	
            	//FIXME This should never happen as this is the kodi download file
            	if(StringUtils.isBlank(file))
            	{
            		continue;
            	}
            	
            	/*
            	 * Next we need to update the KodiDownloader objects if the following conditions are true
            	 *   1) The kodiDownloaders have been loaded (kodiDownloaders is not empty)
            	 *   2) The kodiDownloader has the flag isReadyToRun set
            	 *   3) If the kodiDownloader is not stopped (ie not complete or not failed)
            	 */
            	Map<String,KodiDownloader> synchMap = Collections.synchronizedMap(kodiDownloaders);
            	//Set keySet = synchMap.key()();  // Needn't be in synchronized block
            	synchronized (synchMap) {  // Synchronizing on m, not s!
                	for (String identifier : synchMap.keySet()) {
                		KodiDownloader kodiDownloader = synchMap.get(identifier);
                		
                		//we found a match so update the status and break
                		if(file.toLowerCase().contains(kodiDownloader.getDownloadStatusIdentifier().toLowerCase()))
                		{
                			kodiDownloader.addKodiDownloadStatus(percent,file,totalDownloadSize,totalsize);
                			break;
                		}
    				}            		
//            		Iterator<KodiDownloader> i = keySet.iterator(); // Must be in synchronized block
//            		while (i.hasNext())
//            			foo(i.next());
            	}       
//            	
//            	synchronized (kodiDownloaders) {
//                	for (String identifier : kodiDownloaders.keySet()) {
//                		KodiDownloader kodiDownloader = kodiDownloaders.get(identifier);
//                		
//                		//we found a match so update the status and continue
//                		if(file.toLowerCase().contains(kodiDownloader.getDownloadStatusIdentifier().toLowerCase()))
//                		{
//                			kodiDownloader.addKodiDownloadStatus(percent,file,downloadsize,toString());
//                			continue;
//                		}
//    				}					
//				}
            }
        }catch (Exception e) {
			LOG.warning("Unable to read " + getKODI_PVR_DOWNLOAD_FILE() + ". This might be because it is missing the header: " + e.getMessage());
			e.printStackTrace();
		}
        finally {
            try {
            	parser.close();	
			}
			catch (Exception e) {
				LOG.warning("Unable to close " + HTPC.getKODI_PVR_DOWNLOAD_FILE() + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				LOG.warning("Unable to close " + HTPC.getKODI_PVR_DOWNLOAD_FILE() + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				LOG.warning("Unable to close " + HTPC.getKODI_PVR_DOWNLOAD_FILE() + " InputStream: " + e.getMessage());
			}            
        }
        
        LOG.exiting(CLASS_NAME, "loadKodiDownloadStatusUpdates");      
	}
	
	/**
	 * 
	 */
	public void startDownloadFromDownloadQueue()
	{
		LOG.entering(CLASS_NAME, "downloadFromDownloadQueue");
		
		while(true)
		{			
			LOG.info("Calling: PlexPVRManager.getInstance().downloadFromDownloadQueue()");
			
			//set the download items
			kodiDownloaders = loadDownloadQueue(HTPC.getDOWNLOAD_QUEUE_FILE(),HTPC.getDOWNLOAD_QUEUE_LOCK_FILE());
			if(!kodiDownloaders.isEmpty())
			{
				download();
				
				LOG.info("Completd: downloadFromDownloadQueue");
				publishDownloadsToPlex(kodiDownloaders);
				
				//FIXME We need to handle the completed downloads and then continue to loop to pick up new requests
				break;
			}
			else
			{
				if(HTPC.isWAIT_FOR_NEW_REQUESTS_NO_EXIT())
				{
					HTPC.sleep(HTPC.getKODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME(), "Error while waiting on new requests to come to the QUEUE..");
				}
				else
				{
					LOG.info("There are no more shows to process.");
					break;
				}
			}
		}

		LOG.entering(CLASS_NAME, "downloadFromDownloadQueue");
	}
	
	/**
	 * 
	 */
	private void download()
	{
		ExecutorService executor = Executors.newFixedThreadPool(HTPC.getKODI_DOWNLOAD_THREADS());
		
		//here we need to set the first download item ready to run
		if(kodiDownloaders.size()>0)
		{
			kodiDownloaders.getValue(0).setReadyToRun();
		}
		
		for (KodiDownloader downloadItem : kodiDownloaders.values()) {
	        executor.submit(downloadItem);
		}
		
		executor.shutdown();
        while (!executor.isTerminated()) {
			HTPC.sleep(HTPC.getKODI_UPDATING_PROGRESS_SLEEP_TIME(), "Error while waiting on new requests to come to the QUEUE.");
						
			//Update the KodiDownloader's
			updateDownloadQueue(kodiDownloaders);
        }
        
        //notify the that the threads have completed so downloading is done and we need to notify the kodi pooling monitor
        LOG.info("Finished all download threads for batch download");
	}
	
	/**
	 * FIXME This method gets called from other threads.
	 * @param downloader
	 */
	synchronized public void readyToProcessNextRequest(KodiDownloader downloader) {
		int index = kodiDownloaders.indexOf(downloader.getDownloadStatusIdentifier());
		
		//FIXME ... if the index is -1 then it could not be found
		if(index == -1)
		{
			LOG.severe("Error trying to find the next request to process: " + downloader.getDownloadStatusIdentifier());
		}
		
		index++;
		if(index < kodiDownloaders.size())
		{
			//set the next downloader ready to run
			kodiDownloaders.getValue(index).setReadyToRun();
		}
	}	
	
	

	/**
	 * When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFileString
	 * @return
	 */
	@SuppressWarnings("unused")
	synchronized public LinkedMap<String,KodiDownloader> loadDownloadQueue(String downloadQueueFileString,String downloadQueueLockFileString) {
		LOG.entering(CLASS_NAME, "loadDownloadQueue");
		LinkedMap<String,KodiDownloader> downloadShows = new LinkedMap<String,KodiDownloader>();
				
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        File downloadQueueFileLock = null;
        List<String[]> updatedRecords = new ArrayList<String[]>();
        
        try {
        	downloadQueueFileLock = waitForLockFile(downloadQueueFileString,downloadQueueLockFileString);
        	inputStream = new FileInputStream(downloadQueueFileLock);
            reader = new InputStreamReader(inputStream, "UTF-8");
            
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            
            List<CSVRecord> records = parser.getRecords();

            for (int i = 0; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				
            	String tvdbid = record.get(HTPC.CSV_TVDB_ID);
            	String imdbid = record.get(HTPC.CSV_IMDB_ID);
            	String showName = record.get(HTPC.CSV_NAME);
            	String season = record.get(HTPC.CSV_SEASON);
            	String episode = record.get(HTPC.CSV_EPISODE);
            	String ended = record.get(HTPC.CSV_ENDED);
            	String status = record.get(HTPC.CSV_STATUS);
            	String downloadPercent = record.get(HTPC.CSV_DOWNLOAD_PERCENT);
            	String file = record.get(HTPC.CSV_FILE);
            	String totalDownloadedSize = record.get(HTPC.CSV_DOWNLOADSIZE); //MB
            	String totalSize = record.get(HTPC.CSV_TOTALSIZE); //MB
				
            	boolean seriesEnded=(StringUtils.isNotBlank(ended)) && ("true".equals(ended))?true: false;
            	
        		KodiDownloader download = KodiDownloader.createKodiDownloader(tvdbid,imdbid,showName,season,episode,seriesEnded,status,downloadPercent,file,totalDownloadedSize,totalSize);
        		downloadShows.put(download.getDownloadStatusIdentifier(), download);
            }

		}
		catch (Exception e) {
			// FIXME: handle exception
			LOG.severe("Error loading download requests: " + downloadQueueLockFileString);
		}
        finally {
            try {  
            	parser.close();	
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + downloadQueueLockFileString + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + downloadQueueLockFileString + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + downloadQueueLockFileString + " InputStream: " + e.getMessage());
			}   
            //delete the lock file
            try {
            	if((downloadQueueFileLock != null) && (downloadQueueFileLock.exists()))
            	{
            		downloadQueueFileLock.delete();
            	}
			}
			catch (Exception e) {
				LOG.severe("ERROR: Unable to close " + downloadQueueFileString + " InputStream: " + e.getMessage());
			}
        }
        
        LOG.exiting(CLASS_NAME, "getDownloadsFromDownloadQueueFile",downloadShows);
    	return downloadShows;        
	}		
	
	
	/**
	 * When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFileString
	 * @return
	 */
	@SuppressWarnings("unused")
	synchronized private void updateDownloadQueue(LinkedMap <String, KodiDownloader> activeDownloaders) {
		LOG.entering(CLASS_NAME, "updateDownloadQueue",new Object[]{activeDownloaders});
		
		//First reload the Kodi Download Status File
		loadKodiDownloadStatusUpdates();
				
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        FileWriter csvFileWriter=null;
        CSVPrinter cvsPrinter=null;
        File downloadQueueFileLock = null;
        List<String[]> updatedRecords = new ArrayList<String[]>();
        
        try {
        	//delete the new HTPC.DOWNLOAD_QUEUE_ACTIVE_FILE and then copy the master queue file
        	FileUtils.deleteQuietly(new File(HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE()));
        	FileUtils.copyFile(new File(HTPC.getDOWNLOAD_QUEUE_FILE()), new File(HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE()));
        	downloadQueueFileLock = waitForLockFile(HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE(),HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK());
        	inputStream = new FileInputStream(downloadQueueFileLock);
            reader = new InputStreamReader(inputStream, "UTF-8");
            
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            
            List<CSVRecord> records = parser.getRecords();
            
            for (int i = 0; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				
            	String tvdbid = record.get(HTPC.CSV_TVDB_ID);
            	String imdbid = record.get(HTPC.CSV_IMDB_ID);
            	String name = record.get(HTPC.CSV_NAME);
            	String season = record.get(HTPC.CSV_SEASON);
            	String episode = record.get(HTPC.CSV_EPISODE);
            	String ended = record.get(HTPC.CSV_ENDED);
            	String status = record.get(HTPC.CSV_STATUS);
            	String downloadPercent = record.get(HTPC.CSV_DOWNLOAD_PERCENT);
            	String file = record.get(HTPC.CSV_FILE);
            	String totalDownloadedSize = record.get(HTPC.CSV_DOWNLOADSIZE); //MB
            	String totalSize = record.get(HTPC.CSV_TOTALSIZE); //MB

            	boolean seriesEnded=(StringUtils.isNotBlank(ended)) && ("true".equals(ended))?true: false;
            	
            	String[] originalRecord = {tvdbid,imdbid,name,season,episode,ended,HTPC.getDOWNLOAD_STATUS_SNATCHED(),downloadPercent,file,totalDownloadedSize,totalSize};
            	
            	for (KodiDownloader downloader : activeDownloaders.values()) {
            		
					String kodiSeasonNumber = "";
					String kodiEpisodeNumber = "";
					
					if(downloader instanceof KodiTVShowDownloader)
					{
						kodiSeasonNumber = String.valueOf(((KodiTVShowDownloader)downloader).getSeason());
						kodiEpisodeNumber = String.valueOf(((KodiTVShowDownloader)downloader).getEpisode());						
					}
					KodiDownloaderDetails downloadDetails = downloader.getLatestKodiDownloadDetails();
					//find the record
					if((downloader.showNameMatches(name)) && (season.equals(kodiSeasonNumber)) && (episode.equals(kodiEpisodeNumber)))
					{
						String[] csv = downloader.toCSV();
						LOG.fine("DOWNLOAD_PERCENT UPDATE: " + csv);
						updatedRecords.add(csv);
					}
				}
            }
                
            //Save the updated status file
            if(!updatedRecords.isEmpty())
            {
                //if we got this far then we must have the new list of pruned download videos
                File csvOutputFile=new File(HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE());
                CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(HTPC.CSV_HEADER);
                csvFileWriter = new FileWriter(csvOutputFile);
                cvsPrinter = new CSVPrinter(csvFileWriter, csvFileFormat);
                
                //print the records
                cvsPrinter.printRecords(updatedRecords); 
            }

		}
		catch (Exception e) {
			// FIXME: handle exception
			LOG.severe("Error loading download requests: " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK());
		}
        finally {
            try {  
            	parser.close();	
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK() + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK() + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE_LOCK() + " InputStream: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.flush();
			}
			catch (Exception e) {
				LOG.severe("Unable to flush " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE() + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE() + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	cvsPrinter.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE() + " cvsPrinter: " + e.getMessage());
			}
            
            //delete the lock file
            try {
            	if((downloadQueueFileLock != null) && (downloadQueueFileLock.exists()))
            	{
            		downloadQueueFileLock.delete();
            	}
			}
			catch (Exception e) {
				LOG.severe("ERROR: Unable to close " + HTPC.getDOWNLOAD_QUEUE_ACTIVE_FILE() + " InputStream: " + e.getMessage());
			}
        }
        
        LOG.exiting(CLASS_NAME, "updateDownloadQueue");      
	}			
	
		
	/**
	 * FIXME DSP When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFile
	 * @return
	 */
	synchronized private File waitForLockFile(String sourceFile, String sourceLockFile) {
		LOG.entering(CLASS_NAME, "waitForLockFile");

        File masterFileLock = new File(sourceLockFile);
        File masterFile = new File(sourceFile);
        
        //FIXME Check to see if there is a lock file and if not create one
        while(true)
        {    
            if(masterFileLock.exists())
            {
            	LOG.info("Wating for file lock to be removed.");
            	HTPC.sleep(HTPC.getQUEUE_LOCK_FILE_WAIT_TIME(), "waiting while sleeping while polling for lock file.");
            }
            else
            {
            	try {
            		FileUtils.copyFile(masterFile, masterFileLock);
            		if(masterFileLock.exists())
            		{
            			break;
            		}
    			}
    			catch (Exception e) {
    				LOG.severe("Could not create lock file for: " + sourceFile + " lock file: " + sourceLockFile);
    			}
            }
        }
        
    	return masterFileLock;        
	}		
	
	/**
	 * @return
	 */
	public GmailEmailClient getGmailEmailClient() {
		LOG.entering(CLASS_NAME, "getGmailEmailClient");
		GmailEmailClient client = GmailEmailClient.getInstance();
		LOG.exiting(CLASS_NAME, "getGmailEmailClient",client);
		return client;
	}	
	
	/**
	 * @param file
	 */
	public void deleteQuietly(String file)
	{
		if(file != null)
		{
			File fileObj = new File(file);
			FileUtils.deleteQuietly(fileObj);
		}
	}
}

