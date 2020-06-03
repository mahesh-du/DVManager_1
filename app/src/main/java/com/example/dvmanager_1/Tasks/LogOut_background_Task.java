package com.example.dvmanager_1.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.dvmanager_1.LoginActivity;
import com.example.dvmanager_1.Model.EmailModels.Email_List;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.dvmanager_1.Constants.firestore_email;
import static com.example.dvmanager_1.Tasks.LogOut_Task_Helper.searchEmail;
import static com.example.dvmanager_1.Tasks.LogOut_Task_Helper.signOut;
import static com.example.dvmanager_1.Tasks.LogOut_Task_Helper.updateEmailLoggedInAfterSignOut;

public class LogOut_background_Task extends AsyncTask<Void, Void, String> {

    Activity activity;
    HashMap<String, Object> params;
    ProgressDialog progressDialog;

    public LogOut_background_Task(Activity activity, HashMap<String, Object> params) {
        this.activity = activity;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Logging you out...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String resultTAG = null;

        FirebaseFirestore db                            = (FirebaseFirestore) params.get("firestoreINSTANCE");
        String email                                    = String.valueOf(params.get("email"));

        HashMap<String, Object> returnData = searchEmail(db,firestore_email, email);
        resultTAG = String.valueOf(returnData.get("resultTAG"));
        Email_List newEmailList = (Email_List) returnData.get("updatedEmailLIST");

        if(resultTAG.equals("TASK_SUCCESSFUL")){
            if(signOut()){
                if(updateEmailLoggedInAfterSignOut(db, firestore_email, newEmailList)){
                    resultTAG = "TASK_SUCCESSFUL";
                }else{resultTAG = "EMAIL_LOGGED_IN_AFTER_SIGN_OUT_FAILED";}
            }// no else part required.
        }else{resultTAG = "TASK_NOT_SUCCESSFUL";}

        return resultTAG;
    }

    @Override
    protected void onPostExecute(String resultTAG) {
        progressDialog.dismiss();
        String toastMESSAGE= null, logMESSAGE = null;
        switch (resultTAG){
            case "TASK_SUCCESSFUL":                         {toastMESSAGE = "Logged Out Successfully."; logMESSAGE = "TASK_SUCCESSFUL: Logged Out Successfully.";break;}
            case "EMAIL_LOGGED_IN_AFTER_SIGN_OUT_FAILED":   {toastMESSAGE = "Error Logging Out Failed. Please Retry."; logMESSAGE = "EMAIL_LOGGED_IN_AFTER_SIGN_OUT_FAILED: Error Logging Out Failed. Please Retry.";break;}
            case "TASK_NOT_SUCCESSFUL":                     {toastMESSAGE = "Error Logging Out. Please Retry."; logMESSAGE = "TASK_NOT_SUCCESSFUL: Task not Successful.";break;}
        }

        Toast.makeText(activity, toastMESSAGE, Toast.LENGTH_SHORT).show();
        Log.d("myCount", logMESSAGE);

        if (resultTAG.equals("TASK_SUCCESSFUL")){
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(activity, intent, null);
            activity.finish();
        }else{
            //TODO: revert changes.
        }
    }

}

