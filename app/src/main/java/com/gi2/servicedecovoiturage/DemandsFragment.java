package com.gi2.servicedecovoiturage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class DemandsFragment extends Fragment {



    //clicked Voyage
    Request clickedRequest;


    //List of rides
    static ArrayList<Request> RequestList;
    static CustomAdapterRequests adapter;
    ListView listView;
    EditText departureFilter, arrivalFilter, dateFilter;

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

        add =  (FloatingActionButton) view.findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), AddRequest.class);
                startActivity(intent);
            }
        });

        /*
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                NewRequestDialog dialog = new NewRequestDialog();
                dialog.show(getFragmentManager(),"New Request");
            }
        }); */

        loadDatabase();
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
                            
                            adapter = new CustomAdapterRequests(getActivity(), RequestList);
                            listView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();

                        }


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
