package io.askcloud.pvr.imdb.tools;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.askcloud.pvr.api.pvr.PlexPVRManager;
import io.askcloud.pvr.imdb.ImdbException;
import io.askcloud.pvr.imdb.model.AbstractJsonMapping;
import io.askcloud.pvr.imdb.model.ImdbMovieDetails;
import io.askcloud.pvr.imdb.model.ImdbPerson;
import io.askcloud.pvr.imdb.search.SearchDeserializer;
import io.askcloud.pvr.imdb.search.SearchObject;
import io.askcloud.pvr.imdb.wrapper.ResponseDetail;
import io.askcloud.pvr.imdb.wrapper.WrapperResponse;
import io.askcloud.pvr.imdb.wrapper.WrapperSearch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.yamj.api.common.exception.ApiExceptionType;

public final class ApiBuilder {

	private static Logger log = PlexPVRManager.log;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final String BASE_URL = "http://app.imdb.com/";
    private static final String API_VERSION = "v1";
    private static final String APP_ID = "iphone1";
    private static final String SIG = "app1";
    private final HttpTools httpTools;
    /*
     * Jackson JSON configuration
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SearchDeserializer deserializer = new SearchDeserializer();
        deserializer.registerSearchObject("tconst", ImdbMovieDetails.class);
        deserializer.registerSearchObject("nconst", ImdbPerson.class);

        Version ver = new Version(1, 0, 0, null, "io.askcloud.plexpvr.api.imdb.tools", "SearchDeserializer");
        SimpleModule module = new SimpleModule("PolymorphicSearchDeserializerModule", ver);
        module.addDeserializer(SearchObject.class, deserializer);

        MAPPER.registerModule(module);
    }

    public ApiBuilder(HttpClient httpClient) {
        this.httpTools = new HttpTools(httpClient);
    }

    public URL buildUrl(String function, Map<String, String> arguments, Locale locale) throws ImdbException {
        StringBuilder sbURL = new StringBuilder(BASE_URL);

        sbURL.append(function);
        sbURL.append("?api=").append(API_VERSION);
        sbURL.append("&appid=").append(APP_ID);
        sbURL.append("&locale=").append(locale);
        sbURL.append("&timestamp=").append(System.currentTimeMillis() / MILLIS_PER_SECOND);

        for (Map.Entry<String, String> argEntry : arguments.entrySet()) {
            sbURL.append("&").append(argEntry.getKey());
            sbURL.append("=").append(argEntry.getValue());
        }

        sbURL.append("&sig=").append(SIG);

        log.info("URL: "+  sbURL.toString());
        try {
            return new URL(sbURL.toString());
        } catch (MalformedURLException ex) {
            throw new ImdbException(ApiExceptionType.INVALID_URL, "Failed to convert string to URL", sbURL.toString());
        }
    }

    public <T extends AbstractJsonMapping> T getWrapper(Class<T> clazz, String function, Map<String, String> args, Locale locale) throws ImdbException {
        T result;
        URL url = buildUrl(function, args, locale);

        // Make sure we have a "blank" object to return
        try {
            result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new ImdbException(ApiExceptionType.CONNECTION_ERROR, "Failed to instantiate class " + clazz.getSimpleName(), url, ex);
        }

        try {
            String webPage = httpTools.getRequest(url);
            Object response = MAPPER.readValue(webPage, clazz);
            result = clazz.cast(response);
        } catch (JsonParseException ex) {
            throw new ImdbException(ApiExceptionType.MAPPING_FAILED, "JsonParseException", url, ex);
        } catch (JsonMappingException ex) {
            throw new ImdbException(ApiExceptionType.MAPPING_FAILED, "JsonMappingException", url, ex);
        } catch (IOException ex) {
            throw new ImdbException(ApiExceptionType.CONNECTION_ERROR, "IOException", url, ex);
        }

        return result;
    }

    public ResponseDetail getResponse(String function, Map<String, String> args, Locale locale) throws ImdbException {
        WrapperResponse wr = getWrapper(WrapperResponse.class, function, args, locale);
        return wr.getResult();
    }

    public ResponseDetail getResponse(String function, Locale locale) throws ImdbException {
        return getResponse(function, Collections.<String, String>emptyMap(), locale);
    }

    public WrapperSearch getSearchWrapper(String function, Map<String, String> args, Locale locale) throws ImdbException {
        WrapperSearch wrapper = getWrapper(WrapperSearch.class, function, args, locale);

        if (wrapper == null) {
            throw new ImdbException(ApiExceptionType.UNKNOWN_CAUSE, function + " error");
        }

        return wrapper.getSearchData();
    }

}
