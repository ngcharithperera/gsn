package gsn.storage;

import static gsn.Main.getContainerConfig;
import gsn.Main;
import gsn.beans.DataField;
import gsn.beans.DataTypes;
import gsn.beans.StreamElement;
import gsn.utils.CaseInsensitiveComparator;
import gsn.utils.GSNRuntimeException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Ali Salehi (AliS, ali.salehi-at-epfl.ch)<br>
 * Date: Aug 5, 2005 <br>
 * Time: 9:29:47 AM <br>
 */
public class StorageManager {

	private static boolean mysql = false;

	private static boolean hsql  = false;

	public static enum DATABASE {

		MYSQL ( "jdbc:mysql:" ) {

			/*
			 * Returns the MySQL data type that can store this gsn datafield.
			 * @param field The datafield to be converted. @return convertedType
			 * the data type used by Mysql.
			 */
			public String convertGSNTypeToLocalType ( DataField field ) {
				String convertedType;
				switch ( field.getDataTypeID( ) ) {
				case DataTypes.CHAR :
				case DataTypes.VARCHAR :
					// Because the parameter for the varchar is not
					// optional.
					convertedType = field.getType( );
					break;
				case DataTypes.BINARY :
					convertedType = "BLOB";
					break;
				default :
					convertedType = DataTypes.TYPE_NAMES[ field.getDataTypeID( ) ];
				break;
				}
				return convertedType;
			}

			/*
			 * Returns the jdbc mysql driver.
			 */
			public void loadDriver ( ) throws SQLException {
				DriverManager.registerDriver( new com.mysql.jdbc.Driver( ) );
			}
		} ,
		HSQL ( "jdbc:hsql" ) {

			/*
			 * Returns the HSQLDB data type that can store this gsn datafield.
			 * @param field The datafield to be converted. @return convertedType
			 * the data type used by hsql.
			 */
			public String convertGSNTypeToLocalType ( DataField field ) {
				String convertedType = null;
				switch ( field.getDataTypeID( ) ) {
				case DataTypes.CHAR :
				case DataTypes.VARCHAR :
					// Because the parameter for the varchar is not
					// optional.
					convertedType = field.getType( );
					break;
				default :
					convertedType = DataTypes.TYPE_NAMES[ field.getDataTypeID( ) ];
				break;
				}
				return convertedType;
			}

			/*
			 * Returns the jdbc hsqldb driver.
			 */
			public void loadDriver ( ) throws SQLException {
				// TODO: maybe we only need one driver per database.
				// so we can simply give it as constructor parameter (as
				// jdbcPrefix ?)
				DriverManager.registerDriver( new org.hsqldb.jdbcDriver( ) );
			}
		};

		private final String jdbcPrefix;

		private Driver       driver;

		DATABASE ( String jdbcPrefix ) {
			this.jdbcPrefix = jdbcPrefix;
		}

		public String getJDBCPrefix ( ) {
			return jdbcPrefix;
		}

		/*
		 * Converts from internal GSN data types to a supported DB data type.
		 * @param field The DataField to be converted @return convertedType The
		 * datatype name used by the target database.
		 */

		public abstract String convertGSNTypeToLocalType ( DataField field );

		public abstract void loadDriver ( ) throws SQLException;

	};

	/**
	 * Determinse if the database used is MySql.
	 * 
	 * @return Returns true if the database used is mysql.
	 */
	public static boolean isMysqlDB ( ) {
		return mysql;
	}

	/**
	 * Determining if the database used is HSqlDB.
	 * 
	 * @return true if the database used is HSqlDB.
	 */
	public static boolean isHsql ( ) {
		return hsql;
	}

	private static StorageManager                  ourInstance                   = new StorageManager( );

	private static FastConnectionPool              connectionPool;

	private static final transient Logger          logger                        = Logger.getLogger( StorageManager.class );

	private static final transient boolean         isDebugEnabled                = logger.isDebugEnabled( );

	private TreeMap < CharSequence , TreeSet < CharSequence >> existingTablesToColumnMapping = new TreeMap < CharSequence , TreeSet < CharSequence >>( new CaseInsensitiveComparator( ) );

	public static StorageManager getInstance ( ) {
		return ourInstance;
	}

