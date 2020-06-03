package com.example.dvmanager_1.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.dvmanager_1.MainActivity;
import com.example.dvmanager_1.Model.EmailModels.Email_List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.dvmanager_1.Constants.KEY_institution_path;
import static com.example.dvmanager_1.Constants.firestore_email;
import static com.example.dvmanager_1.Helper.putValueInSharedPreferences;
import static com.example.dvmanager_1.Tasks.Login_Task_Helper.login_isEmailUseable;
import static com.example.dvmanager_1.Tasks.Login_Task_Helper.signIn;
import static com.example.dvmanager_1.Tasks.Login_Task_Helper.updateEmailLoggedInAfterSignIn;

public class Login_background_Task extends AsyncTask<Void, Void, String> {

    Activity activity;
    HashMap<String, Object> params;
    ProgressDialog progressDialog;

    public Login_background_Task(Activity activity, HashMap<String, Object> params) {
        this.activity = activity;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Logging you in...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected String doInBackground(Void... voids) {

        String resultTAG = null;

        FirebaseFirestore db                            = (FirebaseFirestore) params.get("firestoreINSTANCE");
        FirebaseAuth firebaseAuth                       = (FirebaseAuth) params.get("firebaseAUTH");
        String email                                    = String.valueOf(params.get("email"));
        String password                                 = String.valueOf(params.get("password"));
        String institutionPath                          = String.valueOf(params.get("institutionPATH"));

        HashMap<String, Object> resultData = login_isEmailUseable(db, firestore_email, email, institutionPath);
        Email_List emails;
        int email_found_at_INDEX;
        resultTAG = String.valueOf(resultData.get("emailResultTAG"));
        Log.d("myCount", resultTAG);

        if(resultTAG.equals("TASK_SUCCESSFUL_isEmailUseable")){

            emails = (Email_List) resultData.get("emailList");
            email_found_at_INDEX = (int) resultData.get("email_found_at_INDEX");

            if(signIn(firebaseAuth, email, password)){
                if(updateEmailLoggedInAfterSignIn(db, emails, email_found_at_INDEX)){

                    putValueInSharedPreferences(activity, KEY_institution_path, institutionPath);
                    resultTAG = "TASK_SUCCESSFUL";

                }else{resultTAG = "EMAIL_LOGIN_STATUS_UPDATE_FAILED";}
            }else{resultTAG = "EMAIL_SIGN_FAILED";}
        }else{resultTAG = "TASK_FAILED";}

        return resultTAG;
    }

    @Override
    protected void onPostExecute(String resultTAG) {
        progressDialog.dismiss();
        String toastMESSAGE= null, logMESSAGE = null;
        switch (resultTAG){
            case "TASK_SUCCESSFUL":                     {toastMESSAGE = "Logged In Successfully."; logMESSAGE = "TASK_SUCCESSFUL: Logged In Successfully.";break;}
            case "EMAIL_LOGIN_STATUS_UPDATE_FAILED":    {toastMESSAGE = "Error Logging In. Please Retry."; logMESSAGE = "EMAIL_LOGIN_STATUS_UPDATE_FAILED: Error updating Email Logged In status to TRUE.";break;}
            case "EMAIL_SIGN_FAILED":                   {toastMESSAGE = "Error Logging In. Please Retry."; logMESSAGE = "EMAIL_SIGN_FAILED: Authentication Sign In Failed.";break;}
            case "TASK_FAILED":                         {toastMESSAGE = "Please check your Credentials."; logMESSAGE = "TASK_FAILED: login_isEmailUsable cannot find Email.";break;}
        }

        Toast.makeText(activity, toastMESSAGE, Toast.LENGTH_SHORT).show();
        Log.d("myCount", logMESSAGE);
        if (resultTAG.equals("TASK_SUCCESSFUL")){
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(activity, intent, null);
            activity.finish();
        }else{
            //TODO: revert changes.
        }
    }

}

