package com.example.rhythmix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(ProfileActivity.this, LibraryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                return true;
            } else return false;
        });
        // Set the default item to be the "Search" item
        bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(true);
    }
}