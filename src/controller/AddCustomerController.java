package controller;

import dbaccess.DBACustomer;
/*import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;*/ //used before change to lambda
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private TextField customerAddressTxt;

    @FXML
    private ComboBox<String> customerCountryComboBx;

    @FXML
    private ComboBox<String> customerDivisionComboBx;

    @FXML
    private TextField customerNameTxt;

    @FXML
    private TextField customerPhoneTxt;

    @FXML
    private TextField customerPostalCodeTxt;

    /**
     * Lambda Expression for combo box listener. changes division combo box items based on selection in country combo box
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            customerCountryComboBx.setItems(DBACustomer.getAllDistinctCountries());

            customerDivisionComboBx.setItems(DBACustomer.getDivisionsByCountry(customerCountryComboBx.getValue()));

            customerCountryComboBx.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue == null){
                    customerDivisionComboBx.getItems().clear();
                }
                else {

                    String tempCountry = customerCountryComboBx.getValue();

                    ObservableList<String> tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);

                    customerDivisionComboBx.setItems(tempDivisionsByCountry);

                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     *
     * @param event the event of clicking on the add customer button to add customer to db and return to the customer view screen
     * @throws Exception exceptions
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws Exception {

        try {

            //int customerId = Integer.parseInt(customerIdTxt.getText());
            String customerName = customerNameTxt.getText();
            String customerAddress = customerAddressTxt.getText();
            String customerPostalCode = customerPostalCodeTxt.getText();
            String customerPhoneNumber = customerPhoneTxt.getText();
            String customerCountry = customerCountryComboBx.getValue();
            String customerDivision = customerDivisionComboBx.getValue();
            //test
            // System.out.println("Country: " + customerCountry + " Division: " + customerDivision);

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

                int tempDivisionId = DBACustomer.getDivisionIDFromName(customerDivision);
                DBACustomer.addCustomer(customerName, customerAddress, customerPostalCode, customerPhoneNumber, tempDivisionId);

                Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Customer Updated");
                alert.setContentText("Customer has been added.");
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
            alert.setContentText("Could not add customer. Please ensure no fields are left blank.");
            alert.showAndWait();
            //return;

        }
    }

    /**
     *
     * @param event the event of clicking the cancel button to return to the customer view screen
     * @throws Exception exceptions
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
}
