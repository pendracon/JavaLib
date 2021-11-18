package com.veetechis.lib.util;

import java.nio.ByteBuffer;

/**
 * Provides methods for converting between byte arrays and primitive types.
 */
public class ArrayConverter
{
	public static synchronized byte[] intToBytes( int value )
	{
		intBuffer.clear();
		intBuffer.putInt( 0, value );
		return intBuffer.array();
	}

	public static synchronized int bytesToInt( byte[] bytes )
	{
		intBuffer.clear();
		intBuffer.put( bytes, 0, bytes.length );
		intBuffer.flip();
		return intBuffer.getInt();
	}

	public static synchronized byte[] longToBytes( long value )
	{
		longBuffer.clear();
		longBuffer.putLong( 0, value );
		return longBuffer.array();
	}

	public static synchronized long bytesToLong( byte[] bytes )
	{
		longBuffer.clear();
		longBuffer.put( bytes, 0, bytes.length );
		longBuffer.flip();
		return longBuffer.getLong();
	}


	private static ByteBuffer intBuffer = ByteBuffer.allocate(Integer.BYTES);    
	private static ByteBuffer longBuffer = ByteBuffer.allocate(Long.BYTES);    
}
