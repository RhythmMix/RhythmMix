package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.example.rhythmix.Adapter.PlaylistRecyclerViewAdapter;
import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsActivity extends AppCompatActivity {
    private PlaylistRecyclerViewAdapter playlistRecyclerViewAdapter;
    List<Playlist> playlists = new ArrayList<>();
    public static final String TAG = "playlistTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        String emptyFileName="emptyTestFile";
        File emptyFile =new File(getApplicationContext().getFilesDir(),emptyFileName);
        try {
            BufferedWriter emptyFileBufferedWriter= new BufferedWriter(new FileWriter(emptyFile));

            emptyFileBufferedWriter.append("Some text here from Farah\nAnother libe from Farah");

            emptyFileBufferedWriter.close();
        }catch (IOException ioe){
            Log.i(TAG, "could not write locally with filename: "+ emptyFileName);
        }
        String emptyFileS3Key = "someFileOnS3.txt";
        Amplify.Storage.uploadFile(
                emptyFileS3Key,
                emptyFile,
                success ->
                {
                    Log.i(TAG, "S3 upload succeeded and the Key is: " + success.getKey());
                },
                failure ->
                {
                    Log.i(TAG, "S3 upload failed! " + failure.getMessage());
                }
        );

        //==========================================================================

        ImageButton FavoriteImageButton= findViewById(R.id.FavoriteImageButton);
        FavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFavPage= new Intent(PlaylistsActivity.this, AddToFavoritesActivity.class);
                startActivity(goToFavPage);
            }
        });

        //>>>>>>>>>>>>>>>>>>>>>>>CALLING METHODS<<<<<<<<<<<<<<<<<<<<<<<<<
        amplifier();
        setUpPlayListRecyclerView();
        setupBottomNavigationView();

        //////////////////////>>Click here to create a playlist<<<<

        ImageView addListImageView = findViewById(R.id.add_list);
        addListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToPlaylistPopUpActivity popUp = new AddToPlaylistPopUpActivity();
                popUp.show(getSupportFragmentManager(),"exp");
            }
        });

        // Navigation songs/playlists
        RadioGroup navigationBar = findViewById(R.id.navigationBarPlaylist);
        navigationBar.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.playlistsButton) {
                RecyclerView playlistsRecyclerView = findViewById(R.id.playlistsRecycleView);
                playlistsRecyclerView.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.songsButton) {
                startActivity(new Intent(PlaylistsActivity.this, LibraryActivity.class));
            }
        });
        navigationBar.check(R.id.playlistsButton);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>CREATE RECYCLERVIEW<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public void amplifier() {
        Amplify.API.query(
                ModelQuery.list(Playlist.class),
                success -> {
                    Log.i(TAG, "Read tasks successfully");
                    playlists.clear();
                    for (Playlist databaseTask : success.getData()) {
                        ;
                        playlists.add(databaseTask);
                    }
                    runOnUiThread(() -> {
                        playlistRecyclerViewAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "failed to read tasks")
        );
    }
    private void setUpPlayListRecyclerView()
    {
        RecyclerView playlistRecyclerView = (RecyclerView) findViewById(R.id.playlistsRecycleView);
        int numberOfColumns = 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        playlistRecyclerView.setLayoutManager(layoutManager);

        //>>>>>>>>>Static Playlist<<<<<<<<<<<<<<

        playlistRecyclerViewAdapter=new PlaylistRecyclerViewAdapter(playlists,this);
        playlistRecyclerView.setAdapter(playlistRecyclerViewAdapter);
    }
    //==============================
    // Main Navbar
    //==============================
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(PlaylistsActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(PlaylistsActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Library) {
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(PlaylistsActivity.this, ProfileActivity.class));
                ;
                return true;
            } else return false;
        });
        bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(true);
    }

}