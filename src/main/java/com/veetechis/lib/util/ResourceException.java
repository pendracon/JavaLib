package com.veetechis.lib.util;


/**
 * Thrown by the <code>Resources</code> class to indicate an error in resource
 * access.
 *
 * @author		pendraconx@gmail.com
 */
public class ResourceException
	extends Exception
{
	
	/**
	 * Creates a new instance of <code>ResourceException</code> without
	 * a detail message.
	 */
	public ResourceException()
	{
	}
	
	/**
	 * Creates an instance of <code>ResourceException</code> with the
	 * specified detail message.
	 *
	 * @param  msg				the detail message.
	 */
	public ResourceException( String msg )
	{
		super( msg );
	}
	
	/**
	 * Creates an instance of <code>ResourceException</code> with the
	 * specified detail message and parent cause.
	 *
	 * @param  msg				the detail message.
	 * @param  cause			the parent cause.
	 */
	public ResourceException( String msg, Throwable cause )
	{
		super( msg, cause );
	}
	
	/**
	 * Creates an instance of <code>ResourceException</code> with the
	 * given parent cause.
	 *
	 * @param  cause			the parent cause.
	 */
	public ResourceException( Throwable cause )
	{
		super( cause );
	}
	
} // End of exception: +com.vtis.util.ResourceException
