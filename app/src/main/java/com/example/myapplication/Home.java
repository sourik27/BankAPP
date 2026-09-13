package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    double receivedTotal;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        receivedTotal = getIntent().getDoubleExtra("RESULT", 100_000.00);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (savedInstanceState == null) {
            showFragment(HomeFragment.newInstance(receivedTotal, userEmail));
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                showFragment(HomeFragment.newInstance(receivedTotal, userEmail));
                return true;
            } else if (id == R.id.History) {
                showFragment(new TransferHistoryFragment());
                return true;
            } else if (id == R.id.service) {
                showFragment(new ServiceFragment());
                return true;
            }
            return false;
        });
    }

    private void showFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}