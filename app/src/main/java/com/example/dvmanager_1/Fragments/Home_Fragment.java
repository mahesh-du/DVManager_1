package com.example.dvmanager_1.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dvmanager_1.Adapters.FilterData_By_Device_RecyclerViewAdapter;
import com.example.dvmanager_1.Adapters.FilterData_By_Id_Adapter;
import com.example.dvmanager_1.Adapters.RecyclerViewAdapter;
import com.example.dvmanager_1.AnalysisModels.AnalysisModel;
import com.example.dvmanager_1.AnalysisModels.Entries_Model;
import com.example.dvmanager_1.AnalysisModels.Today;
import com.example.dvmanager_1.Constants;
import com.example.dvmanager_1.Model.EntryModel;
import com.example.dvmanager_1.Model.FilterData_By_Device_Model;
import com.example.dvmanager_1.Model.FilterData_By_ID_Model;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.example.dvmanager_1.Helper.DateToLongString;
import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

public class Home_Fragment extends Fragment implements Constants {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    RecyclerView filterData_recyclerView, filterData_ById_recyclerView;
    FilterData_By_Device_RecyclerViewAdapter filterData_recyclerView_Adapter;

    FilterData_By_Id_Adapter filterData_ById_recyclerView_Adapter;
    String Institution_Path;
    List<FilterData_By_Device_Model> devicesList;

    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DatePickerDialog dtp;
    public MaterialProgressBar progressBar;


    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        this.setRetainInstance(true);
        getActivity().setTitle("Home");

        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = getActivity().findViewById(R.id.Loading_ProgressBar);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final HashMap<String, Object> data = new HashMap<>();
        data.put("FILTER_BY", "DATE");
        data.put("START_DATE", DateToLongString(new Date()));
        data.put("END_DATE", DateToLongString(new Date()));
        recyclerView.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadData(data);
            }
        });

