package com.example.rhythmix.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.rhythmix.R;

public class PlaylistsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        // Songs/Playlist Navbar
        RadioGroup navigationBar = findViewById(R.id.navigationBarPlaylist);
        navigationBar.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.playlistsButton) {
                RecyclerView playlistsRecyclerView = findViewById(R.id.playlistsRecycleView);
                playlistsRecyclerView.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.songsButton) {
                startActivity(new Intent(PlaylistsActivity.this, LibraryActivity.class));
            }
        });
        // Set the default selection to "Playlists"
        navigationBar.check(R.id.playlistsButton);
    }
}