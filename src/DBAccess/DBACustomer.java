package DBAccess;

import Utility.UserLoginSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * class for db access for customers
 */
public class DBACustomer {

    /**
     * method to get all customer info from customer table in db
     *
     * @return list of all customers
     */
    public static ObservableList<Customer> getAllCustomers() {

        ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhoneNumber = rs.getString("Phone");
                String customerCountry = rs.getString("Country");
                String customerDivisionName = rs.getString("Division");
                int customerDivisionId = rs.getInt("Division_ID");
                Customer customer = new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerCountry, customerDivisionName, customerDivisionId);
                allCustomersList.add(customer);

                //test
                System.out.println("Customer ID: " + customerId + " Name: " + customerName + " Division ID: " + customerDivisionId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned list");

        return allCustomersList;
    }


    /**
     * method to check date conversion for local/virtual machine
     */
    public static void checkDateConversion() {

        System.out.println("CREATE DATE TEST");
        String sql = "SELECT Create_Date from customers";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("CREATE DATE: " + ts.toLocalDateTime().toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * method to get all customer ids
     *
     * @return list of customer ids
     */
    public static ObservableList<Integer> getAllCustomerIds() {

        ObservableList<Integer> allCustomerIdsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Customer_ID from customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int customerId = rs.getInt("Customer_ID");

                allCustomerIdsList.add(customerId);

                //test
                System.out.println("Customer ID: " + customerId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned ids list");

        return allCustomerIdsList;
    }

    /**
     * method to get all distinct countries
     *
     * @return list of all distinct countries
     */
    public static ObservableList<String> getAllDistinctCountries() throws SQLException {
        ObservableList<String> allDistinctCountriesList = FXCollections.observableArrayList();

        //sql statement for select distinct countries in countries table
        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT DISTINCT Country FROM countries");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String countryName = rs.getString("Country");

            allDistinctCountriesList.add(countryName);

            //test
            System.out.println("Each Country: " + countryName);
        }

        return allDistinctCountriesList;
    }

    /**
     * method to get divisions by country
     *
     * @return list of divisions by country
     */
    public static ObservableList<String> getDivisionsByCountry(String countryName) {
        ObservableList<String> divisionsByCountryList = FXCollections.observableArrayList();

        String divisionByCountry = " ";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT countries.Country, countries.Country_ID, first_level_divisions.Division, first_level_divisions.Division_ID FROM countries RIGHT OUTER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID WHERE countries.Country = ?");

            ps.setString(1, countryName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionByCountry = rs.getString("Division");

                divisionsByCountryList.add(divisionByCountry);

                //test
                System.out.println("Country: " + countryName + " Division: " + divisionByCountry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //sql statement will be join of countries and first_level_divisions on country_id match

        return divisionsByCountryList;
    }

    /**
     * method to get customer division id
     *
     * @return the specific customer division id
     */
    public static int getCustomerDivisionId(int customerID) throws SQLException {

        int divisionId = 0;
        try {
            //sql statement to select division id from customer table where customer id is specific thing
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Division_ID FROM customers WHERE Customer_ID = ?");

            ps.setInt(1, customerID);

            //int divisionId;
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionId = rs.getInt("Division_ID");

                //test
                System.out.println("Customer ID: " + customerID + " Division ID: " + divisionId);
            }
            ;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionId;
    }

    /**
     * method to get name of a division using the name input
     *
     * @param division the name of the division
     * @return the division id
     * @throws SQLException
     */
    public static int getDivisionIDFromName(String division) throws SQLException {

        int divisionId = 0;
        try {
            //sql statement to select division id from customer table where customer id is specific thing
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Division, Division_ID FROM first_level_divisions WHERE Division = ?");

            ps.setString(1, division);

            //int divisionId;
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionId = rs.getInt("Division_ID");

                //test
                System.out.println("Division ID: " + division + " Division ID: " + divisionId);
            }
            ;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionId;
    }


    /**
     * method to update customer on db table
     *
     * @return bool for whether it worked or not
     */
    public static boolean updateCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String customerDivision, int customerID) {

        try {
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement("UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?");

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhoneNumber);
            ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat));
            ps.setString(6, UserLoginSession.getUserLoggedIn().getUserName());
            ps.setInt(7, DBACustomer.getDivisionIDFromName(customerDivision));
            ps.setInt(8, customerID);

            ps.executeUpdate();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        //return true;
    }

    /**
     * method to add customer to db table
     *
     * @return bool for whether or not it worked
     */
    public static boolean addCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, int customerDivisionId) {

        try {
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhoneNumber);
            ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat).toString());
            ps.setString(6, UserLoginSession.getUserLoggedIn().getUserName());
            ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat).toString());
            ps.setString(8, UserLoginSession.getUserLoggedIn().getUserName());
            ps.setInt(9, customerDivisionId);

            ps.executeUpdate();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        //return true;
    }

    private static String getDivisionNameFromId(int customerDivisionId) {

        String divisionName = " ";
        try {
            //sql statement to select division id from customer table where customer id is specific thing
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Division FROM first_level_divisions WHERE Division_ID = ?");

            ps.setInt(1, customerDivisionId);

            //int divisionId;
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionName = rs.getString("Division_ID");

                //test
                System.out.println("Division Name: " + divisionName + " Division ID: " + customerDivisionId);
            }
            ;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionName;
    }


    /**
     * method to delete customer from db table
     *
     * @return bool for whether or not it worked
     */
    public static boolean deleteCustomer(int customerID) throws SQLException {

        Boolean cannotDelete = true;
        Boolean deleted = false;
        if (!DBAAppointment.getAllSelectedCustomerAppointments(customerID).isEmpty()) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("Cannot delete customer");
            alert.setContentText("Cannot delete a customer to has active appointments. \n Please delete all customer appointments first.");
            alert.showAndWait();
            deleted = false;
        } else {
            deleted = true;
        }
        if (DBAAppointment.getAllSelectedCustomerAppointments(customerID).isEmpty()) {
            try {
                PreparedStatement ps = JDBC.getConnection().prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");

                ps.setInt(1, customerID);

                ps.executeUpdate();

                deleted = true;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                deleted = false;
            }
            //deleted = true;
        } else {
            deleted = false;
        }
        //return true;
        return deleted;
    }

    public static ObservableList<String> getAllCustomerNames() {
        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Customer_Name from customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String customerName = rs.getString("Customer_Name");

                allCustomerNamesList.add(customerName);

                //test
                System.out.println("Customer Name: " + customerName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned ids list");

        return allCustomerNamesList;
    }

    public static String getCustomerNameFromId(int customerId) {
        String customerName = " ";
        int customerID;
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Customer_Name, Customer_ID FROM customers WHERE Customer_ID = ?");
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customerName = rs.getString("Customer_Name");
                customerID = rs.getInt("Customer_ID");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerName;
    }

    public static Integer getCustomerIdFromName(String apptCustomerName) {
        String customerName = " ";
        int customerID = 0;
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Customer_Name, Customer_ID FROM customers WHERE Customer_Name = ?");
            ps.setString(1, apptCustomerName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customerName = rs.getString("Customer_Name");
                customerID = rs.getInt("Customer_ID");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerID;
    }
}

