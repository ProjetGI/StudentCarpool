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

import com.gi2.servicedecovoiturage.logregform.R;

public class RideOld extends AppCompatActivity {

    private ActionBar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_old);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        toolBar = getSupportActionBar();
        toolBar.setTitle("Offers");


        loadFragment(new OffersFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.nav_offers:
                                fragment = new OffersFragment();
                                loadFragment(fragment);
                                toolBar.setTitle("Offers");
                                return true;
                            case R.id.nav_demand:
                                fragment = new DemandsFragment();
                                loadFragment(fragment);
                                toolBar.setTitle("Demands");
                                return true;
                            case R.id.nav_notification:
                                fragment = new NotificationFragment();
                                loadFragment(fragment);
                                toolBar.setTitle("Notifications");
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
        transaction.replace(R.id.container, fragment);
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
        startActivity(new Intent(RideOld.this, Users.class));

        return super.onOptionsItemSelected(item);
    }
}


