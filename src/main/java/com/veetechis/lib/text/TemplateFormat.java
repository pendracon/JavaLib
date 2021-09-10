package com.veetechis.lib.text;

import java.util.Iterator;
import java.util.Map;


/**
 * Provides message concatenation via token replacement.
 * 
 *
 * @author		pendraconx@gmail.com
 */
public class TemplateFormat
{
	/**
	 * Performs a simple text-only token replacement.  Returns the given
	 * <code>template</code> parameter with token values defined in the
	 * <code>tokens</code> parameter.  Only token-value pairs where both token
	 * and value are strings will be used for substitution.
	 * 
	 * @param  template			the template with tokens to replace.
	 * @param  tokens			the token replacement values.
	 * @return					the token-substituted template.
	 */
	public static String textFormat( String template, Map tokens )
	{
		Object token = null;
		Object value = null;
		String text = template;
		
		Iterator it = tokens.keySet().iterator();
		while( it.hasNext() ) {
			token = it.next();
			value = tokens.get( token );
			if( (token instanceof String) && (value instanceof String) ) {
				text = text.replaceAll( ((String) token), ((String) value) );
			}
		}
		return text;
	}
	
} // end of TemplateFormat
