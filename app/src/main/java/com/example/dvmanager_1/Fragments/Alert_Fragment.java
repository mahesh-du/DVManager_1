package com.example.dvmanager_1.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dvmanager_1.Adapters.Alerts_RecyclerViewAdapter;
import com.example.dvmanager_1.Model.AlertModels.Alert_BatteryModel;
import com.example.dvmanager_1.Model.AlertModels.Alert_Battery_ListModel;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import static com.example.dvmanager_1.Constants.KEY_institution_path;
import static com.example.dvmanager_1.Constants.firestore_alerts_battery;
import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class Alert_Fragment extends Fragment {

    RecyclerView recyclerView;
    Alerts_RecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Institution_Path.
    String Institution_Path;

    public Alert_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_alert, container, false);
        getActivity().setTitle("Alert");

        recyclerView = view.findViewById(R.id.alerts_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.alerts_swipeRefreshLayout);

        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();

        db.document(Institution_Path + firestore_alerts_battery)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        backgroundTask task = new backgroundTask(Institution_Path);
                        task.execute();
                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                backgroundTask task = new backgroundTask(Institution_Path);
                task.execute();
            }
        });

        return view;
    }
    public class backgroundTask extends AsyncTask<Void, Void, Alert_Battery_ListModel> {

        public String institutionPath;

        public backgroundTask(String institutionPath) {
            this.institutionPath = institutionPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Alert_Battery_ListModel doInBackground(Void... voids) {

            Alert_Battery_ListModel alerts;
            Task<DocumentSnapshot> task = db.document(institutionPath + firestore_alerts_battery).get();

            try {
                Tasks.await(task);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if(task.isSuccessful()){
                DocumentSnapshot d = task.getResult();
                alerts = d.toObject(Alert_Battery_ListModel.class);
                if(alerts.getAlert()!=null){
                    return alerts;
                }else{
                    Log.d("myCount", "AlertsFragment: List is Empty.");
                    return null;
                }
            }else{
                Log.d("myCount", "AlertsFragment: Task not Successful.");
                return null;
            }

        }

        @Override
        protected void onPostExecute(Alert_Battery_ListModel alerts) {

            if(alerts!=null){
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new Alerts_RecyclerViewAdapter(getContext(), alerts.getAlert());
                recyclerView.setAdapter(adapter);

                if(recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(),"Refresh complete",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(),"No Alerts found",Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}


