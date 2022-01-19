package model;

/**
 * The User class for creating and accessing User objects and their attributes.
 */
public class User {

    private int userId;
    private String userName;
    private String password;

    /**
     * User object constructor
     * @param userId the user id
     * @param userName the username
     * @param password the user password
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Method to get the user id
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Method to set the user id
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Method to get the username
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Method to set the username
     * @param userName the username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Method to get user password
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to set the user password
     * @param password the user password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
