package com.gi2.servicedecovoiturage;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gi2.servicedecovoiturage.Ride;
import com.google.firebase.firestore.auth.User;


public class NewRideDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "NewRideDialog";

    //widgets
    private EditText mDeparture, mArrival, mPrice, mTime, mDate;
    private TextView mCreate, mCancel;
    private FirebaseFirestore database;



    //vars


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);

        database = FirebaseFirestore.getInstance();    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_ride, container, false);
        mDeparture = view.findViewById(R.id.ride_departure);
        mArrival = view.findViewById(R.id.ride_arrival);
        mPrice = view.findViewById(R.id.ride_price);
        mTime = view.findViewById(R.id.ride_time);
        mDate = view.findViewById(R.id.ride_date);
        mCreate = view.findViewById(R.id.ride_create);
        mCancel = view.findViewById(R.id.ride_cancel);

        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);

        getDialog().setTitle("New Ride");

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.ride_create:{


                String driver = LoginActivity.currentUser.getUsername();
                String departure = mDeparture.getText().toString();
                String arrival = mArrival.getText().toString();
                String price = mPrice.getText().toString();
                String time = mTime.getText().toString();
                String date = mDate.getText().toString();

                if(!(departure.equals("")&& arrival.equals("")&&price.equals("")&&time.equals("")&&date.equals(""))){
                    getDialog().dismiss();
                    Map<String, Object> data = new HashMap<>();
                    data.put("mDriver", driver);
                    data.put("mDeparture", departure);
                    data.put("mArrival", arrival);
                    data.put("mPrice", price);
                    data.put("mTime", time);
                    data.put("mDate", date);
                    data.put("driverUID",FirebaseAuth.getInstance().getUid());
                    database.collection("offers").add(data);
                    //Toast.makeText(getActivity(), driver, Toast.LENGTH_SHORT).show();

                    OffersFragment.VoyageList.add(new Voyage(driver,departure,arrival,price,time,date,"","",""));
                    OffersFragment.adapter.notifyDataSetChanged();


                }
                else{
                    Toast.makeText(getActivity(), "Data missing", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.ride_cancel:{
                getDialog().dismiss();
                break;
            }
        }
    }

}