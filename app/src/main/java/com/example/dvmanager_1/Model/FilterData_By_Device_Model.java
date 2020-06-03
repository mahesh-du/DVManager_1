package com.example.dvmanager_1.Model;

import com.google.firebase.firestore.GeoPoint;

public class FilterData_By_Device_Model {

    private String Device_Name;
    private Boolean Blocked;
    private Long Scans_Today;
    private Boolean Logged_In;
    private String Logged_In_Email;
    private Long Battery_Remaining;
    private String Battery_Status;
    private String Network_Status;
    private String Actual_Location;
    private GeoPoint GPS_Location;
    private Boolean isChecked;

    public FilterData_By_Device_Model() {
    }

    public FilterData_By_Device_Model(String device_Name, Boolean blocked, Long scans_Today, Boolean logged_In, String logged_In_Email, Long battery_Remaining, String battery_Status, String network_Status, String actual_Location, GeoPoint GPS_Location, Boolean isChecked) {
        Device_Name = device_Name;
        Blocked = blocked;
        Scans_Today = scans_Today;
        Logged_In = logged_In;
        Logged_In_Email = logged_In_Email;
        Battery_Remaining = battery_Remaining;
        Battery_Status = battery_Status;
        Network_Status = network_Status;
        Actual_Location = actual_Location;
        this.GPS_Location = GPS_Location;
        this.isChecked = isChecked;
    }

    public String getDevice_Name() {
        return Device_Name;
    }

    public void setDevice_Name(String device_Name) {
        Device_Name = device_Name;
    }

    public Boolean getBlocked() {
        return Blocked;
    }

    public void setBlocked(Boolean blocked) {
        Blocked = blocked;
    }

    public Long getScans_Today() {
        return Scans_Today;
    }

    public void setScans_Today(Long scans_Today) {
        Scans_Today = scans_Today;
    }

    public Boolean getLogged_In() {
        return Logged_In;
    }

    public void setLogged_In(Boolean logged_In) {
        Logged_In = logged_In;
    }

    public String getLogged_In_Email() {
        return Logged_In_Email;
    }

    public void setLogged_In_Email(String logged_In_Email) {
        Logged_In_Email = logged_In_Email;
    }

    public Long getBattery_Remaining() {
        return Battery_Remaining;
    }

    public void setBattery_Remaining(Long battery_Remaining) {
        Battery_Remaining = battery_Remaining;
    }

    public String getBattery_Status() {
        return Battery_Status;
    }

    public void setBattery_Status(String battery_Status) {
        Battery_Status = battery_Status;
    }

    public String getNetwork_Status() {
        return Network_Status;
    }

    public void setNetwork_Status(String network_Status) {
        Network_Status = network_Status;
    }

    public String getActual_Location() {
        return Actual_Location;
    }

    public void setActual_Location(String actual_Location) {
        Actual_Location = actual_Location;
    }

    public GeoPoint getGPS_Location() {
        return GPS_Location;
    }

    public void setGPS_Location(GeoPoint GPS_Location) {
        this.GPS_Location = GPS_Location;
    }

    public Boolean get_isChecked() {
        return isChecked;
    }

    public void set_isChecked(Boolean checked) {
        isChecked = checked;
    }
}
