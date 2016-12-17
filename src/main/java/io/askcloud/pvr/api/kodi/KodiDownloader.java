package io.askcloud.pvr.api.kodi;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import io.askcloud.pvr.api.HTPC;

/**
 * 
 * @author UFCTester
 *
 */
abstract public class KodiDownloader implements Runnable {

	private static final String CLASS_NAME = KodiDownloader.class.getName();
	private static final Logger LOG = HTPC.LOG_DOWNLOAD;

	protected Stack<KodiDownloaderDetails> downloadStatusDetails = new Stack<KodiDownloaderDetails>();

	private String name = null;
	
	/*
	 * Each time the method isDownloadStuck() this increments this counter if it is not stuck.  
	 * When the counter hits 10 then it will be considered stuck
	 */
	private int downloadHasNotStartedCounter=0;
	
	private boolean downloadHasStarted=false;
	
	private String IMDB_ID = null;
	private String TVDB_ID = null;
	
	private String status=HTPC.getDOWNLOAD_STATUS_QUEUED();
	
	//This flag is set when the download is complete.  No other status objects will be added to the stack
	boolean isComplete = false;
	
	//The flag is set when the download has failed (either it was not found or it hung)
	boolean isFailed = false;

	/*
	 * we can only kick off the download if there is no other download requests being made at the time.  
	 * Downloads can occur at the same time but the request needs to be done one by one
	 */
	private boolean readyToRun = false;
	

	/**
	 * This method will determine if the show being downloaded is a movie or a tv show
	 * @param tvdbid
	 * @param imdbid
	 * @param name
	 * @param season (empty string or null if this is a movie)
	 * @param episode (empty string or null if this is a movie)
	 * @param ended
	 * @param status
	 * @param downloadPercent
	 * @param downloadFile
	 * @param totalDownloadedSize
	 * @param totalSize
	 * @return
	 */
	public static KodiDownloader createKodiDownloader(String tvdbid, String imdbid, String name, String season, String episode,boolean ended,String status,String downloadPercent,String downloadFile,String totalDownloadedSize, String totalSize)
	{
		//TV Show
		if((StringUtils.isNotEmpty(season)) && (StringUtils.isNotEmpty(episode)))
		{
			return new KodiTVShowDownloader(name,imdbid,tvdbid,NumberUtils.toInt(season),NumberUtils.toInt(episode),ended,status,NumberUtils.toInt(downloadPercent),downloadFile,NumberUtils.toInt(totalDownloadedSize),NumberUtils.toInt(totalSize));
		}
		//Movie
		else
		{
			return new KodiMovieDownloader(name,imdbid,status,NumberUtils.toInt(downloadPercent),downloadFile,NumberUtils.toInt(totalDownloadedSize),NumberUtils.toInt(totalSize));
		}
	}		
	
	public class KodiDownloaderDetails {

		private int percent;
		private String file;
		private int totalDownloadedSize;
		private int total;
				
		private KodiDownloaderDetails(String percent, String file, String totalDownloadedSize, String total) {
			this(NumberUtils.toInt(percent),file,NumberUtils.toInt(totalDownloadedSize), NumberUtils.toInt(total));
		}
		
		private KodiDownloaderDetails(int percent, String file, int totalDownloadedSize, int total) {
			super();
			this.percent=percent;
			this.file=file;
			this.totalDownloadedSize=totalDownloadedSize;
			this.total = total;
		}
		
		public int getTotalDownloadedMB() {
			return totalDownloadedSize;
		}
		
		public boolean downloadHasStarted()
		{
			return (getPercent() > -1);
		}
		
		public String getFile() {
			return file;
		}

		public int getPercent() {
			return percent;
		}
				
		public int getTotalMB() {
			return total;
		}

		public boolean isComplete()
		{
			return (100 ==getPercent());
		}
		
		@Override
		public String toString() {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append(getPercent() + "% ");
			sBuffer.append(getFile());
			sBuffer.append(" donwloaded: " + getTotalDownloadedMB());
			sBuffer.append(" total: " + getTotalMB());
			return sBuffer.toString();
		}		
		

	}

