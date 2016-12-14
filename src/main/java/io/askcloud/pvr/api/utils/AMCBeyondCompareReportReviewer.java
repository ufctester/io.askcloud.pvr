/**
 * 
 */
package io.askcloud.pvr.api.utils;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import io.askcloud.pvr.api.HTPC;

/**
 * @author ufctester
 * 
 * <filecomp status="ltonly"> (Means it is a new file that was downloaded) This is what we want for all the files we are looking for
 * <filecomp status="same"> (the file is exactly the same - should never happen)
 * <filecomp status="rtonly"> - Means it is a file which already exists in the destination only (we don't care about these)
 * 
 * 			<filecomp status="rtonly">
 *				<rt>
 *					<name>Better Call Saul - S01E03 - Nacho.mp4</name>
 *					<size>168,350,496</size>
 *					<modified>11/4/2016 8:07:14 PM</modified>
 *				</rt>
 *			</filecomp>
 *			<filecomp status="same">
 *				<lt>
 *					<name>Better Call Saul - S01E04 - Hero.mp4</name>
 *					<size>172,152,275</size>
 *					<modified>11/4/2016 8:07:14 PM</modified>
 *				</lt>
 *				<rt>
 *					<name>Better Call Saul - S01E04 - Hero.mp4</name>
 *					<size>172,152,275</size>
 *					<modified>11/4/2016 8:07:14 PM</modified>
 *				</rt>
 *			</filecomp>
 *			
 *			<filecomp status="ltonly">
 *				<lt>
 *					<name>Better Call Saul - S01E06 - Five-O.mp4</name>
 *					<size>134,586,505</size>
 *					<modified>11/4/2016 8:07:14 PM</modified>
 *				</lt>
 *			</filecomp>
 *						
 *
 */
/**
 * @author tattooman
 *
 */
public class AMCBeyondCompareReportReviewer extends DOMWalker {

	private static final String CLASS_NAME = HTPC.class.getName();
	public static final Logger LOG = HTPC.LOG;
	private boolean hasConflicts=false;
	
	
	/**
	 * @param document
	 */
	public AMCBeyondCompareReportReviewer(File file) {
		super(null);
		
		LOG.entering(CLASS_NAME, "AMCBeyondCompareReportReviewer");
		try {
			setfDocument(HTPCUtils.loadXMLFile(file));
		}
		catch (Exception e) {
			LOG.severe("Error parsing beyond compare report file: " + file.toString());
		}
		LOG.exiting(CLASS_NAME, "AMCBeyondCompareReportReviewer");
	}
	
	/*
	 * <filecomp status="same">
	 * 		<lt>
	 * 			<name>Better Call Saul - S01E04 - Hero.mp4</name>
	 * 			<size>172,152,275</size>
	 * 			<modified>11/4/2016 8:07:14 PM</modified>
	 * 		</lt>
	 * 		<rt>
	 * 			<name>Better Call Saul - S01E04 - Hero.mp4</name>
	 * 			<size>172,152,275</size>
	 * 			<modified>11/4/2016 8:07:14 PM</modified>
	 * 		</rt>
	 * 	</filecomp>
	 * 	
	 * 	<filecomp status="ltonly">
	 * 		<lt>
	 * 			<name>Better Call Saul - S01E06 - Five-O.mp4</name>
	 * 			<size>134,586,505</size>
	 * 			<modified>11/4/2016 8:07:14 PM</modified>
	 * 			</lt>
	 * 	</filecomp>
	 * @see io.askcloud.pvr.api.utils.DOMWalker#handleElementNode(org.w3c.dom.Element)
	 */
	
	@Override
	protected void handleElementNode(Element element) {
		LOG.entering(CLASS_NAME, "handleElementNode");
		LOG.fine("element: " + element);
		
		//we care about if there are any <filecomp status="ltonly"> or <filecomp status="same">
		if("filecomp".equals(element.getTagName()))
		{
			//we want to get the parent tv show season
			//Element parent = (Element)element.getParentNode();
			String statusValue = element.getAttribute("status");
			if("same".equals(statusValue))
			{
				handleSame(element);
			}
			else if("ltonly".equals(statusValue))
			{
				handleNewFile(element);
			}			
		}

		super.handleElementNode(element);
		LOG.exiting(CLASS_NAME, "handleElementNode");
	}
	
	
	/**
	 * <filecomp status="same">
	 * 		<lt>
	 * 			<name>Better Call Saul - S01E04 - Hero.mp4</name>
	 * 			<size>172,152,275</size>
	 * 			<modified>11/4/2016 8:07:14 PM</modified>
	 * 		</lt>
	 * 		<rt>
	 * 			<name>Better Call Saul - S01E04 - Hero.mp4</name>
	 * 			<size>172,152,275</size>
	 * 			<modified>11/4/2016 8:07:14 PM</modified>
	 * 		</rt>
	 * 	</filecomp>
	 * @param filecomp
	 */
	private void handleSame(Element filecomp)
	{
		LOG.severe("Detected a duplicate download.  File exists in both source and target");
		hasConflicts=true;
	}
	
	public boolean containsConflicts() {
		return hasConflicts;
	}
	
	/**
	 * 	<filecomp status="ltonly">
	 * 		<lt>
	 * 			<name>Better Call Saul - S01E06 - Five-O.mp4</name>
	 * 			<size>134,586,505</size>
	 * 			<modified>11/4/2016 8:07:14 PM</modified>
	 * 			</lt>
	 * 	</filecomp>
	 * @param filecomp
	 */
	private void handleNewFile(Element filecomp)
	{
		NodeList nl = filecomp.getElementsByTagName("lt");
		//there should be 1
		if(nl.getLength() == 1)
		{
			Element lt = (Element)nl.item(0);
			String name = getChildElementValueByName(lt, "name");
			String size = getChildElementValueByName(lt, "size");
			String modified = getChildElementValueByName(lt, "modified");
			
			String readable = modified + " B";
			try {
				readable = HTPCUtils.calculateProperFileSizeFromBytes(size);
				LOG.info(name + " size: " + readable);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			LOG.info("name: " + name + " size: " + readable + " modified: " + modified);
		}
		
	}
	
	/**
	 * 	<lt>
	 * 		<name>Better Call Saul - S01E06 - Five-O.mp4</name>
	 * 		<size>134,586,505</size>
	 * 		<modified>11/4/2016 8:07:14 PM</modified>
	 * 	</lt>
	 * @param singlestatus
	 * @return
	 */
	private String getChildElementValueByName(Element singlestatus,String childElementName)
	{
		String name = "";
		NodeList nl = singlestatus.getElementsByTagName(childElementName);
		//there should be 1
		if(nl.getLength() == 1)
		{
			Element element = (Element)nl.item(0);
			name = element.getFirstChild().getNodeValue();
		}	
		
		return name;
	}
	
	@Override
	protected void handleTextNode(Text textNode) {
		LOG.entering(CLASS_NAME, "handleTextNode");
		LOG.fine("textNode: " + textNode);
		//System.out.println("textNode: " + textNode);
		super.handleTextNode(textNode);
		LOG.exiting(CLASS_NAME, "handleTextNode");
	}
	
	

}
