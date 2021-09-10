package com.veetechis.lib.util;


/**
 * Thrown by classes of the <i>VeeTech IS JavaLib</i> package to indicate a
 * <code>key/value</code> processing error.
 *
 * @author		pendraconx@gmail.com
 */
public class KeyValueException
	extends Exception
{
	/**
	 * Creates a new default instance of <code>KeyValueException</code>.
	 */
	public KeyValueException()
	{
		super();
	}

	/**
	 * Creates a new instance of <code>KeyValueException</code> with the
	 * given detail message.
	 *
	 * @param  msg				the detail message.
	 */
	public KeyValueException( String msg )
	{
		super( msg );
	}

	/**
	 * Creates a new instance of <code>KeyValueException</code> with the
	 * given detail message and with the specified source cause.
	 *
	 * @param  msg				the detail message.
	 * @param  cause			the exception source.
	 */
	public KeyValueException( String msg, Throwable cause )
	{
		super( msg, cause );
	}

	/**
	 * Creates a new instance of <code>KeyValueException</code> with the
	 * specified source cause.
	 *
	 * @param  cause			the exception source.
	 */
	public KeyValueException( Throwable cause )
	{
		super( cause );
	}

} // End of class: +com.vtis.util.KeyValueException
