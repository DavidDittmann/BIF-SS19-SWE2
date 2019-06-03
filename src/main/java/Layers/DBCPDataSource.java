package Layers;

import Misc.ConfigManager;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {
    private static ConfigManager conf;
    private static BasicDataSource dataSource;
    private static BasicDataSource getDataSource()
    {
        try {
            if (dataSource == null)
            {
                conf = ConfigManager.getInstance();
                BasicDataSource ds = new BasicDataSource();
                final String tmp = conf.getConfDatabaseConn();
                ds.setUrl(tmp);
                //ds.setUrl("jdbc:derby:mydb;create=true");   //TODO: load from configfile!
                ds.setMinIdle(1);
                ds.setMaxIdle(10);
                ds.setMaxOpenPreparedStatements(100);

                dataSource = ds;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
