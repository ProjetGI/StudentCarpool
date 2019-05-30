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


public class NewReportDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "NewReportDialog";

    //widgets
    private EditText titre, username, description;
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
        View view = inflater.inflate(R.layout.dialog_new_report, container, false);
        titre = view.findViewById(R.id.report_title);
//        username = view.findViewById(R.id.report_username);
        description = view.findViewById(R.id.report_description);

        mCreate = view.findViewById(R.id.report_create);
        mCancel = view.findViewById(R.id.report_cancel);

        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);

        getDialog().setTitle("New Report");

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.report_create:{


                String currentUser = LoginActivity.currentUser.getUsername();
//                String usernameST = username.getText().toString();
                String titreST = titre.getText().toString();
                String descriptionST = description.getText().toString();


                if(!( titreST.equals("")&&descriptionST.equals(""))){
                    getDialog().dismiss();
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", currentUser);
                    data.put("titre", titreST);
//                    data.put("username", usernameST);
                    data.put("description", descriptionST);
                    data.put("reported_user", "test");

                    data.put("currentUserUID",FirebaseAuth.getInstance().getUid());
                    database.collection("reports").add(data);
                    //Toast.makeText(getActivity(), driver, Toast.LENGTH_SHORT).show();

//                    OffersFragment.VoyageList.add(new Voyage(driver,departure,arrival,price,time,date));
//                    OffersFragment.adapter.notifyDataSetChanged();


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