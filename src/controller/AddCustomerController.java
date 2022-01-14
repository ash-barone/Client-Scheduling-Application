package controller;

import DBAccess.DBACustomer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private Button addCustomerBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField customerAddressTxt;

    @FXML
    private ComboBox<String> customerCountryComboBx;

    @FXML
    private ComboBox<String> customerDivisionComboBx;

    @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField customerNameTxt;

    @FXML
    private TextField customerPhoneTxt;

    @FXML
    private TextField customerPostalCodeTxt;

    private Customer tempCustomer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            customerCountryComboBx.setItems(DBACustomer.getAllDistinctCountries());

            customerDivisionComboBx.setItems(DBACustomer.getDivisionsByCountry(customerCountryComboBx.getValue()));

            customerCountryComboBx.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    if (t1 == null){
                        customerDivisionComboBx.getItems().clear();
                    }
                    else {

                        String tempCountry = customerCountryComboBx.getValue();
                        ObservableList<String> tempDivisionsByCountry = FXCollections.observableArrayList();
                        tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);

                        customerDivisionComboBx.setItems(tempDivisionsByCountry);
                        //customerDivisionComboBx.getSelectionModel().select(tempCustomer.getCustomerDivisionName());

                    }
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     *
     * @param event the event of clicking on the add customer button to add customer to db and return to the customer view screen
     * @throws Exception
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws Exception {

        //TODO Add customer stuff only thing done is navigation back to customer view
        try {

            //int customerId = Integer.parseInt(customerIdTxt.getText());
            String customerName = customerNameTxt.getText();
            String customerAddress = customerAddressTxt.getText();
            String customerPostalCode = customerPostalCodeTxt.getText();
            String customerPhoneNumber = customerPhoneTxt.getText();
            String customerCountry = customerCountryComboBx.getValue();
            String customerDivision = customerDivisionComboBx.getValue();
            System.out.println("Country: " + customerCountry + " Division: " + customerDivision);

            if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() || customerPhoneNumber.isEmpty() || customerCountry == null || customerDivision == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct.");
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
            alert.setContentText("Could not add customer.");
            alert.showAndWait();
            return;

        }
    }

    /**
     *
     * @param event the event of clicking the cancel button to return to the customer view screen
     * @throws Exception
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
