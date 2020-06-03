package com.example.dvmanager_1.AnalysisModels;

public class Entries_Model{

    private Long Count;
    private Long Id;
    private Details Details;

    public Entries_Model() {
    }

    public Entries_Model(Long count, Long id, Details details) {
        Count = count;
        Id = id;
        Details = details;
    }

    public Long getCount() {
        return Count;
    }

    public void setCount(Long count) {
        Count = count;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Details getDetails() {
        return Details;
    }

    public void setDetails(Details details) {
        Details = details;
    }
}
