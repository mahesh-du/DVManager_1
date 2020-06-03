package com.example.dvmanager_1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dvmanager_1.Model.FilterData_By_ID_Model;
import com.example.dvmanager_1.R;

import java.util.ArrayList;
import java.util.List;

public class FilterData_By_Id_Adapter extends RecyclerView.Adapter<FilterData_By_Id_Adapter.MyViewHolder> implements Filterable{

    public static List<FilterData_By_ID_Model> dataList;
    private List<FilterData_By_ID_Model> filtered_IdsList;
    private static int currentPosition = 0;
    private Context mContext;

    public FilterData_By_Id_Adapter(List<FilterData_By_ID_Model> dataList, Context mContext) {
        this.dataList = dataList;
        this.filtered_IdsList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_filter_data_by_id,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        final FilterData_By_ID_Model id = filtered_IdsList.get(position);

        myViewHolder.txt_id.setText(String.valueOf(id.getId()));
        myViewHolder.txt_name.setText(id.getName());

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.circleCrop();
        options.error(R.drawable.ic_circle);

        Glide.with(mContext)
                .asBitmap()
                .apply(options)
                .load(getDecodedImage(id.getProfile_Picture()))
                .into(myViewHolder.profile_image);

        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    dataList.get(position).setChecked(true);
                else
                    dataList.get(position).setChecked(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filtered_IdsList.size();
    }

    public Bitmap getDecodedImage(String byteString)
    {
        if(byteString==null)
            return null;
        byte[] imageBytes = Base64.decode(byteString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return decodedImage;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id, txt_name;
        ImageView profile_image;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View view) {
            super(view);

            profile_image = view.findViewById(R.id.list_layout_filter_data_by_id_imageView_profile_picture);
            txt_id = view.findViewById(R.id.list_layout_filter_data_by_id_txt_ID);
            txt_name = view.findViewById(R.id.list_layout_filter_data_by_id_txt_Name);
            checkBox = view.findViewById(R.id.list_layout_filter_data_by_id_checkbox);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filtered_IdsList = dataList;
                } else {

                    ArrayList<FilterData_By_ID_Model> filteredList = new ArrayList<>();

                    for (FilterData_By_ID_Model id : dataList) {

                        if (String.valueOf(id.getId()).toLowerCase().contains(charString.toLowerCase())
                                || id.getName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(id);
                        }
                    }

                    filtered_IdsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered_IdsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtered_IdsList = (List<FilterData_By_ID_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
