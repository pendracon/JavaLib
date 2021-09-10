package com.veetechis.lib.xml;


/**
 * <p>
 * Thrown by <strong>VeeTech IS JavaLib</strong> XML utility classes and other
 * applications to indicate an error in XML document validation.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public class XMLValidationException
extends XMLParseException
{
	/**
	 * <p>
	 * Creates a new instance of <code>XMLValidationException</code> with the
	 * given detail message.
	 * </p>
	 *
	 * @param  msg				the detail message.
	 */
	public XMLValidationException( String msg )
	{
		super( msg );
	}
	
	/**
	 * <p>
	 * Creates a new instance of <code>XMLValidationException</code> with the
	 * given detail message and with the specified source cause.
	 * </p>
	 *
	 * @param  msg				the detail message.
	 * @param  cause			the exception source.
	 */
	public XMLValidationException( String msg, Exception cause )
	{
		super( msg, cause );
	}

	/**
	 * <p>
	 * Creates a new instance of <code>XMLValidationException</code> with the
	 * specified source cause.
	 * </p>
	 *
	 * @param  cause			the exception source.
	 */
	public XMLValidationException( Exception cause )
	{
		super( cause );
	}

} // End of class: +com.vtis.xml.XMLValidationException

