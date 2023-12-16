package com.example.rhythmix.models;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.rhythmix.Adapter.AlbumTracksAdapter;
import com.example.rhythmix.MusicApiInterface;
import com.example.rhythmix.R;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumTracksActivity extends AppCompatActivity {
    private static Retrofit retrofit;
    private static final String TAG = "AlbumTracksActivity";
    private static final String BASE_URL = "https://api.deezer.com/";
    private AlbumTracksAdapter albumTracksAdapter;
    private  Activity context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_tracks);
        context=this;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        long albumId = getIntent().getLongExtra("Album ID", 393851337L);
         String albumCoverUrl = getIntent().getStringExtra("Album Cover");
         String albumTitle = getIntent().getStringExtra("Album Title");

        displayAlbumTracks(albumId);
    }

    private void displayAlbumTracks(long albumId) {
        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Data> retrofitData = musicApiInterface.getAlbumTracks(albumId);
        Log.d(TAG, "retrofitData of album tracks: " + retrofitData);
        Log.d(TAG, "Request URL of album tracks: " + retrofitData.request().url());

        retrofitData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d(TAG, "response in album tracks activity" + response);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "response body in album tracks activity" + response.body());
                    List<Music> albumTracks = response.body().getData();
                    Log.d(TAG, "music details from tracks activity: " + albumTracks);

                    String albumCoverUrl = getIntent().getStringExtra("Album Cover");
                    String albumTitle = getIntent().getStringExtra("Album title");
                    ArrayList<String> albumTracksString = getIntent().getStringArrayListExtra("Album tracks");
                    long albumId = getIntent().getLongExtra("Album ID", 393851337L);


                    Log.d(TAG, "album cover url from album tracks activity " + albumCoverUrl);
                    Log.d(TAG, "album title from album tracks activity " + albumTitle);


//                    ImageView albumCoverImageView = findViewById(R.id.musicImage);
//                    TextView albumTitleTextView = findViewById(R.id.musicTitle);
//                    ImageButton toggleButton = findViewById(R.id.toggleButton);
//                    RecyclerView albumTracksRecyclerView = findViewById(R.id.verticalRecyclerView);
//
//                        MediaPlayer mediaPlayer = new MediaPlayer();
//                    Picasso.get().load(albumCoverUrl).into(albumCoverImageView);
//                    albumTitleTextView.setText(albumTitle);
//
//                    try {
//                        mediaPlayer.setDataSource(context, Uri.parse(albumTracks.get(0).getPreview()));
//                        mediaPlayer.prepareAsync();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    Picasso.get().load(albumCoverUrl).into(albumCoverImageView);
//                    albumTitleTextView.setText(albumTitle);
//
//
//                    toggleButton.setOnClickListener(v -> {
//                        if (mediaPlayer.isPlaying()) {
//                            mediaPlayer.pause();
//                            toggleButton.setImageResource(android.R.drawable.ic_media_play);
//                        } else {
//                            mediaPlayer.start();
//                            toggleButton.setImageResource(android.R.drawable.ic_media_pause);
//                        }
//                    });
//                    if (!albumTracks.isEmpty()) {
//
//                        AlbumTracksAdapter albumTracksAdapter = new AlbumTracksAdapter(albumTracks, AlbumTracksActivity.this);
//                        RecyclerView recyclerView = findViewById(R.id.verticalRecyclerView);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AlbumTracksActivity.this, LinearLayoutManager.VERTICAL, false);
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(albumTracksAdapter);
//
//
//                        Log.d(TAG, "album after is being added" + albumTracks);
//                    } else {
//                        Log.e(TAG, "Album is null");
//                    }
                    ListView listView = findViewById(R.id.listView); ////// changed into the list view and it worked fine ////////
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AlbumTracksActivity.this, android.R.layout.simple_list_item_1);

                    for (Music track : albumTracks) {
                        adapter.add("Track Title: " + track.getTitle() + "\n"
                                + "Preview Link: " + track.getPreview() + "\n\n");
                    }

                    listView.setAdapter(adapter);

                } else {
                    Log.e(TAG, "Unsuccessful response");
                }
            }


            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e(TAG, "OnFailure: " + t.getMessage());
            }
        });
    }
}




//        // Get data passed from MainActivity
//        String albumCoverUrl = getIntent().getStringExtra("Album Cover");
//        String albumTitle = getIntent().getStringExtra("Album Title");
//        ArrayList<String> trackList = getIntent().getStringArrayListExtra("Album Tracks");
//
//        // Set up UI components
//        ImageView albumCoverImageView = findViewById(R.id.albumCoverImageView);
//        TextView albumTitleTextView = findViewById(R.id.albumTitleTextView);
//        ListView tracksListView = findViewById(R.id.tracksListView);
//
//        // Load album cover using Picasso
//        Picasso.get().load(albumCoverUrl).into(albumCoverImageView);
//
//        // Set album title
//        albumTitleTextView.setText(albumTitle);

        // Set up ListView for tracks
//        AlbumTracksAdapter tracksAdapter = new AlbumTracksAdapter(this, trackList);
//        tracksListView.setAdapter(tracksAdapter);