	private StorageManager ( ) {}

	public void createTable ( CharSequence tableName , DataField[] structure ) throws SQLException {
		Connection connection = null;
		dropTable( tableName );
		String sqlCreateStatement = getCreateTableStatement( structure , tableName ).replace( "\"" , "" );
		String sqlCreateIndexStatement = new StringBuilder( "CREATE INDEX " ).append( tableName.toString( ).toLowerCase( ) ).append( "_INDEX ON " ).append( tableName.toString( ).toLowerCase( ) ).append( " (timed DESC)" )
		.toString( );
		if ( isDebugEnabled == true ) logger.debug( new StringBuilder( ).append( "The create table statement is : " ).append( sqlCreateStatement ).toString( ) );
		if ( isDebugEnabled == true ) logger.debug( new StringBuilder( ).append( "The create index statement is : " ).append( sqlCreateIndexStatement ).toString( ) );
		TreeSet < CharSequence > fieldNames = new TreeSet < CharSequence >(new CaseInsensitiveComparator() );

		for ( DataField fieldName : structure ) {
			fieldNames.add( fieldName.getFieldName( ) );
		}
		existingTablesToColumnMapping.put( tableName , fieldNames );

		try {
			connection = connectionPool.borrowConnection( );
			if ( isDebugEnabled == true ) logger.debug( sqlCreateStatement );
			connection.createStatement( ).execute( sqlCreateStatement );
			connection.createStatement( ).execute( sqlCreateIndexStatement );
		} finally {
			if ( connection != null && !connection.isClosed( ) ) connection.close( );
		}
	}

	/**
	 * The method is protected b/c of the StorageManagerTest so that it can be
	 * testable from that class. The generated query is transformed to lower case
	 * before being returned.
	 */
	protected String getCreateTableStatement (  DataField [] structure , CharSequence alias ) {
		StringBuilder result = new StringBuilder( "CREATE " );
		if ( isHsql( ) ) result.append( " CACHED " );
		result.append( "TABLE " ).append( '\"' ).append( alias ).append( '\"' );
		if ( isHsql( ) ) result.append( " (PK BIGINT NOT NULL IDENTITY, timed BIGINT NOT NULL, " );
		if ( isMysqlDB( ) ) result.append( " (PK BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, timed BIGINT NOT NULL, " );
		for ( DataField field : structure ) {
			result.append( '\"' ).append( field.getFieldName( ).toUpperCase( ) ).append( '\"' ).append( ' ' );
			if ( StorageManager.isMysqlDB( ) ) {
				switch ( field.getDataTypeID( ) ) {
				case DataTypes.CHAR :
				case DataTypes.VARCHAR :
					// Because the parameter for the varchar is not
					// optional.
					result.append( field.getType( ) );
					break;
				case DataTypes.BINARY :
					result.append( "BLOB" );
					break;
				default :
					result.append( DataTypes.TYPE_NAMES[ field.getDataTypeID( ) ] );
				break;
				}
			} else if ( StorageManager.isHsql( ) ) {
				switch ( field.getDataTypeID( ) ) {
				case DataTypes.CHAR :
				case DataTypes.VARCHAR :
					// Because the parameter for the varchar is not
					// optional.
					result.append( field.getType( ) );
					break;
				default :
					result.append( DataTypes.TYPE_NAMES[ field.getDataTypeID( ) ] );
				break;
				}

			}
			result.append( " , " );
		}
		return result.deleteCharAt( result.lastIndexOf( "," ) ).append( ")" ).toString( ).toLowerCase( );
	}

	private String sqlType ( String type ) {
		int index = type.trim( ).indexOf( ":" );
		// if ( index >= 0 ) { return type.substring ( 0 , index ) ; }
		if ( index >= 0 ) { return type.toUpperCase( ).substring( 0 , index ); }
		return type.toUpperCase( );
	}

