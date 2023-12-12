package com.example.rhythmix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.datastore.generated.model.Playlist;

import java.util.UUID;
public class CreatePlaylistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        final EditText playlistNameEditText = findViewById(R.id.playlistNameEditText);
        Button createPlaylistButton = findViewById(R.id.createPlaylistButton);

        createPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUser authUser = Amplify.Auth.getCurrentUser();

                if (authUser != null) {
                    String userId = authUser.getUserId();
                    String playlistName = playlistNameEditText.getText().toString();

                    Log.i("Amplify", "User ID: " + userId);
                    Log.i("Amplify", "Playlist Name: " + playlistName);

                    if (playlistName != null && !playlistName.isEmpty()) {
                        Playlist.Builder playlistBuilder = (Playlist.Builder) Playlist.builder()
                                .playlistId(UUID.randomUUID().toString())
                                .playlistName(playlistName)
                                .userID(userId);

                        Log.i("Amplify", "Playlist Builder: " + playlistBuilder);

                        Playlist playlist = playlistBuilder.build();

                        Log.i("Amplify", "Playlist: " + playlist);

                        if (playlist != null) {
                            Amplify.DataStore.save(playlist,
                                    success -> {
                                        Log.i("Amplify", "Saved item: " + success.item().getPlaylistName() +
                                                ", Playlist ID: " + success.item().getPlaylistId());
                                        finish();
                                    },
                                    error -> Log.e("Amplify", "Error saving item", error)
                            );
                        } else {
                            Log.e("Amplify", "Playlist builder returned null");
                        }
                    } else {
                        Log.e("Amplify", "Playlist name is null or empty");
                    }
                } else {
                    Log.e("Amplify", "User not authenticated");
                }
            }
        });
    }
}
