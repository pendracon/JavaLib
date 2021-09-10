package com.veetechis.lib.util;


/**
 * Represents a single <code>key/value</code> binding pair.
 *
 * @author      pendraconx@gmail.com
 */
public class KeyValuePair
{
	private String key;
	private String value;
	private boolean quoted;


	/**
	 * Creates a new uninitialized <code>KeyValuePair</code> instance.
     */
	public KeyValuePair()
	{
	}

	/**
	 * Creates a new <code>KeyValuePair</code> instance with the given binding
	 * pair.
	 *
	 * @param  key				the binding key.
	 * @param  value			the value to bind.
     */
	public KeyValuePair( String key, String value )
	{
		setKey( key );
		setValue( value );
	}

	/**
	 * Sets the binding key.
	 *
	 * @param  key				the binding key to set.
	 */
	public void setKey( String key )
	{
		this.key = key;
	}

	/**
	 * Returns the binding key.
	 *
	 * @return						the binding key.
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * Sets the value to bind.
	 *
	 * @param  value			the value to bind.
	 */
	public void setValue( String value )
	{
		this.value = value;
	}

	/**
	 * Returns the value bound to <code>key</code>.
	 *
	 * @return					the bound value.
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Enables </i>value</i> quoting in <code>toString</code> method if set to
	 * <code>true</code>.
	 *
	 * @param  quoted			enables quoting if true.
	 */
	public void useQuotedString( boolean quoted )
	{
		this.quoted = quoted;
	}

	/**
	 * Returns <code>true</code> if <i>value</i> quoting is enabled for output
	 * by the <code>toString</code> method.
	 *
	 * @return					true if quoting is enabled.
	 */
	public boolean isQuotedString()
	{
		return quoted;
	}

	/**
	 * Returns a string representation of the instance.
	 *
	 * @return					the string representation.
	 */
	public String toString()
	{
		StringBuffer buff = new StringBuffer();
		buff.append( key ).append( "=" );
		if( isQuotedString() )
		{
			buff.append( "\"" ).append( value ).append( "\"" );
		}
		else
		{
			buff.append( value );
		}

		return buff.toString();
	}

	/**
	 * Returns a hash code value for the instance.
	 *
	 * @return					the hash code value.
	 */
	public int hashCode()
	{
		return toString().hashCode();
	}

	/**
	 * Returns <code>true</code> if the given object is internally the same as
	 * this instance.
	 *
	 * @return					true if the object is "equal to" the instance.
	 */
	public boolean equals( Object obj )
	{
		boolean isEqual = false;

		if( obj != null && obj instanceof KeyValuePair )
		{
			String objStr = ((KeyValuePair) obj).toString();
			isEqual = toString().equals( objStr );
		}

		return isEqual;
	}

} // End of class: +com.vtis.util.KeyValuePair
