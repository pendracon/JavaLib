package com.veetechis.lib.sql;

import java.util.Properties;


/**
 * A factory for <code>DbHandler</code> implementation instances.
 *
 * @author      pendraconx@gmail.com
 */
public final class DbHandlerFactory
{
	/**
	 * Type specifiers for <code>DbHandler</code> types available from this
	 * factory.
	 *
	 * Type specifier <code>Handler.JDBC</code> returns an instance of
	 * <code>DbHandler</code> backed by a JDBC driver connected data source.
	 *
	 * Type specifier <code>Handler.JNDI</code> returns an instance of
	 * <code>DbHandler</code> backed by a JNDI datasource connected data source.
	 */
	public static enum Handler { JDBC, JNDI };
	
		
	/**
	 * Returns an instance of the specified <code>DbHandler</code> type
	 * initialized with the given argument.
	 *
	 * Throws an exception if an instance can not be returned.
	 *
	 * @param  type				the DbHandler type to return.
	 * @param  init				the initialization argument.
	 * @return					the initialized DbHandler instance.
	 * @throws					DbHandlerException
	 *							if an instance can not be initialized.
	 */
	public static final DbHandler open( Handler type, String init )
		throws DbHandlerException
	{
		try
		{
			if( type == Handler.JDBC )
			{
				return new JdbcDriverHandler( init );
			}
			else
			{
				return new JndiDataSourceHandler( init );
			}
		}
		catch( Exception exc )
		{
			throw new DbHandlerException( exc );
		}
	}

	/**
	 * Returns an instance of the specified <code>DbHandler</code> type
	 * initialized with the given environment properties.
	 *
	 * Throws an exception if an instance can not be returned.
	 *
	 * @param  type				the DbHandler type to return.
	 * @param  env				the initialization properties.
	 * @return					the initialized DbHandler instance.
	 * @throws					DbHandlerException
	 *							if an instance can not be initialized.
	 */
	public static final DbHandler open( Handler type, Properties env )
		throws DbHandlerException
	{
		try
		{
			if( type == Handler.JDBC )
			{
				return new JdbcDriverHandler( env );
			}
			else
			{
				return new JndiDataSourceHandler( env );
			}
		}
		catch( Exception exc )
		{
			throw new DbHandlerException( exc );
		}
	}


	/*
	 * No instances of this class should be created.
	 */
	private DbHandlerFactory() {}

} // End of class: +com.vtis.sql.DbHandlerFactory
