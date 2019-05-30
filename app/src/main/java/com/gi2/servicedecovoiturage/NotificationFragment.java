package com.gi2.servicedecovoiturage;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gi2.servicedecovoiturage.logregform.R;

import com.gi2.servicedecovoiturage.logregform.R;
import com.gi2.servicedecovoiturage.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationFragment extends Fragment {

    Interaction clickedInteraction;
    Interaction activeInteraction;

    ArrayList<String> reservationsListString = new ArrayList<String>();
    ArrayList<Interaction> interactionsList = new ArrayList<Interaction>();
    ArrayAdapter adapter ;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    Button viewOffer,contact,markascompleted;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.reservations_item, reservationsListString);


        ListView listView = (ListView)view.findViewById(R.id.reservations_list);
        listView.setAdapter(adapter);

        viewOffer=view.findViewById(R.id.viewoffer);
        contact=view.findViewById(R.id.chatnow);
        markascompleted=view.findViewById(R.id.markascompleted);
        final TextView activeRide = view.findViewById(R.id.activeRide);

        database.collection("reservations").whereEqualTo("driver", LoginActivity.currentUser.getUsername()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {

                                Interaction i = d.toObject(Interaction.class);

                                if(i.getState().equals("pending")){
                                    reservationsListString.add("Reservation Request : The client " + i.getClient() + " has sent you a reservation request. ");
                                    i.setId(d.getId());
                                    i.setType("reservation");
                                    interactionsList.add(i);
                                }

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });


        database.collection("invitations").whereEqualTo("client", LoginActivity.currentUser.getUsername()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {

                                Interaction i = d.toObject(Interaction.class);

                                if(i.getState().equals("pending")){

                                    reservationsListString.add("Invitation : The driver " + i.getDriver() + " has sent you an invitation to one of his offers, Click here for more details");
                                    i.setId(d.getId());
                                    i.setType("invitation");
                                    interactionsList.add(i);
                                }


                            }

                            adapter.notifyDataSetChanged();
                        }

                    }
                });


        database.collection("reservations").whereEqualTo("state", "active")
                .whereEqualTo("driver", LoginActivity.currentUser.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            DocumentSnapshot d=list.get(0);
                            activeInteraction = d.toObject(Interaction.class);
                            activeInteraction.setId(d.getId());
                            activeInteraction.setType("reservation");
                            if(activeInteraction.getDriver().equals(LoginActivity.currentUser.getUsername())){
                                contact.setText("Contact clients");
                            }


                            contact.setVisibility(View.VISIBLE);
                            viewOffer.setVisibility(View.VISIBLE);
                            markascompleted.setVisibility(View.VISIBLE);
                            activeRide.setText("Active Ride :");

                        }
                        else{

                        }

                    }
                });



        database.collection("reservations").whereEqualTo("state", "active")
                .whereEqualTo("client", LoginActivity.currentUser.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            DocumentSnapshot d=list.get(0);
                            activeInteraction = d.toObject(Interaction.class);
                            activeInteraction.setId(d.getId());
                            activeInteraction.setType("reservation");

                            if(activeInteraction.getDriver().equals(LoginActivity.currentUser.getUsername())){
                                contact.setText("Contact clients");
                            }

                            contact.setVisibility(View.VISIBLE);
                            viewOffer.setVisibility(View.VISIBLE);
                            markascompleted.setVisibility(View.VISIBLE);
                            activeRide.setText("Active Ride :");

                        }
                        else{

                        }

                    }
                });


        viewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference offer = database.collection("offers").document(activeInteraction.getOffer());
                offer.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Voyage v = documentSnapshot.toObject(Voyage.class);
                                    RidePopup ridePopup = new RidePopup(getActivity());
                                    ridePopup.hideButtons();
                                    ridePopup.show(v);
                                } else {
                                    Toast.makeText(getActivity() , "Document does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activeInteraction.getClient().equals(LoginActivity.currentUser.getUsername())){
                    UserDetails.chatWith = activeInteraction.getDriver();
                    Intent chatIntent = new Intent(getActivity(),Chat.class);
                    getActivity().startActivity(chatIntent);
                }

                else{
                    //startActivity(new Intent(getActivity(), Users.class));
                    final ArrayList<String> clients = new ArrayList<>();
                    database.collection("reservations").whereEqualTo("state", "active")
                            .whereEqualTo("driver", LoginActivity.currentUser.getUsername())
                            .whereEqualTo("offer", activeInteraction.getOffer())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            activeInteraction = d.toObject(Interaction.class);
                                            clients.add(activeInteraction.getClient());
                                        }

                                    }


                                    Toast.makeText(getActivity(), "List Size : "+clients.size(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), Users.class);
                                    Bundle b = new Bundle();
                                    b.putStringArrayList("clients", clients);
                                    intent.putExtras(b); //Put your id to your next Intent
                                    startActivity(intent);

                                }
                            });





                    //show list of clients!!!
                }
