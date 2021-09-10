package com.veetechis.lib.sql;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * This class provides default methods for executing SQL statements against an
 * associated database.  The <code>execute...</code> methods obtain their
 * connections from the abstract method
 * <code>#getConnection():java.sql.Connection</code>.  Sub-classes should
 * override this method to return a connection from an associated driver/factory
 * as appropriate.  Sub-classes must also implement the method
 * <code>DbHandler.open</code> necessary for initializing the resource to return
 * connections.  A default implementation of the method
 * <code>DbHandler.close</code> is here provided to release the resource
 * connection and associated objects, but may be overridden to provide
 * additional functionality as may be required.
 * </p>
 *
 * <p>
 * <b>Usage Note:</b> This class is intended for use in common SQL processing
 * contexts only and supports statement processing with data conforming to the
 * default standards of the execution environment.  This class should not be
 * used for processing specialized application needs.
 * </p>
 *
 * <p>
 * <b>Tech Note:</b> This class is declared "thread-safe" and may be sub-classed
 * for use in <i>Singleton</i> pattern implementations and similar shared access
 * environments.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public abstract class AbstractDbHandler
	implements DbHandler
{
	private Connection _conn;			// connection holder
	private PreparedStatement _ps;		// SQL prepared statement holder
	private ResultSet _rs;				// the execution results holder
	private ResultSetMetaData _rsmd;	// execution results meta data
	private int _cnt;					// affected rows update result

	private boolean nextRow;			// flags a row is available from the
										//	active query results
	private HashMap<String,Object> mrow;// a "mapped row" from active query
										//	results
	private ArrayList<Object> irow;		// an "indexed row" from active
										//	query results
	private boolean transact;			// flags a transaction is started
	private boolean newQuery;			// flags a new query was executed
	private boolean allowNull = true;	// flags support for 'null' values
										//	in queries and/or results

	/*
	 * Better to implement this as a factory "Singleton"...
	 */
	protected AbstractDbHandler()
	{
	}

	/**
	 * <p>
	 * Invoked by clients to signal end of processing and that the attached
	 * resources may be released as required.  If a transaction is in effect, it
	 * must be terminated before calling this method.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an error occurs during release processing or if a
	 * transaction is currently in effect.
	 * </p>
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public synchronized void close()
		throws SQLException
	{
		close( false );
	}

	/**
	 * <p>
	 * Invoked by clients to signal end of processing and that the attached
	 * resources may be released as required.  If the parameter equates to
	 * <code>true</code> and a transaction is in effect, the transaction is
	 * terminated with a rollback before the resource is released.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an error occurs during release processing.
	 * </p>
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public synchronized void close( boolean rollback )
		throws SQLException
	{
		if( rollback && hasTransaction() )
		{
			rollback();
		}
		if( _rs != null ) _rs.close();
		if( _ps != null ) _ps.close();
		if( _conn != null ) _conn.close();
		init();
	}

	/**
	 * <p>
	 * If argument is <code>true</code> then flags all subsequent operations to
	 * allow <code>null</code> values in queries and/or their results.
	 * </p>
	 *
	 * @param  allow			allow null values if true.
	 */
	public synchronized void allowNulls( boolean allow )
	{
		allowNull = allow;
	}

	/**
	 * <p>
	 * Returns <code>true</code> if the current instance is set to allow
	 * <code>null</code> values in subsequent queries and/or their results.
	 * </p>
	 *
	 * @return					true if null values	are allowed.
	 */
	public synchronized boolean isNullAllowed()
	{
		return allowNull;
	}

	/**
	 * <p>
	 * Executes the given query as a SQL prepared statement under a default
	 * execution environment.  Either the supplied row handling methods, or the
	 * <code>getQueryResults</code> method, of this class may be used to access
	 * the returned results.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the statement cannot be executed.
	 * </p>
	 *
	 * <p>
	 * <b>Usage Notes:</b> Things the programmer should be aware of are -
	 * <ul>
	 * <li>Parameters of the statement are passed in an instance of
	 * <code>java.util.List<Object></code> with their corresponding types
	 * specified in the passed in (int) array of <code>java.sql.Types</code>.
	 * <u>Both the list and array must be in relevant index sequence order</u>
	 * or risk an exception or data corruption as the likely result!</li>
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
	 * </p>
	 *
	 * <p>
	 * <b>WARNING:</b> Calling clients must call the method <code>close</code>
	 * to ensure proper handling of resources at the end of results processing!
	 * </p>
	 *
	 * @param  stmt				the statement to execute.
	 * @param  parms			the statement parameters.
	 * @param  types			the parameters' type specifiers.
	 * @throws					java.sql.SQLException
	 *							if the statement cannot be executed.
	 */
	public synchronized void executePreparedQuery( String stmt, List<Object> parms, int[] types )
		throws SQLException
	{
		if( _conn == null ) _conn = getConnection();
		_ps = _conn.prepareStatement( stmt );
		updateParameters( _ps, parms, types );
		_rs = _ps.executeQuery();
		newQuery = true;
	}

	/**
	 * <p>
	 * Executes the given update as a SQL prepared statement under a default
	 * execution environment.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the statement cannot be executed.
	 * </p>
	 *
	 * <p>
	 * <b>Usage Note:</b> This method does not require the client to release
	 * resources by calling <code>close</code> unless called within a
	 * transaction context.  Refer to the method
	 * {@link #executePreparedQuery executePreparedQuery} for additional
	 * information regarding use of this method.
	 * </p>
	 *
	 * @param  stmt				the statement to execute.
	 * @param  parms			the statement parameters.
	 * @param  types			the parameters' type specifiers.
	 * @throws					java.sql.SQLException
	 *							if the statement cannot be executed.
	 * @see						#executePreparedQuery
	 */
	public synchronized void executePreparedUpdate( String stmt, List<Object> parms, int[] types )
		throws SQLException
	{
		if( _conn == null ) _conn = getConnection();
		_ps = _conn.prepareStatement( stmt );
		updateParameters( _ps, parms, types );
		_cnt = _ps.executeUpdate();
		if( ! hasTransaction() ) close();
	}

	/**
	 * <p>
	 * Returns the <code>java.sql.ResultSet</code> resulting from the previous
	 * call to <code>executePreparedQuery</code>.
	 * </p>
	 *
	 * @return					the query results.
	 */
	public synchronized ResultSet getQueryResults()
	{
		return _rs;
	}

	/**
	 * <p>
	 * Returns the number of rows affected by the previous call to
	 * <code>executePreparedUpdate</code>.
	 * </p>
	 *
	 * @return					the update count.
	 */
	public synchronized int getUpdateCount()
	{
		return _cnt;
	}

	/**
	 * <p>
	 * Returns <code>true</code> if a <code>java.sql.ResultSet</code> is active
	 * and has another row to process.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if a database access error occurs.
	 * </p>
	 *
	 * @return					true if another row is available.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	public synchronized boolean nextRow()
		throws SQLException
	{
		nextRow = false;
		if( _rs != null ) nextRow = _rs.next();
		return nextRow;
	}

	/**
	 * <p>
	 * Returns the current row of the active query results as a map
	 * corresponding to the schema field names of the active query results, or
	 * <code>null</code> if no query results are active.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if a database access error occurs.
	 * </p>
	 *
	 * @return					the current row, or null.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	public synchronized Map<String,Object> getMappedRow()
		throws SQLException
	{
		if( _rs == null ) return null;
		if( ! nextRow ) return mrow;

		if( mrow == null )
		{
			mrow = new HashMap<String,Object>();
		}
		else
		{
			mrow.clear();
		}

		if( newQuery )
		{
			_rsmd = _rs.getMetaData();
			newQuery = false;
		}

		int cnt = _rsmd.getColumnCount();
		Object o = null;
		for( int i = 1; i <= cnt; i++ )
		{
			o = _rs.getObject( i );
			if( o != null || isNullAllowed() )
			{
				mrow.put( _rsmd.getColumnName(i), o );
			}
		}

		nextRow = false;
		return mrow;
	}

	/**
	 * <p>
	 * Returns the current row of the active query results as an indexed list,
	 * or <code>null</code> if no query results are active.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if a database access error occurs.
	 * </p>
	 *
	 * @return					the current row, or null.
	 * @throws					java.sql.SQLException
	 *							if a database access error occurs.
	 */
	public synchronized List<Object> getIndexedRow()
		throws SQLException
	{
		if( _rs == null ) return null;
		if( ! nextRow ) return irow;

		if( irow == null )
		{
			irow = new ArrayList<Object>();
		}
		else
		{
			irow.clear();
		}

		if( _rsmd == null )
		{
			_rsmd = _rs.getMetaData();
		}

		int cnt = _rsmd.getColumnCount();
		Object o = null;
		for( int i = 1; i <= cnt; i++ )
		{
			o = _rs.getObject( i );
			if( isNullAllowed() || o != null )
			{
				irow.add( o );
			}
		}

		nextRow = false;
		return irow;
	}

	/**
	 * <p>
	 * Starts a transaction context for subsequent operations on the resource.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if the transaction cannot be started.
	 * </p>
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public synchronized void begin()
		throws SQLException
	{
		if( _conn == null ) _conn = getConnection();
		_conn.setAutoCommit( false );
		transact = true;
	}

	/**
	 * <p>
	 * Signals that the resource should commit its updates to final storage.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an access error occurs or the resource is not in a
	 * transaction context.
	 * </p>
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public synchronized void commit()
		throws SQLException
	{
		_conn.commit();
	}

	/**
	 * <p>
	 * Signals that the resource should abandon its updates and return to the
	 * pre-transaction state.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if an access error occurs or the resource is not in a
	 * transaction context.
	 * </p>
	 *
	 * @throws					java.sql.SQLException
	 *							if an access error occurs.
	 */
	public synchronized void rollback()
		throws SQLException
	{
		_conn.rollback();
	}

	/**
	 * <p>
	 * Returns <code>true</code> if a transaction has been started, but not yet
	 * terminated, on the resource.
	 * </p>
	 *
	 * @return					true if a transaction is in	effect.
	 */
	public synchronized boolean hasTransaction()
	{
		return transact;
	}


	/**
	 * <p>
	 * Returns a <code>java.sql.Connection</code> from the associated
	 * driver/factory.
	 * </p>
	 *
	 * <p>
	 * Throws an exception if a connection cannot be returned.
	 * </p>
	 *
	 * @return					a database connection.
	 * @throws					java.sql.SQLException
	 *							if a connection cannot be returned.
	 */
	protected abstract Connection getConnection()
		throws SQLException;


	/*
	 * Initializes member variables to their default values.
	 */
	private void init()
	{
		transact = false;
		irow = null;
		mrow = null;
		_cnt = 0;
		_rsmd = null;
		_rs = null;
		_ps = null;
		_conn = null;
	}

	/*
	 * This method populates the given PreparedStatement object with the
	 * supplied parameters.  See 'executePrepared...' above.  Throws an
	 * exception if a database access error occurs.
	 */
	private void updateParameters( PreparedStatement ps, List<Object> parms, int[] types )
		throws SQLException
	{
		int idx = 0;
		for( Object o : parms )
		{
			if( o == null && ! isNullAllowed() ) continue;
			switch( types[idx] )
			{
				case Types.ARRAY:
					ps.setArray( idx + 1, (java.sql.Array) o );
					break;
				case Types.BIT:
				case Types.TINYINT:
					ps.setByte( idx + 1, ((Byte) o).byteValue() );
					break;
				case Types.BINARY:
					try
					{
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream( baos );
						oos.writeObject( o );
						oos.flush();
						ps.setBytes( idx + 1, baos.toByteArray() );
						oos.close();
						baos.close();
					}
					catch( IOException e )
					{
						StringBuffer msg = new StringBuffer( "ERROR: " ).
								append( "Unable to convert BINARY parameter - " ).
								append( "index = " ).
								append( idx );
						throw new SQLException( msg.toString() );
					}
					break;
				case Types.BLOB:
					ps.setBlob( idx + 1, (java.sql.Blob) o );
					break;
				case Types.BOOLEAN:
					ps.setBoolean( idx + 1, ((Boolean) o).booleanValue() );
					break;
				case Types.CHAR:
				case Types.VARCHAR:
					ps.setString( idx + 1, (String) o );
					break;
				case Types.CLOB:
					ps.setClob( idx + 1, (java.sql.Clob) o );
					break;
				case Types.DATE:
					ps.setDate( idx + 1, (java.sql.Date) o );
					break;
				case Types.DECIMAL:
				case Types.NUMERIC:
					ps.setBigDecimal( idx + 1, (java.math.BigDecimal) o );
					break;
				case Types.DOUBLE:
					ps.setDouble( idx + 1, ((Double) o).doubleValue() );
					break;
				case Types.FLOAT:
					ps.setFloat( idx + 1, ((Float) o).floatValue() );
					break;
				case Types.INTEGER:
					ps.setInt( idx + 1, ((Integer) o).intValue() );
					break;
				case Types.BIGINT:
					ps.setLong( idx + 1, ((Long) o).longValue() );
					break;
				case Types.NULL:
					if( ! isNullAllowed() )
					{
						StringBuffer msg = new StringBuffer( "ERROR: " ).
								append( "NULL given for parameter - " ).
								append( "index = " ).append( idx ).
								append( "; NULL is not allowed." );
						throw new SQLException( msg.toString() );
					}
					ps.setNull( idx + 1, Types.NULL );
					break;
				case Types.SMALLINT:
					ps.setShort( idx + 1, ((Short) o).shortValue() );
					break;
				case Types.TIME:
					ps.setTime( idx + 1, (java.sql.Time) o );
					break;
				case Types.TIMESTAMP:
					ps.setTimestamp( idx + 1, (java.sql.Timestamp) o );
					break;
				case Types.JAVA_OBJECT:
					ps.setObject( idx, o );
					break;
				default:
					StringBuffer msg = new StringBuffer( "ERROR: " ).
							append( "Unsupported type specifier for parameter - " ).
							append( "index = " ).append( idx ).
							append( "; " ).append( types[idx] );
					throw new SQLException( msg.toString() );
			}
			idx++;
		}
	}

} // End of class: +com.vtis.sql.AbstractDbHandler
