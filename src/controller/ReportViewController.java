package controller;

import DBAccess.DBAAppointment;
import DBAccess.DBAContact;
import DBAccess.DBACustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ReportViewController {

    @FXML
    private Button backBtn;

    @FXML
    private TextArea reportTxtArea;

    @FXML
    private ComboBox<String> contactNameComboBox;

    /**
     * @param event the event of clicking on the back button to navigate back to main menu
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

    @FXML
    void onActionShowContactSchedule(ActionEvent event) throws SQLException {

        //TODO ignoring combo box rn then figuring out if i want to use it
        reportTxtArea.clear();

        ObservableList<String> allContactNames = DBAContact.getAllContactNames();

        ObservableList<String> report = FXCollections.observableArrayList();

        report.add("Appointments By Contact: \n");
        //reportTxtArea.appendText("Appointments By Contact: \n");

        for (String contactName : allContactNames) {
            report.add("\nContact Name: " + contactName + "\n");

            ObservableList<String> allContactAppointments = DBAContact.getAllAppointmentsForContact(DBAContact.getContactIdFromName(contactName));

            if (!allContactAppointments.isEmpty()) {
                for (String contactAppointment : allContactAppointments) {
                    report.add(contactAppointment);
                }
                report.add("\n");
            } else if (allContactAppointments.isEmpty()) {
                report.add("\n This contact has no scheduled appointments.\n");
            }
        }

        //format string of report
        String reportString = String.valueOf(report);
        reportString = reportString.replaceAll(",", "");
        reportString = reportString.substring(1, reportString.length() - 1);

        //test
        // System.out.println(reportString);

        reportTxtArea.appendText(reportString);
    }
}
