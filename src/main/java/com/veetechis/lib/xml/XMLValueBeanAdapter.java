package com.veetechis.lib.xml;


/**
 * <p>
 * A default implementation of the <code>XMLValueBean</code> interface.  This
 * class is available for implementations of <code>XMLValueBean</code> which do
 * not require all of the methods in that interface.  The method implementations
 * in this class are stubs only and which do nothing.
 * </p>
 *
 * @author      pendraconx@gmail.com
 */
public abstract class XMLValueBeanAdapter
implements XMLValueBean
{
	/**
	 * <p>
	 * Creates a new default instance of <code>XMLValueBeanAdapter</code>.
	 * </p>
	 */
	public XMLValueBeanAdapter()
	{
	}
	
	/**
	 * <p>
	 * Indicates that the named element has been opened.
	 * </p>
	 * <p>
	 * Throws an exception if an internal validation error occurs.
	 * </p>
	 * <p>
	 * <i>Usage note</i>: This default implementation is a stub method only.
	 * Calls to this method are ignored.
	 * </p>
	 *
	 * @param  elementName		the element just opened.
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void startElement( String elementName )
	throws XMLValidationException
	{
		// Stub only: ignored
	}

	/**
	 * <p>
	 * Sets the given element attribute in internal storage.
	 * </p>
	 * <p>
	 * Throws an exception if the attribute does not belong to the specified
	 * element or an internal validation error occurs.
	 * </p>
	 * <p>
	 * <i>Usage note</i>: This default implementation is a stub method only.
	 * Calls to this method are ignored.
	 * </p>
	 *
	 * @param  element			the element to which the attribute belongs.
	 * @param  attrib			the attribute's name.
	 * @param  value			the value of the attribute.
	 * @throws					XMLParseException
	 *							if a document processing or validation error
	 *							occurs.
	 */
	public void setElementAttribute(
		String element, String attrib, String value )
	throws XMLParseException
	{
		// Stub only: ignored
	}
	
	/**
	 * <p>
	 * Sets the given element value in internal storage.
	 * </p>
	 * <p>
	 * Throws an exception if an internal validation error occurs.
	 * </p>
	 * <p>
	 * <i>Usage note</i>: This default implementation is a stub method only.
	 * Calls to this method are ignored.
	 * </p>
	 *
	 * @param  element			the element for which the value is given.
	 * @param  value			the value of the element.
	 * @throws					XMLValidationException
	 *							if a document validation error occurs.
	 */
	public void setElementData( String element, String value )
	throws XMLValidationException
	{
		// Stub only: ignored
	}
	
	/**
	 * <p>
	 * Indicates that the named element has been closed.
	 * </p>
	 * <p>
	 * Throws an exception if an internal validation error occurs.
	 * </p>
	 * <p>
	 * <i>Usage note</i>: This default implementation is a stub method only.
	 * Calls to this method are ignored.
	 * </p>
	 *
	 * @param  elementName		the element just closed.
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void endElement( String elementName )
	throws XMLValidationException
	{
		// Stub only: ignored
	}

	/**
	 * <p>
	 * Indicates that document processing is complete and that the instance
	 * should perform any data validation checks that it requires but could not
	 * yet perform.
	 * </p>
	 * <p>
	 * Throws an exception if an error in validation occurs.
	 * </p>
	 * <p>
	 * <i>Usage note</i>: This default implementation is a stub method only.
	 * Calls to this method are ignored.
	 * </p>
	 *
	 * @throws					XMLValidationException
	 *							if a data validation error occurs.
	 */
	public void validate()
	throws XMLValidationException
	{
		// Stub only: ignored
	}
	
} // End of class: +com.vtis.xml.XMLValueBeanAdapter

