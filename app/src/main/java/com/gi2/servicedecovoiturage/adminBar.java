package com.gi2.servicedecovoiturage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class adminBar extends AppCompatActivity {

    private ActionBar toolBar;
    //private FirebaseFirestore database=FirebaseFirestore.getInstance();
    //private FirebaseAuth firebaseAuth;
    //UserProfile user = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_table);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.tabAdm);

        toolBar = getSupportActionBar();
        toolBar.setTitle("Pending users");

        //Toast.makeText(adminBar.this,"id :"+da.getId().toString(),Toast.LENGTH_SHORT).show();

        loadFragment(new PendingUsers());
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.nav_pend:
                                fragment = new PendingUsers();
                                loadFragment(fragment);
                                toolBar.setTitle("Pending users");
                                return true;
                            case R.id.nav_report:
                                fragment = new ReportUsers();
                                loadFragment(fragment);
                                toolBar.setTitle("Reports");
                                return true;

                            case R.id.nav_profile:
                                fragment = new ProfileFragment();
                                loadFragment(fragment);
                                toolBar.setTitle("My Profile");
                                return true;
                        }
                        return false;
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        UserDetails.username=LoginActivity.currentUser.getUsername();
        UserDetails.password=LoginActivity.currentUser.getPassword();
        UserDetails.admin=LoginActivity.currentUser.isAdmin();

        startActivity(new Intent(adminBar.this, Users.class));

        return super.onOptionsItemSelected(item);
    }
}


