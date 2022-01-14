package DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAUser {

    public static ObservableList<User> getAllUser() {

        ObservableList<User> allUsersList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from users";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                User user = new User(userId, userName, password);
                allUsersList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allUsersList;
    }

    public static ObservableList<Integer> getAllUserIds() {

        ObservableList<Integer> allUserIdsList = FXCollections.observableArrayList();
        //= FXCollections.observableArrayList();

        try {
            String sql = "SELECT User_ID from users";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("User_ID");
               // String userName = rs.getString("User_Name");
                //String password = rs.getString("Password");

                //User user = new User(userId, userName, password);
                System.out.println(userId + "\n");
                allUserIdsList.add(userId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allUserIdsList;
    }

    public static String getUserNameFromId(int userId) {

        String userName = " ";
        int userID;
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT User_Name, User_ID FROM users WHERE User_ID = ?");
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userName = rs.getString("User_Name");
                userID = rs.getInt("User_ID");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userName;
    }

    public static ObservableList<String> getAllUserNames() {

        ObservableList<String> allUserNamesList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT User_Name from users";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String userName = rs.getString("User_Name");

                allUserNamesList.add(userName);

                //test
                System.out.println("User Name: " + userName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned ids list");

        return allUserNamesList;
    }

    public static Integer getUserIdFromName(String username) {
        String userName = " ";
        int userID = 0;
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT User_Name, User_ID FROM users WHERE User_Name = ?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userName = rs.getString("User_Name");
                userID = rs.getInt("User_ID");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userID;
    }
}

