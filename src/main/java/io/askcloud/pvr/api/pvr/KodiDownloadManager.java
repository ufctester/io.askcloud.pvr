package io.askcloud.pvr.api.pvr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import io.askcloud.pvr.kodi.jsonrpc.api.AbstractCall;
import io.askcloud.pvr.kodi.jsonrpc.api.call.Addons;
import io.askcloud.pvr.kodi.jsonrpc.config.HostConfig;
import io.askcloud.pvr.kodi.jsonrpc.io.ApiCallback;
import io.askcloud.pvr.kodi.jsonrpc.io.ConnectionListener;
import io.askcloud.pvr.kodi.jsonrpc.io.JavaConnectionManager;
import io.askcloud.pvr.kodi.jsonrpc.notification.AbstractEvent;

/**
 * 
 * @author UFCTester
 * 
 * The KodiDownloadManager is responsible for handling all of the downloads which are happening on Kodi
 * during this automation
 *
 */
/**
 * @author apps
 *
 */
public class KodiDownloadManager {
	private static KodiDownloadManager eINSTANCE;
	private JavaConnectionManager conMgr;

	private static String CLASS_NAME = KodiDownloadManager.class.getName();
	private static Logger log = HTPC.getInstance().getLogger();
	private boolean monitorDownload=true;
	private Set<DownloadStatus> downloadStatus = new LinkedHashSet<DownloadStatus>();
	
	private List<KodiExodusDownloader> downloaders = new ArrayList<KodiExodusDownloader>();

	public class DownloadStatus{
		private String percent;
		private String file;
		private String downloaded;
		private String total;
		private String threadPrefix="";
		public DownloadStatus(String percent, String file, String downloaded, String total) {
			super();
			this.percent=percent;
			this.threadPrefix=(percent != null)?percent : "";
			this.file=file;
			this.downloaded=downloaded;
			this.total = total;
		}
		
		public String getDownloaded() {
			return downloaded;
		}
		
		public String getFile() {
			return file;
		}
		
		public boolean containsInFile(String name)
		{
			if((getFile() != null) && (getFile().toLowerCase().contains(name.toLowerCase())))
			{
				return true;
			}
			return false;
		}
		
		public String getPercent() {
			return percent;
		}
		
		public int getPercentInt()
		{
			try {
				return Integer.valueOf(getPercent()).intValue();
			}
			catch (Exception e) {
				// TODO BASE_CODE: handle exception
			}
			return 0;
		}
		
		public void setThreadPrefix(String threadPrefix) {
			if(threadPrefix != null)
			{
				this.threadPrefix = threadPrefix;
			}
		}
		
		public String getThreadPrefix() {
			return threadPrefix;
		}
		
		public String getTotal() {
			return total;
		}
		
		public boolean isComplete()
		{
			return ("100".equals(getPercent()));
		}
		
		@Override
		public String toString() {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append(getPercent() + "% ");
			sBuffer.append(getFile());
			sBuffer.append(" donwloaded: " + getDownloaded());
			sBuffer.append(" total: " + getTotal());
			return sBuffer.toString();
		}		
	}
	
	public static KodiDownloadManager getInstance() {
		if (eINSTANCE == null) {
			eINSTANCE = new KodiDownloadManager();
			eINSTANCE.clearCacheAndSources();
			eINSTANCE.monitorDownload();
		}
		return eINSTANCE;
	}
	
	
	private KodiDownloadManager() {
		super();
		
		//copy master kodi-downloader.csv download file 
		File sourceFile = new File(HTPC.KDOI_PVR_DOWNLOAD_TRACKER_MASTER);
		File targetFile = new File(HTPC.KDOI_PVR_DOWNLOAD_TRACKER);
		try {
			FileUtils.copyFile(sourceFile, targetFile);
		}
		catch (Exception e) {
			log.severe("ERROR copying file: " + HTPC.KDOI_PVR_DOWNLOAD_TRACKER_MASTER + " to file: " + HTPC.KDOI_PVR_DOWNLOAD_TRACKER);
		}

		if(HTPC.CLEAN_KODI_DOWNLOAD)
		{
			//Kodi TVShows
			HTPC.getInstance().recreateDirectory(HTPC.KODI_DOWNLOAD_TVSHOWS_DIR);

			//Kodi Movies
			HTPC.getInstance().recreateDirectory(HTPC.KODI_DOWNLOAD_MOVIES_DIR);
		}	
	}

