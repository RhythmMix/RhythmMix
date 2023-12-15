package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rhythmix.R;

public class InsidePlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_playlist);

        Intent intent = getIntent();
        String playlistName = intent.getStringExtra("playlistName");
        String playlistBackground = intent.getStringExtra("playlistBackground");


        TextView playlistTitle =findViewById(R.id.title);
        playlistTitle.setText(playlistName);
        ImageView playlistImage = findViewById(R.id.art);
        Glide.with(this)
                .load("https://rhythmmix90bba48f17b9485194f4a1c4ae1c9bc1200138-dev.s3.us-east-2.amazonaws.com/public/" + playlistBackground)
                .error(R.drawable.rhythemix)
                .into(playlistImage);
    }
}