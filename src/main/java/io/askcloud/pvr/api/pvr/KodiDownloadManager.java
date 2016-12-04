package io.askcloud.pvr.api.pvr;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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
public class KodiDownloadManager {
	private static KodiDownloadManager eINSTANCE;
	private JavaConnectionManager conMgr;

	private static String CLASS_NAME = KodiDownloadManager.class.getName();
	private static Logger log = PlexPVRManager.log;
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
			if((getFile() != null) && (getFile().contains(name)))
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
				// TODO: handle exception
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
	}
	
	private KodiDownloadManager() {
		super();
	}

	public static KodiDownloadManager getInstance() {
		if (eINSTANCE == null) {
			eINSTANCE = new KodiDownloadManager();
			eINSTANCE.clearCacheAndSources();
			eINSTANCE.monitorDownload();
		}
		return eINSTANCE;
	}
	
	public JavaConnectionManager getConMgr() {
		if (conMgr == null) {
			HostConfig config = new HostConfig(PlexPVRManager.KODI_HOST, PlexPVRManager.KODI_HTTP_PORT, PlexPVRManager.KODI_SOCKET_PORT);
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
			KodiDownloadManager.getInstance().getConMgr().call(exodus, new ApiCallback<String>() {

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
		
		//sleap 10 seconds so the cache can be cleared 
		try {
			Thread.sleep(PlexPVRManager.CLEAR_CACHE_THREAD_WAIT_TIME);
		}
		catch (Exception e) {
			// TODO: handle exception
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
						thisThread.sleep(PlexPVRManager.KODI_MONITOR_DOWNLOAD_CSV_FILE_LOADER);
						loadKodiDownloadStatusUpdates();
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
	
	private Set<DownloadStatus> loadKodiDownloadStatusUpdates() {
		Set<DownloadStatus> loadDownloadedStatus = new LinkedHashSet<DownloadStatus>();
		
        InputStream inputStream = null;
        Reader reader = null;
        CSVParser parser = null;
        try {
        	inputStream = new FileInputStream(PlexPVRManager.KDOI_PVR_DOWNLOAD_TRACKER);
            reader = new InputStreamReader(inputStream, "UTF-8");
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
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
    			log.severe("Unable to read " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + ". This might be because it is missing the header: " + e.getMessage());
    			e.printStackTrace();
    		}
            finally {
                try {
                	parser.close();	
				}
				catch (Exception e) {
					log.severe("Unable to close " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + " Parser: " + e.getMessage());
				}
                try {
                	reader.close();
				}
				catch (Exception e) {
					log.severe("Unable to close " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + " Reader: " + e.getMessage());
				}
            }	
		}
		catch (Exception e) {
			log.severe("Unable to parse " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + " Parser: " + e.getMessage());
			e.printStackTrace();
		}
        finally {
            try {
            	parser.close();	
			}
			catch (Exception e) {
				log.severe("Unable to close " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + " Parser: " + e.getMessage());
			}
            try {
            	reader.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + " Reader: " + e.getMessage());
			}
            try {
            	inputStream.close();
			}
			catch (Exception e) {
				log.severe("Unable to close " + PlexPVRManager.KODI_PVR_DOWNLOAD_FILE + " InputStream: " + e.getMessage());
			}            
        }
        
        
        downloadStatus.clear();
        downloadStatus.addAll(loadDownloadedStatus);
    	return downloadStatus;        
	}
	
	/**
	 * 
	 */
	public void downloadEpisodes(List<KodiExodusDownloader> missingEpisodes)
	{
		downloaders.addAll(missingEpisodes);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		//here we need to set the first download item ready to run
		if(missingEpisodes.size()>0)
		{
			missingEpisodes.get(0).setReadyToRun();
		}
		
		for (Iterator iterator = missingEpisodes.iterator(); iterator.hasNext();) {
			KodiExodusDownloader missingEpisode = (KodiExodusDownloader) iterator.next();
	        executor.submit(missingEpisode);
		}
		
		executor.shutdown();
        while (!executor.isTerminated()) {
        	try {
        		Thread.currentThread().sleep(5000);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
        }
        System.out.println("Finished all threads for tvshow batch");
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
