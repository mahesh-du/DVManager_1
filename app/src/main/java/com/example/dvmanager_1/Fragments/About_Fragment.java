package com.example.dvmanager_1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dvmanager_1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class About_Fragment extends Fragment {


    public About_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        //TODO: make bnb visible in other fragments.
//        BottomNavigationView navBar = getActivity().findViewById(R.id.bn_navigation);
//        navBar.setVisibility(View.GONE);
        getActivity().setTitle("About");
        return view;
    }

}
