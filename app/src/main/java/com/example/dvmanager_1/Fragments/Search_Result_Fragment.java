package com.example.dvmanager_1.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dvmanager_1.Adapters.Search_Result_Entries_RecyclerViewAdapter;
import com.example.dvmanager_1.AnalysisModels.AnalysisModel;
import com.example.dvmanager_1.AnalysisModels.Entries_Model;
import com.example.dvmanager_1.AnalysisModels.Entry_Model;
import com.example.dvmanager_1.AnalysisModels.Today;
import com.example.dvmanager_1.Constants;
import com.example.dvmanager_1.Model.Search_Result_EntriesModel;
import com.example.dvmanager_1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.dvmanager_1.Helper.UTC_To_Local_Time;
import static com.example.dvmanager_1.Helper.convert_Date_To;
import static com.example.dvmanager_1.Helper.getMonthName;
import static com.example.dvmanager_1.Helper.getValueFromSharedPreferences;

public class Search_Result_Fragment extends Fragment implements Constants {

    RecyclerView recyclerView;
    Search_Result_Entries_RecyclerViewAdapter adapter;
    List<Search_Result_EntriesModel> entriesList;

    SwipeRefreshLayout swipeRefreshLayout;

    public String scanned_Id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView txt_search_result_Name, txt_search_result_Id,txt_search_result_current_entry_status,
            txt_search_result_Blocked, txt_search_result_total_entries, txt_search_result_current_entry_or_exit_time;
    ImageView profileImage, block;

    public static String Institution_Path ;

    public Search_Result_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        Institution_Path = getValueFromSharedPreferences(getContext(), KEY_institution_path).toString();
        getActivity().setTitle("Search Result");

        swipeRefreshLayout = view.findViewById(R.id.search_result_swipeRefreshLayout);

        recyclerView = view.findViewById(R.id.search_result_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Retrieve the scanned_Id
        scanned_Id = getArguments().getString("scanned_Id");

        txt_search_result_Name = view.findViewById(R.id.txt_search_result_Name);
        txt_search_result_Id = view.findViewById(R.id.txt_search_result_Id);
        txt_search_result_current_entry_status = view.findViewById(R.id.txt_search_result_current_entry_status);
        txt_search_result_Blocked = view.findViewById(R.id.txt_search_result_Blocked);
        txt_search_result_total_entries = view.findViewById(R.id.txt_search_result_total_entries);
        txt_search_result_current_entry_or_exit_time = view.findViewById(R.id.txt_search_result_current_entry_or_exit_time);
        profileImage = view.findViewById(R.id.search_result_image);
        block = view.findViewById(R.id.imageView_search_result_block);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_search_result_Blocked.getText().equals("Yes")){
                    block_ID(scanned_Id, true);
                    block.setBackgroundResource(R.drawable.ic_block_green_24dp);
                    txt_search_result_Blocked.setText("No");
                }
                else if(txt_search_result_Blocked.getText().equals("No")) {
                    block_ID(scanned_Id, false);
                    block.setBackgroundResource(R.drawable.ic_block_red_24dp);
                    txt_search_result_Blocked.setText("Yes");
                }

            }
        });

        entriesList = new ArrayList<>();

        loadData();
        entriesList.clear();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
                entriesList.clear();

            }
        });

        return view;
    }

    public void loadData()
    {
        db.document(Institution_Path + firestore_ids +"/" + scanned_Id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()) {
                            txt_search_result_Id.setText(scanned_Id);
                            txt_search_result_Name.setText(documentSnapshot.getString(firestore_IDS_FIELD_Name));

                            RequestOptions options = new RequestOptions();
                            options.centerCrop();
                            options.circleCrop();
                            options.error(R.drawable.ic_circle);

                            Glide.with(getContext())
                                    .asBitmap()
                                    .apply(options)
                                    .load(getDecodedImage(documentSnapshot.getString(firestore_IDS_FIELD_Image)))
                                    .into(profileImage);

                            if (documentSnapshot.getBoolean(firestore_IDS_FIELD_Blocked))              //true.
                            {
                                txt_search_result_Blocked.setText("Yes");
                                block.setBackgroundResource(R.drawable.ic_block_red_24dp);
                            }else if (!documentSnapshot.getBoolean(firestore_IDS_FIELD_Blocked))        //false.
                            {
                                txt_search_result_Blocked.setText("No");
                                block.setBackgroundResource(R.drawable.ic_block_green_24dp);
                            }
                            getEntries();
                        }
                        else
                            Toast.makeText(getContext(), "ID not found." , Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "ID not found. Error: " + e, Toast.LENGTH_LONG).show();
                    }
                })
        ;
    }


    public void block_ID(String id, final boolean wasBlocked){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        if (wasBlocked)
            progressDialog.setMessage("Unblocking ID");
        else
            progressDialog.setMessage("Blocking ID");
        progressDialog.show();

        db.document(Institution_Path + firestore_ids + "/" + id )
                .update(firestore_IDS_FIELD_Blocked, !wasBlocked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(!wasBlocked)
                            Toast.makeText(getContext(), "ID Blocked successfully.", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(), "ID Unblocked successfully.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Unable to block this device. Error: " + e, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
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

    public void getEntries()
    {
        final Date entries_Of_This_Date= new Date();
        String path = Institution_Path + firestore_document_ANALYSIS_STATISTICS_YEAR +
                        "/" + convert_Date_To("YEAR", entries_Of_This_Date)+ "/" +
                        getMonthName(Integer.parseInt(convert_Date_To("MONTH", new Date())));
        db.document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists())
                        {
                            AnalysisModel analysisModel = documentSnapshot.toObject(AnalysisModel.class);
                            if(!analysisModel.getData().isEmpty()){
                                for(Today today : analysisModel.getData()){
                                    if(convert_Date_To("DATE", today.getDate()).equals(convert_Date_To("DATE", entries_Of_This_Date))){
                                       for (Entries_Model entries_model : today.getEntries()){
                                           if(entries_model.getId().toString().equals(scanned_Id)) {
                                               String total_entries = String.valueOf(entries_model.getCount());
                                               String entry_status = entries_model.getDetails().getEntry().get(entries_model.getDetails().getEntry().size() - 1).getStatus();

                                               txt_search_result_total_entries.setText(total_entries);
                                               txt_search_result_current_entry_status.setText(entry_status);

                                               if(entry_status.equals("In"))
                                                   txt_search_result_current_entry_or_exit_time.setText(String.valueOf(entries_model.getDetails().getEntry().get(entries_model.getDetails().getEntry().size() - 1).getTime().getEntry_Time()));
                                               else if(entry_status.equals("Out"))
                                                   txt_search_result_current_entry_or_exit_time.setText(String.valueOf(entries_model.getDetails().getEntry().get(entries_model.getDetails().getEntry().size() - 1).getTime().getExit_Time()));

                                               for (Entry_Model entry_model : entries_model.getDetails().getEntry()){
                                                   entriesList.add(new Search_Result_EntriesModel(UTC_To_Local_Time(entry_model.getTime().getEntry_Time().toString()),UTC_To_Local_Time(entry_model.getTime().getExit_Time().toString()),
                                                           entry_model.getStatus(), entry_model.getGate().getEntry_Gate(), entry_model.getGate().getExit_Gate()));
                                               }
                                           }
                                       }
                                    }
                                }
                            }

                            Collections.reverse(entriesList);
                            adapter = new Search_Result_Entries_RecyclerViewAdapter(entriesList, getContext());
                            recyclerView.setAdapter(adapter);
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), "Refresh complete", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getContext(), "No Entry Found.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
