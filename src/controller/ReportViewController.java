package controller;

import dbaccess.DBAAppointment;
import dbaccess.DBAContact;
import dbaccess.DBACustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * This class is the controller for the Report View screen. Included are methods for showing specific reports when its respective button is clicked.
 * Navigation back to the Main Menu is also included.
 */
public class ReportViewController {

    @FXML
    private TextArea reportTxtArea;

    /**
     * Method for navigating back to the Main Menu.
     * @param event The event of clicking on the Back button.
     * @throws Exception
     */
    @FXML
    void onActionBackToMainMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method for displaying the Type and Month Appointments report in the text area.
     * @param event The event of clicking on the Appointment By Type and Month button.
     */
    @FXML
    void onActionShowTypeAndMonthReport(ActionEvent event) {

        reportTxtArea.clear();

        ObservableList<String> appointmentsByTypeAndMonth = DBAAppointment.getTotalAppointmentsByTypeAndMonth();

        //format report string
        String report = String.valueOf(appointmentsByTypeAndMonth);
        report = report.substring(1, report.length() - 1);
        report = report.replaceAll(",", "");
        reportTxtArea.appendText(report);
    }

    /**
     * Method for displaying the Customer by Division report in the text area.
     * @param event The event of clicking the Customers By Division button.
     */
    @FXML
    void onActionShowCustomersByDivision(ActionEvent event) {

        reportTxtArea.clear();

        ObservableList<String> customersByDivision = DBACustomer.getAllCustomerCountByDivision();

        //format string of report
        String report = String.valueOf(customersByDivision);
        report = report.substring(1, report.length() - 1);
        report = report.replaceAll(",", "");

        reportTxtArea.appendText(report);
    }

    /**
     * Method for displaying the Contact Schedule report in the text area.
     * A Lambda expression is used in place of a for loop to go through each contact in list and list their appointments.
     * @param event The event of clicking the Contact Schedule report button.
     * @throws SQLException
     */
    @FXML
    void onActionShowContactSchedule(ActionEvent event) throws SQLException {

        reportTxtArea.clear();

        ObservableList<String> allContactNames = DBAContact.getAllContactNames();

        ObservableList<String> report = FXCollections.observableArrayList();

        report.add("Appointment Schedules For Each Contact: \n");

        allContactNames.forEach(contactName -> {
            report.add("\nContact Name: " + contactName + " :: Appointment Schedule:" );

            ObservableList<String> allContactAppointments = DBAContact.getAllAppointmentsForContact(DBAContact.getContactIdFromName(contactName));

            if (!allContactAppointments.isEmpty()) {
                report.addAll(allContactAppointments);
                report.add("\n");
            }
        });
        /*for (String contactName : allContactNames) {
            report.add("\nContact Name: " + contactName + "\n");

            ObservableList<String> allContactAppointments = DBAContact.getAllAppointmentsForContact(DBAContact.getContactIdFromName(contactName));

            if (!allContactAppointments.isEmpty()) {
                report.addAll(allContactAppointments);
                report.add("\n");
            }
        }*/

        //format string of report
        String reportString = String.valueOf(report);
        reportString = reportString.replaceAll(",", "");
        reportString = reportString.substring(1, reportString.length() - 1);

        //test
        // System.out.println(reportString);

        reportTxtArea.appendText(reportString);
    }
}
