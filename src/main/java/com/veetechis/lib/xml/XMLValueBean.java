package com.veetechis.lib.xml;


/**
 * <p>
 * Implementations of this interface provide a generic means for mapping data in
 * an XML document to a composite data item.  An instance of this interface
 * should be provided to one of the document processors in this package for
 * storage and validation of the document's data.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public interface XMLValueBean
{
	/**
	 * <p>
	 * Indicates that the named element has been opened.
	 * </p>
	 * <p>
	 * Throws an exception if an internal validation error occurs.
	 * </p>
	 *
	 * @param  elementName		the element just opened.
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void startElement( String elementName )
	throws XMLValidationException;

	 /**
	  * <p>
	  * Sets the given element attribute in internal storage.
	  * </p>
	  * <p>
	  * Throws an exception if the attribute does not belong to the specified
	  * element or an internal validation error occurs.
	  * </p>
	  *
	  * @param  element			the element to which the attribute belongs.
	  * @param	attrib			the attribute's name.
	  * @param  value			the value of the attribute.
	  * @throws					XMLParseException
	  *							if a document processing or validation error
	  *							occurs.
	  */
	public void setElementAttribute(
		String element, String attrib, String value )
	throws XMLParseException;
	
	/**
	 * <p>
	 * Sets the given element value in internal storage.
	 * </p>
	 * <p>
	 * Throws an exception if an internal validation error occurs.
	 * </p>
	 *
	 * @param  element			the element for which the value is given.
	 * @param  value			the value of the element.
	 * @throws					XMLValidationException
	 *							if a document validation error occurs.
	 */
	public void setElementData( String element, String value )
	throws XMLValidationException;
	
	/**
	 * <p>
	 * Indicates that the named element has been closed.
	 * </p>
	 * <p>
	 * Throws an exception if an internal validation error occurs.
	 * </p>
	 *
	 * @param  elementName		the element just closed.
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void endElement( String elementName )
	throws XMLValidationException;

	/**
	 * <p>
	 * Indicates that document processing is complete and that the instance
	 * should perform any data validation checks that it requires but could not
	 * yet perform.
	 * </p>
	 * <p>
	 * Throws an exception if an error in validation occurs.
	 * </p>
	 *
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void validate()
	throws XMLValidationException;
	
} // End of interface: +com.vtis.xml.XMLValueBean

