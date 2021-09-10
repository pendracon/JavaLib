package com.veetechis.lib.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * <p>
 * Provides a collection of utilities for finding and accessing files on the
 * local file system.
 * </p>
 *
 * @author      pendraconx@gmail.com
 */
public class FileUtils
{
	private final static String ALREADY_EXISTS_MSG		= "Path name already exists: ";
	private final static String DOES_NOT_EXIST_MSG		= "Path name does not exist: ";
	private final static String IS_NOT_READABLE_MSG		= "Path name can not be read: ";
	private final static String IS_NOT_WRITEABLE_MSG	= "Path name can not be written: ";
	private final static String IS_NOT_CREATEABLE_MSG	= "Path name can not be created: ";
	private final static String	IS_A_DIRECTORY_MSG		= "Path names a directory: ";
	private final static String IS_NOT_A_DIRECTORY_MSG	= "Path does not name a directory: ";

	
	/**
	 * <p>
	 * Returns the path of the specified file.  The canonical path of the file
	 * is returned if possible.  Otherwise, returns the absolute path of the
	 * file.
	 * </p>
	 *
	 * @param  file				the file whose path to return.
	 * @return					the file's path name.
	 */
	public static String getPathOf( File file )
	{
		String pathName = null;
		
		try
		{
			pathName = file.getCanonicalPath();
		}
		catch( IOException exc )
		{
			pathName += file.getAbsolutePath();
		}
		
		return pathName;
	}
	
	/**
	 * <p>
	 * Makes a copy of the source file as the destination file.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the source file does not exist or can not be read
	 * or is a directory, if it can not be copied to the destination parent
	 * location, or if an I/O error occurs.
	 * </p>
	 *
	 * @param  sourceFile		the file to copy.
	 * @param  destFile			the copy's destination path name.
	 * @throws					FileNotFoundException
	 *							if the source file can not be found, is a
	 *								directory, or can not be accessed.
	 * @throws					DirNotFoundException
	 *							if the destination parent location is not a
	 *								directory or can not be accessed.
	 * @throws					java.io.IOException
	 *							if an I/O error occurs.
	 */
	public static void copyFile( File sourceFile, File destFile )
		throws DirNotFoundException, FileNotFoundException, IOException
	{
		BufferedFileReader src = new BufferedFileReader( sourceFile );
		BufferedFileWriter dst = new BufferedFileWriter( destFile );
		dst.fromReader( src, false );
	}
	
	/**
	 * <p>
	 * Searches the specified file system location (directory) for a file whose
	 * name matches the given file name, including case.  Returns the file if
	 * found, or <code>null</code> if no matching file exists.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the specified search location can not be accessed
	 * or is not a directory, if a matching file is found but can not be
	 * read, or if a directory matching the given file name is found
	 * instead.
	 * </p>
	 *
	 * @param  dir				the directory to search within.
	 * @param  name				the file name to match.
	 * @throws					DirNotFoundException
	 *							if the search location is not a directory or
	 *								can not be accessed.
	 * @throws					java.io.FileNotFoundException
	 *							if a match is found	but is a directory, or is
	 *								inaccessible.
	 */
	public static File findFile( File dir, String name )
		throws FileNotFoundException
	{
		File file = null;
		
		if( isReadableDirectory( dir ) )
		{
			file = new File( dir, name );
			if( ! isReadableFile( file ) ) file = null;
		}
		else
		{
			throw new DirNotFoundException(	IS_NOT_READABLE_MSG + getPathOf( dir ) );
		}
		
		return file;
	}

	/**
	 * <p>
	 * Searches the specified file system location (directory) for all readable
	 * files whose name match the given plain text search pattern, including
	 * case, based on the specified name matching type.  Returns an array of
	 * matching files, or <code>null</code> if no matching files exist.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the specified search location can not be accessed
	 * or is not a directory.
	 * </p>
	 *
	 * @param  dir				the directory to search within.
	 * @param  pattern			the search pattern to match file names.
	 * @param  searchBy			the name matching type, per the method
	 *								FileSearchFilter.setSearchBy.
	 * @throws					DirNotFoundException
	 *							if the search location is not a directory or
	 *								can not be accessed.
	 * @see						FileSearchFilter#setSearchBy
	 */
	public static File[] findFiles( File dir, String pattern, FileSearchFilter.MatchType searchBy )
		throws FileNotFoundException
	{
		return findMatching( dir, pattern, FileSearchFilter.FileType.FILES_ONLY,
				searchBy, FileSearchFilter.CaseType.WITH_CASE );
	}

