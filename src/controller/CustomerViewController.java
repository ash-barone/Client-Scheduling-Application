package controller;

import dbaccess.DBAAppointment;
import dbaccess.DBACustomer;
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


/**
 * This class is the controller for the Customer View Screen. Included are methods that display customers in a table view. Navigation to an add and update form is included as well as navigation back to the Main Menu. Included also is a method for deleting a selected customer as well as all of that selected customer's appointments.
 */
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

    /**
     * Initialize method to populate the Customers TableView and style the TableView
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerTableView.setStyle("-fx-selection-bar: #c2bff8");

        ObservableList<Customer> allCustomers = DBACustomer.getAllCustomers();

        displayCustomers(allCustomers);

    }

    /**
     * Method for exiting the Customer View and returning to the Main Menu
     * @param event The event of clicking on the Back button.
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
     * Method for deleting a customer that has been selected on the TableView. Gives user a confirmation message prompt. Gives an error if the selected customer has current appointments. Customer TableView will repopulate based on the deletion.
     * @param event The event of clicking the Delete Customer button while having a customer selected.
     * @throws SQLException exception
     */
    @FXML
    void onActionDeleteSelectedCustomer(ActionEvent event) throws SQLException {

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if(selectedCustomer == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("You are about to delete Customer with name " + selectedCustomer.getCustomerName() +".");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElseThrow() == ButtonType.OK){
            if (!DBAAppointment.getAllSelectedCustomerAppointments(selectedCustomer.getCustomerId()).isEmpty()) {
                Alert alert3 = new Alert((Alert.AlertType.WARNING));
                alert3.setTitle("Cannot delete customer");
                alert3.setContentText("Cannot delete a customer to has active appointments. \n Please delete all customer appointments first.");
                alert3.showAndWait();
            }
            else {
                DBACustomer.deleteCustomer(selectedCustomer.getCustomerId());
                Alert alert2 = new Alert((Alert.AlertType.CONFIRMATION));
                alert2.setTitle("Customer Deleted");
                alert2.setContentText("Customer " + selectedCustomer.getCustomerName() + " has been deleted.");
                alert2.showAndWait();
            }
        } else {
            alert.close();
        }

        ObservableList<Customer> allCustomers = DBACustomer.getAllCustomers();

        displayCustomers(allCustomers);
    }

    /**
     * Method for deleting an appointment schedule for a customer that has been selected on the TableView. Gives user a confirmation message prompt. Appointment TableView will repopulate based on the deletion whenever the Appointment View screen is next accessed.
     * @param event The event of clicking the Delete All Customer Appointments button while having a customer selected.
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

        Alert alert2 = new Alert((Alert.AlertType.CONFIRMATION));
        alert2.setTitle("Appointments Cancelled");
        alert2.setContentText("All appointments for " + selectedCustomer.getCustomerName() + " have been cancelled.");
        alert2.showAndWait();

    }

    /**
     * Method used to display customers.
     * @param customersList The list of customers to display
     */
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
     * Method for navigating to the Add Customer Screen.
     * @param event The event of clicking the Add New Customer button.
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
     * Method for navigating to the Update Customer Screen when a customer in the TableView is selected. Show error message for no selected customer.
     * @param event The event of clicking the Update Customer button.
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
