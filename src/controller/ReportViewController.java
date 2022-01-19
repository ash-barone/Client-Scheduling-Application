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

public class ReportViewController {

    @FXML
    private TextArea reportTxtArea;

    /**
     * @param event the event of clicking on the back button to navigate back to main menu
     * @throws Exception exception
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
     * Lambda expression used in place of for loop to go through each contact in list and list their appointments.
     * @param event the event of clicking the report button
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
