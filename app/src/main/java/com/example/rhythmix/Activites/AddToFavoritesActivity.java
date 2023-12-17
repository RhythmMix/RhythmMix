package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.example.rhythmix.R;
import com.example.rhythmix.adapter.FavoritesAdapter;
import com.example.rhythmix.models.FavoritesHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddToFavoritesActivity extends AppCompatActivity {
    private List<Favorite> favorites = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private MediaPlayer mediaPlayer;
    private static final String TAG = "AddToFavoritesActivity";
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_favorites);

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(layoutManager);

        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites, this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        FavoritesHandler favoritesHandler = new FavoritesHandler(favorites, this, favoritesAdapter);
//        initialization();
        favoritesHandler.queryFavorites();

    }

//    private void queryFavorites() {
//        Set<String> uniqueIds = new HashSet<>();
//        Amplify.API.query(
//                ModelQuery.list(Favorite.class),
//                response -> {
//                    runOnUiThread(() -> {
//                        for (Favorite favorite : response.getData()) {
//                            String favoriteId = favorite.getFavoriteId();
//                            if (uniqueIds.add(favoriteId)) {
//                                // This means the ID was not already in the set
//                                favorites.add(favorite);
//                                Log.d(TAG, "Added to favorites: " + favorite);
//                            } else {
//                                Log.d(TAG, "Duplicate track found, not added: " + favoriteId);
//                            }
//                        }
//                        favoritesAdapter.notifyDataSetChanged();
//                    });
//                },
//                error -> Log.e(TAG, "Error querying favorites", error)
//        );
//    }



    private void initialization() {

    String trackTitle = getIntent().getStringExtra("TRACK_TITLE");
    String trackArtist = getIntent().getStringExtra("TRACK_ARTIST");
    String trackMp3 = getIntent().getStringExtra("TRACK_MP3");
    String trackCover = getIntent().getStringExtra("TRACK_COVER");

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites, this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        FavoritesHandler favoritesHandler = new FavoritesHandler(favorites, this, favoritesAdapter);
        favoritesHandler.queryFavorites();



//    TextView trackTitleView = findViewById(R.id.title);
//    trackTitleView.setText(trackTitle);
//
//    TextView description = findViewById(R.id.description);
//    description.setText(trackArtist);
//
//    ImageButton previewBtn = findViewById(R.id.preview_button);
//
//    try {
//        mediaPlayer.setDataSource(this, Uri.parse(trackMp3));
//        mediaPlayer.prepareAsync();
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
//
//    previewBtn.setOnClickListener(v -> {
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//            previewBtn.setImageResource(android.R.drawable.ic_media_play);
//        } else {
//            mediaPlayer.start();
//            previewBtn.setImageResource(android.R.drawable.ic_media_pause);
//        }
//    });
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


