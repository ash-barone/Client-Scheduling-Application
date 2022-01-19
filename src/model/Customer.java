package model;

/**
 * The Customer class for creating and accessing Customer objects and their attributes.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private String customerCountry;
    private String customerDivisionName;
    private int customerDivisionId;

    /**
     * Customer object constructor
     * @param customerId the customer id
     * @param customerName the customer name
     * @param customerAddress the customer address
     * @param customerPostalCode the customer postal code
     * @param customerPhoneNumber the customer phone number
     * @param customerCountry the customer country
     * @param customerDivisionName the customer division name
     * @param customerDivisionId the customer division id
     */
    public Customer(int customerId, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String customerCountry, String customerDivisionName, int customerDivisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerCountry = customerCountry;
        this.customerDivisionName = customerDivisionName;
        this.customerDivisionId = customerDivisionId;
    }

    /**
     * Method to get the customer id
     * @return the customer id
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Method to set the customer id
     * @param customerId the customer id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Method to get the customer name
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Method to set the customer name
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Method to get the customer address
     * @return the customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Method to set the customer address
     * @param customerAddress the customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Method to get the customer postal code
     * @return the customer postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * Method to set the customer postal code
     * @param customerPostalCode the customer postal code
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     * Method to get the customer phone number
     * @return the customer phone number
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Method to set the customer phone number
     * @param customerPhoneNumber the customer phone number
     */
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    /**
     * Method to get the customer country
     * @return the customer country
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * Method to set the customer country
     * @param customerCountry the customer country
     */
    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    /**
     * Method to get the customer division name
     * @return the customer division name
     */
    public String getCustomerDivisionName() {
        return customerDivisionName;
    }

    /**
     * Method to set the customer Division name
     * @param customerDivisionName the customer Division name
     */
    public void setCustomerDivisionName(String customerDivisionName) {
        this.customerDivisionName = customerDivisionName;
    }

    /**
     * Method to get the customer division id
     * @return the customer division id
     */
    public int getCustomerDivisionId() {
        return customerDivisionId;
    }

    /**
     * Method to set the customer division id
     * @param customerDivisionId the customer division id
     */
    public void setCustomerDivisionId(int customerDivisionId) {
        this.customerDivisionId = customerDivisionId;
    }
}
