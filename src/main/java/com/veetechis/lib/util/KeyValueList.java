package com.veetechis.lib.util;

import java.util.ArrayList;
import java.util.List;


/**
 * Implements a list of <code>key/value</code> string pairs.
 *
 * @author		pendraconx@gmail.com
 */
public class KeyValueList
{
	private ArrayList<KeyValuePair> list = null;
	private boolean strictChecking;


	/**
	 * Creates a new default instance of <code>KeyValueList</code>.
	 */
	public KeyValueList()
	{
		super();
		list = new ArrayList<KeyValuePair>();
	}

	/**
	 * Adds the given <code>key/value</code> pair to the list.  A
	 * <code>null</code> argument is ignored.
	 *
	 * Throws an exception if either part of the pair is <code>null</code>, or
	 * is empty and strict checking is enabled.
	 *
	 * @param  pair				the <code>key/value</code> pair to add.
	 * @throws					KeyValueException
	 *							if the pair is invalid.
     */
    public void add( KeyValuePair pair )
		throws KeyValueException
    {
		if( pair != null )
		{
			if( pair.getKey() == null || (strictChecking && pair.getKey().equals( "" )) )
			{
				throw new KeyValueException( "Key is invalid." );
			}

			if( pair.getValue() == null || (strictChecking && pair.getValue().equals( "" )) )
			{
				throw new KeyValueException( "Value is invalid." );
			}

			list.add( pair );
		}
	}

	/**
	 * Adds the given parameters as a <code>key/value</code> pair to the list.
	 *
	 * Throws an exception if either argument is <code>null</code>, or is empty
	 * and strict checking is enabled.
	 *
	 * @param  key				the key name for the pair.
	 * @param  value			the value of the pair.
	 * @throws					KeyValueException
	 *							if an argument is invalid.
     */
    public void add( String key, String value )
		throws KeyValueException
    {
		add( new KeyValuePair( key, value ) );
	}

	/**
	 * Returns the named <code>key/value</code> pair, or <code>null</code> if
	 * the pair is not present.  If more than one pair matching the one
	 * specified is part of the list, only the first one found is returned.
	 *
	 * @param  key				the key name of the pair to return.
	 * @return					the pair, or null.
	 */
	public KeyValuePair get( String key )
	{
		if( list != null )
		{
			for( KeyValuePair pair : list )
			{
				if( pair.getKey().equals( key ) )
				{
					return pair;
				}
			}
		}

		return null;
	}

	/**
	 * Removes the named <code>key/value</code> pair from the list.  Returns
	 * <code>true</code> if the list changed as a result of the call (instance
	 * removed).
	 *
	 * @return					true if the instance is removed.
	 */
	public boolean remove( String key )
	{
		return remove( get( key ) );
	}

	/**
	 * Removes the given <code>key/value</code> pair from the list.  Returns
	 * <code>true</code> if the list changed as a result of the call (instance
	 * removed).
	 *
	 * @return					true if the instance is removed.
	 */
	public boolean remove( KeyValuePair pair )
	{
		boolean removed = false;

		if( pair != null )
		{
			removed = list.remove( pair );
		}

		return removed;
	}

	/**
	 * Returns all <code>key/value</code> pairs in the list with the specified
	 * key name, or <code>null</code> if none are present.
	 *
	 * @param  key				the key name of the pairs to return.
	 * @return					the matching list, or null.
	 */
	public KeyValuePair[] find( String key )
	{
		ArrayList<KeyValuePair> pairs = null;
		if( list != null )
		{
			pairs = new ArrayList<KeyValuePair>();
			for( KeyValuePair pair : list )
			{
				if( pair.getKey().equals( key ) )
				{
					pairs.add( pair );
				}
			}
		}

		return toArray( pairs );
	}

	/**
	 * Ensures that no part of a given pair is <code>null</code> or empty when
	 * set to <code>true</code>.  When set to <code>false</code> a pair part may
	 * be empty but not <code>null</code>.
	 *
	 * @param  enabled			enables strict checking when true.
	 */
	public void useStrictChecking( boolean enabled )
	{
		strictChecking = enabled;
	}

	/**
	 * Returns <code>true</code> if strict pair checking is enabled.
	 *
	 * @return					true if strict checking is enabled.
	 * @see						#useStrictChecking
	 */
	public boolean isStrictChecking()
	{
		return strictChecking;
	}

	/**
	 * Returns the number of <code>key/value</code> pairs in the list.
	 *
	 * @return					the number of pairs.
	 */
	public int size()
	{
		return list.size();
	}

	/**
	 * Returns a string representation of the instance.
	 *
	 * @return					the string representation.
	 */
	public String toString()
	{
		StringBuffer buff = new StringBuffer( "[" );

		int i = 0;
		for( KeyValuePair pair : list )
		{
			if( i++ > 0 ) buff.append( "," );
			buff.append( pair.toString() );
		}
		buff.append( "]" );

		return buff.toString();
	}

	/**
	 * Returns the list of <code>key/value</code> pairs as a
	 * <code>KeyValuePair</code> array, or <code>null</code> if the list is
	 * <code>null</code> or empty.
	 *
	 * @return					the array of key/value pairs.
	 */
	public KeyValuePair[] toArray()
	{
		return toArray( list );
	}

	/**
	 * Returns the given list of <code>key/value</code> pairs as a
	 * <code>KeyValuePair</code> array, or <code>null</code> if a
	 * <code>null</code> or empty list is given.
	 *
	 * @param  pairsList		the list of key/value pairs to return.
	 * @return					the array of key/value pairs.
	 */
	public static KeyValuePair[] toArray( List<KeyValuePair> pairsList )
	{
		KeyValuePair[] pairs = null;
		if( pairsList != null && pairsList.size() > 0 )
		{
			pairs = (KeyValuePair[]) pairsList.toArray( new KeyValuePair[pairsList.size()] );
		}

		return pairs;
	}

} // End of class: +com.vtis.util.KeyValueList
