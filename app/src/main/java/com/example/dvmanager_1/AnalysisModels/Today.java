package com.example.dvmanager_1.AnalysisModels;

import java.util.Date;
import java.util.List;

public class Today {

    private Date Date;
    private List<Entries_Model> Entries;

    public Today() {
    }

    public Today(Date date, List<Entries_Model> entries) {
        Date = date;
        Entries = entries;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public List<Entries_Model> getEntries() {
        return Entries;
    }

    public void setEntries(List<Entries_Model> entries) {
        Entries = entries;
    }
}
