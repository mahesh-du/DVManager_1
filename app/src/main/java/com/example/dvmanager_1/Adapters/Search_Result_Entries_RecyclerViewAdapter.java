package com.example.dvmanager_1.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dvmanager_1.Model.Search_Result_EntriesModel;
import com.example.dvmanager_1.R;

import java.util.List;

public class Search_Result_Entries_RecyclerViewAdapter extends RecyclerView.Adapter<Search_Result_Entries_RecyclerViewAdapter.MyViewHolder>  {


    private List<Search_Result_EntriesModel> entriesList;
    private Context context;
    private static int currentPosition = 0;

    public Search_Result_Entries_RecyclerViewAdapter(List<Search_Result_EntriesModel> entriesList, Context context) {
        this.entriesList = entriesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_search_result_entries,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Search_Result_EntriesModel entry = entriesList.get(position);

        myViewHolder.txt_search_result_entry_time_range.setText(entry.getEntry_Time() + " - " + entry.getExit_Time());
        myViewHolder.txt_search_result_entry_gate.setText(entry.getEntry_Gate());
        myViewHolder.txt_search_result_exit_gate.setText(entry.getExit_Gate());
        String temp = entry.getEntry_Status();
        if( temp.equals("In"))
            myViewHolder.btn_entry_status.setBackgroundResource(R.drawable.ic_circle_green) ;
        else if(temp.equals("Out"))
            myViewHolder.btn_entry_status.setBackgroundResource(R.drawable.ic_circle_red) ;

        myViewHolder.search_result_linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {

            //toggling visibility
            myViewHolder.search_result_linearLayout.setVisibility(View.VISIBLE);
        }

        myViewHolder.search_result_outer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return entriesList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_search_result_entry_time_range, txt_search_result_entry_gate, txt_search_result_exit_gate;
        LinearLayout search_result_linearLayout,search_result_outer_linearLayout;
        Button btn_entry_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_search_result_entry_time_range = itemView.findViewById(R.id.txt_search_result_entry_time_range);
            txt_search_result_entry_gate = itemView.findViewById(R.id.txt_search_result_entry_gate);
            txt_search_result_exit_gate = itemView.findViewById(R.id.txt_search_result_exit_gate);

            search_result_linearLayout = itemView.findViewById(R.id.search_result_linearLayout);
            search_result_outer_linearLayout = itemView.findViewById(R.id.search_result_outer_linearLayout);

            btn_entry_status = itemView.findViewById(R.id.btn_entry_status);
        }
    }

}
