package com.example.dvmanager_1.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dvmanager_1.Constants;
import com.example.dvmanager_1.Model.Manage_devicesModel;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Manage_devices_RecyclerViewAdapter extends RecyclerView.Adapter<Manage_devices_RecyclerViewAdapter.MyViewHolder> implements Constants {

    private List<Manage_devicesModel> devicesList;
    private Context context;
    private String institutionPath;
    private static int currentPosition = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Manage_devices_RecyclerViewAdapter(List<Manage_devicesModel> devicesList, Context context, String institutionPath) {
        this.devicesList = devicesList;
        this.context = context;
        this.institutionPath = institutionPath;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_devices,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final Manage_devicesModel device = devicesList.get(position);

        myViewHolder.txt_device_list_Device_Name.setText(device.getDevice_Name());
        myViewHolder.txt_device_list_blocked.setText(device.getBlocked().toString());
        myViewHolder.txt_device_list_scans_today.setText(device.getScans_Today().toString());
        myViewHolder.txt_device_list_logged_in.setText(device.getLogged_In().toString());
        myViewHolder.txt_device_list_logged_in_email.setText(device.getLogged_In_Email());
        myViewHolder.txt_device_list_battery_remaining.setText(device.getBattery_Remaining().toString());
        myViewHolder.txt_device_list_battery_status.setText(device.getBattery_Status());
        myViewHolder.txt_device_list_network_status.setText(device.getNetwork_Status());
        myViewHolder.txt_device_list_actual_position.setText(device.getActual_Location());
        myViewHolder.txt_device_list_gps_location.setText(device.getGPS_Location().toString());

        if(device.getBlocked().toString().equals("false"))
            myViewHolder.imageView_manage_devices_block.setBackgroundResource(R.drawable.ic_block_green_24dp);
        else if(device.getBlocked().toString().equals("true"))
            myViewHolder.imageView_manage_devices_block.setBackgroundResource(R.drawable.ic_block_red_24dp);

        myViewHolder.linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {

            //toggling visibility
            myViewHolder.linearLayout.setVisibility(View.VISIBLE);
        }

        myViewHolder.device_list_outer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });

        myViewHolder.imageView_manage_devices_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: replace with custom dialog.
                final String device_name = myViewHolder.txt_device_list_Device_Name.getText().toString();
                final boolean isBlocked = Boolean.parseBoolean(myViewHolder.txt_device_list_blocked.getText().toString());

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.layout_confirm_dialog, null);

                TextView txt_message, txt_title, txt_device_Name;
                txt_message = alertLayout.findViewById(R.id.layout_confirm_dialog_txt_message);
                txt_title = alertLayout.findViewById(R.id.layout_confirm_dialog_txt_title);
                txt_device_Name = alertLayout.findViewById(R.id.layout_confirm_dialog_txt_device_Name);

                txt_title.setText(R.string.title_confirm);
                txt_device_Name.setText(device_name);
                txt_device_Name.setVisibility(View.VISIBLE);
                if (isBlocked)
                    txt_message.setText(R.string.message_Unblock_Device);
                else
                    txt_message.setText(R.string.message_Block_Device);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateBlocked(device_name, isBlocked);
                        if(isBlocked)
                            myViewHolder.imageView_manage_devices_block.setBackgroundResource(R.drawable.ic_block_green_24dp);
                        else
                            myViewHolder.imageView_manage_devices_block.setBackgroundResource(R.drawable.ic_block_red_24dp);

                        myViewHolder.txt_device_list_blocked.setText(String.valueOf(!isBlocked));
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                //notifyItemChanged(position);
            }
        });
    }

    private void show_Confirm_dialog(final String device_name, final boolean isBlocked, View myViewHolder) {

    }

    public void updateBlocked(String device_name, final boolean wasBlocked){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        if (wasBlocked)
            progressDialog.setMessage("Unblocking Device");
        else
            progressDialog.setMessage("Blocking Device");
        progressDialog.show();

        db.document(institutionPath + firestore_device + "/" + device_name)
                .update(firestore_DEVICE_FIELD_Blocked, !wasBlocked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(!wasBlocked)
                            Toast.makeText(context, "Device Blocked successfully.", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Device Unblocked successfully.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Unable to block this device. Error: " + e, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_device_list_Device_Name, txt_device_list_blocked, txt_device_list_scans_today, txt_device_list_logged_in, txt_device_list_logged_in_email
                , txt_device_list_battery_remaining, txt_device_list_battery_status, txt_device_list_network_status
                , txt_device_list_actual_position, txt_device_list_gps_location;
        ImageView imageView_manage_devices_block;

        LinearLayout linearLayout,device_list_outer_linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_device_list_Device_Name = itemView.findViewById(R.id.txt_device_list_Device_Name);
            txt_device_list_blocked = itemView.findViewById(R.id.txt_device_list_blocked);
            txt_device_list_scans_today = itemView.findViewById(R.id.txt_device_list_scans_today);
            txt_device_list_logged_in= itemView.findViewById(R.id.txt_device_list_logged_in);
            txt_device_list_logged_in_email= itemView.findViewById(R.id.txt_device_list_logged_in_email);
            txt_device_list_battery_remaining = itemView.findViewById(R.id.txt_device_list_battery_remaining);
            txt_device_list_battery_status = itemView.findViewById(R.id.txt_device_list_battery_status);
            txt_device_list_network_status = itemView.findViewById(R.id.txt_device_list_network_status);
            txt_device_list_actual_position = itemView.findViewById(R.id.txt_device_list_actual_position);
            txt_device_list_gps_location = itemView.findViewById(R.id.txt_device_list_gps_location);

            imageView_manage_devices_block = itemView.findViewById(R.id.imageView_manage_devices_block);

            linearLayout = itemView.findViewById(R.id.device_list_linearLayout);
            device_list_outer_linearLayout = itemView.findViewById(R.id.device_list_outer_linearLayout);
        }
    }

}
