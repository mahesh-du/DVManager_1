package com.example.dvmanager_1.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dvmanager_1.Constants;
import com.example.dvmanager_1.Model.Manage_devicesModel;
import com.example.dvmanager_1.Adapters.Manage_devices_RecyclerViewAdapter;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

public class Manage_devices_Fragment extends Fragment implements Constants {

    RecyclerView recyclerView;
    Manage_devices_RecyclerViewAdapter adapter;
    List<Manage_devicesModel> devicesList;

    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Institution_Path;
    public Manage_devices_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_devices, container, false);
        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();

        getActivity().setTitle("Manage Devices");

        swipeRefreshLayout = view.findViewById(R.id.manage_devices_swipeRefreshLayout);

        recyclerView = view.findViewById(R.id.manage_devices_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        devicesList = new ArrayList<>();

        loadData();
        devicesList.clear();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
                devicesList.clear();

            }
        });

        return view;
    }

    public void loadData()
    {
        db.collection(Institution_Path + firestore_device)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                Manage_devicesModel device = new Manage_devicesModel(
                                        document.getString(firestore_DEVICE_FIELD_Device_Name),    //TODO: add column names.
                                        document.getBoolean(firestore_DEVICE_FIELD_Blocked),
                                        document.getLong(firestore_DEVICE_FIELD_Scans_Today),
                                        document.getBoolean(firestore_DEVICE_FIELD_Logged_In),
                                        document.getString(firestore_DEVICE_FIELD_Logged_In_Email),
                                        document.getLong(firestore_DEVICE_FIELD_Battery_Remaining),
                                        document.getString(firestore_DEVICE_FIELD_Battery_Status),
                                        document.getString(firestore_DEVICE_FIELD_Network_Status),
                                        document.getString(firestore_DEVICE_FIELD_Actual_Position),
                                        document.getGeoPoint(firestore_DEVICE_FIELD_GPS_Location)
                                );
                                devicesList.add(device);

                                adapter = new Manage_devices_RecyclerViewAdapter(devicesList, getContext(), Institution_Path);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getContext(), "Refresh complete", Toast.LENGTH_LONG).show();
                            }
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

}