//        if(savedInstanceState!=null){
//            List<EntryModel> entry_data = (List<EntryModel>) savedInstanceState.getSerializable("DATA");
//            if(entry_data!=null){
//                adapter = new RecyclerViewAdapter(entry_data, getContext());
//                if(recyclerView.getVisibility() == View.GONE)
//                    recyclerView.setVisibility(View.VISIBLE);
//                recyclerView.setAdapter(adapter);
//            }
//            else{
//                Toast.makeText(getActivity(),"No entry found",Toast.LENGTH_LONG).show();
//                recyclerView.setVisibility(View.GONE);
//            }
//            swipeRefreshLayout.setRefreshing(false);
//            Toast.makeText(getActivity(),"Refresh complete",Toast.LENGTH_LONG).show();
//        }
//        else
            loadData(data);

        return view;
    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putSerializable("DATA",adapter.getDisplayedList());
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
        menu.findItem(R.id.action_filter).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_filter: {
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof Home_Fragment) {
                    showFilterDialog();
                    return true;
                }
            }
        }

        return false;
    }

    public void showFilterDialog() {
        View alertLayout = getLayoutInflater().inflate(R.layout.layout_filter_data, null);

        final TextInputEditText tiet_start_Date,tiet_end_Date, tiet_Id;
        final LinearLayout date_Linear_Layout, id_Linear_Layout, device_Linear_Layout;
        final CheckBox date_checkBox, id_checkBox, device_checkBox, device_entry_checkbox, device_exit_checkbox;

        filterData_recyclerView         = alertLayout.findViewById(R.id.layout_filter_data_By_Device_recyclerView);
        filterData_ById_recyclerView    = alertLayout.findViewById(R.id.layout_filter_data_By_Id_recyclerView);

        tiet_start_Date = alertLayout.findViewById(R.id.tiet_start_Date);
        tiet_end_Date   = alertLayout.findViewById(R.id.tiet_end_Date);
        tiet_Id         = alertLayout.findViewById(R.id.tiet_Id);

        date_Linear_Layout      = alertLayout.findViewById(R.id.layout_filter_data_By_Date_Linear_Layout);
        id_Linear_Layout        = alertLayout.findViewById(R.id.layout_filter_data_By_Id_Linear_Layout);
        device_Linear_Layout    = alertLayout.findViewById(R.id.layout_filter_data_Device_Linear_Layout);

        date_checkBox           = alertLayout.findViewById(R.id.layout_filter_data_By_Date_checkBox);
        id_checkBox             = alertLayout.findViewById(R.id.layout_filter_data_By_Id_checkBox);
        device_checkBox         = alertLayout.findViewById(R.id.layout_filter_data_By_Device_checkBox);
        device_entry_checkbox   = alertLayout.findViewById(R.id.layout_filter_data_By_Device_entry_checkBox);
        device_exit_checkbox    = alertLayout.findViewById(R.id.layout_filter_data_By_Device_exit_checkBox);

        loadDeviceList();

        load_Id_TASK load_ids = new load_Id_TASK(Institution_Path);
        load_ids.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        tiet_Id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData_ById_recyclerView_Adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        id_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    id_Linear_Layout.setVisibility(View.VISIBLE);
                else
                    id_Linear_Layout.setVisibility(View.GONE);
            }
        });

        date_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    date_Linear_Layout.setVisibility(View.VISIBLE);
                else
                    date_Linear_Layout.setVisibility(View.GONE);
            }
        });

        device_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //TODO: dont load device on checked change remove its visibility.
                    // load it in swipe refresh layout.
                    //loadDeviceList();
                    device_Linear_Layout.setVisibility(View.VISIBLE);

                }
                else
                    device_Linear_Layout.setVisibility(View.GONE);
            }
        });

        tiet_start_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dtp = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tiet_start_Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dtp.getDatePicker().setMaxDate(new Date().getTime());
                dtp.show();
            }
        });

        tiet_end_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dtp = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tiet_end_Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dtp.getDatePicker().setMaxDate(new Date().getTime());
                dtp.show();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Filter", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                HashMap<String,Object> data = new HashMap<>();

                if (id_checkBox.isChecked() && date_checkBox.isChecked() && device_checkBox.isChecked()){

                    Date start_Date = new Date();
                    Date end_Date = new Date();

                    try {
                        start_Date = new SimpleDateFormat("dd/MM/yyyy").parse(tiet_start_Date.getText().toString());
                        end_Date =  new SimpleDateFormat("dd/MM/yyyy").parse(tiet_end_Date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing filter dates: Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                    if(device_entry_checkbox.isChecked() && device_exit_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_BOTH");
                    }else if(device_entry_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_ENTRY");

                    }else if(device_exit_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_EXIT");
                    }

                    data.put("FILTER_BY", "DATE_ID_DEVICE");
                    data.put("START_DATE", DateToLongString(start_Date));
                    data.put("END_DATE", DateToLongString(end_Date));
                    data.put("FILTER_BY_ID_LIST", FilterData_By_Id_Adapter.dataList);
                    data.put("FILTER_BY_DEVICE_LIST", FilterData_By_Device_RecyclerViewAdapter.devicesList);

                }
                else if (date_checkBox.isChecked() && device_checkBox.isChecked()){
                            //both filters.
                    Date start_Date = new Date();
                    Date end_Date = new Date();

                    try {
                        start_Date = new SimpleDateFormat("dd/MM/yyyy").parse(tiet_start_Date.getText().toString());
                        end_Date =  new SimpleDateFormat("dd/MM/yyyy").parse(tiet_end_Date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing filter dates: Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                    if(device_entry_checkbox.isChecked() && device_exit_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_BOTH");
                    }else if(device_entry_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_ENTRY");

                    }else if(device_exit_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_EXIT");
                    }

                    data.put("FILTER_BY", "DATE_DEVICE");
                    data.put("START_DATE", DateToLongString(start_Date));
                    data.put("END_DATE", DateToLongString(end_Date));
                    data.put("FILTER_BY_DEVICE_LIST", FilterData_By_Device_RecyclerViewAdapter.devicesList);

                }
                else if (id_checkBox.isChecked() && date_checkBox.isChecked()){
                    //both filters.
                    Date start_Date = new Date();
                    Date end_Date = new Date();

                    try {
                        start_Date = new SimpleDateFormat("dd/MM/yyyy").parse(tiet_start_Date.getText().toString());
                        end_Date =  new SimpleDateFormat("dd/MM/yyyy").parse(tiet_end_Date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing filter dates: Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                    data.put("FILTER_BY", "DATE_ID");
                    data.put("START_DATE", DateToLongString(start_Date));
                    data.put("END_DATE", DateToLongString(end_Date));
                    data.put("FILTER_BY_ID_LIST", FilterData_By_Id_Adapter.dataList);

                }
                else if (id_checkBox.isChecked()){
                    Date start_Date = new Date();
                    Date end_Date = new Date();

                    try {
                        start_Date = new SimpleDateFormat("dd/MM/yyyy").parse(tiet_start_Date.getText().toString());
                        end_Date =  new SimpleDateFormat("dd/MM/yyyy").parse(tiet_end_Date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing filter dates: Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                    data.put("FILTER_BY", "ID");
                    data.put("START_DATE", DateToLongString(start_Date));
                    data.put("END_DATE", DateToLongString(end_Date));
                    data.put("FILTER_BY_ID_LIST", FilterData_By_Id_Adapter.dataList);
                }
                else if (date_checkBox.isChecked()){

                    Date start_Date = new Date();
                    Date end_Date = new Date();

                    try {
                        start_Date = new SimpleDateFormat("dd/MM/yyyy").parse(tiet_start_Date.getText().toString());
                        end_Date =  new SimpleDateFormat("dd/MM/yyyy").parse(tiet_end_Date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing filter dates: Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                    data.put("FILTER_BY", "DATE");
                    data.put("START_DATE", DateToLongString(start_Date));
                    data.put("END_DATE", DateToLongString(end_Date));

                }
                else if (device_checkBox.isChecked()){
                    data.put("FILTER_BY", "DEVICE");
                    //device filter.
                    if(device_entry_checkbox.isChecked() && device_exit_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_BOTH");
                    }else if(device_entry_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_ENTRY");

                    }else if(device_exit_checkbox.isChecked()){
                        data.put("FILTER_BY_DEVICE", "DEVICE_EXIT");
                    }
                    data.put("FILTER_BY_DEVICE_LIST", FilterData_By_Device_RecyclerViewAdapter.devicesList);
                }
                loadData(data);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        Log.d("TAG", date.toString());
        return date;
    }

    public List<String> getListOfPathsToGetEntryData(Date startDate, Date endDate){

        List<String> pathsList = new ArrayList<>();
        List<List<String>> yearMonthPathList = new ArrayList<>();
        Date dateNow = new Date();

        if(startDate.after(dateNow)){
            //TODO: show error.
        }else if(startDate.equals(dateNow)){
            //endDate = dateNow;
            Calendar calender = Calendar.getInstance();
            calender.setTime(startDate);
            int startDateYear = calender.get(Calendar.YEAR);
            int startDateMonth = calender.get(Calendar.MONTH);

            yearMonthPathList.add(getMonthsPath(String.valueOf(startDateYear),startDateMonth,startDateMonth, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
        }else if(startDate.before(dateNow)){

            if(endDate.before(dateNow)){
                Calendar calender = Calendar.getInstance();
                calender.setTime(startDate);
                int startDateYear = calender.get(Calendar.YEAR);
                calender.setTime(endDate);
                int endDateYear = calender.get(Calendar.YEAR);

                calender.setTime(startDate);
                int startDateMonth = calender.get(Calendar.MONTH);
                //int startDateDay = calender.get(Calendar.DATE);

                calender.setTime(endDate);
                int endDateMonth = calender.get(Calendar.MONTH);
                //int endDateDay = calender.get(Calendar.DATE);

                if(startDateYear == endDateYear){
                    yearMonthPathList.add(getMonthsPath(String.valueOf(startDateYear),startDateMonth,endDateMonth, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                    //TODO:store data in Models.

                }else if(startDateYear < endDateYear){

                    for (int i = startDateYear; i <= endDateYear ; i++) {

                        if(i == startDateYear)
                            yearMonthPathList.add(getMonthsPath(String.valueOf(startDateYear),startDateMonth,12, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                        else if (i == endDateYear)
                            yearMonthPathList.add(getMonthsPath(String.valueOf(startDateYear),1,endDateMonth, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                        else
                            yearMonthPathList.add(getMonthsPath(String.valueOf(startDateYear),1,12, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                    }
                }
            }else if(endDate.equals(dateNow) || endDate.after(dateNow)){
                endDate = dateNow;

                Calendar calender = Calendar.getInstance();
                calender.setTime(startDate);
                int startDateYear = calender.get(Calendar.YEAR);
                calender.setTime(endDate);
                int endDateYear = calender.get(Calendar.YEAR);

                calender.setTime(startDate);
                int startDateMonth = calender.get(Calendar.MONTH);

                calender.setTime(endDate);
                int endDateMonth = calender.get(Calendar.MONTH);

                if(startDateYear == endDateYear){
                    yearMonthPathList.add(getMonthsPath(String.valueOf(startDateYear),startDateMonth,endDateMonth, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));

                }else if(startDateYear < endDateYear){

                    for (int i = startDateYear; i <= endDateYear ; i++) {

                        if(i == startDateYear)
                            yearMonthPathList.add(getMonthsPath(String.valueOf(i),startDateMonth,12, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                        else if (i == endDateYear)
                            yearMonthPathList.add(getMonthsPath(String.valueOf(i),1,endDateMonth, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                        else
                            yearMonthPathList.add(getMonthsPath(String.valueOf(i),1,12, Institution_Path, firestore_document_ANALYSIS_STATISTICS_YEAR));
                    }
                }
            }
        }

        for (List<String> list_Of_Paths : yearMonthPathList){
            for (String path : list_Of_Paths){
                pathsList.add(path);
            }
        }
        return pathsList;
    }

    public List<Today> getData(Date startDate, Date endDate){

        List<String> paths = getListOfPathsToGetEntryData(startDate, endDate);

        List<AnalysisModel> analysisModelList = new ArrayList<>();

        for (String path : paths){
            AnalysisModel analysisModel = convertDocumentSnapshotToModel(getDocumentFromPath(path));

            if(analysisModel!= null){
                for (int i = 0; i < analysisModel.getData().size(); i++){
                Today data = analysisModel.getData().get(i);
                if(getZeroTimeDate(data.getDate()).before(getZeroTimeDate(startDate))){
                    analysisModel.getData().remove(i);
                    i--;}
                else if(getZeroTimeDate(data.getDate()).after(getZeroTimeDate(endDate))){
                    analysisModel.getData().remove(i);
                    i--;}
                }
                analysisModelList.add(analysisModel);
            }
        }

        List<Today> todayList = new ArrayList<>();
        if(analysisModelList!= null) {
            for (AnalysisModel analysisModel : analysisModelList) {
                for (Today data : analysisModel.getData())
                    todayList.add(data);
            }
            Log.d("myCount", "todayList document: " + todayList.size());
            return todayList;
        }
        else
            return null;
    }

    public AnalysisModel convertDocumentSnapshotToModel(DocumentSnapshot document){
        return document.toObject(AnalysisModel.class);
    }

    public DocumentSnapshot getDocumentFromPath(final String path){

        Task<DocumentSnapshot> task = db.document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("myCount","document recieved from: " + path);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("myCount","Unable to get document from: " + path + " /nERROR: " + e);
                    }
                });

        while(!task.isSuccessful()){
            Log.d("myCount","waiting for getDocumentFromPath." );
        }
        return task.getResult();
    }

    public List<String> getMonthsPath(String year, int startMonth, int endMonth, String Institution_Path, String analysis_to_Year_Path){

        //gets all the months String from startMonth till endMonth both included.
        List<String> yearMonthList = new ArrayList<>();
        for (int i = startMonth; i <=endMonth ; i++) {          // i from 0 to 11.

            StringBuilder yearMonthstr = new StringBuilder();
            yearMonthstr.append(Institution_Path);
            yearMonthstr.append(analysis_to_Year_Path);

            yearMonthstr.append("/");
            yearMonthstr.append(year);
            yearMonthstr.append("/");
            // january is 0 not 1.
            switch(i){
                case 0: {yearMonthstr.append("January"); break;}
                case 1: {yearMonthstr.append("February"); break;}
                case 2: {yearMonthstr.append("March"); break;}
                case 3: {yearMonthstr.append("April"); break;}
                case 4: {yearMonthstr.append("May"); break;}
                case 5: {yearMonthstr.append("June"); break;}
                case 6: {yearMonthstr.append("July"); break;}
                case 7: {yearMonthstr.append("August"); break;}
                case 8: {yearMonthstr.append("September"); break;}
                case 9: {yearMonthstr.append("October"); break;}
                case 10: {yearMonthstr.append("November"); break;}
                case 11: {yearMonthstr.append("December"); break;}
            }
            yearMonthList.add(yearMonthstr.toString());
        }
        return yearMonthList;
    }

    public List<EntryModel> makeCollectionsOfData(List<Today> todayList){

        List<EntryModel> Ids_and_ScanCount = new ArrayList<>();
        if(todayList.size()>0) {
            Collections.reverse(todayList);

            for (Today today : todayList) {

                for (Entries_Model entry : today.getEntries()) {
                    Long Id = entry.getId();
                    if (checkIDexists_in_Ids_and_ScanCount(Ids_and_ScanCount, Id, entry.getCount()) == null)
                    {
                        String entry_Status = entry.getDetails().getEntry().get(entry.getDetails().getEntry().size() - 1).getStatus();
                        String entry_gate = entry.getDetails().getEntry().get(entry.getDetails().getEntry().size() - 1).getGate().getEntry_Gate();
                        String exit_gate = entry.getDetails().getEntry().get(entry.getDetails().getEntry().size() - 1).getGate().getExit_Gate();
                        Date entry_Time = entry.getDetails().getEntry().get(entry.getDetails().getEntry().size() - 1).getTime().getEntry_Time();
                        Date exit_Time = entry.getDetails().getEntry().get(entry.getDetails().getEntry().size() - 1).getTime().getExit_Time();

                        List<String> name_blocked_ImageStringList = getNameForId(Id);
                        Ids_and_ScanCount.add(new EntryModel(Id, name_blocked_ImageStringList.get(0),
                                Boolean.parseBoolean(name_blocked_ImageStringList.get(1)), entry.getCount(),
                                entry_Status, entry_gate, exit_gate, entry_Time, exit_Time,
                                name_blocked_ImageStringList.get(2)));
                    }
                    else
                        Ids_and_ScanCount = checkIDexists_in_Ids_and_ScanCount(Ids_and_ScanCount, Id, entry.getCount());
                }
            }
        return Ids_and_ScanCount;
        }
        else
            return null;
    }

    public List<String> getNameForId(Long Id){
        String path = Institution_Path + firestore_ids + "/" + Id;
        Task<DocumentSnapshot> task =db.document(path)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Log.d("myCount","Name recieved from path.");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("myCount","Error retrieving Name from path: " + e);
                                                }
                                            });

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if(task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            String name = "";
            String blocked = "";
            String imageString = "";
            if (document.exists()) {
                name = Objects.requireNonNull(document.get(firestore_IDS_FIELD_Name)).toString();
                blocked = Objects.requireNonNull(document.get(firestore_IDS_FIELD_Blocked)).toString();
                imageString = String.valueOf(document.get(firestore_IDS_FIELD_Image));

            } else {
                name = "No Document";
                blocked = "false";
                imageString = "No Document";
            }
            List<String> details = new ArrayList<>();
            details.add(name);
            details.add(blocked);
            details.add(imageString);
            return details;
        }else{
            Log.d("myCount","getNameForId: " + task.getException());
            return null;
        }
    }

    public List<EntryModel> checkIDexists_in_Ids_and_ScanCount(List<EntryModel> Ids_and_ScanCount, Long Id, Long entry_Count){

        for (int i = 0; i < Ids_and_ScanCount.size(); i++) {
            Long entry_Id = Ids_and_ScanCount.get(i).getID();
            Boolean compareId_and_entry_id = entry_Id.toString().equals(Id.toString());
            if(compareId_and_entry_id) {
                Ids_and_ScanCount.get(i).setTotal_Entry(Ids_and_ScanCount.get(i).getTotal_Entry() + entry_Count);
                return Ids_and_ScanCount;
            }
        }
        return null;
    }

    public void loadData(HashMap<String,Object> data){
        //TODO: Make startDate and endDate dynamic.

        backgroundTask task = new  backgroundTask(data);
        task.execute();
    }

    public List<EntryModel> filterDataByDevice(List<EntryModel> entry_data, String filter_By, List<FilterData_By_Device_Model> devices){

        List<String> gates_list = new ArrayList<>();
        List<EntryModel> newEntry_data = new ArrayList<>();

        if(devices!=null || devices.size()>0) {
            for (FilterData_By_Device_Model device : devices) {
                if ((filter_By.equals("DEVICE_ENTRY") || filter_By.equals("DEVICE_EXIT") || filter_By.equals("DEVICE_BOTH"))
                        && device.get_isChecked()) {
                    gates_list.add(device.getActual_Location());
                }
            }
        }
        if(entry_data!=null) {
            for (EntryModel entry : entry_data) {
                for (String gate : gates_list) {
                    if (filter_By.equals("DEVICE_ENTRY") && entry.getEntry_Gate().equals(gate)) {
                        newEntry_data.add(entry);
                    } else if ((filter_By.equals("DEVICE_EXIT") || filter_By.equals("DEVICE_BOTH"))
                            && (entry.getExit_Gate().equals(gate) || entry.getExit_Gate().equals("Still In"))) {
                        newEntry_data.add(entry);
                    }
                }
            }
        }else{
            return null;
        }
        return newEntry_data;
    }

    public List<EntryModel> filterDataById(List<EntryModel> entry_data, List<FilterData_By_ID_Model> idsList){

        if(idsList!=null) {
            if (idsList.size() > 0) {
                for (FilterData_By_ID_Model id : idsList) {
                    if (entry_data != null) {
                        if (entry_data.size() > 0) {
                            if (!(id.getChecked())) {
                                for (int i = 0; i < entry_data.size(); ) {
                                        EntryModel entry = entry_data.get(i);
                                        if (String.valueOf(entry.getID()).equals(String.valueOf(id.getId()))){
                                            Log.d("myCount", "removed:" + entry.getID());
                                            entry_data.remove(i);
                                        }
                                        else
                                            i++;
                                }
                            }
                        } else {
                            Log.d("myCount", "filterDataById(): entry_data is null or empty.");
                            return null;
                        }
                    } else {
                        Log.d("myCount", "filterDataById(): entry_data is null or empty.");
                        return null;
                    }
                }
            }
        }
        return entry_data;
    }

    public class backgroundTask extends AsyncTask<Void, Void, List<EntryModel>> {

        public HashMap<String,Object> data;
//        ProgressDialog progressDialog;


        public backgroundTask(HashMap<String, Object> data) {
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Loading Data...");
//            progressDialog.show();
             progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<EntryModel> doInBackground(Void... voids) {
            String filter_By = String.valueOf(data.get("FILTER_BY"));
            List<EntryModel> entry_data = new ArrayList<>();

            //TODO: makeCollectionsOfData after filtering.
            switch (filter_By){
                case "DATE":{
                    entry_data = makeCollectionsOfData(getData(new Date(Long.parseLong(String.valueOf(data.get("START_DATE")))),
                        new Date(Long.parseLong(String.valueOf(data.get("END_DATE"))))));
                    return entry_data;
                }
                case "ID":{
                    entry_data = makeCollectionsOfData(getData(new Date(Long.parseLong(DateToLongString(new Date()))),
                            new Date(Long.parseLong(DateToLongString(new Date())))));

                    List<FilterData_By_ID_Model> idsList = (List<FilterData_By_ID_Model>) (data.get("FILTER_BY_ID_LIST"));

                    entry_data = filterDataById(entry_data, idsList);
                    return entry_data;
                }
                case "DATE_ID":{
                    entry_data = makeCollectionsOfData(getData(new Date(Long.parseLong(String.valueOf(data.get("START_DATE")))),
                            new Date(Long.parseLong(String.valueOf(data.get("END_DATE"))))));
                    List<FilterData_By_ID_Model> idsList = (List<FilterData_By_ID_Model>) (data.get("FILTER_BY_ID_LIST"));

                    entry_data = filterDataById(entry_data, idsList);
                    return entry_data;
                }
                case "DATE_ID_DEVICE":{
                    entry_data = makeCollectionsOfData(getData(new Date(Long.parseLong(String.valueOf(data.get("START_DATE")))),
                            new Date(Long.parseLong(String.valueOf(data.get("END_DATE"))))));

                    List<FilterData_By_ID_Model> idsList = (List<FilterData_By_ID_Model>) (data.get("FILTER_BY_ID_LIST"));

                    entry_data = filterDataById(entry_data, idsList);

                    String filter_By_Device = String.valueOf(data.get("FILTER_BY_DEVICE"));
                    List<FilterData_By_Device_Model> devices = (List<FilterData_By_Device_Model>) data.get("FILTER_BY_DEVICE_LIST");

                    entry_data = filterDataByDevice(entry_data, filter_By_Device, devices);
                    return entry_data;
                }
                case "DEVICE":{
                    entry_data = makeCollectionsOfData(getData(new Date(), new Date()));
                    String filter_By_Device = String.valueOf(data.get("FILTER_BY_DEVICE"));
                    List<FilterData_By_Device_Model> devices = (List<FilterData_By_Device_Model>) data.get("FILTER_BY_DEVICE_LIST");
                    entry_data = filterDataByDevice(entry_data, filter_By_Device, devices);
                    return entry_data;
                }
                case "DATE_DEVICE":{
                    entry_data = makeCollectionsOfData(getData(new Date(Long.parseLong(String.valueOf(data.get("START_DATE")))),
                            new Date(Long.parseLong(String.valueOf(data.get("END_DATE"))))));
                    String filter_By_Device = String.valueOf(data.get("FILTER_BY_DEVICE"));
                    List<FilterData_By_Device_Model> devices = (List<FilterData_By_Device_Model>) data.get("FILTER_BY_DEVICE_LIST");

                    entry_data = filterDataByDevice(entry_data, filter_By_Device, devices);
                    return entry_data;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<EntryModel> entry_data) {
//            progressDialog.dismiss();
            progressBar.setVisibility(View.GONE);

            if(entry_data!=null){
                adapter = new RecyclerViewAdapter(entry_data, getContext());
                if(recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
            }
            else{
                Toast.makeText(getActivity(),"No entry found",Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
            }
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),"Refresh complete",Toast.LENGTH_LONG).show();
        }
    }

    public void loadDeviceList()
    {
        db.collection(Institution_Path+ firestore_device)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            devicesList = new ArrayList<>(100);
                            for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                FilterData_By_Device_Model device = new FilterData_By_Device_Model(
                                        document.getString(firestore_DEVICE_FIELD_Device_Name),    //TODO: add column names.
                                        document.getBoolean(firestore_DEVICE_FIELD_Blocked),
                                        document.getLong(firestore_DEVICE_FIELD_Scans_Today),
                                        document.getBoolean(firestore_DEVICE_FIELD_Logged_In),
                                        document.getString(firestore_DEVICE_FIELD_Logged_In_Email),
                                        document.getLong(firestore_DEVICE_FIELD_Battery_Remaining),
                                        document.getString(firestore_DEVICE_FIELD_Battery_Status),
                                        document.getString(firestore_DEVICE_FIELD_Network_Status),
                                        document.getString(firestore_DEVICE_FIELD_Actual_Position),
                                        document.getGeoPoint(firestore_DEVICE_FIELD_GPS_Location),
                                        false
                                );
                                devicesList.add(device);
                            }
                            filterData_recyclerView.setHasFixedSize(false);
                            filterData_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            filterData_recyclerView_Adapter = new FilterData_By_Device_RecyclerViewAdapter(devicesList, getContext());
                            filterData_recyclerView.setAdapter(filterData_recyclerView_Adapter);
                            //swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(),"Refresh complete",Toast.LENGTH_LONG).show();

                        }
                        else
                            Toast.makeText(getContext(),"No devices found.",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Error: " + e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public class load_Id_TASK extends AsyncTask<Void, Void, List<FilterData_By_ID_Model>> {

        public String institutionPath;

        public load_Id_TASK(String institutionPath) {
            this.institutionPath = institutionPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<FilterData_By_ID_Model> doInBackground(Void... voids) {

            Task<QuerySnapshot> getIdsListTask = db.collection(institutionPath + firestore_ids).get();
            List<Long> ids = new ArrayList<>(100);
            final List<FilterData_By_ID_Model> idsWithDetails = new ArrayList<>(100);

            try {
                Tasks.await(getIdsListTask);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if(getIdsListTask.isSuccessful()){
                QuerySnapshot docs = getIdsListTask.getResult();
                if(docs.size()>0){
                    for(DocumentSnapshot doc : docs.getDocuments()){
                        ids.add(Long.parseLong(doc.getId()));
                    }
                }else{return null;}
            }else{return null;}

            List<String> pathList = new ArrayList<>(100);
            for (Long id : ids)
                pathList.add(institutionPath + firestore_ids + "/" + id);

            List<Task<DocumentSnapshot>> taskList = new ArrayList<>();
            for (String taskPath : pathList){
                Task<DocumentSnapshot> task = db.document(taskPath).get();
                taskList.add(task);
            }

            Task<List<Object>> task = Tasks.whenAllSuccess(taskList);

            try {
                Tasks.await(task);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if(task.isSuccessful()){

                for (Object obj : Objects.requireNonNull(task.getResult())){
                    DocumentSnapshot doc = (DocumentSnapshot) obj;
                    if(doc.exists()){
                        FilterData_By_ID_Model id = new FilterData_By_ID_Model();
                        id.setId(Long.parseLong((doc.getId())));
                        id.setName(doc.getString(firestore_IDS_FIELD_Name));
                        id.setProfile_Picture(doc.getString(firestore_IDS_FIELD_Image));

                        idsWithDetails.add(id);
                    }
                }
            }
            else {
                return null;
            }

            return idsWithDetails;
        }

        @Override
        protected void onPostExecute(List<FilterData_By_ID_Model> id_data) {

            if(id_data!=null){
                filterData_ById_recyclerView.setHasFixedSize(false);
                filterData_ById_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                filterData_ById_recyclerView_Adapter = new FilterData_By_Id_Adapter(id_data, getContext());
                filterData_ById_recyclerView.setAdapter(filterData_ById_recyclerView_Adapter);

                if(filterData_ById_recyclerView.getVisibility() == View.GONE)
                    filterData_ById_recyclerView.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(),"load_Id_TASK(): Refresh complete",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(),"load_Id_TASK(): No id found",Toast.LENGTH_LONG).show();
                filterData_ById_recyclerView.setVisibility(View.GONE);
            }
        }
    }

}
