package io.askcloud.pvr.api.pvr;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import io.askcloud.pvr.api.pvr.KodiDownloadManager.DownloadStatus;
import io.askcloud.pvr.kodi.jsonrpc.api.AbstractCall;
import io.askcloud.pvr.kodi.jsonrpc.api.call.Addons;
import io.askcloud.pvr.kodi.jsonrpc.api.call.Files.GetDirectory;
import io.askcloud.pvr.kodi.jsonrpc.io.ApiCallback;
import io.askcloud.pvr.kodi.jsonrpc.model.ListModel;

/**
 * 
 * @author UFCTester
 *
 */
public class KodiExodusTVShowDownloader extends KodiExodusDownloader {

	//i.e. 24 or Person of Interest
	private String SHOW_NAME = null;
	
	//
	private int SEASON = -1;
	
	private int EPISODE = -1;
	
	private DownloadStatus downloadStatus=null;
	
	private static String CLASS_NAME = KodiExodusTVShowDownloader.class.getName();
	private static Logger log = PlexPVRManager.getInstance().getLogger();
	//
	//	public KodiExodusTVShowSeasonDownloader(String showName, String imdbid) {
	//		this(showName,imdbid, -1, -1);
	//
	//	}
	//
	//	public KodiExodusTVShowSeasonDownloader(String showName,String imdbid, int season) {
	//
	//		this(showName,imdbid, season, -1);
	//	}

	public KodiExodusTVShowDownloader(String searchName, String imdbid, String tvdbid,int season, int episode) {

		super();
		log.entering(CLASS_NAME, "KodiExodusDownloader");
		log.exiting(CLASS_NAME, "KodiExodusDownloader");
		imdbid=(StringUtils.isNotBlank(imdbid))?imdbid:null;
		setImdbID(imdbid);
		setTVDBID(tvdbid);
		SHOW_NAME = searchName;
		SEASON = season;
		EPISODE = episode;
	}

	
	@Override
	protected void setThreadName() {
		String threadName = null;
		threadName = "KodiDownloader [" + getLastDownloadStatus().getThreadPrefix() + "% " + SHOW_NAME + " " + "S" + PlexPVRManager.getInstance().getSeason(Integer.valueOf(SEASON).toString()) + "E" + PlexPVRManager.getInstance().getEpisode(Integer.valueOf(EPISODE).toString()) + "]";
		final Thread currentThread = Thread.currentThread();
        currentThread.setName(threadName);	
	}
	
	
	@Override
	protected DownloadStatus getUpdatedDownloadStatus() {
		String name = SHOW_NAME + " S" + PlexPVRManager.getInstance().getSeason(Integer.valueOf(SEASON).toString()) + "E" + PlexPVRManager.getInstance().getEpisode(Integer.valueOf(EPISODE).toString());
		return KodiDownloadManager.getInstance().getDownloadStatus(name);
	}

