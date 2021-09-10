package com.veetechis.lib.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * An dynamic parsing implementation of the <code>XMLValueBean</code> interface.
 * Automatically parses XML data elements and attributes and stores them in an
 * internal linked-list-map data structure.  This implementation performs no
 * data validation and should be sub-classed and the <code>validate</code>
 * method overridden for cases where data validation is required.
 *
 * @author      pendraconx@gmail.com
 */
public class DynamicXMLValueBean
implements XMLValueBean
{
	
	/**
	 * Creates a new empty instance.
	 */
	public DynamicXMLValueBean()
	{
		super();
	}
	
	/**
	 * Returns the name of this element, or <code>null</code> if not defined.
	 * 
	 * @return					the element name, or null.
	 */
	public String getElementName()
	{
		return elementName;
	}
	
	/**
	 * Returns a map of this element's attributes, or <code>null</code> if no
	 * attributes are defined.
	 * 
	 * @return					the attribute map, or null.
	 */
	public Map getAttributes()
	{
		return attributeMap;
	}
	
	/**
	 * Returns the data contained by this element, or <code>null</code> if the
	 * element has no data.
	 * 
	 * @return					the element data, or null.
	 */
	public String getData()
	{
		return elementData;
	}
	
	/**
	 * Returns a list of child elements contained by this element, or
	 * <code>null</code> if the element has no child elements.
	 */
	public List getChildElements()
	{
		return elementList;
	}
	
	/**
	 * Returns <code>true</code> if this element is an empty element (does not
	 * have data nor child elements).
	 * 
	 * @return					true if this is an empty element.
	 */
	public boolean isEmpty()
	{
		return isEmpty;
	}
	
	/**
	 * Returns <code>true</code> if this instance's <code>endElement</code>
	 * method has been called.
	 * 
	 * @return					true if endElement has been called.
	 */
	public boolean isComplete()
	{
		return isComplete;
	}
	
	/**
	 * Returns <code>true</code> if this element is a data element (is not an
	 * empty element and is not a parent element).
	 * 
	 * @return					true if this element has data.
	 */
	public boolean hasData()
	{
		return hasData;
	}
	
	/**
	 * Returns <code>true</code> if this element has defined attributes.
	 * 
	 * @return					true if this element has attributes.
	 */
	public boolean hasAttributes()
	{
		return hasAttributes;
	}
	
	/**
	 * Returns <code>true</code> if this element contains child elements (is a
	 * parent element).
	 * 
	 * @return					true if this is an empty element.
	 */
	public boolean isParent()
	{
		return isParent;
	}
	
	/**
	 * Locates the first occurrence of the requested element and returns its
	 * storage node.  Returns <code>null</code> if the element is not found.
	 * 
	 * @param  elementName		the element to find.
	 * @return					the storage node or null.
	 */
	public DynamicXMLValueBean findElement( String elementName )
	{
		DynamicXMLValueBean node = null;

		if( elementName.equals( getElementName() ) ) {
			node = this;
		}
		else if( isParent() ) {
			DynamicXMLValueBean child = null;
			for( int i = 0; i < elementList.size(); i++ ) {
				child = (DynamicXMLValueBean) elementList.get( i );
				node = child.findElement( elementName );
				if( node != null ) break;
			}
		}
		
		return node;
	}
	
	/**
	 * Locates the first occurrence of the requested element and returns the
	 * value bound to the specified attribute.  Returns <code>null</code> if
	 * either the element or the attribute is not found
	 * 
	 * @param  elementName		the element to find.
	 * @param  attribute		the attribute value to return.
	 * @return					the attribute value or null.
	 */
	public String getElementAttribute( String elementName, String attribute )
	{
		String val = null;
		
		DynamicXMLValueBean node = findElement( elementName );
		if( node != null && node.hasAttributes() ) {
			val = (String) node.getAttributes().get( attribute );
		}
		
		return val;
	}

	/**
	 * Returns a list of all storage nodes matching the requested element.
	 * Returns an empty list if no matching elements are found.
	 * 
	 * @param  elementName		the elements to find.
	 * @return					the storage list or empty list.
	 */
	public List findMatchingElements( String elementName )
	{
		ArrayList nodeList = new ArrayList();
		
		if( elementName.equals( getElementName() ) ) {
			nodeList.add( this );
		}
		else if( isParent() ) {
			DynamicXMLValueBean child = null;
			for( int i = 0; i < elementList.size(); i++ ) {
				child = (DynamicXMLValueBean) elementList.get( i );
				if( elementName.equals( child.getElementName() ) ) {
					nodeList.addAll(
							child.findMatchingElements( elementName ) );
				}
			}
		}
		
		return nodeList;
	}
	
	/**
	 * Indicates that the named element has been opened.
	 * 
	 * @param  elementName		the element just opened.
	 */
	public void startElement( String elementName )
	{
		if( getElementName() == null ) {
			this.elementName = elementName;
		}
		else {
			if( nextElement == null || nextElement.isComplete() ) {
				nextElement = new DynamicXMLValueBean();
				addChildElement( nextElement );
			}
			nextElement.startElement( elementName );
		}
	}

	/**
	 * Sets the given element attribute in internal storage.
	 * 
	 * @param  element			the element to which the attribute belongs.
	 * @param  attrib			the attribute's name.
	 * @param  value			the value of the attribute.
	 */
	public void setElementAttribute(
		String element, String attrib, String value )
	{
		if( element.equals( getElementName() ) ) {
			addAttribute( attrib, value );
		}
		else {
			nextElement.setElementAttribute( element, attrib, value );
		}
	}
	
	/**
	 * Sets the given element value in internal storage.
	 * 
	 * @param  element			the element for which the value is given.
	 * @param  value			the value of the element.
	 */
	public void setElementData( String element, String value )
	{
		if( element.equals( getElementName() ) ) {
			if( dataBuffer == null ) dataBuffer = new StringBuffer();
			dataBuffer.append( value );
		}
		else {
			nextElement.setElementData( element, value );
		}
	}
	
	/**
	 * Indicates that the named element has been closed.
	 * 
	 * @param  elementName		the element just closed.
	 */
	public void endElement( String elementName )
	{
		if( elementName.equals( getElementName() ) ) {
			if( dataBuffer != null ) setData( dataBuffer.toString() );
			isComplete = true;
		}
		else {
			nextElement.endElement( elementName );
		}
	}

	/**
	 * Indicates that document parsing is complete and that the instance should
	 * perform any data validation checks that it requires but could not yet
	 * perform.
	 * 
	 * Throws an exception if an error in validation occurs.
	 *
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void validate()
	throws XMLValidationException
	{
		// Stub only: ignored
	}
	
	/**
	 * Returns this XML bean in its XML document form including all attributes
	 * and child elements, if any.  This method assumes this element is the root
	 * element of the document.
	 * 
	 * @return					the XML document as a string.
	 */
	public String toString()
	{
		return toString( 0 );
	}

	/**
	 * Returns this XML bean in its XML document form including all attributes
	 * and child elements, if any.  This method assumes this element is a child
	 * element beginning at <code>level</code> sub-elements deep.
	 * 
	 * @param  level			the sub-level of this element.
	 * @return					this XML element as a string.
	 */
	public String toString( int level )
	{
		String indent = getIndentPadding( level );
		
		StringBuffer doc = new StringBuffer( indent ).
			append( "<" ).append( getElementName() );
		if( hasAttributes() ) {
			Iterator it = getAttributes().keySet().iterator();
			String attrib = null;
			String value = null;
			while( it.hasNext() ) {
				attrib = (String) it.next();
				value = (String) getAttributes().get( attrib );
				doc.append( " " ).append( attrib ).
					append( "='" ).append( value ).append( "'" );
			}
		}
		if( isEmpty() ) {
			doc.append( "/>" );
		}
		else {
			doc.append( ">" );
			if( isParent() ) {
				List children = getChildElements();
				DynamicXMLValueBean child = null;
				for( int i = 0; i < children.size(); i++ ) {
					child = (DynamicXMLValueBean) children.get( i );
					doc.append( LINE_SEP ).append( child.toString( level + 1 ) );
				}
				doc.append( LINE_SEP ).append( indent );
			}
			else {
				doc.append( getData() );
			}
			doc.append( "</" ).append( getElementName() ).append( ">" );
		}
		
		return doc.toString();
	}
	
	
	/*
	 * Returns an indent string for the specified indent level.  Returns an
	 * empty string if the indent level is zero (0).
	 */
	private String getIndentPadding( int level )
	{
		StringBuffer pad = new StringBuffer( "" );
		for( int i = 0; i < level; i++ ) {
			pad.append( INDENT );
		}
		return pad.toString();
	}
	
	/*
	 * Adds a new child element to the element list.
	 */
	private void addChildElement( DynamicXMLValueBean child )
	{
		if( elementList == null ) elementList = new ArrayList();
		elementList.add( child );
		isParent = true;
		isEmpty = false;
		hasData = false;
	}

	/*
	 * Adds an attribute to the attribute map.
	 */
	private void addAttribute( String attrib, String value )
	{
		if( attributeMap == null ) attributeMap = new HashMap();
		attributeMap.put( attrib, value );
		hasAttributes = true;
	}
	
	/*
	 * Sets the data of this element.
	 */
	private void setData( String value )
	{
		if( value != null && ! value.equals( "" ) ) {
			elementData = value;
			hasData = true;
			isEmpty = false;
			isParent = false;
		}
	}
	
	
	private String elementName;
	private ArrayList elementList;
	private String elementData;
	private StringBuffer dataBuffer;
	private HashMap attributeMap;
	private DynamicXMLValueBean nextElement;
	private boolean isEmpty = true;
	private boolean hasAttributes;
	private boolean hasData;
	private boolean isParent;
	private boolean isComplete;
	
	private final static String LINE_SEP = System.getProperty("line.separator");
	private final static String INDENT = "\t";

} // end of DynamicXMLValueBean