	/**
	 * <p>
	 * Searches the specified file system location (directory) for all readable
	 * files and directories whose name match the given plain text search
	 * pattern, based on the specified matching criteria.  Returns an array of
	 * matching files, or <code>null</code> if no matching files exist.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the specified search location can not be accessed
	 * or is not a directory.
	 * </p>
	 *
	 * @param  dir				the directory to search within.
	 * @param  pattern			the search pattern to match file names.
	 * @param  searchType		the file type to match, per the method
	 *								FileSearchFilter.setSearchType.
	 * @param  searchBy			the name matching type, per the method
	 *								FileSearchFilter.setSearchBy.
	 * @param  matchCase		if set to true, uses case relevance	for name
	 *								matching, per the method
	 *								FileSearchFilter.setCaseRelevance.
	 * @throws					DirNotFoundException
	 *							if the search location is not a directory or
	 *								can not be accessed.
	 * @see						FileSearchFilter#setSearchType
	 * @see						FileSearchFilter#setSearchBy
	 * @see						FileSearchFilter#setCaseRelevance
	 */
	public static File[] findMatching( File dir, String pattern,
			FileSearchFilter.FileType searchType,
			FileSearchFilter.MatchType searchBy,
			FileSearchFilter.CaseType matchCase )
		throws FileNotFoundException
	{
		File[] files = null;
		ArrayList<File> fileList = null;
		
		if( ! isReadableDirectory( dir ) )
			throw new DirNotFoundException( IS_NOT_READABLE_MSG + getPathOf( dir ) );

		FileSearchFilter filter = new FileSearchFilter( pattern, searchType, searchBy, matchCase );
		files = dir.listFiles( filter );
		fileList = new ArrayList<File>();
		for( int i = 0; i < files.length; i++ )
		{
			switch( searchType )
			{
				case FILES_ONLY:
					if( isReadableFile( files[i] ) ) fileList.add( files[i] );
					break;
					
				case DIRS_ONLY:
					if( isReadableDirectory( files[i] ) ) fileList.add( files[i] );
					break;
					
				default:
					if( files[i].isDirectory() )
					{
						if( isReadableDirectory( files[i] ) ) fileList.add( files[i] );
					}
					else
					{
						if( isReadableFile( files[i] ) ) fileList.add( files[i] );
					}
			}
		}
		if( fileList.size() > 0 )
		{
			files = fileList.toArray( new File[fileList.size()] );
		}
		else
		{
			files = null;
		}
		
		return files;
	}

	/**
	 * <p>
	 * Returns <code>true</code> if the argument identifies a readable directory
	 * on the local file system.
	 * </p>
	 *
	 * @param  dir				the directory to check.
	 * @return					true if the entity exists, is a	directory, and
	 *								is readable.
	 * @throws					DirNotFoundException
	 *							if the entity 'dir' does not exist or is not a
	 *								directory.
	 */
	public static boolean isReadableDirectory( File dir )
		throws DirNotFoundException
	{
		boolean isValid = false;
		String dirName = getPathOf( dir );
		String error = null;
		
		try
		{
			if( dir.exists() )
			{
				if( dir.isDirectory() )
				{
					if( dir.canRead() )
					{
						isValid = true;
					}
				}
				else
				{
					error = IS_NOT_A_DIRECTORY_MSG + dirName;
				}
			}
			else
			{
				error = DOES_NOT_EXIST_MSG + dirName;
			}
		
			if( error != null )
			{
				throw new DirNotFoundException( error );
			}
		}
		catch( SecurityException exc )
		{
		}
		
		return isValid;
	}
	 
