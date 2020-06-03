package com.example.dvmanager_1.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dvmanager_1.Adapters.My_Ids_RecyclerViewAdapter;
import com.example.dvmanager_1.Model.Student_Id_Model;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.dvmanager_1.Constants.KEY_institution_path;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Address;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Admission_No;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Age;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Email;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Gender;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Image;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Name;
import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Phone_Number;
import static com.example.dvmanager_1.Constants.firestore_ids;
import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

public class My_Ids_Fragment extends Fragment {

    RecyclerView myIds_recyclerView;
    My_Ids_RecyclerViewAdapter adapter;
    SwipeRefreshLayout myIds_swipeRefreshLayout;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Institution_Path.
    String Institution_Path;

    public My_Ids_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ids, container, false);
        getActivity().setTitle("My Id's");
        setHasOptionsMenu(true);

        myIds_recyclerView = view.findViewById(R.id.myIds_recyclerView);
        myIds_swipeRefreshLayout = view.findViewById(R.id.myIds_swipeRefreshLayout);

        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();

        backgroundTask task = new backgroundTask(Institution_Path);
        task.execute();

        myIds_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myIds_swipeRefreshLayout.setRefreshing(true);
                backgroundTask task = new backgroundTask(Institution_Path);
                task.execute();
            }
        });

        return view;
    }


    public class backgroundTask extends AsyncTask<Void, Void, List<Student_Id_Model>> {

        public String institutionPath;

        public backgroundTask(String institutionPath) {
            this.institutionPath = institutionPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Student_Id_Model> doInBackground(Void... voids) {

            Task<QuerySnapshot> getIdsListTask = db.collection(institutionPath + firestore_ids).get();
            List<Long> ids = new ArrayList<>();
            final List<Student_Id_Model> idsWithDetails = new ArrayList<>();

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

            List<String> pathList = new ArrayList<>();
            for (Long id : ids)
                pathList.add(institutionPath + firestore_ids + "/" + id);

            List<Task<DocumentSnapshot>> taskList = new ArrayList<>();
            for (String taskPath : pathList){
                Task<DocumentSnapshot> task = db.document(taskPath).get();
                taskList.add(task);
            }
            Log.d("MY_IDS", db.getFirestoreSettings().isPersistenceEnabled()+"");

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
                        Student_Id_Model id = new Student_Id_Model();
                        id.setId(Long.parseLong((doc.getId())));
                        id.setName(doc.getString(firestore_IDS_FIELD_Name));
                        id.setAge(doc.getLong(firestore_IDS_FIELD_Age));
                        id.setGender(doc.getString(firestore_IDS_FIELD_Gender));
                        id.setEmail(doc.getString(firestore_IDS_FIELD_Email));
                        id.setPhone_No(doc.getLong(firestore_IDS_FIELD_Phone_Number));
                        id.setAdmission_No(doc.getLong(firestore_IDS_FIELD_Admission_No));
                        id.setAddress(doc.getString(firestore_IDS_FIELD_Address));
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
        protected void onPostExecute(List<Student_Id_Model> id_data) {

            if(id_data!=null){
                myIds_recyclerView.setHasFixedSize(false);
                myIds_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new My_Ids_RecyclerViewAdapter(getContext(), id_data);
                myIds_recyclerView.setAdapter(adapter);

                if(myIds_recyclerView.getVisibility() == View.GONE)
                    myIds_recyclerView.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(),"Refresh complete",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(),"No id found",Toast.LENGTH_LONG).show();
                myIds_recyclerView.setVisibility(View.GONE);
            }
            myIds_swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.findItem(R.id.action_search).setVisible(true);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        //change color of close button in searchView.
        ImageView searchClose = searchView.findViewById (androidx.appcompat.R.id.search_close_btn);
        //change color
        searchClose.setColorFilter (Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_search: {
                    return true;
                }
            }

        return false;
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}
