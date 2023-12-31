package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Music;
import com.amplifyframework.datastore.generated.model.PlaylistMusic;
import com.bumptech.glide.Glide;
import com.example.rhythmix.Adapter.MusicAdapter;
import com.example.rhythmix.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InsidePlaylistActivity extends AppCompatActivity {
    private List<Music> musicList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private String playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_playlist);

        Intent intent = getIntent();
        playlistId = intent.getStringExtra("playlistId");
        String playlistName = intent.getStringExtra("playlistName");
        String playlistBackground = intent.getStringExtra("playlistBackground");
        TextView playlistTitle = findViewById(R.id.title);
        playlistTitle.setText(playlistName);
        ImageView playlistImage = findViewById(R.id.art);
        Glide.with(this)
                .load("https://rhythmmix90bba48f17b9485194f4a1c4ae1c9bc1200138-dev.s3.us-east-2.amazonaws.com/public/" + playlistBackground)
                .error(R.drawable.rhythemix)
                .into(playlistImage);
        Log.i("IDTAG", "MYYYYYY IDDDDD ISSS : " + playlistId);

        // Get The Recycler View
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicAdapter = new MusicAdapter(musicList);
        recyclerView.setAdapter(musicAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (playlistId != null) {
            fetchMusicForPlaylist(playlistId);
        } else {
            Log.e("InsidePlaylistActivity", "No playlistId provided");
        }
    }
    private void fetchMusicForPlaylist(String playlistId) {
        Set<String> uniqueIds = new HashSet<>();
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            String currentUserId = authUser.getUserId();
            Amplify.API.query(
                    ModelQuery.list(PlaylistMusic.class, PlaylistMusic.PLAYLIST.contains(playlistId)),
                    response -> {
                        Log.i("InsidePlaylistActivity", "Successfully retrieved playlist music items");
                        PaginatedResult<PlaylistMusic> playlistMusicList = response.getData();
                        musicList.clear();
                        for (PlaylistMusic playlistMusic : playlistMusicList) {
                            Music music = playlistMusic.getTrack();
                            if (music != null) {
                                musicList.add(music);
                            }
                        }
                        runOnUiThread(() -> musicAdapter.notifyDataSetChanged());
                    },
                    error -> Log.e("InsidePlaylistActivity", "Error fetching playlist music items", error)
            );
        }
    }
}