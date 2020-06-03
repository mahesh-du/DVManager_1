package com.example.dvmanager_1.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dvmanager_1.Constants;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

public class Scan_Fragment extends Fragment implements ZXingScannerView.ResultHandler, Constants {

    public static String scanned_Id = "";
    private ZXingScannerView mScannerView;
    public EditText et_firstname, et_lastname, et_phoneNo, et_email, et_address, et_gender, et_age, et_admission_no;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ZXingScannerView.ResultHandler resultHandler;

    String Institution_Path;

    public Scan_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());/*{   */// Programmatically initialize the scanner view
        resultHandler = this;
        getActivity().setTitle("Scan");
        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        mScannerView.setAutoFocus(true);
        mScannerView.setSquareViewFinder(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        Log.d("myCount", "ScanActivity: OnPause.");
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.d("myCount", "ScanActivity: " + rawResult.getText()); // Prints scan results

        scanned_Id = rawResult.getText();
        mScannerView.stopCamera();
        mScannerView.stopCameraPreview();
        db.document(Institution_Path + firestore_ids+"/" + scanned_Id)
                .get()
                /*.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String Name = task.getResult().getString(firestore_IDS_FIELD_Name);
                                String Email = task.getResult().getString(firestore_IDS_FIELD_Email);

                                if(Name!= null && Email!=null)
                                    show_Clear_data_dialog(Name,Email);
                                else
                                    loadScan_Add_Details_Fragment();
                            }else{
                                Toast.makeText(getContext(), "ID does not exists.", Toast.LENGTH_LONG).show();
                                mScannerView.startCamera();
                                mScannerView.resumeCameraPreview(resultHandler);
                            }
                        }
                        else
                            Toast.makeText(getContext(), "Task not successful.", Toast.LENGTH_LONG).show();
                    }
                })*/.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getId().equals(scanned_Id)) {
                        String Name = documentSnapshot.getString(firestore_IDS_FIELD_Name);
                        String Email = documentSnapshot.getString(firestore_IDS_FIELD_Email);

                        if(Name!= null && Email!=null)
                            show_Clear_data_dialog(Name,Email);
                        else
                            loadScan_Add_Details_Fragment();
                    }else{
                        Toast.makeText(getContext(), "ID does not exists.", Toast.LENGTH_LONG).show();
                        mScannerView.startCamera();
                        mScannerView.resumeCameraPreview(resultHandler);
                    }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Task not successful.ERROR: " + e , Toast.LENGTH_LONG).show();
            }
        });
    }


    private void show_Clear_data_dialog(final String Name, String Email) {

        View alertLayout = getLayoutInflater().inflate(R.layout.layout_scan_clear_data, null);

        TextView txt_message, txt_Id, txt_Name, txt_Email;
        txt_message = alertLayout.findViewById(R.id.txt_scan_message);
        txt_Id = alertLayout.findViewById(R.id.txt_scan_Id);
        txt_Name = alertLayout.findViewById(R.id.txt_scan_Name);
        txt_Email = alertLayout.findViewById(R.id.txt_scan_Email);

        txt_message.setText("Following Data exists in the scanned Id. \nDo you want to Clean it?");
        txt_Id.setText(scanned_Id);
        txt_Name.setText(Name);
        txt_Email.setText(Email);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        //alert.setTitle("Warning!");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                mScannerView.startCamera();
                mScannerView.resumeCameraPreview(resultHandler);
            }
        });

        alert.setNegativeButton("Confirm Deletion", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                loadScan_Add_Details_Fragment();
                Toast.makeText(getContext(), "Name: " + Name, Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void loadScan_Add_Details_Fragment(){

        Fragment add_Details_Fragment = new Scan_Add_Details_Fragment();
        Bundle args = new Bundle();
        args.putString("scanned_Id", scanned_Id);
        add_Details_Fragment.setArguments(args);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, add_Details_Fragment).addToBackStack(backstack_TAG_Scan_Add_Details_FRAGMENT)
                .commit();
    }

    //TODO: not in use.
    private void scan_add_data_dialog() {

        mScannerView.stopCameraPreview();

        View alertLayout = getLayoutInflater().inflate(R.layout.layout_scan_add_details,null);

        et_firstname = alertLayout.findViewById(R.id.et_firstname);
        et_lastname = alertLayout.findViewById(R.id.et_lastname);
        et_phoneNo = alertLayout.findViewById(R.id.et_phoneNo);
        et_email = alertLayout.findViewById(R.id.et_email);
        et_admission_no = alertLayout.findViewById(R.id.et_admission_no);
        et_address = alertLayout.findViewById(R.id.et_address);
        et_gender = alertLayout.findViewById(R.id.et_gender);
        et_age = alertLayout.findViewById(R.id.et_age);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        //alert.setTitle("Warning!");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                mScannerView.startCamera();
                mScannerView.resumeCameraPreview(resultHandler);
            }
        });

        alert.setNegativeButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                update_ID_Details(Institution_Path + firestore_ids+"/" + scanned_Id);
                mScannerView.startCamera();
                mScannerView.resumeCameraPreview(resultHandler);

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

        //Below: changes size of dialog.
        //Window window = dialog.getWindow();
        //window.setLayout(850, 1000);
    }

    public void update_ID_Details(String path)
    {
        db.document(path)
                .update(getUpdatedDetails())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Details updated Successfully.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Unable to update details.", Toast.LENGTH_LONG).show();
                    }
                });
        //TODO: mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);
    }

    public Map<String,Object> getUpdatedDetails()
    {
        Map<String, Object> params = new HashMap<>();
        if((!et_firstname.getText().toString().isEmpty() || !et_lastname.getText().toString().isEmpty()))
            params.put("Name", et_firstname.getText() + " " + et_lastname.getText());
        if(!et_phoneNo.getText().toString().isEmpty())
            params.put("Phone Number",Long.parseLong(et_phoneNo.getText().toString()));
        if(!et_email.getText().toString().isEmpty())
            params.put("Email", et_email.getText().toString());
        if(!et_address.getText().toString().isEmpty())
            params.put("Address", et_address.getText().toString());
        if(!et_gender.getText().toString().isEmpty())
            params.put("Gender", et_gender.getText().toString());
        if(!et_age.getText().toString().isEmpty())
            params.put("Age", Long.parseLong(et_age.getText().toString()));
        if(!et_admission_no.getText().toString().isEmpty())
            params.put("Admission No", Long.parseLong(et_admission_no.getText().toString()));
        //TODO: add else part
        return params;
    }

}
