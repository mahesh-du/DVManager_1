package com.example.dvmanager_1.AnalysisModels;

import java.util.List;

public class Details {

    private List<Entry_Model> Entry;
    
    public Details(){}

    public Details(List<Entry_Model> entry) {
        Entry = entry;
    }

    public List<Entry_Model> getEntry() {
        return Entry;
    }

    public void setEntry(List<Entry_Model> entry) {
        Entry = entry;
    }
}
