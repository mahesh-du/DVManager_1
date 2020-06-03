package com.example.dvmanager_1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dvmanager_1.Model.EntryModel;
import com.example.dvmanager_1.R;

import java.io.Serializable;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {

    private List<EntryModel> entryList;
    private Context context;
    private static int currentPosition = 0;

    public RecyclerViewAdapter(List<EntryModel> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout_entry,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        EntryModel entry = entryList.get(position);

        myViewHolder.txt_Id.setText(String.valueOf(entry.getID()));
        myViewHolder.txt_Name.setText(entry.getName());
        myViewHolder.txt_Blocked.setText(entry.getBlocked().toString());
        myViewHolder.txt_Total_Entry.setText(String.valueOf(entry.getTotal_Entry()));
        myViewHolder.txt_Entry_Status.setText(entry.getEntry_Status());
        myViewHolder.txt_Entry_Gate.setText(entry.getEntry_Gate());
        myViewHolder.txt_Exit_Gate.setText(entry.getExit_Gate());
        myViewHolder.txt_Entry_Time.setText(entry.getEntry_Time().toString());
        myViewHolder.txt_Exit_Time.setText(entry.getExit_Time().toString());

        myViewHolder.linearLayout.setVisibility(View.GONE);

        String temp = entry.getEntry_Status();

        if(temp.equals("In"))
            myViewHolder.imgView_entry_status.setBackgroundResource(R.drawable.ic_circle_green) ;
        else if(temp.equals("Out"))
            myViewHolder.imgView_entry_status.setBackgroundResource(R.drawable.ic_circle_red) ;

        //myViewHolder.image.setImageBitmap(getDecodedImage(entry.getProfile_ImageByteString()));

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.circleCrop();
        options.error(R.drawable.ic_circle);

        Glide.with(context)
                .asBitmap()
                .apply(options)
                .load(getDecodedImage(entry.getProfile_ImageByteString()))
                .into(myViewHolder.image);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {

            //toggling visibility
            myViewHolder.linearLayout.setVisibility(View.VISIBLE);
        }

        myViewHolder.txt_Name.setOnClickListener(new View.OnClickListener() {
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
        return entryList.size();
    }

    public Serializable getDisplayedList(){
        return (Serializable) entryList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_Id, txt_Name, txt_Blocked, txt_Total_Entry, txt_Entry_Status, txt_Entry_Gate, txt_Exit_Gate, txt_Entry_Time, txt_Exit_Time;
        ImageView imgView_entry_status,image;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_Id = itemView.findViewById(R.id.txt_list_layout_entry_Id);
            txt_Name = itemView.findViewById(R.id.txt_list_layout_entry_Name);
            txt_Blocked = itemView.findViewById(R.id.txt_list_layout_entry_Blocked);
            txt_Total_Entry = itemView.findViewById(R.id.txt_list_layout_entry_Total_Entries);
            txt_Entry_Status = itemView.findViewById(R.id.txt_list_layout_entry_Entry_Status);
            txt_Entry_Gate = itemView.findViewById(R.id.txt_list_layout_entry_Entry_Gate);
            txt_Exit_Gate = itemView.findViewById(R.id.txt_list_layout_entry_Exit_Gate);
            txt_Entry_Time = itemView.findViewById(R.id.txt_list_layout_entry_Entry_Time);
            txt_Exit_Time = itemView.findViewById(R.id.txt_list_layout_entry_Exit_Time);

            imgView_entry_status = itemView.findViewById(R.id.imgView_list_layout_entry_Entry_status);
            image = itemView.findViewById(R.id.list_layout_entry_image);

            linearLayout = itemView.findViewById(R.id.list_layout_entry_linearLayout);
        }
    }

    public Bitmap getDecodedImage(String byteString)
    {
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
}
