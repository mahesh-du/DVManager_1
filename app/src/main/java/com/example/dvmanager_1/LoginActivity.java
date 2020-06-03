package com.example.dvmanager_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dvmanager_1.Tasks.Login_background_Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.dvmanager_1.Constants.firestore_institution;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginActivity extends AppCompatActivity {

    TextInputEditText et_email, et_password, et_institutionName;
    TextView txt_forgot_password, txt_register_email;
    Button btn_login;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email            = findViewById(R.id.tiet_login_email);
        et_password         = findViewById(R.id.tiet_login_password);
        et_institutionName  = findViewById(R.id.tiet_login_institutionName);
        txt_forgot_password = findViewById(R.id.txt_login_forgot_password);
        txt_register_email  = findViewById(R.id.txt_login_register_email);
        btn_login           = findViewById(R.id.btn_login);

        //not required though. Already checked conditions in SplashScreenActivity.
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!= null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String email = et_email.getText().toString();
                final String password= et_password.getText().toString();
                final String institutionName= et_institutionName.getText().toString();

                if (email.isEmpty()) {
                    et_email.setError("Provide your Email first!");
                    et_email.requestFocus();
                } else if (password.isEmpty()) {
                    et_password.setError("Enter Password!");
                    et_password.requestFocus();
                }else if (institutionName.isEmpty()) {
                    et_institutionName.setError("Provide your Institution Name!");
                    et_institutionName.requestFocus();
                }else if (!(email.isEmpty() && password.isEmpty() && institutionName.isEmpty())) {

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("firestoreINSTANCE", db);
                    data.put("firebaseAUTH", firebaseAuth);
                    data.put("email", email);
                    data.put("password", password);
                    data.put("institutionPATH", firestore_institution + "/" +institutionName);

                    Login_background_Task login_background_task = new Login_background_Task(LoginActivity.this, data);
                    login_background_task.execute();

                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_register_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterDeviceActivity.class);
                startActivity(intent);
                //TODO: check if after registering device onBackPressed calls LoginActivity.(It shouldn't).
            }
        });
        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!et_email.getText().toString().isEmpty()){
                    firebaseAuth.sendPasswordResetEmail(et_email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("myCount", "Email sent.");
                                        Toast.makeText(LoginActivity.this, "Email Sent. Please Check your inbox.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d("myCount", "Email sent failed.");
                                        Toast.makeText(LoginActivity.this, "Email Sent failed. Please Retry.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(LoginActivity.this, "Please enter email.", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
