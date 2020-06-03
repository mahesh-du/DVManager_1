package com.example.dvmanager_1.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dvmanager_1.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scan_Add_Details_Profile_PictureFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imgView_profile_picture;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public Scan_Add_Details_Profile_PictureFragment() {
        // Required empty public constructor
    }

    SendMessage SM;
    interface SendMessage {
        void sendData(HashMap<String, Bitmap> message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_add_details_profile_picture, container, false);
        imgView_profile_picture = view.findViewById(R.id.img_scan_add_details_camera_profile_picture);

        imgView_profile_picture.setOnClickListener(new View.OnClickListener()
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgView_profile_picture.setImageBitmap(photo);
            HashMap<String,Bitmap> bitmap_HashMap = new HashMap<>();
            bitmap_HashMap.put("PROFILE_PICTURE", photo);
            SM.sendData(bitmap_HashMap);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
