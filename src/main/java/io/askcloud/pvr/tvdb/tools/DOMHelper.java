package io.askcloud.pvr.tvdb.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.yamj.api.common.exception.ApiExceptionType;
import org.yamj.api.common.http.DigestedResponse;
import org.yamj.api.common.http.DigestedResponseReader;

import io.askcloud.pvr.api.HTPC;
import io.askcloud.pvr.imdb.tools.ApiBuilder;
import io.askcloud.pvr.tvdb.TvDbException;

/**
 * Generic set of routines to process the DOM model data
 *
 * @author Stuart.Boston
 *
 */
public class DOMHelper {

	private static final String CLASS_NAME = DOMHelper.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);
    private static final String YES = "yes";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Charset CHARSET = Charset.forName(DEFAULT_CHARSET);
    private static final int RETRY_COUNT = 5;
    // Milliseconds to retry
    private static final int RETRY_TIME = 250;
    private static HttpClient httpClient = null;
    // Constants
    private static final String ERROR_WRITING = "Error writing the document to {}";
    private static final String ERROR_UNABLE_TO_PARSE = "Unable to parse TheTVDb response, please try again later.";
    private static final int HTTP_STATUS_300 = 300;
    private static final int HTTP_STATUS_500 = 500;

    // Hide the constructor
    protected DOMHelper() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    public static void setHttpClient(HttpClient newHttpClient) {
        httpClient = newHttpClient;
    }

    /**
     * Gets the string value of the tag element name passed
     *
     * @param element
     * @param tagName
     * @return
     */
    public static String getValueFromElement(Element element, String tagName) {
        NodeList elementNodeList = element.getElementsByTagName(tagName);
        if (elementNodeList == null) {
            return "";
        } else {
            Element tagElement = (Element) elementNodeList.item(0);
            if (tagElement == null) {
                return "";
            }

            NodeList tagNodeList = tagElement.getChildNodes();
            if (tagNodeList == null || tagNodeList.getLength() == 0) {
                return "";
            }
            return tagNodeList.item(0).getNodeValue();
        }
    }

    /**
     * Get a DOM document from the supplied URL
     *
     * @param url
     * @return
     * @throws com.omertron.thetvdbapi.TvDbException
     */
    public static synchronized Document getEventDocFromUrl(String url) throws TvDbException {
        Document doc = null;

        String webPage = getValidWebpage(url);
        
        // If the webpage returned is null or empty, quit
        if(StringUtils.isBlank(webPage)){
            return null;
        }
        
        try (InputStream in = new ByteArrayInputStream(webPage.getBytes(CHARSET))) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(in);
            doc.getDocumentElement().normalize();
        } catch (UnsupportedEncodingException ex) {
            throw new TvDbException(ApiExceptionType.INVALID_URL, "Unable to encode URL", url, ex);
        } catch (ParserConfigurationException | SAXException | IOException error) {
            throw new TvDbException(ApiExceptionType.MAPPING_FAILED, ERROR_UNABLE_TO_PARSE, url, error);
        }

        return doc;
    }

    private static String getValidWebpage(String url) throws TvDbException {
        // Count the number of times we download the web page
        int retryCount = 0;
        // Is the web page valid
        boolean valid = false;
        DigestedResponse webPage;

        while (!valid && (retryCount < RETRY_COUNT)) {
            retryCount++;
            webPage = requestWebPage(url);
            final String content = webPage.getContent();
            if (StringUtils.isNotBlank(content)) {
                // See if the ID is null
                if (!content.contains("<id>") || content.contains("<id></id>")) {
                    // Wait an increasing amount of time the more retries that happen
                    waiting(retryCount * RETRY_TIME);
                    continue;
                }

                valid = true;
            }

            // Couldn't get a valid webPage so, quit.
            if (!valid) {
                throw new TvDbException(ApiExceptionType.UNKNOWN_CAUSE, webPage.getContent(), webPage.getStatusCode(), url);
            }

            return content;
        }

        return null;
    }

    /**
     * Convert a DOM document to a string
     *
     * @param doc
     * @return
     * @throws TransformerException
     */
    public static String convertDocToString(Document doc) throws TransformerException {
        //set up a transformer
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, YES);
        trans.setOutputProperty(OutputKeys.INDENT, YES);

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        return sw.toString();
    }

    /**
     * Write the Document out to a file using nice formatting
     *
     * @param doc The document to save
     * @param localFile The file to write to
     * @return
     */
    public static boolean writeDocumentToFile(Document doc, String localFile) {
        try {
            TransformerFactory transfact = TransformerFactory.newInstance();
            Transformer trans = transfact.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, YES);
            trans.setOutputProperty(OutputKeys.INDENT, YES);
            trans.transform(new DOMSource(doc), new StreamResult(new File(localFile)));
            return true;
        } catch (TransformerConfigurationException ex) {
            LOG.warning(ERROR_WRITING + " " + localFile + " exception: " + ex.getMessage());
            return false;
        } catch (TransformerException ex) {
        	LOG.warning(ERROR_WRITING + " " + localFile + " exception: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Add a child element to a parent element
     *
     * @param doc
     * @param parentElement
     * @param elementName
     * @param elementValue
     */
    public static void appendChild(Document doc, Element parentElement, String elementName, String elementValue) {
        Element child = doc.createElement(elementName);
        Text text = doc.createTextNode(elementValue);
        child.appendChild(text);
        parentElement.appendChild(child);
    }

    /**
     * Wait for a few milliseconds
     *
     * @param milliseconds
     */
    private static void waiting(int milliseconds) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        do {
            t1 = System.currentTimeMillis();
        } while ((t1 - t0) < milliseconds);
    }

    private static DigestedResponse requestWebPage(String url) throws TvDbException {
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("accept", "application/xml");
            final DigestedResponse response = DigestedResponseReader.requestContent(httpClient, httpGet, CHARSET);

            if (response.getStatusCode() == 0) {
                throw new TvDbException(ApiExceptionType.CONNECTION_ERROR, "Error retrieving URL", url);
            } else if (response.getStatusCode() >= HTTP_STATUS_500) {
                throw new TvDbException(ApiExceptionType.HTTP_503_ERROR, response.getContent(), response.getStatusCode(), url);
            } else if (response.getStatusCode() >= HTTP_STATUS_300) {
                throw new TvDbException(ApiExceptionType.HTTP_404_ERROR, response.getContent(), response.getStatusCode(), url);
            }

            return response;
        } catch (IOException ex) {
            throw new TvDbException(ApiExceptionType.CONNECTION_ERROR, "Error retrieving URL", url, ex);
        }

    }
}
