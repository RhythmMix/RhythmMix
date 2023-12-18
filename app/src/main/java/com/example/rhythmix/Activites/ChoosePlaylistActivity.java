package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.PlaylistMusic;
import com.example.rhythmix.R;
import com.example.rhythmix.adapter.ChoosePlaylistAdapter;
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



//    public void addToPlaylistAndAmplify(Track selectedTrack) {
//        String trackTitle = getTrackIntent.getStringExtra("TRACK_TITLE");
//        String trackArtist = getTrackIntent.getStringExtra("TRACK_ARTIST");
//        String trackMp3 = getTrackIntent.getStringExtra("TRACK_MP3");
//        String trackCover = getTrackIntent.getStringExtra("TrackCover");
//
//        Music music = Music.builder()
//                .musicTitle(trackTitle)
//                .musicArtist(trackArtist)
//                .musicCover(trackCover)
//                .musicMp3(trackMp3)
//                .build();
//
//        if (getPlaylistId != null) {
//            PlaylistMusic playlistMusic = PlaylistMusic.builder()
//                    .playlist(Playlist.justId(getPlaylistId))
//                    .track(music)
//                    .build();
//
//            Amplify.API.mutate(
//                    ModelMutation.create(playlistMusic),
//                    successResponse -> {
//                        Log.i(TAG, "Saved PlaylistMusic item: " + successResponse.getData());
//                        saveMusicItem(music);
//                    },
//                    failureResponse -> Log.e(TAG, "Error saving PlaylistMusic item", failureResponse)
//            );
//            runOnUiThread(() -> {
//                choosePlaylistAdapter.notifyDataSetChanged();
//            });
//        } else {
//            Log.e(TAG, "No playlist ID available");
//        }
//    }
//    private void saveMusicItem(Music music) {
//        Amplify.API.mutate(
//                ModelMutation.create(music),
//                successResponse -> Log.i(TAG, "Saved Music item: " + successResponse.getData()),
//                failureResponse -> Log.e(TAG, "Error saving Music item", failureResponse)
//        );
//    }
    private void setUpPlayListRecyclerView() {
        RecyclerView playlistRecyclerView = findViewById(R.id.playlistsRecycleView);
        int numberOfColumns = 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        playlistRecyclerView.setLayoutManager(layoutManager);
        choosePlaylistAdapter = new ChoosePlaylistAdapter(playlists, this, this);
        playlistRecyclerView.setAdapter(choosePlaylistAdapter);

    }
}