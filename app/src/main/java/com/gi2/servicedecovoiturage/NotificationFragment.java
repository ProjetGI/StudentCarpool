package com.gi2.servicedecovoiturage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gi2.servicedecovoiturage.logregform.R;

import com.gi2.servicedecovoiturage.logregform.R;
import com.gi2.servicedecovoiturage.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationFragment extends Fragment {

    Interaction clickedInteraction;

    ArrayList<String> reservationsList = new ArrayList<String>();
    ArrayList<Interaction> interactionsList = new ArrayList<Interaction>();
    ArrayAdapter adapter ;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

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
                R.layout.reservations_item, reservationsList);


        ListView listView = (ListView)view.findViewById(R.id.reservations_list);
        listView.setAdapter(adapter);

        database.collection("reservations").whereEqualTo("driver", LoginActivity.currentUser.getUsername()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {

                                Interaction i = d.toObject(Interaction.class);
                                boolean isActive = (i.getState().equals("Active"));
                                i.setDriverPhoneNumber(LoginActivity.currentUser.getPhoneNumber());

                                reservationsList.add("The client " + i.getClient() + " has sent you a reservation request. "
                                        //+(isActive?"He can be reached at +"+i.getClientPhoneNumber():"")
                                        +". (Status :" + i.getState() + ")");


                                i.setId(d.getId());
                                interactionsList.add(i);

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });

        database.collection("reservations").whereEqualTo("client", LoginActivity.currentUser.getUsername()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {

                                Interaction i = d.toObject(Interaction.class);
                                boolean isActive = (i.getState().equals("Active"));
                                i.setClientPhoneNumber(LoginActivity.currentUser.getPhoneNumber());


                                reservationsList.add("You have sent a reservation request for the driver "+i.getDriver()
                                        //+(isActive?"He can be reached at +"+i.getDriverPhoneNumber():"")
                                        +". (Status :" + i.getState() + ")");

                                i.setId(d.getId());
                                interactionsList.add(i);

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedInteraction = interactionsList.get(i);
                final int clickedReservation = i;
                final boolean isDriver = clickedInteraction.getDriver().equals(LoginActivity.currentUser.getUsername());

                if(clickedInteraction.getState().equals("pending")){

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirmation")
                            .setMessage(isDriver?"Do you really want to accept this request?":"Do you want to cancel this request?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(getActivity(), "Yaay", Toast.LENGTH_SHORT).show();

                                    String newState = isDriver?"Active":"Canceled";
                                    database.collection("reservations").document(clickedInteraction.getId())
                                            .update("state",newState);
                                    clickedInteraction.setState(newState);

                                    reservationsList.remove(clickedReservation);
                                    reservationsList.add(clickedReservation, isDriver?"The ride is now active! You should contact the client for more informations."
                                            :"The reservation request for the driver "+clickedInteraction.getDriver()+" has been canceled");

                                    adapter.notifyDataSetChanged();


                                }})
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {


                                    String newState = isDriver?"Declined":clickedInteraction.getState();
                                    database.collection("reservations").document(clickedInteraction.getId())
                                            .update("state",newState);
                                    clickedInteraction.setState(newState);

                                    reservationsList.remove(clickedReservation);
                                    reservationsList.add(clickedReservation, isDriver?"You have declined this request!"
                                            :"You have sent a reservation request for the driver "+clickedInteraction.getDriver()+". (Status :" + clickedInteraction.getState() + ")");

                                    adapter.notifyDataSetChanged();

                                }}).show();

                }




            }
        });

        return view;
    }



}
