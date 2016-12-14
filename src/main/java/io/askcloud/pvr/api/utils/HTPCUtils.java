package io.askcloud.pvr.api.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.askcloud.pvr.api.HTPC;


/**
 * @author ufctester
 *
 */
public class HTPCUtils
{
	final public static String UTF_8 = "UTF-8"; //$NON-NLS-1$
	
	public HTPCUtils()
	{
		super();
	}
	
	static private String formatFileSize(long size) {
        String hrSize = null;
        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec1 = new DecimalFormat("0.00");
        DecimalFormat dec2 = new DecimalFormat("0");
        if (t>1) {
            hrSize = isDouble(t) ? dec1.format(t).concat(" TB") : dec2.format(t).concat(" TB");
        } else if (g>1) {
            hrSize = isDouble(g) ? dec1.format(g).concat(" GB") : dec2.format(g).concat(" GB");
        } else if (m>1) {
            hrSize = isDouble(m) ? dec1.format(m).concat(" MB") : dec2.format(m).concat(" MB");
        } else if (k>1) {
            hrSize = isDouble(k) ? dec1.format(k).concat(" KB") : dec2.format(k).concat(" KB");
        } else {
            hrSize = isDouble(b) ? dec1.format(b).concat(" B") : dec2.format(b).concat(" B");
        }
        return hrSize;
    }
	
	static private boolean isDouble(double value) {
        if (value % 1 == 0) {
            HTPC.LOG.fine("value is " + value + " and is not double");
            return false;
        } else {
        	HTPC.LOG.fine("value is " + value + " and is double");
            return true;
        }
    }	
	
	public static String calculateProperFileSizeFromBytes(String bytes){
		String bytesString = bytes.replace(",", "");
		Integer integerObj = Integer.valueOf(bytesString);
		Long passIn = Long.valueOf(integerObj);
		return formatFileSize(passIn);
		//double passIn = 1.0 * bytesObj.intValue();
		//return calculateProperFileSize(passIn);	
	}
	/**
	 * @param cloneParent
	 * @param source
	 */
	public static void cloneElement(Element cloneParent,Element source)
	{
		if((cloneParent == null) || (source == null))
		{
			return;
		}
		
		//clone attributes
		NamedNodeMap map = source.getAttributes();
		if(map != null)
		{
			for (int i = 0; i < map.getLength(); i++) {
				Attr attr = (Attr)map.item(i);
				cloneParent.setAttribute(attr.getName(), attr.getValue());				
			}
		}
		
		NodeList children = source.getChildNodes();
		if(children != null)
		{
			for (int i = 0; i < children.getLength(); i++) {
				Node sourceChild = (Node)children.item(i);
				if(sourceChild instanceof Element)
				{
					Element sourceChildElement = (Element)sourceChild;
					
					Element cloneElement = cloneParent.getOwnerDocument().createElement(sourceChildElement.getTagName());
					cloneParent.appendChild(cloneElement);
					cloneElement(cloneElement, sourceChildElement);
				}		
			}
		}
	}
	
	/**
	 * @param sourceMessage
	 * @return
	 */
	static public String formatXML(String sourceMessage)
	{
		Document doc = HTPCUtils.toXMLDocument(sourceMessage);
		return HTPCUtils.serializeToString(doc);
	}

