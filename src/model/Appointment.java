package model;

import java.sql.Timestamp;

/**
 * The Appointment class for creating and accessing Appointment objects and their attributes.
 */
public class Appointment {

    private int apptId;
    private String apptTitle;
    private String apptDescription;
    private String apptLocation;
    private String apptType;
    private Timestamp apptStartDateTime;
    private Timestamp apptEndDateTime;
    private int apptCustomerId;
    private int apptUserId;
    private int apptContactId;
    private String apptContactName;

    /**
     * The constructor for Appointment objects.
     * @param apptId the appt id
     * @param apptTitle the appt title
     * @param apptDescription the appt description
     * @param apptLocation the appt location
     * @param apptType the appt type
     * @param apptStartDateTime the appt start time and date
     * @param apptEndDateTime the appt end time and date
     * @param apptCustomerId the appt customer id
     * @param apptUserId the appt user id
     * @param apptContactId the appt contact id
     * @param apptContactName the appt contact name
     */
    public Appointment(int apptId, String apptTitle, String apptDescription, String apptLocation, String apptType, Timestamp apptStartDateTime, Timestamp apptEndDateTime, int apptCustomerId, int apptUserId, int apptContactId, String apptContactName) {
        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.apptStartDateTime = apptStartDateTime;
        this.apptEndDateTime = apptEndDateTime;
        this.apptCustomerId = apptCustomerId;
        this.apptUserId = apptUserId;
        this.apptContactId = apptContactId;
        this.apptContactName = apptContactName;
    }

    /**
     * Method to get appt id.
     * @return appt id
     */
    public int getApptId() {
        return apptId;
    }

    /**
     * Method to set appt id
     * @param apptId the appt id
     */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    /**
     * Method to get the appt title.
     * @return the appt title
     */
    public String getApptTitle() {
        return apptTitle;
    }

    /**
     * Method to set the appt title
     * @param apptTitle the appt tile
     */
    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    /**
     * Method to get the appt description
     * @return the appt description
     */
    public String getApptDescription() {
        return apptDescription;
    }

    /**
     * Method to set the appt description
     * @param apptDescription the appt description
     */
    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }

    /**
     * Method to get the appt location
     * @return the appt location
     */
    public String getApptLocation() {
        return apptLocation;
    }

    /**
     * Method to set the appt location
     * @param apptLocation the appt location
     */
    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    /**
     * Method to get the appt type
     * @return the appt type
     */
    public String getApptType() {
        return apptType;
    }

    /**
     * Method to set the appt type
     * @param apptType the appt type
     */
    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    /**
     * Method to get the appt start
     * @return the appt stat
     */
    public Timestamp getApptStartDateTime() {
        return apptStartDateTime;
    }

    /**
     * Method to set the appt start
     * @param apptStartDateTime the appt start
     */
    public void setApptStartDateTime(Timestamp apptStartDateTime) {
        this.apptStartDateTime = apptStartDateTime;
    }

    /**
     * Method to get the appt end
     * @return the appt end
     */
    public Timestamp getApptEndDateTime() {
        return apptEndDateTime;
    }

    /**
     * Method to set the appt end
     * @param apptEndDateTime the appt end
     */
    public void setApptEndDateTime(Timestamp apptEndDateTime) {
        this.apptEndDateTime = apptEndDateTime;
    }

    /**
     * Method to get the appt customer id
     * @return the appt customer id
     */
    public int getApptCustomerId() {
        return apptCustomerId;
    }

    /**
     * Method for setting the appt customer id
     * @param apptCustomerId the appt customer id
     */
    public void setApptCustomerId(int apptCustomerId) {
        this.apptCustomerId = apptCustomerId;
    }

    /**
     * Method for getting the appt user id
     * @return the appt user id
     */
    public int getApptUserId() {
        return apptUserId;
    }

    /**
     * Method for setting the appt user id
     * @param apptUserId the appt user id
     */
    public void setApptUserId(int apptUserId) {
        this.apptUserId = apptUserId;
    }

    /**
     * Method for getting the appt contact id
     * @return the appt contact id
     */
    public int getApptContactId() {
        return apptContactId;
    }

    /**
     * Method for setting the appt contact id
     * @param apptContactId the appt contact id
     */
    public void setApptContactId(int apptContactId) {
        this.apptContactId = apptContactId;
    }

    /**
     * Method for getting the appt contact name
     * @return the appt contact name
     */
   public String getApptContactName() {
        return apptContactName;
    }

    /**
     * Method for setting the appt contact name
     * @param apptContactName the appt contact name
     */
    public void setApptContactName(String apptContactName) {
        this.apptContactName = apptContactName;
    }
}
