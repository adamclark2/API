package com.durp.Model.Managers;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

import java.io.*;
import java.util.*;

/**
 * Manages connections to a database
 *
 * This defines a standard place to store databaseConfig.txt, the file where passwords are stored
 * Use this class to get a connection to the database
 */
public class DatabaseManager {
    private SQLServerDataSource db;
    private Connection connection;

    private static final int c_SERVERNAME=0;
    private static final int c_DATEBASENAME=1;
    private static final int c_USERNAME=2;
    private static final int c_PASSWORD=3;

    /**
     * Create a new DatabaseManager to interact with the SQL Database
     *
     * This will load a preference file from System.getProperty("user.home") named databaseConfig.txt
     * The file is delimited by new lines and contains:
     * <ul>
     *     <li>ServerName</li>
     *     <li>DatabaseName</li>
     *     <li>UserName</li>
     *     <li>Password</li>
     * </ul>
     * Keep the order: ServerName, DatabaseName, UserName, Password and don't user labels
     *
     * Example file:
     * sql.microsoft.com
     * helloWorld
     * admin
     * 1234Password
     *
     * @author Adam
     * @throws IOException
     * @throws SQLServerException
     */
    public DatabaseManager() throws IOException, SQLServerException{
        //Load preference file and create connection/database
        File preferenceFile = new File(System.getProperty("user.home") + File.separator + "databaseConfig.txt");
        Scanner sc = new Scanner(preferenceFile);
        String[] preferences = new String[4];
        for(int i=0;i < 4;i++){
            //Exception handled by caller
            //if we can't get connection info
            //we can't connect to the database
            preferences[i] = sc.nextLine();
        }

        //Init db
        db = new SQLServerDataSource();
        db.setServerName(preferences[c_SERVERNAME]);
        db.setDatabaseName(preferences[c_DATEBASENAME]);
        db.setUser(preferences[c_USERNAME]);
        db.setPassword(preferences[c_PASSWORD]);

        //init connection
        connection = db.getConnection();
    }

    /**
     * get a connection from the database
     *
     * Prepare statements, interact with the database etc.
     *
     * @return
     * @throws SQLException
     * @author Adam
     */
    public Connection getConnection() throws SQLException{
        if(connection == null){
            throw new SQLException("Invalid state connection is null");
        }
        return connection;
    }

    /**
     * Close the connection
     *
     * @author Adam
     */
    public void close() throws SQLException{
        connection.close();
        db = null;
        connection = null;
    }
}
