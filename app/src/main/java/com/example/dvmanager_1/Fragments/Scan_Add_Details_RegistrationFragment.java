package com.example.dvmanager_1.Fragments;


import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dvmanager_1.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scan_Add_Details_RegistrationFragment extends Fragment {

    TextInputEditText tiet_name, tiet_email, tiet_phone_no, tiet_admission_no, tiet_address, tiet_gender, tiet_age;

    public Scan_Add_Details_RegistrationFragment() {
        // Required empty public constructor
    }

    SendMessage SM;
    interface SendMessage {
        void sendRegistrationData(HashMap<String, Object> message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_add_details_registration, container, false);

        tiet_name = view.findViewById(R.id.tiet_name);
        tiet_email = view.findViewById(R.id.tiet_email);
        tiet_phone_no = view.findViewById(R.id.tiet_phone_no);
        tiet_admission_no = view.findViewById(R.id.tiet_admission_no);
        tiet_address = view.findViewById(R.id.tiet_address);
        tiet_gender = view.findViewById(R.id.tiet_gender);
        tiet_age = view.findViewById(R.id.tiet_age);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        HashMap<String,Object> registration_details_HashMap = new HashMap<>();
        registration_details_HashMap.put("NAME",tiet_name.getText());
        registration_details_HashMap.put("EMAIL",tiet_email.getText());
        registration_details_HashMap.put("PHONE_NO",tiet_phone_no.getText());
        registration_details_HashMap.put("ADMISSION_NO",tiet_admission_no.getText());
        registration_details_HashMap.put("ADDRESS",tiet_address.getText());
        registration_details_HashMap.put("GENDER",tiet_gender.getText());
        registration_details_HashMap.put("AGE",tiet_age.getText());
        registration_details_HashMap.put("BLOCKED", false);
        SM.sendRegistrationData(registration_details_HashMap);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

}
