package repositories;

import Utils.Utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Represents a database for managing academic publications and related data.
 * This is a singleton class.
 * */
public class Database {

    private static Database instance;

    private Properties credentials = new Properties();
    private String username = "";
    private String password = "";
    private String database = "";
    private String connectionString = "";
    private Connection connection;
    private static String credentialsFile = "src/credentials.properties";

    /**
     * Database class will take the credentials from the user and will provide the connection.
     * */
    private Database() {
        try {
            if(Utils.isValidString(credentialsFile)) {
                InputStream stream = new FileInputStream(credentialsFile);
                credentials.load(stream);
                username = credentials.getProperty("username");
                password = credentials.getProperty("password");
                database = credentials.getProperty("database");
                connectionString = credentials.getProperty("connectionString");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a singleton instance of the database for managing academic publications and related data.
     * @return The singleton instance of the database.
     * */
    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }

        return instance;
    }

    /**
     * Establishes a connection to the academic publications and related data database.
     * @return A connection object representing the connection to the database.
     * */
    public Connection connectDatabase() {
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            Statement statement = connection.createStatement();
            statement.execute("use " + database + ";");     // using specific database
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the connection to the academic publications and related data database.
     * @return True if the disconnection is successful, false otherwise.
     * */
    public boolean disconnectDatabase() {
        try {
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}