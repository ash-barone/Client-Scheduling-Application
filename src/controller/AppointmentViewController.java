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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentViewController implements Initializable {

    @FXML
    private RadioButton allAppointmentsRBtn;

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentContactIdCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdCol;

    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionCol;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> appointmentEndCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;

    @FXML
    private TableColumn<Appointment, String> appointmentLocationCol;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> appointmentStartCol;

    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentUserIdCol;

    @FXML
    private RadioButton appointmentsByMonthRBtn;

    @FXML
    private RadioButton appointmentsByWeekRBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allAppointmentsRBtn.setSelected(true);

        //ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointments = DBAAppointment.getAllAppointments();

        displayAppointments(allAppointments);
        //System.out.println(allAppointments);

    }
    /**
     *
     * @param event the event of clicking on the back button to return to the main menu
     * @throws Exception exception
     */
    @FXML
    void onActionBackToMainMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking the deleted appointment button while having an appointment selected then remove it from db
     */
    @FXML
    void onActionDeleteSelectedAppointment(ActionEvent event) {

        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("You are about to cancel Appointment with ID " + selectedAppointment.getApptId() +".");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElseThrow() == ButtonType.OK){
            DBAAppointment.deleteAppointment(selectedAppointment.getApptId());
        } else {
            alert.close();
        }
        //DBAAppointment.deleteAppointment(selectedAppointment.getApptId());

        Alert alert2 = new Alert((Alert.AlertType.CONFIRMATION));
        alert2.setTitle("Appointment Cancelled");
        alert2.setContentText("Appointment with ID " + selectedAppointment.getApptId() + " has been cancelled.");
        alert2.showAndWait();

        if (allAppointmentsRBtn.isSelected()) {
            allAppointmentsRBtn.setSelected(true);
            //ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
            ObservableList<Appointment> allAppointments = DBAAppointment.getAllAppointments();
            displayAppointments(allAppointments);
        }
        else if (appointmentsByMonthRBtn.isSelected()){
            appointmentsByMonthRBtn.setSelected(true);
            ZonedDateTime start = ZonedDateTime.now(UserLoginSession.getLoggedInUserTimeZone());
            ZonedDateTime end = start.plusMonths(1);
            ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

            //ObservableList<Appointment> allAppointmentsByMonth = FXCollections.observableArrayList();
            ObservableList<Appointment> allAppointmentsByMonth = DBAAppointment.getAppointmentsByDateRange(startUTC, endUTC);
            displayAppointments(allAppointmentsByMonth);
        }
        else if (appointmentsByWeekRBtn.isSelected()){

            ZonedDateTime start = ZonedDateTime.now(UserLoginSession.getLoggedInUserTimeZone());
            ZonedDateTime end = start.plusWeeks(1);
            ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

            //ObservableList<Appointment> allAppointmentsByWeek = FXCollections.observableArrayList();
            ObservableList<Appointment> allAppointmentsByWeek = DBAAppointment.getAppointmentsByDateRange(startUTC, endUTC);
            displayAppointments(allAppointmentsByWeek);
        }
    }

    /**
     *
     * @param event the event of selecting the all appointments radio to show all appointments in the tableview
     */
    @FXML
    void onActionDisplayAllAppointments(ActionEvent event) {

        allAppointmentsRBtn.setSelected(true);
        //ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointments = DBAAppointment.getAllAppointments();
        displayAppointments(allAppointments);
    }

    /**
     * method to display appointments
     * @param appointmentsList the list of appts to display
     */
    public void displayAppointments(ObservableList<Appointment> appointmentsList) {

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("apptStartDateTime"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("apptEndDateTime"));
        appointmentCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("apptCustomerId"));
        appointmentUserIdCol.setCellValueFactory(new PropertyValueFactory<>("apptUserId"));
        appointmentContactIdCol.setCellValueFactory(new PropertyValueFactory<>("apptContactId"));
        //appointmentContactName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apptContactName"));

        appointmentsTableView.setItems(appointmentsList);
    }
    /**
     *
     * @param event the event of selecting the appointments by month radio to show all appointments in current month
     */
    @FXML
    void onActionDisplayAppointmentsByMonth(ActionEvent event) {

        appointmentsByMonthRBtn.setSelected(true);
        ZonedDateTime start = ZonedDateTime.now(UserLoginSession.getLoggedInUserTimeZone());
        ZonedDateTime end = start.plusMonths(1);
        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

        //ObservableList<Appointment> allAppointmentsByMonth = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointmentsByMonth = DBAAppointment.getAppointmentsByDateRange(startUTC, endUTC);
        displayAppointments(allAppointmentsByMonth);
    }

    /**
     *
     * @param event the event of selecting the appointments by week radio to show all appointments in the current week
     */
    @FXML
    void onActionDisplayAppointmentsByWeek(ActionEvent event) {

        appointmentsByWeekRBtn.setSelected(true);
        ZonedDateTime start = ZonedDateTime.now(UserLoginSession.getLoggedInUserTimeZone());
        ZonedDateTime end = start.plusWeeks(1);
        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

        //ObservableList<Appointment> allAppointmentsByWeek = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointmentsByWeek = DBAAppointment.getAppointmentsByDateRange(startUTC, endUTC);
        displayAppointments(allAppointmentsByWeek);
    }

    /**
     *
     * @param event of clicking the add appointment button to go to the add appointment screen
     * @throws Exception exception
     */
    @FXML
    void onActionToAddAppointmentScreen(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking the update appointment screen while having an appointment selected. sends the selected appointment info to update screen to you can update info
     * @throws Exception exception
     */
    @FXML
    void onActionToUpdateAppointmentScreen(ActionEvent event) throws Exception {

        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no appointment selected. Please select an appointment.");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
        loader.load();

        UpdateAppointmentController UAController = loader.getController();
        System.out.println("selectedAppointment: " + selectedAppointment);
        UAController.sendAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Update Appointment");
        stage.setScene(new Scene(scene));
        stage.show();

    }
}
