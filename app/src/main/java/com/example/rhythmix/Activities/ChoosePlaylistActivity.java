package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.PlaylistMusic;
import com.example.rhythmix.Adapter.ChoosePlaylistAdapter;
import com.example.rhythmix.R;
import com.example.rhythmix.models.Track;
import com.amplifyframework.datastore.generated.model.Music;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChoosePlaylistActivity extends AppCompatActivity implements ChoosePlaylistAdapter.OnPlaylistClickListener{
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

        //>>>>>>>>>>>>>>>>Handel BackCase<<<<<<<<<<<<<<<<<<<

        ImageButton backToMain =findViewById(R.id.backButton);
        backToMain.setOnClickListener(view -> {
            Intent backToMainG =new Intent(this,MainActivity.class);
            startActivity(backToMainG);
        });

        //>>>>>>>>>>>>>>>>>>>>>>>CALLING METHODS<<<<<<<<<<<<<<<<<<<<<<<<<
        amplifier();
        setUpPlayListRecyclerView();
        getTrackIntent = getIntent();
        Log.i("TAG","PlaylistId is : "+getPlaylistId);
    }
    private void onPlaylistSelected(String playlistId) {
        getPlaylistId = playlistId;
    }
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
    @Override
    public void onPlaylistClick(String playlistId) {
        Log.i(TAG, "Selected Playlist Id: " + playlistId);
        onPlaylistSelected(playlistId);
        Track selectedTrack = (Track) getTrackIntent.getSerializableExtra("SELECTED_TRACK");
//        addToPlaylistAndAmplify(selectedTrack);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>Create a track on database<<<<<<<<<<<<<<<<<<<<<<<<

    private void setUpPlayListRecyclerView() {
        RecyclerView playlistRecyclerView = findViewById(R.id.playlistsRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        playlistRecyclerView.setLayoutManager(layoutManager);
        choosePlaylistAdapter = new ChoosePlaylistAdapter(playlists, this, this);
        playlistRecyclerView.setAdapter(choosePlaylistAdapter);

    }
}