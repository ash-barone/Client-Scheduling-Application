package model;

/**
 * The Contact class for creating and accessing Contact objects and their attributes.
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * Contact object constructor
     * @param contactId the contact id
     * @param contactName the contact name
     * @param contactEmail the contact email
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Method to get the contact id
     * @return the contact id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Method to set the contact id
     * @param contactId the contact id
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Method to get the contact name
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Method to set the contact name
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Method to get the contact email
     * @return the contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * method to set the contact email
     * @param contactEmail the contact email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
