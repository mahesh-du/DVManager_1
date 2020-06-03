package com.example.dvmanager_1.AnalysisModels;

public class Entry_Model {

    private String Status;
    private Gate_map Gate;
    private Time_map Time;

    public Entry_Model() {
    }

    public Entry_Model(String status, Gate_map gate, Time_map time) {
        Status = status;
        Gate = gate;
        Time = time;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Gate_map getGate() {
        return Gate;
    }

    public void setGate(Gate_map gate) {
        Gate = gate;
    }

    public Time_map getTime() {
        return Time;
    }

    public void setTime(Time_map time) {
        Time = time;
    }
}
