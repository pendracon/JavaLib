package com.veetechis.lib.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Represents a generic implementation of the <i>Data Access Object</i> (DAO)
 * pattern.  Provides methods for automatically obtaining a connection to a SQL
 * data source and executing statements against it.
 *
 * @author		pendraconx@gmail.com
 */
public interface DbHandler
{
	/**
	 * Sets the name of configuration resource to the value given.
	 *
	 * Throws an exception if the named resource is invalid.
	 *
	 * @param  resource			the configuration resource name.
	 * @throws					DbHandlerException
	 *							if the resource is invalid.
	 */
	public void setConfigResourceName( String resource )
		throws DbHandlerException;

	/**
	 * Returns the name of the configuration resource.
	 *
	 * @return					the configuration resource name.
	 */
	public String getConfigResourceName();

	/**
	 * Sets the handler's configuration properties to the given argument.
	 *
	 * Throws an exception if the configuration is invalid.
	 *
	 * @param  config			the configuration properties.
	 * @throws					DbHandlerException
	 *							if the configuration is invalid.
	 */
	public void setConfigProperties( Properties config )
		throws DbHandlerException;

	/**
	 * Returns the handler's environment configuration properties.
	 *
	 * @return					the configuration properties.
	 */
	public Properties getConfigProperties();

	/**
	 * Invoked by clients to signal end of processing and that the attached
	 * resources may be released as required.  If a transaction is in effect, it
	 * must be terminated before calling this method.
	 *
	 * Throws an exception if an error occurs during release processing or a
	 * transaction is currently in effect.
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public void close()
		throws SQLException;

	/**
	 * Invoked by clients to signal end of processing and that the attached
	 * resources may be released as required.  If the parameter equates to
	 * <code>true</code> and a transaction is in effect, the transaction is
	 * terminated with a rollback before the resource is released.
	 *
	 * Throws an exception if an error occurs during release processing.
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public void close( boolean rollback )
		throws SQLException;

	/**
	 * If argument is <code>true</code> then it flags all subsequent operations
	 * to allow <code>null</code> values in queries and their results.
	 *
	 * @param  allow			allow null values if true.
	 */
	public void allowNulls( boolean allow );

	/**
	 * Returns <code>true</code> if the current instance is set to allow
	 * <code>null</code> values in subsequent queries and their results.
	 *
	 * @return					true if null values	are allowed.
	 */
	public boolean isNullAllowed();

	/**
	 * Executes the given query as a SQL prepared statement under a default
	 * execution environment.  Either the supplied row handling methods, or the
	 * <code>getQueryResults</code> method, of this class may be used to access
	 * the returned results.
	 *
	 * Throws an exception if the statement cannot be executed.
	 *
	 * <b>Usage Notes:</b> Things the programmer should be aware of are -
	 * <ul>
	 * <li>Parameters of the statement are passed in an instance of
	 * <code>java.util.List<Object></code> with their corresponding types
	 * specified in the passed in (int) array of <code>java.sql.Types</code>.
	 * <u>Both the list and array must be in relevant index sequence order</u>
	 * or risk an exception and/or data corruption as the likely result!</li>
	 *
	 * <li>Supported object types include all types matching an appropriate
	 * <code>java.sql.PreparedStatement.setXXX</code> except types of
	 * <code>java.io.InputStream</code>.</li>
	 *
	 * <li>Types of <code>byte</code> should be type specified with
	 * <code>java.sql.Types.BIT</code>, and types of <code>byte[]</code>
	 * with <code>Types.[xxx]BINARY</code>.  All other type specifier must
	 * closely match the type supported by the relevant <code>setXXX</code>
	 * method.</li>
	 *
	 * <li>Primitive data types must be wrapped in an appropriate object
	 * wrapper.</li>
	 * </ul>
	 *
	 * <b>WARNING:</b> Calling clients must call the method <code>close</code>
	 * to ensure proper handling of resources at the end of results processing!
	 *
	 * @param  stmt				the statement to execute.
	 * @param  parms			the statement parameters.
	 * @param  types			the parameters' type specifiers.
	 * @throws					java.sql.SQLException
	 *							if the statement cannot be executed.
	 */
	public void executePreparedQuery( String stmt, List<Object> parms, int[] types )
		throws SQLException;

	/**
	 * Executes the given update as a SQL prepared statement under a default
	 * execution environment.
	 *
	 * Throws an exception if the statement cannot be executed.
	 *
	 * <b>Usage Note:</b> This method does not require the client to release
	 * resources by calling <code>close</code> unless called within a
	 * transaction context.  Refer to the method
	 * {@link #executePreparedQuery executePreparedQuery} for additional
	 * information regarding use of this method.
	 *
	 * @param  stmt				the statement to execute.
	 * @param  parms			the statement parameters.
	 * @param  types			the parameters' type specifiers.
	 * @throws					java.sql.SQLException
	 *							if the statement cannot be executed.
	 * @see						#executePreparedQuery
	 */
	public void executePreparedUpdate( String stmt, List<Object> parms, int[] types )
		throws SQLException;

	/**
	 * Returns the <code>java.sql.ResultSet</code> resulting from the previous
	 * call to <code>executePreparedQuery</code>.
	 *
	 * @return					the query results.
	 */
	public ResultSet getQueryResults();

	/**
	 * Returns the number of rows affected by the previous call to
	 * <code>executePreparedUpdate</code>.
	 *
	 * @return					the update count.
	 */
	public int getUpdateCount();

	/**
	 * Returns <code>true</code> if a <code>java.sql.ResultSet</code> is active
	 * and has another row to process.
	 *
	 * Throws an exception if a database access error occurs.
	 *
	 * @return					true if another row is available.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	public boolean nextRow()
		throws SQLException;

	/**
	 * Returns the current row of the active query results as a map, or
	 * <code>null</code> if no query results are active.
	 *
	 * Throws an exception if a database access error occurs.
	 *
	 * @return					the current row, or null.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	public Map<String,Object> getMappedRow()
		throws SQLException;

	/**
	 * Returns the current row of the active query results as an indexed list,
	 * or <code>null</code> if no query results are active.
	 *
	 * Throws an exception if a database access error occurs.
	 *
	 * @return					the current row, or null.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	public List<Object> getIndexedRow()
		throws SQLException;

	/**
	 * Starts a transaction context for subsequent operations on the resource.
	 *
	 * Throws an exception if the transaction cannot be started.
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public void begin()
		throws SQLException;

	/**
	 * Signals that the resource should commit its updates to final storage.
	 *
	 * Throws an exception if an access error occurs or the resource is not in a
	 * transaction context.
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public void commit()
		throws SQLException;

	/**
	 * Signals that the resource should abandon its updates and return to the
	 * pre-transaction state.
	 *
	 * Throws an exception if an access error occurs or the resource is not in a
	 * transaction context.
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public void rollback()
		throws SQLException;

	/**
	 * Returns <code>true</code> if a transaction has been started, but not yet
	 * terminated, on the resource.
	 *
	 * @return					true if a transaction is in	effect.
	 */
	public boolean hasTransaction();

} // End of interface: +com.vtis.sql.DbHandler
