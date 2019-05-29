package com.gi2.servicedecovoiturage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

import com.gi2.servicedecovoiturage.Ride;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class RideInviteChooser extends DialogFragment{

    private static final String TAG = "RideInviteChooser";
    final String errorMessage="You have no active rides currently!";
    private Request req;
    Spinner spin;
    private Button invite,cancel;
    private FirebaseFirestore database;
    ArrayList<Voyage> VoyageList;
    Voyage selectedVoyage;


    public void setReq(Request req) {
        this.req=req;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);

        database = FirebaseFirestore.getInstance();
        VoyageList = new ArrayList<Voyage>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_invite_chooser, container, false);
        spin = (Spinner) view.findViewById(R.id.spinner);

        final ArrayList<String> choices = new ArrayList<String>();
        database.collection("offers").whereEqualTo("mDriver", LoginActivity.currentUser.getUsername()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){

                                Voyage v = d.toObject(Voyage.class);
                                v.setDocumentName(d.getId());
                                VoyageList.add(v);
                                choices.add(v.getmDeparture()+"→"+v.getmArrival() + " ("+v.getmPrice()+"DH)");


                            }

                        }

                        else{
                            choices.add(errorMessage);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, choices);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(adapter);


                    }
                });






        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!choices.get(0).equals(errorMessage))
                    selectedVoyage = VoyageList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        invite = view.findViewById(R.id.invite);
        cancel = view.findViewById(R.id.cancel);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!choices.get(0).equals(errorMessage)){
                    Map<String, Object> data = new HashMap<>();
                    data.put("driver", LoginActivity.currentUser.getUsername());
                    data.put("client", req.getClient());
                    data.put("state", "pending");
                    data.put("invitationID", req.getDocumentName());
                    data.put("offer", selectedVoyage.getDocumentName());

                    database.collection("invitations").add(data);

                    Toast.makeText(getActivity(), "You have successfully sent the invitation!", Toast.LENGTH_SHORT).show();
                }

                getDialog().dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();


            }
        });


        getDialog().setTitle("New Invitation");

        return view;
    }




}