	/*
	 * This method first transforms the query to lower case and then executes it.
	 */
	public void createView ( CharSequence viewName , CharSequence selectQuery ) {
		Connection connection = null;
		try {
			connection = connectionPool.borrowConnection( );
			StringBuilder viewStatement = new StringBuilder( "create view " ).append( viewName.toString( ) ).append( " AS ( " ).append( selectQuery.toString( ).toLowerCase( ) ).append(" ) ");
			connection.createStatement( ).execute( viewStatement.toString() );
		} catch ( SQLException e ) {
			logger.error( e.getMessage( ) , e );
		} finally {
			try {
				if ( connection != null && !connection.isClosed( ) ) connection.close( );
			} catch ( SQLException e ) {}
		}
	}

	private static int MAX_DB_CONNECTIONS;

	/**
	 * This method first transforms the query to lower case and then executes it.
	 */
	public PreparedStatement obtainPreparedStatementForQuery ( CharSequence sql ) throws SQLException {
		Connection connection = connectionPool.borrowConnection( );
		connection.setAutoCommit(true);
		PreparedStatement toReturn = null;
		try {
			toReturn = connection.prepareStatement( sql.toString( ).toLowerCase( ) );
			if ( isDebugEnabled == true ) {
				logger.debug( new StringBuilder( ).append( "insertion prepared statement created: " ).append( sql ).toString( ) );
			}
		} catch ( SQLException e ) {
			logger.error( e.getMessage( ) , e );
			try {
				if ( connection != null && !connection.isClosed( ) ) connection.close( );
			} catch ( Exception e1 ) {}
		}
		return toReturn;
	}

	/**
	 * Inserts a stream element into the database.
	 * 
	 * @param tableName The database name which should be already created.
	 * @param se The stream element to Insert in to the specified database.
	 * @param duplicateErrorSuspend If set to true, the method ignores the
	 * exception caused by the duplicated rows in the specified table.
	 * @return True if the insert is done successfully. Any exceptions during the
	 * insertion process causes this method to return false.
	 */
	public boolean insertData ( CharSequence tableName , StreamElement se , boolean duplicateErrorSuspend ) {
		PreparedStatement ps = null;
		StringBuilder stringBuilder = null;
		try {
			stringBuilder = getInsertStatement( tableName , se );
			ps = obtainPreparedStatementForQuery( stringBuilder );
			int counter;
			for ( counter = 1 ; counter <= se.getFieldTypes( ).length ; counter++ ) {
				Serializable value = se.getData( )[ counter - 1 ];
				switch ( se.getFieldTypes( )[ counter - 1 ] ) {
				case DataTypes.VARCHAR :
					if (value==null)
						ps.setNull(counter, Types.VARCHAR);
					else
						ps.setString(counter, value.toString());
					break;
				case DataTypes.CHAR :
					if (value==null)
						ps.setNull(counter, Types.CHAR);
					else
						ps.setString(counter, value.toString());
					break;
				case DataTypes.INTEGER :
					if (value==null)
						ps.setNull(counter, Types.INTEGER);
					else
						ps.setInt( counter , ( Integer ) value );
					break;
				case DataTypes.SMALLINT :
					if (value==null)
						ps.setNull(counter, Types.SMALLINT);
					else
						ps.setShort( counter , ( Short ) value );
					break;
				case DataTypes.TINYINT :
					if (value==null)
						ps.setNull(counter, Types.TINYINT);
					else
						ps.setByte( counter , ( Byte ) value );
					break;
				case DataTypes.DOUBLE :
					if (value==null)
						ps.setNull(counter, Types.DOUBLE);
					else 
						ps.setDouble( counter , (Double)value );
					break;
				case DataTypes.BIGINT :
					if (value==null)
						ps.setNull(counter, Types.BIGINT);
					else
						ps.setLong( counter , ( Long )value );
					break;
				case DataTypes.BINARY :
					if (value==null)
						ps.setNull(counter, Types.BINARY);
					else
						ps.setBytes( counter , ( byte [ ] ) value );
					break;
				default :
					logger.error( "The type conversion is not supported for : " + se.getFieldNames( )[ counter - 1 ] + "(" + se.getFieldTypes( )[ counter - 1 ] + ")" );
				}
			}
			ps.setLong( counter , se.getTimeStamp( ) );
			ps.executeUpdate( );
		} catch ( SQLException e ) {
			if ( duplicateErrorSuspend && e.getMessage( ).contains( "Unique constraint violation" ) ) return true;
			logger.warn( e.getMessage( ) , e );
			if ( isDebugEnabled == true ) logger.debug( e.getMessage( ) , e );
			return false;
		} catch ( GSNRuntimeException e ) {
			if ( e.getType( ) == GSNRuntimeException.UNEXPECTED_VIRTUAL_SENSOR_REMOVAL ) {
				if ( isDebugEnabled == true ) logger.debug( "An stream element dropped due to unexpected virtual sensor removal." , e );
			} else
				logger.warn( "Inserting a stream element failed : " + se.toString( ) , e );
		} finally {
			if ( ps != null ) {
				try {
					if ( !ps.getConnection( ).isClosed( ) ) ps.getConnection( ).close( );
				} catch ( SQLException e ) {
					e.printStackTrace( );
				}
			}
		}
		return true;
	}

