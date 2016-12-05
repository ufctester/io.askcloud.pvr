package io.askcloud.pvr.imdb.tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.yamj.api.common.exception.ApiExceptionType;
import org.yamj.api.common.http.DigestedResponse;
import org.yamj.api.common.http.DigestedResponseReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.askcloud.pvr.api.pvr.PlexPVRManager;
import io.askcloud.pvr.imdb.ImdbException;
import io.askcloud.pvr.imdb.model.AbstractJsonMapping;
import io.askcloud.pvr.imdb.model.ImdbError;
import io.askcloud.pvr.imdb.model.ImdbMovieDetails;
import io.askcloud.pvr.imdb.model.ImdbPerson;
import io.askcloud.pvr.imdb.search.SearchDeserializer;
import io.askcloud.pvr.imdb.search.SearchObject;
import io.askcloud.pvr.imdb.wrapper.ResponseDetail;
import io.askcloud.pvr.imdb.wrapper.WrapperResponse;
import io.askcloud.pvr.imdb.wrapper.WrapperSearch;

public final class ApiBuilder {

	private static Logger log = PlexPVRManager.getInstance().getLogger();
    private static final int MILLIS_PER_SECOND = 1000;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Charset CHARSET = Charset.forName(DEFAULT_CHARSET);
    private static HttpClient httpClient;
    private static final String BASE_URL = "http://app.imdb.com/";
    private static final String API_VERSION = "v1";
    private static final String APP_ID = "iphone1";
    private static final String SIG = "app1";
    private static Locale imdbLocale = Locale.getDefault();
    /*
     * Jackson JSON configuration
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SearchDeserializer deserializer = new SearchDeserializer();
        deserializer.registerSearchObject("tconst", ImdbMovieDetails.class);
        deserializer.registerSearchObject("nconst", ImdbPerson.class);

        Version ver = new Version(1, 0, 0, null, "com.omertron.imdbapi.tools", "SearchDeserializer");
        SimpleModule module = new SimpleModule("PolymorphicSearchDeserializerModule", ver);
        module.addDeserializer(SearchObject.class, deserializer);

        MAPPER.registerModule(module);
    }

    private ApiBuilder() {
        throw new UnsupportedOperationException("Class cannot be instantiate");
    }

    public static void setHttpClient(HttpClient httpClient) {
        ApiBuilder.httpClient = httpClient;
    }

    public static void setLocale(Locale locale) {
        ApiBuilder.imdbLocale = locale;
        log.fine("Setting locale to " + imdbLocale.toString());
    }

    public static URL buildUrl(String function, Map<String, String> arguments) {
        StringBuilder sbURL = new StringBuilder(BASE_URL);

        sbURL.append(function);
        sbURL.append("?api=").append(API_VERSION);
        sbURL.append("&appid=").append(APP_ID);
        sbURL.append("&locale=").append(imdbLocale);
        sbURL.append("&timestamp=").append(System.currentTimeMillis() / MILLIS_PER_SECOND);

        for (Map.Entry<String, String> argEntry : arguments.entrySet()) {
            sbURL.append("&").append(argEntry.getKey());
            sbURL.append("=").append(argEntry.getValue());
        }

        sbURL.append("&sig=").append(SIG);

        log.fine("URL " + sbURL.toString());
        try {
            return new URL(sbURL.toString());
        } catch (MalformedURLException ex) {
            log.severe("Failed to convert string to URL: " +  ex.getMessage());
            return null;
        }
    }

    public static <T extends AbstractJsonMapping> T getWrapper(Class<T> clazz, String function, Map<String, String> args) {
        T result;

        // Make sure we have a "blank" object to return
        try {
            result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            log.severe("Failed to instantiate class " + clazz.getSimpleName()+ " exception: " + ex.getMessage());
            return null;
        }

        try {
            String webPage = requestWebPage(buildUrl(function, args));
            Object response = MAPPER.readValue(webPage, clazz);
            result = clazz.cast(response);
        } catch (JsonParseException ex) {
        	log.severe("JsonParseException " + " exception: " + ex.getMessage());
            result.setStatusMessage("JsonParseException: " + ex.getMessage(), ex);
        } catch (JsonMappingException ex) {
        	log.severe("JsonMappingException " + " exception: " + ex.getMessage());
            result.setStatusMessage("JsonMappingException: " + ex.getMessage(), ex);
        } catch (IOException ex) {
        	log.severe("IOException " + " exception: " + ex.getMessage());
            result.setStatusMessage("IOException: " + ex.getMessage(), ex);
        } catch (ImdbException ex) {
        	log.severe("ImbdException " + " exception: " + ex.getMessage());
            result.setStatusMessage("ImdbExceptio2n: " + ex.getResponse(), ex);
        }

        return result;
    }

    public static ResponseDetail getResponse(String function, Map<String, String> args) {
        WrapperResponse wr = getWrapper(WrapperResponse.class, function, args);
        return wr.getResult();
    }

    public static ResponseDetail getResponse(String function) {
        return getResponse(function, Collections.<String, String>emptyMap());
    }

    public static WrapperSearch getSearchWrapper(String function, Map<String, String> args) {
        WrapperSearch wrapper = getWrapper(WrapperSearch.class, function, args);

        if (wrapper == null) {
            return null;
        }

        return wrapper.getSearchData();
    }

    /**
     * Download the URL into a String
     *
     * @param url
     * @return
     * @throws AllocineException
     */
    private static String requestWebPage(URL url) throws ImdbException {
        try {
            HttpGet httpGet = new HttpGet(url.toURI());
            httpGet.addHeader("accept", "application/json");

            final DigestedResponse response = DigestedResponseReader.requestContent(httpClient, httpGet, CHARSET);

            if (response.getStatusCode() == 0) {
                throw new ImdbException(ApiExceptionType.CONNECTION_ERROR, "Error retrieving URL", url);
            } else if (response.getStatusCode() >= 500) {
                ImdbError error = MAPPER.readValue(response.getContent(), ImdbError.class);
                throw new ImdbException(ApiExceptionType.HTTP_503_ERROR, error.getStatusMessage().getMessage(), url);
            } else if (response.getStatusCode() >= 300) {
                ImdbError error = MAPPER.readValue(response.getContent(), ImdbError.class);
                throw new ImdbException(ApiExceptionType.HTTP_404_ERROR, error.getStatusMessage().getMessage(), url);
            }

            return response.getContent();
        } catch (URISyntaxException ex) {
            throw new ImdbException(ApiExceptionType.INVALID_URL, "Invalid URL", url, ex);
        } catch (IOException ex) {
            throw new ImdbException(ApiExceptionType.CONNECTION_ERROR, "Error retrieving URL", url, ex);
        }
    }
}
