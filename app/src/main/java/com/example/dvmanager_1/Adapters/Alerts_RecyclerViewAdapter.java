package com.example.dvmanager_1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dvmanager_1.Model.AlertModels.Alert_BatteryModel;
import com.example.dvmanager_1.R;

import java.util.List;

public class Alerts_RecyclerViewAdapter extends RecyclerView.Adapter<Alerts_RecyclerViewAdapter.MyViewHolder>{

    private List<Alert_BatteryModel> alertsList;
    private static int currentPosition = 0;
    private Context context;

    public Alerts_RecyclerViewAdapter(Context context, List<Alert_BatteryModel> alertsList) {
        this.alertsList = alertsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_alerts,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Alerts_RecyclerViewAdapter.MyViewHolder myViewHolder, final int position) {
        Alert_BatteryModel alert = alertsList.get(position);

        myViewHolder.txt_alert_type                 .setText(String.valueOf(alert.getAlertType()));
        myViewHolder.txt_device_Name                .setText(String.valueOf(alert.getDevice_Name()));
        myViewHolder.txt_battery_remaining          .setText(String.valueOf(alert.getBattery_Remaining()));
        myViewHolder.txt_battery_status             .setText(String.valueOf(alert.getBattery_Status()));
        myViewHolder.txt_approx_battery_life        .setText(String.valueOf("00:00:00"));
        myViewHolder.txt_device_actual_location     .setText(String.valueOf(alert.getDevice_Actual_Position()));
        myViewHolder.txt_message                    .setText(String.valueOf(alert.getMessage()));

        myViewHolder.inner_linearLayout.setVisibility(View.GONE);

        if (currentPosition == position) {
            //toggling visibility
            myViewHolder.inner_linearLayout.setVisibility(View.VISIBLE);
        }

        myViewHolder.outer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_alert_type, txt_device_Name, txt_device_actual_location, txt_battery_remaining,
                txt_battery_status, txt_approx_battery_life, txt_message;
        LinearLayout outer_linearLayout, inner_linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_alert_type              = itemView.findViewById(R.id.list_layout_alerts_alert_type);
            txt_device_Name             = itemView.findViewById(R.id.list_layout_alerts_device_name);
            txt_device_actual_location  = itemView.findViewById(R.id.list_layout_alerts_device_actual_location);
            txt_battery_remaining       = itemView.findViewById(R.id.list_layout_alerts_battery_remaining);
            txt_battery_status          = itemView.findViewById(R.id.list_layout_alerts_battery_status);
            txt_approx_battery_life     = itemView.findViewById(R.id.list_layout_alerts_approx_battery_life);
            txt_message                 = itemView.findViewById(R.id.list_layout_alerts_message);

            outer_linearLayout  = itemView.findViewById(R.id.list_layout_alerts_outer_LinearLayout);
            inner_linearLayout  = itemView.findViewById(R.id.list_layout_alerts_inner_LinearLayout);
        }
    }

    @Override
    public int getItemCount() {
        return alertsList.size();
    }
}
