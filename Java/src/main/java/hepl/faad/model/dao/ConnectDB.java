package hepl.faad.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;

public class ConnectDB {
    private static Connection conn = null;
    public Connection getConn() {
        return conn;
    }

    public ConnectDB()
    {
        try
        {
            if (conn == null || conn.isClosed())
            {
                Properties properties = new Properties();

                FileInputStream input = new FileInputStream("serveurachat.properties");
                properties.load(input);
                input.close();

                String sCon = properties.getProperty("DB_URL");
                String sUser = properties.getProperty("DB_USER");
                String sPwd = properties.getProperty("DB_PASSWORD");

                Class.forName("com.mysql.cj.jdbc.Driver");

                conn = DriverManager.getConnection(sCon, sUser, sPwd);
            }
        }
        catch (ClassNotFoundException | SQLException | IOException ex)
        {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void close()
    {
        try
        {
            if (conn != null)
            {
                conn.close();
                System.out.println("Closing DB connection");
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
