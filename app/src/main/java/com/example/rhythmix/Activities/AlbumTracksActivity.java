package com.example.rhythmix.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.Adapter.AlbumTracksAdapter;
import com.example.rhythmix.MusicApiInterface;
import com.example.rhythmix.R;
import com.example.rhythmix.models.APIConfig;
import com.example.rhythmix.models.Data;
import com.example.rhythmix.models.Track;
import com.squareup.picasso.Picasso;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_tracks);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        long albumId = getIntent().getLongExtra("Album ID", 393851337L);

        String activityTitle= getIntent().getStringExtra("Album title");
        TextView title=findViewById(R.id.activityTitle);
        title.setText(activityTitle);

        ImageView cover=findViewById(R.id.activityTitleCover);
        String activityTitleImage= getIntent().getStringExtra("Album Cover");
        Picasso.get().load(activityTitleImage).into(cover);

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
                    List<Track> albumTracks = response.body().getData();
                    Log.d(TAG, "music details from tracks activity: " + albumTracks);

                    if (!albumTracks.isEmpty()) {
                        // Create the adapter and set it to the RecyclerView
                        albumTracksAdapter = new AlbumTracksAdapter(albumTracks);
                        RecyclerView recyclerView = findViewById(R.id.albumRecyclerView);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AlbumTracksActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(albumTracksAdapter);
                    } else {
                        Log.e(TAG, "Album is null");
                    }
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