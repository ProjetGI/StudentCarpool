package com.gi2.servicedecovoiturage;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RidePopup{

    Dialog myDialog; //Main Dialog
    private FirebaseFirestore database;
    FirebaseStorage storage;
    private ImageView profilePic;
    private TextView name, departure,arrival,date,price,places,description,txtclose;
    Button action,contact,viewprofile;
    Context ctx;


    public RidePopup(Context ctx){

        storage = FirebaseStorage.getInstance();

        myDialog = new Dialog(ctx);
        myDialog.setContentView(R.layout.ride_popup);

        this.ctx = ctx;
        database = FirebaseFirestore.getInstance();


        name = myDialog.findViewById(R.id.fullname);
        arrival = myDialog.findViewById(R.id.arrival);
        departure = myDialog.findViewById(R.id.departure);
        date = myDialog.findViewById(R.id.date);
        price = myDialog.findViewById(R.id.price);
        places = myDialog.findViewById(R.id.places);
        description = myDialog.findViewById(R.id.maxPlaces);
        profilePic = myDialog.findViewById(R.id.photo);

        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        action = myDialog.findViewById(R.id.action);
        contact = myDialog.findViewById(R.id.message);
        viewprofile = myDialog.findViewById(R.id.viewprofile);


    }

    public void show(Voyage v) {

        final Voyage finalVoyage = v;

        name.setText(v.getmDriver());
        departure.setText(v.getmDeparture());
        arrival.setText(v.getmArrival());
        date.setText(v.getmDate());
        price.setText(v.getmPrice());
        description.setText(v.getmDescription());
        places.setText(v.getCurrentPlaces() + "/" + v.getMaxPlaces());

        StorageReference storageRef = storage.getReferenceFromUrl("gs://service-de-covoiturage.appspot.com")
                .child("users/"+ v.getmDriver() +".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profilePic.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}


        action.setText("Book this ride");
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("client", LoginActivity.currentUser.getUsername());
                data.put("driver", finalVoyage.getmDriver());
                data.put("state", "pending");
                data.put("offer", finalVoyage.getDocumentName());

                database.collection("reservations").add(data);

                myDialog.dismiss();
                Toast.makeText(ctx, "Your request for this ride has been sent successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetails.chatWith = finalVoyage.getmDriver();
                Intent chatIntent = new Intent(ctx,Chat.class);
                ctx.startActivity(chatIntent);

            }
        });

        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.collection("users").whereEqualTo("username", finalVoyage.getmDriver())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                DocumentSnapshot d = list.get(0);

                                ArrayList<String> infos = new ArrayList<>();
                                infos.add(d.getString("username"));
                                infos.add(d.getString("email"));
                                infos.add(d.getString("phone-number"));


                                Intent intent = new Intent(ctx, profilActivity.class);
                                Bundle b = new Bundle();
                                b.putStringArrayList("infos", infos);
                                intent.putExtras(b); //Put your id to your next Intent
                                ctx.startActivity(intent);



                            }
                        });

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }


    public void hideButtons(){
        action.setVisibility(View.GONE);
        contact.setVisibility(View.GONE);
    }



}
