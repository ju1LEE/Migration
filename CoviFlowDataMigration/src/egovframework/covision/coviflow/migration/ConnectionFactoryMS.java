package egovframework.covision.coviflow.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class ConnectionFactoryMS {
	
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String MARIA_DRIVER = "org.mariadb.jdbc.Driver";
	public static final String TIBERO_DRIVER = "com.tmax.tibero.jdbc.TbDriver";
	public static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DBCP_DRIVER = "org.apache.commons.dbcp2.PoolingDriver";
	public static final String DBCP_NAME = "jdbc:apache:commons:dbcp:";

	public static final String MARIA_VALIDATION_SQL = "select 1";
	public static final String ORACLE_VALIDATION_SQL = "select 1 from dual";
	public static final String MSSQL_VALIDATION_SQL = "select 1";
	public static final String CUBRID_VALIDATION_SQL = "select 1 from db_root";
	
	private static interface Singleton {
		final ConnectionFactoryMS INSTANCE = new ConnectionFactoryMS();
	}

	private final DataSource dataSource;

	private ConnectionFactoryMS() {

		// 1. Register the Driver to the jbdc.driver java property
		try {
			Class.forName(SQLSERVER_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 2. Create the Connection Factory (DriverManagerConnectionFactory)
		Properties connectionProps=new Properties(); 
		connectionProps.put("user", "gwuser"); 
		connectionProps.put("password", "Devsno1@)17"); 
		
		DriverManagerConnectionFactory driverManagerConnectionFactory = new DriverManagerConnectionFactory(
				"jdbc:sqlserver://192.168.32.171:1433;DatabaseName=COVI_SMART", connectionProps);

		// 3. Instantiate the Factory of Pooled Objects
		PoolableConnectionFactory poolfactory = new PoolableConnectionFactory(
				driverManagerConnectionFactory, null);
		poolfactory.setValidationQuery(MSSQL_VALIDATION_SQL);

		// 4. Create the Pool with the PoolableConnection objects
		GenericObjectPool<PoolableConnection> connectionPool = null;
		connectionPool = new GenericObjectPool<PoolableConnection>(poolfactory);
		connectionPool.setMinIdle(0);
		connectionPool.setMaxWaitMillis(6000);
		connectionPool.setMaxTotal(8);

		// 5. Set the objectPool to enforces the association (prevent bugs)
		poolfactory.setPool(connectionPool);

		// 6. Get the Driver of the pool and register them
		try {
			Class.forName(DBCP_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PoolingDriver dbcpDriver;
		try {
			dbcpDriver = (PoolingDriver) DriverManager.getDriver(DBCP_NAME);
			dbcpDriver.registerPool("ConnectionFactoryMS", connectionPool);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.dataSource = new PoolingDataSource<PoolableConnection>(connectionPool);
	}

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
}
