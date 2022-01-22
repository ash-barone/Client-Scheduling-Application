package controller;

import dbaccess.DBACustomer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is the controller class for the Update Customer form. Inside are methods which allow population of customer data into specific fields as well as ability to update said customer. Navigation back to Customer View is included as well as a method that is used on the Customer View Controller for sending a selected customer's data to the Update Customer form.
 */
public class UpdateCustomerController implements Initializable {

    @FXML
    private TextField customerAddressTxt;

    @FXML
    private ComboBox<String> customerCountryComboBx;

    @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField customerNameTxt;

    @FXML
    private TextField customerPhoneTxt;

    @FXML
    private TextField customerPostalCodeTxt;

    @FXML
    private ComboBox<String> customerDivisionComboBox;

    /**
     * ***LAMBDA EXPRESSION*** Initialize method used to set items for the Country and Division combo boxes. A Lambda expression is used to add a listener that sets Division combo box items based on Country combo box selection.
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerCountryComboBx.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null){

                customerDivisionComboBox.getItems().clear();

            }
            else {

                String tempCountry = customerCountryComboBx.getValue();

                ObservableList<String> tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);

                customerDivisionComboBox.setItems(tempDivisionsByCountry);

            }
        });
    }

    /**
     * Update customer method for saving any updates made on the form for the populated customer to the connected database when the Update button is clicked. Provides customer error messages for blank fields or issues updating the customer. When update is successful, the application returns to the Customer View screen.
     * @param event The event of clicking on the Update button.
     */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) {

        try {

            int customerId = Integer.parseInt(customerIdTxt.getText());
            String customerName = customerNameTxt.getText();
            String customerAddress = customerAddressTxt.getText();
            String customerPostalCode = customerPostalCodeTxt.getText();
            String customerPhoneNumber = customerPhoneTxt.getText();
            String customerCountry = customerCountryComboBx.getValue();
            String customerDivision = customerDivisionComboBox.getValue();

            boolean fieldsNotBlank = true;

            //custom error messages for blank fields
            if (customerName.isEmpty()) {
                fieldsNotBlank = false;

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Name cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerAddress.isEmpty()) {
                fieldsNotBlank = false;

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Address cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerPostalCode.isEmpty()) {
                fieldsNotBlank = false;

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Postal Code cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerPhoneNumber.isEmpty()) {
                fieldsNotBlank = false;

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Phone Number cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerCountry == null) {
                fieldsNotBlank = false;

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Country cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (customerDivision == null) {
                fieldsNotBlank = false;

                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Division cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (!fieldsNotBlank) {
                //do not update customer
            }
            else {
                DBACustomer.updateCustomer(customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivision, customerId);

                Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Customer Updated");
                alert.setContentText("Customer " + customerName + " has been updated.");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Customer View");
                stage.setScene(scene);
                stage.show();
            }

        } catch (IOException throwables){
            throwables.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Could not update customer. Please ensure all values are correct.");
            alert.showAndWait();

        }
    }

    /**
     * Method for exiting the update form and returning to the Customer View screen.
     * @param event The event of clicking the Cancel button.
     * @throws Exception The exception for failed screen change.
     */
    @FXML
    void onActionCancelToCustomerView(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The Send Customer method is used to set the text fields and combo boxes to values taken from a customer selected on the Customer View.
     * @param customer The customer selected for update.
     * @throws SQLException The exception for SQL statement errors.
     */
    public void sendCustomer (Customer customer) throws SQLException {

        //set text in all text fields
        customerIdTxt.setText(String.valueOf(customer.getCustomerId()));
        customerNameTxt.setText(customer.getCustomerName());
        customerAddressTxt.setText(customer.getCustomerAddress());
        customerPostalCodeTxt.setText(customer.getCustomerPostalCode());
        customerPhoneTxt.setText((customer.getCustomerPhoneNumber()));

        //set selection in country combo box
        customerCountryComboBx.setItems(DBACustomer.getAllDistinctCountries());
        customerCountryComboBx.getSelectionModel().select(customer.getCustomerCountry());

        String tempCountry = customer.getCustomerCountry();
        //test
        // System.out.println("tempCountry = " + tempCountry + "\n");

        ObservableList<String> tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);
        //test
        // System.out.println("tempDivisionByCountry = " +tempDivisionsByCountry);
        // System.out.println("tempCustomer's Division: " + customer.getCustomerDivisionName());

        //set selection in division combo box
        customerDivisionComboBox.setItems(tempDivisionsByCountry);
        customerDivisionComboBox.getSelectionModel().select(customer.getCustomerDivisionName());

    }
}
