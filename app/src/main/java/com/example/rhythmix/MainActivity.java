package com.example.rhythmix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmix.activities.AllSongsActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToAllSongsButton();

    }

    public void goToAllSongsButton(){
        Button allSongsButton = findViewById(R.id.goToAllSongsButton);
        allSongsButton.setOnClickListener(view -> {
            Intent goToAllSongs = new Intent(MainActivity.this, AllSongsActivity.class);
            startActivity(goToAllSongs);
        });
    }
}
