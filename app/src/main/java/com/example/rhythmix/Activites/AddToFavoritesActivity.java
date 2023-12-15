package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.R;
import com.example.rhythmix.adapter.DataListAdapter;
import com.example.rhythmix.models.Music;

import java.util.ArrayList;
import java.util.List;

public class AddToFavoritesActivity extends AppCompatActivity {
    public static final String ADD_TO_FAVORITES_ACTIVITY_TAG = "AddToFavoritesActivity";
    private List<Music> favoriteTracksList = new ArrayList<>();
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
        addToFavorites(trackId, trackTitle, trackArtist, trackMp3);
    }

    private void addToFavorites(long trackId, String trackTitle, String trackArtist, String trackMp3) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            final String[] userEmail = {""};
            Amplify.Auth.fetchUserAttributes(
                    attributes -> {
                        for (AuthUserAttribute attribute : attributes) {
                            if ("email".equals(attribute.getKey().getKeyString())) {
                                userEmail[0] = attribute.getValue();
                            }
                        }
                        buildUserAndAddToFavorites(authUser, userEmail[0], trackId, trackTitle, trackArtist, trackMp3);

                        // Updating the adapter with the new favorite track
                        Music favoriteTrack = favoriteTracksList.get(0); // Create a Music object with the necessary details
                        favoriteTracksList.add(favoriteTrack);
                        favoritesAdapter.notifyDataSetChanged();
                    },
                    error -> Log.e(ADD_TO_FAVORITES_ACTIVITY_TAG, "Error fetching user attributes", error)
            );
        } else {
            Log.e(ADD_TO_FAVORITES_ACTIVITY_TAG, "User not authenticated");
        }
    }

    private void buildUserAndAddToFavorites(AuthUser authUser, String userEmail, long trackId, String trackTitle, String trackArtist, String trackMp3) {
        User user = User.builder()
                .email(userEmail)
                .id(authUser.getUserId())
                .username(authUser.getUsername())
                .userImageS3Key("")
                .build();

//        ImageButton addToFavoritesButton = findViewById(R.id.addToFavorite);
        Favorite favorite = Favorite.builder()
                .favoriteId(String.valueOf(trackId))
                .favoriteTitle(trackTitle)
                .favoriteArtist(trackMp3)
                .favoriteMp3(trackArtist)
                .userId(authUser.getUserId())
                .build();

        Amplify.API.mutate(
                ModelMutation.create(favorite),
                successResponse -> Log.i(ADD_TO_FAVORITES_ACTIVITY_TAG, "Saved item for playlist with name: " + successResponse.getData()),
                failureResponse -> Log.e(ADD_TO_FAVORITES_ACTIVITY_TAG, "Error saving item", failureResponse)
                );
}
}