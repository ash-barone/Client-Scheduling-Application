package DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * class for access methods for contacts
 */
public class DBAContact {

    /**
     * method to get all contacts
     * @return all contacts
     */
    public static ObservableList<Contact> getAllContacts() {

        ObservableList<Contact> allContactsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                Contact contact = new Contact(contactId, contactName, contactEmail);
                allContactsList.add(contact);

                //test
                System.out.println("Contact ID: " + contactId + " Name: " + contactName + " Contact Email: " + contactEmail);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allContactsList;
    }

    /**
     * method to get all contact names
     * @return all contact names
     */
    public static ObservableList<String> getAllContactNames(){
        ObservableList<String> allContactNamesList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Contact_Name from contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String contactName = rs.getString("Contact_Name");

                allContactNamesList.add(contactName);

                System.out.println("Contact Name: " + contactName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allContactNamesList;
    }

    public static int getContactIdFromName(String contactName) throws SQLException {

        int contactId = 0;
        try {
            //sql statement to select division id from customer table where customer id is specific thing
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Contact_Name, Contact_ID FROM contacts WHERE Contact_Name = ?");

            ps.setString(1, contactName);

            //int divisionId;
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contactId = rs.getInt("Contact_ID");

                //test
                System.out.println("Contact ID: " + contactId + " Contact Name: " + contactName);
            }
            ;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactId;
    }

    /**
     * method to get all contact ids
     * @return all contact ids
     */
    public static ObservableList<Integer> getAllContactIds(){
        ObservableList<Integer> allContactIdsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Contact_ID from contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");

                allContactIdsList.add(contactId);

                System.out.println("Contact ID: " + contactId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allContactIdsList;
    }

    /**
     * method to get all appointments for a specific contact
     * @param contactID
     * @return list of all appts for that contact
     */
    public static ObservableList<String> getAllAppointmentsForContact(int contactID){
        ObservableList<String> allAppointmentsForContactList = FXCollections.observableArrayList();
        try {
            // did not work for setString() String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments WHERE Contact_ID = " + contactID);

            //works for ? in sql statement NVM just changed string structure
            //ps.setInt(1, contactID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String apptType = rs.getString("Type");
                String apptStart = rs.getString("Start");
                String apptEnd = rs.getString("End");
                String customerId = rs.getString("Customer_ID");
                String contactId = rs.getString("Contact_ID");

                String eachAppt = "Appointment ID: " + apptId + " Title: " + apptTitle + " Description: " + apptDescription + " Type: " + apptType + " Start: " + apptStart + " End: " + apptEnd + " Customer ID: " + customerId + " Contact ID: " + contactId + "\n";
                System.out.println("Appointment info: " + eachAppt);

                allAppointmentsForContactList.add(eachAppt);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allAppointmentsForContactList;
    }

}
