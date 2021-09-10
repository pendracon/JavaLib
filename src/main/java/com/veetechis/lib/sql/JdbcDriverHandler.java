package com.veetechis.lib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.veetechis.lib.util.Resources;
import com.veetechis.lib.util.ResourceException;


/**
 * This class encapsulates access to a non-pooled <code>java.sql.Driver</code>
 * implementation for access to a compatible data source.  Instances of this
 * class are returned by <code>DbHandlerFactory</code> when type
 * <code>DbHandlerFactory.Handler.JDBC</code> is specified in calls to the
 * factory's <code>open</code> method.
 *
 * @author		pendraconx@gmail.com
 */
public class JdbcDriverHandler
	extends AbstractDbHandler
{
	private String propsFileName;		// the config properties file name.
	private String driver;				// the JDBC driver implementation
	private String url;					// the connection URL
	private String login;				// the database connection ID
	private String pass;				// the database connection password
	private boolean useProps;			// flags use of connection
										//	properties object as parameter
										//	to getConnection
	private Properties config;			// resource configuration properties
	private boolean loaded;				// flags driver is loaded
	private Connection conn;			// the database connection.


	/**
	 * Creates and initializes a new instance of <code>JdbcDriverHandler</code>
	 * for access to the resource specified in the named resource configuration
	 * properties file.
	 *
	 * Throws an exception if the specified properties file cannot be found or
	 * does not contain required properties.
	 *
	 * The named properties file must be loadable from the application's runtime
	 * classpath and meet the general properties contract specified for the
	 * <code>setConfigProperties</code> method of this class.
	 *
	 * @param  propsFileName	the name of the resource configuration file.
	 * @throws					DbHandlerException
	 *							if the configuration cannot be accessed, or is
	 *								invalid.
	 * @see						#setConfigProperties(Properties)
	 */
	JdbcDriverHandler( String propsFileName )
		throws DbHandlerException
	{
		setConfigResourceName( propsFileName );
	}

	/**
	 * Creates and initializes a new instance of <code>JdbcDriverHandler</code>
	 * for access to the resource specified in the given resource configuration
	 * properties.
	 *
	 * Throws an exception if the given properties object does not contain
	 * required properties.
	 *
	 * The given properties file must meet the general properties contract
	 * specified for the <code>setConfigProperties</code> method of this class.
	 *
	 * @param  config			the resource configuration object.
	 * @throws					DbHandlerException
	 *							if the configuration is invalid.
	 * @see						#setConfigProperties(Properties)
	 */
	JdbcDriverHandler( Properties config )
		throws DbHandlerException
	{
		setConfigProperties( config );
	}


	/**
	 * Updates the configuration resource file name associated with this
	 * instance.
	 *
	 * Throws an exception if the specified properties file cannot be found or
	 * does not contain required properties.
	 *
	 * The named properties file must be loadable from the application's runtime
	 * classpath and meet the general properties contract specified for the
	 * <code>setConfigProperties</code> method of this class.
	 *
	 * <b>Usage Note:</b> Setting this attribute forces a reload of the driver
	 * class and any connection properties passed in.
	 *
	 * @param  propsFileName	the new configuration resource file name.
	 * @throws					DbHandlerException
	 *							if the configuration cannot be accessed, or is
	 *								invalid.
	 * @see						#setConfigProperties(Properties)
	 */
	public void setConfigResourceName( String propsFileName )
		throws DbHandlerException
	{
		Properties config = null;
		try
		{
			config = Resources.loadPropertiesResource( propsFileName );
		}
		catch( ResourceException exc )
		{
			throw new DbHandlerException(
					"Error while attempting to load configuration from resource '" +
					propsFileName + "'.", exc );
		}
		setConfigProperties( config );
		this.propsFileName = propsFileName;
		loaded = false;
	}

	/**
	 * Returns the configuration resource file name associated with this
	 * instance.
	 *
	 * @return					the configuration resource file name.
	 */
	public String getConfigResourceName()
	{
		return propsFileName;
	}

	/**
	 * Updates the resource configuration properties associated with this
	 * instance.
	 *
	 * Throws an exception if the given properties do not contain at least the
	 * minimum required values, as identified below.
	 *
	 * The given properties should contain values for one or more of the
	 * following binding names:
	 * <ul>
	 * <li><u>@DB_DRIVER_CLASSNAME_KEY@</u>: the fully-qualified classname of
	 *		the implementing JDBC resource connector (REQUIRED)</li>
	 *
	 * <li><u>@DB_CONNECTION_URL_KEY@</u>: the fully-formed connection URL for
	 *		the target resource (REQUIRED)</li>
	 *
	 * <li><u>@DB_LOGIN_USERNAME_KEY@</u>: the user login ID to the target
	 *		resource</li>
	 *
	 * <li><u>@DB_LOGIN_PASSWORD_KEY@</u>: the user login password to the
	 *		target resource.</li>
	 *
	 * <li><u>@DB_CONNECTION_USE_PROPERTIES_KEY@</u>: if evaluates to "true"
	 *		(ignoring case), indicates other properties are contained which
	 *		should be passed to the resource manager as a parameter to
	 *		<code>getConnection</code>.</li>
	 * </ul>
	 *
	 * <b>Usage Note:</b> Setting this attribute forces a reload of the driver
	 * class and any connection properties passed in.
	 *
	 * @param  config			the new resource configuration properties.
	 * @throws					DbHandlerException
	 *							if the configuration is invalid.
	 */
	public void setConfigProperties( Properties config )
		throws DbHandlerException
	{
		String error = null;

		if( ! config.containsKey( "@DB_DRIVER_CLASSNAME_KEY@" ) )
		{
			error = "ERROR: Configuration does not specify a driver class!";
		}

		if( ! config.containsKey( "@DB_CONNECTION_URL_KEY@" ) )
		{
			error = "ERROR: Configuration does not specify a connection URL!";
		}

		if( error != null )
		{
			throw new DbHandlerException( error );
		}
		this.config = config;
		loaded = false;
	}

	/**
	 * Returns the resource configuration Properties object associated with this
	 * instance.
	 *
	 * @return					the resource configuration properties.
	 */
	public Properties getConfigProperties()
	{
		return config;
	}

	/**
	 * Returns a database connection from the specified driver implementation.
	 *
	 * Throws an exception if a database access error occurs.
	 *
	 * @return					a database connection.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	protected Connection getConnection()
		throws SQLException
	{
		if( loaded == false )
		{
			try
			{
				loadDriver();
			}
			catch( ClassNotFoundException ex )
			{
				String msg = "Driver Access Failure: " + ex.getMessage();
				throw new SQLException( msg );
			}
		}

		if( conn == null || conn.isClosed() )
		{
			if( login != null )
			{
				conn = DriverManager.getConnection( url, login, pass );
			}
			else if( useProps )
			{
				conn = DriverManager.getConnection( url, config );
			}
			else
			{
				conn = DriverManager.getConnection( url );
			}
		}
		return conn;
	}

	/*
	 * (Re)Loads the previously specified driver implementation and connection
	 * properties.  Throws an exception if the specified driver cannot be found
	 * or loaded.
	 */
	private void loadDriver()
		throws ClassNotFoundException
	{
		conn = null;

		driver = config.getProperty( "@DB_DRIVER_CLASSNAME_KEY@" );
		url = config.getProperty( "@DB_CONNECTION_URL_KEY@" );
		login = config.getProperty( "@DB_LOGIN_USERNAME_KEY@" );
		pass = config.getProperty( "@DB_LOGIN_PASSWORD_KEY@" );
		useProps = Boolean.
				valueOf( config.getProperty( "@DB_CONNECTION_USE_PROPERTIES_KEY@" ) ).booleanValue();

		Class.forName( driver );
		loaded = true;
	}

} // End of class: +com.vtis.sql.JdbcDriverHandler
