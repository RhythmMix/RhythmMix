package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.Adapter.AlbumTracksAdapter;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumTracksActivity extends AppCompatActivity {
//    private static Retrofit retrofit;
//    private static final String TAG = "AlbumTracksActivity";
//    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/";
//    private AlbumTracksAdapter albumTracksAdapter;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_album_tracks);
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        long albumId = getIntent().getLongExtra("Album ID", 393851337L);
//        String albumCoverUrl = getIntent().getStringExtra("Album Cover");
//        String albumTitle = getIntent().getStringExtra("Album Title");
//
//        displayAlbumTracks(albumId);
//    }
//
//        private void displayAlbumTracks(long albumId) {
//            MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
//            Call<Album> retrofitData = musicApiInterface.getAlbumTracks(albumId);
//            Log.d(TAG, "retrofitData of albums: " + retrofitData);
//            Log.d(TAG, "Request URL of albums: " + retrofitData.request().url());
//
//            retrofitData.enqueue(new Callback<Album>() {
//                @Override
//                public void onResponse(Call<Album> call, Response<Album> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Album album = response.body();
//                        Log.d(TAG, "Album details: " + album);
//                        Log.d(TAG, "imageUrl=" + album.getCover());
//                        Log.d(TAG, "Album Id=" + album.getId());
////                        List<Music> albumTracks = album.getTracks();
//
//                        if (album != null && album.getCover() != null && album.getTitle() != null) {
//                            List<Music> albumTracks = new ArrayList<>();
//                            if (!albumTracks.isEmpty()) {
//                            RecyclerView albumTracksRecyclerView = findViewById(R.id.verticalRecyclerView);
//
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AlbumTracksActivity.this);
//                                albumTracksRecyclerView.setLayoutManager(layoutManager);
//
//                                albumTracksAdapter = new AlbumTracksAdapter(albumTracks);
//                                albumTracksRecyclerView.setAdapter(albumTracksAdapter);
//
//
//                                Log.d(TAG, "album after is being added" + album);
//                            } else {
//                                Log.e(TAG, "Album is null");
//                            }
//                        } else {
//                            Log.e(TAG, "Unsuccessful response");
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Album> call, Throwable t) {
//                    Log.e(TAG, "OnFailure: " + t.getMessage());
//                }
//            });
//        }



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
    }

