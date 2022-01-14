package controller;

import DBAccess.DBACustomer;
//import DBAccess.DBCountries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class TestScreen implements Initializable {

    public TableColumn idCol;
    public TableColumn nameCol;
    public TableView dataTable;

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle){

    }

    @FXML
    public void showMe(ActionEvent actionEvent){

        ObservableList<Customer> customerList = DBACustomer.getAllCustomers();

        for(Customer customer : customerList){
            System.out.println("Customer ID: " + customer.getCustomerId() + " Customer Name: " + customer.getCustomerName() + " Address: " + customer.getCustomerAddress() + " Postal Code: " + customer.getCustomerPostalCode() + " Phone Number: " + customer.getCustomerPhoneNumber() + " Division ID: " + customer.getCustomerDivisionId());
        }
    }

}
