package com.example.dvmanager_1.Model;

public class Search_Result_Entries_RawModel {

    private Object Entry_Status;
    private Object Gate;
    private Object DateTime;

    public Search_Result_Entries_RawModel(Object entry_Status, Object gate, Object dateTime) {
        Entry_Status = entry_Status;
        Gate = gate;
        DateTime = dateTime;
    }

    public Object getEntry_Status() {
        return Entry_Status;
    }

    public Object getGate() {
        return Gate;
    }

    public Object getDateTime() {
        return DateTime;
    }
}