	/**
	 * <p>
	 * Returns <code>true</code> if the argument identifies a writeable
	 * directory on the local file system.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the directory is not found or if the argument
	 * identifies a normal file.
	 * </p>
	 *
	 * @param  dir				the directory to check.
	 * @return					true if the entity exists, is a	directory, and
	 *								is writeable.
	 * @throws					DirNotFoundException
	 *							if the entity 'dir' does not exist or is not a
	 *								directory.
	 */
	public static boolean isWriteableDirectory( File dir )
		throws DirNotFoundException
	{
		boolean isValid = false;
		String dirName = getPathOf( dir );
		String error = null;
		
		try
		{
			if( dir.exists() )
			{
				if( dir.isDirectory() )
				{
					if( dir.canWrite() )
					{
						isValid = true;
					}
				}
				else
				{
					error = IS_NOT_A_DIRECTORY_MSG + dirName;
				}
			}
			else
			{
				error = DOES_NOT_EXIST_MSG + dirName;
			}
		
			if( error != null )
			{
				throw new DirNotFoundException( error );
			}
		}
		catch( SecurityException exc )
		{
		}
		
		return isValid;
	}

	/**
	 * <p>
	 * Returns <code>true</code> if the specified path name does not exist and
	 * can be created, including creating any necessary parent directories, on
	 * the local file system.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the path name can not be created because it
	 * already exists or if a parent name of the path identifies a file.
	 * </p>
	 *
	 * @param  path				the entity to check.
	 * @return					true if the entity exists, is a	directory, and
	 *								is writeable.
	 * @throws					java.io.IOException
	 *							if the path name already exists, or a parent is
	 *								not a directory.
	 */
	public static boolean isCreateablePath( File path )
		throws IOException
	{
		if( path.exists() )
		{
			throw new IOException( ALREADY_EXISTS_MSG + getPathOf( path ) );
		}
		
		boolean createable = false;
		
		File parent = path.getParentFile();
		while( parent != null )
		{
			try
			{
				if( isWriteableDirectory( parent ) )
				{
					createable = true;
					break;
				}
				else
				{
					break;
				}
			}
			catch( DirNotFoundException exc )
			{
				if( ! parent.isDirectory() )
				{
					throw exc;
				}
				parent = parent.getParentFile();
			}
		}
		
		return createable;
	}
	
	/**
	 * <p>
	 * Returns <code>true</code> if the argument identifies a readable file on
	 * the local file system.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the specified entity is not found or identifies a
	 * directory instead of a file.
	 * </p>
	 *
	 * @param  file				the file to check.
	 * @return					true if the entity exists, is not a	directory,
	 *								and is readable.
	 * @throws					java.io.FileNotFoundException
	 *							if the file does not exist or is a directory.
	 */
	public static boolean isReadableFile( File file )
		throws FileNotFoundException
	{
		boolean isValid = false;
		String fileName = getPathOf( file );
		String error = null;
		
		try
		{
			if( file.exists() )
			{
				if( ! file.isDirectory() )
				{
					if( file.canRead() )
					{
						isValid = true;
					}
				}
				else
				{
					error = IS_A_DIRECTORY_MSG + fileName;
				}
			}
			else
			{
				error = DOES_NOT_EXIST_MSG + fileName;
			}
		
			if( error != null )
			{
				throw new FileNotFoundException( error );
			}
		}
		catch( SecurityException exc )
		{
		}
		
		return isValid;
	}
	
	/**
	 * <p>
	 * Returns <code>true</code> if the argument identifies a writeable
	 * file on the local file system.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the file is not found or if the argument
	 * identifies a directory.
	 * </p>
	 *
	 * @param  file				the file to check.
	 * @return					true if the path name exists, is a file, and is
	 *								writeable.
	 * @throws					FileNotFoundException
	 *							if the path name 'file' does not exist or is a
	 *								directory.
	 */
	public static boolean isWriteableFile( File file )
		throws FileNotFoundException
	{
		boolean isValid = false;
		String fileName = getPathOf( file );
		String error = null;
		
		try
		{
			if( file.exists() )
			{
				if( ! file.isDirectory() )
				{
					if( file.canWrite() )
					{
						isValid = true;
					}
				}
				else
				{
					error = IS_A_DIRECTORY_MSG + fileName;
				}
			}
			else
			{
				error = DOES_NOT_EXIST_MSG + fileName;
			}
		
			if( error != null )
			{
				throw new FileNotFoundException( error );
			}
		}
		catch( SecurityException exc )
		{
		}
		
		return isValid;
	}

} // End of class: +com.vtis.io.FileUtils
