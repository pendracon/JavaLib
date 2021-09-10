package com.veetechis.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * This class encapsulates access to a <code>javax.sql.DataSource</code>
 * obtained from the JNDI naming service of a (J2EE/JavaEE) compatible
 * application server.
 *
 * <b>Usage Note:</b> This class is intended for use only in environments
 * utilizing Container-based Authentication.
 *
 * @author		pendraconx@gmail.com
 */
public class JndiDataSourceHandler
	extends AbstractDbHandler
{
	private DataSource ds;				// the data source
	private String lookupName;			// the JNDI lookup name
	private Context jndi;				// the JNDI context
	private Properties env;				// JNDI environment properties
	
	
	/*
	 * Creates a new instance of JndiDataSourceHandler for the data source
	 * bound to the specified JNDI lookup name in the default naming service
	 * context.
	 *
	 * @param  lookupName		the JNDI lookup name of the data source.
	 */
	JndiDataSourceHandler( String lookupName )
	{
		setConfigResourceName( lookupName );
	}
	
	/**
	 * Creates a new instance of <code>JndiDataSourceHandler</code> for access
	 * to the resource bound to the JNDI lookup name found in the given
	 * naming environment properties.
	 *
	 * <b>Usage Note:</b> The resource's JNDI lookup name is expected to be
	 * identified in the properties object by the key:
	 * <code>@DS_LOOKUP_NAME_KEY@</code>.  See <code>this(String)</code> for
	 * additional considerations.
	 *
	 * @param  env				the JNDI provider properties.
	 * @see						#this(String)
	 */
	JndiDataSourceHandler( Properties env )
	{
		this( env.getProperty( "@DS_LOOKUP_NAME_KEY@" ) );
		setConfigProperties( env );
	}
	
	/**
	 * Sets the lookup name of the underlying data source reference to be used
	 * by the next SQL process.
	 *
	 * <b>Usage Note:</b> Setting this attribute forces a reload of the of the
	 * specified <code>javax.sql.DataSource</code> object bound in the naming
	 * service.
	 *
	 * @param  lookupName		the data source lookup name.
	 */
	public void setConfigResourceName( String lookupName )
	{
		this.lookupName = lookupName;
		ds = null;
	}
	
	/**
	 * Returns the lookup name of the currently associated data source
	 * reference.
	 *
	 * @return					the lookup name.
	 */
	public String getConfigResourceName()
	{
		return lookupName;
	}

	/**
	 * Sets the naming context environment to be used by the next SQL process.
	 *
	 * <b>Usage Note:</b> Setting this attribute forces a reload of the
	 * previously specified <code>javax.sql.DataSource</code> object bound in
	 * the naming service.
	 *
	 * @param  env				the context environment.
	 */
	public void setConfigProperties( Properties env )
	{
		this.env = env;
		ds = null;
	}

	/**
	 * Returns the naming context environment properties for the currently
	 * associated data source reference.
	 *
	 * @return					the context properties.
	 */
	public Properties getConfigProperties()
	{
		return env;
	}
	
	/**
	 * Returns a database connection from the underlying data source.
	 *
	 * Throws an exception if a database access error occurs.
	 *
	 * @return					a database connection, or null.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	protected Connection getConnection()
		throws SQLException
	{
		if( ds == null )
		{
			try
			{
				loadDataSource();
			}
			catch( NamingException ex )
			{
				String msg = "DataSource Access Failure: " + ex.getMessage();
				throw new SQLException( msg );
			}
		}

		Connection conn = ds.getConnection();
		conn.setAutoCommit( true );
		return conn;
	}
	
	/*
	 * Obtains a reference to the previously specified DataSource instance.
	 * Throws an exception if the DataSource cannot be referenced.
	 */
	private void loadDataSource()
		throws NamingException
	{
		if( env == null )
		{
			jndi = new InitialContext();
		}
		else
		{
			jndi = new InitialContext( env );
		}

		jndi = (Context) jndi.lookup( "java:comp/env" );
		ds = (DataSource) jndi.lookup( lookupName );
	}
	
} // End of class: +com.vtis.sql.JndiDataSourceHandler
