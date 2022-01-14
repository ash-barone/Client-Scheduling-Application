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
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

    //TODO fill in all the ? below
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

    @FXML
    private Button updateAppointmentBtn;

    private Appointment tempAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        javafx.util.Callback<DatePicker, DateCell> dayCellFactory= (javafx.util.Callback<DatePicker, DateCell>) this.getDayCellFactory();
        apptDatePicker.setDayCellFactory(dayCellFactory);
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

    /**
     *
     * @param event the event of clicking on the update appointment button to update the appointment in the db then go back to the appointment view screen
     * @throws Exception
     */
    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws Exception {

        //TODO the actual update statement and stuff
        try {

            int apptId = Integer.parseInt(appointmentIdTxt.getText());
            String apptTitle = appointmentTitleTxt.getText();
            String apptDescription = appointmentDescriptionTxt.getText();
            String apptLocation = appointmentLocationTxt.getText();
            String apptType = appointmentTypeComboBx.getValue();
            LocalDate apptStartDate = apptDatePicker.getValue();
            LocalDateTime apptStartUser = LocalDateTime.of(apptDatePicker.getValue(), LocalTime.parse(apptStartTimeTxt.getText()));
            LocalDateTime apptEndUser = LocalDateTime.of(apptDatePicker.getValue(), LocalTime.parse(apptEndTimeTxt.getText()));
            ZonedDateTime apptStartUserZone = ZonedDateTime.of(apptStartUser, UserLoginSession.getLoggedInUserTimeZone());
            ZonedDateTime apptEndUserZone = ZonedDateTime.of(apptEndUser, UserLoginSession.getLoggedInUserTimeZone());
            ZonedDateTime apptStart = apptStartUserZone.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime apptEnd = apptEndUserZone.withZoneSameInstant(ZoneOffset.UTC);

            //last updated
            String lastUpdatedBy = UserLoginSession.getUserLoggedIn().getUserName();


            Integer apptCustomerId = DBACustomer.getCustomerIdFromName(appointmentCustomerNameComboBx.getValue());
            Integer apptUserId = DBAUser.getUserIdFromName(appointmentUserNameComboBx.getValue());
            String apptContactName = appointmentContactNameComboBx.getValue();
            Integer apptContactId = DBAContact.getContactIdFromName(appointmentContactNameComboBx.getValue());

            if (apptTitle.isEmpty() || apptDescription.isEmpty() || apptLocation.isEmpty() || apptType == null || apptCustomerId == null || apptUserId == null || apptContactName == null || apptContactId == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct.");
                alert.showAndWait();
            }
            else {
                DBAAppointment.updateAppointment(apptTitle, apptDescription, apptLocation, apptType, apptStart, apptEnd, lastUpdatedBy, apptCustomerId, apptUserId, apptContactId, apptId);

                Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Appointment Updated");
                alert.setContentText("Appointment has been updated.");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Appointment View");
                stage.setScene(scene);
                stage.show();
            }


            //back to customer view after update


        } catch (DateTimeParseException throwables){
            throwables.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Could not update appointment.");
            alert.showAndWait();
            return;

        }
    }

    public void sendAppointment (Appointment appointment) throws SQLException {

        //set temp part to be passed for removal upon saving
        //convert times to get able to set text for text fields
        try {
            tempAppointment = appointment;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            ZonedDateTime startDateTimeUTC = tempAppointment.getApptStartDateTime().toInstant().atZone(ZoneOffset.UTC);
            ZonedDateTime startDateTimeUser = startDateTimeUTC.withZoneSameInstant(UserLoginSession.getLoggedInUserTimeZone());
            String apptStartTimeText = startDateTimeUser.format(dateTimeFormatter);
            ZonedDateTime endDateTimeUTC = tempAppointment.getApptEndDateTime().toInstant().atZone(ZoneOffset.UTC);
            ZonedDateTime endDateTimeUser = endDateTimeUTC.withZoneSameInstant(UserLoginSession.getLoggedInUserTimeZone());
            String apptEndTimeText = endDateTimeUser.format(dateTimeFormatter);


            /**
             * get index for part so that modified part can maintain its same index after modified
             */
            //id = DBACustomer.ge;
            //System.out.println(index + " is the index");

            //fill in all your labels
            appointmentIdTxt.setText(String.valueOf(tempAppointment.getApptId()));
            appointmentTitleTxt.setText(tempAppointment.getApptTitle());
            appointmentDescriptionTxt.setText(tempAppointment.getApptDescription());
            appointmentLocationTxt.setText(tempAppointment.getApptLocation());
            //appointmentContactIdTxt.setText(String.valueOf(appointment.getApptContactId()));
            //appointmentContactNameTxt.setText((appointment.()));

            //set country combo box
            appointmentTypeComboBx.setItems(DBAAppointment.getAllApptTypes());
            appointmentCustomerNameComboBx.setItems(DBACustomer.getAllCustomerNames());
            appointmentUserNameComboBx.setItems(DBAUser.getAllUserNames());
            appointmentContactNameComboBx.setItems(DBAContact.getAllContactNames());

            appointmentTypeComboBx.getSelectionModel().select(tempAppointment.getApptType());
            appointmentCustomerNameComboBx.getSelectionModel().select(DBACustomer.getCustomerNameFromId(tempAppointment.getApptCustomerId()));
            appointmentUserNameComboBx.getSelectionModel().select(DBAUser.getUserNameFromId(tempAppointment.getApptUserId()));
            appointmentContactNameComboBx.getSelectionModel().select(tempAppointment.getApptContactName());

            //test
            System.out.println("User ID: " + tempAppointment.getApptUserId());
            System.out.println("Customer ID: " + tempAppointment.getApptCustomerId());

            //time stuff
            apptDatePicker.setValue(tempAppointment.getApptStartDateTime().toLocalDateTime().toLocalDate());
            apptStartTimeTxt.setText(apptStartTimeText);
            apptEndTimeTxt.setText(apptEndTimeText);

        } catch (NumberFormatException throwables){
            throwables.printStackTrace();
        }



        //String tempTitle = tempAppointment.getApptTitle();
        //System.out.println("tempCountry = " + tempTitle + "\n");
        //ObservableList<String> tempDivisionsByCountry = FXCollections.observableArrayList();
        //tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);
        //System.out.println("tempDivisionByCountry = " +tempDivisionsByCountry);

        //System.out.println("tempCustomer's Divison: " + tempCustomer.getCustomerDivisionName());
        //set division combo box
        //customerDivisionComboBox.setItems(tempDivisionsByCountry);
        //customerDivisionComboBox.getSelectionModel().select(tempCustomer.getCustomerDivisionName());


        //customerDivisionComboBox.setItems(DBACustomer.getDivisionsByCountry(tempCustomer.getCustomerCountry()));
        //customerDivisionComboBox.getSelectionModel().select(tempCustomer.getCustomerDivisionName());
    }

    private javafx.util.Callback<DatePicker, DateCell> getDayCellFactory() {

        final javafx.util.Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            //@Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Disable Monday, Tueday, Wednesday.
                        if (item.getDayOfWeek() == DayOfWeek.SATURDAY //
                                || item.getDayOfWeek() == DayOfWeek.SUNDAY){ //
                            //|| item.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
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
