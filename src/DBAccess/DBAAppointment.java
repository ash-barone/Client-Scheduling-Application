package DBAccess;

import Utility.UserLoginSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * class for managing db access for appointments and crud
 */
public class DBAAppointment {

    /**
     * method to get all appointment in a list for use in tableview
     * @return all appointments list
     */
    public static ObservableList<Appointment> getAllAppointments() {

        ObservableList<Appointment> allAppointmentsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String apptLocation = rs.getString("Location");
                String apptType = rs.getString("Type");
                Timestamp apptStartDateTime = rs.getTimestamp("Start");
                Timestamp apptEndDateTime = rs.getTimestamp("End");
                int apptCustomerId = rs.getInt("Customer_ID");
                int apptUserId = rs.getInt("User_ID");
                int apptContactId = rs.getInt("Contact_ID");
                String apptContactName = rs.getString("Contact_Name");

                Appointment appointment = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime, apptCustomerId, apptUserId, apptContactId, apptContactName);
                allAppointmentsList.add(appointment);

                //test
                System.out.println(" Get All Appointments ::: Appt ID: " + apptId + " Appt Title: " + apptTitle + "Appt Description: " + apptDescription + "\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allAppointmentsList;
    }

    /**
     * method to update existing appt
     * @param apptTitle the appt title
     * @param apptDescription the appt description
     * @param apptLocation the appt location
     * @param apptType the appt type
     * @param apptStart the appt start
     * @param apptEnd the appt end
     * @param lastUpdatedBy who updated appt last
     * @param customerID the appt customer id
     * @param userID the appt user id
     * @param contactID the appt contact id
     * @param apptID the appt id
     * @return bool for whether the appt was updated or not
     */
    public static boolean updateAppointment(String apptTitle, String apptDescription, String apptLocation, String apptType, ZonedDateTime apptStart, ZonedDateTime apptEnd, String lastUpdatedBy, int customerID, int userID, int contactID, int apptID){

        try{
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement("UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?");

            ps.setString(1, apptTitle);
            ps.setString(2, apptDescription);
            ps.setString(3, apptLocation);
            ps.setString(4, apptType);
            ps.setString(5, apptStart.format(dateTimeFormat));
            ps.setString(6, apptEnd.format(dateTimeFormat));
            ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat));
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, customerID);
            ps.setInt(10, userID);
            ps.setInt(11, contactID);
            ps.setInt(12, apptID);


            ps.executeUpdate();
            System.out.println("Updated appt: " + apptID + "\n");

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //return true;
    }

    /**
     * method to add appointment
     * @param apptTitle the appt title
     * @param apptDescription the appt description
     * @param apptLocation the appt location
     * @param apptType the appt type
     * @param apptStart the appt start
     * @param apptEnd the appt end
     * @param createdBy who created appt
     * @param lastUpdatedBy who updated appt
     * @param customerID the appt customer id
     * @param userID the appt user id
     * @param contactID the appt contact id
     * @return bool for whether or not the appt was added
     */
    public static boolean addAppointment(String apptTitle, String apptDescription, String apptLocation, String apptType, ZonedDateTime apptStart, ZonedDateTime apptEnd, String createdBy, String lastUpdatedBy, int customerID, int userID, int contactID){
        try{
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement("INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, apptTitle);
            ps.setString(2, apptDescription);
            ps.setString(3, apptLocation);
            ps.setString(4, apptType);
            ps.setString(5, apptStart.format(dateTimeFormat));
            ps.setString(6, apptEnd.format(dateTimeFormat));
            ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat));
            ps.setString(8, createdBy);
            ps.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat));
            ps.setString(10, lastUpdatedBy);
            ps.setInt(11, customerID);
            ps.setInt(12, userID);
            ps.setInt(13, contactID);


            ps.executeUpdate();
            //System.out.println("Added appt::: Appt title: " + apptTitle + " appt Descrip: " +apptDescription + "\n");

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //return true;
    }

    /**
     * method to delete appointments
     * @param apptID the appt id to search for delete
     * @return bool for whether or not it worked
     */
    public static boolean deleteAppointment(int apptID){

        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");

            ps.setInt(1, apptID);

            ps.executeUpdate();
            System.out.println("Appointment Deleted: " + apptID + "\n");
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //return true;
    }

    /**
     * method to get appointments within 15 minutes of login
     * @return
     */
    public static ObservableList<Appointment> getAppointmentsIn15Minutes(){
        //TODO figure this one out lol appt in 15 minutes
        ObservableList<Appointment> appointmentsIn15MinutesList = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int userID = UserLoginSession.getUserLoggedIn().getUserId();
        System.out.println("User ID: " + userID + "\n");
        LocalDateTime timeNow = LocalDateTime.now();
        ZonedDateTime timeNowForUser = timeNow.atZone(UserLoginSession.getLoggedInUserTimeZone());
        ZonedDateTime timeNowUTC = timeNowForUser.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime timeNowUTCAdd15Minutes = timeNowUTC.plusMinutes(15);
        String start = timeNowUTC.format(dateTimeFormatter);
        String end = timeNowUTCAdd15Minutes.format(dateTimeFormatter);


        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE User_ID = ? AND Start BETWEEN ? AND ?");

            ps.setInt(1, userID);
            //LocalDateTime timeNow = LocalDateTime.now();
            //ZonedDateTime timeNowForUser = timeNow.atZone(UserLoginSession.getLoggedInUserTimeZone());
            ps.setString(2, start);
            ps.setString(3, end);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String apptLocation = rs.getString("Location");
                String apptType = rs.getString("Type");
                Timestamp apptStartDateTime = rs.getTimestamp("Start");
                Timestamp apptEndDateTime = rs.getTimestamp("End");
                int apptCustomerId = rs.getInt("Customer_ID");
                int apptUserId = rs.getInt("User_ID");
                int apptContactId = rs.getInt("Contact_ID");
                String apptContactName = rs.getString("Contact_Name");

                Appointment eachAppt = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime/*, apptCreateDate, apptCreatedBy, apptLastUpdate, apptLastUpdatedBy*/, apptCustomerId, apptUserId, apptContactId, apptContactName);
                appointmentsIn15MinutesList.add(eachAppt);
                System.out.println("Appointment in 15 Minutes ID: " + apptId + " Start: " + apptStartDateTime );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentsIn15MinutesList;

    }

    /**
     * method to get all appointment within a date range for use in tableview for month and week display
     * @param start the start date
     * @param end the end date
     * @return list of appointments for tableview by month or week
     */
    public static ObservableList<Appointment> getAppointmentsByDateRange(ZonedDateTime start, ZonedDateTime end){
        ObservableList<Appointment> appointmentsByDateRangeList = FXCollections.observableArrayList();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Start BETWEEN ? AND ?");

            ps.setString(1, String.valueOf(start));
            ps.setString(2, String.valueOf(end));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String apptLocation = rs.getString("Location");
                String apptType = rs.getString("Type");
                Timestamp apptStartDateTime = rs.getTimestamp("Start");
                Timestamp apptEndDateTime = rs.getTimestamp("End");
                int apptCustomerId = rs.getInt("Customer_ID");
                int apptUserId = rs.getInt("User_ID");
                int apptContactId = rs.getInt("Contact_ID");
                String apptContactName = rs.getString("Contact_Name"); //when using join for contact name

                Appointment eachAppointment = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime,apptCustomerId, apptUserId, apptContactId, apptContactName);
                appointmentsByDateRangeList.add(eachAppointment);
                System.out.println("Each Appt :" + apptId + " Appt Start: " + apptStartDateTime + " Appt End: " + apptEndDateTime);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsByDateRangeList;
    }

    /**
     * method to find all appointments for a select customer for checking before deleting a customer
     * @param customerID the customer to check for
     * @return list of all appts for customer
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllSelectedCustomerAppointments(int customerID) throws SQLException {

        ObservableList<Appointment> selectedCustomerAppointments = FXCollections.observableArrayList();

        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Customer_ID = ?");

        //ps.setString(1, dateOfAppointment.toString());
        ps.setInt(1, customerID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int apptId = rs.getInt("Appointment_ID");
            String apptTitle = rs.getString("Title");
            String apptDescription = rs.getString("Description");
            String apptLocation = rs.getString("Location");
            String apptType = rs.getString("Type");
            Timestamp apptStartDateTime = rs.getTimestamp("Start");
            Timestamp apptEndDateTime = rs.getTimestamp("End");
            /*Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
            String apptCreatedBy = rs.getString("Created_By") ;
            Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
            String apptLastUpdatedBy = rs.getString("Last_Updated_By");*/
            int apptCustomerId = rs.getInt("Customer_ID");
            int apptUserId = rs.getInt("User_ID");
            int apptContactId = rs.getInt("Contact_ID");
            String apptContactName = rs.getString("Contact_Name");

            Appointment eachAppt = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime/*, apptCreateDate, apptCreatedBy, apptLastUpdate, apptLastUpdatedBy*/, apptCustomerId, apptUserId, apptContactId, apptContactName);
            selectedCustomerAppointments.add(eachAppt);

            //test
            // System.out.println("Appointment Start: " + apptStartDateTime + " Appointment ID: " + apptId + "\n");
        }
        return selectedCustomerAppointments;
    }

    /**
     * method to check for all customer appointments for specific date for use when adding new appointment
     * @param customerID the customer id to check appointments for
     * @param dateOfAppointment the date to check appointments for
     * @return list of all appointments on the same date as the input appointment
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllSelectedCustomerAppointmentsByDate(int customerID, LocalDate dateOfAppointment) throws SQLException {

        ObservableList<Appointment> selectedCustomerAppointmentByDateList = FXCollections.observableArrayList();

        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments JOIN contacts ON appointment.Contact_ID = contacts.Contact_ID WHERE datediff(appointments.Start, ?) = 0 AND Customer_ID = ?");

        ps.setString(1, dateOfAppointment.toString());
        ps.setInt(2, customerID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int apptId = rs.getInt("Appointment_ID");
            String apptTitle = rs.getString("Title");
            String apptDescription = rs.getString("Description");
            String apptLocation = rs.getString("Location");
            String apptType = rs.getString("Type");
            Timestamp apptStartDateTime = rs.getTimestamp("Start");
            Timestamp apptEndDateTime = rs.getTimestamp("End");
            /*Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
            String apptCreatedBy = rs.getString("Created_By") ;
            Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
            String apptLastUpdatedBy = rs.getString("Last_Updated_By");*/
            int apptCustomerId = rs.getInt("Customer_ID");
            int apptUserId = rs.getInt("User_ID");
            int apptContactId = rs.getInt("Contact_ID");
            String apptContactName = rs.getString("Contact_Name");

            Appointment eachAppt = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime/*, apptCreateDate, apptCreatedBy, apptLastUpdate, apptLastUpdatedBy*/, apptCustomerId, apptUserId, apptContactId, apptContactName);
            selectedCustomerAppointmentByDateList.add(eachAppt);
            System.out.println("Appointment Start: " + apptStartDateTime + " Appointment ID: " + apptId + "\n");
        }
        return selectedCustomerAppointmentByDateList;
    }

    public static ObservableList<String> getAllApptTypes() {
        ObservableList<String> allApptTypes = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT DISTINCT Type FROM appointments");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String apptType = rs.getString("Type");

                allApptTypes.add(apptType);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allApptTypes;
    }
        /**
     * method to return a report of appointments by type and month
     * @return
     */
    public static ObservableList<String> getTotalAppointmentsByTypeAndMonth(){
        ObservableList<String> totalAppointmentsByTypeAndMonth = FXCollections.observableArrayList();
        totalAppointmentsByTypeAndMonth.add("Appointments By Type and Month Report: \n");

        try{
            PreparedStatement psType = JDBC.getConnection().prepareStatement("SELECT Type, COUNT(Type) FROM appointments GROUP BY Type");

            PreparedStatement psMonth = JDBC.getConnection().prepareStatement("SELECT MONTHNAME(Start), COUNT(MONTH(Start)) FROM appointments GROUP BY MONTHNAME(Start)");

            ResultSet rsType = psType.executeQuery();
            ResultSet rsMonth = psMonth.executeQuery();

            while (rsType.next()) {
                String eachType = "Appointment Type: " + rsType.getString("Type") + "\n" + "Number of " + rsType.getString("Type") + " Appointments: " + rsType.getString("COUNT(Type)") + "\n";
                totalAppointmentsByTypeAndMonth.add(eachType);

                //test
                System.out.println(eachType);
            }

            while(rsMonth.next()) {
                String eachMonth = "Appointment Month: " + rsMonth.getString("MONTHNAME(start)") + "\n" + "Number of " + rsMonth.getString("MONTHNAME(Start)") + " Appointments: " + rsMonth.getString("COUNT(MONTH(Start))") +"\n";
                totalAppointmentsByTypeAndMonth.add(eachMonth);

                //test
                System.out.println(eachMonth);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return totalAppointmentsByTypeAndMonth;
    }

    /**
     * method to delete all appointments for a selected customer
     * @param customerID the selected customer is
     * @return all appointments for the selected customer
     */
    public static boolean deleteAllSelectedCustomerAppointments(int customerID){

        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement("DELETE FROM appointments WHERE Customer_ID = ?");

            ps.setInt(1, customerID);

            ps.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //return true;

    }

}