	/**
	 * @param element
	 * @return
	 */
	static public String serializeToString(Node element)
	{
		try
		{
			if (element == null)
			{
				throw new IllegalArgumentException();
			}

			StringWriter os = new StringWriter();

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			// Unless a width is set, there will be only line breaks but no
			// indentation.
			// The IBM JDK and the Sun JDK don't agree on the property name,
			// so we set them both.
			//
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8);

			transformer.transform(new DOMSource(element), new StreamResult(os));

			return os.getBuffer().toString();
		}
		catch (TransformerException exception)
		{

		}
		catch (Exception exe)
		{

		}
		return null;
	}

	/**
	 * This method will load the given <code>InputStream</code> corresponding
	 * to an XML File
	 * 
	 * @param inputStream
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	static public Document loadXMLFile(InputStream inputStream) throws ParserConfigurationException, SAXException,
			IOException
	{
		Document document = null;

		try
		{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setValidating(false);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(inputStream);
		}
		finally
		{
			try
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
			catch (Exception exe)
			{
			}
		}

		return document;
	}

	/**
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	static public Document createDocument() throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(false);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		return document;
	}
	
	/**
	 * @param document
	 * @return
	 */
	static public Document toXMLDocument(String document){
		try
		{
			ByteArrayInputStream is = new ByteArrayInputStream(document.getBytes());
			return loadXMLFile(is);
		}
		catch (Exception e)
		{
			//do nothing
		}
		return null;
	}
	
	
	/**
	 * This method will load the given <code>File</code> XML File 
	 * @param targetFile
	 * @return Document
	 * @throws ParserConfigurationException,SAXException,IOException
	 */
	static public Document loadXMLFile(File targetFile) throws ParserConfigurationException,SAXException,IOException
	{
		Document document = null;
		
		InputStream inputStream = null;
		try
		{	
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setValidating(false);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(targetFile);			    
		}
		finally
		{
			try
			{
				if(inputStream != null)
					inputStream.close();
			}
			catch(Exception exe)
			{
				exe.printStackTrace();
				//Do nothing input stream must already be closed
			}
		}		
		
		return document;
	}	
	
	
//	/**
//	 * Perform an XPath query on a given BPEL file and return the results.
//	 * 
//	 * Sample usage: NodeList list = executeXpathQuery( file, new SimpleNamespaceContext("bpws", "http://schemas.xmlsoap.org/ws/2004/03/business-process/"), "//bpws:correlationSet//Jon" );
//	 * @throws IOException 
//	 * @throws SAXException 
//	 * @throws ParserConfigurationException 
//	 * @throws XPathExpressionException 
//	 * 
//	 * @return A NodeList containing the results
//	 * 
//	 */
//	static public NodeList executeXPathQuery( Document doc, NamespaceContext context, String query ) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
//		if(context == null)
//		{
//			context = new SimpleNamespaceContext();
//		}
//		
//		XPathFactory factory=XPathFactory.newInstance();
//		XPath xPath=factory.newXPath();
//
//		xPath.setNamespaceContext( context );
//		NodeList nl = (NodeList)xPath.evaluate(query, doc.getDocumentElement(), XPathConstants.NODESET  );
//		return nl;
//	}
	
	
	/**
	 * This method serializes the stored cache XML File
	 */
	static public void saveXMLFile(File xmlFile,Document document) throws Exception
	{
		try
		{
			FileOutputStream stream = null;
			OutputStreamWriter writer = null;
		
			try
			{
				if(xmlFile != null)
				{			
					stream = new FileOutputStream(xmlFile);
//					OutputFormat format  = new OutputFormat( document );   //Serialize DOM
//					//format.setLineWidth(80);
//					format.setIndenting(true);
//					format.setIndent(2);
//					//format.setPreserveSpace(false);
//							
//					stream = new FileOutputStream(xmlFile);
//					writer = new OutputStreamWriter(stream, "utf-8"); //$NON-NLS-1$	
//									
//					Serializer serializer = SerializerFactory.getSerializerFactory("xml").makeSerializer(writer, format);//$NON-NLS-1$
//					serializer.asDOMSerializer().serialize(document);		
//					
					TransformerFactory tf = TransformerFactory.newInstance();

					try {
						tf.setAttribute("indent-number", new Integer(4)); //$NON-NLS-1$
					} catch (IllegalArgumentException illegalArgumentException) {
						// do nothing;
					}

					Transformer transformer = tf.newTransformer();

					transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
					transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
					
				      // Be sure to use actual encoding of the transformer which might be non-null even if encoding started as null.
				      writer = new OutputStreamWriter(stream, "UTF-8");
				      transformer.transform(new DOMSource(document), new StreamResult(writer));
				}
			} 	
			finally
			{
				try
				{
					if(stream != null)
						stream.close();
				
					if(writer != null)
						writer.close();
				}
				catch(Exception exe)
				{
					//We could not close the streams -- DO NOTHING
				}
			}		
		} 
		catch ( Exception exe) 
		{
			throw exe;
		}	
	}
	
}
