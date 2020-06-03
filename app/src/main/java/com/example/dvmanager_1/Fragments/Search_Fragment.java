package com.example.dvmanager_1.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dvmanager_1.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.dvmanager_1.Constants.backstack_TAG_Search_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Search_Result_FRAGMENT;

public class Search_Fragment extends Fragment implements ZXingScannerView.ResultHandler {

    public static String scanned_Id = "";
    private ZXingScannerView mScannerView;

    public Search_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mScannerView = new ZXingScannerView(getActivity());/*{   */// Programmatically initialize the scanner view

        getActivity().setTitle("Search");

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

    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.d("myCount", "SearchFragment: " + rawResult.getText()); // Prints scan results

//        scanned_Id = rawResult.getText();

        Search_Result_Fragment search_result_fragment = new Search_Result_Fragment();
        Bundle args = new Bundle();
        args.putString("scanned_Id", rawResult.getText());
        search_result_fragment.setArguments(args);

        loadFragment(search_result_fragment);

        mScannerView.stopCamera();

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment,backstack_TAG_Search_Result_FRAGMENT).addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }



}
