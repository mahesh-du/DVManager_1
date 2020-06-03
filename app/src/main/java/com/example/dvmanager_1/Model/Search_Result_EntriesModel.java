package com.example.dvmanager_1.Model;

public class Search_Result_EntriesModel {

    private String Entry_Time;
    private String Exit_Time;
    private String Entry_Status;
    private String Entry_Gate;
    private String Exit_Gate;

    public Search_Result_EntriesModel(String entry_Time, String exit_Time, String entry_Status, String entry_Gate, String exit_Gate) {
        Entry_Time = entry_Time;
        Exit_Time = exit_Time;
        Entry_Status = entry_Status;
        Entry_Gate = entry_Gate;
        Exit_Gate = exit_Gate;
    }

    public String getEntry_Time() {
        return Entry_Time;
    }

    public void setEntry_Time(String entry_Time) {
        Entry_Time = entry_Time;
    }

    public String getExit_Time() {
        return Exit_Time;
    }

    public void setExit_Time(String exit_Time) {
        Exit_Time = exit_Time;
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
}
