package controller;

import DBAccess.DBAAppointment;
import Utility.UserLoginSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Label passwordLbl;

    @FXML
    private TextField passwordTxt;

    @FXML
    private Label userNameLbl;

    @FXML
    private TextField userNameTxt;

    @FXML
    private Label userZoneIdLbl;

    @FXML
    private Label welcomeLbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //testing
        //set language
        ResourceBundle rb = ResourceBundle.getBundle(/*string is the rb prop file name*/ "Utility/languageproperties", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            welcomeLbl.setText(rb.getString("Welcome!"));
            userNameLbl.setText(rb.getString("Username"));
            passwordLbl.setText(rb.getString("Password"));
            loginBtn.setText(rb.getString("SignIn"));
            //test
            // System.out.println("language detected " + Locale.getDefault().getLanguage());
        } else {
            //test
            // System.out.println("language not detected");
        }

        userZoneIdLbl.setText(String.valueOf(Locale.getDefault()));
    }

    /**
     * @param event the event of clicking the sign-in button to attempt a log-in and write the attempt to login_activity.txt
     * @throws Exception exception
     */
    @FXML
    void onActionAttemptLogin(ActionEvent event) throws Exception {

        String username = userNameTxt.getText();
        String pass = passwordTxt.getText();

        boolean successfulLogin = UserLoginSession.attemptToLogInUser(username, pass);

        UserLoginSession.logUserActivity(successfulLogin, username);
        //test
        // System.out.println("user: " + username + " " + "pass: " + pass);
        if (successfulLogin) {

            ObservableList<Appointment> appointmentsIn15 = DBAAppointment.getAppointmentsIn15Minutes();
            for(Appointment appt : appointmentsIn15) {
                String str = "Appointments In The Next 15 Minutes: \nAppointment ID: " + appt.getApptId() + " Appointment Start: " + appt.getApptStartDateTime().toString();
                ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, str, btn);
                alert.showAndWait();
            }

            if (appointmentsIn15.isEmpty()) {
                ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No appointments in the next 15 minutes.", btn);
                alert.showAndWait();

            }

            //load new screen
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();

        } else {

            ResourceBundle rb = ResourceBundle.getBundle(/*string is the rb prop file name*/ "Utility/languageproperties", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle(rb.getString("UnableToLogin"));
                alert.setContentText(rb.getString("UsernameOfPassIncorrect"));
                alert.showAndWait();
                //return;
            }
        }
    }
}

