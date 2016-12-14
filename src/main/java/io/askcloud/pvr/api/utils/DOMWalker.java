package io.askcloud.pvr.api.utils; 

import org.w3c.dom.*;


/**
 * @author ufctester
 *
 */
public class DOMWalker extends Object
{

	private Document fDocument;
	  
	/**
	 * DOMWalker constructor
	 */
	public DOMWalker(Document document)
	{
		super();
		fDocument = document;	
	}
	
	public void setfDocument(Document fDocument) {
		this.fDocument = fDocument;
	}

	/**
	 * This method will walk the DOM model
	 */
	public void walkDocument()
	{
		walkNode(fDocument);
	}
	/**
	 * This method walks recursivly down the nodes
	 * @param node
	 */
	final private void walkNode(Node node) 
	{
		// is there anything to do?
		if (node == null) 
		{
			return;
		}
	
		int type = node.getNodeType();
		switch (type) 
		{
			// print document
			case Node.DOCUMENT_NODE: 
			{
				walkNode(((Document)node).getDocumentElement());
				break;
			}
		
				// print element with attributes
			case Node.ELEMENT_NODE: 
			{
				Element element = (Element)node;
				handleElementNode(element);
				
				NodeList children = node.getChildNodes();
				if (children != null) 
				{
					int len = children.getLength();
					for (int i = 0; i < len; i++) 
					{
						walkNode(children.item(i));
					}
				}
	
				break;
			}
		
			// handle entity reference nodes
			case Node.ENTITY_REFERENCE_NODE: 
			{
				NodeList children = node.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++) 
					{
						walkNode(children.item(i));
					}
				}
				break;
			}
			//handle text node
			case Node.TEXT_NODE:
			{
				Text text = (Text)node;
				handleTextNode(text);
			}
		}	
	} // traverse
	
	/**
	 * @param elementNode
	 */
	protected void handleElementNode(Element element)
	{

	}

	/**
	 * @param textNode
	 */
	protected void handleTextNode(Text textNode)
	{

	}
}
