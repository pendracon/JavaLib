package com.veetechis.lib.sql;


/**
 * Thrown by <strong>VeeTech IS JavaLib</strong> database handling classes to
 * indicate an error in their processing.
 *
 * @author		pendraconx@gmail.com
 */
public class DbHandlerException
	extends Exception
{
	/**
	 * Creates a new default instance of <code>DbHandlerException</code>.
	 */
	public DbHandlerException()
	{
		super();
	}
	
	/**
	 * Creates a new instance of <code>DbHandlerException</code> with the given
	 * detail message.
	 * 
	 * @param  msg				the detail message.
	 */
	public DbHandlerException( String msg )
	{
		super( msg );
	}
	
	/**
	 * Creates a new instance of <code>DbHandlerException</code> with the given
	 * detail message and with the specified source cause.
	 * 
	 * @param  msg				the detail message.
	 * @param  cause			the exception source.
	 */
	public DbHandlerException( String msg, Throwable cause )
	{
		super( msg, cause );
	}

	/**
	 * Creates a new instance of <code>DbHandlerException</code> with the
	 * specified source cause.
	 * 
	 * @param  cause			the exception source.
	 */
	public DbHandlerException( Throwable cause )
	{
		super( cause );
	}

} // End of class: +com.vtis.sql.DbHandlerException