	/**
	 * Inserts a stream element into the database.
	 * 
	 * @param tableName The database name which should be already created.
	 * @param se The stream element to Insert in to the specified database. This
	 * method has the
	 * <code>duplicateErrorSuspend<code> set to false hence the method generates exceptions in the case of attempting to generate
	 *                  duplicated rows in the specified table.
	 * @return True if the insert is done successfully. Any exceptions during the insertion process
	 *         causes this method to return false.
	 */
	public boolean insertData ( CharSequence tableName , StreamElement se ) {
		return insertData( tableName , se , false );
	}

	public void insertDataNoDupError ( String tableName , Collection < StreamElement > data ) {
		for ( StreamElement se : data )
			insertDataNoDupError( tableName , se );
	}

	/**
	 * Inserts a stream element into the database.
	 * 
	 * @param tableName The database name which should be already created.
	 * @param data The stream element to Insert in to the specified database.
	 * This method has the
	 * <code>duplicateErrorSuspend<code> set to true hence the method won't generate any exceptions caused by attempting to
	 *                  insert duplicated rows in the specified table.
	 * @return True if the insert is done successfully. Any exceptions during the insertion process
	 *         causes this method to return false.
	 */
	public boolean insertDataNoDupError ( String tableName , StreamElement data ) {
		return insertData( tableName , data , true );
	}

	/**
	 * Creates a sql statement which can be used for inserting the specified
	 * stream element in to the specified table.
	 * 
	 * @param tableName The table which the generated sql will pointing to.
	 * @param se The stream element for which the sql statement is generated.
	 * @return A sql statement which can be used for inserting the provided
	 * stream element into the specified table.
	 */
	private StringBuilder getInsertStatement ( CharSequence tableName , StreamElement se ) {
		StringBuilder toReturn = new StringBuilder( "insert into " ).append( tableName ).append( " ( " );
		Collection < CharSequence > fieldsDefinedToBeStored = existingTablesToColumnMapping.get( tableName );
		for ( String fieldName : se.getFieldNames( ) ) {
			if ( fieldsDefinedToBeStored == null ) { throw new GSNRuntimeException( "Unexpected virtual sensor removal" , GSNRuntimeException.UNEXPECTED_VIRTUAL_SENSOR_REMOVAL ); }
			if ( fieldsDefinedToBeStored.contains( fieldName ) )
				toReturn.append( fieldName ).append( " ," );
			else if ( logger.isDebugEnabled( ) ) {
				StringBuilder acceptedFields = new StringBuilder( "The field : " ).append( fieldName ).append( "is ignored, accepted fields are : " );
				for ( CharSequence name : fieldsDefinedToBeStored )
					acceptedFields.append( name ).append( ", " );
				logger.debug( acceptedFields.toString( ) );
			}
		}
		toReturn.append( " timed " ).append( " ) values (" );
		for ( int i = 0 ; i < se.getFieldNames( ).length ; i++ ) {
			if ( fieldsDefinedToBeStored.contains( se.getFieldNames( )[ i ].toUpperCase( ) ) ) toReturn.append( " ? ," );
		}
		toReturn.append( " ? )" );
		return toReturn;
	}

	/**
	 * Executes the query of the database. Returns the specified colIndex of the
	 * first row. Useful for image recovery of the web interface.
	 * 
	 * @param query The query to be executed.
	 * @return A resultset with only one row and one column. The method returns
	 * null if the resultset.next() returns false for the first time (e.g., when
	 * the resultset is empty). In case the return value is not null, don't
	 * forget to close the result set.
	 */

