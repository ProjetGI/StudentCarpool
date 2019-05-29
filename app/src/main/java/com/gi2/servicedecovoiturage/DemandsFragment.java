package com.gi2.servicedecovoiturage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemandsFragment extends Fragment {



    //clicked Voyage
    Request clickedRequest;


    //List of rides
    static ArrayList<Request> RequestList;
    static CustomAdapterRequests adapter;
    ListView listView;

    ImageView item_bg;

    FloatingActionButton add;
    private FirebaseFirestore database;


    public DemandsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_demands, container, false);


        listView = (ListView) view.findViewById(R.id.listView);
        RequestList = new ArrayList<Request>();

        database = FirebaseFirestore.getInstance();

        add =  (FloatingActionButton) view.findViewById(R.id.fabrequest);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                NewRequestDialog dialog = new NewRequestDialog();
                dialog.show(getFragmentManager(),"New Request");
            }
        });

        loadDatabase();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedRequest = RequestList.get(i);

                RideInviteChooser dialog = new RideInviteChooser();
                dialog.setReq(clickedRequest);
                dialog.show(getFragmentManager(),"New Invitation");





                /*new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to book this ride?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity(), "Yaay", Toast.LENGTH_SHORT).show();
                                //HEREEE
                                Map<String, Object> data = new HashMap<>();
                                data.put("driver", LoginActivity.currentUser.getUsername());
                                data.put("client", clickedRequest.getClient());
                                data.put("state", "pending");
                                data.put("invitation", clickedRequest.getDocumentName());

                                database.collection("invitations").add(data);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();*/
            }
        });

        return view;
    }

    private void loadDatabase(){

        adapter = new CustomAdapterRequests(getActivity(), RequestList);
        listView.setAdapter(adapter);


        database.collection("requests").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){

                                Request r = d.toObject(Request.class);
                                r.setDocumentName(d.getId());
                                RequestList.add(r);

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });


    }



}
