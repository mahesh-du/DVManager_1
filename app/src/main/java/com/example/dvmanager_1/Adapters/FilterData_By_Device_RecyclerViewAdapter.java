package com.example.dvmanager_1.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dvmanager_1.Model.FilterData_By_Device_Model;
import com.example.dvmanager_1.R;

import java.util.List;

public class FilterData_By_Device_RecyclerViewAdapter extends RecyclerView.Adapter<FilterData_By_Device_RecyclerViewAdapter.MyViewHolder> {

    public static List<FilterData_By_Device_Model> devicesList;
    private Context context;
    private static int currentPosition = 0;

    public FilterData_By_Device_RecyclerViewAdapter(List<FilterData_By_Device_Model> devicesList, Context context) {
        this.devicesList = devicesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_filter_data_by_device_recyclerview,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterData_By_Device_RecyclerViewAdapter.MyViewHolder myViewHolder, final int position) {

        final FilterData_By_Device_Model device = devicesList.get(position);

        myViewHolder.txt_layout_filter_data_By_Device_Device_Name.setText(device.getDevice_Name());
        myViewHolder.txt_layout_filter_data_By_Device_blocked.setText(device.getBlocked().toString());
        myViewHolder.txt_layout_filter_data_By_Device_scans_today.setText(device.getScans_Today().toString());
        myViewHolder.txt_layout_filter_data_By_Device_logged_in.setText(device.getLogged_In().toString());
        myViewHolder.txt_layout_filter_data_By_Device_logged_in_email.setText(device.getLogged_In_Email());
        myViewHolder.txt_layout_filter_data_By_Device_battery_remaining.setText(device.getBattery_Remaining().toString());
        myViewHolder.txt_layout_filter_data_By_Device_battery_status.setText(device.getBattery_Status());
        myViewHolder.txt_layout_filter_data_By_Device_network_status.setText(device.getNetwork_Status());
        myViewHolder.txt_layout_filter_data_By_Device_actual_position.setText(device.getActual_Location());
        myViewHolder.txt_layout_filter_data_By_Device_gps_location.setText(device.getGPS_Location().toString());

        myViewHolder.layout_filter_data_By_Device_linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //toggling visibility
            myViewHolder.layout_filter_data_By_Device_linearLayout.setVisibility(View.VISIBLE);
        }

        myViewHolder.layout_filter_data_By_Device_outer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });

        myViewHolder.layout_filter_data_By_Device_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    device.set_isChecked(true);
                else
                    device.set_isChecked(false);
            }
        });
    }


    @Override
    public int getItemCount() {
        return devicesList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_layout_filter_data_By_Device_Device_Name,
                txt_layout_filter_data_By_Device_blocked,
                txt_layout_filter_data_By_Device_scans_today,
                txt_layout_filter_data_By_Device_logged_in,
                txt_layout_filter_data_By_Device_logged_in_email,
                txt_layout_filter_data_By_Device_battery_remaining,
                txt_layout_filter_data_By_Device_battery_status,
                txt_layout_filter_data_By_Device_network_status,
                txt_layout_filter_data_By_Device_actual_position,
                txt_layout_filter_data_By_Device_gps_location;

        LinearLayout layout_filter_data_By_Device_linearLayout,layout_filter_data_By_Device_outer_linearLayout;
        CheckBox layout_filter_data_By_Device_checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_layout_filter_data_By_Device_Device_Name = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_Device_Name);
            txt_layout_filter_data_By_Device_blocked = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_blocked);
            txt_layout_filter_data_By_Device_scans_today = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_scans_today);
            txt_layout_filter_data_By_Device_logged_in= itemView.findViewById(R.id.txt_layout_filter_data_By_Device_logged_in);
            txt_layout_filter_data_By_Device_logged_in_email= itemView.findViewById(R.id.txt_layout_filter_data_By_Device_logged_in_email);
            txt_layout_filter_data_By_Device_battery_remaining = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_battery_remaining);
            txt_layout_filter_data_By_Device_battery_status = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_battery_status);
            txt_layout_filter_data_By_Device_network_status = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_network_status);
            txt_layout_filter_data_By_Device_actual_position = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_actual_position);
            txt_layout_filter_data_By_Device_gps_location = itemView.findViewById(R.id.txt_layout_filter_data_By_Device_gps_location);

            layout_filter_data_By_Device_checkBox = itemView.findViewById(R.id.layout_filter_data_By_Device_checkBox);

            layout_filter_data_By_Device_linearLayout = itemView.findViewById(R.id.layout_filter_data_By_Device_linearLayout);
            layout_filter_data_By_Device_outer_linearLayout = itemView.findViewById(R.id.layout_filter_data_By_Device_outer_linearLayout);
        }
    }


}
