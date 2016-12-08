package io.askcloud.pvr.api.pvr;

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
	
	public class CmdDefaultExecuteResultHandler extends DefaultExecuteResultHandler
	{
		public CmdDefaultExecuteResultHandler() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onProcessComplete(int exitValue) {
			log.entering(CLASS_NAME, "onProcessComplete");
			log.exiting(CLASS_NAME, "onProcessComplete");
			super.onProcessComplete(exitValue);
		}
		@Override
		public void onProcessFailed(ExecuteException e) {
			log.entering(CLASS_NAME, "onProcessFailed",e);
			log.exiting(CLASS_NAME, "onProcessFailed");
			super.onProcessFailed(e);
		}
		
		
		
	}	

	private HTPC() {

		super();

		init();
	}

	private void init() {
		log.entering(CLASS_NAME, "init");
		
		initLogger();
		
		log.exiting(CLASS_NAME, "init");
	}
	
	public void recreateDirectory(String directory)
	{
		log.entering(CLASS_NAME, "recreateDirectory", new Object[] {directory});
		
		try {
			
			File targetDirectoryFile = new File(directory);
			log.info("Deleting Old Directory and Files: " + directory);
			printFilesAndDirs(targetDirectoryFile);
			FileUtils.deleteQuietly(targetDirectoryFile);
			
			FileUtils.forceMkdir(targetDirectoryFile);
			
			log.info("Old Files Should Be Gone: " + directory);
			printFilesAndDirs(targetDirectoryFile);			
		}
		catch (Exception e) {
			log.severe("Unable to recreate directory: " + directory + " exception: " + e.getMessage());
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
				log.info("Size: " + file.length() + "  File Found: " + file.toString());
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
		//args.add(0, FILE_BOT_EXE);
		//String[] cmd = args.toArray(new String[args.size()]);
		StringBuffer commandArgs = new StringBuffer();
        for (String arg : args)
        	commandArgs.append(" " + arg);
        
//        try {
//            Process p = Runtime.getRuntime().exec(cmd);
//            p.waitFor();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
        
        try {
        	DefaultExecuteResultHandler rh = new CmdDefaultExecuteResultHandler();
            String line = FILE_BOT_EXE + commandArgs;
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(cmdLine,rh);   
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
		log.entering(CLASS_NAME, "findTVShowEpisodesHave", new Object[] {directory});
		File missingEpisodeFile = new File(HTPC.FILEBOT_SERIES_EPISODES_HAVE_FILE);
		try {
			log.info("Deleting old missing episode file: " + missingEpisodeFile);
			FileUtils.deleteQuietly(missingEpisodeFile);
		}
		catch (Exception e) {
			log.severe("ERROR deleting file: " + missingEpisodeFile);
		}
		
		try {		
			directory="\"" + directory + "\"";
			callFileBot(new String[] { "-script", HTPC.FILEBOT_FIND_SERIES_EPISODES_HAVE, directory,"--output",HTPC.FILEBOT_SERIES_EPISODES_HAVE_FILE , "--log", "info"});
		}
		catch(SecurityException e)
		{
			log.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		log.exiting(CLASS_NAME, "findTVShowEpisodesHave");
	}
	
	/**
	 * @param directory
	 */
	public void findMissingTVShowEpisodes(String directory) {
		log.entering(CLASS_NAME, "findMissingEpisodes", new Object[] {directory});
		File missingEpisodeFile = new File(HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE);
		try {
			log.info("Deleting old missing episode file: " + missingEpisodeFile);
			FileUtils.deleteQuietly(missingEpisodeFile);
		}
		catch (Exception e) {
			log.severe("ERROR deleting file: " + missingEpisodeFile);
		}
		
		try {		
			directory="\"" + directory + "\"";
			//Main.main(new String[] { "-script", PlexPVRManager.FILE_BOT_FIND_MISSING_EPISODES, directory,"--output",PlexPVRManager.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILE_BOT_FIND_MISSING_EPISODES_EXCLUDES,"--log", "all"});
			//callFileBot(new String[] { "-script", PlexPVRManager.FILE_BOT_FIND_MISSING_EPISODES, directory,"--output",PlexPVRManager.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILE_BOT_FIND_MISSING_EPISODES_EXCLUDES,"--log", "info"});
			//Main.main(new String[] { "-list", "--db", "thetvdb", "--q", "Dexter", "--format", "{plex}" });
			callFileBot(new String[] { "-script", HTPC.FILEBOT_FIND_SERIES_MISSING_EPISODES, directory,"--output",HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE , "--def", "excludeList=" + FILEBOT_FIND_SERIES_EPISODES_MISSING_EXCLUDES,"--log", "info"});
		}
		catch(SecurityException e)
		{
			log.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		log.exiting(CLASS_NAME, "findMissingEpisodes");
	}
	
	/**
	 * @param directory
	 * @return
	 */
	public void findCompletedEpisodes(String directory) {
		log.entering(CLASS_NAME, "findCompletedEpisodes", new Object[] {directory});
		File seriesEndedFile = new File(HTPC.FILEBOT_SERIES_ENDED_EPISODES_FILE);
		try {
			log.info("Deleting old missing episode file: " + seriesEndedFile);
			FileUtils.deleteQuietly(seriesEndedFile);
		}
		catch (Exception e) {
			log.severe("ERROR deleting file: " + seriesEndedFile);
		}
		
		try {			
			//Main.main(new String[] { "-script", PlexPVRManager.FILEBOT_FIND_SERIES_ENDED_EPISODES, directory, "--output",PlexPVRManager.FILEBOT_SERIES_ENDED_EPISODES_FILE });
			callFileBot(new String[] { "-script", HTPC.FILEBOT_FIND_SERIES_ENDED_EPISODES, directory, "--output",HTPC.FILEBOT_SERIES_ENDED_EPISODES_FILE });
		}
		catch(SecurityException e)
		{
			log.severe("Error getting missing episodes: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		log.exiting(CLASS_NAME, "findCompletedEpisodes");
	}	
	
	/**
	 * @return
	 */
	public List<KodiExodusDownloader> loadMovieQueue(int numberOfDownloadsToHandle)
	{
		return loadMovieRequests(HTPC.FILEBOT_MOVIE_MISSING_QUEUE_FILE,numberOfDownloadsToHandle);
	}

	/**
	 * @param numberOfDownloadsToHandle
	 * @return
	 */
	public List<KodiExodusDownloader> loadTVShowQueue(int numberOfDownloadsToHandle)
	{
		return loadTVShowRequests(HTPC.FILEBOT_SERIES_EPISODES_MISSING_QUEUE_FILE,numberOfDownloadsToHandle);
	}
	
	/**
	 * @param numberOfDownloadsToHandle
	 * @return
	 */
	public List<KodiExodusDownloader> loadTVShowEpisodesMissing(int numberOfDownloadsToHandle)
	{
		return loadTVShowRequests(HTPC.FILEBOT_SERIES_EPISODES_MISSING_FILE,numberOfDownloadsToHandle);
	}
	
	/**
	 * @param sourceDirectory
	 * @param targetDirectory
	 */
	public void automatedMediaCenter(String sourceDirectory, String targetDirectory) {
		log.entering(CLASS_NAME, "findMissingEpisodes", new Object[] {sourceDirectory,targetDirectory});
				
		String fileBotDestForwardSlashes=targetDirectory.replace("\\\\", "/");
		try {
			//Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "copy", "-non-strict", sourceDirectory, "--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
			//		"subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "ecludeList", "amc-exclude.txt", "--def", "--log", "all" });			
		}
		catch(SecurityException e)
		{
			log.severe("Error Handling Filebot AMC: " + e.getMessage());			
			e.printStackTrace();
		}
		finally {
			
		}
		
		log.exiting(CLASS_NAME, "findMissingEpisodes");		
	}

	/**
	 * @param sourceDirectory
	 * @param targetDirectory
	 */
	public void automatedMediaCenterTest(String sourceDirectory, String targetDirectory) {
		
		
		String fileBotDestForwardSlashes=targetDirectory.replace("\\\\", "/");
		//Main.main(new String[] { "-script", "fn:amc", "--output", targetDirectory, "--action", "test", "-non-strict", sourceDirectory, "--conflict", "override", "--def","movieFormat=\"" + fileBotDestForwardSlashes + "/Movies/{fn}\"",
		//		"subtitles", "en", "music", "y", "artwork", "n", "--log-file", "amc.log", "--def", "--log", "all" });
	}
	
	/**
	 * @param missingEpisodeFile
	 * @return
	 */
	synchronized private List<KodiExodusDownloader> loadMovieRequests(String movieQueueFileString,int numberOfDownloadsToHandle) {
		List<KodiExodusDownloader> movies = new ArrayList<KodiExodusDownloader>();
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
	                	KodiExodusDownloader movie = createMissingMovie(imdbid,movieName);
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
				log.severe("Unable to close " + movieQueueFileString + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFileString + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFileString + " InputStream: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.flush();
			}
			catch (Exception e) {
				log.severe("Unable to flush " + movieQueueFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	cvsPrinter.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + movieQueueFileString + " cvsPrinter: " + e.getMessage());
			}
            
            //delete the lock file
            try {
            	if((missingMoviesLock != null) && (missingMoviesLock.exists()))
            	{
            		missingMoviesLock.delete();
            	}
			}
			catch (Exception e) {
				log.severe("ERROR: Unable to close " + missingMoviesLock + " InputStream: " + e.getMessage());
			}            
        }
        
    	return movies;        
	}	
	

	/**
	 * FIXME DSP When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFileString
	 * @param numberOfDownloadsToHandle (if this is <= 0 then we will download them all.
	 * @return
	 */
	synchronized private List<KodiExodusDownloader> loadTVShowRequests(String missingEpisodeFileString,int numberOfDownloadsToHandle) {
		List<KodiExodusDownloader> missingEpisodes = new ArrayList<KodiExodusDownloader>();
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
        List<CSVRecord> prunedRecords = new ArrayList<CSVRecord>();
        
        try {
        	missingEpisodeLockFile = waitForLockFile(missingEpisodeFileString);
        	inputStream = new FileInputStream(missingEpisodeLockFile);
            reader = new InputStreamReader(inputStream, "UTF-8");
            
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            
            List<CSVRecord> records = parser.getRecords();
            
        	//"TVDB_ID,SERIES_NAME,SEASON,EPISODE"
            String[] header = {"TVDB_ID","IMDB_ID","SERIES_NAME","SEASON","EPISODE","ENDED"};
            
            for (int i = 0; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				
				//purge the numberOfDownloadsToHandle
				if((numberOfDownloadsToHandle <= 0) || (i < numberOfDownloadsToHandle))
				{
	            	String tvdbid = record.get("TVDB_ID");
	            	String imdbid = record.get("IMDB_ID");
	            	String seriesName = record.get("SERIES_NAME");
	            	String season = record.get("SEASON");
	            	String episode = record.get("EPISODE");
	            	String ended = record.get("ENDED");
	            	
	            	boolean seriesEnded=((ended != null) && ("true".equals(ended)))?true: false;
	            	
	            	//if includeMissingEpisodes is not empty then only include these ones
	            	if(!includeMissingEpisodes.isEmpty())
	            	{
	            		if(includeMissingEpisodes.contains(tvdbid))
	            		{
	            			KodiExodusTVShowDownloader missingEpisode = createMissingEpisode(tvdbid,imdbid,seriesName,season,episode,seriesEnded);
	                    	missingEpisodes.add(missingEpisode);      
	            		}
	            	}
	            	else if(!excludeMissingEpisodes.contains(tvdbid))
	            	{
	            		KodiExodusTVShowDownloader missingEpisode = createMissingEpisode(tvdbid,imdbid,seriesName,season,episode,seriesEnded);
	                	missingEpisodes.add(missingEpisode);
	            	}
		        }					
				//else we are over so print the rest back to the master file
				else
				{
					prunedRecords.add(record);
				}
			}
            
            //if we got this far then we must have the new list of pruned download videos
            File csvOutputFile=new File(missingEpisodeFileString);
            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(header);
            csvFileWriter = new FileWriter(csvOutputFile);
            cvsPrinter = new CSVPrinter(csvFileWriter, csvFileFormat);
            
            //print the records
            cvsPrinter.printRecords(prunedRecords);         

		}
		catch (Exception e) {
			// FIXME: handle exception
			log.severe("Error loading TVShow Requests: " + missingEpisodeLockFile);
		}
        finally {
            try {  
            	parser.close();	
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeLockFile + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeLockFile + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeLockFile + " InputStream: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.flush();
			}
			catch (Exception e) {
				log.severe("Unable to flush " + missingEpisodeFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	csvFileWriter.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeFileString + " csvFileWriter: " + e.getMessage());
			}   
            
            try {
            	cvsPrinter.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + missingEpisodeFileString + " cvsPrinter: " + e.getMessage());
			}
            
            //delete the lock file
            try {
            	if((missingEpisodeLockFile != null) && (missingEpisodeLockFile.exists()))
            	{
            		missingEpisodeLockFile.delete();
            	}
			}
			catch (Exception e) {
				log.severe("ERROR: Unable to close " + missingEpisodeFileString + " InputStream: " + e.getMessage());
			}
        }
        
    	return missingEpisodes;        
	}		
	
	/**
	 * FIXME DSP When loading the TVShowRequests we should put a lock in place so we can have multiple applications to handle the 
	 * tv show requests
	 * @param missingEpisodeFile
	 * @return
	 */
	synchronized private File waitForLockFile(String masterFileString) {
		log.entering(CLASS_NAME, "waitForLockFile", new Object[] { masterFileString });
		if(masterFileString == null)
		{
			log.severe("Error with the inpurt masterFile: " + masterFileString);
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
                	//FIXME we need to pause the thread and check again
                	Thread.sleep(HTPC.QUEUE_LOCK_FILE_WAIT_TIME);
				}
				catch (Exception e) {
					log.severe("ERROR waiting while sleeping while polling for lock file.");
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
    				log.severe("Could not create lock file for: " + masterFileString + " lock file: " + masterFileLockString);
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
	public KodiExodusTVShowDownloader createMissingEpisode(String tvdbid, String imdbid, String seriesName, String season, String episode,boolean ended)
	{
		return new KodiExodusTVShowDownloader(seriesName,imdbid,tvdbid,HTPC.getInstance().getSeasonInt(season),HTPC.getInstance().getEpisodeInt(episode),ended);
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
				log.severe("Error trying to get the movie db api: " + e.getMessage());
			}
		}
		return moviedbAPI;
	}
	
	/**
	 * @return
	 */
	public Logger getLogger() {
		return log;
	}
}
