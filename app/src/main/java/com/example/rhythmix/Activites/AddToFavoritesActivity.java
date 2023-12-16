package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;
import com.example.rhythmix.adapter.DataListAdapter;
import com.example.rhythmix.models.Track;

import java.util.ArrayList;
import java.util.List;

public class AddToFavoritesActivity extends AppCompatActivity {
    public static final String ADD_TO_FAVORITES_ACTIVITY_TAG = "AddToFavoritesActivity";
    private List<Track> favoriteTracksList = new ArrayList<>();
    private DataListAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_favorites);

        // Initializing RecyclerView
        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(layoutManager);

        favoritesAdapter = new DataListAdapter(this, favoriteTracksList);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Retrieving track details from Intent
        long trackId = getIntent().getLongExtra("TRACK_ID", 0);
        String trackTitle = getIntent().getStringExtra("TRACK_TITLE");
        String trackArtist = getIntent().getStringExtra("TRACK_ARTIST");
        String trackMp3 = getIntent().getStringExtra("TRACK_MP3");

        // Adding the track to favorites and updating the RecyclerView
//        addToFavorites(trackId, trackTitle, trackArtist, trackMp3);
    }
}