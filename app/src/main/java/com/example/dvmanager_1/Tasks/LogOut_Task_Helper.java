package com.example.dvmanager_1.Tasks;

import android.util.Log;

import com.example.dvmanager_1.Model.EmailModels.EmailModel;
import com.example.dvmanager_1.Model.EmailModels.Email_List;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class LogOut_Task_Helper {

    public static HashMap<String, Object> searchEmail(FirebaseFirestore db, String path, final String email){

        String resultTAG = null;
        HashMap<String, Object> resultData = new HashMap<>();

        Task<DocumentSnapshot> task = db.document(path)
                .get();

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            resultTAG = "error_try_catch_searchEmail";
            Log.d("myCount", "Error in searchEmail(): " + e);
        }

        if(task.isSuccessful()){
            final Email_List email_list = task.getResult().toObject(Email_List.class);

            if(email_list!= null){
                for (int i = 0; i< email_list.getEmails().size(); i++)
                {
                    EmailModel emailModel = email_list.getEmails().get(i);
                    if(emailModel.getEmail().equals(email))
                    {
                        if(emailModel.getRole().equals("Manager")){
                            if(emailModel.getLogged_In()){
                                emailModel.setLogged_In(false);
                                resultTAG = "TASK_SUCCESSFUL";
                                resultData.put("updatedEmailLIST", email_list);
                                break;
                            }else{resultTAG = "ERROR_LOGGED_IN_ALREADY_FALSE"; break;}
                        }//TODO: check this loop for role.
                    }else if(i == (email_list.getEmails().size() -1))
                        resultTAG = "EMAIL_NOT_FOUND";
                }
            }
            else{ resultTAG = "ERROR_RETRIEVING_LIST";}
        }else{
            resultTAG = "ERROR_TASK_NOT_SUCCESSFUL";
            Log.d("myCount", "ERROR_TASK_NOT_SUCCESSFUL: " + task.getException());
        }

        resultData.put("resultTAG", resultTAG);

        return resultData;
    }

    public static boolean updateEmailLoggedInAfterSignOut(FirebaseFirestore db, String path, Email_List email_list){

        final boolean[] isTaskSuccessfull = {false};
        Task<Void> task = db.document(path)
                .set(email_list);
        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d("myCount", "Error in searchEmail(): " + e);
        }

        if(task.isSuccessful())
            isTaskSuccessfull[0] = true;
        else
            isTaskSuccessfull[0] = false;


        return isTaskSuccessfull[0];
    }

    public static boolean signOut(){
        FirebaseAuth.getInstance().signOut();
        return true;
    }
}
