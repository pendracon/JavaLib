package com.veetechis.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.veetechis.lib.io.FileUtils;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Provides methods for accessing various application and system resources.
 *
 * @author		pendraconx@gmail.com
 */
public class Resources
{
	/**
	 * Loads and returns the specified resource bundle from the runtime
	 * class path.
	 *
	 * Throws an exception if the resource can not be found or loaded.
	 *
	 * @param  resourceName		the fully-qualified path name of the resource.
	 * @return					the resource bundle instance.
	 * @throws					ResourceException
	 *							if the resource can not be loaded.
	 */
	public static ResourceBundle loadResourceBundle( String resourceName )
		throws ResourceException
	{
		ResourceBundle res = null;
		
		try
		{
			res = ResourceBundle.getBundle( resourceName, Locale.getDefault(),
					Class.forName( "com.veetech.lib.util.Resources" ).getClassLoader() );
		}
		catch( Exception exc )
		{
			throw new ResourceException(
					"Error while attempting to load resource bundle: " + resourceName, exc );
		}
		
		return res;
	}
	
	/**
	 * Loads and returns the specified properties resource from the runtime
	 * class path.
	 *
	 * Throws an exception if the resource can not be found or loaded.
	 *
	 * @param  resourceName		the fully-qualified path name of the resource.
	 * @return					the resource properties instance.
	 * @throws					ResourceException
	 *							if the resource can not be loaded.
	 */
	public static Properties loadPropertiesResource( String resourceName )
		throws ResourceException
	{
		Properties res = new Properties();
		
		try
		{
			res.load( Class.forName( "com.veetech.lib.util.Resources" ).
					getClassLoader().getResourceAsStream( resourceName ) );
		}
		catch( Exception exc )
		{
			throw new ResourceException(
					"Error while attempting to load properties resource: " + resourceName, exc );
		}
		
		return res;
	}
	
	/**
	 * Loads and returns the specified properties file from the local host file
	 * system.
	 *
	 * Throws an exception if the file can not be found or loaded.
	 *
	 * @param  filename			the fully-qualified file name of the properties
	 *								file.
	 * @return					the properties instance.
	 * @throws					ResourceException
	 *							if the properties can not be loaded.
	 */
	public static Properties loadPropertiesFile( String filename )
		throws ResourceException
	{
		return loadPropertiesFile( new File( filename ) );
	}
	
	/**
	 * Loads and returns the properties file specifed by the given
	 * <code>File</code> instance from the local host file system.
	 *
	 * Throws an exception if the file can not be found or loaded.
	 *
	 * @param  file				the properties file pointer instance.
	 * @return					the properties instance.
	 * @throws					ResourceException
	 *							if the properties can not be loaded.
	 */
	public static Properties loadPropertiesFile( File file )
		throws ResourceException
	{
		String error = null;
		String path = FileUtils.getPathOf( file );
		
		try
		{
			if( ! FileUtils.isReadableFile( file ) )
			{
				error = "File is not readable: " + path;
			}
		}
		catch( FileNotFoundException exc )
		{
			error = "File not found: " + path;
		}
		catch( IOException exc )
		{
			error = "File can not be accessed: " + path;
		}

		if( error != null )
		{
			throw new ResourceException( error );
		}
		
		Properties props = new Properties();		
		try
		{
			props.load( new FileInputStream( file ) );
		}
		catch( IOException exc )
		{
			throw new ResourceException( "Error while reading file: " + path, exc );
		}
		
		return props;
	}
	
} // End of class: +com.vtis.util.Resources
