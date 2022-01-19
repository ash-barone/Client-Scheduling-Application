package dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *  This class is the main JDBC access class which connects the application to the chosen database. This allows access for all other DBA classes and is necessary for application to run.
 */
public abstract class JDBCAccess {

    //connect to database from code repository
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";// LOCAL
    //private static final String jdbcUrl = protocol + vendor + location + databaseName;// UTC if I was in the .23 version of oracle
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static final String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     * Method for starting the connection to the db.
     */
    public static void openConnection() {
        try {
            Class.forName(driver); // Locate Driver

            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object

            System.out.println("Connection successful!");
        }
        catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Method to get the connection.
     * @return the connection to the db
     */
    public static Connection getConnection(){
        return connection;
    }

    /**
     * Method to end the connection to the db.
     */
    public static void closeConnection() {
        try {
            connection.close();

            System.out.println("Connection closed!");
        }
        catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
