package com.example.dvmanager_1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dvmanager_1.Model.Student_Id_Model;
import com.example.dvmanager_1.R;

import java.util.ArrayList;
import java.util.List;

public class My_Ids_RecyclerViewAdapter extends RecyclerView.Adapter<My_Ids_RecyclerViewAdapter.MyViewHolder> implements Filterable {

    private List<Student_Id_Model> idsList;
    private List<Student_Id_Model> filtered_IdsList;
    private static int currentPosition = 0;
    private Context context;

    public My_Ids_RecyclerViewAdapter(Context context, List<Student_Id_Model> IdsList) {
        this.context = context;
        this.filtered_IdsList = IdsList;
        idsList = IdsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_my_ids,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        Student_Id_Model id = filtered_IdsList.get(position);

        myViewHolder.txt_Id             .setText(String.valueOf(id.getId()));
        myViewHolder.txt_Name           .setText(id.getName());
        myViewHolder.txt_age            .setText(String.valueOf(id.getAge()));
        myViewHolder.txt_gender         .setText(id.getGender());
        myViewHolder.txt_email          .setText(id.getEmail());
        myViewHolder.txt_phone_no       .setText(String.valueOf(id.getPhone_No()));
        myViewHolder.txt_admission_no   .setText(String.valueOf(id.getAdmission_No()));
        myViewHolder.txt_address        .setText(id.getAddress());

        myViewHolder.inner_linearLayout.setVisibility(View.GONE);

        //myViewHolder.profile_picture.setImageBitmap(getDecodedImage(id.getProfile_Picture()));

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.circleCrop();
        options.error(R.drawable.ic_circle);

        Glide.with(context)
                .asBitmap()
                .apply(options)
                .load(getDecodedImage(id.getProfile_Picture()))
                .into(myViewHolder.profile_picture);

        //if the position is equals to the item position which is to be expanded
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

    @Override
    public int getItemCount() {
        return filtered_IdsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_Id, txt_Name, txt_age, txt_gender, txt_email, txt_phone_no, txt_admission_no, txt_address;
        ImageView profile_picture;
        LinearLayout outer_linearLayout, inner_linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_Id              = itemView.findViewById(R.id.txt_list_layout_my_Ids_Id);
            txt_Name            = itemView.findViewById(R.id.txt_list_layout_my_Ids_Name);
            txt_age             = itemView.findViewById(R.id.txt_list_layout_my_Ids_Age);
            txt_gender          = itemView.findViewById(R.id.txt_list_layout_my_Ids_Gender);
            txt_email           = itemView.findViewById(R.id.txt_list_layout_my_Ids_Email);
            txt_phone_no        = itemView.findViewById(R.id.txt_list_layout_my_Ids_Phone_No);
            txt_admission_no    = itemView.findViewById(R.id.txt_list_layout_my_Ids_Admission_No);
            txt_address         = itemView.findViewById(R.id.txt_list_layout_my_Ids_Address);

            profile_picture     = itemView.findViewById(R.id.list_layout_my_ids_profile_picture);

            outer_linearLayout  = itemView.findViewById(R.id.outer_list_layout_entry_linearLayout);
            inner_linearLayout  = itemView.findViewById(R.id.inner_list_layout_my_Ids_linearLayout);
        }
    }

    public Bitmap getDecodedImage(String byteString)
    {
        if(byteString==null)
            return null;
        byte[] imageBytes = Base64.decode(byteString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return decodedImage;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filtered_IdsList = idsList;
                } else {

                    ArrayList<Student_Id_Model> filteredList = new ArrayList<>();

                    for (Student_Id_Model id : idsList) {

                        if (String.valueOf(id.getId()).toLowerCase().contains(charString.toLowerCase())
                                || id.getName().toLowerCase().contains(charString.toLowerCase())
                                || id.getEmail().toLowerCase().contains(charString.toLowerCase())) {

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
                filtered_IdsList = (List<Student_Id_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
