package com.example.dvmanager_1.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dvmanager_1.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scan_Add_Details_Other_ID_Proof_Fragment extends Fragment {


    private static final int CAMERA_REQUEST = 1888;
    private ImageView imgView_other_id_proof;

    public Scan_Add_Details_Other_ID_Proof_Fragment() {
        // Required empty public constructor
    }

    SendMessage SM;
    interface SendMessage {
        void sendIDProofData(HashMap<String, Bitmap> message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_add_details_other_id_proof, container, false);

        imgView_other_id_proof = view.findViewById(R.id.img_scan_add_details_other_details_id_proof);

        imgView_other_id_proof.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgView_other_id_proof.setImageBitmap(photo);
            HashMap<String,Bitmap> bitmap_HashMap = new HashMap<>();
            bitmap_HashMap.put("OTHER_ID_PROOF", photo);
            SM.sendIDProofData(bitmap_HashMap);
        }
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