	public ResultSet getBinaryFieldByQuery ( StringBuilder query , String colName , long pk ) {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = obtainPreparedStatementForQuery( query );
			ps.setLong( 1 , pk );
			ResultSet rs = ps.executeQuery( );
			if ( rs.next( ) )
				resultSet = rs;
			else {
				rs.getStatement( ).getConnection( ).close( );
			}
		} catch ( SQLException e ) {
			logger.error( e.getMessage( ) , e );
		}
		return resultSet;
	}

	/**
	 * This method is solely used by the search.jsp file in order to get the
	 * binary data. <br>
	 * The pair of this method is <code> returnResultSet(ResultSet) </code>.
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQueryWithResultSet ( String query ) throws SQLException {
		Connection connection = null;
		connection = connectionPool.borrowConnection( );
		ResultSet toReturn = connection.createStatement( ).executeQuery( query );
		return toReturn;
	}

	/**
	 * This method is solely used by the search.jsp file in order to get the
	 * binary data and it is the pair of the
	 * <code>executeQueryWithResultSet (String) </code> method.
	 */
	public void returnResultSet ( ResultSet resultSet ) {
		if ( resultSet == null ) return;
		try {
			resultSet.getStatement( ).getConnection( ).close( );
		} catch ( SQLException e ) {
			logger.error( "error while returning the results set. A connection is lost." );
			logger.error( e.getMessage( ) , e );
		}
	}

	public boolean isThereAnyResult ( CharSequence sqlQuery ) {
		boolean toreturn = false;
		PreparedStatement queryPreparedStatement = null;
		try {
			queryPreparedStatement = obtainPreparedStatementForQuery( sqlQuery );
			ResultSet resultSet = queryPreparedStatement.executeQuery( );
			toreturn = resultSet.next( );
		} catch ( SQLException error ) {
			logger.error( error.getMessage( ) , error );
		} finally {
			try {
				if ( queryPreparedStatement != null ) queryPreparedStatement.getConnection( ).close( );
			} catch ( SQLException e ) {}
		}
		return toreturn;
	}

	public void removeAllFrom ( String tableName ) {
		String sqlDeleteAllStatement = new StringBuilder( " delete from " ).append( tableName ).toString( );
		Connection connection = null;
		try {
			connection = connectionPool.borrowConnection( );
			connection.createStatement( ).execute( sqlDeleteAllStatement.replace( "\"" , "" ) );
		} catch ( SQLException e ) {
			logger.error( e.getMessage( ) , e );
		} finally {
			try {
				if ( connection != null && !connection.isClosed( ) ) connection.close( );
			} catch ( SQLException e ) {}
		}
	}

	/**
	 * This method drops the specified table. Note that
	 * 
	 * @param tableName : The name of the table to be dropped.
	 */
	public void dropTable ( CharSequence tableName ) {
		if ( logger.isDebugEnabled( ) ) logger.debug( "Dropping table structure: " + tableName );
		String dropIndexStatement = null;
		if ( isHsql( ) ) dropIndexStatement = new StringBuilder( "DROP INDEX " ).append( tableName.toString( ).toLowerCase( ) ).append( "_INDEX IF EXISTS " ).toString( );
		// if (StorageManager.getInstance ().isMysqlDB ())
		// dropIndexStatement = new StringBuilder ( " DROP INDEX " ).append (
		// tableName.toUpperCase () ).append ( "_INDEX ON " ).append (
		// tableName.toUpperCase ()).toString ();

		String dropTableStatement = new StringBuilder( " DROP TABLE IF EXISTS " ).append( tableName ).toString( ).replace( "\"" , "" );
		Connection connection = null;
		try {
			connection = connectionPool.borrowConnection( );
			if ( dropIndexStatement != null ) connection.createStatement( ).execute( dropIndexStatement );
			if ( dropTableStatement != null ) connection.createStatement( ).execute( dropTableStatement );
		} catch ( SQLException e ) {
			logger.info( e.getMessage( ) , e );
		} finally {
			try {
				if ( connection != null && !connection.isClosed( ) ) connection.close( );
			} catch ( SQLException e ) {

			}
			existingTablesToColumnMapping.remove( tableName );
		}
	}

	public void dropView ( CharSequence viewName ) {
		Connection connection = null;
		try {
			connection = connectionPool.borrowConnection( );
			String dropViewStatement;
			if ( StorageManager.isMysqlDB( ) )
				dropViewStatement = new StringBuilder( "drop view IF EXISTS " ).append( viewName ).toString( );
			else
				dropViewStatement = new StringBuilder( "drop view " ).append( viewName ).toString( );

			connection.createStatement( ).execute( dropViewStatement );
		} catch ( SQLException e ) {
			logger.error( e.getMessage( ) , e );
		} finally {
			try {
				if ( connection != null && !connection.isClosed( ) ) connection.close( );
			} catch ( SQLException e ) {

			}
		}
	}

	public void shutdown ( ) throws SQLException {
		if ( StorageManager.isHsql( ) ) {
			Connection conn = connectionPool.borrowConnection( );
			conn.createStatement( ).execute( "SHUTDOWN" );
			logger.warn( "Closing the database server (for HSqlDB) [done]." );

		}
		logger.warn( "Closing the connection pool [done]." );
		connectionPool.close( );
	}

	public void renameTable ( String oldName , String newName ) {
		Connection connection = null;
		try {
			connection = connectionPool.borrowConnection( );
			String renameTableStatement = new StringBuilder( "alter table " ).append( oldName ).append( " rename to " ).append( newName ).toString( );
			connection.createStatement( ).execute( renameTableStatement );
		} catch ( SQLException e ) {
			logger.error( e.getMessage( ) , e );
		} finally {
			try {
				if ( connection != null && !connection.isClosed( ) ) connection.close( );
			} catch ( SQLException e ) {

			};
		}
	}

	public DataEnumerator executeQuery ( CharSequence query , boolean binaryFieldsLinked ) {
		PreparedStatement ps = null;
		try {
			ps = obtainPreparedStatementForQuery( query );
		} catch ( SQLException e ) {
			logger.warn( e.getMessage( ) , e );
		}
		return new DataEnumerator( ps , binaryFieldsLinked );
	}

	public int executeUpdate ( CharSequence updateStatement ) {
		PreparedStatement updatePreparedStatement = null;
		try {
			updatePreparedStatement = obtainPreparedStatementForQuery( updateStatement );
			if ( updatePreparedStatement != null ) return updatePreparedStatement.executeUpdate( );
		} catch ( SQLException error ) {
			logger.error( error.getMessage( ) , error );
		} finally {
			try {
				if ( updatePreparedStatement != null ) updatePreparedStatement.getConnection( ).close( );
			} catch ( SQLException e ) {
				e.printStackTrace( );
			}
		}
		return -1;
	}
	public static final int               DEFAULT_STORAGE_POOL_SIZE        = 100;


	public void initialize ( String databaseDriver , String databaseUserName , String databasePassword , String databaseURL ) {
		if ( databaseDriver.trim( ).equalsIgnoreCase( "org.hsqldb.jdbcDriver" ) )
			hsql = true;
		else if ( databaseDriver.trim( ).equalsIgnoreCase( "com.mysql.jdbc.Driver" ) )
			mysql = true;
		else {
			logger.error( new StringBuilder( ).append( "The GSN doesn't support the database driver : " ).append( databaseDriver ).toString( ) );
			logger.error( new StringBuilder( ).append( "Please check the storage element in the file : " ).append( getContainerConfig( ).getContainerFileName( ) ).toString( ) );
			System.exit( 1 );
		}
		if (Main.getContainerConfig()==null || Main.getContainerConfig().getStoragePoolSize()<=0)
			MAX_DB_CONNECTIONS=DEFAULT_STORAGE_POOL_SIZE;
		else
			MAX_DB_CONNECTIONS = Main.getContainerConfig( ).getStoragePoolSize( );
		connectionPool = new FastConnectionPool( databaseDriver , databaseUserName , databasePassword , databaseURL , MAX_DB_CONNECTIONS );
	}
	/**
	 * Gets a table name and returns true if the table exists.
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public  boolean getTableSchema(CharSequence name) throws SQLException {
	
		return false;
	}
}

/**
 * @author Ali Salehi (AliS, ali.salehi-at-epfl.ch)<br>
 */
