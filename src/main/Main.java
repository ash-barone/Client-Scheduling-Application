package main;

import DBAccess.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    /**
     * start begins at the login screen
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        // testing load screen System.out.println("pls");
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * connection to db starts and ends here
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        //start connection
        JDBC.openConnection();

        /*
        // dbacustomer methods check
        DBACustomer.checkDateConversion();
        // update id with one needing deleted DBACustomer.deleteCustomer(8);
        DBACustomer.getAllCustomers();
        DBACustomer.getAllCustomerIds();
        DBACustomer.getCustomerDivisionId(1);
        DBACustomer.getAllDistinctCountries();
        DBACustomer.getDivisionIDFromName("New Mexico");
        DBACustomer.getDivisionsByCountry("U.S");
         */

        // delete test for when cus added from login DBACustomer.deleteCustomer(5);

        /*
        System.out.println("\n" + "\n");
        //dbacontact methods check
        DBAContact.getAllContacts();
        DBAContact.getAllContactNames();
        DBAContact.getAllContactIds();
        DBAContact.getAllAppointmentsForContact(2); //works now!

         */

        //test for delete customer fuctionality
        /*ZonedDateTime start = ZonedDateTime.now().plusMinutes(10);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(30);
        DBAAppointment.addAppointment("test title", "test descrip", "test loc","test type", start, end, "test created by", "test updated by", 1, 1, 1);
        start = start.plusDays(10);
        end = end.plusDays(10);
        DBAAppointment.addAppointment("test title2", "test descrip", "test loc","test type", start, end, "test created by", "test updated by", 1, 1, 1);
        start = start.minusYears(2);
        end = end.minusYears(2);
        DBAAppointment.updateAppointment("title update", "description", "location", "Planning Session", start, end, "test", 1, 1, 3, 1);


         */

        /*DBAAppointment.deleteAppointment(18);
        DBAAppointment.deleteAppointment(19);
        DBACustomer.deleteCustomer(9);

        /*
        dbaappointment methods check
        DBAAppointment.getAllAppointments();
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = ZonedDateTime.now();
        DBAAppointment.deleteAppointment(6);
        DBAAppointment.deleteAppointment(7);
        DBAAppointment.deleteAppointment(8);
        DBAAppointment.addAppointment("test title", "test descrip", "test loc","test type", start, end, "test created by", "test updated by", 1, 1, 1);
        System.out.println("after added appointment" + "\n");
        DBAAppointment.getAllAppointments();
        DBAAppointment.updateAppointment("test 2", "test descrip 2", "test loc 2", "test type 2", start, end, "test updated by 2", 1, 1, 1, 7);
        DBAAppointment.getAllAppointments();

        DBAAppointment.getTotalAppointmentsByTypeAndMonth();

        DBAAppointment.deleteAppointment(16);
        DBAAppointment.deleteAppointment(17);
        LocalDate dateOfAppointment = LocalDate.now();
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(10);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(30);


        DBAAppointment.deleteAppointment(3);
        DBAAppointment.addAppointment("test title", "test descrip", "test loc","test type", start, end, "test created by", "test updated by", 1, 1, 1);

        DBAAppointment.getAllSelectedCustomerAppointmentsByDate(1, dateOfAppointment);
        DBAAppointment.getAppointmentsIn15Minutes();

        DBAAppointment.deleteAppointment(9);
        DBAAppointment.deleteAppointment(10);
        DBAAppointment.getAllAppointments();
        */
        //DBACustomer.getAllCustomers();

        DBAUser.getAllUserIds();



        //launch args
        launch(args);

        //close database
        JDBC.closeConnection();

    }
}
