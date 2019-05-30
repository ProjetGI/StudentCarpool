
package com.gi2.servicedecovoiturage;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView logo, joinus;
    private AutoCompleteTextView username, email, password,phonenumber,CNE;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Firebase.setAndroidContext(this);

        initializeGUI();

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputName = username.getText().toString().trim();
                final String inputPw = password.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();
                final String inputPhoneNumber = phonenumber.getText().toString().trim();
                final String inputCNE = CNE.getText().toString().trim();

                if(validateInput(inputName, inputPw, inputEmail))
                         registerUser(inputName, inputPw, inputEmail,inputPhoneNumber,inputCNE);
                         Toast.makeText(RegistrationActivity.this,"Successfully registered",Toast.LENGTH_SHORT).show();

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

    }


    private void initializeGUI(){

        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        username = findViewById(R.id.atvUsernameReg);
        email =  findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        phonenumber = findViewById(R.id.atvPhonenumberReg);
        CNE = findViewById(R.id.atvCNEReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(final String inputName, final String inputPw, final String inputEmail, final String inputPhoneNumber, final String inputCNE) {

        progressDialog.setMessage("Verificating...");
        progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserData(inputName,inputPw,inputEmail);
                        addFireStoreUser(inputName,inputEmail,inputPw,inputPhoneNumber,inputCNE,false);
                        Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"Email already exists.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


    private void sendUserData(String username, String password, String email){

        //AN OTHER ONE
        Firebase reference = new Firebase("https://service-de-covoiturage.firebaseio.com/users");
        reference.child(username).child("password").setValue(password);

    }

    private boolean validateInput(String inName, String inPw, String inEmail){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            return false;
        }
        if(inPw.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }
        if(inEmail.isEmpty()){
            email.setError("Email is empty.");
            return false;
        }

        return true;
    }

    private void addFireStoreUser(String name, String email,String password,String phoneNumber, String CNE,Boolean admin){

        //get current logged in user IUD
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userIUD = currentFirebaseUser.getUid();

        System.out.println("user ID is : "+userIUD);

        //Add user data
        Map<String, Object> data = new HashMap<>();
        data.put("username", name);
        data.put("email", email);
        data.put("password",password);
        data.put("phone-number",phoneNumber);
        data.put("CNE",CNE);
        data.put("admin",admin);
        data.put("userid",userIUD);
        data.put("pending",true);
        FirebaseFirestore.getInstance().collection("users").document(userIUD).set(data);
        System.out.println("NEW USER ADDED!");
    }


}
