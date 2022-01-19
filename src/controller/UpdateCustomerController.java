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
import utility.UserLoginSession;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
     * lambda expression for listener. sets divisions based on country selected
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerCountryComboBx.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null){
                customerDivisionComboBox.getItems().clear();
            }
            else {

                String tempCountry = customerCountryComboBx.getValue();
                //ObservableList<String> tempDivisionsByCountry = FXCollections.observableArrayList();
                ObservableList<String> tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);

                customerDivisionComboBox.setItems(tempDivisionsByCountry);
                //customerDivisionComboBox.getSelectionModel().select(tempCustomer.getCustomerDivisionName());

            }
        });
    }

    /**
     *
     * @param event the event of clicking on the update customer button to update the customer in db then return to customer view screen
     *
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

            if (customerName.isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Name cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerAddress.isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Address cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerPostalCode.isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Postal Code cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerPhoneNumber.isEmpty()) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Phone Number cannot be left blank. Please fill in a value.");
                alert.showAndWait();
            }
            if (customerCountry == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Country cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            if (customerDivision == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Customer Division cannot be left blank. Please select a value.");
                alert.showAndWait();
            }
            else {
                DBACustomer.updateCustomer(customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivision, customerId);

                Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Customer Updated");
                alert.setContentText("Customer has been updated.");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Customer View");
                stage.setScene(scene);
                stage.show();
            }


            //back to customer view after update


        } catch (IOException throwables){
            throwables.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Could not update customer.");
            alert.showAndWait();
            //return;

        }
    }

    /**
     *
     * @param event the event of clicking the cancel button to return to the customer view screen
     * @throws Exception exception
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


    public void sendCustomer (Customer customer) throws SQLException {

        //fill in all your labels
        customerIdTxt.setText(String.valueOf(customer.getCustomerId()));
        customerNameTxt.setText(customer.getCustomerName());
        customerAddressTxt.setText(customer.getCustomerAddress());
        customerPostalCodeTxt.setText(customer.getCustomerPostalCode());
        customerPhoneTxt.setText((customer.getCustomerPhoneNumber()));

        //set country combo box
        customerCountryComboBx.setItems(DBACustomer.getAllDistinctCountries());
        customerCountryComboBx.getSelectionModel().select(customer.getCustomerCountry());

        String tempCountry = customer.getCustomerCountry();
        //test
        // System.out.println("tempCountry = " + tempCountry + "\n");

        ObservableList<String> tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);
        //test
        // System.out.println("tempDivisionByCountry = " +tempDivisionsByCountry);

        //test
        // System.out.println("tempCustomer's Division: " + customer.getCustomerDivisionName());

        //set division combo box
        customerDivisionComboBox.setItems(tempDivisionsByCountry);
        customerDivisionComboBox.getSelectionModel().select(customer.getCustomerDivisionName());

    }
}
