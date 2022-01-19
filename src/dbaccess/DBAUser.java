package dbaccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is the Database Access management class for CRUD operations of users. Methods included will get lists or returns of different data as required by operations.
 */
public class DBAUser {

    /**
     * Method to get all user ids.
     * @return list of user ids
     */
    public static ObservableList<Integer> getAllUserIds() {

        ObservableList<Integer> allUserIdsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT User_ID from users";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("User_ID");

                //test
                // String userName = rs.getString("User_Name");
                //String password = rs.getString("Password");
                //User user = new User(userId, userName, password);
                //System.out.println(userId + "\n");

                allUserIdsList.add(userId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allUserIdsList;
    }

    /**
     * Method to get username from id.
     * @param userId the id of the user
     * @return the username
     */
    public static String getUserNameFromId(int userId) {

        String userName = " ";
        //int userID; //for testing

        try {
            String sql = "SELECT User_Name FROM users WHERE User_ID = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userName = rs.getString("User_Name");
                //userID = rs.getInt("User_ID"); //for testing

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userName;
    }

    /**
     * Method to get all usernames.
     * @return list of all usernames
     */
    public static ObservableList<String> getAllUserNames() {

        ObservableList<String> allUserNamesList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT User_Name from users";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String userName = rs.getString("User_Name");

                allUserNamesList.add(userName);

                //test
                //System.out.println("User Name: " + userName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned ids list");

        return allUserNamesList;
    }

    /**
     * Method to get user id from name.
     * @param username the name of the user
     * @return the user id
     */
    public static Integer getUserIdFromName(String username) {
        //String userName = " ";
        int userID = 0;

        try {

            String sql = "SELECT User_ID FROM users WHERE User_Name = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //userName = rs.getString("User_Name"); //for testing

                userID = rs.getInt("User_ID");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userID;
    }
}

