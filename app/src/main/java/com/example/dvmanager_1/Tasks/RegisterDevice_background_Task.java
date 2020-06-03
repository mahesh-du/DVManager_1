package com.example.dvmanager_1.Tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.dvmanager_1.LoginActivity;
import com.example.dvmanager_1.MainActivity;
import com.example.dvmanager_1.Model.EmailModels.Email_List;
import com.example.dvmanager_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.dvmanager_1.Constants.firestore_email;
import static com.example.dvmanager_1.Tasks.RegisterDevice_Task_Helper.createEmail;
import static com.example.dvmanager_1.Tasks.RegisterDevice_Task_Helper.isEmailUseable;
import static com.example.dvmanager_1.Tasks.RegisterDevice_Task_Helper.set_isEmailCreatedTrue;

public class RegisterDevice_background_Task extends AsyncTask<Void, Void, String> {

    AppCompatActivity activity;
    HashMap<String, Object> params;
    ProgressDialog progressDialog;

    public RegisterDevice_background_Task(AppCompatActivity activity, HashMap<String, Object> params) {
        this.activity = activity;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Registering your Device...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected String doInBackground(Void... voids) {

        String result = null;

        FirebaseFirestore db            = (FirebaseFirestore) params.get("firestoreINSTANCE");
        FirebaseAuth firebaseAuth       = (FirebaseAuth) params.get("firebaseAUTH");
        String institutionNAME          = String.valueOf(params.get("institutionNAME"));
        String email                    = String.valueOf(params.get("email"));
        String password                 = String.valueOf(params.get("password"));

        HashMap<String, Object> data = isEmailUseable(db, firestore_email, email, institutionNAME);
        String resultTAG = String.valueOf(data.get("resultTAG"));
        Email_List emails = (Email_List) data.get("emailLIST");
        int index_email_was_found_at = (int) data.get("email_found_at_INDEX");

        if(resultTAG.equals("EMAIL_USEABLE")){
            if (createEmail(firebaseAuth, email, password)){
                if (set_isEmailCreatedTrue(db, emails, index_email_was_found_at, firestore_email)){
                    result = "TASK_SUCCESSFUL";
                }else{result = "set_isEmailCreatedTrue_FAILED";}
            }else{result = "createEmail_FAILED";}
        }else{result = resultTAG;}

        return result;
    }

    @Override
    protected void onPostExecute(String resultTAG) {
        progressDialog.dismiss();
        String toastMESSAGE = null, logMESSAGE = null;

        switch (resultTAG){
            case "TASK_SUCCESSFUL":{toastMESSAGE = "Email Registered Successfully."; logMESSAGE = "TASK_SUCCESSFUL: Email Registered Successfully."; break;}
            case "set_isEmailCreatedTrue_FAILED":{toastMESSAGE = "Email Creation Failed."; logMESSAGE = "set_isEmailCreatedTrue_FAILED: isEmailCreated wasn't set to TRUE."; break;}
            case "createEmail_FAILED":{toastMESSAGE = "Email Creation Failed."; logMESSAGE = "createEmail_FAILED: Email Creation Failed."; break;}

            case "EMAIL_LOGGED_IN":{toastMESSAGE = "Email you entered is already in use by another Device."; logMESSAGE = "EMAIL_LOGGED_IN: This Email's loggedIn is TRUE."; break;}
            case "EMAIL_BLOCKED":{toastMESSAGE = "This Email is Blocked. Kindly use another Email."; logMESSAGE = "EMAIL_BLOCKED: Email is blocked."; break;}
            case "EMAIL_ALREADY_CREATED":{toastMESSAGE = "Email was already created."; logMESSAGE = "EMAIL_ALREADY_CREATED: isEmailCreatedTrue already set to TRUE."; break;}
            case "EMAIL_NOT_FOR_MANAGER":{toastMESSAGE = "This Email doesn't have Manager Level Privileges."; logMESSAGE = "EMAIL_NOT_FOR_MANAGER: This Email doesn't have Manager Level Privileges."; break;}
            case "EMAIL_NOT_FOR_THIS_INSTITUTION":{toastMESSAGE = "Email doesn't belong to your Institution."; logMESSAGE = "EMAIL_NOT_FOR_THIS_INSTITUTION: Email doesn't belong to your Institution."; break;}
            case "EMAIL_NOT_FOUND":{toastMESSAGE = "Email not found. Please Retry."; logMESSAGE = "EMAIL_NOT_FOUND: Email not found."; break;}

            case "EMAIL_LIST_EMPTY_ERROR":{toastMESSAGE = "Email not found. Please Retry."; logMESSAGE = "EMAIL_LIST_EMPTY_ERROR: Email List was empty or null."; break;}
            case "EMAIL_TASK_NOT_SUCCESSFUL_ERROR":{toastMESSAGE = "Error fetching Email Credentials. Please Retry."; logMESSAGE = "EMAIL_TASK_NOT_SUCCESSFUL_ERROR: Task not Successful."; break;}
            case "EMAIL_TRY_CATCH_ERROR":{toastMESSAGE = "An Error occurred. Please Retry."; logMESSAGE = "EMAIL_TRY_CATCH_ERROR: An Exception occurred."; break;}
        }

        Toast.makeText(activity, toastMESSAGE, Toast.LENGTH_SHORT).show();
        Log.d("myCount", logMESSAGE);
        if (resultTAG.equals("TASK_SUCCESSFUL")) {
//            logOut();
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(activity, intent, null);
            activity.finish();
        }else{
            //TODO: revert changes.
        }
    }

    public void logOut() {
                HashMap<String, Object> data = new HashMap<>();
                data.put("firestoreINSTANCE", (FirebaseFirestore) params.get("firestoreINSTANCE"));
                data.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                LogOut_background_Task logOut_background_task = new LogOut_background_Task(activity, data);
                logOut_background_task.execute();
    }
}
