package com.gi2.servicedecovoiturage;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;

import java.util.ArrayList;
import java.util.List;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class InteractionsActivity extends AppCompatActivity {

    //Current user
    static UserProfile currentUser;


    //List of rides
    static ArrayList<Interaction> interactionsList;
    static CustomAdapterInteractions adapter;
    ListView listView;

    TextView driver, client, state;

    //FloatingActionButton add;
    private FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        listView = findViewById(R.id.listView);
        interactionsList = new ArrayList<Interaction>();

        database = FirebaseFirestore.getInstance();

        /*add =  (FloatingActionButton)findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                NewRideDialog dialog = new NewRideDialog();
                dialog.show(getSupportFragmentManager(),"New Ride");
            }
        });*/

        loadDatabase();
        initCurrentUser();

    }

    private void loadDatabase(){

        adapter = new CustomAdapterInteractions(getApplicationContext(), interactionsList);
        listView.setAdapter(adapter);


        database.collection("reservations").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){

                                Interaction i = d.toObject(Interaction.class);
                                interactionsList.add(i);

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
                            currentUser.setUserid(documentSnapshot.getString("userid"));
                            currentUser.setAdmin(documentSnapshot.getBoolean("admin"));
                            currentUser.setPending(documentSnapshot.getBoolean("pending"));
                        } else {
                            Toast.makeText(InteractionsActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(InteractionsActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
