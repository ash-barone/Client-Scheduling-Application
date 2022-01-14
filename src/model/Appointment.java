package model;

import java.sql.Timestamp;

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

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    public String getApptDescription() {
        return apptDescription;
    }

    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType() {
        this.apptType = apptType;
    }

    public Timestamp getApptStartDateTime() {
        return apptStartDateTime;
    }

    public void setApptStartDateTime(Timestamp apptStartDateTime) {
        this.apptStartDateTime = apptStartDateTime;
    }

    public Timestamp getApptEndDateTime() {
        return apptEndDateTime;
    }

    public void setApptEndDateTime(Timestamp apptEndDateTime) {
        this.apptEndDateTime = apptEndDateTime;
    }

    public int getApptCustomerId() {
        return apptCustomerId;
    }

    public void setApptCustomerId(int apptCustomerId) {
        this.apptCustomerId = apptCustomerId;
    }

    public int getApptUserId() {
        return apptUserId;
    }

    public void setApptUserId(int apptUserId) {
        this.apptUserId = apptUserId;
    }

    public int getApptContactId() {
        return apptContactId;
    }

    public void setApptContactId(int apptContactId) {
        this.apptContactId = apptContactId;
    }

   public String getApptContactName() {
        return apptContactName;
    }

    public void setApptContactName(String apptContactName) {
        this.apptContactName = apptContactName;
    }
}
