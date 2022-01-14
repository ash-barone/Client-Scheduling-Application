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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {

    //TODO name the combo box things

    @FXML
    private Button cancelBtn;

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

    @FXML
    private Button updateCustomerBtn;

    private Customer tempCustomer;

    private int id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerCountryComboBx.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (t1 == null){
                    customerDivisionComboBox.getItems().clear();
                }
                else {

                    String tempCountry = customerCountryComboBx.getValue();
                    ObservableList<String> tempDivisionsByCountry = FXCollections.observableArrayList();
                    tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);

                    customerDivisionComboBox.setItems(tempDivisionsByCountry);
                    //customerDivisionComboBox.getSelectionModel().select(tempCustomer.getCustomerDivisionName());

                }
            }
        });
    }

    /**
     *
     * @param event the event of clicking on the update customer button to update the customer in db then return to customer view screen
     * @throws Exception
     */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws Exception {

        //TODO Update customer stuff only thing done is navigation back to customer view

        try {

            int customerId = Integer.parseInt(customerIdTxt.getText());
            String customerName = customerNameTxt.getText();
            String customerAddress = customerAddressTxt.getText();
            String customerPostalCode = customerPostalCodeTxt.getText();
            String customerPhoneNumber = customerPhoneTxt.getText();
            String customerCountry = customerCountryComboBx.getValue();
            String customerDivision = customerDivisionComboBox.getValue();

            if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() || customerPhoneNumber.isEmpty() || customerCountry == null|| customerDivision == null) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct.");
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


    public void sendCustomer (Customer customer) throws SQLException {

        //set temp part to be passed for removal upon saving
        tempCustomer = customer;

        /**
         * get index for part so that modified part can maintain its same index after modified
         */
        //id = DBACustomer.ge;
        //System.out.println(index + " is the index");

        //fill in all your labels
        customerIdTxt.setText(String.valueOf(customer.getCustomerId()));
        customerNameTxt.setText(customer.getCustomerName());
        customerAddressTxt.setText(customer.getCustomerAddress());
        customerPostalCodeTxt.setText(customer.getCustomerPostalCode());
        customerPhoneTxt.setText((customer.getCustomerPhoneNumber()));

        //set country combo box
        customerCountryComboBx.setItems(DBACustomer.getAllDistinctCountries());
        customerCountryComboBx.getSelectionModel().select(tempCustomer.getCustomerCountry());//TODO figure this out lol

        String tempCountry = tempCustomer.getCustomerCountry();
        System.out.println("tempCountry = " + tempCountry + "\n");
        ObservableList<String> tempDivisionsByCountry = FXCollections.observableArrayList();
        tempDivisionsByCountry = DBACustomer.getDivisionsByCountry(tempCountry);
        System.out.println("tempDivisionByCountry = " +tempDivisionsByCountry);

        System.out.println("tempCustomer's Divison: " + tempCustomer.getCustomerDivisionName());
        //set division combo box
        customerDivisionComboBox.setItems(tempDivisionsByCountry);
        customerDivisionComboBox.getSelectionModel().select(tempCustomer.getCustomerDivisionName());


        //customerDivisionComboBox.setItems(DBACustomer.getDivisionsByCountry(tempCustomer.getCustomerCountry()));
        //customerDivisionComboBox.getSelectionModel().select(tempCustomer.getCustomerDivisionName());
    }
}
