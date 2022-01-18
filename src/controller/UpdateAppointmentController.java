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
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

    @FXML
    private ComboBox<String> appointmentContactNameComboBx;

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
    private TextField apptEndTimeTxt;

    @FXML
    private DatePicker apptDatePicker;

    @FXML
    private TextField apptStartTimeTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        javafx.util.Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        apptDatePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     *
     * @param event the event of clicking on the cancel button to return to the appointment view screen
     * @throws Exception exceptions
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
     * @throws Exception exceptions
     */
    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws Exception {

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


            int apptCustomerId = 0;
            apptCustomerId = DBACustomer.getCustomerIdFromName(appointmentCustomerNameComboBx.getValue());
            int apptUserId = 0;
            apptUserId = DBAUser.getUserIdFromName(appointmentUserNameComboBx.getValue());
            String apptContactName = appointmentContactNameComboBx.getValue();
            int apptContactId = 0;
            apptContactId = DBAContact.getContactIdFromName(appointmentContactNameComboBx.getValue());

            //set business hours to check against input
            ZonedDateTime businessStartTime = ZonedDateTime.of(apptDatePicker.getValue(), LocalTime.of(8,0), ZoneId.of("America/New_York"));
            ZonedDateTime businessEndTime = ZonedDateTime.of(apptDatePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York"));


            if (!DBAAppointment.checkForOverlappingCustomerAppointments(apptCustomerId, apptStartDate, apptStartUser, apptEndUser)) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure appointment does not overlap with any existing appointments.\n");
                alert.showAndWait();
            }
            else if (apptEnd.compareTo(apptStart) <= 0 || apptStart.isAfter(businessEndTime) || apptStart.isBefore(businessStartTime) || apptEnd.isAfter(businessEndTime) || apptEnd.isBefore(businessStartTime)){
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure appointment is scheduled within business hours listed on form.\n");
                alert.showAndWait();
            }
            else if (apptStart.isAfter(businessEndTime) || apptStart.isBefore(businessStartTime) || apptEnd.isAfter(businessEndTime) || apptEnd.isBefore(businessStartTime) ||apptTitle.isEmpty() || apptDescription.isEmpty() || apptLocation.isEmpty() || apptType == null || apptCustomerId == 0 || apptUserId == 0 || apptContactName == null || apptContactId == 0) {
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
            //return;

        }
    }

    public void sendAppointment (Appointment appointment) {

        //set temp part to be passed for removal upon saving
        //convert times to get able to set text for text fields
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            ZonedDateTime startDateTimeUTC = appointment.getApptStartDateTime().toInstant().atZone(ZoneOffset.UTC);
            ZonedDateTime startDateTimeUser = startDateTimeUTC.withZoneSameInstant(UserLoginSession.getLoggedInUserTimeZone());
            String apptStartTimeText = startDateTimeUser.format(dateTimeFormatter);
            ZonedDateTime endDateTimeUTC = appointment.getApptEndDateTime().toInstant().atZone(ZoneOffset.UTC);
            ZonedDateTime endDateTimeUser = endDateTimeUTC.withZoneSameInstant(UserLoginSession.getLoggedInUserTimeZone());
            String apptEndTimeText = endDateTimeUser.format(dateTimeFormatter);


            //fill in all your labels
            appointmentIdTxt.setText(String.valueOf(appointment.getApptId()));
            appointmentTitleTxt.setText(appointment.getApptTitle());
            appointmentDescriptionTxt.setText(appointment.getApptDescription());
            appointmentLocationTxt.setText(appointment.getApptLocation());


            //set combo boxes
            appointmentTypeComboBx.setItems(DBAAppointment.getAllApptTypes());
            appointmentCustomerNameComboBx.setItems(DBACustomer.getAllCustomerNames());
            appointmentUserNameComboBx.setItems(DBAUser.getAllUserNames());
            appointmentContactNameComboBx.setItems(DBAContact.getAllContactNames());

            appointmentTypeComboBx.getSelectionModel().select(appointment.getApptType());
            appointmentCustomerNameComboBx.getSelectionModel().select(DBACustomer.getCustomerNameFromId(appointment.getApptCustomerId()));
            appointmentUserNameComboBx.getSelectionModel().select(DBAUser.getUserNameFromId(appointment.getApptUserId()));
            appointmentContactNameComboBx.getSelectionModel().select(appointment.getApptContactName());

            //test
            //System.out.println("User ID: " + appointment.getApptUserId());
            //System.out.println("Customer ID: " + appointment.getApptCustomerId());

            //time stuff
            apptDatePicker.setValue(appointment.getApptStartDateTime().toLocalDateTime().toLocalDate());
            apptStartTimeTxt.setText(apptStartTimeText);
            apptEndTimeTxt.setText(apptEndTimeText);

        } catch (NumberFormatException throwables){
            throwables.printStackTrace();
        }
    }

    private javafx.util.Callback<DatePicker, DateCell> getDayCellFactory() {

        return new Callback<>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        LocalDate today = LocalDate.now();
                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || date.compareTo(today) < 0) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
    }

}
