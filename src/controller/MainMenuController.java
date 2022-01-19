package controller;

import utility.UserLoginSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is the controller for the Main Menu. Included are navigation methods for accessing the Customer View, Appointment View, and Reports View.
 * Navigation back to log-in screen is also included which will log the current user out.
 */
public class MainMenuController {

    /**
     * Method for navigating to the Appointment View screen.
     * @param event The event of clicking the Appointments View button.
     * @throws Exception
     */
    @FXML
    void onActionToAppointmentsView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

    }

    /**
     * Method for navigating to the Customer View screen.
     * @param event The event of clicking the Customer View button.
     * @throws Exception
     */
    @FXML
    void onActionToCustomerView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.centerOnScreen();
        stage.setTitle("Customer View");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

    }

    /**
     * Method for navigating to the Report View screen.
     * @param event The event of clicking on the Report View button.
     * @throws Exception
     */
    @FXML
    void onActionToReportsView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/ReportView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Report View");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

    }

    /**
     * Method to log the user out and navigate back to the log-in screen.
     * @param event The event of clicking on the sign-off button.
     * @throws Exception
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
