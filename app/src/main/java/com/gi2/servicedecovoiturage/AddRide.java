package com.gi2.servicedecovoiturage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddRide extends AppCompatActivity {

    private final static int PLACE_PICKER_REQUEST = 999;
    EditText arrivalPicker;
    EditText departurePicker;
    EditText datePicker;
    EditText timePicker ;
    EditText pricePicker;
    EditText descriptionPicker;
    EditText placesPicker;
    private FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ride);

        database = FirebaseFirestore.getInstance();

        arrivalPicker = findViewById(R.id.arrivalPicker);
        departurePicker = findViewById(R.id.departurePicker);
        datePicker= findViewById(R.id.DatePicker);
        timePicker = findViewById(R.id.timePicker);
        pricePicker = findViewById(R.id.pricePicker);
        descriptionPicker = findViewById(R.id.description);
        placesPicker = findViewById(R.id.maxPlaces);

        pickDeparture();
        pickArrival();
        pickDate();
        pickTime();
    }

    private void pickTime() {
        timePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddRide.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker time, int selectedHour, int selectedMinute) {
                        timePicker.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    private void pickDeparture() {
        departurePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlacePickerActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void pickArrival() {
        arrivalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlacePickerActivity.class);
                startActivityForResult(intent,2);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
           String place = data.getStringExtra("selected_place");
           if(place!="")
                switch(requestCode){
                    case 1:
                        departurePicker.setText(place);
                        break;
                    case 2:
                        arrivalPicker.setText(place);
                        break;
                }
        }


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

                datePicker.setText(sdf.format(myCalendar.getTime()));
            }

        };

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddRide.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String driver = LoginActivity.currentUser.getUsername();
        String departure = departurePicker.getText().toString();
        String arrival = arrivalPicker.getText().toString();
        String price = pricePicker.getText().toString();
        String time = timePicker.getText().toString();
        String date = datePicker.getText().toString();
        String description = descriptionPicker.getText().toString();
        String places = placesPicker.getText().toString();

        if(!(departure.equals("")&& arrival.equals("")&&price.equals("")&&time.equals("")&&date.equals(""))){
            Map<String, Object> data = new HashMap<>();
            data.put("mDriver", driver);
            data.put("mDeparture", departure);
            data.put("mArrival", arrival);
            data.put("mPrice", price);
            data.put("mTime", time);
            data.put("mDate", date);
            data.put("mDescription", description);
            data.put("maxPlaces", places);
            data.put("driverUID",FirebaseAuth.getInstance().getUid());
            database.collection("offers").add(data);
            //Toast.makeText(getActivity(), driver, Toast.LENGTH_SHORT).show();
            //TODO add current place picker
            OffersFragment.VoyageList.add(new Voyage(driver,departure,arrival,price,time,date,description,"4","0"));
            OffersFragment.adapter.notifyDataSetChanged();
            finish();
        }
        else{
            Toast.makeText(this, "Data missing", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
