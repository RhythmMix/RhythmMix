package com.example.rhythmix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.User;

import java.util.UUID;
public class CreatePlaylistActivity extends AppCompatActivity {

    public static final String CREATE_PLAY_LIST_ACTIVITY_TAG = "CreatePlaylistActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        Button createPlaylistButton = findViewById(R.id.createPlaylistButton);
        createPlaylistButton.setOnClickListener(v -> createPlaylist());
    }

    private void createPlaylist() {
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
                        buildUserAndPlaylist(authUser, userEmail[0]);
                    },
                    error -> Log.e(CREATE_PLAY_LIST_ACTIVITY_TAG, "Error fetching user attributes", error)
            );
        } else {
            Log.e(CREATE_PLAY_LIST_ACTIVITY_TAG, "User not authenticated");
        }
    }

    private void buildUserAndPlaylist(AuthUser authUser, String userEmail) {
        EditText playlistNameEditText = findViewById(R.id.playlistNameEditText);

        User user = User.builder()
                .email(userEmail)
                .id(authUser.getUserId())
                .username(authUser.getUsername())
                .userImageS3Key("")
                .build();


        String playlistName = playlistNameEditText.getText().toString();
        Log.i(CREATE_PLAY_LIST_ACTIVITY_TAG, "Playlist Name: " + playlistName);


        Playlist playlist = Playlist.builder()
                .playlistName(playlistName)
                .user(user)
                .playlistBackground("")
                .build();


        Amplify.API.mutate(
                ModelMutation.create(playlist),
                successResponse -> Log.i(CREATE_PLAY_LIST_ACTIVITY_TAG, "Saved item for playList with name: "+successResponse.getData().getPlaylistName()),
                failureResponse -> Log.e(CREATE_PLAY_LIST_ACTIVITY_TAG, "Error saving item", failureResponse)
        );
    }
}