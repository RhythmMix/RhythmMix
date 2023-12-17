package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.example.rhythmix.Adapter.PlaylistRecyclerViewAdapter;
import com.example.rhythmix.R;

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



        //>>>>>>>>>>>>>>>>>>>>>>>CALLING METHODS<<<<<<<<<<<<<<<<<<<<<<<<<
        amplifier();
        setUpPlayListRecyclerView();


        //////////////////////>>Click here to create a playlist<<<<

        ImageView addListImageView = findViewById(R.id.add_list);
        addListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistsActivity.this, CreatePlaylistActivity.class);
                startActivity(intent);
            }
        });
        // Songs/Playlist Navbar
        RadioGroup navigationBar = findViewById(R.id.navigationBarPlaylist);
        navigationBar.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.playlistsButton) {
                RecyclerView playlistsRecyclerView = findViewById(R.id.playlistsRecycleView);
                playlistsRecyclerView.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.songsButton) {
//                startActivity(new Intent(PlaylistsActivity.this, LibraryActivity.class));
            }
        });
        // Set the default selection to "Playlists"
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


}
