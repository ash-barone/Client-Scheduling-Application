package controller;

import dbaccess.DBAAppointment;
import utility.UserLoginSession;
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

/**
 * This class is the controller for the Log-in Screen. Included is a method to attempt log-in when clicking the log-in button.
 * This class utilizes the UserLoginSession class.
 */
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

    /**
     * Initialize method for setting language resource bundle based on the user's computer's setting.
     * Will choose French or English depending on local machine settings.
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //testing
        //set language
        ResourceBundle rb = ResourceBundle.getBundle("utility/languageproperties", Locale.getDefault());

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
     * Method to attempt user log-in using the text in the username and password fields.
     * Lambda expression used in place of a for loop to go through each appointment in the appointments in 15 minutes list to display and alert for each appointment assigned to the successfully logged-in user in the next 15 minutes.
     * PrintWriter used for each log-in attempt to store attempt in login_activity.txt.
     * @param event The event of clicking the sign-in button.
     * @throws Exception
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

            appointmentsIn15.forEach(appt -> {
                String str = "Appointments In The Next 15 Minutes: \nAppointment ID: " + appt.getApptId() + " Appointment Start: " + appt.getApptStartDateTime().toString();
                ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, str, btn);
                alert.showAndWait();
            });
            /*for(Appointment appt : appointmentsIn15) {
                String str = "Appointments In The Next 15 Minutes: \nAppointment ID: " + appt.getApptId() + " Appointment Start: " + appt.getApptStartDateTime().toString();
                ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, str, btn);
                alert.showAndWait();
            }*/

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

            ResourceBundle rb = ResourceBundle.getBundle(/*string is the rb prop file name*/ "utility/languageproperties", Locale.getDefault());

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

