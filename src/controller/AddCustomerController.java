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

/**
 * This class is the controller class for the Add Customer form. Inside are methods which allow the user to add a new customer to the connected database. Navigation back to Customer View is included as well.
 */
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
     * ***LAMBDA EXPRESSION*** The initialize method is used here to set the items for the Country combo box and the Division combo box. A Lambda Expression is used for adding a listener that changes the Division combo box items based on selection in the Country combo box.
     * @param url The url
     * @param resourceBundle The resource bundle
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
     * Add customer method for saving an added Customer to the connected database when the Add button is clicked. Provides customer error messages for blank fields or issues adding the customer. When add is successful, the application returns to the Customer View screen.
     * @param event The event of clicking on the Add button.
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) {

        try {

            String customerName = customerNameTxt.getText();
            String customerAddress = customerAddressTxt.getText();
            String customerPostalCode = customerPostalCodeTxt.getText();
            String customerPhoneNumber = customerPhoneTxt.getText();
            String customerCountry = customerCountryComboBx.getValue();
            String customerDivision = customerDivisionComboBx.getValue();
            //test
            // System.out.println("Country: " + customerCountry + " Division: " + customerDivision);

            //custom error message for fields left blank
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

        } catch (IOException | SQLException throwables){
            throwables.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Could not add customer. Please ensure no fields are left blank.");
            alert.showAndWait();
            //return;

        }
    }

    /**
     * Method for exiting the add form and returning to the Customer View screen.
     * @param event The event of clicking the Cancel button.
     * @throws Exception The exception for failed screen change
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
