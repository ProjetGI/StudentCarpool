package com.gi2.servicedecovoiturage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Ride extends AppCompatActivity {

    //Current user
    static UserProfile currentUser;

    //clicked Voyage
    Voyage clickedVoyage;


    //List of rides
    static ArrayList<Voyage> VoyageList;
    static CustomAdapter adapter;
    ListView listView;

    TextView departure, arrival, price;
    ImageView item_bg;

    FloatingActionButton add;
    private FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        listView = findViewById(R.id.listView);
        VoyageList = new ArrayList<Voyage>();

        database = FirebaseFirestore.getInstance();

        add =  (FloatingActionButton)findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                NewRideDialog dialog = new NewRideDialog();
                dialog.show(getSupportFragmentManager(),"New Ride");
            }
        });

        loadDatabase();
        initCurrentUser();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedVoyage = VoyageList.get(i);
                new AlertDialog.Builder(Ride.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to book this ride?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(Ride.this, "Yaay", Toast.LENGTH_SHORT).show();
                                //HEREEE
                                Map<String, Object> data = new HashMap<>();
                                data.put("client", currentUser.getUsername());
                                data.put("driver", clickedVoyage.getmDriver());
                                data.put("state", "pending");
                                data.put("offer", clickedVoyage.getDocumentName());

                                database.collection("reservations").add(data);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    private void loadDatabase(){

        adapter = new CustomAdapter(getApplicationContext(), VoyageList);
        listView.setAdapter(adapter);


        database.collection("offers").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){

                                Voyage v = d.toObject(Voyage.class);
                                v.setDocumentName(d.getId());
                                VoyageList.add(v);

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });


    }

    private void initCurrentUser(){

        currentUser = new UserProfile("test","test","test","test","test",false,"test");

        String currentDriverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Toast.makeText(this, "UID IS "+currentDriverUID, Toast.LENGTH_SHORT).show();
        DocumentReference currentDriverInfo = database.collection("users").document(currentDriverUID);


        currentDriverInfo.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            currentUser.setUsername(documentSnapshot.getString("username"));
                            currentUser.setEmail(documentSnapshot.getString("email"));
                            currentUser.setAdmin(documentSnapshot.getBoolean("admin"));
                            currentUser.setEmail(documentSnapshot.getString("users"));

                        } else {
                            Toast.makeText(Ride.this , "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Ride.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
