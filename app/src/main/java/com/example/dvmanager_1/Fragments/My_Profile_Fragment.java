package com.example.dvmanager_1.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.dvmanager_1.Constants.KEY_institution_path;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTIONS_FIELD_INSTITUTION_Names;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_Address;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_Blank_Ids;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_Devices_Registered;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_Email;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_Ids_Registered;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_Name;
import static com.example.dvmanager_1.Constants.firestore_INSTITUTION_FIELD_Institution_PhoneNo;
import static com.example.dvmanager_1.Constants.firestore_institution_Details;
import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class My_Profile_Fragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Institution_Path;
    TextView Institution_Name, Institution_Email, Institution_PhoneNo, Institution_Address,
            Institution_Ids_Registered , Institution_Blank_Ids, Institution_Devices_Registered;

    public My_Profile_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        getActivity().setTitle("My Profile");
        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();

        Institution_Name                = view.findViewById(R.id.alert_fragment_Institution_Name);
        Institution_Email               = view.findViewById(R.id.alert_fragment_Institution_Email);
        Institution_PhoneNo             = view.findViewById(R.id.alert_fragment_Institution_Phone_No);
        Institution_Address             = view.findViewById(R.id.alert_fragment_Institution_Address);
        Institution_Ids_Registered      = view.findViewById(R.id.alert_fragment_Ids_Registered);
        Institution_Blank_Ids           = view.findViewById(R.id.alert_fragment_Blank_Ids);
        Institution_Devices_Registered  = view.findViewById(R.id.alert_fragment_Devices_Registered);

        loadUserProfile();

        return view;
    }

    public void loadUserProfile(){
        String path = Institution_Path + firestore_institution_Details;
        db.document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Institution_Name                .setText(documentSnapshot.getString(firestore_INSTITUTION_FIELD_Institution_Name));
                        Institution_Email               .setText(documentSnapshot.getString(firestore_INSTITUTION_FIELD_Institution_Email));
                        Institution_PhoneNo             .setText(String.valueOf(documentSnapshot.getLong(firestore_INSTITUTION_FIELD_Institution_PhoneNo)));
                        Institution_Address             .setText(documentSnapshot.getString(firestore_INSTITUTION_FIELD_Institution_Address));
                        Institution_Ids_Registered      .setText(String.valueOf(documentSnapshot.getLong(firestore_INSTITUTION_FIELD_Institution_Ids_Registered)));
                        Institution_Blank_Ids           .setText(String.valueOf(documentSnapshot.getLong(firestore_INSTITUTION_FIELD_Institution_Blank_Ids)));
                        Institution_Devices_Registered  .setText(String.valueOf(documentSnapshot.getLong(firestore_INSTITUTION_FIELD_Institution_Devices_Registered)));

                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("myCount", "Error: " + e);
            }
        });
    }

}
