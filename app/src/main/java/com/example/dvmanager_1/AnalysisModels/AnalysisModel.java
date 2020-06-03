package com.example.dvmanager_1.AnalysisModels;

import java.util.List;

public class AnalysisModel {

    private List<Today> Data;

    public AnalysisModel() {
    }

    public AnalysisModel(List<Today> data) {
        Data = data;
    }

    public List<Today> getData() {
        return Data;
    }

    public void setData(List<Today> data) {
        Data = data;
    }
}
