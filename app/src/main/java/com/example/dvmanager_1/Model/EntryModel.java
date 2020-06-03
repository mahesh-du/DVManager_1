package com.example.dvmanager_1.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class EntryModel implements Serializable {

    private Long ID;
    private String Name;
    private Boolean Blocked;
    private Long Total_Entry;
    private String Entry_Status;
    private String Entry_Gate;
    private String Exit_Gate;
    private Date Entry_Time;
    private Date Exit_Time;
    private String Profile_ImageByteString;

    public EntryModel() {
    }

    public EntryModel(Long ID, Long total_Entry, String entry_Status, String entry_Gate, String exit_Gate, Date entry_Time, Date exit_Time) {
        this.ID = ID;
        Total_Entry = total_Entry;
        Entry_Status = entry_Status;
        Entry_Gate = entry_Gate;
        Exit_Gate = exit_Gate;
        Entry_Time = entry_Time;
        Exit_Time = exit_Time;
    }

    public EntryModel(Long ID, String name, Boolean blocked, Long total_Entry, String entry_Status, String entry_Gate, String exit_Gate, Date entry_Time, Date exit_Time, String profile_ImageByteString) {
        this.ID = ID;
        Name = name;
        Blocked = blocked;
        Total_Entry = total_Entry;
        Entry_Status = entry_Status;
        Entry_Gate = entry_Gate;
        Exit_Gate = exit_Gate;
        Entry_Time = entry_Time;
        Exit_Time = exit_Time;
        Profile_ImageByteString = profile_ImageByteString;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getBlocked() {
        return Blocked;
    }

    public void setBlocked(Boolean blocked) {
        Blocked = blocked;
    }

    public Long getTotal_Entry() {
        return Total_Entry;
    }

    public void setTotal_Entry(Long total_Entry) {
        Total_Entry = total_Entry;
    }

    public String getEntry_Status() {
        return Entry_Status;
    }

    public void setEntry_Status(String entry_Status) {
        Entry_Status = entry_Status;
    }

    public String getEntry_Gate() {
        return Entry_Gate;
    }

    public void setEntry_Gate(String entry_Gate) {
        Entry_Gate = entry_Gate;
    }

    public String getExit_Gate() {
        return Exit_Gate;
    }

    public void setExit_Gate(String exit_Gate) {
        Exit_Gate = exit_Gate;
    }

    public Date getEntry_Time() {
        return Entry_Time;
    }

    public void setEntry_Time(Date entry_Time) {
        Entry_Time = entry_Time;
    }

    public Date getExit_Time() {
        return Exit_Time;
    }

    public void setExit_Time(Date exit_Time) {
        Exit_Time = exit_Time;
    }

    public String getProfile_ImageByteString() {
        return Profile_ImageByteString;
    }

    public void setProfile_ImageByteString(String profile_ImageByteString) {
        Profile_ImageByteString = profile_ImageByteString;
    }
}
