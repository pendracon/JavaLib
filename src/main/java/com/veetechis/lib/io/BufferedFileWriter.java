package com.veetechis.lib.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;


/**
 * <p>
 * Provides methods for buffered writing of various data types to a file on the
 * local file system.
 * </p>
 *
 * @author      pendraconx@gmail.com
 */
public class BufferedFileWriter
{
	
	/**
	 * <p>
	 * Creates a new instance of <code>BufferedFileWriter</code> for writing to
	 * the specified file.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the file exists but can not be written, or if the
	 * file is not found and can not be created (<i>i.e.</i> no parent write
	 * permission, parent names a file, etc).
	 * </p>
	 *
	 * @param  file				the file to access.
	 * @throws					java.io.FileNotFoundException
	 *							if the file does not exist and can not be
	 *								created.
	 * @throws					java.io.IOException
	 *							if the file exists and can not be written, or
	 *								is a directory.
	 */
	public BufferedFileWriter( File file )
		throws IOException
	{
		if( file.isDirectory() )
		{
			throw new IOException( "Path exists and names a directory: " + FileUtils.getPathOf( file ) );
		}
		
		boolean valid = false;
		
		try
		{
			valid = FileUtils.isWriteableFile( file );
		}
		catch( FileNotFoundException exc )
		{
			valid = FileUtils.isCreateablePath( file );
			createParent = true;
		}
		
		if( ! valid )
		{
			throw new IOException( "No write permission for file: " + FileUtils.getPathOf( file ) );
		}

		this.file = file;
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
	 * Appends the given text to the file referenced by this instance.  If the
	 * file does not exist it is created.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an I/O error occurs while writing to the file.
	 * </p>
	 *
	 * @param  text				the text to append.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public void append( String text )
		throws IOException
	{
		writer = getWriter( true );
		
		writer.write( text, 0, text.length() );
		writer.flush();
	}
	
	/**
	 * <p>
	 * Writes the given text to the file referenced by this instance starting at
	 * the beginning position in the file (previous contents are destroyed).  If
	 * the file does not exist it is created.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an I/O error occurs while writing to the file.
	 * </p>
	 *
	 * @param  text				the text to write.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public void write( String text )
		throws IOException
	{
		writer = getWriter( false );

		writer.write( text, 0, text.length() );
		writer.flush();
	}
	
	/**
	 * <p>
	 * Writes the given text to the file referenced by this instance as a new
	 * line of character date starting at the current position in the file.  If
	 * the file does not exist it is created and the text is written at the
	 * beginning of the file.  The text is terminated with the line separator
	 * sequence for the the host platform.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an I/O error occurs while writing to the file.
	 * </p>
	 *
	 * @param  text				the text to write with line termination.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public void writeLine( String text )
		throws IOException
	{
		write( text );
		getWriter( false ).write( LINE_SEP.charAt( 0 ) );
	}

	/**
	 * <p>
	 * Writes the content of the given source reader as a stream of bytes
	 * starting at the current position in the referenced file.  If
	 * <code>append</code> equates to <code>false</code> and the referenced file
	 * already exists it is overwritten.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an I/O error occurs.
	 * </p>
	 *
	 * @param  source			the source reader.
	 * @param  append			appends to the end of the file if true,
	 *								overwrites it if false.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public void fromReader( BufferedFileReader source, boolean append )
		throws IOException
	{
		fos = new FileOutputStream( file, append );
		int datab = -1;
		while( (datab = source.read()) != -1 )
		{
			fos.write( datab );
		}
		fos.flush();
	}
	
	/**
	 * <p>
	 * Serializes the given object to the file referenced by this instance.  If
	 * the referenced file exists it is overwritten.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the argument is not serializable or if an I/O
	 * error occurs while writing to the file.
	 * </p>
	 *
	 * @param  obj				the object to serialize.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 * @throws					java.io.NotSerializableException
	 *							if the object does not implement
	 *								java.io.Serializable.
	 */
	public void fromObject( Object obj )
		throws IOException, NotSerializableException
	{
		if( file.exists() )
		{
			file.delete();
		}
		else
		{
			checkParentPath();
		}
		
		oos = new ObjectOutputStream( new FileOutputStream( file ) );
		oos.writeObject( obj );
		oos.flush();
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
		if( writer != null )
		{
			writer.close();
			writer = null;
		}
		
		if( fos != null )
		{
			fos.close();
			fos = null;
		}
		
		if( oos != null )
		{
			oos.close();
			oos = null;
		}

		file = null;
		appending = null;
	}
	
	/*
	 * Creates the parent path hierarchy if it does not already exist.
	 */
	private void checkParentPath()
	{
		if( createParent ) file.getParentFile().mkdirs();
	}
	
	/*
	 * Returns a FileWriter instance associated with the instance.
	 */
	private FileWriter getWriter( boolean append )
		throws IOException
	{
		checkParentPath();
		if( writer == null ) writer = new FileWriter( file, append );
		
		return writer;
	}

	
	private File file;					// the file being accessed
	private FileWriter writer;
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	private boolean createParent;
	private Boolean appending;

	private final static String LINE_SEP = System.getProperty( "line.separator" );

} // End of class: +com.vtis.io.BufferedFileWriter
