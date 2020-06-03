package com.example.dvmanager_1.Tasks;

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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.dvmanager_1.Constants.firestore_email;
import static com.example.dvmanager_1.Constants.firestore_institution;

public class Login_Task_Helper {

    public static boolean signIn(FirebaseAuth firebaseAuth, String email, String password ){

        final boolean[] isTaskSuccessfull = {false};
        Task<AuthResult> task = firebaseAuth.signInWithEmailAndPassword(email, password);

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d("myCount", "Error in isEmailUseable(): " + e);
        }

        if (task.isSuccessful()) {
            isTaskSuccessfull[0] = true;
        } else {
            isTaskSuccessfull[0] = false;
            Log.d("myCount","LogIn Not successfull./nError: " + task.getException());
        }
        return isTaskSuccessfull[0];
    }

    public static boolean updateEmailLoggedInAfterSignIn(FirebaseFirestore db, Email_List email_list, int email_found_at_INDEX){

        email_list.getEmails().get(email_found_at_INDEX).set_isEmailCreated(true);
        email_list.getEmails().get(email_found_at_INDEX).setLogged_In(true);

        final boolean[] isTaskSuccessfull = {false};
        Task<Void> task = db.document(firestore_email)
                .set(email_list);

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d("myCount", "Error in updateEmailLoggedInAfterSignIn(): " + e);
        }

        if(task.isSuccessful()){
            isTaskSuccessfull[0] = true;
            Log.d("myCount", "updateEmailLoggedInAfterSignIn: Email updated successfully.");
        }else{
            isTaskSuccessfull[0] = false;
            Log.d("myCount", "updateEmailLoggedInAfterSignIn: Email update Not successfull./nError: " + task.getException());
        }

        return isTaskSuccessfull[0];
    }

    public static HashMap<String, Object> login_isEmailUseable(FirebaseFirestore db, String path, final String email, String institutionPath) {

        String emailResultTAG = "ERROR_isEmailUseable";
        HashMap<String,Object> resultData = new HashMap<>();
        Task<DocumentSnapshot> task = db.document(path)
                .get();

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d("myCount", "Error in checkEmail_RegisteredDevice(): " + e);
        }

        if(task.isSuccessful()){
            final Email_List email_list = task.getResult().toObject(Email_List.class);

            for (int i = 0; i< Objects.requireNonNull(email_list).getEmails().size(); i++)
            {
                final EmailModel emailModel = email_list.getEmails().get(i);
                if(emailModel.getEmail().equals(email))
                {
                    if(emailModel.getRole().equals("Manager")){
                        if(emailModel.getInstitution_Path().equals(institutionPath)){
                            if(emailModel.get_isEmailCreated()) {
                                if (emailModel.getBlocked() == false) {
                                    if (emailModel.getLogged_In() == false) {
                                        emailResultTAG = "TASK_SUCCESSFUL_isEmailUseable";
                                        resultData.put("emailList", email_list);
                                        resultData.put("email_found_at_INDEX", i);
                                        break;
                                    }
                                    else {emailResultTAG = "LOGGED_IN_isEmailUseable";break;}
                                } else {emailResultTAG = "BLOCKED_isEmailUseable";break;}
                            }else{emailResultTAG = "EMAIL_NOT_isEmailUseable"; break; }
                        }else{emailResultTAG = "INSTITUTION_NOT_isEmailUseable"; break; }
                    }else{emailResultTAG = "ROLE_NOT_MANAGER_isEmailUseable"; break;}
                }else if(i == (email_list.getEmails().size() -1))
                    //TODO: check this whole if-else scenario.
                    emailResultTAG = "NOT_FOUND_isEmailUseable";
            }
        }else{
            Log.d("myCount", "Error in checkEmail_RegisteredDevice(): " + task.getException());
            emailResultTAG = "ERROR_isEmailUseable";
        }
        resultData.put("emailResultTAG", emailResultTAG);
        return resultData;
    }

}
