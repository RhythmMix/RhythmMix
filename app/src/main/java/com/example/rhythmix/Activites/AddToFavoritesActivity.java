package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.User;
import com.bumptech.glide.Glide;
import com.example.rhythmix.R;
import com.example.rhythmix.adapter.DataListAdapter;
import com.example.rhythmix.models.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddToFavoritesActivity extends AppCompatActivity {
    public static final String ADD_TO_FAVORITES_ACTIVITY_TAG = "AddToFavoritesActivity";
    private CompletableFuture<Favorite> favoriteTracksList = null;
    private CompletableFuture<List<Favorite>> favoritesAdapter = null;
    private EditText trackTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_favorites);

        // Initializing RecyclerView
//        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        favoritesRecyclerView.setLayoutManager(layoutManager);
//
//        favoritesAdapter = new DataListAdapter(this, favoriteTracksList);
//        favoritesRecyclerView.setAdapter(favoritesAdapter);


        favoriteTracksList = new CompletableFuture<>();
        favoritesAdapter = new CompletableFuture<>();

        trackTitle = findViewById(R.id.playlistNameEditText);


//        Button createPlaylistButton = findViewById(R.id.createPlaylistButton);
//        createPlaylistButton.setOnClickListener(v ->
//                        createPlaylist()  );
//
        // Retrieving track details from Intent
        long trackId = getIntent().getLongExtra("TRACK_ID", 0);
        String trackTitle = getIntent().getStringExtra("TRACK_TITLE");
        String trackArtist = getIntent().getStringExtra("TRACK_ARTIST");
        String trackMp3 = getIntent().getStringExtra("TRACK_MP3");
        String trackImage = getIntent().getStringExtra("TRACK_COVER");

        // Adding the track to favorites and updating the RecyclerView
//        addToFavorites(trackId, trackTitle, trackArtist, trackMp3);


        TextView playlistTitle = findViewById(R.id.title);
        playlistTitle.setText(trackArtist);
        ImageView playlistImage = findViewById(R.id.art);

        Glide.with(this)
                .load(trackImage)
                .error(R.drawable.rhythemix)
                .into(playlistImage);
    }



//    private void queryFavoriteTracks() {
//        Amplify.API.query(
//                ModelQuery.list(Favorite.class),
//                success -> {
//                    Log.i(ADD_TO_FAVORITES_ACTIVITY_TAG, "Read Favorite Tracks successfully");
//                    favoriteTracksList.clear();
//                    for (Favorite databaseTracks : success.getData()) {
//                        // Check if the task belongs to the selected team
//                        if (databaseTracks.getFavoriteMp3() != null && databaseTracks.getFavoriteArtist() != null && databaseTracks.getFavoriteTitle() != null) {
//                            favoriteTracksList.add(databaseTracks);
//                        }
//                    }
//                    favoritesAdapter.notifyDataSetChanged();
//
//                },
//                failure -> Log.i(ADD_TO_FAVORITES_ACTIVITY_TAG, "Couldn't read favorite tracks from DynamoDB ")
//        );
//    }
//
//    interface OnCheckResultListener {
//        void onResult(boolean exists);
//    }
}


