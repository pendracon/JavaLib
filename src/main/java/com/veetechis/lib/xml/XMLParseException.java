package com.veetechis.lib.xml;

import org.xml.sax.SAXException;


/**
 * <p>
 * Thrown by <strong>VeeTech IS JavaLib</strong> XML utility classes and other
 * applications to indicate an error in XML document parsing.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public class XMLParseException
extends SAXException
{
	/**
	 * <p>
	 * Creates a new instance of <code>XMLParseException</code> with the given
	 * detail message.
	 * </p>
	 *
	 * @param  msg				the detail message.
	 */
	public XMLParseException( String msg )
	{
		super( msg );
	}
	
	/**
	 * <p>
	 * Creates a new instance of <code>XMLParseException</code> with the given
	 * detail message and with the specified source cause.
	 * </p>
	 *
	 * @param  msg				the detail message.
	 * @param  cause			the exception source.
	 */
	public XMLParseException( String msg, Exception cause )
	{
		super( msg, cause );
	}

	/**
	 * <p>
	 * Creates a new instance of <code>XMLParseException</code> with the
	 * specified source cause.
	 * </p>
	 *
	 * @param  cause			the exception source.
	 */
	public XMLParseException( Exception cause )
	{
		super( cause );
	}

} // End of class: +com.vtis.xml.XMLParseException

