package com.gi2.servicedecovoiturage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn, btnTwitter;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    static UserProfile currentUser;
    private FirebaseFirestore database=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeGUI();

        user = firebaseAuth.getCurrentUser();

        if(user != null) {
            initCurrentUserAndLaunchActivity();
        }



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString();
                String inPassword = password.getText().toString();

                if(validateInput(inEmail, inPassword)){
                    signUser(inEmail, inPassword);
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,PWresetActivity.class));
            }
        });



    }



    public void signUser(String email, String password){

        progressDialog.setMessage("Verificating...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    initCurrentUserAndLaunchActivity();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initializeGUI(){

        logo = findViewById(R.id.ivLogLogo);
        ivSignIn = findViewById(R.id.ivSignIn);
        btnTwitter = findViewById(R.id.ivFacebook);
        btnTwitter.setVisibility(View.GONE);
        email = findViewById(R.id.atvEmailLog);
        password = findViewById(R.id.atvPasswordLog);
        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }


    public boolean validateInput(String inemail, String inpassword){

        if(inemail.isEmpty()){
            email.setError("Email field is empty.");
            return false;
        }
        if(inpassword.isEmpty()){
            password.setError("Password is empty.");

        }

        return true;
    }


    private void initCurrentUserAndLaunchActivity(){


        currentUser = new UserProfile("test","test","test","test","test",false,"test");

        final String currentDriverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference currentDriverInfo = database.collection("users").document(currentDriverUID);

        progressDialog.setMessage("Verificating...");
        progressDialog.show();

        currentDriverInfo.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            currentUser.setUsername(documentSnapshot.getString("username"));
                            currentUser.setEmail(documentSnapshot.getString("email"));
                            currentUser.setPhoneNumber(documentSnapshot.getString("phone-number"));
                            currentUser.setCNE(documentSnapshot.getString("CNE"));
                            currentUser.setAdmin(documentSnapshot.getBoolean("admin"));
                            currentUser.setPending(documentSnapshot.getBoolean("pending"));
                            currentUser.setUserid(currentDriverUID);


                            if(!currentUser.getPending()) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                if (currentUser.isAdmin()) {
                                    Toast.makeText(LoginActivity.this, "Welcome administrator!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, adminBar.class));
                                }
                                else
                                {
                                    startActivity(new Intent(LoginActivity.this, RideOld.class));
                                }
                            }

                            else Toast.makeText(LoginActivity.this, "Sorry you are not yet approved by the admin", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }

                        else {
                            Toast.makeText(LoginActivity.this , "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
