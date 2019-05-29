
package com.gi2.servicedecovoiturage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.firebase.client.Firebase;
import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ReportUsers  extends Fragment {

    //clicked Voyage
    Report report;
    static ArrayList<Report> ReportList;
    static CustomAdapterUserReports adapter;
    ListView listView;
    private FirebaseFirestore database;


    public ReportUsers() {
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
        View view = inflater.inflate(R.layout.fragement_pend, container, false);


        listView = (ListView) view.findViewById(R.id.listView);
        ReportList = new ArrayList<Report>();

        database = FirebaseFirestore.getInstance();



        loadDatabase();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                report = ReportList.get(i);

                                Intent intent = new Intent(getContext(),ReportDetail.class);
                                intent.putExtra ("user", report.getUsername());
                                intent.putExtra ("reported", report.getReported_user());
                                intent.putExtra ("description", report.getDescription());
                                intent.putExtra ("titre", report.getTitre());
                                startActivity(intent);

                            }});



        return view;
    }

    private void loadDatabase(){

        adapter = new CustomAdapterUserReports(getActivity(), ReportList);
        listView.setAdapter(adapter);


        database.collection("reports").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){

                                    Report r = new Report();
                                    r.setDescription(d.getString("description"));
                                    r.setUsername(d.getString("username"));
                                    r.setReported_user(d.getString("reported_user"));
                                    r.setTitre(d.getString("titre"));
                                    ReportList.add(r);

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });



    }



}


