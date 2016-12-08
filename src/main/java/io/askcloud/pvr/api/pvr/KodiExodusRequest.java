package io.askcloud.pvr.api.pvr;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.askcloud.pvr.kodi.jsonrpc.api.call.Addons;

/**
 * 
 * @author UFCTester
 *
 */
abstract public class KodiExodusRequest implements Runnable {

	private static String CLASS_NAME = KodiExodusRequest.class.getName();
	protected static Logger log = HTPC.getInstance().getLogger();
	private String IMDB_ID = null;
	private String TVDB_ID = null;

	public KodiExodusRequest() {
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

}
