package com.example.dvmanager_1.AnalysisModels;

import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.dvmanager_1.Helper.convert_Date_To;

public class AnalysisHelper {


    public static List<Integer> getChartData(List<Today> todayList)
    {
        List<Integer> hourList = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));   //TODO: works when size is 23 instead
                                                                                                                    // of 24.
        String logMessage = "";
        if(todayList.size()>0) {
            for (Today today : todayList) {
                if (today.getEntries().size() > 0) {
                    for (Entries_Model entries_model : today.getEntries()) {
                        List<Entry_Model> entry_modelList = entries_model.getDetails().getEntry();
                        if (entry_modelList.size() > 0) {
                            for (Entry_Model entry_model : entry_modelList)
                            {
                                int entryHour = Integer.parseInt(convert_Date_To("HOUR_OF_DAY", entry_model.getTime().getEntry_Time()));
                                hourList.set(entryHour,hourList.get(entryHour) + 1);

                            }

                        } else
                            logMessage = "getPeopleInsideCount(): No Entry in getEntry.";
                    }
                } else
                    logMessage = "getPeopleInsideCount(): No Entries in getEntries.";
            }
        }
        else
            logMessage = "getPeopleInsideCount(): todayList is empty.";

        Log.d("TAG", logMessage);
        return hourList;
    }

    public static void setChart(List<Today> todayList, LineChart chart, String filter, String descriptionText)
    {
        List<Entry> entries = new ArrayList<>();
        LineData lineData = new LineData();
        LineDataSet dataSet;
        switch (filter)
        {
            case "TODAY": {
                List<Integer> hourList = getChartData(todayList);
                int index = 0;
                for (Integer hour : hourList){
                    entries.add(new Entry(index,hour));
                    index++;
                }
                dataSet = new LineDataSet(entries, "Entries");
                dataSet.setCircleHoleRadius(1.5f); // styling, ...
                dataSet.setLineWidth(2);
                dataSet.setDrawFilled(true);
                dataSet.setValueTextSize(19f);

                dataSet.setDrawValues(true);

                lineData.addDataSet(dataSet);
                chart.setData(lineData);
                break;
            }
            case "WEEK": {
                break;
            }
        }

        Description desc = new Description();
        desc.setText(descriptionText);
        chart.setDescription(desc);
        chart.animateXY(2000, 2000);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setGranularity(1f);
        chart.setVisibleXRangeMaximum(4f);
        chart.getAxisLeft().setSpaceBottom(30f);

        // see highlighting values to get listener when one taps on a value... to get its details.

        chart.invalidate(); // refresh

    }

    public static String getTotalEntries(List<Today> todayList)
    {
        int totalEntries = 0;
        String logMessage = "";
        if(todayList.size()>0) {
            for (Today today : todayList) {
                if (today.getEntries().size() > 0)
                    for (Entries_Model entry : today.getEntries())
                        totalEntries += entry.getCount();
//                else
//                    totalEntries = 0;

            }
            logMessage = "getTotalEntries(): " + totalEntries;
        }
        else
            logMessage = "getTotalEntries(): todayList isEmpty.";


        Log.d("TAG", logMessage);
        return String.valueOf(totalEntries);
    }

    public static String getPeopleInsideCount(List<Today> todayList)
    {
        int peopleInsideCount = 0;
        String logMessage = "";
        if(todayList.size()>0) {
            for (Today today : todayList) {
                if (today.getEntries().size() > 0) {
                    for (Entries_Model entries_model : today.getEntries()) {
                        List<Entry_Model> entry_modelList = entries_model.getDetails().getEntry();
                        if (entry_modelList.size() > 0) {
                            Entry_Model lastEntry = entry_modelList.get(entry_modelList.size() - 1);
                            if (lastEntry.getStatus().equals("In"))
                                peopleInsideCount += 1;
                        } else
                            logMessage = "getPeopleInsideCount(): No Entry in getEntry.";
                    }
                } else
                    logMessage = "getPeopleInsideCount(): No Entries in getEntries.";
            }
        }
        else
            logMessage = "getPeopleInsideCount(): todayList is empty.";

        Log.d("TAG", logMessage);
        return String.valueOf(peopleInsideCount);
    }

    public static int getPeakHours(List<Today> todayList)
    {
        List<Integer> hourList = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));

        String logMessage = "";
        if(todayList.size()>0) {
            for (Today today : todayList) {
                if (today.getEntries().size() > 0) {
                    for (Entries_Model entries_model : today.getEntries()) {
                        List<Entry_Model> entry_modelList = entries_model.getDetails().getEntry();
                        if (entry_modelList.size() > 0) {
                            for (Entry_Model entry_model : entry_modelList)
                            {
                                int entryHour = Integer.parseInt(Objects.requireNonNull(convert_Date_To("HOUR_OF_DAY", entry_model.getTime().getEntry_Time())));
                                int exitHour = Integer.parseInt(Objects.requireNonNull(convert_Date_To("HOUR_OF_DAY", entry_model.getTime().getExit_Time())));
                                for (int i = entryHour; i <= exitHour; i++) {
                                    hourList.set(i,hourList.get(i) + 1);
                                }
                            }

                        } else
                            logMessage = "getPeopleInsideCount(): No Entry in getEntry.";
                    }
                } else
                    logMessage = "getPeopleInsideCount(): No Entries in getEntries.";
            }
        }
        else
            logMessage = "getPeopleInsideCount(): todayList is empty.";

        int peakHour = 0;
        int peakHourPeopleCount = 0;
        for (Integer peopleCount : hourList)
        {
            if(peopleCount > peakHourPeopleCount){                     //TODO: if two hours have same peakHourPeopleCount both of them should be returned.
                peakHourPeopleCount = peopleCount;                     // it returns only the first hour.
                peakHour = hourList.indexOf(peopleCount);
            }
        }

        logMessage += " PeakHour: " + peakHour;
        Log.d("TAG", logMessage);

        return peakHour;
    }

    public static String getPeakHourRange(List<Today> todayList)
    {
        int peakHour = getPeakHours(todayList);                     //If returns 0 that means from 0 - 1.
        Log.d("TAG", "getPeakHourRange(): " + peakHour + ":00 - "  + (peakHour + 1) + ":00");
        return peakHour + ":00 - "  + (peakHour + 1) + ":00";
    }

}
