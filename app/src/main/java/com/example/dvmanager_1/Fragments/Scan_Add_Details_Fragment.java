package com.example.dvmanager_1.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.dvmanager_1.Constants;
import com.example.dvmanager_1.MainActivity;
import com.example.dvmanager_1.Model.Scan_Add_Details_Model;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scan_Add_Details_Fragment extends Fragment implements Scan_Add_Details_Profile_PictureFragment.SendMessage,
        Scan_Add_Details_RegistrationFragment.SendMessage,
        Scan_Add_Details_Other_ID_Proof_Fragment.SendMessage, Constants {

    Button btn_back, btn_next;
    public int fragment_index = 0;
    public Scan_Add_Details_Model scan_add_details_model;
    String scanned_Id= "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Institution_Path;

    public Scan_Add_Details_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan__add__details, container, false);

        //sets window adjustment to false when keyboard appears.
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();
        scan_add_details_model = new Scan_Add_Details_Model();

//        BottomNavigationView navBar = getActivity().findViewById(R.id.bn_navigation);
//        navBar.setVisibility(View.GONE);

        btn_back = view.findViewById(R.id.btn_back);
        btn_next = view.findViewById(R.id.btn_next);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_scan_add_details_container, new Scan_Add_Details_Profile_PictureFragment(),backstack_TAG_Scan_Add_Details_Profile_Picture_FRAGMENT)
                .addToBackStack(backstack_TAG_Scan_Add_Details_Profile_Picture_FRAGMENT)
                .commit();

        scanned_Id = getArguments().getString("scanned_Id");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment_index>0)
                    fragment_index--;


                switch (fragment_index){
//                    case 2:{loadFragment(new Scan_Add_Details_Optional_DetailsFragment()); break;}
                    case 1:{loadFragment(new Scan_Add_Details_RegistrationFragment()); break;}
                    case 0:{loadFragment(new Scan_Add_Details_Profile_PictureFragment()); break;}
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment_index<2) {
                    fragment_index++;
                    switch (fragment_index){
                        case 1:{loadFragment(new Scan_Add_Details_RegistrationFragment()); break;}
                        case 2:{loadFragment(new Scan_Add_Details_Other_ID_Proof_Fragment()); break;}
//                    case 3:{loadFragment(new Scan_Add_Details_Optional_DetailsFragment()); break;}
                    }
                }else if(fragment_index == 2){
                    //TODO: validate object fields.
                    // and post them.

                    if(isDataValid(scan_add_details_model)){
                        sendData(scan_add_details_model, scanned_Id);
                    }
                }


            }
        });

        return view;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_scan_add_details_container, fragment).addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void sendData(HashMap<String, Bitmap> message) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        message.get("PROFILE_PICTURE").compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        scan_add_details_model.setprofile_Picture(temp);
        Toast.makeText(getContext(), "" + message.get("PROFILE_PICTURE").getWidth(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendRegistrationData(HashMap<String, Object> message) {
        scan_add_details_model.setname( message.get("NAME").toString());
        scan_add_details_model.setemail( message.get("EMAIL").toString());
        scan_add_details_model.setphone_No( Long.parseLong(
                (String.valueOf(message.get("PHONE_NO")).equals("") ? "0" : String.valueOf(message.get("PHONE_NO")))));
        scan_add_details_model.setadmission_No( Long.parseLong(
                (String.valueOf(message.get("ADMISSION_NO")).equals("") ? "0" : String.valueOf(message.get("ADMISSION_NO")))));
        scan_add_details_model.setaddress( message.get("ADDRESS").toString());
        scan_add_details_model.setgender( message.get("GENDER").toString());
        scan_add_details_model.setage( Long.parseLong(
                (String.valueOf(message.get("AGE")).equals("") ? "0": String.valueOf(message.get("AGE")))));
        scan_add_details_model.setBlocked(Boolean.parseBoolean(message.get("BLOCKED").toString()));
    }


    @Override
    public void sendIDProofData(HashMap<String, Bitmap> message) {

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        message.get("OTHER_ID_PROOF").compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        scan_add_details_model.setother_Id_Proof( temp);
        Toast.makeText(getContext(), "" + message.get("OTHER_ID_PROOF").getWidth(), Toast.LENGTH_SHORT).show();
    }

    public boolean isDataValid(Scan_Add_Details_Model scan_add_details_model){

        if(scan_add_details_model.getname()==null){
            Toast.makeText(getContext(), "Name field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getemail()==null){
            Toast.makeText(getContext(), "Email field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getphone_No()==null){
            Toast.makeText(getContext(), "Phone_No field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getadmission_No()==null){
            Toast.makeText(getContext(), "Admission_No field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getaddress()==null){
            Toast.makeText(getContext(), "Address field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getgender()==null){
            Toast.makeText(getContext(), "Gender field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getage()==null){
            Toast.makeText(getContext(), "Age field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getprofile_Picture()==null){
            Toast.makeText(getContext(), "Profile_picture field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(scan_add_details_model.getother_Id_Proof()==null){
            Toast.makeText(getContext(), "Other_Id_Proof field is empty", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public void sendData(Scan_Add_Details_Model scan_add_details_model, String scanned_Id){

        db.document(Institution_Path + firestore_ids + "/" + scanned_Id)
                .set(scan_add_details_model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Id updated Successfully.", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error updating Id: " + e, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
    }
}
