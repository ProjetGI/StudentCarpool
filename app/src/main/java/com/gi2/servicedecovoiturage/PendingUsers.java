
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

public class PendingUsers  extends Fragment {

    //clicked Voyage
    UserProfile user;
    static ArrayList<UserProfile> userPendingList;
    static CustomAdapterUserPending adapter;
    ListView listView;
    private FirebaseFirestore database;


    public PendingUsers() {
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
        userPendingList = new ArrayList<UserProfile>();

        database = FirebaseFirestore.getInstance();



        loadDatabase();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                user = userPendingList.get(i);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("Do you want to approve this profile?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity(), "profile approved", Toast.LENGTH_SHORT).show();
                                //registerUser(user.getUsername(),user.getPassword(), user.getEmail(), user.getPhoneNumber());
                                database.collection("users").document(user.getUserid())
                                        .update("pending",false );
                                userPendingList.remove(user);
                                adapter.notifyDataSetChanged();
                                listView.invalidateViews();



                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        return view;
    }

    private void loadDatabase(){

        adapter = new CustomAdapterUserPending(getActivity(), userPendingList);
        listView.setAdapter(adapter);


        database.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                if(d.getBoolean("pending"))
                                {
                                    UserProfile currentUser = new UserProfile();
                                    currentUser.setUsername(d.getString("username"));
                                    currentUser.setEmail(d.getString("email"));
                                    currentUser.setPhoneNumber(d.getString("phone-number"));
                                    currentUser.setCNE(d.getString("CNE"));
                                    currentUser.setUserid(d.getString("userid"));

                                    userPendingList.add(currentUser);
                                }

                            }

                            adapter.notifyDataSetChanged();

                        }


                    }
                });



    }



}


