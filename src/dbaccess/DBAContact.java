package dbaccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is the Database Access management class for CRUD operations of contacts. Included are methods to get specific lists of contacts and get lists of specific data as required for operations.
 */
public class DBAContact {

    /**
     * Method to get all contact names.
     * @return all contact names list
     */
    public static ObservableList<String> getAllContactNames(){
        ObservableList<String> allContactNamesList = FXCollections.observableArrayList();

        try {
            //sql statement to get contact names
            String sql = "SELECT Contact_Name from contacts";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String contactName = rs.getString("Contact_Name");

                allContactNamesList.add(contactName);

                //test
                //System.out.println("Contact Name: " + contactName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allContactNamesList;
    }

    /**
     * Method to get contact id from name.
     * @param contactName the name of the contact
     * @return contact id int
     */
    public static int getContactIdFromName(String contactName) {

        int contactId = 0;

        try {
            //sql statement to get contact id from name
            String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setString(1, contactName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contactId = rs.getInt("Contact_ID");

                //test
                //System.out.println("Contact ID: " + contactId + " Contact Name: " + contactName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactId;
    }

    /**
     * Method to get contact name from an id.
     * @param contactId the id of the contact
     * @return the name of the contact
     */
    public static String getContactNameFromId(int contactId) {

        String contactName = " ";

        try {
            //sql statement to get contact name from id
            String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setInt(1, contactId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contactName = rs.getString("Contact_Name");

                //test
                //System.out.println("Contact ID: " + contactId + " Contact Name: " + contactName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactName;
    }

    /**
     * Method to get all appointments for a specific contact.
     * @param contactID the id of the contact
     * @return list of all appts for that contact
     */
    public static ObservableList<String> getAllAppointmentsForContact(int contactID) {
        ObservableList<String> allAppointmentsForContactList = FXCollections.observableArrayList();

        //String contactName = DBAContact.getContactNameFromId(contactID);

        try {
            //sql statement to select appts for specific contact
            String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setInt(1, contactID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String apptType = rs.getString("Type");
                String apptStart = rs.getString("Start");
                String apptEnd = rs.getString("End");
                String customerId = rs.getString("Customer_ID");

                String eachAppt = "\nAppointment ID: " + apptId + " Title: " + apptTitle + " Description: " + apptDescription + " Type: " + apptType + " Start: " + apptStart + " End: " + apptEnd + " Customer ID: " + customerId;
                //System.out.println("Appointment info: " + eachAppt);

                allAppointmentsForContactList.add(eachAppt);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (allAppointmentsForContactList.isEmpty()){
            String noAppts = "\nContact does not have any appointments currently scheduled.";
            allAppointmentsForContactList.add(noAppts);
        }
        return allAppointmentsForContactList;
    }

}