;
            }
        });

        markascompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activeInteraction.getClient().equals(LoginActivity.currentUser.getUsername())){
                    Toast.makeText(getActivity(), "Only the driver can mark the ride as completed", Toast.LENGTH_SHORT).show();
                }
                else if(activeInteraction.getDriver().equals(LoginActivity.currentUser.getUsername())){

                    database.collection("reservations").whereEqualTo("state", "active")
                            .whereEqualTo("driver", LoginActivity.currentUser.getUsername())
                            .whereEqualTo("offer", activeInteraction.getOffer())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot d : list) {

                                            DocumentReference reservation = database.collection("reservations").document(d.getId());
                                            reservation.update("state", "completed");
                                        }

                                    }


                                    Toast.makeText(getActivity(), "The ride is marked as completed successfully!", Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedInteraction = interactionsList.get(i);
                //final int clickedReservation = i;
                //final boolean isDriver = clickedInteraction.getDriver().equals(LoginActivity.currentUser.getUsername());

                if(clickedInteraction.getType().equals("invitation")){

                    DocumentReference offer = database.collection("offers").document(clickedInteraction.getOffer());
                    offer.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {

                                        Voyage v = documentSnapshot.toObject(Voyage.class);
                                        RidePopup ridePopup = new RidePopup(getActivity());
                                        ridePopup.show(v);

                                        DocumentReference invitation = database.collection("invitations").document(clickedInteraction.getId());
                                        invitation.update("state", "reservation-sent");
                                    } else {
                                        Toast.makeText(getActivity() , "Document does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();

                                }
                            });


                }
                else if(clickedInteraction.getType().equals("reservation")){
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Reservation confirmation");
                    builder.setMessage("Are you sure you want to accept this ride request from the client "+clickedInteraction.getClient()+" ?");

                    // add the buttons
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                            DocumentReference offer = database.collection("offers").document(clickedInteraction.getOffer());
                            offer.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {

                                                Voyage v = documentSnapshot.toObject(Voyage.class);
                                                int newNombreDePlaces = Integer.parseInt(v.getCurrentPlaces())+1;

                                                DocumentReference offer = database.collection("offers").document(clickedInteraction.getOffer());
                                                offer.update("currentPlaces", Integer.toString(newNombreDePlaces));

                                                DocumentReference reservation = database.collection("reservations").document(clickedInteraction.getId());
                                                reservation.update("state", "active");


                                            } else {
                                                Toast.makeText(getActivity() , "Document does not exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }
                    });
                    builder.setNeutralButton("View Ride", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DocumentReference offer = database.collection("offers").document(clickedInteraction.getOffer());
                            offer.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {

                                                Voyage v = documentSnapshot.toObject(Voyage.class);
                                                RidePopup ridePopup = new RidePopup(getActivity());
                                                ridePopup.hideButtons();
                                                ridePopup.show(v);


                                            } else {
                                                Toast.makeText(getActivity() , "Document does not exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }




            }
        });

        return view;
    }






}