	@Override
	protected void requestDownload() {
		log.entering(CLASS_NAME, "requestDownload");

		//Exodus calls urllib.quote_plus() on the show name before it makes the rest call
		String tvShow = URLEncoder.encode(SHOW_NAME);
		tvShow = URLEncoder.encode(tvShow);
		GetDirectory exodus = new GetDirectory(
				"plugin://plugin.video.exodus/?action=tvshowPage&url=http%3A%2F%2Fapi-v2launch.trakt.tv%2Fsearch%3Ftype%3Dshow%26limit%3D3020%26page%3D1%26query%3D" + tvShow);

		log.info("Season URL: " + "plugin://plugin.video.exodus/?action=tvshowPage&url=http%3A%2F%2Fapi-v2launch.trakt.tv%2Fsearch%3Ftype%3Dshow%26limit%3D30%26page%3D1%26query%3D" + tvShow);
		KodiDownloadManager.getInstance().getConMgr().call(exodus, new ApiCallback<ListModel.FileItem>() {

			/*
			 * plugin://plugin.video.exodus/?action=seasons&tvshowtitle=Person of Interest&year=2011&imdb=tt1839578&tvdb=248742
			 */
			@Override
			public void onResponse(AbstractCall<ListModel.FileItem> call) {
				log.entering(CLASS_NAME, "downloadTVShow::onResponse", new Object[] { call });

				String tvShowURL = null;
				//seach for the right tv show
				for (ListModel.FileItem res : call.getResults()) {

					try {
						//take the first item since it will be the one we want 99% of the time
						String pluginURL = res.file;
						String url = URLDecoder.decode(res.file);
						//System.out.println(" searchNav " + call.getResults().size() + " sources");
						log.info("TV Show: " + url);
						//plugin://plugin.video.exodus/?action=seasons&tvshowtitle=Brothers & Sisters&year=2006&imdb=tt0758737&tvdb=79506
						URL urlObj = new URL(pluginURL.replaceAll("plugin://", "http://"));

						//{action=episodes, tvshowtitle=Person of Interest, year=2011, imdb=tt1839578, tvdb=248742, season=1}
						Map<String, String> query = splitQuery(urlObj);
						String action = query.get("action");
						String tvshowtitle = query.get("tvshowtitle");
						String year = query.get("year");
						String imdb = query.get("imdb");
						String tvdb = query.get("tvdb");

						//check if tvdbid is set first
						if(getTVDBID() != null)
						{
							if (getTVDBID().equals(tvdb)) {
								tvShowURL = pluginURL;
								break;
							}							
						}
						
						//If an IMDB_ID is provided then lets use it, otherwise use the name
						else if (getImdbID() != null) {
							if (getImdbID().equals(imdb)) {
								tvShowURL = pluginURL;
								break;
							}
						}
						//if IMDB_ID is not provided then use the tv show name (not as good as the id)
						else if (SHOW_NAME.equals(tvshowtitle)) {
							tvShowURL = pluginURL;
							break;
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (tvShowURL != null) {
					downloadTVShowSeasons(tvShowURL);
				}
				else {
					log.severe("ERROR: Unable to find TV Show named: " + SHOW_NAME);
				}
			}

			@Override
			public void onError(int code, String message, String hint) {
				log.severe("ERROR " + code + ": " + message);
			}
		});
		log.exiting(CLASS_NAME, "requestDownload");
	}

	/**
	 * plugin://plugin.video.exodus/?action=seasons&tvshowtitle=Person of Interest&year=2011&imdb=tt1839578&tvdb=248742
	 */
	private void downloadTVShowSeasons(String tvShowURL) {
		log.entering(CLASS_NAME, "downloadTVShowSeasons", new Object[] { tvShowURL });
		String decode = URLDecoder.decode(tvShowURL);
		log.fine("donwloadTVShowSeasonURL: " + decode);
		GetDirectory exodus = new GetDirectory(tvShowURL);

		KodiDownloadManager.getInstance().getConMgr().call(exodus, new ApiCallback<ListModel.FileItem>() {

			@Override
			public void onResponse(AbstractCall<ListModel.FileItem> call) {
				log.entering(CLASS_NAME, "downloadTVShowSeasons::onResponse", new Object[] { call });

				for (ListModel.FileItem res : call.getResults()) {
					//log.fine("  " + res.file);
					try {

						URL urlObj = new URL(res.file.replaceAll("plugin://", "http://"));

						//{action=episodes, tvshowtitle=Person of Interest, year=2011, imdb=tt1839578, tvdb=248742, season=1}
						Map<String, String> query = splitQuery(urlObj);
						String action = query.get("action");
						String tvshowtitle = query.get("tvshowtitle");
						String year = query.get("year");
						String imdb = query.get("imdb");
						String tvdb = query.get("tvdb");
						String season = query.get("season");
						log.finest("action: " + action + " tvshow: " + tvshowtitle + " Season " + season + " year: " + year + " imdb: " + imdb + " tvdb: " + tvdb);
						downloadTVShowSeasonEpisode(tvshowtitle, season, res.file);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(int code, String message, String hint) {
				log.severe("ERROR " + code + ": " + message);
			}
		});

		log.exiting(CLASS_NAME, "downloadTVShowSeasons");
	}

	/**
	 * plugin://plugin.video.exodus/?action=episodes&tvshowtitle=Person+of+Interest&year=2011&imdb=tt1839578&tvdb=248742&season=1
	 */
	private void downloadTVShowSeasonEpisode(String tvshowtitle, String season, String tvShowSeasonURL) {

		if(SEASON < 1){
			return;
		}

		log.entering(CLASS_NAME, "downloadTVShowSeasonEpisode", new Object[] { tvshowtitle, season, tvShowSeasonURL });
		GetDirectory exodus = new GetDirectory(tvShowSeasonURL);

		KodiDownloadManager.getInstance().getConMgr().call(exodus, new ApiCallback<ListModel.FileItem>() {

			@Override
			public void onResponse(AbstractCall<ListModel.FileItem> call) {

				//This is called for each season episode
				for (ListModel.FileItem res : call.getResults()) {
					log.fine("  " + res.file);
					try {
						URL urlObj = new URL(res.file.replaceAll("plugin://", "http://"));

						//{action=play, title=Pilot, year=2011, imdb=tt1839578, tvdb=248742, season=1, episode=1, tvshowtitle=Person of Interest, premiered=2011-09-22, meta={"rating": "7.7", "imdb": "tt1839578", "year": "2011", "duration": "2700", "plot": "Reese and Finch must figure out whether a young prosecutor is a victim or a suspect.", "votes": "293", "thumb": "http://thetvdb.com/banners/episodes/248742/4099507.jpg", "title": "Pilot", "tvdb": "248742", "mpaa": "TV-14", "writer": "Jonathan Nolan", "label": "Pilot", "season": "1", "status": "Ended", "poster": "http://thetvdb.com/banners/posters/248742-16.jpg", "tvshowtitle": "Person of Interest", "mediatype": "episode", "director": "David Semel", "studio": "CBS", "genre": "Action / Adventure / Drama / Mystery / Thriller", "banner": "http://thetvdb.com/banners/graphical/248742-g11.jpg", "fanart": "http://thetvdb.com/banners/fanart/original/248742-21.jpg", "episode": "1", "premiered": "2011-09-22", "cast": [["Michael Emerson", ""], ["Jim Caviezel", ""], ["Amy Acker", ""], ["Sarah Shahi", ""], ["Taraji P. Henson", ""], ["Kevin Chapman", ""], ["Paige Turco", ""], ["John Nolan", ""], ["Enrico Colantoni", ""]], "trailer": "plugin://plugin.video.exodus/?action=trailer&name=Person+of+Interest"}, t=20161121182419430000}
						Map<String, String> query = splitQuery(urlObj);
						String action = query.get("action");
						String title = query.get("title");
						String year = query.get("year");
						String imdb = query.get("imdb");
						String tvdb = query.get("tvdb");
						String season = query.get("season");
						String episode = query.get("episode");
						String tvshowtitle = query.get("tvshowtitle");
						String premiered = query.get("premiered");
						String meta = query.get("meta");

						ObjectMapper mapper = new ObjectMapper();
						Map<String, String> map = mapper.readValue(meta, Map.class);
						String label = (String) map.get("label");
						String trailer = (String) map.get("trailer");
						String fanart = (String) map.get("fanart");
						String poster = (String) map.get("poster");

						//info("action: " + action + " tvshow: " + tvshowtitle + " Season " + season + " Episode " + episode + " year: " + year + " imdb: " + imdb + " tvdb: " + tvdb);
						//info("title: " + title + " S" + seasonStr + "E" + episodeStr + " year: " + year + " imdb: " + imdb + " tvdb: " + tvdb);
						log.info("Season " + PlexPVRManager.getInstance().getSeason(season) + "E" + PlexPVRManager.getInstance().getEpisode(episode) + " title: " + title + " premiered: " + premiered + " year: " + year + " imdb: " + imdb + " tvdb: " + tvdb);
						downloadTVShowBySeasonEpisodeFromURL(tvshowtitle, season, episode, res.file);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(int code, String message, String hint) {
				log.severe("ERROR " + code + ": " + message);
			}
		});
		log.exiting(CLASS_NAME, "downloadTVShowSeasonEpisode");
	}

	/**
	 * plugin://plugin.video.exodus/?action=play&title=Pilot&year=2011&imdb=tt1839578&tvdb=248742&season=1&episode=1&tvshowtitle=Person+of+Interest&premiered=2011-09-22&meta=%7B%22rating%22%3A+%227.7%22%2C+%22imdb%22%3A+%22tt1839578%22%2C+%22year%22%3A+%222011%22%2C+%22duration%22%3A+%222700%22%2C+%22plot%22%3A+%22Reese+and+Finch+must+figure+out+whether+a+young+prosecutor+is+a+victim+or+a+suspect.%22%2C+%22votes%22%3A+%22293%22%2C+%22thumb%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Fepisodes%2F248742%2F4099507.jpg%22%2C+%22title%22%3A+%22Pilot%22%2C+%22tvdb%22%3A+%22248742%22%2C+%22mpaa%22%3A+%22TV-14%22%2C+%22writer%22%3A+%22Jonathan+Nolan%22%2C+%22label%22%3A+%22Pilot%22%2C+%22season%22%3A+%221%22%2C+%22status%22%3A+%22Ended%22%2C+%22poster%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Fposters%2F248742-16.jpg%22%2C+%22tvshowtitle%22%3A+%22Person+of+Interest%22%2C+%22mediatype%22%3A+%22episode%22%2C+%22director%22%3A+%22David+Semel%22%2C+%22studio%22%3A+%22CBS%22%2C+%22genre%22%3A+%22Action+%2F+Adventure+%2F+Drama+%2F+Mystery+%2F+Thriller%22%2C+%22banner%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Fgraphical%2F248742-g11.jpg%22%2C+%22fanart%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Ffanart%2Foriginal%2F248742-21.jpg%22%2C+%22episode%22%3A+%221%22%2C+%22premiered%22%3A+%222011-09-22%22%2C+%22cast%22%3A+%5B%5B%22Michael+Emerson%22%2C+%22%22%5D%2C+%5B%22Jim+Caviezel%22%2C+%22%22%5D%2C+%5B%22Amy+Acker%22%2C+%22%22%5D%2C+%5B%22Sarah+Shahi%22%2C+%22%22%5D%2C+%5B%22Taraji+P.+Henson%22%2C+%22%22%5D%2C+%5B%22Kevin+Chapman%22%2C+%22%22%5D%2C+%5B%22Paige+Turco%22%2C+%22%22%5D%2C+%5B%22John+Nolan%22%2C+%22%22%5D%2C+%5B%22Enrico+Colantoni%22%2C+%22%22%5D%5D%2C+%22trailer%22%3A+%22plugin%3A%2F%2Fplugin.video.exodus%2F%3Faction%3Dtrailer%26name%3DPerson%2Bof%2BInterest%22%7D&t=20161121170832290000
	 */
	private void downloadTVShowBySeasonEpisodeFromURL(String tvshowtitle, String season, String episode, String tvShowSeasonEpisodeURL) {
		log.entering(CLASS_NAME, "downloadTVShowBySeasonEpisodeFromURL", new Object[] { tvshowtitle, season, episode, tvShowSeasonEpisodeURL });
		if(Integer.valueOf(season).intValue() != SEASON) {
			return;
		}

		boolean shouldDownloadEpisode = false;
		//check to see if all episodes should be downloaded or this specific one
		//-1 means all episodes so break out of the look instead of returning
		if (EPISODE == -1) {
			shouldDownloadEpisode = false;
		}
		else if (EPISODE == Integer.valueOf(episode).intValue()) {
			shouldDownloadEpisode = true;
		}

		if (!shouldDownloadEpisode) {
			log.info("EPISODES DOES NOT INCLUDE " + episode + " SO DO NOT DOWNLOAD TVSHOW: " + tvshowtitle + " season: " + season + " episode: " + episode);
			return;
		}
		else {
			log.info("EPISODES includes " + episode + " so download tvshow: " + tvshowtitle + " season: " + season + " episode: " + episode);
		}

		//Check if it is a missing episode
		boolean episodeExists = episodeExists(tvshowtitle, season, episode);
		if (episodeExists) {
			return;
		}
		
		//plugin://plugin.video.exodus/?action=play&title=Pilot&year=2011&imdb=tt1839578&tvdb=248742&season=1&episode=1&tvshowtitle=Person+of+Interest&premiered=2011-09-22&meta=%7B%22rating%22%3A+%227.7%22%2C+%22imdb%22%3A+%22tt1839578%22%2C+%22year%22%3A+%222011%22%2C+%22duration%22%3A+%222700%22%2C+%22plot%22%3A+%22Reese+and+Finch+must+figure+out+whether+a+young+prosecutor+is+a+victim+or+a+suspect.%22%2C+%22votes%22%3A+%22293%22%2C+%22thumb%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Fepisodes%2F248742%2F4099507.jpg%22%2C+%22title%22%3A+%22Pilot%22%2C+%22tvdb%22%3A+%22248742%22%2C+%22mpaa%22%3A+%22TV-14%22%2C+%22writer%22%3A+%22Jonathan+Nolan%22%2C+%22label%22%3A+%22Pilot%22%2C+%22season%22%3A+%221%22%2C+%22status%22%3A+%22Ended%22%2C+%22poster%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Fposters%2F248742-16.jpg%22%2C+%22tvshowtitle%22%3A+%22Person+of+Interest%22%2C+%22mediatype%22%3A+%22episode%22%2C+%22director%22%3A+%22David+Semel%22%2C+%22studio%22%3A+%22CBS%22%2C+%22genre%22%3A+%22Action+%2F+Adventure+%2F+Drama+%2F+Mystery+%2F+Thriller%22%2C+%22banner%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Fgraphical%2F248742-g11.jpg%22%2C+%22fanart%22%3A+%22http%3A%2F%2Fthetvdb.com%2Fbanners%2Ffanart%2Foriginal%2F248742-21.jpg%22%2C+%22episode%22%3A+%221%22%2C+%22premiered%22%3A+%222011-09-22%22%2C+%22cast%22%3A+%5B%5B%22Michael+Emerson%22%2C+%22%22%5D%2C+%5B%22Jim+Caviezel%22%2C+%22%22%5D%2C+%5B%22Amy+Acker%22%2C+%22%22%5D%2C+%5B%22Sarah+Shahi%22%2C+%22%22%5D%2C+%5B%22Taraji+P.+Henson%22%2C+%22%22%5D%2C+%5B%22Kevin+Chapman%22%2C+%22%22%5D%2C+%5B%22Paige+Turco%22%2C+%22%22%5D%2C+%5B%22John+Nolan%22%2C+%22%22%5D%2C+%5B%22Enrico+Colantoni%22%2C+%22%22%5D%5D%2C+%22trailer%22%3A+%22plugin%3A%2F%2Fplugin.video.exodus%2F%3Faction%3Dtrailer%26name%3DPerson%2Bof%2BInterest%22%7D&t=20161121170832290000
		tvShowSeasonEpisodeURL = tvShowSeasonEpisodeURL.replace("plugin://plugin.video.exodus/?action=play", "?action=tvDownloader");
		log.info("url: " + tvShowSeasonEpisodeURL);

		Addons.ExecuteAddon exodus = new Addons.ExecuteAddon("plugin.video.exodus", tvShowSeasonEpisodeURL);
		//GetDirectory exodus = new GetDirectory(tvShowSeasonEpisodeURL);

		log.info("Calling Kodi: Download " + tvshowtitle + " S" + PlexPVRManager.getInstance().getSeason(season) + "E" + PlexPVRManager.getInstance().getEpisode(episode));
		//KodiDownloadManager.getInstance().getConMgr().call(exodus, null);
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
	
		log.exiting(CLASS_NAME, "downloadTVShowBySeasonEpisodeFromURL");
	}


	private boolean episodeExists(String tvShowName, String season, String episode) {
		String showFileName = tvShowName + " " + "S" + PlexPVRManager.getInstance().getSeason(season) + "E" + PlexPVRManager.getInstance().getEpisode(episode);

		log.info("Checking if episode exists.  tvShowName: " + tvShowName + " season: " + season + " episode: " + episode);

		//mp4
		File file = new File(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR + "\\" + tvShowName + "\\Season " + season + "\\" + showFileName + ".mp4");
		if (file.exists() && !file.isDirectory()) {
			log.info("Episode alerady exists: " + file.toString());
			return true;
		}

		file = new File(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR + "\\" + tvShowName + "\\Season " + season + "\\" + showFileName + ".mkv");
		if (file.exists() && !file.isDirectory()) {
			log.info("Episode alerady exists: " + file.toString());
			return true;
		}

		file = new File(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR + "\\" + tvShowName + "\\Season " + season + "\\" + showFileName + ".avi");
		if (file.exists() && !file.isDirectory()) {
			log.info("Episode alerady exists: " + file.toString());
			return true;
		}

		file = new File(PlexPVRManager.KODI_DOWNLOAD_TVSHOWS_DIR + "\\" + tvShowName + "\\Season " + season + "\\" + showFileName + ".flv");
		if (file.exists() && !file.isDirectory()) {
			log.info("Episode alerady exists: " + file.toString());
			return true;
		}

		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("TVDB_ID: " + getTVDBID() + " SeriesName: " + SHOW_NAME + " S" + PlexPVRManager.getInstance().getSeason(Integer.valueOf(SEASON).toString()) + "E" + PlexPVRManager.getInstance().getEpisode(Integer.valueOf(EPISODE).toString()));
		return sBuffer.toString();
	}	
}
