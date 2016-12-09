package io.askcloud.pvr.api.kodi;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import io.askcloud.pvr.api.HTPC;
import io.askcloud.pvr.api.kodi.KodiManager.DownloadStatus;
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
public class KodiMovieDownloader extends KodiDownloader {

	//i.e. 24 or Person of Interest
	private String MOVIE_NAME = null;

	private static final String CLASS_NAME = KodiMovieDownloader.class.getName();
	private static final Logger LOG = HTPC.LOG_DOWNLOAD;

	public KodiMovieDownloader(String movieName,String imdbID) {
		super();
		initArgs(movieName, imdbID);
	}
	
	@Override
	protected void setThreadName() {
		
		String threadName = null;
		threadName = "KodiDownloader [" + getLastDownloadStatus().getThreadPrefix() + "% " + MOVIE_NAME + "]";
		final Thread currentThread = Thread.currentThread();
        currentThread.setName(threadName);			
	}
	
	public String getMovieName() {
		return MOVIE_NAME;
	}
	
	@Override
	protected DownloadStatus getUpdatedDownloadStatus() {
		return HTPC.getInstance().getKodiManager().getDownloadStatus(MOVIE_NAME);
	}
	
	/**
	 * 
	 * @param searchName
	 * @param imdbid
	 * @param season
	 * @param episodes
	 */
	public void initArgs(String searchName, String imdbid)
	{
		LOG.entering(CLASS_NAME, "initArgs", new Object[] { searchName,imdbid});
		setImdbID(imdbid);
		MOVIE_NAME = searchName;
		LOG.exiting(CLASS_NAME, "initArgs");
	}


