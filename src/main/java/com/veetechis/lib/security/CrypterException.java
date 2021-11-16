package com.veetechis.lib.security;

import java.security.GeneralSecurityException;

/**
 * Thrown by Crypter to indicate an error in its operations.
 */
public class CrypterException
extends GeneralSecurityException
{
	public CrypterException( String message )
	{
		super( message );
	}

	public CrypterException( Exception cause )
	{
		super( cause );
	}

	public CrypterException( String message, Exception cause )
	{
		super( message, cause );
	}
}
