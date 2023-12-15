package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Search) {
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(SearchActivity.this, LibraryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                return true;
            } else return false;
        });
        // Set the default item to be the "Search" item
        bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(true);


        Button gotoplaylist=findViewById(R.id.goToplaylist);
        gotoplaylist.setOnClickListener(view -> {
            Intent intent=new Intent(SearchActivity.this,PlaylistsActivity.class);
            startActivity(intent);
        });

}
    }
