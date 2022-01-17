package controller;

import DBAccess.DBAAppointment;
import DBAccess.DBACustomer;
import DBAccess.JDBC;
import Utility.UserLoginSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.EventObject;
import java.util.Locale;
import java.util.Objects;
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

    private Locale ZoneID;

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

        userZoneIdLbl.setText(String.valueOf(ZoneID.getDefault()));
    }

    /**
     * @param event the event of clicking the sign in button to attempt a log in and write the attempt to login_activity.txt
     * @throws Exception
     */
    @FXML
    void onActionAttemptLogin(ActionEvent event) throws Exception {

        //TODO actually attempt of login based on user data in db

        String username = userNameTxt.getText();
        String pass = passwordTxt.getText();

        // can't use bc not user ID User loginUser = new User(username, password)
        //if statement to check log in success or not

        boolean successfulLogin = UserLoginSession.attemptToLogInUser(username, pass);

        //test
        // System.out.println("user: " + username + " " + "pass: " + pass);
        if (successfulLogin == true) {

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
            // test add cust
            //DBACustomer.addCustomer("Ashley Barone", "123 Road Street", "30040", "404-234-2424", 29);
            //ZonedDateTime start = ZonedDateTime.now().plusMinutes(10);
            //ZonedDateTime end = ZonedDateTime.now().plusMinutes(30);
            //DBAAppointment.addAppointment("test title", "test descrip", "test loc","test type", start, end, "test created by", "test updated by", 12, 1, 1);


            //test cust after test add
            //DBACustomer.getAllCustomers();

            //test update customer REMEMBER TO MATCH CUST ID TO ADDED ID
            //DBACustomer.updateCustomer("Elizabeth Bailey", "345 Street Road", "30004", "404-234-4242", "Georgia", 8);

            // test for all cust after test update
            //DBACustomer.getAllCustomers();

            //test appt in 15 minutes
            /*ObservableList<Appointment> appointmentsIn15 = DBAAppointment.getAppointmentsIn15Minutes();
            for(Appointment appt : appointmentsIn15) {
                String str = "Appointments In 15 Minutes: \n Appointment ID: " + appt.getApptId() + " Appointment Start: " + appt.getApptStartDateTime().toString();
                ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, str, btn);
                alert.showAndWait();
            }

            if (appointmentsIn15.isEmpty()) {
                ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No appointments in 15 minutes.", btn);
                alert.showAndWait();

            }*/

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
                return;
            }
        }

        //TODO printwriter for logging all login attempts

       /* Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();*/
    }

    //login success check method
    //maybe move into a log in session utility?? or in DBAuser?
    /*public boolean loginAttempt(String username, String pass) {

        int userId = 1;
        String userName = "1";
        String password = "1";

        try {
            String sql = "SELECT * FROM users WHERE User_Name LIKE '%test%' AND Password LIKE '%test%'";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userId = rs.getInt("User_ID");
                userName = rs.getString("User_Name");
                password = rs.getString("Password");

            } catch(SQLException throwables){
                throwables.printStackTrace();
            }
        if (username.equals(userName) && pass.equals(password)) {
            User user = new User(userId, userName, password);
            System.out.println("ID: " + userId + "Name: " + userName + "password: " + password);
            return true;
        }
        else{System.out.println("false");
            return false;

        }
    }*/
}

