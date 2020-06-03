package com.example.dvmanager_1.AnalysisModels;

public class Gate_map {

    private String Entry_Gate;
    private String Exit_Gate;

    public Gate_map() {
    }

    public Gate_map(String entry_Gate, String exit_Gate) {
        Entry_Gate = entry_Gate;
        Exit_Gate = exit_Gate;
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
