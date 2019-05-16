package Layers;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {
    private static BasicDataSource dataSource;
    private static BasicDataSource getDataSource()
    {
        if (dataSource == null)
        {
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl("jdbc:derby:mydb;create=true");   //TODO: load from configfile!
            ds.setMinIdle(1);
            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(100);

            dataSource = ds;
        }
        return dataSource;
    }
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
