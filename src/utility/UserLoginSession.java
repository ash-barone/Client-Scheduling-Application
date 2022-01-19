package utility;

import dbaccess.JDBCAccess;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Utility class to store information regarding current user log-in session. Methods include creating a user object for the logged-in user and storage of user time zone and locale.
 */
public class UserLoginSession {

    private static User userLoggedIn;
    private static ZoneId loggedInUserTimeZone;
    private static Locale loggedInUserLocale;
    int loggedInUserId = 0;
    String loggedInUserName = " ";
    String loggedInPassword = " ";

    /**
     * Method to get the user object that is logged-in.
     * @return userLoggedIn to be used to see which user is logged-in
     */
    public static User getUserLoggedIn() {

        return userLoggedIn;
    }

    /**
     * Method to get the logged-in user's locale.
     * @return user locale to determine where user is located
     */
    public static Locale getLoggedInUserLocale() {

        return loggedInUserLocale;
    }

    /**
     * Method to get the time zone for the logged-in user.
     * @return user time zone for CRUD operations
     */
    public static ZoneId getLoggedInUserTimeZone() {

        return loggedInUserTimeZone;
    }

    /**
     * Method for checking input username and password against the database user rows to ensure access should be granted. Creates a new user object to be stored as the logged-in user for time zone and other access
     * @param username the username from the text field on login screen
     * @param pass the password from the text field on login screen
     * @return true or false for if the user was logged in successfully
     */
    public static boolean attemptToLogInUser(String username, String pass){

            int userId = 0;
            String userName = " ";
            String password = " "; //move to outside?

            try {
                String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

                PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

                ps.setString(1, username);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    userId = rs.getInt("User_ID");
                    userName = rs.getString("User_Name");
                    password = rs.getString("Password");

                    //test
                    // System.out.println("before catch and if statement userName: " + userName + " password: " + password);
                }
            } catch(SQLException throwables){
                throwables.printStackTrace();
            }
            if (username.equals(userName) && pass.equals(password)) {
                //User user = new User(userId, userName, password);
                //test
                // System.out.println("ID: " + userId + " username: " + userName + " password: " + password);

                //set current logged-in user info for insert and updates plus locale
                userLoggedIn = new User(userId, userName, password);
                loggedInUserTimeZone = ZoneId.systemDefault();
                loggedInUserLocale = Locale.getDefault();

                //test for user info
                //System.out.println("Logged in: " + userLoggedIn.getUserName() + " User Time Zone: " + loggedInUserTimeZone + " User Locale: " + loggedInUserLocale);
                return true;
            }
            else{
                //test for no user
                //System.out.println(" User Time Zone: " + loggedInUserTimeZone + " User Locale: " + loggedInUserLocale);

                return false;
            }
    }

    /**
     * Method to clear logged-in user info when signing out.
     */
    public static void logUserOff(){

        userLoggedIn = null;
        loggedInUserTimeZone = null;
        loggedInUserLocale = null;

        //test confirm user info cleared
        //System.out.println(" User Time Zone: " + loggedInUserTimeZone + " User Locale: " + loggedInUserLocale);

    }

    /**
     * Method for creating and using a PrintWriter to write log-in attempt info to a file named login_activity.txt.
     * @param attemptToLogInUser bool for whether or not the user was successfully logged in
     * @param username the username entered
     * @throws IOException exception
     */
    public static void logUserActivity(boolean attemptToLogInUser, String username) throws IOException {

        try {
            FileWriter fw = new FileWriter("login_activity.txt",true);
            PrintWriter ps = new PrintWriter(fw);
            LocalDateTime nowTime = LocalDateTime.now();
            ZonedDateTime nowTimeZoned = ZonedDateTime.of(nowTime, ZoneId.systemDefault());
            ZonedDateTime nowTimeUTC = nowTimeZoned.withZoneSameInstant(ZoneOffset.UTC);
            if (username == ""){
                username = "NO USERNAME ENTERED";
            }
            ps.append("Attempted Log-in By User: " + username + " :: Time: " + nowTimeUTC + " UTC :: Successful: " + attemptToLogInUser + "\n");
            ps.close();

            //test
            //System.out.println(" username: " + username + " Time: " + nowTimeUTC + " success?: " + attemptToLogInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
