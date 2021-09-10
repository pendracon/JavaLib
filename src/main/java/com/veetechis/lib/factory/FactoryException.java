package com.veetechis.lib.factory;


/**
 * Thrown by objects which implement the <i>factory design pattern</i> to
 * indicate an error has occurred while fulfilling a creation request.
 *
 * @author		pendraconx@gmail.com
 */
public class FactoryException
extends Exception
{
	
	/**
	 * Creates a new instance of <code>FactoryException</code> without a detail
	 * message.
	 */
	public FactoryException()
	{
	}
	
	/**
	 * Creates an instance of <code>FactoryException</code> with the specified
	 * detail message.
	 *
	 * @param  msg				the detail message.
	 */
	public FactoryException( String msg )
	{
		super( msg );
	}
	
	/**
	 * Creates an instance of <code>FactoryException</code> with the specified
	 * detail message and parent cause.
	 *
	 * @param  msg				the detail message.
	 * @param  cause			the parent cause.
	 */
	public FactoryException( String msg, Throwable cause )
	{
		super( msg, cause );
	}
	
	/**
	 * Creates an instance of <code>FactoryException</code> with the given
	 * parent cause.
	 *
	 * @param  cause			the parent cause.
	 */
	public FactoryException( Throwable cause )
	{
		super( cause );
	}
	
} // end of FactoryException
