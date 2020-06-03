package com.example.dvmanager_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.dvmanager_1.Tasks.RegisterDevice_background_Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.dvmanager_1.Constants.firestore_INSTITUTIONS_FIELD_INSTITUTION_Names;
import static com.example.dvmanager_1.Constants.firestore_institution;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterDeviceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputEditText et_email, et_password;
    AppCompatSpinner sp_institutionName;
    Button btn_register_Device;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);

        firebaseAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.tiet_register_device_email);
        et_password = findViewById(R.id.tiet_register_device_password);
        sp_institutionName = findViewById(R.id.sp_register_device_institution_Name);
        btn_register_Device = findViewById(R.id.btn_register_Device);

        sp_institutionName.setOnItemSelectedListener(this);
        btn_register_Device.setOnClickListener(this);

        // load Spinner data.
        setInstitutionNameSpinner setinstitutionnameSpinner = new setInstitutionNameSpinner(RegisterDeviceActivity.this, db);
        setinstitutionnameSpinner.execute();

    }

    @Override
    public void onClick(View view) {
        final String email, password, institutionName;

        email = et_email.getText().toString();
        password = et_password.getText().toString();
        institutionName = String.valueOf(sp_institutionName.getSelectedItem());

        if(email.isEmpty()){
            et_email.setError("Provide your Email first!");
            et_email.requestFocus();
        }else if (password.isEmpty()) {
            et_password.setError("Set your password");
            et_password.requestFocus();
        }else if (!(email.isEmpty() && password.isEmpty())) {

            HashMap<String, Object> data = new HashMap<>();
            data.put("firestoreINSTANCE", db);
            data.put("firebaseAUTH", firebaseAuth);
            data.put("email",email);
            data.put("password", password);
            data.put("institutionNAME", institutionName);

            RegisterDevice_background_Task registerDevice = new RegisterDevice_background_Task( RegisterDeviceActivity.this,data);
            registerDevice.execute();

        }else{
            Toast.makeText(RegisterDeviceActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class setInstitutionNameSpinner extends AsyncTask<Void, Void, List<String>> {

        Context context;
        FirebaseFirestore db;
        ProgressDialog progressDialog;

        public setInstitutionNameSpinner(Context context, FirebaseFirestore db) {
            this.context = context;
            this.db = db;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> institutionNameList;
            Task<DocumentSnapshot> task = db.document(firestore_institution)
                    .get();

            try {
                Tasks.await(task);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Log.d("myCount", "Error in setInstitutionNameSpinner: " + e);
            }

            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                institutionNameList = (List<String>) doc.get(firestore_INSTITUTIONS_FIELD_INSTITUTION_Names);

            }else
                return null;

            return institutionNameList;

        }

        @Override
        protected void onPostExecute(List<String> institutionNameList) {
            if (institutionNameList!= null|| institutionNameList.size()>0){
                ArrayAdapter<String> sp_Institution_Name_Adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, institutionNameList);
                sp_Institution_Name_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_institutionName.setAdapter(sp_Institution_Name_Adapter);
            }
            else
                Toast.makeText(context, "No Institution Exists.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
