package com.veetechis.lib.security;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * <p>
 * This class provides utility methods for handling encoding and hashing of
 * special use and that are not provided in standard libraries.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public class Encoder
{
    /**
	 * <p>
	 * The character matrix for pseudo-random generation of IDs and keys.
	 * Alternation of upper and lower letters with rotating 3-step interspersion
	 * of numbers helps create "random" hashes.
	 * </p>
     */
    public final static char[] KEY_MATRIX = {
		'A','b','1','C','d','2','E','f','3','G','h','4','I',
		'j','5','K','l','6','M','n','7','O','p','8','Q','r',
		'9','S','t','0','U','v','1','W','x','2','Y','z','3',
		'a','B','4','c','D','5','e','F','6','g','H','7','i',
		'J','8','k','L','9','m','N','0','o','P','1','q','R',
		'2','s','T','3','u','V','4','w','X','5','y','Z','6'
	};
    
	/**
	 * <p>
	 * Alphabet for Base64 encoding.
	 * </p>
	 *
	 * @see		<a href="http://www.faqs.org/rfcs/rfc1521.html">
	 *				RFC 1521, <i>MIME Part One</i>, @ 5.2:
	 *				Base64 Content-Transfer-Encoding</a>
	 */
	public final static char[] BASE64_ALPHA = {
		'A','B','C','D','E','F','G','H','I','J','K','L','M',
		'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
		'a','b','c','d','e','f','g','h','i','j','k','l','m',
		'n','o','p','q','r','s','t','u','v','w','x','y','z',
		'0','1','2','3','4','5','6','7','8','9','+','/'
	};
    

    /**
	 * <p>
	 * Provides a "randomly" generated key of <code>length</code> bytes long.
	 * Returns <code>null</code> if <code>length</code> <= 0.
	 * </p>
	 *
	 * @param  length			the length of the key to generate.
	 * @return					the generated key or null.
     */
    public static String generateKey( int length )
    {
		if( length <= 0 ) return null;
		
        StringBuffer key = new StringBuffer();
        for( int i = 0, idx = 0; i < length; i++ )
		{
            idx = (int) (Math.random() * KEY_MATRIX.length);
            key.append( KEY_MATRIX[idx] );
        }
        return key.toString();
    }
    
    /**
	 * <p>
	 * Returns the given <code>text</code> in Base64 encoded form.  Returns
	 * <code>null</code> if the given <code>text</code> is either empty ("") or
	 * <code>null</code>.
	 * </p>
     * 
     * @param  text				the text to encode.
     * @return					the encoded text or null.
     */
    public static String encodeBase64( String text )
    {
		if( text == null || text.equals( "" ) ) return null;
		
		/* Prepares the bitstream and output buffers for processing.
		 */
		StringBuffer bits = new StringBuffer();
		StringBuffer out = new StringBuffer();

		/* Encode the data.
		 */
		String buff = null;				// processing buffer
		int bufflen = 0;				// processing buffer length
		int bitslen = 0;				// bitstream buffer length
		int end = 0;					// end substring index

		int len = text.length();
		for( int i = 0; i < len; i += 3 )
		{
			/* Get 3-character bit group.
			 */
			for( int j = 0; j < 3; j++ )
			{
				/* Check to add pad bits.
				 */
				if( i + j >= len )
				{
					if( bits.length() == 8 ) bits.append( "0000" );
					if( bits.length() == 16 ) bits.append( "00" );
					break;
				}

				/* Append character to bitstream.
				 */
				buff = Integer.toBinaryString( (int) text.charAt( i + j ) );
				bufflen = buff.length();
				if( bufflen < 8 )
				{
					bufflen = 8 - bufflen;
					for( int k = 0; k < bufflen; k++ )
					{
						buff = "0" + buff;
					}
				}
				bits.append( buff );
			}
			
			/* Append bitstream to output as 4-char Base64 string.
			 */
			bitslen = bits.length();
			for( int j = 0; j < bitslen; j += 6 )
			{
				end = j + 6;
				try
				{
					buff = bits.substring( j, end );
				}
				catch( StringIndexOutOfBoundsException sioobe )
				{
					buff = bits.substring( j, end - 1 );
				}
				out.append( BASE64_ALPHA[Integer.parseInt( buff, 2 )] );
			}

			/* Clear bitstream for next iteration.
			 */
			bits.delete( 0, bitslen );
		}
	
		/* Add pad characters to output.
		 */
		if( bitslen < 24 )
		{
			int padcnt = (24 - bitslen) / 6;
			for( int i = 0; i < padcnt; i++ ) out.append( "=" );
		}

		return out.toString();
    }

	/**
	 * <p>
	 * Encodes and returns the given text argument as a hex coded MD5 hash.
	 * Returns <code>null</code> if the argument is empty ("") or
	 * <code>null</code>.
	 * </p>
	 *
	 * @param  text				the text to hash.
	 * @return					the text as an MD5 hash.
	 */
	public static String encodeMD5( String text )
	{
		if( text == null || text.length() == 0 ) return null;
		
		return (new DigestUtils().md5Hex( text ));
	}
	
} // End of class: +com.vtis.security.Encoder
