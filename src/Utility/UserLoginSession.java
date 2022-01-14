package Utility;

import DBAccess.JDBC;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;

/**
 * user login utility used to manage logged in user info like time zone, locale, and user id for CRUD operations
 */
public class UserLoginSession {

    private static User userLoggedIn;
    private static ZoneId loggedInUserTimeZone;
    private static Locale loggedInUserLocale;
    int loggedInUserId = 0;
    String loggedInUserName = " ";
    String loggedInPassword = " ";

    /**
     *
     * @return userLoggedIn to be used to see which user is logged in
     */
    public static User getUserLoggedIn() {

        return userLoggedIn;
    }

    /**
     *
     * @return user locale to determine where user is located
     */
    public static Locale getLoggedInUserLocale() {

        return loggedInUserLocale;
    }

    /**
     *
     * @return user time zone for CRUD operations
     */
    public static ZoneId getLoggedInUserTimeZone() {

        return loggedInUserTimeZone;
    }

    /**
     *
     * @param username the username from the text field on login screen
     * @param pass the password from the text field on login screen
     * @return true or false for whether or not the user was logged in successfully
     */
    public static boolean attemptToLogInUser(String username, String pass){

        //TODO printwriter for storing each log in attempt
            int userId = 0;
            String userName = " ";
            String password = " "; //move to outside?

            try {
                //String sql = "SELECT * FROM users WHERE User_Name LIKE '%1%' AND Password LIKE '%2%'";

                PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");

                ps.setString(1, username);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    userId = rs.getInt("User_ID");
                    userName = rs.getString("User_Name");
                    password = rs.getString("Password");

                    System.out.println("before catch and if statement userName: " + userName + " password: " + password);
               /* if (username == userName && pass == password) {
                    User user = new User(userId, userName, password);
                    System.out.println("ID: " + userId + "Name: " + userName + "password: " + password);
                    return true;
                }*/
                }
            } catch(SQLException throwables){
                throwables.printStackTrace();
            }
            if (username.equals(userName) && pass.equals(password)) {
                User user = new User(userId, userName, password);
                System.out.println("ID: " + userId + " username: " + userName + " password: " + password);

                //set current logged in user info for insert and updates plus locale stuff i might use?
                userLoggedIn = new User(userId, userName, password);
                loggedInUserTimeZone = ZoneId.systemDefault();
                loggedInUserLocale = Locale.getDefault();

                //test for user info
                System.out.println("Logged in: " + userLoggedIn.getUserName() + " User Time Zone: " + loggedInUserTimeZone + " User Locale: " + loggedInUserLocale);
                return true;
            }
            else{
                //test for no user
                System.out.println(" User Time Zone: " + loggedInUserTimeZone + " User Locale: " + loggedInUserLocale);

                return false;
            }
    }

    /**
     * method to clear logged in user info
     */
    public static void logUserOff(){

        userLoggedIn = null;
        loggedInUserTimeZone = null;
        loggedInUserLocale = null;

        //test confirm user info cleared
        System.out.println(" User Time Zone: " + loggedInUserTimeZone + " User Locale: " + loggedInUserLocale);

    }
}