	@Override
	protected void requestDownload() {
		LOG.entering(CLASS_NAME, "requestDownload");
		
		//Exodus calls urllib.quote_plus() on the show name before it makes the rest call
		String movie = URLEncoder.encode(MOVIE_NAME);
		movie = URLEncoder.encode(movie);
		GetDirectory exodus = new GetDirectory(
				"plugin://plugin.video.exodus/?action=moviePage&url=http%3A%2F%2Fapi-v2launch.trakt.tv%2Fsearch%3Ftype%3Dmovie%26limit%3D20%26page%3D1%26query%3D" + movie);
			
		HTPC.getInstance().getKodiManager().getConMgr().call(exodus, new ApiCallback<ListModel.FileItem>() {

			/*
			 * plugin://plugin.video.exodus/?action=play&title=Door+to+Door&year=2002&imdb=tt0274468&meta=%7B%22rating%22%3A+%227.9%22%2C+%22votes%22%3A+%223%2C515%22%2C+%22writer%22%3A+%22William+H.+Macy+%2F+Steven+Schachter%22%2C+%22cast%22%3A+%5B%5B%22William+H.+Macy%22%2C+%22%22%5D%2C+%5B%22Kyra+Sedgwick%22%2C+%22%22%5D%2C+%5B%22Kathy+Baker%22%2C+%22%22%5D%2C+%5B%22Joel+Brooks%22%2C+%22%22%5D%5D%2C+%22plot%22%3A+%22Bill+Porter%2C+a+man+afflicted+with+cerebral+palsy%2C+is+desperate+to+find+a+job+despite+his+condition.+He+uses+his+sense+of+humor%2C+determination+and+winning+spirit+to+convince+a+manager+to+hire+him+as+a+door-to-door+salesman+for+Watkins%2C+a+supplier+of+household+items+and+baking+products.+Porter+walks+several+miles+every+day+on+his+sales+route%2C+eventually+working+his+way+into+the+hearts+of+his+customers.+This+film+is+based+on+a+true+story.%22%2C+%22poster%22%3A+%22https%3A%2F%2Fimages-na.ssl-images-amazon.com%2Fimages%2FM%2FMV5BMjAzMzk2MzkwN15BMl5BanBnXkFtZTYwOTE1Mzg5._V1_SX500.jpg%22%2C+%22title%22%3A+%22Door+to+Door%22%2C+%22originaltitle%22%3A+%22Door+to+Door%22%2C+%22premiered%22%3A+%222002-07-14%22%2C+%22next%22%3A+%22http%3A%2F%2Fapi-v2launch.trakt.tv%2Fsearch%3Flimit%3D20%26type%3Dmovie%26page%3D2%26query%3DThe%2BDoors%22%2C+%22director%22%3A+%22Steven+Schachter%22%2C+%22genre%22%3A+%22Drama%22%2C+%22mediatype%22%3A+%22movie%22%2C+%22imdb%22%3A+%22tt0274468%22%2C+%22trailer%22%3A+%22plugin%3A%2F%2Fplugin.video.exodus%2F%3Faction%3Dtrailer%26name%3DDoor%2Bto%2BDoor%2B%25282002%2529%22%2C+%22year%22%3A+%222002%22%2C+%22duration%22%3A+%225400%22%2C+%22poster3%22%3A+%22https%3A%2F%2Fimage.tmdb.org%2Ft%2Fp%2Fw300%2F3sFU5Tig9bfETcPL1piYXMuGTff.jpg%22%2C+%22metacache%22%3A+true%2C+%22fanart2%22%3A+%22https%3A%2F%2Fimage.tmdb.org%2Ft%2Fp%2Fw1280%2FwkMBBKDf848bQgIhWzrnX1nn9n4.jpg%22%7D&t=20161125145244696000
			 */
			@Override
			public void onResponse(AbstractCall<ListModel.FileItem> call) {
				LOG.entering(CLASS_NAME, "downloadMovie::onResponse", new Object[] { call });

				String movieURL=null;
				//seach for the right tv show
				for (ListModel.FileItem res : call.getResults()) {

					try {
						//take the first item since it will be the one we want 99% of the time
						String pluginURL = res.file;
						String url = URLDecoder.decode(res.file);
						//System.out.println(" searchNav " + call.getResults().size() + " sources");
						LOG.info("Movie: " + url);

						//plugin://plugin.video.exodus/?action=play&title=Door+to+Door&year=2002&imdb=tt0274468&meta=%7B%22rating%22%3A+%227.9%22%2C+%22votes%22%3A+%223%2C515%22%2C+%22writer%22%3A+%22William+H.+Macy+%2F+Steven+Schachter%22%2C+%22cast%22%3A+%5B%5B%22William+H.+Macy%22%2C+%22%22%5D%2C+%5B%22Kyra+Sedgwick%22%2C+%22%22%5D%2C+%5B%22Kathy+Baker%22%2C+%22%22%5D%2C+%5B%22Joel+Brooks%22%2C+%22%22%5D%5D%2C+%22plot%22%3A+%22Bill+Porter%2C+a+man+afflicted+with+cerebral+palsy%2C+is+desperate+to+find+a+job+despite+his+condition.+He+uses+his+sense+of+humor%2C+determination+and+winning+spirit+to+convince+a+manager+to+hire+him+as+a+door-to-door+salesman+for+Watkins%2C+a+supplier+of+household+items+and+baking+products.+Porter+walks+several+miles+every+day+on+his+sales+route%2C+eventually+working+his+way+into+the+hearts+of+his+customers.+This+film+is+based+on+a+true+story.%22%2C+%22poster%22%3A+%22https%3A%2F%2Fimages-na.ssl-images-amazon.com%2Fimages%2FM%2FMV5BMjAzMzk2MzkwN15BMl5BanBnXkFtZTYwOTE1Mzg5._V1_SX500.jpg%22%2C+%22title%22%3A+%22Door+to+Door%22%2C+%22originaltitle%22%3A+%22Door+to+Door%22%2C+%22premiered%22%3A+%222002-07-14%22%2C+%22next%22%3A+%22http%3A%2F%2Fapi-v2launch.trakt.tv%2Fsearch%3Flimit%3D20%26type%3Dmovie%26page%3D2%26query%3DThe%2BDoors%22%2C+%22director%22%3A+%22Steven+Schachter%22%2C+%22genre%22%3A+%22Drama%22%2C+%22mediatype%22%3A+%22movie%22%2C+%22imdb%22%3A+%22tt0274468%22%2C+%22trailer%22%3A+%22plugin%3A%2F%2Fplugin.video.exodus%2F%3Faction%3Dtrailer%26name%3DDoor%2Bto%2BDoor%2B%25282002%2529%22%2C+%22year%22%3A+%222002%22%2C+%22duration%22%3A+%225400%22%2C+%22poster3%22%3A+%22https%3A%2F%2Fimage.tmdb.org%2Ft%2Fp%2Fw300%2F3sFU5Tig9bfETcPL1piYXMuGTff.jpg%22%2C+%22metacache%22%3A+true%2C+%22fanart2%22%3A+%22https%3A%2F%2Fimage.tmdb.org%2Ft%2Fp%2Fw1280%2FwkMBBKDf848bQgIhWzrnX1nn9n4.jpg%22%7D&t=20161125145244696000
						URL urlObj = new URL(pluginURL.replaceAll("plugin://", "http://"));
	
						Map<String, String> query = splitQuery(urlObj);
						String action = query.get("action");
						String title = query.get("title");
						String year = query.get("year");
						String imdb = query.get("imdb");
						String meta = query.get("meta");

						ObjectMapper mapper = new ObjectMapper();
						Map<String, String> map = mapper.readValue(meta, Map.class);
						String rating = (String) map.get("rating");
						String trailer = (String) map.get("trailer");
						String votes = (String) map.get("votes");
						String writer = (String) map.get("writer");
						//String cast = (String) map.get("cast");
						String plot = (String) map.get("plot");
						String poster = (String) map.get("poster");
						String originaltitle = (String) map.get("originaltitle");
						String premiered = (String) map.get("premiered");
						String next = (String) map.get("next");
						String director = (String) map.get("director");
						String mediatype = (String) map.get("mediatype");
						String genre = (String) map.get("genre");
						String duration = (String) map.get("duration");
						LOG.info("action: " + action + " title: " + title + " year " + year + " imdb: " + imdb);
						
						//If an IMDB_ID is provided then lets use it, otherwise use the name
						if(getImdbID() != null)
						{
							if(getImdbID().equals(imdb))
							{
								movieURL=pluginURL;
								break;
							}
						}
						//if IMDB_ID is not provided then use the movie name (not as good as the id)
						else if(MOVIE_NAME.equals(title))
						{
							movieURL=pluginURL;
							break;
						}					
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if(movieURL != null)
				{
					downloadMovieFromURL(MOVIE_NAME,movieURL);
				}
				else
				{
					LOG.severe("ERROR: Unable to find Movie named: " + MOVIE_NAME);
				}
			}

			@Override
			public void onError(int code, String message, String hint) {
				LOG.severe("ERROR " + code + ": " + message);
			}
		});
		LOG.exiting(CLASS_NAME, "requestDownload");
	}

	/**
	 * plugin://plugin.video.exodus/?action=play&title=Door+to+Door&year=2002&imdb=tt0274468&meta=%7B%22rating%22%3A+%227.9%22%2C+%22votes%22%3A+%223%2C515%22%2C+%22writer%22%3A+%22William+H.+Macy+%2F+Steven+Schachter%22%2C+%22cast%22%3A+%5B%5B%22William+H.+Macy%22%2C+%22%22%5D%2C+%5B%22Kyra+Sedgwick%22%2C+%22%22%5D%2C+%5B%22Kathy+Baker%22%2C+%22%22%5D%2C+%5B%22Joel+Brooks%22%2C+%22%22%5D%5D%2C+%22plot%22%3A+%22Bill+Porter%2C+a+man+afflicted+with+cerebral+palsy%2C+is+desperate+to+find+a+job+despite+his+condition.+He+uses+his+sense+of+humor%2C+determination+and+winning+spirit+to+convince+a+manager+to+hire+him+as+a+door-to-door+salesman+for+Watkins%2C+a+supplier+of+household+items+and+baking+products.+Porter+walks+several+miles+every+day+on+his+sales+route%2C+eventually+working+his+way+into+the+hearts+of+his+customers.+This+film+is+based+on+a+true+story.%22%2C+%22poster%22%3A+%22https%3A%2F%2Fimages-na.ssl-images-amazon.com%2Fimages%2FM%2FMV5BMjAzMzk2MzkwN15BMl5BanBnXkFtZTYwOTE1Mzg5._V1_SX500.jpg%22%2C+%22title%22%3A+%22Door+to+Door%22%2C+%22originaltitle%22%3A+%22Door+to+Door%22%2C+%22premiered%22%3A+%222002-07-14%22%2C+%22next%22%3A+%22http%3A%2F%2Fapi-v2launch.trakt.tv%2Fsearch%3Flimit%3D20%26type%3Dmovie%26page%3D2%26query%3DThe%2BDoors%22%2C+%22director%22%3A+%22Steven+Schachter%22%2C+%22genre%22%3A+%22Drama%22%2C+%22mediatype%22%3A+%22movie%22%2C+%22imdb%22%3A+%22tt0274468%22%2C+%22trailer%22%3A+%22plugin%3A%2F%2Fplugin.video.exodus%2F%3Faction%3Dtrailer%26name%3DDoor%2Bto%2BDoor%2B%25282002%2529%22%2C+%22year%22%3A+%222002%22%2C+%22duration%22%3A+%225400%22%2C+%22poster3%22%3A+%22https%3A%2F%2Fimage.tmdb.org%2Ft%2Fp%2Fw300%2F3sFU5Tig9bfETcPL1piYXMuGTff.jpg%22%2C+%22metacache%22%3A+true%2C+%22fanart2%22%3A+%22https%3A%2F%2Fimage.tmdb.org%2Ft%2Fp%2Fw1280%2FwkMBBKDf848bQgIhWzrnX1nn9n4.jpg%22%7D&t=20161125145244696000
	 */
	private void downloadMovieFromURL(String movieName, String movieURL) {
		LOG.entering(CLASS_NAME, "downloadMovieFromURL", new Object[] { movieName, movieURL});
		//movieURL = movieURL.replace("action=play", "action=tvDownloader");
		movieURL = movieURL.replace("plugin://plugin.video.exodus/?action=play", "?action=tvDownloader");

		LOG.info("url: " + movieURL);
		
		//GetDirectory exodus = new GetDirectory(movieURL);
		Addons.ExecuteAddon exodus = new Addons.ExecuteAddon("plugin.video.exodus",movieURL);

		LOG.info("Calling Kodi: Download " + movieName);
		try {
			HTPC.getInstance().getKodiManager().getConMgr().call(exodus, new ApiCallback<String>() {

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
			e.printStackTrace();
		}
		LOG.exiting(CLASS_NAME, "downloadTVShowBySeasonEpisodeFromURL");
	}
}
