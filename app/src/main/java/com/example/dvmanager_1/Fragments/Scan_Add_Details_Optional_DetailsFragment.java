package com.example.dvmanager_1.Fragments;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.dvmanager_1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scan_Add_Details_Optional_DetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout root;
    Button btn_add_view;
    ArrayAdapter<String> sp_fields_Adapter;
    List<String> sp_fields_List,sp_selected_fields_list;

    public Scan_Add_Details_Optional_DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan__add__details__optional__details, container, false);
        root = view.findViewById(R.id.fragment_scan_add_details_optional_details_linearLayout);
        btn_add_view = view.findViewById(R.id.fragment_scan_add_optional_details_btn_add_view);
        btn_add_view.setOnClickListener(this);
        sp_fields_List = new ArrayList<>(50);
        sp_fields_List.add("Person to contact in case of emergency");
        sp_fields_List.add("Emergency Contact Number");
        sp_fields_List.add("Degree Program");
        sp_fields_List.add("Courses Enrolled In");
        sp_fields_List.add("Interests");
        sp_fields_List.add("DOB");

        sp_selected_fields_list = new ArrayList<>(50);

        View child = getLayoutInflater().inflate(R.layout.layout_scan_add_optional_details, null);
        AppCompatSpinner sp_fields = child.findViewById(R.id.sp_fields);
        sp_fields.setOnItemSelectedListener(this);
        sp_fields_Adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, sp_fields_List);
        sp_fields.setAdapter(sp_fields_Adapter);
        root.addView(child);
        return view;
    }

    @Override
    public void onClick(final View v) {
        int count = root.getChildCount();

        if(count < sp_fields_List.size()) {
            final View child = getLayoutInflater().inflate(R.layout.layout_scan_add_optional_details, null);
            Button btn_delete_View = child.findViewById(R.id.btn_delete_view);
            btn_delete_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    root.removeView(child);
                }
            });

            AppCompatSpinner sp_fields = child.findViewById(R.id.sp_fields);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, sp_fields_List);
            List<AppCompatSpinner> spinnerList = getAllSpinners(root);

            for (int i = 0; i < spinnerList.size(); i++) {
                    adapter.remove(spinnerList.get(i).getSelectedItem().toString());
            }
            sp_fields.setOnItemSelectedListener(this);
            sp_fields.setAdapter(adapter);

//            sp_fields.setSelection(Adapter.NO_SELECTION,true);
            root.addView(child);
        }
    }

    private List<AppCompatSpinner> getAllSpinners(LinearLayout viewGroup) {
        List<AppCompatSpinner> spinnerList = new ArrayList<>();

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof LinearLayout) {
                List<AppCompatSpinner> returnedData = getAllSpinners((LinearLayout) view);
                if(returnedData.size()>0){
                    if(spinnerList.size()>0){
                        for (AppCompatSpinner returnedSpinner: returnedData){
                                for(AppCompatSpinner spinner: spinnerList){
                                    if(!returnedSpinner.getSelectedItem().equals(spinner.getSelectedItem()))
                                        spinnerList.add(returnedSpinner);
                                }
                        }
                    }else{
                        spinnerList.addAll(returnedData);
                    }
                }
            }else if (view instanceof androidx.appcompat.widget.AppCompatSpinner) {
                AppCompatSpinner spinner = (AppCompatSpinner) view;
                spinnerList.add(spinner);
            }
        }
        return spinnerList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String newSelectedItem = String.valueOf(parent.getAdapter().getItem(position));
        List<String> newSelectedItemsList = new ArrayList<>();

        List<AppCompatSpinner> spinnerList = getAllSpinners(root);
        int count = root.getChildCount();
        if(spinnerList.size()>1){
            for (int i = 0; i < spinnerList.size(); i++) {
//                View child = root.getChildAt(i).findViewById(R.id.sp_fields);
//                if (child instanceof AppCompatSpinner) {
//
//                }
                newSelectedItemsList.add(String.valueOf(spinnerList.get(i).getSelectedItem()));

            }

            String oldSelectedItem = null;
            for (String field : sp_selected_fields_list){
                for (String new_Selected_Item : newSelectedItemsList) {
                    if(!field.equals(new_Selected_Item))
                        oldSelectedItem = field;
                }
            }

            for (int i = 0; i < count; i++) {
                View child = root.getChildAt(i).findViewById(R.id.sp_fields);
                if (child instanceof AppCompatSpinner) {
                    if(child != view) {
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) (((AppCompatSpinner) child).getAdapter());
                        adapter.add(oldSelectedItem);
                        adapter.remove(newSelectedItem);
                        ((AppCompatSpinner) child).setAdapter(adapter);
                    }
                }
            }

        }
        else
            sp_selected_fields_list.add(newSelectedItem);


//        sp_fields_Adapter.remove(parent.getSelectedItem().toString());
//
//        sp_selected_fields_list.add(parent.getAdapter().getItem(position).toString());
//        List<String> temp_list = new ArrayList<>();
//        temp_list.addAll(sp_fields_List);
//
//        for (int i = 0; i < count; i++) {
//            View child = root.getChildAt(i);
//            if (child instanceof AppCompatSpinner){
//
//                String newSelectedItem =((AppCompatSpinner) child).getSelectedItem().toString();
//                for (String field : sp_selected_fields_list)
//                {
//                    if(temp_list.contains(field))
//                        temp_list.remove(field);
//                }
//            }
//
//
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