class FastConnectionPool {

	transient Logger                                      logger     = Logger.getLogger( FastConnectionPool.class );

	private transient static Class < FastConnectionPool > lock       = FastConnectionPool.class;

	private String                                        dbURL;

	private Properties                                    properties = new Properties( );

	ComboPooledDataSource                                 cpds       = new ComboPooledDataSource( );

	public FastConnectionPool ( String driverClassName , String username , String password , String databaseURL , int maximumOpenConnectionsToDB ) {

		this.dbURL = databaseURL;
		cpds.setJdbcUrl( databaseURL );
		cpds.setUser( username );
		cpds.setPassword( password );
		// cpds.setMaxStatements(30);
		cpds.setMinPoolSize( 5 );
		cpds.setAcquireIncrement( 5 );
		cpds.setMaxPoolSize( 30 );
		logger.info( "Initializing the access to the database server ..." );

		properties.put( "user" , username );
		properties.put( "username" , username );
		properties.put( "password" , password );
		properties.put( "ignorecase" , "true" );
		properties.put( "autocommit" , "true" );
		properties.put( "useUnicode" , "true" );

		properties.put( "autoReconnect" , "true" );
		properties.put( "autoReconnectForPools" , "true" );
		properties.put( "cacheCallableStmts" , "true" );
		properties.put( "cachePrepStmts" , "true" );
		properties.put( "cacheResultSetMetadata" , "true" );
		properties.put( "cacheResultSetMetadata" , "true" );
		properties.put( "defaultFetchSize" , "5" );
		properties.put( "useLocalSessionState" , "true" );
		properties.put( "characterEncoding" , "sjis" );
		properties.put( "useLocalSessionState" , "true" );
		properties.put( "useServerPrepStmts" , "false" );
		properties.put( "prepStmtCacheSize" , "512" );

		try {
			cpds.setDriverClass( driverClassName );
			Class.forName( driverClassName );
		} catch ( Exception e ) {
			e.printStackTrace( );
			System.exit( 1 );
		}

		Connection con = null;
		try {
			con = borrowConnection( );
			if ( StorageManager.isHsql( ) ) {
				con.createStatement( ).execute( "SET REFERENTIAL_INTEGRITY FALSE" );
				con.createStatement( ).execute( "CREATE ALIAS NOW_MILLIS FOR \"java.lang.System.currentTimeMillis\";" );
			}
			if ( StorageManager.isMysqlDB( ) ) {
				ResultSet rs = con.createStatement( ).executeQuery( "select version();" );
				rs.next( );
				String versionInfo = rs.getString( 1 );
				if ( !versionInfo.trim( ).startsWith( "5." ) ) {
					logger.error( new StringBuilder( ).append( "You are using MySQL version : " ).append( versionInfo ).toString( ) );
					logger.error( "To run GSN using MySQL, you need version 5.0 or later." );
					System.exit( 1 );
				}
			}
			logger.info( "Testing the Access to Database Server. [done]" );
		} catch ( SQLException e ) {
			logger.error( new StringBuilder( ).append( "Connecting to the database with the following properties failed :" ).append( "\n\t UserName :" ).append( username ).append( "\n\t Password : " )
					.append( password ).append( "\n\t Driver class : " ).append( driverClassName ).append( "\n\t Database URL : " ).append( databaseURL ).toString( ) );
			logger.error( new StringBuilder( ).append( e.getMessage( ) ).append( ", Please refer to the logs for more detailed information." ).toString( ) );
			logger.error( "Make sure in the : " + getContainerConfig( ).getContainerFileName( ) + " file, the <storage ...> element is correct." );
			e.printStackTrace( );
			if ( logger.isInfoEnabled( ) ) logger.info( e.getMessage( ) , e );
			System.exit( 1 );
		} finally {
			try {
				if ( con != null && !con.isClosed( ) ) con.close( );
			} catch ( SQLException e ) {}
		}

	}

	public Connection borrowConnection ( ) throws SQLException {
		Connection conn = cpds.getConnection( );
		conn.setAutoCommit( true );
		return conn;
	}

	public void close ( ) {
		cpds.close( );
	}
}
