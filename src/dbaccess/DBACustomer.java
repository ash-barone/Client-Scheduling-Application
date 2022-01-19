package dbaccess;

import utility.UserLoginSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is the Database Access management class for CRUD operations of customers. Included are methods to get specific lists of customers, add and update appointment, and get lists of specific data as required for operations.
 */
public class DBACustomer {

    /**
     * Method to get all customer rows.
     * @return list of all customers
     */
    public static ObservableList<Customer> getAllCustomers() {

        ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();

        try {
            //sql statement to get all customers
            String sql = "SELECT * from customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

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
                //System.out.println("Customer ID: " + customerId + " Name: " + customerName + " Division ID: " + customerDivisionId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned list");

        return allCustomersList;
    }

    /**
     * Method to add customer row.
     * @param customerName the customer name
     * @param customerAddress the customer address
     * @param customerPostalCode the customer postal code
     * @param customerPhoneNumber the customer phone number
     * @param customerDivisionId the customer division id
     * @return bool for if it worked or not
     */
    public static boolean addCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, int customerDivisionId) {

        try {
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement to add customer row
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhoneNumber);
            ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat));
            ps.setString(6, UserLoginSession.getUserLoggedIn().getUserName());
            ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormat));
            ps.setString(8, UserLoginSession.getUserLoggedIn().getUserName());
            ps.setInt(9, customerDivisionId);

            ps.executeUpdate();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }

    /**
     * Method to update customer row
     * @param customerName the customer name
     * @param customerAddress the customer address
     * @param customerPostalCode the customer postal code
     * @param customerPhoneNumber the customer phone number
     * @param customerDivision the customer division
     * @param customerID the customer id
     * @return bool for whether it worked or not
     */
    public static boolean updateCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String customerDivision, int customerID) {

        try {
            //set date pattern to match db table
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //sql statement to update customer row
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

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

    }

    /**
     * Method to get all distinct countries from the countries table for use in add update customer forms
     * @return list of all distinct countries
     * @throws SQLException exception
     */
    public static ObservableList<String> getAllDistinctCountries() throws SQLException {

        ObservableList<String> allDistinctCountriesList = FXCollections.observableArrayList();

        //sql statement for select distinct countries in countries table
        String sql = "SELECT DISTINCT Country FROM countries";

        PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String countryName = rs.getString("Country");

            allDistinctCountriesList.add(countryName);

            //test
            //System.out.println("Each Country: " + countryName);
        }

        return allDistinctCountriesList;
    }

    /**
     * Method to get divisions by country for use in add and update customer forms.
     * @param countryName the country name
     * @return list of divisions by country
     */
    public static ObservableList<String> getDivisionsByCountry(String countryName) {

        ObservableList<String> divisionsByCountryList = FXCollections.observableArrayList();

        String divisionByCountry;

        try {
            //sql statement to get all divisions for specific country
            String sql = "SELECT countries.Country, countries.Country_ID, first_level_divisions.Division, first_level_divisions.Division_ID FROM countries JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID WHERE countries.Country = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setString(1, countryName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionByCountry = rs.getString("Division");

                divisionsByCountryList.add(divisionByCountry);

                //test
                //System.out.println("Country: " + countryName + " Division: " + divisionByCountry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionsByCountryList;
    }

    /**
     * Method to get id of a division using the name.
     * @param division the name of the division
     * @return the division id
     * @throws SQLException exception
     */
    public static int getDivisionIDFromName(String division) throws SQLException {

        int divisionId = 0;

        try {
            //sql statement to select division id from a name
            String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setString(1, division);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionId = rs.getInt("Division_ID");

                //test
                //System.out.println("Division ID: " + division + " Division ID: " + divisionId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionId;
    }

    /**
     * Method to delete customer row.
     * @param customerID the customer id
     * @return bool for if it worked
     * @throws SQLException exception
     */
    public static boolean deleteCustomer(int customerID) throws SQLException {

        boolean deleted;

        if (DBAAppointment.getAllSelectedCustomerAppointments(customerID).isEmpty()) {

            try {
                //sql statement to delete customer row
                String sql = "DELETE FROM customers WHERE Customer_ID = ?";

                PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

                ps.setInt(1, customerID);

                ps.executeUpdate();

                deleted = true;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                deleted = false;
            }
        } else {
            deleted = false;
        }
        return deleted;
    }

    /**
     * Method to get all customer names for use in combo box
     * @return list of all customer names
     */
    public static ObservableList<String> getAllCustomerNames() {
        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();

        try {
            //sql statement to get all customer names
            String sql = "SELECT Customer_Name from customers";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String customerName = rs.getString("Customer_Name");

                allCustomerNamesList.add(customerName);

                //test
                //System.out.println("Customer Name: " + customerName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //test
        //System.out.println("returned ids list");

        return allCustomerNamesList;
    }

    /**
     * Method to get customer name from id for use in combo boxes.
     * @param customerId the id of the customer
     * @return the customer name
     */
    public static String getCustomerNameFromId(int customerId) {

        String customerName = " ";

        try {
            //sql statement to get customer name from id
            String sql = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customerName = rs.getString("Customer_Name");
                //int customerID = rs.getInt("Customer_ID"); //for testing

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerName;
    }

    /**
     * Method to get customer id from name.
     * @param apptCustomerName the name of the customer
     * @return the customer id
     */
    public static Integer getCustomerIdFromName(String apptCustomerName) {
        int customerID = 0;

        try {
            //sql statement to get customer id from name
            String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name = ?";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ps.setString(1, apptCustomerName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //String customerName = rs.getString("Customer_Name"); //for testing
                customerID = rs.getInt("Customer_ID");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerID;
    }

    /**
     * Method to get number of customers in each distinct division.
     * @return list with count of customers in each division
     */
    public static ObservableList<String> getAllCustomerCountByDivision() {

        ObservableList<String> customerCountByDivision = FXCollections.observableArrayList();

        customerCountByDivision.add("Customers by Distinct Division: \n");

        try{
            //sql statement to get number of customers per distinct divisions
            String sql = "SELECT first_level_divisions.Division, COUNT(customers.Division_ID) FROM first_level_divisions JOIN customers ON first_level_divisions.Division_ID = customers.Division_ID GROUP BY customers.Division_ID;\n";

            PreparedStatement ps = JDBCAccess.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String divisionName = rs.getString("first_level_divisions.Division");
                String customerCount = rs.getString("COUNT(customers.Division_ID)");

                String eachDivision = "\nDivision: " + divisionName + "\n" + "Number of " + divisionName + " Customers: " + customerCount +"\n";

                customerCountByDivision.add(eachDivision);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return customerCountByDivision;
    }
}

