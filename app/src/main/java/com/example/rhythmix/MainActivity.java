package com.example.rhythmix;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.rhythmix.Adapter.DataListAdapter;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/";
    private static Retrofit retrofit;
    DataListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Data> retrofitData = musicApiInterface.getData("eminem");
        Log.d(TAG, "Request URL: " + retrofitData.request().url());

               retrofitData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Music> musicList = response.body().getData();

                    if (!musicList.isEmpty()) {
                        StringBuilder displayText = new StringBuilder();

                        for (Music music : musicList) {
                            displayText.append("Title: ").append(music.getTitle()).append("\n")
                                    .append("Artist: ").append(music.getArtist().getName()).append("\n")
                                    .append("Album: ").append(music.getAlbum().getTitle()).append("\n\n")
                                    .append("Track: ").append(music.getPreview()).append("\n");
                        }

                        RecyclerView musicListRecyclerView = findViewById(R.id.recyclerView);

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        musicListRecyclerView.setLayoutManager(layoutManager);

                        adapter = new DataListAdapter(MainActivity.this, musicList);

                        musicListRecyclerView.setAdapter(adapter);

                        Log.d(TAG, "OnSuccess:" + response.body());
                    } else {
                        Log.e(TAG, "Empty music list");
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