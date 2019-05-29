package com.gi2.servicedecovoiturage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.grpc.Context;

public class Users extends AppCompatActivity {
    ListView usersList;
    //ArrayList<String> al = new ArrayList<>();
    ArrayList<User> UsersList = new ArrayList<User>();
    int totalUsers = 0;
    ProgressDialog pd;
    ImageView imageView;
    private UserAdapter adapter;
    private FirebaseFirestore database=FirebaseFirestore.getInstance();
    Interaction activeInteraction;
    ArrayList<String> clients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        usersList = (ListView)findViewById(R.id.usersList);
        //imageView = (ImageView) findViewById(R.id.imgView);

        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://service-de-covoiturage.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = UsersList.get(position).getmUsername();
                startActivity(new Intent(getApplicationContext(), Chat.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            final Iterator i = obj.keys();


            Bundle b = getIntent().getExtras();
            clients = null;
            if(b != null)
                clients = b.getStringArrayList("clients");



            while(i.hasNext()){
                final String key = i.next().toString();

                if(!key.equals(UserDetails.username)) {

                    User user = new User(key);
                    if(clients!=null)
                    {
                        if(clients.contains(user.getmUsername()))
                            UsersList.add(user);
                    }
                    else
                        UsersList.add(user);

                }
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //usersList.setVisibility(View.VISIBLE);
        adapter = new UserAdapter(getApplicationContext(), UsersList);
        usersList.setAdapter(adapter);

        pd.dismiss();
        //imageView.setVisibility(View.GONE);


    }
}