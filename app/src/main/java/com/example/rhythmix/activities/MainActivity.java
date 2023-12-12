package com.example.rhythmix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button press=findViewById(R.id.button3);
        press.setOnClickListener(view -> {
            Intent nn=new Intent(MainActivity.this, MainActivity2.class);
            startActivity(nn);
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);

            if (item.getItemId() == R.id.Home) {
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(MainActivity.this, LibraryActivity.class));
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                item.setChecked(true);
                return true;
            } else return false;
        });

        goToLogIn();
    }

    public void goToLogIn(){
        Button logIn = findViewById(R.id.button4);
        logIn.setOnClickListener(view -> {
            Intent goToAllSongs = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToAllSongs);
        });
    }
}

