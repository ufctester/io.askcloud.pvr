package io.askcloud.pvr.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import io.askcloud.pvr.api.kodi.KodiManager;
import io.askcloud.pvr.api.kodi.KodiDownloader;
import io.askcloud.pvr.api.kodi.KodiMovieDownloader;
import io.askcloud.pvr.api.kodi.KodiTVShowDownloader;
import io.askcloud.pvr.themoviedb.MovieDbException;
import io.askcloud.pvr.themoviedb.TheMovieDbApi;
import io.askcloud.pvr.tvdb.TheTVDBApi;

/**
 * 
 * @author UFCTester
 *
 */
public class HTPC implements IHTPC{
	private static HTPC eINSTANCE = null;
	
	
	private static final String CLASS_NAME = HTPC.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);
	
	public static final Logger LOG_DOWNLOAD = Logger.getLogger(LOG_DOWNLOAD_TR);
	public static final Logger LOG_LOAD_KODI_STATUS = Logger.getLogger(LOG_LOAD_KODI_STATUS_TR);
	public static final Logger LOG_DOWNLOAD_KODI_MONITOR_THREAD = Logger.getLogger(LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR);
	public static final Logger LOG_FILE_BOT = Logger.getLogger(LOG_FILE_BOT_TR);
	
	
	//TV and Movie DB API
	public TheTVDBApi tvdbAPI=null;
	public TheMovieDbApi moviedbAPI=null;
	
	public static boolean CLEAN_KODI_DOWNLOAD = false;
	
	private static Set<String> excludeMissingEpisodes = new LinkedHashSet<String>();
	
	//default is everything except the exlude list.  This can be overwritten
	private static Set<String> includeMissingEpisodes = new LinkedHashSet<String>();
	
    static
    {
		includeMissingEpisodes = new LinkedHashSet<String>();
		//includeMissingEpisodes.add("311946"); //Man with a Plan
    }
	
	private static Set<String> excludeMovies = new LinkedHashSet<String>();
	
	//This is now in the file FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES="C:/gitbash/opt/eclipse/workspace/io.askcloud.pvr/filebot/find-series-episodes-missing-excludes.txt";
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
    
    public static void main(String[] args) {
   	 Options options = new Options();
   	 options.addOption("h", "help", false, "show help.");
        options.addOption("a", "amc", false, "Run Filebot AMC");
        options.addOption("t", "tvqueue", false, "Download TV Shows from TV Queue");
        options.addOption("e", "tvmissing", false, "Download TV Shows from Series Episode Missing");
        options.addOption("m", "moviequeue", false, "Download Movie from Movie Queue");
        options.addOption("f", "find", false, "Find TV Shows or Movies from TVDB and TMDB");
        options.addOption("s", "findmissingepisodes", false, "Find series missing episodes which Filebot");

        CommandLineParser parser = new DefaultParser();
        org.apache.commons.cli.CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h"))
			{
				 HelpFormatter formater = new HelpFormatter();
				 formater.printHelp("Main", options);
				 System.exit(0);
			}
			else if (cmd.hasOption("a")) {
				 HTPCFactory.getInstance().runFilebotAMCRequest();
				 System.exit(0);
			}
			else if (cmd.hasOption("t")) {
				 HTPCFactory.getInstance().runDonwloadTVShowQueueMonitorRequest();
				 System.exit(0);
			}
			else if (cmd.hasOption("e")) {
				 HTPCFactory.getInstance().runDownloadTVShowEpisodesMissingRequest();
				 System.exit(0);
			}
			else if (cmd.hasOption("m")) {
				 HTPCFactory.getInstance().runDownloadMovieQueueMonitorRequest();
				 System.exit(0);
			}
			else if (cmd.hasOption("f")) {
				 HTPCFactory.getInstance().runFindTVShowOrMovieRequest();
				 System.exit(0);
			}
			else if (cmd.hasOption("s")) {
				 HTPCFactory.getInstance().runFindMissingTVShowEpisodesRequest();
				 //filebot runs different threads so we need to wait to ensure it is complete
				 //System.exit(0);
			}			
			else {
				 HelpFormatter formater = new HelpFormatter();
				 formater.printHelp("Main", options);
				 System.exit(0);
			}
		}
		catch (ParseException e) {
			LOG.severe("Failed to parse comand line properties: exception: " + e.getMessage());
			 HelpFormatter formater = new HelpFormatter();
			 formater.printHelp("Main", options);
			 System.exit(0);
		}
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

	private HTPC() {

		super();

		init();
	}

	private void init() {
		LOG.entering(CLASS_NAME, "init");
		
		initLogger();
		
		LOG.exiting(CLASS_NAME, "init");
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
	

	public void printFileSizes(File directory) {
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
	 * 
	 * @return
	 */
	public KodiManager getKodiManager() {
		return KodiManager.getInstance();
	}
	
	
	public String getSeason(String season) {
		return (season.length() == 1) ? "0" + season : season;
	}
	
	public String getSeason(int season) {
		try {
			return getSeason(Integer.valueOf(season).toString());
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	
	public String getEpisode(int episode) {
		try {
			return getEpisode(Integer.valueOf(episode).toString());
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public String getEpisode(String episode) {
		return (episode.length() == 1) ? "0" + episode : episode;
	}

	public int getSeasonInt(String season) {
		try {
			return Integer.valueOf(season).intValue();
		}
		catch (Exception e) {
			// TODO BASE_CODE: handle exception
		}
		return -1;
	}

	public int getEpisodeInt(String episode) {
		try {
			return Integer.valueOf(episode).intValue();
		}
		catch (Exception e) {
			// TODO BASE_CODE: handle exception
		}
		return -1;
	}	
	

	//Main.main(new String[]{"-list", "--db", "thetvdb", "--q", "Dexter","--format", "{plex}"});
	//

	
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
        	DefaultExecuteResultHandler rh = new FileBotExecuteResultHandler();
            String line = FILE_BOT_EXE + commandArgs;
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
		File missingEpisodeFile = new File(HTPC.FILEBOT_SERIES_EPISODES_HAVE_FILE);
		try {
			LOG.info("Deleting old missing episode file: " + missingEpisodeFile);
			FileUtils.deleteQuietly(missingEpisodeFile);
		}
		catch (Exception e) {
			LOG.severe("ERROR deleting file: " + missingEpisodeFile);
		}
		
		try {		
			directory="\"" + directory + "\"";
			callFileBot(new String[] { "-script", HTPC.FILEBOT_FIND_SERIES_EPISODES_HAVE, directory,"--output",HTPC.FILEBOT_SERIES_EPISODES_HAVE_FILE , "--log", "info"});
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
		File missingEpisodeFile = new File(HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE);
		try {
			LOG.info("Deleting old missing episode file: " + missingEpisodeFile);
			FileUtils.deleteQuietly(missingEpisodeFile);
		}
		catch (Exception e) {
			LOG.severe("ERROR deleting file: " + missingEpisodeFile);
		}
		
		try {		
			
			//Main.main(new String[] { "-script", PlexPVRManager.FILE_BOT_FIND_MISSING_EPISODES, directory,"--output",PlexPVRManager.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILE_BOT_FIND_MISSING_EPISODES_EXCLUDES,"--log", "all"});
			//callFileBot(new String[] { "-script", PlexPVRManager.FILE_BOT_FIND_MISSING_EPISODES, directory,"--output",PlexPVRManager.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILE_BOT_FIND_MISSING_EPISODES_EXCLUDES,"--log", "info"});
			//Main.main(new String[] { "-list", "--db", "thetvdb", "--q", "Dexter", "--format", "{plex}" });
			directory="\"" + directory + "\"";
			callFileBot(new String[] { "-script", HTPC.FILEBOT_FIND_SERIES_MISSING_EPISODES, directory,"--output",HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES,"--log", "info"});
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
		File seriesEndedFile = new File(HTPC.FILEBOT_SERIES_ENDED_EPISODES_FILE);
		try {
			LOG.info("Deleting old missing episode file: " + seriesEndedFile);
			FileUtils.deleteQuietly(seriesEndedFile);
		}
		catch (Exception e) {
			LOG.severe("ERROR deleting file: " + seriesEndedFile);
		}
		
		try {			
			//Main.main(new String[] { "-script", PlexPVRManager.FILEBOT_FIND_SERIES_ENDED_EPISODES, directory, "--output",PlexPVRManager.FILEBOT_SERIES_ENDED_EPISODES_FILE });
			callFileBot(new String[] { "-script", HTPC.FILEBOT_FIND_SERIES_ENDED_EPISODES, directory, "--output",HTPC.FILEBOT_SERIES_ENDED_EPISODES_FILE });
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
	 * @return
	 */
	public List<KodiDownloader> loadMovieQueue(int numberOfDownloadsToHandle)
	{
		return loadMovieRequests(HTPC.FILEBOT_MOVIE_MISSING_QUEUE_FILE,numberOfDownloadsToHandle);
	}

	/**
	 * @param numberOfDownloadsToHandle
	 * @return
	 */
	public List<KodiDownloader> loadTVShowQueue(int numberOfDownloadsToHandle)
	{
		return loadAndUpdateTVShowRequests(HTPC.FILEBOT_SERIES_EPISODES_MISSING_QUEUE_FILE,Collections.emptyList(),numberOfDownloadsToHandle);
	}
	
	/**
	 * @param numberOfDownloadsToHandle
	 * @return
	 */
	public List<KodiDownloader> loadTVShowEpisodesMissing(int numberOfDownloadsToHandle)
	{
		return loadAndUpdateTVShowRequests(HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE,Collections.emptyList(),numberOfDownloadsToHandle);
	}
	
	/**
	 * @param numberOfDownloadsToHandle
	 * @return
	 */
	public List<KodiDownloader> loadTVShowEpisodesMissingAndUpdateProgress(List<KodiDownloader> downloaders)
	{
		return loadAndUpdateTVShowRequests(HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE,downloaders,-1);
	}	
	
	/**
	 * @param sourceDirectory
	 * @param targetDirectory
	 */
	public void automatedMediaCenter(String sourceDirectory, String targetDirectory) {
		LOG.entering(CLASS_NAME, "findMissingEpisodes", new Object[] {sourceDirectory,targetDirectory});
				
		String fileBotDestForwardSlashes=targetDirectory.replace("\\\\", "/");
		try {
			//Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "copy", "-non-strict", sourceDirectory, "--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
			//		"subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "ecludeList", "amc-exclude.txt", "--def", "--log", "all" });
			
			String[] args = new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "copy", "-non-strict", sourceDirectory, 
					"--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
					"subtitles", "en", "music", "y", "artwork", "n", "--log-file", FILEBOT_AMC_LOG, "--def", "excludeList=" + FILEBOT_AMC_EXCLUDE_LIST, "--log", "all" };					
					
			callFileBot(args);
		}
		catch(SecurityException e)
		{
			LOG.severe("Error Handling Filebot AMC: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		LOG.exiting(CLASS_NAME, "findMissingEpisodes");		
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
					"subtitles", "en", "music", "y", "artwork", "n", "--log-file", FILEBOT_AMC_LOG, "--def", "excludeList=" + FILEBOT_AMC_EXCLUDE_LIST, "--log", "all" };					
					
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
	 * @param missingEpisodeFile
	 * @return
	 */
	synchronized private List<KodiDownloader> loadMovieRequests(String movieQueueFileString,int numberOfDownloadsToHandle) {
		List<KodiDownloader> movies = new ArrayList<KodiDownloader>();
		if(movieQueueFileString == null)
		{
			return movies;
		}
		
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        FileWriter csvFileWriter=null;
        CSVPrinter cvsPrinter=null;
        File missingMoviesLock = null;
        List<CSVRecord> prunedRecords = new ArrayList<CSVRecord>();
        
        try {
        	missingMoviesLock = waitForLockFile(movieQueueFileString);
        	inputStream = new FileInputStream(missingMoviesLock);
            reader = new InputStreamReader(inputStream, "UTF-8");
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            
            List<CSVRecord> records = parser.getRecords();
            
        	//"TVDB_ID,SERIES_NAME,SEASON,EPISODE"
            String[] header = {"IMDB_ID","MOVIE_NAME"};
            
            for (int i = 0; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				
				//purge the numberOfDownloadsToHandle
				if((numberOfDownloadsToHandle <= 0) || (i < numberOfDownloadsToHandle))
				{
	            	final String imdbid = record.get("IMDB_ID");
	            	final String movieName = record.get("MOVIE_NAME");
	
	            	//if includeMissingEpisodes is not empty then only include these ones
	            	if(!excludeMovies.contains(imdbid))
	            	{
	                	KodiDownloader movie = createMissingMovie(imdbid,movieName);
	                	movies.add(movie);
	            	}
		        }					
				//else we are over so print the rest back to the master file
				else
				{
					prunedRecords.add(record);
				}
            }

            
            //if we got this far then we must have the new list of pruned download videos
            File csvOutputFile=new File(movieQueueFileString);
            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(header);
            csvFileWriter = new FileWriter(csvOutputFile);
            cvsPrinter = new CSVPrinter(csvFileWriter, csvFileFormat);
            
            //print the records
            cvsPrinter.printRecords(prunedRecords);               
            
            
        }
		catch (Exception e) {
			// TODO BASE_CODE: handle exception
		}
        finally {
            try {
            	parser.close();	
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + movieQueueFileString + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + movieQueueFileString + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + movieQueueFileString + " InputStream: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.flush();
			}
			catch (Exception e) {
				LOG.severe("Unable to flush " + movieQueueFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + movieQueueFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	cvsPrinter.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + movieQueueFileString + " cvsPrinter: " + e.getMessage());
			}
            
            //delete the lock file
            try {
            	if((missingMoviesLock != null) && (missingMoviesLock.exists()))
            	{
            		missingMoviesLock.delete();
            	}
			}
			catch (Exception e) {
				LOG.severe("ERROR: Unable to close " + missingMoviesLock + " InputStream: " + e.getMessage());
			}            
        }
        
    	return movies;        
	}	
	

	/**
	 * When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFileString
	 * @param numberOfDownloadsToHandle (if this is <= 0 then we will download them all.
	 * @return
	 */
	synchronized private List<KodiDownloader> loadAndUpdateTVShowRequests(String missingEpisodeFileString,List<KodiDownloader> currentRequestProgress,int numberOfDownloadsToHandle) {
		LOG.entering(CLASS_NAME, "loadTVShowRequests",new Object[]{missingEpisodeFileString,numberOfDownloadsToHandle});
		List<KodiDownloader> missingEpisodes = new ArrayList<KodiDownloader>();
		if(missingEpisodeFileString == null)
		{
			return missingEpisodes;
		}
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        FileWriter csvFileWriter=null;
        CSVPrinter cvsPrinter=null;
        File missingEpisodeLockFile = null;
        List<String[]> updatedRecords = new ArrayList<String[]>();
        
        try {
        	missingEpisodeLockFile = waitForLockFile(missingEpisodeFileString);
        	inputStream = new FileInputStream(missingEpisodeLockFile);
            reader = new InputStreamReader(inputStream, "UTF-8");
            
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            
            List<CSVRecord> records = parser.getRecords();
            
        	//"TVDB_ID,SERIES_NAME,SEASON,EPISODE"
            String[] header = {"TVDB_ID","IMDB_ID","SERIES_NAME","SEASON","EPISODE","ENDED","STATUS","DOWNLOAD_PERCENT"};
            
            for (int i = 0; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				
            	String tvdbid = record.get("TVDB_ID");
            	String imdbid = record.get("IMDB_ID");
            	String seriesName = record.get("SERIES_NAME");
            	String season = record.get("SEASON");
            	String episode = record.get("EPISODE");
            	String ended = record.get("ENDED");
            	String status = record.get("STATUS");
            	String downloadPercent = record.get("DOWNLOAD_PERCENT");
            	
				String seasonNumber = HTPC.getInstance().getSeason(season);
				String episodeNumber = HTPC.getInstance().getSeason(episode);
				
            	boolean seriesEnded=((ended != null) && ("true".equals(ended)))?true: false;
            	
            	String[] originalRecord = {tvdbid,imdbid,seriesName,season,episode,ended,status,downloadPercent};
            	
            	
                //If the list is not empty then we just want to update the progress
                if(!currentRequestProgress.isEmpty())
                {
                	boolean addedRecord=false;
                	for (KodiDownloader downloader : currentRequestProgress) {
						if(downloader instanceof KodiTVShowDownloader)
						{
							KodiTVShowDownloader kodi=(KodiTVShowDownloader)downloader;
							String kodiSeasonNumber = HTPC.getInstance().getSeason(kodi.getSeason());
							String kodiEpisodeNumber = HTPC.getInstance().getSeason(kodi.getEpisode());
							
							//find the record
							if((kodi.getShowName().equals(seriesName)) && (seasonNumber.equals(kodiSeasonNumber)) && (episodeNumber.equals(kodiEpisodeNumber)))
							{
								String[] updatedRecord = {tvdbid,imdbid,seriesName,season,episode,ended,status,kodi.getLastDownloadStatus().getPercent()};
								LOG.fine("DOWNLOAD_PERCENT UPDATE: " +updatedRecord);
								addedRecord=true;
								updatedRecords.add(updatedRecord);
								break;
							}
						}
					}
					
					//we did not find it so just put the same record back
                	if(!addedRecord)
                	{
                		updatedRecords.add(originalRecord);                	
                	}
                }
                else
                {
                	String[] snatchedRecord=new String[]{tvdbid,imdbid,seriesName,season,episode,ended,"SNATCHED",downloadPercent};
                	
    				//purge the numberOfDownloadsToHandle
    				if((numberOfDownloadsToHandle <= 0) || (i < numberOfDownloadsToHandle))
    				{
    	            	//check if the status is queued.  If it is then we can grab it, otherwise we have to just skip it
    					if(!"QUEUED".equals(status))
    					{
    						updatedRecords.add(originalRecord);
    						continue;
    					}
    	            	
    	            	//Extra Filtering if includeMissingEpisodes is not empty then only include these ones
    	            	if(((!includeMissingEpisodes.isEmpty()) && (includeMissingEpisodes.contains(tvdbid))) 
    	            			|| (!excludeMissingEpisodes.contains(tvdbid)))
    	            	{
    	            		KodiTVShowDownloader missingEpisode = createMissingEpisode(tvdbid,imdbid,seriesName,season,episode,seriesEnded);
    	                    missingEpisodes.add(missingEpisode);      
    	                    
    	                    //we need to update the record
    	                    
    	                    LOG.info("SNATCHED: " + snatchedRecord);
    	                    updatedRecords.add(snatchedRecord);
    	            	}
    		        }					
    				//else we are over so print the rest back to the master file
    				else
    				{
    					updatedRecords.add(originalRecord);
    				}
                }
			}
            
            //if we got this far then we must have the new list of pruned download videos
            File csvOutputFile=new File(missingEpisodeFileString);
            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(header);
            csvFileWriter = new FileWriter(csvOutputFile);
            cvsPrinter = new CSVPrinter(csvFileWriter, csvFileFormat);
            
            //print the records
            cvsPrinter.printRecords(updatedRecords);         

		}
		catch (Exception e) {
			// FIXME: handle exception
			LOG.severe("Error loading TVShow Requests: " + missingEpisodeLockFile);
		}
        finally {
            try {  
            	parser.close();	
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + missingEpisodeLockFile + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + missingEpisodeLockFile + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + missingEpisodeLockFile + " InputStream: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.flush();
			}
			catch (Exception e) {
				LOG.severe("Unable to flush " + missingEpisodeFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + missingEpisodeFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	cvsPrinter.close();
			}
			catch (Exception e) {
				LOG.severe("Unable to close " + missingEpisodeFileString + " cvsPrinter: " + e.getMessage());
			}
            
            //delete the lock file
            try {
            	if((missingEpisodeLockFile != null) && (missingEpisodeLockFile.exists()))
            	{
            		missingEpisodeLockFile.delete();
            	}
			}
			catch (Exception e) {
				LOG.severe("ERROR: Unable to close " + missingEpisodeFileString + " InputStream: " + e.getMessage());
			}
        }
        
        LOG.exiting(CLASS_NAME, "loadTVShowRequests",missingEpisodes);
    	return missingEpisodes;        
	}		
	
	/**
	 * FIXME DSP When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFile
	 * @return
	 */
	synchronized private File waitForLockFile(String masterFileString) {
		LOG.entering(CLASS_NAME, "waitForLockFile", new Object[] { masterFileString });
		if(masterFileString == null)
		{
			LOG.severe("Error with the inpurt masterFile: " + masterFileString);
			return null;
		}
		
        String masterFileLockString=masterFileString + ".lock";
        File masterFileLock = new File(masterFileLockString);
        File masterFile = new File(masterFileString);
        
        //FIXME Check to see if there is a lock file and if not create one
        while(true)
        {    
            if(masterFileLock.exists())
            {
            	try {
            		LOG.info("Wating for file lock to be removed.");
                	//FIXME we need to pause the thread and check again
                	Thread.sleep(HTPC.QUEUE_LOCK_FILE_WAIT_TIME);
				}
				catch (Exception e) {
					LOG.severe("ERROR waiting while sleeping while polling for lock file.");
				}
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
    				LOG.severe("Could not create lock file for: " + masterFileString + " lock file: " + masterFileLockString);
    			}
            }
        }
        
    	return masterFileLock;        
	}			
	
	/**
	 * @param tvdbid
	 * @param seriesName
	 * @param season
	 * @param episode
	 * @return
	 */
	public KodiTVShowDownloader createMissingEpisode(String tvdbid, String imdbid, String seriesName, String season, String episode,boolean ended)
	{
		return new KodiTVShowDownloader(seriesName,imdbid,tvdbid,HTPC.getInstance().getSeasonInt(season),HTPC.getInstance().getEpisodeInt(episode),ended);
	}
	
	/**
	 * @param imdbid
	 * @param movieName
	 * @return
	 */
	public KodiDownloader createMissingMovie(String imdbid, String movieName)
	{
		return new KodiMovieDownloader(movieName,imdbid);
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
}
