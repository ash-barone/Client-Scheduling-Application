package controller;

import Utility.UserLoginSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    /**
     *
     * @param event the event of clicking the Appointments View button that navigates to that screen
     * @throws Exception exception
     */
    @FXML
    void onActionToAppointmentsView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking the customer view button that navigate to that screen
     * @throws Exception exception
     */
    @FXML
    void onActionToCustomerView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the vent of clicking on the report view button that navigates to that screen
     * @throws Exception exception
     */
    @FXML
    void onActionToReportsView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/ReportView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Report View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking on the sign-off button that logs user out and navigates back to the login screen
     * @throws Exception exception
     */
    @FXML
    void onActionLogUserOut(ActionEvent event) throws Exception {

        //user log off action to clear user info
        UserLoginSession.logUserOff();
        //test to delete added customer test and for delete customer functionality
        //DBACustomer.deleteCustomer(9);

        //navigation back to log-in screen
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.show();
    }
}