	public JavaConnectionManager getConMgr() {
		if (conMgr == null) {
			HostConfig config = new HostConfig(HTPC.KODI_HOST, HTPC.KODI_HTTP_PORT, HTPC.KODI_SOCKET_PORT);
			conMgr = new JavaConnectionManager();
			conMgr.registerConnectionListener(new ConnectionListener() {

				@Override
				public void notificationReceived(AbstractEvent event) {
					log.info("Event received: " + event.getClass().getCanonicalName());
				}

				@Override
				public void disconnected() {
					log.info("Event: Disconnected");

				}

				@Override
				public void connected() {
					log.info("Event: Connected");

				}
			});
			log.info("Connecting...");
			conMgr.connect(config);
		}
		return conMgr;
	}
	
	/**
	 * plugin://plugin.video.exodus/?action=clearCacheAndSources
	 */
	public void clearCacheAndSources() {
		log.entering(CLASS_NAME, "clearCacheAndSources");
		String clearCacheAndSourcesCacheURL="?action=clearCacheAndSources";

		log.info("url: " + clearCacheAndSourcesCacheURL);
		
		Addons.ExecuteAddon exodus = new Addons.ExecuteAddon("plugin.video.exodus",clearCacheAndSourcesCacheURL);

		try {
			HTPC.getInstance().getKodiManager().getConMgr().call(exodus, new ApiCallback<String>() {

				@Override
				public void onResponse(AbstractCall<String> call) {
					log.entering(CLASS_NAME, "clearCacheAndSources::onResponse", new Object[] { call });
				}

				@Override
				public void onError(int code, String message, String hint) {
					log.severe("ERROR " + code + ": " + message);
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//sleap  
		try {
			Thread.sleep(HTPC.CLEAR_CACHE_THREAD_WAIT_TIME);
		}
		catch (Exception e) {
			log.severe("ERROR waiting while sleeping when clearing the Kodi Exodus cache.");
		}			
		log.exiting(CLASS_NAME, "clearCacheAndSources");
	}
	
	public void downloadMovieFromKodiExodus(final String movieName,String imdbID) {		
		KodiExodusMovieDownloader downloader = new KodiExodusMovieDownloader(movieName,imdbID);
		downloader.requestDownload();
	}
	
	public DownloadStatus getDownloadStatus(String name)
	{
		for (Iterator<DownloadStatus> iterator = getDownloadStatus().iterator(); iterator.hasNext();) {
			DownloadStatus status = iterator.next();
			if(status.containsInFile(name))
			{
				return status;
			}
		}
		return new DownloadStatus("", "", "", "");
	}

	public Set<String> getKodiCompletedDownloadedFiles() {
		Set<String> files = new LinkedHashSet<String>();
		
		for (Iterator<DownloadStatus> iterator = getDownloadStatus().iterator(); iterator.hasNext();) {
			DownloadStatus status = iterator.next();
			if(status.isComplete())
			{
				files.add(status.getFile());
			}
		}
		return files;
	}
	
	public Set<String> getKodiNotCompletedDownloadedFiles() {
		Set<String> files = new LinkedHashSet<String>();
		
		for (Iterator<DownloadStatus> iterator = getDownloadStatus().iterator(); iterator.hasNext();) {
			DownloadStatus status = iterator.next();
			if(!status.isComplete())
			{
				files.add(status.getFile());
			}
		}
		return files;
	}
	
	public Set<DownloadStatus> getDownloadStatus() {
		return downloadStatus;
	}
	
	private void monitorDownload() {
		if(!monitorDownload)
		{
			return;
		}
		
		final Thread thread = new Thread("Kodi-Exodus-Downloader-PoolMonitor") {
			public void run() {
				Thread thisThread = Thread.currentThread();
				while (true) {
					try {
						//Pool every 5 seconds
						thisThread.sleep(HTPC.KODI_MONITOR_DOWNLOAD_PERCENTAGE_CSV_FILE_LOADER);
						
						Set<DownloadStatus> loadDownloadedStatus=loadKodiDownloadStatusUpdates();
				        downloadStatus.clear();
				        downloadStatus.addAll(loadDownloadedStatus);
					}
					catch (Exception e) {
						e.printStackTrace();
						log.exiting(CLASS_NAME, "Kodi-Exodus-Downloader-PoolMonitor");
					}
				}
			}
		};
		thread.start();		
	}		
	
	synchronized public Set<DownloadStatus> loadKodiDownloadStatusUpdates() {
		Set<DownloadStatus> loadDownloadedStatus = new LinkedHashSet<DownloadStatus>();
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        try {
        	inputStream = new FileInputStream(HTPC.KDOI_PVR_DOWNLOAD_TRACKER);
            reader = new InputStreamReader(inputStream, "UTF-8");
            CSVFormat format = CSVFormat.EXCEL.withHeader().withQuoteMode(QuoteMode.MINIMAL);
            parser = new CSVParser(reader, format);
            try {
                for (final CSVRecord record : parser) {
                	final String percent = record.get("PERCENT").replaceAll("%","");
                	final String file = record.get("FILE");
                	final String downloadsize = record.get("DOWNLOADSIZE");
                	final String totalsize = record.get("TOTALSIZE");
                	DownloadStatus status = new DownloadStatus(percent, file, downloadsize, totalsize);
                	loadDownloadedStatus.add(status);
                }
            }catch (Exception e) {
    			log.severe("Unable to read " + HTPC.KODI_PVR_DOWNLOAD_FILE + ". This might be because it is missing the header: " + e.getMessage());
    			e.printStackTrace();
    		}
            finally {
                try {
                	parser.close();	
				}
				catch (Exception e) {
					log.severe("Unable to close " + HTPC.KODI_PVR_DOWNLOAD_FILE + " Parser: " + e.getMessage());
				}
                try {
                	reader.close();
				}
				catch (Exception e) {
					log.severe("Unable to close " + HTPC.KODI_PVR_DOWNLOAD_FILE + " Reader: " + e.getMessage());
				}
            }	
		}
		catch (Exception e) {
			log.severe("Unable to parse " + HTPC.KODI_PVR_DOWNLOAD_FILE + " Parser: " + e.getMessage());
			e.printStackTrace();
		}
        finally {
            try {
            	parser.close();	
			}
			catch (Exception e) {
				log.severe("Unable to close " + HTPC.KODI_PVR_DOWNLOAD_FILE + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + HTPC.KODI_PVR_DOWNLOAD_FILE + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + HTPC.KODI_PVR_DOWNLOAD_FILE + " InputStream: " + e.getMessage());
			}            
        }
        
    	return loadDownloadedStatus;        
	}
	
	/**
	 * 
	 */
	public void downloadSeriesEpisodeMissing()
	{
		log.entering(CLASS_NAME, "downloadSeriesEpisodeMissing");
		
		while(true)
		{			
			log.info("Calling: PlexPVRManager.getInstance().loadTVShowEpisodesMissing()");
			List<KodiExodusDownloader> downloadItems = HTPC.getInstance().loadTVShowEpisodesMissing(2);
			if(!downloadItems.isEmpty())
			{
				download(downloadItems);
				
				log.info("Completd: downloadSeriesEpisodeMissing");
				for (KodiExodusDownloader item : downloadItems) {
					log.info("Completd items: " + item.toString());
				}
				
			}
			else
			{
				if(HTPC.WAIT_FOR_NEW_REQUESTS_NO_EXIT)
				{
		        	try {
		            	//FIXME we need to pause the thread and check again
		        		Thread.sleep(HTPC.KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME);
					}
					catch (Exception e) {
						log.severe("ERROR waiting while sleeping while polling for lock file.");
					}	
				}
				else
				{
					log.info("There are no more shows to process.");
					break;
				}
			}
		}

		log.entering(CLASS_NAME, "downloadSeriesEpisodeMissing");
	}
	
	
	
	/**
	 * 
	 */
	public void downloadSeriesEpisodeQueue()
	{
		log.entering(CLASS_NAME, "downloadSeriesEpisodeQueue");
		
		while(true)
		{
			log.info("Calling: PlexPVRManager.getInstance().loadTVShowQueue()");			
			List<KodiExodusDownloader> downloadItems = HTPC.getInstance().loadTVShowQueue(2);
			if(!downloadItems.isEmpty())
			{
				download(downloadItems);
				
				log.info("Completd: downloadSeriesEpisodeQueue");
				for (KodiExodusDownloader item : downloadItems) {
					log.info("Completd items: " + item.toString());
				}
				
			}
			else
			{
				log.info("There are no more shows to process.");
				break;
//	        	try {
//	            	//FIXME we need to pause the thread and check again
//	        		Thread.sleep(PlexPVRManager.KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME);
//				}
//				catch (Exception e) {
//					log.severe("ERROR waiting while sleeping while polling for lock file.");
//				}			
			}
		}

		log.entering(CLASS_NAME, "downloadSeriesEpisodeQueue");
	}
	
	/**
	 * 
	 */
	public void downloadMoviesQueue()
	{
		log.entering(CLASS_NAME, "downloadMoviesQueue");
		
		while(true)
		{
			log.info("Calling: PlexPVRManager.getInstance().loadMovieQueue()");
			List<KodiExodusDownloader> downloadItems = HTPC.getInstance().loadMovieQueue(2);
			if(!downloadItems.isEmpty())
			{
				download(downloadItems);
				
				log.info("Completd: downloadMoviesQueue");
				for (KodiExodusDownloader item : downloadItems) {
					log.info("Completd items: " + item.toString());
				}
				
			}
			else
			{
				log.info("There are no more shows to process.");
				break;
//	        	try {
//	            	//FIXME we need to pause the thread and check again
//	        		Thread.sleep(PlexPVRManager.KODI_SERIES_MISSING_EPISODES_EMPTY_SLEEP_TIME);
//				}
//				catch (Exception e) {
//					log.severe("ERROR waiting while sleeping while polling for lock file.");
//				}			
			}
		}

		log.entering(CLASS_NAME, "downloadMoviesQueue");
	}	
		
	
	/**
	 * 
	 */
	private void download(List<KodiExodusDownloader> downloadItems)
	{
		downloaders.addAll(downloadItems);
		ExecutorService executor = Executors.newFixedThreadPool(HTPC.KODI_DOWNLOAD_THREADS);
		
		//here we need to set the first download item ready to run
		if(downloadItems.size()>0)
		{
			downloadItems.get(0).setReadyToRun();
		}
		
		for (Iterator iterator = downloadItems.iterator(); iterator.hasNext();) {
			KodiExodusDownloader downloadItem = (KodiExodusDownloader) iterator.next();
	        executor.submit(downloadItem);
		}
		
		executor.shutdown();
        while (!executor.isTerminated()) {
        	try {
        		Thread.currentThread().sleep(5000);
			}
			catch (Exception e) {
				// TODO BASE_CODE: handle exception
			}
        }
        
        //notify the that the threads have completed so downloading is done and we need to notify the kodi pooling monitor
        System.out.println("Finished all download threads for batch download");
	}
	
	synchronized public void readyToProcessNextRequest(KodiExodusDownloader downloader) {
		int index = downloaders.indexOf(downloader);
		index++;
		if(index < downloaders.size())
		{
			//set the next downloader ready to run
			downloaders.get(index).setReadyToRun();
		}
	}	
}
