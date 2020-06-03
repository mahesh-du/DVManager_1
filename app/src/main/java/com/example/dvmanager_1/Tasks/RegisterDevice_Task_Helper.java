package com.example.dvmanager_1.Tasks;

import android.app.Activity;
import android.util.Log;

import com.example.dvmanager_1.Model.EmailModels.EmailModel;
import com.example.dvmanager_1.Model.EmailModels.Email_List;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.dvmanager_1.Constants.firestore_institution;

public class RegisterDevice_Task_Helper {

    public static boolean createEmail(FirebaseAuth firebaseAuth, final String email, String password){

        final boolean[] isTaskSuccessfull = {false};
        Task<AuthResult> task = firebaseAuth.createUserWithEmailAndPassword(email, password);

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d("myCount", "Error in isEmailUseable(): " + e);
        }

        if (task.isSuccessful()) {
            isTaskSuccessfull[0] = true;
            Log.d("myCount", "Email Creation Successfull.");
        }else
            Log.d("myCount", "Email Creation not Successfull: " + task.getException());

        return isTaskSuccessfull[0];
    }

    public static boolean set_isEmailCreatedTrue(FirebaseFirestore db, Email_List emails, int email_index_found_at, String path){

        final boolean[] isTaskSuccessfull = {false};
        emails.getEmails().get(email_index_found_at).set_isEmailCreated(true);

        Task<Void> set_isEmailCreated_TrueTASK = db.document(path)
                .set(emails);
        try {
            Tasks.await(set_isEmailCreated_TrueTASK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d("myCount", "Error in set_isEmailCreatedTrue(): " + e);
        }

        if (set_isEmailCreated_TrueTASK.isSuccessful()) {
            isTaskSuccessfull[0] = true;
            Log.d("myCount", "isEmailCreatedTrue set to TRUE.");
        }else
            Log.d("myCount", "isEmailCreatedTrue still false. Error: " + set_isEmailCreated_TrueTASK.getException());

        return isTaskSuccessfull[0];
    }

    public static HashMap<String, Object> isEmailUseable(FirebaseFirestore db, String path, String Email, String institutionNAME){

        String resultTAG = null;
        HashMap<String, Object> returnData = new HashMap<>();
        Task<DocumentSnapshot> task =db.document(path)
                .get();

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            resultTAG = "EMAIL_TRY_CATCH_ERROR";
            Log.d("myCount", "Error in isEmailUseable(): " + e);
        }

        if (task.isSuccessful()){
            Email_List email_list = task.getResult().toObject(Email_List.class);

            if(email_list!= null){

                for (int i = 0; i< email_list.getEmails().size(); i++)
                {
                    EmailModel email = email_list.getEmails().get(i);
                    String role = email.getRole();
                    Boolean blocked = email.getBlocked();
                    Boolean logged_In = email.getLogged_In();
                    Boolean isEmailCreated = email.get_isEmailCreated();
                    String institutionPath = email.getInstitution_Path();

                    if(email.getEmail().equals(Email))
                    {
                        if(institutionPath.equals(firestore_institution + "/" + institutionNAME)){
                            if(role.equals("Manager")){
                                if(!isEmailCreated){
                                    if(!blocked){
                                        if(!logged_In){
                                            resultTAG = "EMAIL_USEABLE";
                                            returnData.put("emailLIST", email_list);
                                            returnData.put("email_found_at_INDEX", i);
                                        }else{resultTAG = "EMAIL_LOGGED_IN";}
                                    }else{resultTAG = "EMAIL_BLOCKED";}
                                }else{resultTAG = "EMAIL_ALREADY_CREATED";}
                            }else{resultTAG = "EMAIL_NOT_FOR_MANAGER";}
                        }else{resultTAG = "EMAIL_NOT_FOR_THIS_INSTITUTION";}
                    }else if(i == (email_list.getEmails().size() -1))
                        resultTAG = "EMAIL_NOT_FOUND";
                }

            }else{
                Log.d("myCount", "Error in isEmailUseable(): email list null");
                resultTAG = "EMAIL_LIST_EMPTY_ERROR";
            }
        } else {
            resultTAG = "EMAIL_TASK_NOT_SUCCESSFUL_ERROR";
            Log.d("myCount", "Error in isEmailUseable(): " + task.getException());
        }
        returnData.put("resultTAG", resultTAG);

        return returnData;
    }


}
