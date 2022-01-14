package controller;

import DBAccess.DBAAppointment;
import DBAccess.DBAContact;
import DBAccess.DBACustomer;
import DBAccess.DBAUser;
import Utility.UserLoginSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    //TODO fill in all ? below

    @FXML
    private Button addAppointmentBtn;

    @FXML
    private ComboBox<String> appointmentContactNameComboBx;

    @FXML
    private TextField appointmentContactIdTxt;

    @FXML
    private ComboBox<String> appointmentCustomerNameComboBx;

    @FXML
    private TextField appointmentDescriptionTxt;

    @FXML
    private TextField appointmentIdTxt;

    @FXML
    private TextField appointmentLocationTxt;

    @FXML
    private TextField appointmentTitleTxt;

    @FXML
    private ComboBox<String> appointmentTypeComboBx;

    @FXML
    private ComboBox<String> appointmentUserNameComboBx;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField apptEndTimeTxt;

    @FXML
    private DatePicker apptDatePicker;

    @FXML
    private TextField apptStartTimeTxt;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentContactNameComboBx.setItems(DBAContact.getAllContactNames());
        appointmentTypeComboBx.setItems(DBAAppointment.getAllApptTypes());
        appointmentCustomerNameComboBx.setItems(DBACustomer.getAllCustomerNames());
        appointmentUserNameComboBx.setItems(DBAUser.getAllUserNames());

        javafx.util.Callback<DatePicker, DateCell> dayCellFactory= (javafx.util.Callback<DatePicker, DateCell>) this.getDayCellFactory();
        apptDatePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     *
     * @param event the event of clicking the add appointment button to add appointment to db and return to the appointment view screen
     * @throws Exception
     */
    @FXML
    void onActionAddAppointment(ActionEvent event) throws Exception {

        //TODO the actual add appointment to db thing
        try {

            //int apptId = Integer.parseInt(appointmentIdTxt.getText());
            String apptTitle = appointmentTitleTxt.getText();
            String apptDescription = appointmentDescriptionTxt.getText();
            String apptLocation = appointmentLocationTxt.getText();
            String apptType = appointmentTypeComboBx.getValue();

            //error when not inputting time //instead of throwing error for incorrect values just show not alert and throws errors
                LocalDate apptDate = apptDatePicker.getValue();
                LocalDateTime apptStartUser = LocalDateTime.of(apptDate, LocalTime.parse(apptStartTimeTxt.getText()));
                LocalDateTime apptEndUser = LocalDateTime.of(apptDate, LocalTime.parse(apptEndTimeTxt.getText()));


                ZonedDateTime apptStartUserZone = ZonedDateTime.of(apptStartUser, UserLoginSession.getLoggedInUserTimeZone());
                ZonedDateTime apptEndUserZone = ZonedDateTime.of(apptEndUser, UserLoginSession.getLoggedInUserTimeZone());
                ZonedDateTime apptStart = apptStartUserZone.withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime apptEnd = apptEndUserZone.withZoneSameInstant(ZoneOffset.UTC);


            //last updated
            String lastUpdatedBy = UserLoginSession.getUserLoggedIn().getUserName();
            String createdBy = UserLoginSession.getUserLoggedIn().getUserName();


            Integer apptCustomerId = DBACustomer.getCustomerIdFromName(appointmentCustomerNameComboBx.getValue());
            Integer apptUserId = DBAUser.getUserIdFromName(appointmentUserNameComboBx.getValue());
            String apptContactName = appointmentContactNameComboBx.getValue();
            //Integer apptContactId = Integer.parseInt(appointmentContactIdTxt.getText());
            Integer apptContactId = DBAContact.getContactIdFromName(apptContactName);

            if (apptTitle.isEmpty() || apptDescription.isEmpty() || apptLocation.isEmpty() || apptType == null || apptCustomerId == null || apptUserId == null || apptContactName == null || apptContactId == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct.");
                alert.showAndWait();
            }
            else { /*String apptTitle, String apptDescription, String apptLocation, String apptType, ZonedDateTime apptStart, ZonedDateTime apptEnd, String createdBy, String lastUpdatedBy, int customerID, int userID, int contactID*/
                DBAAppointment.addAppointment(apptTitle, apptDescription, apptLocation, apptType, apptStart, apptEnd, createdBy, lastUpdatedBy, apptCustomerId, apptUserId, apptContactId);

                Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Appointment Added");
                alert.setContentText("Appointment has been added.");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Appointment View");
                stage.setScene(scene);
                stage.show();
            }


            //back to customer view after update


        } catch (DateTimeParseException throwables2){
            //throwables.printStackTrace();
            throwables2.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Could not add appointment. Ensure all values are correct.");
            alert.showAndWait();
            return;

        }
    }

    /**
     *
     * @param event the event of clicking on the cancel button to return to the appointment view screen
     * @throws Exception
     */
    @FXML
    void onActionCancelToAppointmentView(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    private javafx.util.Callback<DatePicker, DateCell> getDayCellFactory() {

        final javafx.util.Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            //@Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        LocalDate today = LocalDate.now();
                        // Disable Monday, Tueday, Wednesday.
                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY //
                                || date.getDayOfWeek() == DayOfWeek.SUNDAY //
                                || date.compareTo(today) < 0) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

}

