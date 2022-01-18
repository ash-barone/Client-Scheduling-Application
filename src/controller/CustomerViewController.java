package controller;

import DBAccess.DBAAppointment;
import DBAccess.DBACustomer;
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
import model.Customer;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {

    @FXML
    private TableColumn<Customer, String> customerAddressCol;

    @FXML
    private TableColumn<Customer, Integer> customerDivisionIdCol;

    @FXML
    private TableColumn<Customer, Integer> customerIdCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> customerPhoneCol;

    @FXML
    private TableColumn<Customer, String> customerPostalCodeCol;

    @FXML
    private TableView<Customer> customerTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        //ObservableList<Customer> allCustomers = DBACustomer.getAllCustomers();
        ObservableList<Customer> allCustomers = DBACustomer.getAllCustomers();

        displayCustomers(allCustomers);

    }

    public void displayCustomers(ObservableList<Customer> customersList){
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        customerDivisionIdCol.setCellValueFactory(new PropertyValueFactory<>("customerDivisionId"));

        customerTableView.setItems(customersList);
    }

    /**
     *
     * @param event the event of clicking the back button to return to the main menu
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
     * @param event the event of clicking the delete all appointment button while selecting a customer. deletes all appointments for that customer
     */
    @FXML
    void onActionDeleteAllAppointmentsForSelectedCustomer(ActionEvent event) {

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if(selectedCustomer == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText("You are about to cancel all appointments for customer " + selectedCustomer.getCustomerName() +".");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElseThrow() == ButtonType.OK){
            DBAAppointment.deleteAllSelectedCustomerAppointments(selectedCustomer.getCustomerId());
        } else {
            alert.close();
        }
        //DBAAppointment.deleteAppointment(selectedAppointment.getApptId());

        Alert alert2 = new Alert((Alert.AlertType.CONFIRMATION));
        alert2.setTitle("Appointments Cancelled");
        alert2.setContentText("All appointments for " + selectedCustomer.getCustomerName() + " have been cancelled.");
        alert2.showAndWait();

    }

    /**
     *
     * @param event the event of clicking the delete customer button while selecting a customer
     */
    @FXML
    void onActionDeleteSelectedCustomer(ActionEvent event) throws SQLException {


        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        //DBACustomer.deleteCustomer(selectedCustomer.getCustomerId());

        if (DBACustomer.deleteCustomer((selectedCustomer.getCustomerId()))){
            Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
            alert.setTitle("Customer Deleted");
            alert.setContentText("Customer " + selectedCustomer.getCustomerName() + " has been deleted.");
            alert.showAndWait();
        }

        //ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ObservableList<Customer> allCustomers = DBACustomer.getAllCustomers();

        displayCustomers(allCustomers);
    }

    /**
     *
     * @param event the event of clicking the add customer button to go to the add customer screen
     * @throws Exception exception
     */
    @FXML
    void onActionToAddCustomerScreen(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking on the update customer button while selecting a customer to update. then send that customer info to update screen to be updated. navigate to update customer screen
     * @throws Exception exception
     */
    @FXML
    void onActionToUpdateCustomerScreen(ActionEvent event) throws Exception {

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if(selectedCustomer == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no customer selected. Please select a customer.");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
        loader.load();

        UpdateCustomerController UCController = loader.getController();
        UCController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem());

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
