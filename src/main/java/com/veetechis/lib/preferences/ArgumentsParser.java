package com.veetechis.lib.preferences;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.veetechis.lib.util.KeyValueException;
import com.veetechis.lib.util.KeyValueList;
import com.veetechis.lib.util.KeyValuePair;


/**
 * <p>
 * Accepts and parses an array of arguments (typically, command-line arguments).
 * Parsing is configured by calls to <code>setArgSwitch</code> and
 * <code>setParmSeparator</code>.  Arguments are expected to start with '--' as
 * a switch indicator by default.  By default, arguments and their parameters
 * are white-space separated, or occupy ordered array elements (<i>i.e.</i>
 * <code>arg,parm</code>).
 * </p>
 *
 * @author      pendraconx@gmail.com
 */
public class ArgumentsParser
{
	private String argSwitch;
	private String parmSep;
	
	/**
	 * <p>
	 * Constant for the default argument switch.
	 * </p>
	 */
	public final static String DEFAULT_SWITCH = "--";
	
	/**
	 * <p>
	 * Constant for the default parameter separator.
	 * </p>
	 */
	public final static String DEFAULT_SEP = " ";
	
	
	/**
	 * <p>
	 * Creates a new default instance of <code>ArgsParser</code>.
	 * </p>
	 */
	public ArgumentsParser()
	{
		argSwitch = DEFAULT_SWITCH;
		parmSep = DEFAULT_SEP;
	}
	
	/**
	 * <p>
	 * Sets the argument switch for the next call to <code>parse</code>.
	 * </p>
	 *
	 * @param  argSwitch		the new argument switch.
	 */
	public void setArgSwitch( String argSwitch )
	{
		this.argSwitch = argSwitch;
	}
	
	/**
	 * <p>
	 * Returns the argument switch which will be used by the next call to
	 * <code>parse</code>.
	 * </p>
	 *
	 * @return					the current argument switch.
	 */
	public String getArgSwitch()
	{
		return argSwitch;
	}
	
	/**
	 * <p>
	 * Sets the parameter separator for the next call to <code>parse</code>.
	 * </p>
	 *
	 * @param  parmSep			the new parameter separator.
	 */
	public void setParmSeparator( String parmSep )
	{
		this.parmSep = parmSep;
	}
	
	/**
	 * <p>
	 * Returns the parameter separator which will be used by the next call to
	 * <code>parse</code>.
	 * </p>
	 *
	 * @return					the current parameter separator.
	 */
	public String getParmSeparator()
	{
		return parmSep;
	}
	
	/**
	 * <p>
	 * Parses the given array as a argument/parameter list.  Results of the
	 * parsing are returned as a list of <code>key=value</code> pairs.  Returns
	 * <code>null</code> if the given array is null.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the list does not contain arguments as determined
	 * by <code>getArgSwitch</code>.
	 * </p>
	 *
	 * @param  args				the argument list to parse.
	 * @return					the arguments as a <code>key=value</code> list.
	 * @throws					com.vtis.util.KeyValueException
	 *							if the given list does not contain switched
	 *								arguments.
	 */
	public KeyValueList parse( String[] args )
		throws KeyValueException
	{
		ArrayList<String> argList = validateArgs( args );
		
		KeyValueList list = new KeyValueList();
		String arg = null, parm = null;
		
		for( int i = 0; i < argList.size(); i++ )
		{
			arg = argList.get( i );
			if( arg.startsWith( argSwitch ) )
			{
				parm = argList.get( i + 1 );
				if( parm.startsWith( argSwitch ) )
				{
					parm = "";
				}
				else
				{
					i++;
				}
				list.add( new KeyValuePair( arg, parm ) );
			}
		}
		
		return list;
	}
	
	
	/*
	 * Validates and 'sanitizes' the given arguments by:
	 *
	 *		1) ensuring there is at least one correctly switched argument in the
	 *			array; and
	 *
	 *		2) breaking up the argument array into a more manageable ArrayList.
	 *
	 * Throws an exception if no switch arguments are present.
	 */
	private ArrayList<String> validateArgs( String[] args )
		throws KeyValueException
	{
		ArrayList<String> argList = new ArrayList<String>();
		StringTokenizer stk = null;
		String element = null;
		int argCnt = 0;
		
		for( int i = 0; i < args.length; i++ )
		{
			stk = new StringTokenizer( args[i], parmSep );
			while( stk.hasMoreTokens() )
			{
				element = stk.nextToken();
				if( element.startsWith( argSwitch ) ) argCnt++;
				argList.add( element );
			}
		}
		if( argCnt == 0 ) throw new KeyValueException();
		
		return argList;
	}
	
	
} // End of class: +com.vtis.prefs.ArgsParser
