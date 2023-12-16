package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.example.rhythmix.R;
import com.example.rhythmix.adapter.ChoosePlaylistAdapter;
import com.example.rhythmix.adapter.playlistRecyclerViewAdapter;
import com.example.rhythmix.models.Track;
import com.amplifyframework.datastore.generated.model.Music;
import com.amplifyframework.datastore.generated.model.User;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoosePlaylistActivity extends AppCompatActivity implements ChoosePlaylistAdapter.OnPlaylistClickListener {
    private ChoosePlaylistAdapter choosePlaylistAdapter;
    List<Playlist> playlists = new ArrayList<>();
    public static final String TAG = "playlistTag";
    String getPlaylistId;
    Intent getTrackIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_playlist);


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
        getTrackIntent = getIntent();
        amplifier();
        setUpPlayListRecyclerView();
        if (getTrackIntent.hasExtra("SELECTED_TRACK")) {
            Track selectedTrack = (Track) getTrackIntent.getSerializableExtra("SELECTED_TRACK");
            addToPlaylist(selectedTrack);
        }
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
                        choosePlaylistAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "failed to read tasks")
        );
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>Create a track on database<<<<<<<<<<<<<<<<<<<<<<<<
    private void addToPlaylist(Track selectedTrack) {
        String trackTitle = getTrackIntent.getStringExtra("TRACK_TITLE");
        String trackArtist= getTrackIntent.getStringExtra("TRACK_ARTIST");
        String trackMp3 = getTrackIntent.getStringExtra("TRACK_MP3");
        String trackCover = getTrackIntent.getStringExtra("TrackCover");
        buildUserAndAddToPlaylist( trackTitle, trackArtist, trackMp3, trackCover);

    }
    private void buildUserAndAddToPlaylist( String trackTitle, String trackArtist, String trackMp3, String cover) {
        Music music=Music.builder()
                .musicTitle(trackTitle)
                .musicArtist(trackArtist)
                .musicCover(cover)
                .musicMp3(trackMp3)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(music),
                successResponse -> Log.i(TAG, "Saved item for playlist with name: " + successResponse.getData()),
                failureResponse -> Log.e(TAG, "Error saving item", failureResponse)
        );
    }

    private void setUpPlayListRecyclerView()
    {
        RecyclerView playlistRecyclerView = (RecyclerView) findViewById(R.id.playlistsRecycleView);
        int numberOfColumns = 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        playlistRecyclerView.setLayoutManager(layoutManager);

        //>>>>>>>>>Static Playlist<<<<<<<<<<<<<<

        choosePlaylistAdapter=new ChoosePlaylistAdapter(playlists,this,this);
        playlistRecyclerView.setAdapter(choosePlaylistAdapter);
    }

    @Override
    public void onPlaylistClick(String playlistId) {
        Intent goToInsidePlaylist = new Intent(this, InsidePlaylistActivity.class);
        goToInsidePlaylist.putExtra("playlistId", playlistId);
        Log.i(TAG, "The Playlist Id is :"+playlistId);
        getPlaylistId=playlistId;
        startActivity(goToInsidePlaylist);
    }
}