package com.veetechis.lib.io;

import java.io.FileNotFoundException;


/**
 * <p>
 * Thrown when a special class of <code>FileNotFoundException</code> is needed
 * when relating specifically to directories.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public class DirNotFoundException
	extends FileNotFoundException
{
	
	/**
	 * <p>
	 * Creates a new instance of <code>DirNotFoundException</code> without
	 * detail message.
	 * </p>
	 */
	public DirNotFoundException()
	{
	}
	
	/**
	 * <p>
	 * Creates an instance of <code>DirNotFoundException</code> with the
	 * specified detail message.
	 * </p>
	 *
	 * @param  msg				the detail message.
	 */
	public DirNotFoundException( String msg )
	{
		super( msg );
	}
	
} // End of exception: +com.vtis.io.DirNotFoundException