	public KodiDownloader(String imdbid, String tvdbid,String name,String status,int percentDownloadStatus,String downloadFile, int totalDownloadedSize, int totalSize) {
		super();
		LOG.entering(CLASS_NAME, "KodiDownloader",new Object[]{imdbid,tvdbid,name,status,percentDownloadStatus,downloadFile});
		setName(name);
		setImdbID(imdbid);
		setTVDBID(tvdbid);
		setStatus(status);
		
		//add the first KodiDownloadStatus on the stack to setup the stack
		KodiDownloaderDetails kodiDownloadStatus = new KodiDownloaderDetails(percentDownloadStatus, downloadFile, totalDownloadedSize, totalSize);
		downloadStatusDetails.push(kodiDownloadStatus);
		
		//This is needed in case we load an existing queue file and some are done and some are not.
		if(percentDownloadStatus == 100)
		{
			downloadCompleted();
		}
		
		LOG.exiting(CLASS_NAME, "KodiDownloader");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Gey the IMDB ID
	 */
	public String getImdbID() {
		return IMDB_ID;
	}

	public void setImdbID(String imdbid) {
		this.IMDB_ID=(StringUtils.isNotBlank(imdbid))?imdbid:null;
	}

	public String getTVDBID() {
		return TVDB_ID;
	}

	public void setTVDBID(String tvdbId) {
		this.TVDB_ID=(StringUtils.isNotBlank(tvdbId))?tvdbId:null;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getStatus() {
		return status;
	}

	protected Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = url.getQuery();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	/**
	 * Some shows are named "The Americans (2013)" which can not be found in kodi
	 * @param searchString
	 * @return
	 */
	protected String normailizeShowName(String searchString) {
		LOG.entering(CLASS_NAME, "normailizeSearchName",new Object[]{searchString});
		String results = searchString;
		if (results == null) {
			return results;
		}

		//Some shows are named "The Americans (2013)" which can not be found in kodi
		String searchStringWithBrackets = StringUtils.substringBetween(searchString, "(", ")");
		if (searchStringWithBrackets != null) {
			results = searchString.replace("(" + searchStringWithBrackets + ")", "");
		}
		else {
			results = searchString;
		}

		results = StringUtils.normalizeSpace(results);
		LOG.exiting(CLASS_NAME, "normailizeSearchName",results);
		return results;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public boolean showNameMatches(String name)
	{
		name = normailizeShowName(name);
		return (getName().equals(name));
	}

	/**
	 * Set the Thread Name
	 */
	protected void setThreadName() {
		LOG.entering(CLASS_NAME, "setThreadName");
		String threadName = null;
		String threadPrefix = "NOT_STARTED: ";
				
		//Download has started
		if(downloadStatusDetails.size() > 1)
		{
			threadPrefix = String.valueOf(getLatestKodiDownloadDetails().getPercent());
		}
		
		//get the last download status
		threadName = "KodiDownloader [" + threadPrefix + "% " + getDownloadStatusIdentifier() + "]";
		final Thread currentThread = Thread.currentThread();
        currentThread.setName(threadName);
        LOG.exiting(CLASS_NAME, "setThreadName");
	}
	

	/*
	 * Download the videos
	 */
	abstract protected void requestDownload();

	@Override
	public void run() {
		LOG.entering(CLASS_NAME, "run");
		
		while (true) {
			if (isReadyToRun()) {
				break;
			}
			else {
				//set the thread name
				HTPC.sleep(HTPC.getKODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME(), "Error while waiting on starting the thread execution.");
			}
		}

		try {
			//set the thread name
			setThreadName();

			//send the download request to kodi
			requestDownload();

			//Monitor the progress until it is determined that the download is stuck or downloaded
			monitor();
		}
		catch (Exception e) {
			LOG.severe("Error occured in KodiExodusDownloader: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			/*
			 * something bad happened if the request has not been made and we are at this point so notify the kodi manager
			 * to handle the next request
			 */
			if (!downloadHasStarted) {
				downloadHasStarted = true;
				HTPC.getInstance().readyToProcessNextRequest(this);
			}
		}
		
		LOG.exiting(CLASS_NAME, "run");
	}

	public void setReadyToRun() {
		readyToRun = true;
	}
	
	/**
	 * @return
	 */
	public boolean isReadyToRun() {
		return readyToRun;
	}
	
	/*
	 * FIXME This method is called by the main thread.
	 * 
	 * Next we need to update the KodiDownloader status object if the following conditions are true
	 *   1) The kodiDownloader has the flag isReadyToRun set
	 *   2) If the kodiDownloader is not stopped (ie not complete or not failed)
	 */	
	synchronized public void addKodiDownloadStatus(String percent, String file, String totalDownloadedSize, String total)
	{
    	//FIXME This should never happen as this is the kodi download file
    	if(StringUtils.isBlank(file))
    	{
    		return;
    	}
    	
		//Only add the status if the above 2 scenarios are true
    	if((isReadyToRun()) && (!isStopped()))
		{
    		KodiDownloaderDetails details = new KodiDownloaderDetails(percent, file, totalDownloadedSize, total);
    		downloadStatusDetails.push(details);
		}
	}
	
	/**
	 * Monitor the progress until it is determined that the download is stuck or downloaded
	 */
	protected void monitor() {
		LOG.entering(CLASS_NAME, "monitor");
		
		/*
		 * Monitor the progress until it is determined that the download is stuck or downloaded
		 * For a download to be stuck the status must be stuck for at least AAAAAA minutes which includes the time the monitor sleeps KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME
		 */
		while (true) {
			//set the thread name
			setThreadName();

			HTPC.sleep(HTPC.getKODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME(), "Error while monitoring the download thread.");

			//The download is stuck.  It was either not picked up or it is not downloading anymore
			if(isDownloadStuck())
			{
				downloadHung();
				LOG.exiting(CLASS_NAME, "monitor");
				return;				
			}
			
			//If the stack size is 1 then it has not started and just continue to sleep
			if(downloadStatusDetails.size() == 1)
			{
				LOG.info("Download has not started: " + getDownloadStatusIdentifier());
				continue;
			}
			
			//if we get to this then the download has started so trigger the next download if it has not been triggered
			if(!downloadHasStarted) {
				downloadHasStarted = true;
				HTPC.getInstance().readyToProcessNextRequest(this);
			}
			
			//just get the latest download status
			KodiDownloaderDetails latestStatus = getLatestKodiDownloadDetails(); 			

			//get the download files
			LOG.info(latestStatus.getPercent() + "% Download Status: " + latestStatus.getFile());

			//If it is complete then we can return
			if (latestStatus.isComplete()) {
				setThreadName();
				downloadCompleted();
				LOG.exiting(CLASS_NAME, "monitor");
				return;
			}
		}		
	}

	/**
	 * The download completed and is 100%
	 */
	protected void downloadCompleted() {
		LOG.entering(CLASS_NAME, "downloadCompleted",new Object[]{status});
		this.isComplete=true;
		LOG.info("Download is complete: " + getDownloadStatusIdentifier());
		LOG.exiting(CLASS_NAME, "downloadCompleted");
	}

	/**
	 * 
	 */
	protected void downloadHung() {
		LOG.entering(CLASS_NAME, "downloadHung");
		this.isFailed=true;
		LOG.severe("Download Looks like it is hung: " + toString());
		LOG.exiting(CLASS_NAME, "downloadHung");
	}

	/**
	 * This method should return a unique string that can be matched against the latest set 
	 * of all status objects which the monitor collects every (x) number of seconds.
	 * @return
	 */
	abstract public String getDownloadStatusIdentifier();

	/**
	 * The Download is considered to be stuck if there are 10 download objects with the same percentage starting at the top
	 * @param identifier
	 * @return
	 */
	private boolean isDownloadStuck()
	{
		//if it is complete then it can not be stuck so return
		if(isComplete())
		{
			return false;
		}
		int stuckCounter = 0;
		downloadHasNotStartedCounter++;
		
		/*
		 * Lets ensure the download has at least started. 
		 * If we came into this method more then 10 times and the download still has not started 
		 */
		if((downloadHasNotStartedCounter > 10) && (!downloadHasStarted))
		{
			return true;
		}
		
		//only check if there is at least 10 status objects
		if(downloadStatusDetails.size() >= 10)
		{
			int latestDownloadStatus = getLatestKodiDownloadDetails().getPercent();
			for (KodiDownloaderDetails status : downloadStatusDetails) {
				if(stuckCounter == 10)
				{
					return true;
				}
				
				int currentPercentage = status.getPercent();
				//so far it is stuck so continue to check
				if(currentPercentage == latestDownloadStatus)
				{
					stuckCounter++;
				}
				//the percentages are not the same so it can not be stuck
				else
				{
					return false;
				}
			}			
		}		

		//not stuck
		return false;
	}	
	
	/**
	 * @return
	 */
	synchronized public KodiDownloaderDetails getLatestKodiDownloadDetails()
	{
		return downloadStatusDetails.peek();
	}
	/**
	 * 
	 * @return true if the download is complete
	 */
	public boolean isComplete() {
		return isComplete;
	}
	
	/**
	 * @return true if the download has failed
	 */
	public boolean isFailed() {
		return isFailed;
	}
	
	/**
	 * @return true if isComplete() is true or isFailed() is true
	 */
	public boolean isStopped() {
		return (isComplete() || isFailed());
	}	
	
	/*
	 * 
	 */
	abstract public String[] toCSV();
}


