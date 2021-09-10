package com.veetechis.lib.io;

import java.io.File;
import java.io.FilenameFilter;


/**
 * <p>
 * A filter for searching a file system location for files whose names match a
 * given file name.  The filter supports the following search characteristics
 * (* = default):
 * <ul>
 * <li>Match files only, directories only, or both*.</li>
 * <li>Match names based on whether a file name starts, ends, contains*, or
 * matches exactly the search name.</li>
 * <li>Match with case relevance* or non-case relevance.  Note that case
 * relevant matching may not be supported on all hosts or file systems.</li>
 * </ul>
 * </p>
 *
 * @author      pendraconx@gmail.com
 */
public class FileSearchFilter
	implements FilenameFilter
{
	private String searchName;
	private FileType searchFor;
	private MatchType searchBy;
	private CaseType matchCase;
	
	/**
	 * <p>
	 * Enum for specifying files, directories, or both as search targets.
	 * </p>
	 */
	public static enum FileType { FILES_ONLY, DIRS_ONLY, FILES_AND_DIRS }
	
	/**
	 * <p>
	 * Enum for specifying a file or directory name must match its start or end,
	 * need only contain, or match exactly the search name.
	 * </p>
	 */
	 public static enum MatchType { NAME_START, NAME_END, NAME_HAS, NAME_EXACT }
	
	/**
	 * <p>
	 * Enum for specifying a case relevant search, where supported, or a
	 * non-case relevant search is to be conducted.
	 * </p>
	 */
	public static enum CaseType { WITH_CASE, NOT_WITH_CASE }
	
	
	/**
	 * <p>
	 * Creates a new default instance of <code>FileSearchFilter</code> with a
	 * <code>null</code> search name.  A search name must be provided through a
	 * call to the method <code>setSearchName</code> or the instance will not
	 * return any matches.
	 * </p>
	 */
	public FileSearchFilter()
	{
		searchFor = FileType.FILES_AND_DIRS;
		searchBy = MatchType.NAME_HAS;
		matchCase = CaseType.WITH_CASE;
	}
	
	/**
	 * <p>
	 * Creates a new default instance of <code>FileSearchFilter</code> with the
	 * given search name.
	 * </p>
	 * 
	 * @param  name				the search name to match.
	 */
	public FileSearchFilter( String name )
	{
		this();
		searchName = name;
	}
	
	/**
	 * <p>
	 * Creates a new instance of <code>FileSearchFilter</code> with the given
	 * search name and the specified search characteristics.
	 * </p>
	 * 
	 * @param  name				the search name to match.
	 * @param  searchType		the type of entity search to conduct.
	 * @param  searchBy			the type of name matching to conduct.
	 * @param  withCase			name searching is case relevant when true.
	 * @see						#setSearchType
	 * @see						#setSearchBy
	 */
	public FileSearchFilter( String name, FileType searchType, MatchType searchBy, CaseType withCase )
	{
		setSearchType( searchType );
		setSearchBy( searchBy );
		searchName = name;
		matchCase = withCase;
	}
	
	/**
	 * <p>
	 * Sets the search name against which the names of file system entities are
	 * to be compared.
	 * </p>
	 *
	 * @param  name				the search name to match.
	 */
	public void setSearchName( String name )
	{
		searchName = name;
	}
		
	/**
	 * <p>
	 * Returns the search name against which the names of file system entities
	 * are compared.
	 * </p>
	 *
	 * @return					the search name to match.
	 */
	public String getSearchName()
	{
		return searchName;
	}
		
	/**
	 * <p>
	 * Sets the file system entity type that is to be filtered on; must be one
	 * of: <code>FILES_ONLY</code>, <code>DIRS_ONLY</code>, or
	 * <code>FILES_AND_DIRS</code>.
	 * </p>
	 *
	 * @param  type				the type of search to conduct.
	 */
	public void setSearchType( FileType type )
	{
		searchFor = type;
	}

	/**
	 * <p>
	 * Returns the file system entity type that is being filtered on; returns
	 * one of the types specified in the method <code>setSearchType</code>.
	 * </p>
	 *
	 * @return					the type of search.
	 */
	public FileType getSearchType()
	{
		return searchFor;
	}
		
	/**
	 * <p>
	 * Sets the type of name matching to be performed against the searched file
	 * system entities; must be one of: <code>MatchType.NAME_START</code>,
	 * <code>MatchType.NAME_END</code>, <code>MatchType.NAME_HAS</code>, or
	 * <code>MatchType.NAME_EXACT</code>.
	 * </p>
	 *
	 * @param  type				the type of name matching to conduct.
	 */
	public void setSearchBy( MatchType type )
	{
		searchBy = type;
	}

	/**
	 * <p>
	 * Returns the type of name matching performed against entities in the
	 * search file system; returns one of the types specified in the method
	 * <code>setSearchBy</code>.
	 * </p>
	 *
	 * @return					the type of name matching.
	 */
	public MatchType getSearchBy()
	{
		return searchBy;
	}
	
	/**
	 * <p>
	 * Sets the case relevance of the name search.  Name searching is case
	 * relevant when set to <code>true</code> (or <code>WITH_CASE</code>).
	 * </p>
	 *
	 * <p>
	 * Calling this method has no effect when searching on file systems where
	 * relevance is not supported.  In other words, on host platforms and file
	 * systems where case is not distinguished in entity names, setting this
	 * attribute to <code>true</code> produces the same search results as
	 * setting it to <code>false</code>.
	 * </p>
	 *
	 * @param  withCase			name searching is case relevant when true.
	 */
	public void setCaseRelevance( CaseType withCase )
	{
		matchCase = withCase;
	}
	
	/**
	 * <p>
	 * Returns <code>true</code> (or <code>WITH_CASE</code>) if case relevance
	 * is used in the name search.
	 * </p>
	 *
	 * @return					true if name search is case relevant.
	 */
	public boolean isCaseRelevant()
	{
		if( matchCase == CaseType.WITH_CASE )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*
	 * <p>
	 * Returns 'true' if the given file 'name' fulfills the pattern
	 * contract.
	 * </p>
	 */
	public boolean accept( File dir, String name )
	{
		boolean isMatch = false;

		String sname = searchName;
		String tname = name;
		if( ! isCaseRelevant() )
		{
			sname = searchName.toLowerCase();
			tname = name.toLowerCase();
		}
		
		switch( searchBy )
		{
			case NAME_START:
				if( tname.startsWith( sname ) )
					isMatch = true;
				break;
				
			case NAME_END:
				if( tname.endsWith( sname ) )
					isMatch = true;
				break;
				
			case NAME_HAS:
				if( tname.indexOf( sname ) > -1 )
					isMatch = true;
				break;
				
			case NAME_EXACT:
				if( tname.equals( sname ) )
					isMatch = true;
				break;
		}
		
		if( searchFor != FileType.FILES_AND_DIRS )
		{
			File test = new File( dir, tname );
			if( searchFor == FileType.FILES_ONLY && test.isDirectory() )
			{
				isMatch = false;
			}
			if( searchFor == FileType.DIRS_ONLY && ! test.isDirectory() )
			{
				isMatch = false;
			}
		}
		
		return isMatch;
	}
		
} // End of class: +com.vtis.io.FilenameSearchFilter
