package io.askcloud.pvr.api.kodi;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import io.askcloud.pvr.api.HTPC;
import io.askcloud.pvr.api.kodi.KodiManager.DownloadStatus;

/**
 * 
 * @author UFCTester
 *
 */
abstract public class KodiDownloader implements Runnable {

	private static final String CLASS_NAME = KodiDownloader.class.getName();
	private static final Logger LOG = HTPC.LOG_DOWNLOAD;

	private String IMDB_ID = null;
	private String TVDB_ID = null;

	private Stack<DownloadStatus> downloadStatusStack = new Stack<DownloadStatus>();
	boolean requestHasBeenMade = false;

	/*
	 * we can only kick off the download if there is no other download requests being made at the time.  
	 * Downloads can occur at the same time but the request needs to be done one by one
	 */
	private boolean readyToRun = false;

	public KodiDownloader() {
		super();
	}

	/*
	 * Gey the IMDB ID
	 */
	public String getImdbID() {
		return IMDB_ID;
	}

	public void setImdbID(String iMDB_ID) {
		IMDB_ID = iMDB_ID;
	}

	public String getTVDBID() {
		return TVDB_ID;
	}

	public void setTVDBID(String tVDB_ID) {
		TVDB_ID = tVDB_ID;
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
	protected String normailizeSearchName(String searchString) {
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

		return StringUtils.normalizeSpace(results);
	}

	/**
	 * Set the Thread Name
	 */
	abstract protected void setThreadName();

	/*
	 * Download the videos
	 */
	abstract protected void requestDownload();

	@Override
	public void run() {
		LOG.entering(CLASS_NAME, "run");
		
		while (true) {
			if (readyToRun) {
				break;
			}
			else {
				try {
					//set the thread name
					getLastDownloadStatus().setThreadPrefix("WAITING-KODI-LOAD-SOURCES");
					setThreadName();
					Thread.sleep(HTPC.KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME);
				}
				catch (Exception e) {
					LOG.severe("ERROR SLEEPING (" + HTPC.KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME/1000 + "s) while waiting on starting the thread execution. Thread: " + Thread.currentThread().getName() + " exception: " + e.getMessage());
				}
			}
		}

		try {
			DownloadStatus status = getLastDownloadStatus();
			status.setThreadPrefix(status.getPercent());

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
			if (!requestHasBeenMade) {
				requestHasBeenMade = true;
				HTPC.getInstance().getKodiManager().readyToProcessNextRequest(this);
			}
		}
		
		LOG.exiting(CLASS_NAME, "run");
	}

	public void setReadyToRun() {
		readyToRun = true;
	}

	/**
	 * Monitor the progress until it is determined that the download is stuck or downloaded
	 */
	protected void monitor() {
		LOG.entering(CLASS_NAME, "monitor");
		//Monitor the progress until it is determined that the download is stuck or downloaded
		while (true) {
			//set the thread name
			setThreadName();

			try {
				Thread.sleep(HTPC.KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME);
			}
			catch (Exception e) {
				LOG.severe("ERROR SLEEPING (" + HTPC.KODI_EXODUS_DOWNLOAD_MONITOR_THREAD_WAIT_TIME/1000 + "s) while monitoring the download thread. Thread: " + Thread.currentThread().getName() + " exception: " + e.getMessage());
			}

			DownloadStatus status = getUpdatedDownloadStatus();

			//lets first check if it was picked up.  If it was then we are free to start searching for a new one.
			if ((!requestHasBeenMade) && (!"".equals(status.getDownloaded()))) {
				requestHasBeenMade = true;
				HTPC.getInstance().getKodiManager().readyToProcessNextRequest(this);
			}

			//get the download files
			LOG.info(status.getPercent() + "% Download Status: " + status.getFile());

			//If it is complete then we can return
			if (status.isComplete()) {
				downloadCompleted(status);
				LOG.exiting(CLASS_NAME, "monitor");
				return;
			}

			int currentPercent = status.getPercentInt();

			//Stop if the percent does not change after 5 mins.
			//At the rate of 10 seconds per loop, we need 50 status objects with the same percentage to stop
			final int maxIdleThreshold = 15;
			if (downloadStatusStack.size() > maxIdleThreshold) {
				int counter = 0;
				for (Iterator iterator = downloadStatusStack.iterator(); iterator.hasNext();) {
					DownloadStatus downloadStatus = (DownloadStatus) iterator.next();

					//percent matches
					if (downloadStatus.getPercentInt() == currentPercent) {
						counter++;
					}
				}

				//we are over 50
				if (counter > maxIdleThreshold) {
					downloadHung(status);
					LOG.exiting(CLASS_NAME, "monitor");
					return;
				}
			}

			//otherwise if we got this far just continue to wait and try it again.
			downloadStatusStack.push(status);
			setThreadName();
		}
		
		
	}

	/**
	 * The download completed and is 100%
	 */
	protected void downloadCompleted(DownloadStatus status) {
		LOG.info("Download is complete: " + status.getPercent() + "%" + status.getFile());
	}

	/**
	 * 
	 */
	protected void downloadHung(DownloadStatus status) {
		LOG.severe("Download Looks like it is hung: " + toString());
	}

	abstract protected DownloadStatus getUpdatedDownloadStatus();

	/**
	 * @return
	 */
	public DownloadStatus getLastDownloadStatus() {
		if (downloadStatusStack.size() == 0) {
			DownloadStatus status = getUpdatedDownloadStatus();
			downloadStatusStack.push(status);
			return status;
		}
		else {
			return downloadStatusStack.peek();
		}
	}
}
