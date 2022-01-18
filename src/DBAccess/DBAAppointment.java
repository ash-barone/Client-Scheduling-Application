package DBAccess;

import Utility.UserLoginSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

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
            //sql statement to get all appts
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
                //System.out.println(" Get All Appointments ::: Appt ID: " + apptId + " Appt Title: " + apptTitle + "Appt Description: " + apptDescription + "\n");
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

            //sql statement to update appt
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

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

            //test
            // System.out.println("Updated appt: " + apptID + "\n");

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
     * @return bool for if the appt was added
     */
    public static boolean addAppointment(String apptTitle, String apptDescription, String apptLocation, String apptType, ZonedDateTime apptStart, ZonedDateTime apptEnd, String createdBy, String lastUpdatedBy, int customerID, int userID, int contactID){
        try{
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement to add new appt
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

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
     * method to delete all appointments for a selected customer
     * @param customerID the selected customer is
     * @return all appointments for the selected customer
     */
    public static boolean deleteAllSelectedCustomerAppointments(int customerID){

        try{
            //sql statement to delete appts for specific customer id
            String sql = "DELETE FROM appointments WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customerID);

            ps.executeUpdate();

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
     * @return bool for if it worked
     */
    public static boolean deleteAppointment(int apptID){

        try{
            //sql statement for deleting appt
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, apptID);

            ps.executeUpdate();

            //test
            // System.out.println("Appointment Deleted: " + apptID + "\n");

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //return true;
    }

    /**
     * method to get appointments within 15 minutes of login
     *
     */
    public static ObservableList<Appointment> getAppointmentsIn15Minutes(){

        ObservableList<Appointment> appointmentsIn15MinutesList = FXCollections.observableArrayList();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int userID = UserLoginSession.getUserLoggedIn().getUserId();

        //test
        //System.out.println("User ID: " + userID + "\n");

        //get now time and build to convert to string for sql statement
        LocalDateTime timeNow = LocalDateTime.now(); //local time now
        ZonedDateTime timeNowForUser = timeNow.atZone(UserLoginSession.getLoggedInUserTimeZone()); //local time in user time zone
        ZonedDateTime timeNowUTC = timeNowForUser.withZoneSameInstant(ZoneOffset.UTC); //offset time above to UTC
        ZonedDateTime timeNowUTCAdd15Minutes = timeNowUTC.plusMinutes(15); //add 15 minutes to above
        String start = timeNowUTC.format(dateTimeFormatter); //format date time to now time in UTC
        String end = timeNowUTCAdd15Minutes.format(dateTimeFormatter); // format date time for 15 minutes added to now time UTC


        try{
            //sql statement to select appointments within a 15 minutes range
            String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE User_ID = ? AND Start BETWEEN ? AND ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, userID);
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

                //test
                // System.out.println("Appointment in 15 Minutes ID: " + apptId + " Start: " + apptStartDateTime );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentsIn15MinutesList;

    }

    /**
     * method to get all appointment within a date range for use in tableview for month and week display
     *
     * @return list of appointments for tableview by month or week
     */
    public static ObservableList<Appointment> getAppointmentsByDateRangeWeek(){
        ObservableList<Appointment> appointmentsByDateRangeWeekList = FXCollections.observableArrayList();

        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try{
            //sql statement to select appts within a date range for diff tableviews
            String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE YEAR(Start) = YEAR(CURDATE()) AND MONTH(Start) = MONTH(CURDATE()) AND WEEK(Start) = WEEK(CURDATE());";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //ps.setString(1, String.valueOf(start));
            //ps.setString(2, String.valueOf(end));

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
                appointmentsByDateRangeWeekList.add(eachAppointment);

                //test
                // System.out.println("Each Appt :" + apptId + " Appt Start: " + apptStartDateTime + " Appt End: " + apptEndDateTime);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsByDateRangeWeekList;
    }

    /**
     * method to get all appointment within a date range for use in tableview for month and week display
     *
     * @return list of appointments for tableview by month or week
     */
    public static ObservableList<Appointment> getAppointmentsByDateRangeMonth(){
        ObservableList<Appointment> appointmentsByDateRangeMonthList = FXCollections.observableArrayList();

        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try{
            //sql statement to select appts within a date range for diff tableviews
            String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE YEAR(Start) = YEAR(CURDATE()) AND MONTH(Start) = MONTH(CURDATE())";//Start BETWEEN ? AND ?";

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
                String apptContactName = rs.getString("Contact_Name"); //when using join for contact name

                Appointment eachAppointment = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime,apptCustomerId, apptUserId, apptContactId, apptContactName);
                appointmentsByDateRangeMonthList.add(eachAppointment);

                //test
                // System.out.println("Each Appt :" + apptId + " Appt Start: " + apptStartDateTime + " Appt End: " + apptEndDateTime);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsByDateRangeMonthList;
    }

    /**
     * method to find all appointments for a select customer for checking before deleting a customer
     * @param customerID the customer to check for
     * @return list of all appts for customer
     * @throws SQLException exception
     */
    public static ObservableList<Appointment> getAllSelectedCustomerAppointments(int customerID) throws SQLException {

        ObservableList<Appointment> selectedCustomerAppointments = FXCollections.observableArrayList();

        //sql statement to get all selected customer appts
        String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

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
     * @param apptCustomerID the customer id to check appointments for
     * @param apptStartDate the date to check appointments for
     * @return list of all appointments on the same date as the input appointment
     * @throws SQLException exception
     */
    public static ObservableList<Appointment> getAllSelectedCustomerAppointmentsByDate(int apptID, int apptCustomerID, LocalDate apptStartDate) throws SQLException {

        ObservableList<Appointment> selectedCustomerAppointmentByDateList = FXCollections.observableArrayList();

        //sql statement for getting all selected customer appointments by date
        String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Customer_ID = ? AND DATEDIFF(appointments.Start, ?) = 0 AND appointments.Appointment_ID <> ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, apptCustomerID);
        ps.setString(2, apptStartDate.toString());
        ps.setInt(3, apptID);

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

            Appointment eachAppt = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, apptStartDateTime, apptEndDateTime/*, apptCreateDate, apptCreatedBy, apptLastUpdate, apptLastUpdatedBy*/, apptCustomerId, apptUserId, apptContactId, apptContactName);

            selectedCustomerAppointmentByDateList.add(eachAppt);

            //test
            // System.out.println("Appointment Start: " + apptStartDateTime + " Appointment ID: " + apptId + "\n");
        }
        return selectedCustomerAppointmentByDateList;
    }

    /**
     * method to check for overlapping appointments time when adding new appointment
     * @param apptCustomerID the customer id
     * @param apptStartDate the start date for appointment
     * @param apptStartUser the start time for appointment in user time zone
     * @param apptEndUser the end time for appointment in user time zone
     * @return boolean
     * @throws SQLException exception
     */
    public static Boolean checkForOverlappingCustomerAppointments(int apptID, int apptCustomerID, LocalDate apptStartDate, LocalDateTime apptStartUser, LocalDateTime apptEndUser) throws SQLException {
        ObservableList<Appointment> allCustomerAppointmentsByDate = getAllSelectedCustomerAppointmentsByDate(apptID, apptCustomerID, apptStartDate);

        for (Appointment appointment : allCustomerAppointmentsByDate) {
            //set var for appt start and end for each appointment in list to compare to a new appts start and end
            LocalDateTime existingApptStart = appointment.getApptStartDateTime().toLocalDateTime();
            LocalDateTime existingApptEnd = appointment.getApptEndDateTime().toLocalDateTime();

            //check for any overlap in new appt with existing appts
            if ((existingApptEnd.isAfter(apptStartUser) & existingApptEnd.isBefore(apptEndUser)) || (existingApptEnd.isAfter(apptEndUser) & existingApptStart.isBefore(apptStartUser)) || (existingApptStart.isAfter(apptStartUser) & existingApptStart.isBefore(apptEndUser)) || existingApptStart.isEqual(apptStartUser) || existingApptEnd.isEqual(apptEndUser)) {
                return false; //there is overlap
            }
            else {
                return true; //there is no overlap
            }
        }
        return true;
    }

    /**
     * method to get all distinct appt types
     *
     */
    public static ObservableList<String> getAllApptTypes() {
        ObservableList<String> allApptTypes = FXCollections.observableArrayList();

        try {
            //sql statement to get each distinct appt type for combo box
            String sql = "SELECT DISTINCT Type FROM appointments";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

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
     *
     */
    public static ObservableList<String> getTotalAppointmentsByTypeAndMonth(){
        ObservableList<String> totalAppointmentsByTypeAndMonth = FXCollections.observableArrayList();

        totalAppointmentsByTypeAndMonth.add("Appointments By Distinct Type and Month:\n");

        try{
            //sql statement for getting a count of each type of appointment
            String sql = "SELECT DISTINCT Type, COUNT(Type), MONTHNAME(Start) FROM appointments GROUP BY MONTHNAME(Start), Type;";

            //sql statement for getting count of all appointments in each month // no longer needed because of report cleanup
            //String sql2 = "SELECT MONTHNAME(Start), COUNT(MONTH(Start)) FROM appointments GROUP BY MONTHNAME(Start)";

            PreparedStatement psType = JDBC.getConnection().prepareStatement(sql);

            //PreparedStatement psMonth = JDBC.getConnection().prepareStatement(sql2);

            ResultSet rsType = psType.executeQuery();

            //ResultSet rsMonth = psMonth.executeQuery();

            while (rsType.next()) {
                String apptType = rsType.getString("Type");
                String apptTypeCount = rsType.getString("COUNT(Type)");
                String apptMonthName = rsType.getString("MONTHNAME(Start)");

                String eachType = "\nMonth: " + apptMonthName + "\nAppointment Type: " + apptType + "\n" + "Number of " + apptType + " Appointments in " + apptMonthName + ": " + apptTypeCount + "\n";

                totalAppointmentsByTypeAndMonth.add(eachType);

                //test
                //System.out.println(eachType);
            }

            /*while(rsMonth.next()) {
                String monthName = rsMonth.getString("MONTHNAME(start)");
                String monthNameAppts = rsMonth.getString("COUNT(MONTH(Start))");

                String eachMonth = "\nAppointment Month: " + monthName + "\n" + "Number of " + monthName + " Appointments: " + monthNameAppts +"\n";

                totalAppointmentsByTypeAndMonth.add(eachMonth);

                //test
                //System.out.println(eachMonth);
            }*/

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return totalAppointmentsByTypeAndMonth;
    }
}
