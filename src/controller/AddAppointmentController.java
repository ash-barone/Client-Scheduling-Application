package controller;

import dbaccess.DBAAppointment;
import dbaccess.DBAContact;
import dbaccess.DBACustomer;
import dbaccess.DBAUser;
import utility.UserLoginSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Add Appointment screen. Included are methods that allow attempts to add a new appointment to the connected database. Navigation for exit to form back to the Appointment View is included as well.
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private ComboBox<String> appointmentContactNameComboBx;

    @FXML
    private ComboBox<String> appointmentCustomerNameComboBx;

    @FXML
    private TextField appointmentDescriptionTxt;

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

    @FXML
    private Label userTimeZoneLbl;

    /**
     * The initialize method changes a label on screen to display the user's time zone for reference when setting appointment times. This method also sets the items for the Type, Contact, Customer, and User combo boxes as well as limits on available dates within the Date Picker.
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userTimeZoneLbl.setText(String.valueOf(UserLoginSession.getLoggedInUserTimeZone()));

        appointmentContactNameComboBx.setItems(DBAContact.getAllContactNames());
        appointmentTypeComboBx.setItems(DBAAppointment.getAllApptTypes());
        appointmentCustomerNameComboBx.setItems(DBACustomer.getAllCustomerNames());
        appointmentUserNameComboBx.setItems(DBAUser.getAllUserNames());

        javafx.util.Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        apptDatePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     * This method allows an attempt to add an appointment to the database. There are custom error messages provided for any fields left blank as well as custom error messages for scheduling issues such as failure to follow business hours or scheduling an overlapping appointment for a customer. On a success, the application will change back to the Appointment View screen.
     * @param event The event of clicking the Add button.
     * @throws Exception exception
     */
    @FXML
    void onActionAddAppointment(ActionEvent event) throws Exception {

        try {

            //check for fields left blank
            if (appointmentTitleTxt.getText().isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Title cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (appointmentDescriptionTxt.getText().isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Description cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (appointmentLocationTxt.getText().isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Location cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (appointmentTypeComboBx.getValue() == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Type cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (DBACustomer.getCustomerIdFromName(appointmentCustomerNameComboBx.getValue()) == 0) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (DBAUser.getUserIdFromName(appointmentUserNameComboBx.getValue()) == 0) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("User cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (appointmentContactNameComboBx.getValue() == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Contact cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (apptDatePicker.getValue() == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Appointment Date cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (apptStartTimeTxt.getText().isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Start Time cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (apptEndTimeTxt.getText().isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("End Time cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }

            String apptTitle = appointmentTitleTxt.getText();
            String apptDescription = appointmentDescriptionTxt.getText();
            String apptLocation = appointmentLocationTxt.getText();
            String apptType = appointmentTypeComboBx.getValue();

            //get date and time set
            LocalDate apptDate = apptDatePicker.getValue();
            LocalDateTime apptStartUser = LocalDateTime.of(apptDate, LocalTime.parse(apptStartTimeTxt.getText()));
            LocalDateTime apptEndUser = LocalDateTime.of(apptDate, LocalTime.parse(apptEndTimeTxt.getText()));

            //test
            // System.out.println("start: " + apptStartUser + " end: " + apptEndUser);

            ZonedDateTime apptStartUserZone = ZonedDateTime.of(apptStartUser, UserLoginSession.getLoggedInUserTimeZone());
            ZonedDateTime apptEndUserZone = ZonedDateTime.of(apptEndUser, UserLoginSession.getLoggedInUserTimeZone());

            //test
            // System.out.println("start in zone: " + apptStartUserZone + " end in zone: " + apptEndUserZone);

            ZonedDateTime apptStart = apptStartUserZone.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime apptEnd = apptEndUserZone.withZoneSameInstant(ZoneOffset.UTC);

            //test
            // System.out.println("start in UTC: " + apptStart + " end in UTC: " + apptEnd);

            String lastUpdatedBy = UserLoginSession.getUserLoggedIn().getUserName();
            String createdBy = UserLoginSession.getUserLoggedIn().getUserName();

            int apptCustomerId = DBACustomer.getCustomerIdFromName(appointmentCustomerNameComboBx.getValue());
            int apptUserId = DBAUser.getUserIdFromName(appointmentUserNameComboBx.getValue());
            String apptContactName = appointmentContactNameComboBx.getValue();
            int apptContactId = DBAContact.getContactIdFromName(apptContactName);

            //set business hours to check against input
            ZonedDateTime businessStartTime = ZonedDateTime.of(apptDatePicker.getValue(), LocalTime.of(8,0), ZoneId.of("America/New_York"));
            ZonedDateTime businessEndTime = ZonedDateTime.of(apptDatePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York"));

            int apptID = 0; //0 bc adding new appointment there is not yet an appt id

            //input validation check for blank fields


            //time validation
            if (!DBAAppointment.checkForOverlappingCustomerAppointments(apptID, apptCustomerId, apptDate, apptStartUser, apptEndUser)) {
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
            //add appointment if everything else checks out
            else {
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

        } catch (DateTimeParseException throwables){
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Could not add appointment. Please ensure no fields are left blank.");
            alert.showAndWait();

        }
    }

    /**
     * The method to exit the Add form and return to the Appointment View
     * @param event The event of clicking on the Cancel button.
     * @throws Exception exception
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
     * The method for disabling days in the past from being selected for a new appointment.
     * @return The update for disabled days
     */
    private javafx.util.Callback<DatePicker, DateCell> getDayCellFactory() {

        return new Callback<>() {

            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate theDay, boolean blank) {
                        super.updateItem(theDay, blank);

                        LocalDate today = LocalDate.now();
                        if (theDay.compareTo(today) < 0) {
                            setDisable(true);
                            setStyle("-fx-background-color: #c2bff8;");
                        }
                    }
                };
            }
        };
    }
}

