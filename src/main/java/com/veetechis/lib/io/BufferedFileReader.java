package com.veetechis.lib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * <p>
 * Provides buffered access to a file on the local file system with methods to
 * read the content of the file into various data types.
 * </p>
 *
 * @author      pendraconx@gmail.com
 */
public class BufferedFileReader
{
	/**
	 * The file being accessed.
	 */
	protected File file;
	
	
	/**
	 * <p>
	 * Creates a new instance of <code>BufferedFileReader</code> for the
	 * specified file.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the specified file is not found or can not be
	 * accessed for other reasons related to the local file system (<i>i.e.</i>
	 * "permission denied", file names a directory, etc.).
	 * </p>
	 *
	 * @param  file				the file to access.
	 * @throws					java.io.FileNotFoundException
	 *							if the file does not exist or is not accessible.
	 */
	public BufferedFileReader( File file )
		throws FileNotFoundException
	{
		if( FileUtils.isReadableFile( file ) )
		{
			this.file = file;
		}
	}
	
	/**
	 * <p>
	 * Returns the file referenced by the instance.
	 * </p>
	 *
	 * @return					the referenced file.
	 */
	public File getFile()
	{
		return file;
	}
	
	/**
	 * <p>
	 * Returns the line of text starting at the current index within the file
	 * referenced by this instance.  Returns <code>null</code> if the end of
	 * the file has been reached.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an I/O error occurs.
	 * </p>
	 *
	 * @return					the line of text.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public String readLine()
		throws IOException
	{
		if( reader == null )
		{
			reader = new BufferedReader( new FileReader( file ) );
		}
		
		return reader.readLine();
	}
	
	/**
	 * <p>
	 * Reads and returns the next byte from the current position in the
	 * referenced file and advances the file's position pointer.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an I/O error occurs.
	 * </p>
	 *
	 * @return					the next byte of data.
	 * @throws					java.io.IOException
	 *							if a read error occurs.
	 */
	public int read()
		throws IOException
	{
		return getFileInputStream().read();
	}
	
	/**
	 * <p>
	 * Deserializes the file referenced by this instance and returns it in
	 * object form.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an error occurs while reading the file, of if the
	 * type of object the file is in deserialized form is not found on the local
	 * class path.
	 * </p>
	 *
	 * @return					the file contents in object form.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 * @throws					java.lang.ClassNotFoundException
	 *							if the class type of the deserialized object is
	 *								not on the class path.
	 */
	public Object toObject()
		throws IOException, ClassNotFoundException
	{
		ois = new ObjectInputStream( getFileInputStream() );
		return ois.readObject();
	}

	/**
	 * <p>
	 * Closes the reader.
	 * </p>
	 *
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public void close()
		throws IOException
	{
		if( reader != null )
		{
			reader.close();
			reader = null;
		}
		
		if( fis != null )
		{
			fis.close();
			fis = null;
		}
		
		if( ois != null )
		{
			ois.close();
			ois = null;
		}
	}
	
	/*
	 * <p>
	 * Returns the file input stream for the referenced file.
	 * </p>
	 */
	private FileInputStream getFileInputStream()
		throws IOException
	{
		if( fis == null ) fis = new FileInputStream( file );
		return fis;
	}
	

	private FileInputStream fis;
	private ObjectInputStream ois;
	private BufferedReader reader;
	
} // End of class: +com.vtis.io.BufferedFileReader
