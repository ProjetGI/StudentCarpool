package com.gi2.servicedecovoiturage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OffersFragment extends Fragment {

    //Current user
    static UserProfile currentUser;

    //clicked Voyage
    Voyage clickedVoyage;
    RidePopup ridePopup;


    //List of rides
    static ArrayList<Voyage> VoyageList;
    static CustomAdapter adapter;
    ListView listView;
    EditText departureFilter, arrivalFilter, dateFilter;

    TextView departure, arrival, price;
    ImageView item_bg;

    FloatingActionButton add;
    private FirebaseFirestore database;


    public OffersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        ridePopup = new RidePopup(getActivity());

        listView = (ListView) view.findViewById(R.id.listView);
        VoyageList = new ArrayList<Voyage>();

        database = FirebaseFirestore.getInstance();

        add =  (FloatingActionButton) view.findViewById(R.id.fab);
        /*add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                NewRideDialog dialog = new NewRideDialog();
                dialog.show(getFragmentManager(),"New Ride");
            }
        });*/

        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), AddRide.class);
                startActivity(intent);
            }
        });

        loadDatabase();
        //initCurrentUser();

        departureFilter = (EditText) view.findViewById(R.id.departureFilter);
        departureFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filter(departureFilter.getText().toString());
            }
        });

        arrivalFilter = (EditText) view.findViewById(R.id.arrivalFilter);
        arrivalFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filterArrival(arrivalFilter.getText().toString());
            }
        });

        dateFilter= (EditText) view.findViewById(R.id.dateFilter);

        dateFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filterDate(dateFilter.getText().toString());
            }
        });

        pickDate();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedVoyage = VoyageList.get(i);
                ridePopup.show(clickedVoyage);

                /*new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to book this ride?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity(), "Yaay", Toast.LENGTH_SHORT).show();
                                //HEREEE
                                Map<String, Object> data = new HashMap<>();
                                data.put("client", currentUser.getUsername());
                                data.put("driver", clickedVoyage.getmDriver());
                                data.put("state", "pending");
                                data.put("offer", clickedVoyage.getDocumentName());

                                database.collection("reservations").add(data);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();*/
            }
        });

        return view;
    }

    private void loadDatabase(){
        //adapter = new CustomAdapter(getActivity(), VoyageList);
        //listView.setAdapter(adapter);
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
                            adapter = new CustomAdapter(getActivity(), VoyageList);
                            listView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
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

    private void pickDate(){
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateFilter.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

}
